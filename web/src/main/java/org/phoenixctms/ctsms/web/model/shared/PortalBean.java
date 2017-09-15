package org.phoenixctms.ctsms.web.model.shared;

import java.util.ArrayList;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.NotificationTypeVO;
import org.phoenixctms.ctsms.vo.NotificationVO;
import org.phoenixctms.ctsms.web.model.DynamicEntityMenu;
import org.phoenixctms.ctsms.web.model.DynamicHomeMenu;
import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.model.ManagedBeanBase;
import org.phoenixctms.ctsms.web.model.RecentEntityMenuBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

@ManagedBean
@ViewScoped
public class PortalBean extends ManagedBeanBase {

	private ArrayList<PortalModuleItem> portalModuleItems;
	private ArrayList<SelectItem> filterNotificationTypes;
	private NotificationLazyModel notificationModel;
	private Long notificationCountOfDay;
	private final static String NOTIFICATION_RECENT_STYLECLASS = "ctsms-recent-notification";
	private final static String NOTIFICATION_OBSOLETE_STYLECLASS = "ctsms-obsolete-notification";

	public PortalBean() {
		super();
		notificationModel = new NotificationLazyModel();
	}

	public String getEntityStyleClass(NotificationVO notification) {
		if (notification != null) {
			if (!notification.isObsolete()) {
				String nodeStyleClass = notification.getType().getNodeStyleClass();
				if (nodeStyleClass != null && nodeStyleClass.length() > 0) {
					return nodeStyleClass;
				} else {
					switch (notification.getType().getType()) {
						case MAINTENANCE_REMINDER:
							return notification.getMaintenanceScheduleItem().getInventory().getCategory().getNodeStyleClass();
						case INVENTORY_INACTIVE:
							return notification.getInventoryStatusEntry().getInventory().getCategory().getNodeStyleClass();
						case INVENTORY_INACTIVE_BOOKING:
							return notification.getInventoryBooking().getInventory().getCategory().getNodeStyleClass();
						case STAFF_INACTIVE:
							return notification.getStaffStatusEntry().getStaff().getCategory().getNodeStyleClass();
						case STAFF_INACTIVE_DUTY_ROSTER_TURN:
							return notification.getDutyRosterTurn().getStaff() == null ? "" : notification.getDutyRosterTurn().getStaff().getCategory().getNodeStyleClass();
						case PROBAND_INACTIVE:
							return notification.getProbandStatusEntry().getProband().isDecrypted() ? notification.getProbandStatusEntry().getProband().getCategory()
									.getNodeStyleClass() : "ctsms-icon-encrypted";
						case PROBAND_INACTIVE_VISIT_SCHEDULE_ITEM:
							return notification.getProbandStatusEntry().getProband().isDecrypted() ? notification.getProbandStatusEntry().getProband().getCategory()
									.getNodeStyleClass() : "ctsms-icon-encrypted";
						case EXPIRING_COURSE:
							return notification.getCourse().getCategory().getNodeStyleClass();
						case EXPIRING_COURSE_PARTICIPATION:
							return notification.getCourseParticipationStatusEntry().getCourse().getCategory().getNodeStyleClass();
						case COURSE_PARTICIPATION_STATUS_UPDATED:
							return notification.getCourseParticipationStatusEntry().getCourse().getCategory().getNodeStyleClass();
						case TIMELINE_EVENT_REMINDER:
							return notification.getTimelineEvent().getType().getNodeStyleClass();
						case VISIT_SCHEDULE_ITEM_REMINDER:
							return "";
						case EXPIRING_PROBAND_AUTO_DELETE:
							return notification.getProband().isDecrypted() ? notification.getProband().getCategory().getNodeStyleClass() : "ctsms-icon-encrypted";
						case EXPIRING_PASSWORD:
							return "";
						case TRIAL_STATUS_UPDATED:
							return notification.getTrial().getStatus().getNodeStyleClass();
						case ECRF_STATUS_UPDATED:
							return notification.getEcrfStatusEntry().getStatus().getNodeStyleClass();
						case NEW_ECRF_FIELD_STATUS:
							if (notification.getEcrfFieldStatusEntry().getStatus().getInitial() && !notification.getEcrfFieldStatusEntry().getStatus().getResolved()) {
								return "ctsms-icon-flag-red";
							} else if (notification.getEcrfFieldStatusEntry().getStatus().getUpdated()) {
								return "ctsms-icon-flag-orange";
							} else if (notification.getEcrfFieldStatusEntry().getStatus().getProposed()) {
								return "ctsms-icon-flag-blue";
							} else if (notification.getEcrfFieldStatusEntry().getStatus().getResolved()) {
								return "ctsms-icon-flag-green";
							} else {
								return "ctsms-icon-flag";
							}
							// if (notification.getEcrfFieldStatusEntry().getStatus().getInitial() && !notification.getEcrfFieldStatusEntry().getStatus().getResolved()) {
							// return "ctsms-icon-flag-red";
							// } else if (notification.getEcrfFieldStatusEntry().getStatus().getUpdated()) {
							// return "ctsms-icon-flag-orange";
							// } else if (notification.getEcrfFieldStatusEntry().getStatus().getProposed()) {
							// return "ctsms-icon-flag-blue";
							// } else if (notification.getEcrfFieldStatusEntry().getStatus().getResolved() && !notification.getEcrfFieldStatusEntry().getStatus().getInitial()) {
							// return "ctsms-icon-flag-green";
							// } else {
							// return "ctsms-icon-flag";
							// }
						case PROBANDS_DELETED:
							return "";
						case TRIAL_TAG_MISSING:
							return notification.getTrial().getStatus().getNodeStyleClass();
						case DUTY_ROSTER_TURN_UPDATED:
						case DUTY_ROSTER_TURN_ASSIGNED:
						case DUTY_ROSTER_TURN_UNASSIGNED:
						case DUTY_ROSTER_TURN_DELETED:
							return notification.getStaff().getCategory().getNodeStyleClass();
						case NEW_COURSE:
							return notification.getCourse().getCategory().getNodeStyleClass();
						case USER_ACCOUNT:
							return "";
						default:
							return "";
					}
				}
			} else {
				return "ctsms-strikethrough";
			}
		}
		return "";
	}

