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

public abstract class DynamicEntityMenu extends RecentEntityMenuBase {

	public static DynamicEntityMenu getCourseEntityMenu() {
		return (new DynamicEntityMenu() {

			@Override
			protected DBModule getDbModule() {
				return DBModule.COURSE_DB;
			}

			@Override
			protected String getEntityHomeMenuItemLabel() {
				return Messages.getString(MessageCodes.COURSE_HOME_MENU_ITEM_LABEL);
			}

			@Override
			protected String getEntityHomeMenuItemOnClick() {
				return "openCourseHome()";
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
			protected boolean hasEntity(JournalEntryOutVO journalEntry) {
				return journalEntry.getCourse() != null;
			}
		});
	}

	public static DynamicEntityMenu getInputFieldEntityMenu() {
		return (new DynamicEntityMenu() {

			@Override
			protected DBModule getDbModule() {
				return DBModule.INPUT_FIELD_DB;
			}

			@Override
			protected String getEntityHomeMenuItemLabel() {
				return Messages.getString(MessageCodes.INPUT_FIELD_HOME_MENU_ITEM_LABEL);
			}

			@Override
			protected String getEntityHomeMenuItemOnClick() {
				return "openInputFieldHome()";
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
			protected boolean hasEntity(JournalEntryOutVO journalEntry) {
				return journalEntry.getInputField() != null;
			}
		});
	}

	public static DynamicEntityMenu getInventoryEntityMenu() {
		return (new DynamicEntityMenu() {

			@Override
			protected DBModule getDbModule() {
				return DBModule.INVENTORY_DB;
			}

			@Override
			protected String getEntityHomeMenuItemLabel() {
				return Messages.getString(MessageCodes.INVENTORY_HOME_MENU_ITEM_LABEL);
			}

			@Override
			protected String getEntityHomeMenuItemOnClick() {
				return "openInventoryHome()";
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
			protected boolean hasEntity(JournalEntryOutVO journalEntry) {
				return journalEntry.getInventory() != null;
			}
		});
	}

	public static DynamicEntityMenu getMassMailEntityMenu() {
		return (new DynamicEntityMenu() {

			@Override
			protected DBModule getDbModule() {
				return DBModule.MASS_MAIL_DB;
			}

			@Override
			protected String getEntityHomeMenuItemLabel() {
				return Messages.getString(MessageCodes.MASS_MAIL_HOME_MENU_ITEM_LABEL);
			}

			@Override
			protected String getEntityHomeMenuItemOnClick() {
				return "openMassMailHome()";
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
				return "openMassMail({0})";
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
			protected boolean hasEntity(JournalEntryOutVO journalEntry) {
				return journalEntry.getMassMail() != null;
			}
		});
	}

	public static DynamicEntityMenu getProbandEntityMenu() {
		return (new DynamicEntityMenu() {

			@Override
			protected DBModule getDbModule() {
				return DBModule.PROBAND_DB;
			}

			@Override
			protected String getEntityHomeMenuItemLabel() {
				return Messages.getString(MessageCodes.PROBAND_HOME_MENU_ITEM_LABEL);
			}

			@Override
			protected String getEntityHomeMenuItemOnClick() {
				return "openProbandHome()";
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
			protected boolean hasEntity(JournalEntryOutVO journalEntry) {
				return journalEntry.getProband() != null;
			}
		});
	}

	public static DynamicEntityMenu getStaffEntityMenu() {
		return (new DynamicEntityMenu() {

			@Override
			protected void addMenuItems(SessionScopeBean sessionScopeBean, MenuModel entityModel) {
				if (entityModel != null) {
					Submenu subMenu = addSubMenu(sessionScopeBean, entityModel);
					if (sessionScopeBean != null) {
						subMenu.getChildren().add(getDutyRosterScheduleMenuItem());
						if (sessionScopeBean.getHasUserIdentity() && WebUtil.isStaffPerson(sessionScopeBean.getUserIdentity())) {
							Submenu myCoursesMenu = new Submenu();
							myCoursesMenu.setLabel(Messages.getString(MessageCodes.MY_COURSES_MENU_ITEM_LABEL));
							myCoursesMenu.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-mycourse");
							myCoursesMenu.setId("myCoursesMenu");
							subMenu.getChildren().add(myCoursesMenu);
							MenuItem myUpcomingCoursesMenuItem = new MenuItem();
							myUpcomingCoursesMenuItem.setValue(Messages.getString(MessageCodes.MY_UPCOMING_COURSES_MENU_ITEM_LABEL));
							myUpcomingCoursesMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-upcomingcourse");
							myUpcomingCoursesMenuItem.setOnclick("openUpcomingCourseOverview()");
							myUpcomingCoursesMenuItem.setUrl("#");
							myUpcomingCoursesMenuItem.setId("myUpcomingCoursesMenuItem");
							myCoursesMenu.getChildren().add(myUpcomingCoursesMenuItem);
							MenuItem myExpiringParticipationsMenuItem = new MenuItem();
							myExpiringParticipationsMenuItem.setValue(Messages.getString(MessageCodes.MY_EXPIRING_PARTICIPATIONS_MENU_ITEM_LABEL));
							myExpiringParticipationsMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-courseexpiration");
							myExpiringParticipationsMenuItem.setOnclick("openExpiringParticipationOverview()");
							myExpiringParticipationsMenuItem.setUrl("#");
							myExpiringParticipationsMenuItem.setId("myExpiringParticipationsMenuItem");
							myCoursesMenu.getChildren().add(myExpiringParticipationsMenuItem);
						}
					}
				}
			}

			@Override
			protected DBModule getDbModule() {
				return DBModule.STAFF_DB;
			}

			@Override
			protected String getEntityHomeMenuItemLabel() {
				return Messages.getString(MessageCodes.STAFF_HOME_MENU_ITEM_LABEL);
			}

			@Override
			protected String getEntityHomeMenuItemOnClick() {
				return "openStaffHome()";
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
			protected boolean hasEntity(JournalEntryOutVO journalEntry) {
				return journalEntry.getStaff() != null;
			}
		});
	}

	public static DynamicEntityMenu getTrialEntityMenu() {
		return (new DynamicEntityMenu() {

			@Override
			protected DBModule getDbModule() {
				return DBModule.TRIAL_DB;
			}

			@Override
			protected String getEntityHomeMenuItemLabel() {
				return Messages.getString(MessageCodes.TRIAL_HOME_MENU_ITEM_LABEL);
			}

			@Override
			protected String getEntityHomeMenuItemOnClick() {
				return "openTrialHome()";
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
			protected boolean hasEntity(JournalEntryOutVO journalEntry) {
				return journalEntry.getTrial() != null;
			}
		});
	}

	public static DynamicEntityMenu getUserEntityMenu() {
		return (new DynamicEntityMenu() {

			@Override
			protected DBModule getDbModule() {
				return DBModule.USER_DB;
			}

			@Override
			protected String getEntityHomeMenuItemLabel() {
				return Messages.getString(MessageCodes.USER_HOME_MENU_ITEM_LABEL);
			}

			@Override
			protected String getEntityHomeMenuItemOnClick() {
				return "openUserHome()";
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
			addNewEntityMenuItem(entityModel);
			MenuItem entityHomeMenuItem = new MenuItem();
			entityHomeMenuItem.setValue(getEntityHomeMenuItemLabel());
			entityHomeMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " " + getHomeIcon());
			entityHomeMenuItem.setOnclick(getEntityHomeMenuItemOnClick());
			entityHomeMenuItem.setUrl("#");
			entityHomeMenuItem.setId(ENTITY_HOME_MENU_ITEM_PREFIX + moduleValue);
			entityModel.addMenuItem(entityHomeMenuItem);
		} else {
			MenuItem portalMenuItem = new MenuItem();
			portalMenuItem.setValue(Messages.getString(MessageCodes.PORTAL_MENU_ITEM_LABEL));
			portalMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-home");
			portalMenuItem.setOnclick("openPortal()");
			portalMenuItem.setUrl("#");
			portalMenuItem.setId(RecentEntityMenuBase.PORTAL_MENU_ITEM_ID_PREFIX + moduleValue);
			entityModel.addMenuItem(portalMenuItem);
		}
		return entityModel;
	}

	protected abstract String getEntityHomeMenuItemLabel();

	protected abstract String getEntityHomeMenuItemOnClick();

}
