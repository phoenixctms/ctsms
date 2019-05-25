package org.phoenixctms.ctsms.web.model.trial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.web.model.LazyDataModelBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;

public abstract class ProbandMoneyTransferSummaryLazyModelBase<T> extends LazyDataModelBase<T> {

	protected Long trialId;
	private Collection<String> costTypesTruncated;
	private Collection<String> costTypes;
	private boolean isCostTypeColumnsTruncated;
	private ArrayList<SelectItem> truncatedCostTypes;
	private Long truncatedCostTypeIndex;
	protected Boolean paid;

	protected ProbandMoneyTransferSummaryLazyModelBase() {
		super();
		initSets();
	}

	public void clearSelectedColumns() {
		truncatedCostTypeIndex = null;
	}

	public Collection<String> getCostTypes() {
		return costTypes;
	}

	public Collection<String> getCostTypesTruncated() {
		return costTypesTruncated;
	}

	public Boolean getPaid() {
		return paid;
	}

	public Long getTrialId() {
		return trialId;
	}

	public Long getTruncatedCostTypeIndex() {
		return truncatedCostTypeIndex;
	}

	public ArrayList<SelectItem> getTruncatedCostTypes() {
		return truncatedCostTypes;
	}

	private void initSets() {
		costTypesTruncated = null;
		isCostTypeColumnsTruncated = false;
		truncatedCostTypes = null;
		if (costTypes == null) {
			costTypes = new ArrayList<String>();
			costTypesTruncated = costTypes;
			truncatedCostTypes = new ArrayList<SelectItem>();
			truncatedCostTypeIndex = null;
		} else {
			Long limit = Settings.getLongNullable(SettingCodes.MAX_COST_TYPES_COLUMNS, Bundle.SETTINGS, DefaultSettings.MAX_COST_TYPES_COLUMNS);
			truncatedCostTypes = new ArrayList<SelectItem>();
			if (limit != null && costTypes.size() > limit) {
				costTypesTruncated = new ArrayList<String>();
				Iterator<String> it = costTypes.iterator();
				Long index = 0l;
				while (it.hasNext()) {
					if ((costTypesTruncated.size() + 1) < limit) {
						costTypesTruncated.add(it.next());
					} else {
						if (truncatedCostTypeIndex == null) {
							truncatedCostTypeIndex = index;
						}
						truncatedCostTypes.add(new SelectItem(index.toString(), it.next()));
					}
					index++;
				}
				isCostTypeColumnsTruncated = true;
			} else {
				costTypesTruncated = costTypes;
				truncatedCostTypeIndex = null;
			}
		}
	}

	public boolean isCostTypeColumnsTruncated() {
		return isCostTypeColumnsTruncated;
	}

	public void setCostTypes(Collection<String> costTypes) {
		this.costTypes = costTypes;
		initSets();
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
		initSets();
	}

	public void setTrialId(Long trialId) {
		this.trialId = trialId;
		initSets();
	}

	public void setTruncatedCostTypeIndex(Long truncatedCostTypeIndex) {
		this.truncatedCostTypeIndex = truncatedCostTypeIndex;
	}

	@Override
	public void updateRowCount() {
		super.updateRowCount();
		initSets();
	}
}
