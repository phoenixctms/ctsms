package org.phoenixctms.ctsms.web.model;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.JournalEntryOutVO;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

public abstract class DynamicHomeMenu extends RecentEntityMenuBase {

	public static DynamicHomeMenu getCourseHomeMenu() {
		return (new DynamicHomeMenu() {

			@Override
			protected void addMenuItems(SessionScopeBean sessionScopeBean, MenuModel entityModel) {
				if (entityModel != null) {
					Submenu subMenu = addSubMenu(sessionScopeBean, entityModel);
					MenuItem adminUpcomingCoursesMenuItem = new MenuItem();
					adminUpcomingCoursesMenuItem.setValue(Messages.getString(MessageCodes.ADMIN_UPCOMING_COURSES_MENU_ITEM_LABEL));
					adminUpcomingCoursesMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-upcomingcourse");
					adminUpcomingCoursesMenuItem.setOnclick("openAdminUpcomingCourseOverview()");
					adminUpcomingCoursesMenuItem.setUrl("#");
					adminUpcomingCoursesMenuItem.setId("adminUpcomingCoursesMenuItem");
					subMenu.getChildren().add(adminUpcomingCoursesMenuItem);
					MenuItem expiringCoursesMenuItem = new MenuItem();
					expiringCoursesMenuItem.setValue(Messages.getString(MessageCodes.EXPIRING_COURSES_MENU_ITEM_LABEL));
					expiringCoursesMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-courseexpiration");
					expiringCoursesMenuItem.setOnclick("openExpiringCourseOverview()");
					expiringCoursesMenuItem.setUrl("#");
					expiringCoursesMenuItem.setId("expiringCoursesMenuItem");
					subMenu.getChildren().add(expiringCoursesMenuItem);
					MenuItem adminExpiringParticipationsMenuItem = new MenuItem();
					adminExpiringParticipationsMenuItem.setValue(Messages.getString(MessageCodes.ADMIN_EXPIRING_PARTICIPATIONS_MENU_ITEM_LABEL));
					adminExpiringParticipationsMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-admincourseexpiration");
					adminExpiringParticipationsMenuItem.setOnclick("openAdminExpiringParticipationOverview()");
					adminExpiringParticipationsMenuItem.setUrl("#");
					adminExpiringParticipationsMenuItem.setId("adminExpiringParticipationsMenuItem");
					subMenu.getChildren().add(adminExpiringParticipationsMenuItem);
				}
			}

			@Override
			protected DBModule getDbModule() {
				return DBModule.COURSE_DB;
			}

			@Override
			protected String getEntityIcon(JournalEntryOutVO journalEntry) {
				return journalEntry.getCourse().getCategory().getNodeStyleClass();
			}

			@Override
			protected long getEntityId(JournalEntryOutVO journalEntry) {
				return journalEntry.getCourse().getId();
			}

			@Override
			protected String getHomeIcon() {
				return "ctsms-icon-coursehome";
			}

			@Override
			protected JournalModule getJournalModule() {
				return JournalModule.COURSE_JOURNAL;
			}

			@Override
			protected String getOpenEntityJsName() {
				return "openCourse({0})";
			}

			@Override
			protected String getOpenNewEntityJsName() {
				return "openNewCourse()";
			}

			@Override
			protected String getOpenNewEntityMenuItemLabel() {
				return Messages.getString(MessageCodes.COURSE_OPEN_NEW_MENU_ITEM_LABEL);
			}

			@Override
			protected String getRecentEntityMenuItemLabel(JournalEntryOutVO journalEntry) {
				return CommonUtil.courseOutVOToString(journalEntry.getCourse());
			}

			@Override
			protected String getSearchMenuItemOnClick() {
				return "openCourseSearch(null)";
			}

			@Override
			protected boolean hasEntity(JournalEntryOutVO journalEntry) {
				return journalEntry.getCourse() != null;
			}
		});
	}

