package org.phoenixctms.ctsms.web.model.shared;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CommonUtil.EllipsisPlacement;
import org.phoenixctms.ctsms.vo.ActivityTagVO;
import org.phoenixctms.ctsms.vo.JournalEntryOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.web.component.tagcloud.DefaultTagCloudItem;
import org.phoenixctms.ctsms.web.component.tagcloud.DefaultTagCloudModel;
import org.phoenixctms.ctsms.web.component.tagcloud.TagCloudItem;
import org.phoenixctms.ctsms.web.model.RecentEntityMenuBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.GetParamNames;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.Urls;
import org.phoenixctms.ctsms.web.util.WebUtil;
import org.primefaces.component.menuitem.MenuItem;

public class PortalModuleItem {

	private static String getEntityUrl(Urls entityUrl, GetParamNames idParameter, long idValue) {
		StringBuilder url = new StringBuilder(entityUrl.toString());
		url.append("?");
		url.append(idParameter);
		url.append("=");
		url.append(idValue);
		return url.toString();
	}

	private String label;
	private String icon;
	private String description;
	private ArrayList<RecentEntityMenuBase> menus;
	private String onClick;
	private DefaultTagCloudModel tagModel;
	private JournalModule module;

	public PortalModuleItem(String label, String icon, String description, ArrayList<RecentEntityMenuBase> menus,
			String onClick, JournalModule module) {
		this.label = label;
		this.icon = icon;
		this.description = description;
		this.onClick = onClick;
		tagModel = new DefaultTagCloudModel();
		this.menus = menus;
		this.module = module;
	}

	public String getDescription() {
		return description;
	}

	public String getIcon() {
		return icon;
	}

	public String getLabel() {
		return label;
	}

	public List<MenuItem> getMenuItems() {
		return RecentEntityMenuBase.getMenuItems(menus);
	}

	public String getOnClick() {
		return onClick;
	}

	public DefaultTagCloudModel getTagModel() {
		return tagModel;
	}