	public boolean getEntityStyleClassIsUiIcon(NotificationVO notification) {
		if (notification != null) {
			if (!notification.isObsolete()) {
				String nodeStyleClass = notification.getType().getNodeStyleClass();
				if (!(nodeStyleClass != null && nodeStyleClass.length() > 0)) {
					// return false;
					// } else {
					switch (notification.getType().getType()) {
						case ECRF_STATUS_UPDATED:
							return true;
							// default:
							// return false;
					}
				}
				// } else {
				// return "ctsms-strikethrough";
			}
		}
		// return "";
		return false;
	}

	public ArrayList<SelectItem> getFilterNotificationTypes() {
		if (filterNotificationTypes == null) {
			filterNotificationTypes = WebUtil.getAllNotificationTypes();
			filterNotificationTypes.add(0, new SelectItem(CommonUtil.NO_SELECTION_VALUE, ""));
		}
		return filterNotificationTypes;
	}

	public Long getNotificationCountOfDay() {
		return notificationCountOfDay;
	}

	public NotificationLazyModel getNotificationModel() {
		return notificationModel;
	}

	// public Boolean getNotificationSent(NotificationVO notification) {
	// StaffOutVO identitiy;
	// if (notification != null && (identitiy = WebUtil.getUserIdentity()) != null) {
	// Iterator<NotificationRecipientVO> recipientIt = notification.getRecipients().iterator();
	// while (recipientIt.hasNext()) {
	// NotificationRecipientVO recipient = recipientIt.next();
	// if (identitiy.getId() == recipient.getStaff().getId()) {
	// return recipient.isSent();
	// }
	// }
	// return false;
	// }
	// return null;
	// }