	public static DynamicHomeMenu getInputFieldHomeMenu() {
		return (new DynamicHomeMenu() {

			@Override
			protected DBModule getDbModule() {
				return DBModule.INPUT_FIELD_DB;
			}

			@Override
			protected String getEntityIcon(JournalEntryOutVO journalEntry) {
				return WebUtil.getInputFieldIcon(journalEntry.getInputField());
			}

			@Override
			protected long getEntityId(JournalEntryOutVO journalEntry) {
				return journalEntry.getInputField().getId();
			}

			@Override
			protected String getHomeIcon() {
				return "ctsms-icon-inputfieldhome";
			}

			@Override
			protected JournalModule getJournalModule() {
				return JournalModule.INPUT_FIELD_JOURNAL;
			}

			@Override
			protected String getOpenEntityJsName() {
				return "openInputField({0})";
			}

			@Override
			protected String getOpenNewEntityJsName() {
				return "openNewInputField()";
			}

			@Override
			protected String getOpenNewEntityMenuItemLabel() {
				return Messages.getString(MessageCodes.INPUT_FIELD_OPEN_NEW_MENU_ITEM_LABEL);
			}

			@Override
			protected String getRecentEntityMenuItemLabel(JournalEntryOutVO journalEntry) {
				return CommonUtil.inputFieldOutVOToString(journalEntry.getInputField());
			}

			@Override
			protected String getSearchMenuItemOnClick() {
				return "openInputFieldSearch(null)";
			}

			@Override
			protected boolean hasEntity(JournalEntryOutVO journalEntry) {
				return journalEntry.getInputField() != null;
			}
		});
	}

	public static DynamicHomeMenu getInventoryHomeMenu() {
		return (new DynamicHomeMenu() {

			@Override
			protected void addMenuItems(SessionScopeBean sessionScopeBean, MenuModel entityModel) {
				if (entityModel != null) {
					Submenu subMenu = addSubMenu(sessionScopeBean, entityModel);
					MenuItem availabilityStatusMenuItem = new MenuItem();
					availabilityStatusMenuItem.setValue(Messages.getString(MessageCodes.INVENTORY_AVAILABILITY_STATUS_MENU_ITEM_LABEL));
					availabilityStatusMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-information");
					availabilityStatusMenuItem.setOnclick("openInventoryStatusOverview()");
					availabilityStatusMenuItem.setUrl("#");
					availabilityStatusMenuItem.setId("availabilityStatusMenuItem");
					subMenu.getChildren().add(availabilityStatusMenuItem);
					MenuItem maintenanceRemindersMenuItem = new MenuItem();
					maintenanceRemindersMenuItem.setValue(Messages.getString(MessageCodes.MAINTENANCE_REMINDERS_MENU_ITEM_LABEL));
					maintenanceRemindersMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-maintenance");
					maintenanceRemindersMenuItem.setOnclick("openInventoryMaintenanceOverview()");
					maintenanceRemindersMenuItem.setUrl("#");
					maintenanceRemindersMenuItem.setId("maintenanceRemindersMenuItem");
					subMenu.getChildren().add(maintenanceRemindersMenuItem);
					MenuItem bookingScheduleMenuItem = new MenuItem();
					bookingScheduleMenuItem.setValue(Messages.getString(MessageCodes.BOOKING_SCHEDULE_MENU_ITEM_LABEL));
					bookingScheduleMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-calendar " + WebUtil.MENU_BOLD_STYLECLASS);
					bookingScheduleMenuItem.setOnclick("openInventoryBookingSchedule()");
					bookingScheduleMenuItem.setUrl("#");
					bookingScheduleMenuItem.setId("bookingScheduleMenuItem");
					subMenu.getChildren().add(bookingScheduleMenuItem);
					MenuItem bookingSummaryOverviewMenuItem = new MenuItem();
					bookingSummaryOverviewMenuItem.setValue(Messages.getString(MessageCodes.BOOKING_SUMMARY_OVERVIEW_MENU_ITEM_LABEL));
					bookingSummaryOverviewMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-clock");
					bookingSummaryOverviewMenuItem.setOnclick("openBookingSummaryOverview()");
					bookingSummaryOverviewMenuItem.setUrl("#");
					bookingSummaryOverviewMenuItem.setId("bookingSummaryOverviewMenuItem");
					subMenu.getChildren().add(bookingSummaryOverviewMenuItem);
				}
			}

			@Override
			protected DBModule getDbModule() {
				return DBModule.INVENTORY_DB;
			}

			@Override
			protected String getEntityIcon(JournalEntryOutVO journalEntry) {
				return journalEntry.getInventory().getCategory().getNodeStyleClass();
			}

			@Override
			protected long getEntityId(JournalEntryOutVO journalEntry) {
				return journalEntry.getInventory().getId();
			}

			@Override
			protected String getHomeIcon() {
				return "ctsms-icon-inventoryhome";
			}

			@Override
			protected JournalModule getJournalModule() {
				return JournalModule.INVENTORY_JOURNAL;
			}

			@Override
			protected String getOpenEntityJsName() {
				return "openInventory({0})";
			}

			@Override
			protected String getOpenNewEntityJsName() {
				return "openNewInventory()";
			}

			@Override
			protected String getOpenNewEntityMenuItemLabel() {
				return Messages.getString(MessageCodes.INVENTORY_OPEN_NEW_MENU_ITEM_LABEL);
			}

			@Override
			protected String getRecentEntityMenuItemLabel(JournalEntryOutVO journalEntry) {
				return CommonUtil.inventoryOutVOToString(journalEntry.getInventory());
			}

			@Override
			protected String getSearchMenuItemOnClick() {
				return "openInventorySearch(null)";
			}

			@Override
			protected boolean hasEntity(JournalEntryOutVO journalEntry) {
				return journalEntry.getInventory() != null;
			}
		});
	}

