package org.phoenixctms.ctsms.executable.csv;

import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;

import org.phoenixctms.ctsms.util.ExecUtil;
import org.phoenixctms.ctsms.util.JobOutput;

public class CsvExporter {

	@Autowired
	protected PermissionTemplateWriter permissionTemplateWriter;
	private JobOutput jobOutput;

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