	public String getOpenEntityJSCall(NotificationVO notification) {
		if (notification != null) {
			switch (notification.getType().getType()) {
				case MAINTENANCE_REMINDER:
					return "openInventory(" + Long.toString(notification.getMaintenanceScheduleItem().getInventory().getId()) + ")";
				case INVENTORY_INACTIVE:
					return "openInventory(" + Long.toString(notification.getInventoryStatusEntry().getInventory().getId()) + ")";
				case INVENTORY_INACTIVE_BOOKING:
					return "openInventory(" + Long.toString(notification.getInventoryBooking().getInventory().getId()) + ")";
				case STAFF_INACTIVE:
					return "openStaff(" + Long.toString(notification.getStaffStatusEntry().getStaff().getId()) + ")";
				case STAFF_INACTIVE_DUTY_ROSTER_TURN:
					if (notification.getDutyRosterTurn().getStaff() != null) {
						return "openStaff(" + Long.toString(notification.getDutyRosterTurn().getStaff().getId()) + ")";
					}
					return "";
				case PROBAND_INACTIVE:
					return "openProband(" + Long.toString(notification.getProbandStatusEntry().getProband().getId()) + ")";
				case PROBAND_INACTIVE_VISIT_SCHEDULE_ITEM:
					return "openProband(" + Long.toString(notification.getProbandStatusEntry().getProband().getId()) + ")";
				case EXPIRING_COURSE:
					return "openCourse(" + Long.toString(notification.getCourse().getId()) + ")";
				case EXPIRING_COURSE_PARTICIPATION:
					return "openCourse(" + Long.toString(notification.getCourseParticipationStatusEntry().getCourse().getId()) + ")";
				case COURSE_PARTICIPATION_STATUS_UPDATED:
					return "openStaff(" + Long.toString(notification.getCourseParticipationStatusEntry().getStaff().getId()) + ")";
				case TIMELINE_EVENT_REMINDER:
					return "openTrial(" + Long.toString(notification.getTimelineEvent().getTrial().getId()) + ")";
				case VISIT_SCHEDULE_ITEM_REMINDER:
					return "openTrial(" + Long.toString(notification.getVisitScheduleItem().getTrial().getId()) + ")";
				case EXPIRING_PROBAND_AUTO_DELETE:
					return "openProband(" + Long.toString(notification.getProband().getId()) + ")";
				case EXPIRING_PASSWORD:
					return "openUser(" + Long.toString(notification.getPassword().getUser().getId()) + ")";
				case TRIAL_STATUS_UPDATED:
					return "openTrial(" + Long.toString(notification.getTrial().getId()) + ")";
				case ECRF_STATUS_UPDATED:
					// return "openProband(" + Long.toString(notification.getListEntry().getProband().getId()) + ")";
					return "openTrial(" + Long.toString(notification.getEcrfStatusEntry().getListEntry().getTrial().getId()) + ")";
				case NEW_ECRF_FIELD_STATUS:
					return "openTrial(" + Long.toString(notification.getEcrfFieldStatusEntry().getListEntry().getTrial().getId()) + ")";
				case PROBANDS_DELETED:
					return "";
				case TRIAL_TAG_MISSING:
					return "openTrial(" + Long.toString(notification.getTrial().getId()) + ")";
				case DUTY_ROSTER_TURN_UPDATED:
				case DUTY_ROSTER_TURN_ASSIGNED:
				case DUTY_ROSTER_TURN_UNASSIGNED:
					if (notification.getDutyRosterTurn().getTrial() != null) {
						return "openTrial(" + Long.toString(notification.getDutyRosterTurn().getTrial().getId()) + ")";
					}
					return "";
				case NEW_COURSE:
					return "openUpcomigCourseOverview()";
				case USER_ACCOUNT:
					return "openUser(" + Long.toString(notification.getUser().getId()) + ")";
				case DUTY_ROSTER_TURN_DELETED:
					return "openStaff(" + Long.toString(notification.getStaff().getId()) + ")";
				default:
					return "";
			}
		}
		return "";
	}

	public ArrayList<PortalModuleItem> getPortalModuleItems() {
		return portalModuleItems;
	}