	public static DynamicHomeMenu getMassMailHomeMenu() {
		return (new DynamicHomeMenu() {

			@Override
			protected void addMenuItems(SessionScopeBean sessionScopeBean, MenuModel entityModel) {
				if (entityModel != null) {
					Submenu subMenu = addSubMenu(sessionScopeBean, entityModel);
					MenuItem recipientOverviewMenuItem = new MenuItem();
					recipientOverviewMenuItem.setValue(Messages.getString(MessageCodes.RECIPIENT_OVERVIEW_MENU_ITEM_LABEL));
					recipientOverviewMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-queue");
					recipientOverviewMenuItem.setOnclick("openRecipientOverview()");
					recipientOverviewMenuItem.setUrl("#");
					recipientOverviewMenuItem.setId("recipientOverviewMenuItem");
					subMenu.getChildren().add(recipientOverviewMenuItem);
				}
			}

			@Override
			protected DBModule getDbModule() {
				return DBModule.MASS_MAIL_DB;
			}

			@Override
			protected String getEntityIcon(JournalEntryOutVO journalEntry) {
				return journalEntry.getMassMail().getStatus().getNodeStyleClass();
			}

			@Override
			protected long getEntityId(JournalEntryOutVO journalEntry) {
				return journalEntry.getMassMail().getId();
			}

			@Override
			protected String getHomeIcon() {
				return "ctsms-icon-massmailhome";
			}

			@Override
			protected JournalModule getJournalModule() {
				return JournalModule.MASS_MAIL_JOURNAL;
			}

			@Override
			protected String getOpenEntityJsName() {
				return "openMassMailField({0})";
			}

			@Override
			protected String getOpenNewEntityJsName() {
				return "openNewMassMail()";
			}

			@Override
			protected String getOpenNewEntityMenuItemLabel() {
				return Messages.getString(MessageCodes.MASS_MAIL_OPEN_NEW_MENU_ITEM_LABEL);
			}

			@Override
			protected String getRecentEntityMenuItemLabel(JournalEntryOutVO journalEntry) {
				return CommonUtil.massMailOutVOToString(journalEntry.getMassMail());
			}

			@Override
			protected String getSearchMenuItemOnClick() {
				return "openMassMailSearch(null)";
			}

			@Override
			protected boolean hasEntity(JournalEntryOutVO journalEntry) {
				return journalEntry.getMassMail() != null;
			}
		});
	}

