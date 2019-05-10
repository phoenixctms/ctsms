package org.phoenixctms.ctsms.executable.csv;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.phoenixctms.ctsms.Search;
import org.phoenixctms.ctsms.SearchParameter;
import org.phoenixctms.ctsms.domain.Permission;
import org.phoenixctms.ctsms.domain.PermissionDao;
import org.phoenixctms.ctsms.domain.ProfilePermissionDao;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter;
import org.phoenixctms.ctsms.util.ChunkedDaoOperationAdapter.PageSizes;
import org.phoenixctms.ctsms.util.ExecUtil;
import org.phoenixctms.ctsms.util.JobOutput;
import org.springframework.beans.factory.annotation.Autowired;

public class CsvExporter {

	@Autowired
	protected PermissionTemplateWriter permissionTemplateWriter;
	@Autowired
	protected PermissionDefinitionWriter permissionDefinitionWriter;
	@Autowired
	protected PermissionDao permissionDao;
	@Autowired
	protected ProfilePermissionDao profilePermissionDao;
	private JobOutput jobOutput;

	public long exportPermissionDefinitions(String fileName, String encoding) throws Exception {
		ChunkedDaoOperationAdapter permissionProcessor = new ChunkedDaoOperationAdapter<PermissionDao, Permission>(permissionDao) {

			@Override
			protected boolean process(Collection<Permission> page,
					Object passThrough) throws Exception {
				return false;
			}

			@Override
			protected boolean process(Permission permission, Object passThrough)
					throws Exception {
				Map<String, Object> inOut = (Map<String, Object>) passThrough;
				permissionDao.evict(permission);
				permission.setProfilePermissions(profilePermissionDao.search(new Search(new SearchParameter[] {
						new SearchParameter("permission.id", permission.getId(), SearchParameter.EQUAL_COMPARATOR) })));
				((ArrayList<Permission>) inOut.get("permissions")).add(permission);
				return true;
			}
		};
		Map<String, Object> passThrough = new HashMap<String, Object>();
		ArrayList<Permission> permissions = new ArrayList<Permission>();
		passThrough.put("permissions", permissions);
		permissionProcessor.processEach(PageSizes.DEFAULT, passThrough); // huge: out of mem
		return exportPermissionDefinitions(fileName, encoding, permissions);
	}

	public long exportPermissionDefinitions(String fileName, String encoding, Collection<Permission> permissions) throws Exception {
		permissionDefinitionWriter.setJobOutput(jobOutput);
		permissionDefinitionWriter.setPermissions(permissions);
		return printLines(fileName, encoding, permissionDefinitionWriter);
	}

	public long exportPermissionTemplate(String fileName, String encoding) throws Exception {
		permissionTemplateWriter.setJobOutput(jobOutput);
		return printLines(fileName, encoding, permissionTemplateWriter);
	}

	private long printLines(String fileName, String encoding, LineWriter lineWriter) throws Exception {
		jobOutput.println("writing to file " + fileName);
		PrintWriter writer = new PrintWriter(fileName, ExecUtil.sanitizeEncoding(encoding, jobOutput));
		lineWriter.init(writer);
		lineWriter.printLines();
		writer.close();
		jobOutput.println(lineWriter.getLineCount() + " rows exported");
		return lineWriter.getLineCount();
	}

	public void setJobOutput(JobOutput jobOutput) {
		this.jobOutput = jobOutput;
	}
}