	@PostConstruct
	private void init() {
		initIn();
		initSets();
	}

	private void initIn() {
	}

	private void initSets() {
		if (portalModuleItems == null) {
			portalModuleItems = new ArrayList<PortalModuleItem>();
			if (WebUtil.getModuleEnabled(DBModule.INVENTORY_DB)) {
				portalModuleItems.add(new PortalModuleItem(Messages.getString(MessageCodes.INVENTORY_PORTAL_ITEM_LABEL), "ctsms-largeicon-inventoryhome", Messages
						.getString(MessageCodes.INVENTORY_PORTAL_ITEM_DESCRIPTION),
						new ArrayList<RecentEntityMenuBase>() {

							{
								add(DynamicHomeMenu.getInventoryHomeMenu());
								add(DynamicEntityMenu.getInventoryEntityMenu());
							}
						}, "openInventoryHome()", JournalModule.INVENTORY_JOURNAL));
			}
			if (WebUtil.getModuleEnabled(DBModule.STAFF_DB)) {
				portalModuleItems.add(new PortalModuleItem(Messages.getString(MessageCodes.STAFF_PORTAL_ITEM_LABEL), "ctsms-largeicon-staffhome", Messages
						.getString(MessageCodes.STAFF_PORTAL_ITEM_DESCRIPTION),
						new ArrayList<RecentEntityMenuBase>() {

							{
								add(DynamicHomeMenu.getStaffHomeMenu());
								add(DynamicEntityMenu.getStaffEntityMenu());
							}
						}, "openStaffHome()", JournalModule.STAFF_JOURNAL));
			}
			if (WebUtil.getModuleEnabled(DBModule.COURSE_DB)) {
				portalModuleItems.add(new PortalModuleItem(Messages.getString(MessageCodes.COURSE_PORTAL_ITEM_LABEL), "ctsms-largeicon-coursehome", Messages
						.getString(MessageCodes.COURSE_PORTAL_ITEM_DESCRIPTION),
						new ArrayList<RecentEntityMenuBase>() {

							{
								add(DynamicHomeMenu.getCourseHomeMenu());
								add(DynamicEntityMenu.getCourseEntityMenu());
							}
						}, "openCourseHome()", JournalModule.COURSE_JOURNAL));
			}
			if (WebUtil.getModuleEnabled(DBModule.TRIAL_DB)) {
				portalModuleItems.add(new PortalModuleItem(Messages.getString(MessageCodes.TRIAL_PORTAL_ITEM_LABEL), "ctsms-largeicon-trialhome", Messages
						.getString(MessageCodes.TRIAL_PORTAL_ITEM_DESCRIPTION),
						new ArrayList<RecentEntityMenuBase>() {

							{
								add(DynamicHomeMenu.getTrialHomeMenu());
								add(DynamicEntityMenu.getTrialEntityMenu());
							}
						}, "openTrialHome()", JournalModule.TRIAL_JOURNAL));
			}
			if (WebUtil.getModuleEnabled(DBModule.PROBAND_DB)) {
				portalModuleItems.add(new PortalModuleItem(Messages.getString(MessageCodes.PROBAND_PORTAL_ITEM_LABEL), "ctsms-largeicon-probandhome", Messages
						.getString(MessageCodes.PROBAND_PORTAL_ITEM_DESCRIPTION),
						new ArrayList<RecentEntityMenuBase>() {

							{
								add(DynamicHomeMenu.getProbandHomeMenu());
								add(DynamicEntityMenu.getProbandEntityMenu());
							}
						}, "openProbandHome()", JournalModule.PROBAND_JOURNAL));
			}
			if (WebUtil.getModuleEnabled(DBModule.INPUT_FIELD_DB)) {
				portalModuleItems.add(new PortalModuleItem(Messages.getString(MessageCodes.INPUT_FIELD_PORTAL_ITEM_LABEL), "ctsms-largeicon-inputfieldhome", Messages
						.getString(MessageCodes.INPUT_FIELD_PORTAL_ITEM_DESCRIPTION),
						new ArrayList<RecentEntityMenuBase>() {

							{
								add(DynamicHomeMenu.getInputFieldHomeMenu());
								add(DynamicEntityMenu.getInputFieldEntityMenu());
							}
						}, "openInputFieldHome()", JournalModule.INPUT_FIELD_JOURNAL));
			}
			if (WebUtil.getModuleEnabled(DBModule.USER_DB)) {
				portalModuleItems.add(new PortalModuleItem(Messages.getString(MessageCodes.USER_PORTAL_ITEM_LABEL), "ctsms-largeicon-userhome", Messages
						.getString(MessageCodes.USER_PORTAL_ITEM_DESCRIPTION),
						new ArrayList<RecentEntityMenuBase>() {

							{
								add(DynamicHomeMenu.getUserHomeMenu());
								add(DynamicEntityMenu.getUserEntityMenu());
							}
						}, "openUserHome()", JournalModule.USER_JOURNAL));
			}
		}
		Iterator<PortalModuleItem> moduleItemIt = portalModuleItems.iterator();
		while (moduleItemIt.hasNext()) {
			moduleItemIt.next().updateTagModel();
		}
		notificationModel.updateRowCount();
		LazyDataModelBase.clearFilters("notification_list");
		notificationCountOfDay = null;
		try {
			notificationCountOfDay = WebUtil.getServiceLocator().getUserService().getNotificationCountByDay(WebUtil.getAuthentication(),
					null,
					Settings.getBooleanNullable(SettingCodes.NOTIFICATIONS_OBSOLETE, Bundle.SETTINGS, DefaultSettings.NOTIFICATIONS_OBSOLETE),
					Settings.getBooleanNullable(SettingCodes.NOTIFICATIONS_SEND, Bundle.SETTINGS, DefaultSettings.NOTIFICATIONS_SEND),
					Settings.getBooleanNullable(SettingCodes.NOTIFICATIONS_SHOW, Bundle.SETTINGS, DefaultSettings.NOTIFICATIONS_SHOW),
					Settings.getBooleanNullable(SettingCodes.NOTIFICATIONS_SENT, Bundle.SETTINGS, DefaultSettings.NOTIFICATIONS_SENT),
					Settings.getBooleanNullable(SettingCodes.NOTIFICATIONS_DROPPED, Bundle.SETTINGS, DefaultSettings.NOTIFICATIONS_DROPPED));
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
	}

	@Override
	public boolean isCreateable() {
		return false;
	}

	@Override
	public boolean isCreated() {
		return false;
	}

	public boolean isOpenEntityEnabled(NotificationVO notification) {
		if (notification != null) {
			switch (notification.getType().getType()) {
				case PROBAND_INACTIVE:
					return notification.getProbandStatusEntry().getProband().isDecrypted();
				case PROBAND_INACTIVE_VISIT_SCHEDULE_ITEM:
					return notification.getProbandStatusEntry().getProband().isDecrypted();
				case EXPIRING_PROBAND_AUTO_DELETE:
					return notification.getProband().isDecrypted();
				case PROBANDS_DELETED:
					return false;
				default:
					return true;
			}
		}
		return false;
	}

	public boolean isOpenEntityRendered(NotificationVO notification) {
		NotificationTypeVO type;
		if (notification != null && (type = notification.getType()) != null) {
			switch (type.getType()) {
				case PROBANDS_DELETED:
					return false;
				default:
					return true;
			}
		}
		return false;
	}

	@Override
	public String loadAction() {
		initIn();
		initSets();
		return LOAD_OUTCOME;
	}

	public String notificationToColor(NotificationVO notification) {
		if (notification != null) {
			StringBuilder result = new StringBuilder();
			if (notification.isObsolete()) {
				result.append(NOTIFICATION_OBSOLETE_STYLECLASS);
			} else {
				result.append(NOTIFICATION_RECENT_STYLECLASS);
				result.append(" ");
				result.append(WebUtil.colorToStyleClass(notification.getType().getColor()));
			}
			return result.toString();
		}
		return "";
	}
}