	public static DynamicHomeMenu getProbandHomeMenu() {
		return (new DynamicHomeMenu() {

			@Override
			protected void addMenuItems(SessionScopeBean sessionScopeBean, MenuModel entityModel) {
				if (entityModel != null) {
					Submenu subMenu = addSubMenu(sessionScopeBean, entityModel);
					MenuItem availabilityStatusMenuItem = new MenuItem();
					availabilityStatusMenuItem.setValue(Messages.getString(MessageCodes.PROBAND_AVAILABILITY_STATUS_MENU_ITEM_LABEL));
					availabilityStatusMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-information");
					availabilityStatusMenuItem.setOnclick("openProbandStatusOverview()");
					availabilityStatusMenuItem.setUrl("#");
					availabilityStatusMenuItem.setId("availabilityStatusMenuItem");
					subMenu.getChildren().add(availabilityStatusMenuItem);
					MenuItem autoDeleteRemindersMenuItem = new MenuItem();
					autoDeleteRemindersMenuItem.setValue(Messages.getString(MessageCodes.AUTO_DELETE_REMINDERS_MENU_ITEM_LABEL));
					autoDeleteRemindersMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-autodelete");
					autoDeleteRemindersMenuItem.setOnclick("openAutoDeletionProbandOverview()");
					autoDeleteRemindersMenuItem.setUrl("#");
					autoDeleteRemindersMenuItem.setId("autoDeleteRemindersMenuItem");
					subMenu.getChildren().add(autoDeleteRemindersMenuItem);
				}
			}

			@Override
			protected DBModule getDbModule() {
				return DBModule.PROBAND_DB;
			}

			@Override
			protected String getEntityIcon(JournalEntryOutVO journalEntry) {
				return journalEntry.getProband().getCategory().getNodeStyleClass();
			}

			@Override
			protected long getEntityId(JournalEntryOutVO journalEntry) {
				return journalEntry.getProband().getId();
			}

			@Override
			protected String getHomeIcon() {
				return "ctsms-icon-probandhome";
			}

			@Override
			protected JournalModule getJournalModule() {
				return JournalModule.PROBAND_JOURNAL;
			}

			@Override
			protected String getOpenEntityJsName() {
				return "openProband({0})";
			}

			@Override
			protected String getOpenNewEntityJsName() {
				return "openNewProband()";
			}

			@Override
			protected String getOpenNewEntityMenuItemLabel() {
				return Messages.getString(MessageCodes.PROBAND_OPEN_NEW_MENU_ITEM_LABEL);
			}

			@Override
			protected String getRecentEntityMenuItemLabel(JournalEntryOutVO journalEntry) {
				return CommonUtil.probandOutVOToString(journalEntry.getProband());
			}

			@Override
			protected String getSearchMenuItemOnClick() {
				return "openProbandSearch(null)";
			}

			@Override
			protected boolean hasEntity(JournalEntryOutVO journalEntry) {
				return journalEntry.getProband() != null;
			}
		});
	}

	public static DynamicHomeMenu getStaffHomeMenu() {
		return (new DynamicHomeMenu() {

			@Override
			protected void addMenuItems(SessionScopeBean sessionScopeBean, MenuModel entityModel) {
				if (entityModel != null) {
					Submenu subMenu = addSubMenu(sessionScopeBean, entityModel);
					MenuItem availabilityStatusMenuItem = new MenuItem();
					availabilityStatusMenuItem.setValue(Messages.getString(MessageCodes.STAFF_AVAILABILITY_STATUS_MENU_ITEM_LABEL));
					availabilityStatusMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-information");
					availabilityStatusMenuItem.setOnclick("openStaffStatusOverview()");
					availabilityStatusMenuItem.setUrl("#");
					availabilityStatusMenuItem.setId("availabilityStatusMenuItem");
					subMenu.getChildren().add(availabilityStatusMenuItem);
					MenuItem staffShiftSummaryOverviewMenuItem = new MenuItem();
					staffShiftSummaryOverviewMenuItem.setValue(Messages.getString(MessageCodes.STAFF_SHIFT_SUMMARY_OVERVIEW_MENU_ITEM_LABEL));
					staffShiftSummaryOverviewMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-clock");
					staffShiftSummaryOverviewMenuItem.setOnclick("openStaffShiftSummaryOverview()");
					staffShiftSummaryOverviewMenuItem.setUrl("#");
					staffShiftSummaryOverviewMenuItem.setId("staffShiftSummaryOverviewMenuItem");
					subMenu.getChildren().add(staffShiftSummaryOverviewMenuItem);
				}
			}

			@Override
			protected DBModule getDbModule() {
				return DBModule.STAFF_DB;
			}

			@Override
			protected String getEntityIcon(JournalEntryOutVO journalEntry) {
				return journalEntry.getStaff().getCategory().getNodeStyleClass();
			}

			@Override
			protected long getEntityId(JournalEntryOutVO journalEntry) {
				return journalEntry.getStaff().getId();
			}

			@Override
			protected String getHomeIcon() {
				return "ctsms-icon-staffhome";
			}

			@Override
			protected JournalModule getJournalModule() {
				return JournalModule.STAFF_JOURNAL;
			}

			@Override
			protected String getOpenEntityJsName() {
				return "openStaff({0})";
			}

			@Override
			protected String getOpenNewEntityJsName() {
				return "openNewStaff()";
			}

			@Override
			protected String getOpenNewEntityMenuItemLabel() {
				return Messages.getString(MessageCodes.STAFF_OPEN_NEW_MENU_ITEM_LABEL);
			}

			@Override
			protected String getRecentEntityMenuItemLabel(JournalEntryOutVO journalEntry) {
				return CommonUtil.staffOutVOToString(journalEntry.getStaff());
			}

			@Override
			protected String getSearchMenuItemOnClick() {
				return "openStaffSearch(null)";
			}

			@Override
			protected boolean hasEntity(JournalEntryOutVO journalEntry) {
				return journalEntry.getStaff() != null;
			}
		});
	}

