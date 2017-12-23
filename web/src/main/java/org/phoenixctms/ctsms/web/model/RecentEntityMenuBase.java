package org.phoenixctms.ctsms.web.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.faces.component.UIComponent;

import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CommonUtil.EllipsisPlacement;
import org.phoenixctms.ctsms.vo.JournalEntryOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.model.MenuModel;

public abstract class RecentEntityMenuBase {

	protected final static String PORTAL_MENU_ITEM_ID_PREFIX = "portalMenuItem_";
	protected final static String ENTITY_HOME_MENU_ITEM_PREFIX = "entityHomeMenuItem_";

	private static void findMenuItems(List<UIComponent> children, LinkedHashMap<String, MenuItem> menuItems) {
		if (children != null && children.size() > 0) {
			Iterator<UIComponent> it = children.iterator();
			while (it.hasNext()) {
				UIComponent component = it.next();
				if (component instanceof MenuItem) {
					MenuItem menuItem = (MenuItem) component;
					if (!menuItems.containsKey(menuItem.getId()) &&
							!menuItem.getId().startsWith(PORTAL_MENU_ITEM_ID_PREFIX) &&
							!menuItem.getId().startsWith(ENTITY_HOME_MENU_ITEM_PREFIX)) {
						menuItems.put(menuItem.getId(), menuItem);
					}
				} else if (component instanceof Submenu) {
					findMenuItems(((Submenu) component).getChildren(), menuItems);
				}
			}
		}
	}

	protected static MenuItem getDutyRosterScheduleMenuItem() {
		MenuItem dutyRosterScheduleMenuItem = new MenuItem();
		dutyRosterScheduleMenuItem.setValue(Messages.getString(MessageCodes.DUTY_ROSTER_SCHEDULE_MENU_ITEM_LABEL));
		dutyRosterScheduleMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-calendar " + WebUtil.MENU_BOLD_STYLECLASS);
		dutyRosterScheduleMenuItem.setOnclick("openDutyRosterSchedule()");
		dutyRosterScheduleMenuItem.setUrl("#");
		dutyRosterScheduleMenuItem.setId("dutyRosterScheduleMenuItem");
		return dutyRosterScheduleMenuItem;
	}

	public static List<MenuItem> getMenuItems(ArrayList<RecentEntityMenuBase> menus) {
		LinkedHashMap<String, MenuItem> menuItems = new LinkedHashMap<String, MenuItem>();
		if (menus != null && menus.size() > 0) {
			Iterator<RecentEntityMenuBase> it = menus.iterator();
			while (it.hasNext()) {
				findMenuItems(it.next().createMenuModel(WebUtil.getSessionScopeBean(), 0).getContents(), menuItems);
			}
		}
		return new ArrayList<MenuItem>(menuItems.values());
	}

	protected abstract void addMenuItems(SessionScopeBean sessionScopeBean, MenuModel entityModel);

	protected final void addNewEntityMenuItem(MenuModel entityModel) {
		if (entityModel != null) {
			String moduleValue = getDbModule().getValue();
			MenuItem newEntityMenuItem = new MenuItem();
			newEntityMenuItem.setValue(getOpenNewEntityMenuItemLabel()); // .getDisplayName(true,TimeZone.LONG,userLocale));
			newEntityMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-new");
			newEntityMenuItem.setOnclick(getOpenNewEntityJsName());
			newEntityMenuItem.setUrl("#");
			newEntityMenuItem.setId("newEntityMenuItem_" + moduleValue);
			entityModel.addMenuItem(newEntityMenuItem);
		}
	}

