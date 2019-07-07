package org.phoenixctms.ctsms.web.model.shared;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.JsUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.FileContentInVO;
import org.phoenixctms.ctsms.vo.FileInVO;
import org.phoenixctms.ctsms.vo.FileOutVO;
import org.phoenixctms.ctsms.vo.FilePDFVO;
import org.phoenixctms.ctsms.vo.FileStreamInVO;
import org.phoenixctms.ctsms.vo.FileStreamOutVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.MassMailOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.web.model.DefaultTreeNode;
import org.phoenixctms.ctsms.web.model.FileFolderVO;
import org.phoenixctms.ctsms.web.model.IDVOTreeNode;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

@ManagedBean
@ViewScoped
public class FileBean extends ManagedBeanBase {

	public static final String PUBLIC_FILE_PATH = "file";

	public static void copyFileOutToIn(FileInVO in, FileOutVO out) {
		if (in != null && out != null) {
			InventoryOutVO inventoryVO = out.getInventory();
			StaffOutVO staffVO = out.getStaff();
			CourseOutVO courseVO = out.getCourse();
			TrialOutVO trialVO = out.getTrial();
			MassMailOutVO massMailVO = out.getMassMail();
			ProbandOutVO probandVO = out.getProband();
			in.setActive(out.getActive());
			in.setPublicFile(out.getPublicFile());
			in.setComment(out.getComment());
			in.setCourseId(courseVO == null ? null : courseVO.getId());
			in.setId(out.getId());
			in.setVersion(out.getVersion());
			in.setInventoryId(inventoryVO == null ? null : inventoryVO.getId());
			in.setLogicalPath(out.getLogicalPath());
			in.setModule(out.getModule());
			in.setProbandId(probandVO == null ? null : probandVO.getId());
			in.setStaffId(staffVO == null ? null : staffVO.getId());
			in.setTitle(out.getTitle());
			in.setTrialId(trialVO == null ? null : trialVO.getId());
			in.setMassMailId(massMailVO == null ? null : massMailVO.getId());
		}
	}

	private static TreeNode createFileRootTreeNode() {
		DefaultTreeNode fileRoot = new DefaultTreeNode(new FileFolderVO(), null);
		fileRoot.setType(WebUtil.FOLDER_NODE_TYPE);
		fileRoot.setExpanded(true);
		return fileRoot;
	}

	public static void initFileDefaultValues(FileInVO in, Long entityId, FileModule module) {
		if (in != null) {
			in.setActive(Settings.getBoolean(SettingCodes.FILE_ACTIVE_PRESET, Bundle.SETTINGS, DefaultSettings.FILE_ACTIVE_PRESET));
			in.setPublicFile(Settings.getBoolean(SettingCodes.FILE_PUBLIC_PRESET, Bundle.SETTINGS, DefaultSettings.FILE_PUBLIC_PRESET));
			in.setComment(Messages.getString(MessageCodes.FILE_COMMENT_PRESET));
			in.setCourseId(FileModule.COURSE_DOCUMENT.equals(module) ? entityId : null);
			in.setId(null);
			in.setVersion(null);
			in.setInventoryId(FileModule.INVENTORY_DOCUMENT.equals(module) ? entityId : null);
			in.setLogicalPath(Settings.getString(SettingCodes.FILE_LOGICAL_PATH_PRESET, Bundle.SETTINGS, DefaultSettings.FILE_LOGICAL_PATH_PRESET));
			in.setModule(module);
			in.setProbandId(FileModule.PROBAND_DOCUMENT.equals(module) ? entityId : null);
			in.setStaffId(FileModule.STAFF_DOCUMENT.equals(module) ? entityId : null);
			in.setTitle(Messages.getString(MessageCodes.FILE_TITLE_PRESET));
			in.setTrialId(FileModule.TRIAL_DOCUMENT.equals(module) ? entityId : null);
			in.setMassMailId(FileModule.MASS_MAIL_DOCUMENT.equals(module) ? entityId : null);
		}
	}