	public void updateTagModel() {
		tagModel.clear();
		UserOutVO user = WebUtil.getUser();
		Collection<ActivityTagVO> activityTags = null;
		try {
			activityTags = WebUtil
					.getServiceLocator()
					.getJournalService()
					.getActivityTags(WebUtil.getAuthentication(), module, null, null, user == null ? null : user.getDepartment().getId(),
							Settings.getIntNullable(SettingCodes.MAX_TAG_CLOUD_ITEMS, Bundle.SETTINGS, DefaultSettings.MAX_TAG_CLOUD_ITEMS),
							Settings.getBoolean(SettingCodes.LIMIT_JOURNAL_ENTRY_TAG_CLOUD, Bundle.SETTINGS, DefaultSettings.LIMIT_JOURNAL_ENTRY_TAG_CLOUD));
		} catch (ServiceException e) {
		} catch (AuthenticationException e) {
			WebUtil.publishException(e);
		} catch (AuthorisationException e) {
		} catch (IllegalArgumentException e) {
		}
		if (activityTags != null) {
			Iterator<ActivityTagVO> activityTagsIt = activityTags.iterator();
			Long maxCount = null;
			int labelLength = Settings.getInt(SettingCodes.TAG_CLOUD_ITEM_LABEL_LENGTH, Bundle.SETTINGS, DefaultSettings.TAG_CLOUD_ITEM_LABEL_LENGTH);
			while (activityTagsIt.hasNext()) {
				ActivityTagVO activityTag = activityTagsIt.next();
				JournalEntryOutVO journalEntry = activityTag.getJournalEntry();
				DefaultTagCloudItem tagCloudItem = new DefaultTagCloudItem();
				Long count = activityTag.getCount();
				switch (module) {
					case INVENTORY_JOURNAL:
						if (count != null && count > 0) {
							tagCloudItem.setLabel(CommonUtil.clipString(CommonUtil.inventoryOutVOToString(journalEntry.getInventory()), labelLength, CommonUtil.DEFAULT_ELLIPSIS,
									EllipsisPlacement.TRAILING));
							tagCloudItem.setOnclick(MessageFormat.format("openInventory({0})", Long.toString(journalEntry.getInventory().getId())));
							tagModel.addTag(tagCloudItem);
							maxCount = (maxCount == null ? activityTag.getCount() : Math.max(maxCount, count));
							tagCloudItem.setStrength(CommonUtil.safeLongToInt(count));
						}
						break;
					case STAFF_JOURNAL:
						if (count != null && count > 0) {
							tagCloudItem.setLabel(CommonUtil.clipString(CommonUtil.staffOutVOToString(journalEntry.getStaff()), labelLength, CommonUtil.DEFAULT_ELLIPSIS,
									EllipsisPlacement.TRAILING));
							tagCloudItem.setOnclick(MessageFormat.format("openStaff({0})", Long.toString(journalEntry.getStaff().getId())));
							tagModel.addTag(tagCloudItem);
							maxCount = (maxCount == null ? activityTag.getCount() : Math.max(maxCount, count));
							tagCloudItem.setStrength(CommonUtil.safeLongToInt(count));
						}
						break;
					case COURSE_JOURNAL:
						if (count != null && count > 0) {
							tagCloudItem.setLabel(CommonUtil.clipString(CommonUtil.courseOutVOToString(journalEntry.getCourse()), labelLength, CommonUtil.DEFAULT_ELLIPSIS,
									EllipsisPlacement.TRAILING));
							tagCloudItem.setOnclick(MessageFormat.format("openCourse({0})", Long.toString(journalEntry.getCourse().getId())));
							tagModel.addTag(tagCloudItem);
							maxCount = (maxCount == null ? activityTag.getCount() : Math.max(maxCount, count));
							tagCloudItem.setStrength(CommonUtil.safeLongToInt(count));
						}
						break;
					case TRIAL_JOURNAL:
						if (count != null && count > 0) {
							tagCloudItem.setLabel(CommonUtil.clipString(CommonUtil.trialOutVOToString(journalEntry.getTrial()), labelLength, CommonUtil.DEFAULT_ELLIPSIS,
									EllipsisPlacement.TRAILING));
							tagCloudItem.setOnclick(MessageFormat.format("openTrial({0})", Long.toString(journalEntry.getTrial().getId())));
							tagModel.addTag(tagCloudItem);
							maxCount = (maxCount == null ? activityTag.getCount() : Math.max(maxCount, count));
							tagCloudItem.setStrength(CommonUtil.safeLongToInt(count));
						}
						break;
					case PROBAND_JOURNAL:
						if (count != null && count > 0) {
							tagCloudItem.setLabel(CommonUtil.clipString(CommonUtil.probandOutVOToString(journalEntry.getProband()), labelLength, CommonUtil.DEFAULT_ELLIPSIS,
									EllipsisPlacement.TRAILING));
							tagCloudItem.setOnclick(MessageFormat.format("openProband({0})", Long.toString(journalEntry.getProband().getId())));
							tagModel.addTag(tagCloudItem);
							maxCount = (maxCount == null ? activityTag.getCount() : Math.max(maxCount, count));
							tagCloudItem.setStrength(CommonUtil.safeLongToInt(count));
						}
						break;
					case USER_JOURNAL:
						if (count != null && count > 0) {
							tagCloudItem.setLabel(CommonUtil.clipString(CommonUtil.userOutVOToString(journalEntry.getUser()), labelLength, CommonUtil.DEFAULT_ELLIPSIS,
									EllipsisPlacement.TRAILING));
							tagCloudItem.setOnclick(MessageFormat.format("openUser({0})", Long.toString(journalEntry.getUser().getId())));
							tagModel.addTag(tagCloudItem);
							maxCount = (maxCount == null ? activityTag.getCount() : Math.max(maxCount, count));
							tagCloudItem.setStrength(CommonUtil.safeLongToInt(count));
						}
						break;
					case INPUT_FIELD_JOURNAL:
						if (count != null && count > 0) {
							tagCloudItem.setLabel(CommonUtil.clipString(CommonUtil.inputFieldOutVOToString(journalEntry.getInputField()), labelLength, CommonUtil.DEFAULT_ELLIPSIS,
									EllipsisPlacement.TRAILING));
							tagCloudItem.setOnclick(MessageFormat.format("openInputField({0})", Long.toString(journalEntry.getInputField().getId())));
							tagModel.addTag(tagCloudItem);
							maxCount = (maxCount == null ? activityTag.getCount() : Math.max(maxCount, count));
							tagCloudItem.setStrength(CommonUtil.safeLongToInt(count));
						}
						break;
					default:
						break;
				}
			}
			if (maxCount != null && maxCount > 0) {
				Iterator<TagCloudItem> tagsIt = tagModel.getTags().iterator();
				Float scale = Settings.getFloat(SettingCodes.MAX_TAG_CLOUD_ITEM_STRENGTH, Bundle.SETTINGS, DefaultSettings.MAX_TAG_CLOUD_ITEM_STRENGTH)
						/ ((float) CommonUtil.safeLongToInt(maxCount));
				while (tagsIt.hasNext()) {
					TagCloudItem tagCloudItem = tagsIt.next();
					tagCloudItem.setStrength((int) Math.round(((float) tagCloudItem.getStrength()) * scale));
				}
			}
		}
	}
}