	protected final void addRecentEntityMenu(MenuModel entityModel, UserOutVO user, int maxRecentEntities) {
		if (entityModel != null && user != null && maxRecentEntities > 0) {
			String moduleValue = getDbModule().getValue();
			Collection<JournalEntryOutVO> journalEntryVOs = null;
			if (Settings.getBoolean(SettingCodes.ENABLE_RECENT_ENTITIES_MENU, Bundle.SETTINGS, DefaultSettings.ENABLE_RECENT_ENTITIES_MENU)) {
				try {
					journalEntryVOs = WebUtil.getServiceLocator().getJournalService()
							.getRecent(WebUtil.getAuthentication(), getJournalModule(), user.getId(), null, null, maxRecentEntities,
									Settings.getBoolean(SettingCodes.LIMIT_JOURNAL_ENTRY_RECENT, Bundle.SETTINGS, DefaultSettings.LIMIT_JOURNAL_ENTRY_RECENT));
				} catch (ServiceException e) {
				} catch (AuthenticationException e) {
					WebUtil.publishException(e);
				} catch (AuthorisationException e) {
				} catch (IllegalArgumentException e) {
				}
			}
			if (journalEntryVOs != null && journalEntryVOs.size() > 0) {
				Submenu recentEntitiesMenu = new Submenu();
				recentEntitiesMenu.setLabel(Messages.getString(MessageCodes.RECENT_ENTITIES_MENU_LABEL));
				recentEntitiesMenu.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " ctsms-icon-history");
				recentEntitiesMenu.setId("recentEntitiesMenu_" + moduleValue);
				entityModel.addSubmenu(recentEntitiesMenu);
				int menuItemLabelClipMaxLength = Settings.getInt(SettingCodes.MENU_ITEM_LABEL_CLIP_MAX_LENGTH, Bundle.SETTINGS, DefaultSettings.MENU_ITEM_LABEL_CLIP_MAX_LENGTH);
				int i = 0;
				Iterator<JournalEntryOutVO> it = journalEntryVOs.iterator();
				while (it.hasNext()) {
					JournalEntryOutVO journalEntryVO = it.next();
					if (hasEntity(journalEntryVO)) {
						MenuItem recentEntityMenuItem = new MenuItem();
						recentEntityMenuItem.setValue(CommonUtil.clipString(getRecentEntityMenuItemLabel(journalEntryVO), menuItemLabelClipMaxLength, CommonUtil.DEFAULT_ELLIPSIS,
								EllipsisPlacement.TRAILING)); // .getDisplayName(true,TimeZone.LONG,userLocale));
						recentEntityMenuItem.setOnclick(MessageFormat.format(getOpenEntityJsName(), Long.toString(getEntityId(journalEntryVO))));
						String iconClass = getEntityIcon(journalEntryVO);
						if (iconClass != null && iconClass.length() > 0) {
							recentEntityMenuItem.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " " + iconClass);
						}
						recentEntityMenuItem.setUrl("#");
						recentEntityMenuItem.setId("recentEntityMenuItem_" + Integer.toString(i) + "_" + moduleValue);
						recentEntitiesMenu.getChildren().add(recentEntityMenuItem);
						i++;
					}
				}
			}
		}
	}

	protected Submenu addSubMenu(SessionScopeBean sessionScopeBean, MenuModel entityModel) {
		String moduleValue = getDbModule().getValue();
		Submenu subMenu = new Submenu();
		subMenu.setIcon(WebUtil.MENUBAR_ICON_STYLECLASS + " " + getHomeIcon());
		subMenu.setLabel(Messages.getString(MessageCodes.HOME_SUBMENU_LABEL));
		subMenu.setId("subMenu_" + moduleValue);
		entityModel.addSubmenu(subMenu);
		return subMenu;
	}

	public abstract MenuModel createMenuModel(SessionScopeBean sessionScopeBean, int maxRecentEntities);

	protected abstract DBModule getDbModule();

	protected abstract String getEntityIcon(JournalEntryOutVO journalEntry);

	protected abstract long getEntityId(JournalEntryOutVO journalEntry);

	protected abstract String getHomeIcon();

	protected abstract JournalModule getJournalModule();

	protected abstract String getOpenEntityJsName();

	protected abstract String getOpenNewEntityJsName();

	protected abstract String getOpenNewEntityMenuItemLabel();

	protected abstract String getRecentEntityMenuItemLabel(JournalEntryOutVO journalEntry);

	protected abstract boolean hasEntity(JournalEntryOutVO journalEntry);

}