	public static DynamicHomeMenu getTrialHomeMenu() {
		return (new DynamicHomeMenu() {

			@Override
			protected void addMenuItems(SessionScopeBean sessionScopeBean, MenuModel entityModel) {
				if (entityModel != null) {
					Submenu subMenu = addSubMenu(sessionScopeBean, entityModel);
					MenuItem timelineEventRemindersMenuItem = new MenuItem();
					timelineEventRemindersMenuItem.setValue(Messages.getString(MessageCodes.TIMELINE_EVENT_REMINDERS_MENU_ITEM_LABEL));
					timelineEventRemindersMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-timelineevents");
					timelineEventRemindersMenuItem.setOnclick("openTimelineEventOverview()");
					timelineEventRemindersMenuItem.setUrl("#");
					timelineEventRemindersMenuItem.setId("timelineEventRemindersMenuItem");
					subMenu.getChildren().add(timelineEventRemindersMenuItem);
					// if (WebUtil.resourceExists("/trial/trialTimeline.xhtml")) {
					// if (WebUtil.resourceExists("/trial/trialTimelineXXX.xhtml")) {
					MenuItem timelineMenuItem = new MenuItem();
					timelineMenuItem.setValue(Messages.getString(MessageCodes.TIMELINE_MENU_ITEM_LABEL));
					timelineMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-timeline");
					timelineMenuItem.setOnclick("openTrialTimeline()");
					timelineMenuItem.setUrl("#");
					timelineMenuItem.setId("timelineMenuItem");
					subMenu.getChildren().add(timelineMenuItem);
					// }
					if (sessionScopeBean != null) {
						subMenu.getChildren().add(getDutyRosterScheduleMenuItem());
					}
					MenuItem trialShiftSummaryOverviewMenuItem = new MenuItem();
					trialShiftSummaryOverviewMenuItem.setValue(Messages.getString(MessageCodes.TRIAL_SHIFT_SUMMARY_OVERVIEW_MENU_ITEM_LABEL));
					trialShiftSummaryOverviewMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-clock");
					trialShiftSummaryOverviewMenuItem.setOnclick("openTrialShiftSummaryOverview()");
					trialShiftSummaryOverviewMenuItem.setUrl("#");
					trialShiftSummaryOverviewMenuItem.setId("trialShiftSummaryOverviewMenuItem");
					subMenu.getChildren().add(trialShiftSummaryOverviewMenuItem);
					MenuItem moneyTransferOverviewMenuItem = new MenuItem();
					moneyTransferOverviewMenuItem.setValue(Messages.getString(MessageCodes.MONEY_TRANSFER_OVERVIEW_MENU_ITEM_LABEL));
					moneyTransferOverviewMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-moneytransfers");
					moneyTransferOverviewMenuItem.setOnclick("openMoneyTransferOverview()");
					moneyTransferOverviewMenuItem.setUrl("#");
					moneyTransferOverviewMenuItem.setId("moneyTransferOverviewMenuItem");
					subMenu.getChildren().add(moneyTransferOverviewMenuItem);
				}
			}

			@Override
			protected DBModule getDbModule() {
				return DBModule.TRIAL_DB;
			}

			@Override
			protected String getEntityIcon(JournalEntryOutVO journalEntry) {
				return journalEntry.getTrial().getStatus().getNodeStyleClass();
			}

			@Override
			protected long getEntityId(JournalEntryOutVO journalEntry) {
				return journalEntry.getTrial().getId();
			}

			@Override
			protected String getHomeIcon() {
				return "ctsms-icon-trialhome";
			}

			@Override
			protected JournalModule getJournalModule() {
				return JournalModule.TRIAL_JOURNAL;
			}

			@Override
			protected String getOpenEntityJsName() {
				return "openTrial({0})";
			}

			@Override
			protected String getOpenNewEntityJsName() {
				return "openNewTrial()";
			}

			@Override
			protected String getOpenNewEntityMenuItemLabel() {
				return Messages.getString(MessageCodes.TRIAL_OPEN_NEW_MENU_ITEM_LABEL);
			}

			@Override
			protected String getRecentEntityMenuItemLabel(JournalEntryOutVO journalEntry) {
				return CommonUtil.trialOutVOToString(journalEntry.getTrial());
			}

			@Override
			protected String getSearchMenuItemOnClick() {
				return "openTrialSearch(null)";
			}

			@Override
			protected boolean hasEntity(JournalEntryOutVO journalEntry) {
				return journalEntry.getTrial() != null;
			}
		});
	}