	private static void loadFileFolderTree(TreeNode parent, FileOutVO selected, boolean select, FileModule module, Long id, String parentLogicalPath, Boolean active,
			Boolean publicFile, PSFVO psf,
			int depth,
			boolean selectable) {
		if (module != null) {
			Collection<String> folders = null;
			try {
				folders = WebUtil.getServiceLocator().getFileService().getFileFolders(WebUtil.getAuthentication(), module, id, parentLogicalPath, false, active, publicFile, psf);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (folders != null) {
				Iterator<String> it = folders.iterator();
				while (it.hasNext()) {
					String folder = it.next();
					DefaultTreeNode folderNode;
					if (parent.getData() instanceof FileFolderVO) {
						FileFolderVO parentFolder = ((FileFolderVO) parent.getData());
						folderNode = new DefaultTreeNode(new FileFolderVO(parentLogicalPath, folder, parentFolder), parent);
						parentFolder.incrementFolderCount();
					} else {
						folderNode = new DefaultTreeNode(new FileFolderVO(parentLogicalPath, folder, null), parent);
					}
					folderNode.setType(WebUtil.FOLDER_NODE_TYPE);
					folderNode.setSelectable(selectable);
					if (depth != 0) {
						loadFileFolderTree(folderNode, selected, select, module, id, folder, active, publicFile, psf, depth - 1, selectable);
					}
				}
			}
			Collection<FileOutVO> fileVOs = null;
			try {
				fileVOs = WebUtil.getServiceLocator().getFileService().getFiles(WebUtil.getAuthentication(), module, id, parentLogicalPath, false, active, publicFile, psf);
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
			if (fileVOs != null) {
				Iterator<FileOutVO> it = fileVOs.iterator();
				while (it.hasNext()) {
					FileOutVO fileVO = it.next();
					IDVOTreeNode fileNode = new IDVOTreeNode(fileVO, parent);
					if (parent.getData() instanceof FileFolderVO) {
						FileFolderVO parentFolderVO = ((FileFolderVO) parent.getData());
						parentFolderVO.incrementFileCount();
						parentFolderVO.addSize(fileVO.getSize());
						parentFolderVO.updateModifiedTimestamp(fileVO.getModifiedTimestamp());
					}
					if (selected != null && selected.getId() == fileVO.getId()) {
						fileNode.setSelected(select);
						parent.setExpanded(true);
					}
					fileNode.setType(WebUtil.FILE_NODE_TYPE);
					fileNode.setSelectable(selectable);
				}
			}
		}
	}

	private FileInVO in;
	private FileOutVO out;
	private FileOutVO lastUploadedOut;
	private Long entityId;
	private FileModule module;
	private InventoryOutVO inventory;
	private StaffOutVO staff;
	private CourseOutVO course;
	private TrialOutVO trial;
	private ProbandOutVO proband;
	// private InputFieldOutVO inputField;
	private MassMailOutVO massMail;
	private FileContentInVO contentIn;
	private FileStreamInVO streamIn;
	private TreeNode fileRoot;
	private String allowTypes;
	private Boolean streamUploadEnabled;
	private Integer uploadSizeLimit;
	private Long fileCount;
	private String logicalFileSystemStats;
	private boolean useFileEncryption;
	private String fileNameFilter;
	private String fileLogicalPathFilter;
	private String titleFilter;
	private Boolean activeFilter;
	private Boolean publicFilter;

	public FileBean() {
		super();
		useFileEncryption = false;
		fileCount = null;
		logicalFileSystemStats = null;
		streamUploadEnabled = null;
		try {
			streamUploadEnabled = WebUtil.getServiceLocator().getToolsService().isStreamUploadEnabled();
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		uploadSizeLimit = null;
		try {
			uploadSizeLimit = WebUtil.getServiceLocator().getToolsService().getUploadSizeLimit();
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		}
		this.fileRoot = createFileRootTreeNode();
	}

	@Override
	public String addAction() {
		return addAction(true);
	}

	private String addAction(boolean init) {
		if (streamUploadEnabled == null) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.FILE_STREAM_UPLOAD_MODE_UNDEFINED);
			return ERROR_OUTCOME;
		}
		FileInVO backup = new FileInVO(in);
		// Long idBackup = in.getId();
		// Long versionBackup = in.getVersion();
		in.setId(null);
		in.setVersion(null);
		in.setLogicalPath(CommonUtil.fixLogicalPathFolderName(in.getLogicalPath()));
		try {
			if (streamUploadEnabled) {
				out = WebUtil.getServiceLocator().getFileService().addFile(WebUtil.getAuthentication(), in, streamIn);
			} else {
				out = WebUtil.getServiceLocator().getFileService().addFile(WebUtil.getAuthentication(), in, contentIn);
			}
			if (init) {
				initIn();
				initSets();
			}
			addOperationSuccessMessage(MessageCodes.ADD_OPERATION_SUCCESSFUL);
			return ADD_OUTCOME;
		} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			in.copy(backup);
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	@Override
	protected void appendRequestContextCallbackArgs(boolean operationSuccess) {
		if (module != null) {
			RequestContext requestContext;
			switch (module) {
				case INVENTORY_DOCUMENT:
					requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_INVENTORY_FILE_TAB_TITLE_BASE64, JSValues.AJAX_INVENTORY_FILE_COUNT,
							MessageCodes.INVENTORY_FILES_TAB_TITLE, MessageCodes.INVENTORY_FILES_TAB_TITLE_WITH_COUNT, fileCount);
					if (operationSuccess && in.getInventoryId() != null) {
						WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_INVENTORY_JOURNAL_TAB_TITLE_BASE64,
								JSValues.AJAX_INVENTORY_JOURNAL_ENTRY_COUNT, MessageCodes.INVENTORY_JOURNAL_TAB_TITLE, MessageCodes.INVENTORY_JOURNAL_TAB_TITLE_WITH_COUNT,
								WebUtil.getJournalCount(JournalModule.INVENTORY_JOURNAL, in.getInventoryId()));
					}
					break;
				case STAFF_DOCUMENT:
					requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_STAFF_FILE_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_FILE_COUNT,
							MessageCodes.STAFF_FILES_TAB_TITLE, MessageCodes.STAFF_FILES_TAB_TITLE_WITH_COUNT, fileCount);
					if (operationSuccess && in.getStaffId() != null) {
						WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_STAFF_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_STAFF_JOURNAL_ENTRY_COUNT,
								MessageCodes.STAFF_JOURNAL_TAB_TITLE, MessageCodes.STAFF_JOURNAL_TAB_TITLE_WITH_COUNT,
								WebUtil.getJournalCount(JournalModule.STAFF_JOURNAL, in.getStaffId()));
					}
					break;
				case COURSE_DOCUMENT:
					requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_COURSE_FILE_TAB_TITLE_BASE64, JSValues.AJAX_COURSE_FILE_COUNT,
							MessageCodes.COURSE_FILES_TAB_TITLE, MessageCodes.COURSE_FILES_TAB_TITLE_WITH_COUNT, fileCount);
					if (operationSuccess && in.getCourseId() != null) {
						WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_COURSE_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_COURSE_JOURNAL_ENTRY_COUNT,
								MessageCodes.COURSE_JOURNAL_TAB_TITLE, MessageCodes.COURSE_JOURNAL_TAB_TITLE_WITH_COUNT,
								WebUtil.getJournalCount(JournalModule.COURSE_JOURNAL, in.getCourseId()));
					}
					break;
				case TRIAL_DOCUMENT:
					requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_TRIAL_FILE_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_FILE_COUNT,
							MessageCodes.TRIAL_FILES_TAB_TITLE, MessageCodes.TRIAL_FILES_TAB_TITLE_WITH_COUNT, fileCount);
					if (operationSuccess && in.getTrialId() != null) {
						WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_TRIAL_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_TRIAL_JOURNAL_ENTRY_COUNT,
								MessageCodes.TRIAL_JOURNAL_TAB_TITLE, MessageCodes.TRIAL_JOURNAL_TAB_TITLE_WITH_COUNT,
								WebUtil.getJournalCount(JournalModule.TRIAL_JOURNAL, in.getTrialId()));
					}
					break;
				case PROBAND_DOCUMENT:
					requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_PROBAND_FILE_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_FILE_COUNT,
							MessageCodes.PROBAND_FILES_TAB_TITLE, MessageCodes.PROBAND_FILES_TAB_TITLE_WITH_COUNT, fileCount);
					if (operationSuccess && in.getProbandId() != null) {
						WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_PROBAND_JOURNAL_TAB_TITLE_BASE64, JSValues.AJAX_PROBAND_JOURNAL_ENTRY_COUNT,
								MessageCodes.PROBAND_JOURNAL_TAB_TITLE, MessageCodes.PROBAND_JOURNAL_TAB_TITLE_WITH_COUNT,
								WebUtil.getJournalCount(JournalModule.PROBAND_JOURNAL, in.getProbandId()));
					}
					break;
				case MASS_MAIL_DOCUMENT:
					requestContext = WebUtil.appendRequestContextCallbackTabTitleArgs(null, JSValues.AJAX_MASS_MAIL_FILE_TAB_TITLE_BASE64, JSValues.AJAX_MASS_MAIL_FILE_COUNT,
							MessageCodes.MASS_MAIL_FILES_TAB_TITLE, MessageCodes.MASS_MAIL_FILES_TAB_TITLE_WITH_COUNT, fileCount);
					if (operationSuccess && in.getMassMailId() != null) {
						WebUtil.appendRequestContextCallbackTabTitleArgs(requestContext, JSValues.AJAX_MASS_MAIL_JOURNAL_TAB_TITLE_BASE64,
								JSValues.AJAX_MASS_MAIL_JOURNAL_ENTRY_COUNT,
								MessageCodes.MASS_MAIL_JOURNAL_TAB_TITLE, MessageCodes.MASS_MAIL_JOURNAL_TAB_TITLE_WITH_COUNT,
								WebUtil.getJournalCount(JournalModule.MASS_MAIL_JOURNAL, in.getMassMailId()));
					}
					break;
				default:
					break;
			}
		}
	}

	@Override
	protected String changeAction(Long id) {
		out = null;
		if (id != null) {
			try {
				out = WebUtil.getServiceLocator().getFileService().getFile(WebUtil.getAuthentication(), id);
				if (!out.isDecrypted()) {
					Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.ENCRYPTED_FILE, Long.toString(out.getId()));
					out = null;
					return ERROR_OUTCOME;
				}
			} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
			}
		}
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	private String changeAction(String param, FileModule module) {
		out = null;
		this.entityId = WebUtil.stringToLong(param);
		this.module = module;
		fileNameFilter = null;
		fileLogicalPathFilter = null;
		titleFilter = null;
		activeFilter = null;
		publicFilter = null;
		initIn();
		initSets();
		return CHANGE_OUTCOME;
	}

	public void changeCourse(String param) {
		actionPostProcess(changeCourseAction(param));
	}

	public String changeCourseAction(String param) {
		return changeAction(param, FileModule.COURSE_DOCUMENT);
	}

	public void changeInventory(String param) {
		actionPostProcess(changeInventoryAction(param));
	}

	public String changeInventoryAction(String param) {
		return changeAction(param, FileModule.INVENTORY_DOCUMENT);
	}

	public void changeMassMail(String param) {
		actionPostProcess(changeMassMailAction(param));
	}

	public String changeMassMailAction(String param) {
		return changeAction(param, FileModule.MASS_MAIL_DOCUMENT);
	}

	public void changeProband(String param) {
		actionPostProcess(changeProbandAction(param));
	}

	public String changeProbandAction(String param) {
		return changeAction(param, FileModule.PROBAND_DOCUMENT);
	}

	public void changeStaff(String param) {
		actionPostProcess(changeStaffAction(param));
	}

	public String changeStaffAction(String param) {
		return changeAction(param, FileModule.STAFF_DOCUMENT);
	}

	public void changeTrial(String param) {
		actionPostProcess(changeTrialAction(param));
	}

	public String changeTrialAction(String param) {
		return changeAction(param, FileModule.TRIAL_DOCUMENT);
	}

	public List<String> completeLogicalPath(String query) {
		this.in.setLogicalPath(query);
		return WebUtil.completeLogicalPath(module, entityId, query);
	}

	public List<String> completeLogicalPathFilter(String query) {
		fileLogicalPathFilter = query;
		return WebUtil.completeLogicalPath(module, entityId, query);
	}

	private PSFVO createSFVO() {
		Map<String, String> fileFilters = new HashMap<String, String>();
		if (titleFilter != null && titleFilter.length() > 0) {
			fileFilters.put(useFileEncryption ? WebUtil.FILE_TITLE_HASH_PSF_PROPERTY_NAME : WebUtil.FILE_TITLE_PSF_PROPERTY_NAME, titleFilter);
		}
		if (fileNameFilter != null && fileNameFilter.length() > 0) {
			fileFilters.put(useFileEncryption ? WebUtil.FILE_NAME_HASH_PSF_PROPERTY_NAME : WebUtil.FILE_NAME_PSF_PROPERTY_NAME, fileNameFilter);
		}
		if (fileLogicalPathFilter != null && fileLogicalPathFilter.length() > 0) {
			fileFilters.put(WebUtil.FILE_LOGICAL_PATH_PSF_PROPERTY_NAME, CommonUtil.fixLogicalPathFolderName(fileLogicalPathFilter));
		}
		if (activeFilter != null) {
			fileFilters.put(WebUtil.FILE_ACTIVE_PSF_PROPERTY_NAME, activeFilter.toString());
		}
		if (publicFilter != null) {
			fileFilters.put(WebUtil.FILE_PUBLIC_PSF_PROPERTY_NAME, publicFilter.toString());
		}
		PSFVO sf = new PSFVO();
		if (!useFileEncryption) {
			sf.setSortField(WebUtil.FILE_TITLE_PSF_PROPERTY_NAME);
			sf.setSortOrder(true);
		} else {
			sf.setSortField(WebUtil.FILE_ID_PSF_PROPERTY_NAME);
			sf.setSortOrder(true);
		}
		sf.setFilters(fileFilters);
		return sf;
	}

	@Override
	public String deleteAction() {
		return deleteAction(in.getId());
	}

	@Override
	public String deleteAction(Long id) {
		try {
			out = WebUtil.getServiceLocator().getFileService().deleteFile(WebUtil.getAuthentication(), id);
			initIn();
			initSets();
			out = null;
			addOperationSuccessMessage(MessageCodes.DELETE_OPERATION_SUCCESSFUL);
			return DELETE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public final void deleteBulk() {
		actionPostProcess(deleteBulkAction());
	}

	public String deleteBulkAction() {
		try {
			PSFVO sf = createSFVO();
			if (sf.getFilters().size() == 0) {
				throw new IllegalArgumentException(Messages.getString(MessageCodes.NO_FILE_FILTERS));
			}
			Collection<FileOutVO> files = WebUtil.getServiceLocator().getFileService()
					.deleteFiles(WebUtil.getAuthentication(), module, entityId, null, false, null, null, sf);
			long itemsLeft = sf.getRowCount() - files.size();
			if (itemsLeft > 0) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.BULK_DELETE_OPERATION_INCOMPLETE, files.size(), sf.getRowCount());
			} else {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_INFO, MessageCodes.BULK_DELETE_OPERATION_SUCCESSFUL, sf.getRowCount(), sf.getRowCount());
			}
			initIn();
			initSets();
			out = null;
			return BULK_DELETE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public Boolean getActiveFilter() {
		return activeFilter;
	}

	public StreamedContent getAggregatedPdfFilesStreamedContent() throws Exception {
		if (entityId != null) {
			try {
				FilePDFVO aggregatedPDFFiles = WebUtil.getServiceLocator().getFileService()
						.aggregatePDFFiles(WebUtil.getAuthentication(), module, entityId, null, false, null, null, createSFVO());
				return new DefaultStreamedContent(new ByteArrayInputStream(aggregatedPDFFiles.getDocumentDatas()), aggregatedPDFFiles.getContentType().getMimeType(),
						aggregatedPDFFiles.getFileName());
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
				throw e;
			} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
				throw e;
			}
		}
		return null;
	}

	public String getAllowTypes() {
		return allowTypes;
	}

	public String getFileDownloadLinkLabel() {
		if (out != null) {
			return out.getFileName();
		} else {
			return Messages.getString(MessageCodes.NO_FILE_LINK_LABEL);
		}
	}

	public String getFileLogicalPathFilter() {
		return fileLogicalPathFilter;
	}

	public String getFileNameFilter() {
		return fileNameFilter;
	}

	public TreeNode getFileRoot() {
		return fileRoot;
	}

	public StreamedContent getFileStreamedContent() throws Exception {
		if (out != null) {
			try {
				FileStreamOutVO streamOut = WebUtil.getServiceLocator().getFileService().getFileStream(WebUtil.getAuthentication(), out.getId());
				return new DefaultStreamedContent(streamOut.getStream(), streamOut.getContentType().getMimeType(), streamOut.getFileName());
			} catch (AuthorisationException | ServiceException | IllegalArgumentException e) {
				throw e;
			} catch (AuthenticationException e) {
				WebUtil.publishException(e);
				throw e;
			}
		}
		return null;
	}

	public FileInVO getIn() {
		return in;
	}

	public String getLogicalFileSystemStats() {
		return logicalFileSystemStats;
	}

	@Override
	public String getModifiedAnnotation() {
		if (out != null) {
			return WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			return super.getModifiedAnnotation();
		}
	}

	public String getNodeType(Object data) {
		if (data instanceof FileOutVO) {
			return WebUtil.FILE_NODE_TYPE;
		} else if (data instanceof FileFolderVO) {
			return WebUtil.FOLDER_NODE_TYPE;
		}
		return DefaultTreeNode.DEFAULT_TYPE;
	}

	public FileOutVO getOut() {
		return out;
	}

	public String getPublicFileSignupUrl() {
		if (out != null) {
			String publicFileUrlFormat = Settings.getString(SettingCodes.PUBLIC_FILE_SIGNUP_URL, Bundle.SETTINGS, DefaultSettings.PUBLIC_FILE_SIGNUP_URL);
			if (!CommonUtil.isEmptyString(publicFileUrlFormat)) {
				return MessageFormat.format(publicFileUrlFormat, Long.toString(out.getId()));
			}
		}
		return null;
	}

	public String getPublicFileUrl() {
		if (out != null) {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			StringBuffer url = new StringBuffer(WebUtil.getBaseUrl(request));
			url.append(request.getContextPath()).append("/").append(PUBLIC_FILE_PATH);
			url.append('?').append(GetParamNames.FILE_ID.toString()).append("=").append(Long.toString(out.getId()));
			return url.toString();
		}
		return null;
	}

	public Boolean getPublicFilter() {
		return publicFilter;
	}

	public TreeNode getSelectedFile() {
		// System.out.println("getselectedfile");
		return IDVOTreeNode.findNode(fileRoot, this.out);
	}

	@Override
	public String getTitle() {
		if (out != null) {
			return Messages.getMessage(MessageCodes.FILE_TITLE, Long.toString(out.getId()), out.getTitle());
		} else {
			return Messages.getString(MessageCodes.CREATE_NEW_FILE);
		}
	}

	public String getTitleFilter() {
		return titleFilter;
	}

	public Integer getUploadSizeLimit() {
		return uploadSizeLimit;
	}

	public void handleActiveFilterChanged() {
		updateFileFolderTree(this.module, this.entityId);
	}

	public void handleFileNameFilterKeyUp() {
		updateFileFolderTree(this.module, this.entityId);
	}

	public void handleFileUpload(FileUploadEvent event) {
		if (streamUploadEnabled == null) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.FILE_STREAM_UPLOAD_MODE_UNDEFINED);
			return;
		}
		UploadedFile uploadedFile = event.getFile();
		if (streamUploadEnabled) {
			contentIn = null;
			streamIn = new FileStreamInVO();
			streamIn.setFileName(uploadedFile.getFileName());
			streamIn.setMimeType(uploadedFile.getContentType());
			streamIn.setSize(uploadedFile.getSize());
			try {
				streamIn.setStream(uploadedFile.getInputstream());
				addOperationSuccessMessage(MessageCodes.UPLOAD_OPERATION_SUCCESSFUL);
			} catch (IOException e) {
				streamIn = null;
				Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
				return;
			}
		} else {
			streamIn = null;
			contentIn = new FileContentInVO();
			contentIn.setFileName(uploadedFile.getFileName());
			contentIn.setMimeType(uploadedFile.getContentType());
			contentIn.setDatas(uploadedFile.getContents());
			addOperationSuccessMessage(MessageCodes.UPLOAD_OPERATION_SUCCESSFUL);
		}
		if (!(in.getTitle() != null && in.getTitle().length() > 0) || in.getTitle().equals(Messages.getString(MessageCodes.FILE_TITLE_PRESET))) {
			in.setTitle(uploadedFile.getFileName());
			if (isCreateable()) {
				String path = CommonUtil.fixLogicalPathFolderName(in.getLogicalPath());
				if (ADD_OUTCOME.equals(addAction(false))) {
					// updateFileFolderTree(module, entityId, false);
					// updateLogicalFileSystemStats();
					lastUploadedOut = out;
					out = null;
					initIn();
					// initSets();
					in.setLogicalPath(path);
				}
			}
		}
	}

	public void handleFileUploaded() {
		updateFileFolderTree(module, entityId, lastUploadedOut, false);
		lastUploadedOut = null;
		updateLogicalFileSystemStats();
		appendRequestContextCallbackArgs(true);
	}

	public void handleLogicalPathFilterSelect(SelectEvent event) {
		fileLogicalPathFilter = (String) event.getObject();
		updateFileFolderTree(this.module, this.entityId);
	}

	public void handleLogicalPathSelect(SelectEvent event) {
		in.setLogicalPath((String) event.getObject());
	}

	public void handlePublicFilterChanged() {
		updateFileFolderTree(this.module, this.entityId);
	}

	public void handleTitleFilterKeyUp() {
		updateFileFolderTree(this.module, this.entityId);
	}

	@PostConstruct
	private void init() {
		Long id = WebUtil.getLongParamValue(GetParamNames.FILE_ID);
		if (id != null) {
			this.load(id);
		} else {
			initIn();
			initSets();
		}
	}

	private void initIn() {
		if (in == null) {
			in = new FileInVO();
		}
		if (out != null) {
			copyFileOutToIn(in, out);
			if (in.getInventoryId() != null) {
				entityId = in.getInventoryId();
				module = FileModule.INVENTORY_DOCUMENT;
			} else if (in.getStaffId() != null) {
				entityId = in.getStaffId();
				module = FileModule.STAFF_DOCUMENT;
			} else if (in.getCourseId() != null) {
				entityId = in.getCourseId();
				module = FileModule.COURSE_DOCUMENT;
			} else if (in.getTrialId() != null) {
				entityId = in.getTrialId();
				module = FileModule.TRIAL_DOCUMENT;
			} else if (in.getProbandId() != null) {
				entityId = in.getProbandId();
				module = FileModule.PROBAND_DOCUMENT;
			} else if (in.getMassMailId() != null) {
				entityId = in.getMassMailId();
				module = FileModule.MASS_MAIL_DOCUMENT;
			} else {
				entityId = null;
				module = null;
			}
		} else {
			initFileDefaultValues(in, entityId, module);
		}
		contentIn = null;
		streamIn = null;
	}

	private void initSets() {
		inventory = (FileModule.INVENTORY_DOCUMENT.equals(module) ? WebUtil.getInventory(entityId, null, null, null) : null);
		staff = (FileModule.STAFF_DOCUMENT.equals(module) ? WebUtil.getStaff(entityId, null, null, null) : null);
		course = (FileModule.COURSE_DOCUMENT.equals(module) ? WebUtil.getCourse(entityId, null, null, null) : null);
		trial = (FileModule.TRIAL_DOCUMENT.equals(module) ? WebUtil.getTrial(entityId) : null);
		proband = (FileModule.PROBAND_DOCUMENT.equals(module) ? WebUtil.getProband(entityId, null, null, null) : null);
		massMail = (FileModule.MASS_MAIL_DOCUMENT.equals(module) ? WebUtil.getMassMail(entityId) : null);
		useFileEncryption = CommonUtil.getUseFileEncryption(module);
		lastUploadedOut = null;
		updateFileFolderTree(module, entityId);
		updateLogicalFileSystemStats();
		allowTypes = WebUtil.getAllowedFileExtensionsPattern(module, null);
		if (module != null) {
			switch (module) {
				case INVENTORY_DOCUMENT:
					break;
				case STAFF_DOCUMENT:
					break;
				case COURSE_DOCUMENT:
					break;
				case TRIAL_DOCUMENT:
					if (WebUtil.isTrialLocked(trial)) {
						Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.TRIAL_LOCKED);
					}
					break;
				case PROBAND_DOCUMENT:
					if (WebUtil.isProbandLocked(proband)) {
						Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.PROBAND_LOCKED);
					}
					break;
				case MASS_MAIL_DOCUMENT:
					if (WebUtil.isMassMailLocked(massMail)) {
						Messages.addLocalizedMessage(FacesMessage.SEVERITY_WARN, MessageCodes.MASS_MAIL_LOCKED);
					}
					break;
				default:
					break;
			}
		}
		if (streamUploadEnabled == null) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.FILE_STREAM_UPLOAD_MODE_UNDEFINED);
		}
	}

	public boolean isBulkRemovable() {
		if (module != null && entityId != null && createSFVO().getFilters().size() > 0) {
			switch (module) {
				//				case INVENTORY_DOCUMENT:
				//					return entityId != null;
				//				case STAFF_DOCUMENT:
				//					return entityId != null;
				//				case COURSE_DOCUMENT:
				//					return entityId != null;
				case TRIAL_DOCUMENT:
					return !WebUtil.isTrialLocked(trial);
				case PROBAND_DOCUMENT:
					return !WebUtil.isProbandLocked(proband);
				case MASS_MAIL_DOCUMENT:
					return !WebUtil.isMassMailLocked(massMail);
				default:
					break;
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean isCreateable() {
		if (module != null) {
			switch (module) {
				case INVENTORY_DOCUMENT:
					return (in.getInventoryId() == null ? false : isFileUploaded());
				case STAFF_DOCUMENT:
					return (in.getStaffId() == null ? false : isFileUploaded());
				case COURSE_DOCUMENT:
					return (in.getCourseId() == null ? false : isFileUploaded());
				case TRIAL_DOCUMENT:
					return (in.getTrialId() == null ? false : isFileUploaded() && !WebUtil.isTrialLocked(trial));
				case PROBAND_DOCUMENT:
					return (in.getProbandId() == null ? false : isFileUploaded() && !WebUtil.isProbandLocked(proband));
				case MASS_MAIL_DOCUMENT:
					return (in.getMassMailId() == null ? false : isFileUploaded() && !WebUtil.isMassMailLocked(massMail));
				default:
					break;
			}
		}
		return false;
	}

	@Override
	public boolean isCreated() {
		return out != null;
	}

	public boolean isDynamic() {
		if (module != null) {
			switch (module) {
				case INVENTORY_DOCUMENT:
					return Settings.getBoolean(SettingCodes.INVENTORY_FILE_FOLDER_TREE_DYNAMIC, Bundle.SETTINGS, DefaultSettings.INVENTORY_FILE_FOLDER_TREE_DYNAMIC_DEFAULT);
				case STAFF_DOCUMENT:
					return Settings.getBoolean(SettingCodes.STAFF_FILE_FOLDER_TREE_DYNAMIC, Bundle.SETTINGS, DefaultSettings.STAFF_FILE_FOLDER_TREE_DYNAMIC_DEFAULT);
				case COURSE_DOCUMENT:
					return Settings.getBoolean(SettingCodes.COURSE_FILE_FOLDER_TREE_DYNAMIC, Bundle.SETTINGS, DefaultSettings.COURSE_FILE_FOLDER_TREE_DYNAMIC_DEFAULT);
				case TRIAL_DOCUMENT:
					return Settings.getBoolean(SettingCodes.TRIAL_FILE_FOLDER_TREE_DYNAMIC, Bundle.SETTINGS, DefaultSettings.TRIAL_FILE_FOLDER_TREE_DYNAMIC_DEFAULT);
				case PROBAND_DOCUMENT:
					return Settings.getBoolean(SettingCodes.PROBAND_FILE_FOLDER_TREE_DYNAMIC, Bundle.SETTINGS, DefaultSettings.PROBAND_FILE_FOLDER_TREE_DYNAMIC_DEFAULT);
				case MASS_MAIL_DOCUMENT:
					return Settings.getBoolean(SettingCodes.MASS_MAIL_FILE_FOLDER_TREE_DYNAMIC, Bundle.SETTINGS, DefaultSettings.MASS_MAIL_FILE_FOLDER_TREE_DYNAMIC_DEFAULT);
				default:
					break;
			}
		}
		return true;
	}

	@Override
	public boolean isEditable() {
		if (module != null) {
			switch (module) {
				case INVENTORY_DOCUMENT:
					return super.isEditable();
				case STAFF_DOCUMENT:
					return super.isEditable();
				case COURSE_DOCUMENT:
					return super.isEditable();
				case TRIAL_DOCUMENT:
					return isCreated() && !WebUtil.isTrialLocked(trial);
				case PROBAND_DOCUMENT:
					return isCreated() && !WebUtil.isProbandLocked(proband);
				case MASS_MAIL_DOCUMENT:
					return isCreated() && !WebUtil.isMassMailLocked(massMail);
				default:
					break;
			}
		}
		return super.isEditable();
	}

	public boolean isFileUpdateEnabled() {
		return isEditable() && isFileUploaded();
	}

	private boolean isFileUploaded() {
		if (streamUploadEnabled != null) {
			if (streamUploadEnabled) {
				if (streamIn != null) {
					return true;
				}
			} else {
				if (contentIn != null) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isInputVisible() {
		if (module != null) {
			switch (module) {
				case INVENTORY_DOCUMENT:
					return true;
				case STAFF_DOCUMENT:
					return true;
				case COURSE_DOCUMENT:
					return true;
				case TRIAL_DOCUMENT:
					return isCreated() || !WebUtil.isTrialLocked(trial);
				case PROBAND_DOCUMENT:
					return isCreated() || !WebUtil.isProbandLocked(proband);
				case MASS_MAIL_DOCUMENT:
					return isCreated() || !WebUtil.isMassMailLocked(massMail);
				default:
					return true;
			}
		}
		return true;
	}

	@Override
	public boolean isRemovable() {
		if (module != null) {
			switch (module) {
				case INVENTORY_DOCUMENT:
					return super.isRemovable();
				case STAFF_DOCUMENT:
					return super.isRemovable();
				case COURSE_DOCUMENT:
					return super.isRemovable();
				case TRIAL_DOCUMENT:
					return isCreated() && !WebUtil.isTrialLocked(trial);
				case PROBAND_DOCUMENT:
					return isCreated() && !WebUtil.isProbandLocked(proband);
				case MASS_MAIL_DOCUMENT:
					return isCreated() && !WebUtil.isMassMailLocked(massMail);
				default:
					break;
			}
		}
		return super.isRemovable();
	}

	public boolean isUseFileEncryption() {
		return useFileEncryption;
	}

	@Override
	public String loadAction() {
		return loadAction(in.getId());
	}

	@Override
	public String loadAction(Long id) {
		out = null;
		try {
			out = WebUtil.getServiceLocator().getFileService().getFile(WebUtil.getAuthentication(), id);
			if (!out.isDecrypted()) {
				Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.ENCRYPTED_FILE, Long.toString(out.getId()));
				out = null;
				return ERROR_OUTCOME;
			}
			return LOAD_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		} finally {
			initIn();
			initSets();
		}
		return ERROR_OUTCOME;
	}

	public void onNodeExpand(NodeExpandEvent event) {
		TreeNode treeNode = event.getTreeNode();
		if (treeNode != null) {
			Iterator<TreeNode> it = treeNode.getChildren().iterator();
			while (it.hasNext()) {
				it.next().setParent(null);
			}
			treeNode.getChildren().clear();
			loadFileFolderTree(treeNode, out, true, this.module, this.entityId, ((FileFolderVO) treeNode.getData()).getFolderPath(), null, null, createSFVO(), 1, true);
		}
	}

	@Override
	public String resetAction() {
		out = null;
		initIn();
		initSets();
		return RESET_OUTCOME;
	}

	public void selectFileByNode() {
		Long fileId = WebUtil.getLongParamValue(GetParamNames.FILE_ID);
		if (fileId != null) {
			change(fileId.toString());
		} else {
			String logicalPath = JsUtil.decodeBase64(WebUtil.getParamValue(GetParamNames.LOGICAL_PATH));
			if (logicalPath != null) {
				in.setLogicalPath(logicalPath);
			} else {
				// since this is an actionlistener of a command request, we allow deselection explicitly
				this.out = null;
				this.initIn();
				initSets();
			}
		}
	}

	public void setActiveFilter(Boolean activeFilter) {
		this.activeFilter = activeFilter;
	}

	public void setFileLogicalPathFilter(String fileLogicalPathFilter) {
		this.fileLogicalPathFilter = fileLogicalPathFilter;
	}

	public void setFileNameFilter(String fileNameFilter) {
		this.fileNameFilter = fileNameFilter;
	}

	public void setPublicFilter(Boolean publicFilter) {
		this.publicFilter = publicFilter;
	}

	public void setSelectedFile(TreeNode node) { // treetable only
		if (node != null) {
			node.setSelected(false);
			if (node.getData() instanceof FileOutVO) {
				change(Long.toString(((FileOutVO) node.getData()).getId())); // we load the instance again, to refresh the tree hirarchy depth....
			} else if (node.getData() instanceof FileFolderVO) {
				in.setLogicalPath(((FileFolderVO) node.getData()).getFolderPath());
			}
		}
	}

	public void setTitleFilter(String titleFilter) {
		this.titleFilter = titleFilter;
	}

	@Override
	public String updateAction() {
		in.setLogicalPath(CommonUtil.fixLogicalPathFolderName(in.getLogicalPath()));
		try {
			out = WebUtil.getServiceLocator().getFileService().updateFile(WebUtil.getAuthentication(), in);
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	public void updateFile() {
		actionPostProcess(updateFileAction());
	}

	public String updateFileAction() {
		if (streamUploadEnabled == null) {
			Messages.addLocalizedMessage(FacesMessage.SEVERITY_ERROR, MessageCodes.FILE_STREAM_UPLOAD_MODE_UNDEFINED);
			return ERROR_OUTCOME;
		}
		in.setLogicalPath(CommonUtil.fixLogicalPathFolderName(in.getLogicalPath()));
		try {
			if (streamUploadEnabled) {
				out = WebUtil.getServiceLocator().getFileService().updateFile(WebUtil.getAuthentication(), in, streamIn);
			} else {
				out = WebUtil.getServiceLocator().getFileService().updateFile(WebUtil.getAuthentication(), in, contentIn);
			}
			initIn();
			initSets();
			addOperationSuccessMessage(MessageCodes.UPDATE_OPERATION_SUCCESSFUL);
			return UPDATE_OUTCOME;
		} catch (ServiceException | AuthorisationException | IllegalArgumentException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		} catch (AuthenticationException e) {
			Messages.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			WebUtil.publishException(e);
		}
		return ERROR_OUTCOME;
	}

	private void updateFileFolderTree(FileModule module, Long id) {
		updateFileFolderTree(module, id, out, true);
	}

	private void updateFileFolderTree(FileModule module, Long id, FileOutVO selectedOut, boolean select) {
		fileRoot.getChildren().clear();
		((FileFolderVO) fileRoot.getData()).resetCounts();
		loadFileFolderTree(fileRoot, selectedOut, select, module, id, CommonUtil.LOGICAL_PATH_SEPARATOR, null, null, createSFVO(), isDynamic() ? 1 : -1, true);
	}

	private void updateLogicalFileSystemStats() {
		if (!isDynamic()) {
			long totalSize = 0l;
			long totalFileCount = 0l;
			if (fileRoot != null) {
				Iterator<TreeNode> rootChildrenIt = fileRoot.getChildren().iterator();
				while (rootChildrenIt.hasNext()) {
					Object data = rootChildrenIt.next().getData();
					if (data instanceof FileFolderVO) {
						FileFolderVO folderData = (FileFolderVO) data;
						totalSize += folderData.getSize();
						totalFileCount += folderData.getTotalFileCount();
					} else if (data instanceof FileOutVO) {
						FileOutVO fileData = (FileOutVO) data;
						totalSize += fileData.getSize();
						totalFileCount += 1;
					}
				}
			}
			fileCount = totalFileCount;
			logicalFileSystemStats = Messages.getMessage(MessageCodes.LOGICAL_FILE_SYSTEM_STATS_LABEL, CommonUtil.humanReadableByteCount(totalSize), totalFileCount);
		} else {
			fileCount = null;
			logicalFileSystemStats = null;
		}
	}
}