	public static DynamicHomeMenu getUserHomeMenu() {
		return (new DynamicHomeMenu() {

			@Override
			protected DBModule getDbModule() {
				return DBModule.USER_DB;
			}

			@Override
			protected String getEntityIcon(JournalEntryOutVO journalEntry) {
				return null;
			}

			@Override
			protected long getEntityId(JournalEntryOutVO journalEntry) {
				return journalEntry.getUser().getId();
			}

			@Override
			protected String getHomeIcon() {
				return "ctsms-icon-userhome";
			}

			@Override
			protected JournalModule getJournalModule() {
				return JournalModule.USER_JOURNAL;
			}

			@Override
			protected String getOpenEntityJsName() {
				return "openUser({0})";
			}

			@Override
			protected String getOpenNewEntityJsName() {
				return "openNewUser()";
			}

			@Override
			protected String getOpenNewEntityMenuItemLabel() {
				return Messages.getString(MessageCodes.USER_OPEN_NEW_MENU_ITEM_LABEL);
			}

			@Override
			protected String getRecentEntityMenuItemLabel(JournalEntryOutVO journalEntry) {
				return CommonUtil.userOutVOToString(journalEntry.getUser());
			}

			@Override
			protected String getSearchMenuItemOnClick() {
				return "openUserSearch(null)";
			}

			@Override
			protected boolean hasEntity(JournalEntryOutVO journalEntry) {
				return journalEntry.getUser() != null;
			}
		});
	}


	@Override
	protected void addMenuItems(SessionScopeBean sessionScopeBean, MenuModel entityModel) {
	}

	@Override
	public MenuModel createMenuModel(SessionScopeBean sessionScopeBean, int maxRecentEntities) {
		MenuModel entityModel = new DefaultMenuModel();
		String moduleValue = getDbModule().getValue();
		if (WebUtil.getModuleEnabled(getDbModule())) {
			addRecentEntityMenu(entityModel, sessionScopeBean.getUser(), maxRecentEntities);
			addMenuItems(sessionScopeBean, entityModel);
			MenuItem searchMenuItem = new MenuItem();
			searchMenuItem.setValue(Messages.getString(MessageCodes.SEARCH_MENU_ITEM_LABEL));
			searchMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-search");
			searchMenuItem.setOnclick(getSearchMenuItemOnClick());
			searchMenuItem.setUrl("#");
			searchMenuItem.setId("searchMenuItem_" + moduleValue);
			entityModel.addMenuItem(searchMenuItem);
			addNewEntityMenuItem(entityModel);
		}
		MenuItem portalMenuItem = new MenuItem();
		portalMenuItem.setValue(Messages.getString(MessageCodes.PORTAL_MENU_ITEM_LABEL));
		portalMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-home");
		portalMenuItem.setOnclick("openPortal()");
		portalMenuItem.setUrl("#");
		portalMenuItem.setId(RecentEntityMenuBase.PORTAL_MENU_ITEM_ID_PREFIX + moduleValue);
		entityModel.addMenuItem(portalMenuItem);
		return entityModel;
	}



	protected abstract String getSearchMenuItemOnClick();
}
