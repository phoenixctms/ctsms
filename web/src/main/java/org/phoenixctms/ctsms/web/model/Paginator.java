package org.phoenixctms.ctsms.web.model;

import java.util.ArrayList;
import java.util.Iterator;

import javax.faces.model.SelectItem;

import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;

public abstract class Paginator {

	private ArrayList<SelectItem> pages;
	private ArrayList<SelectItem> pageSizes;
	private int pageCount;
	protected PSFVO psf;
	private int selectedPage;
	private int pageSize;
	private boolean showPagesMessage;
	private boolean isInputFieldForm;
	private boolean isShowNavigatorButtons;
	protected ManagedBeanBase bean;
	private int fieldsPerRow;
	// private int maxFieldsPerRow;
	// private ArrayList<SelectItem> fieldsPerRows;

	// private boolean pagesEnabled;
	public Paginator() {
		this(null);
	}

	public Paginator(ManagedBeanBase bean) {
		this.bean = bean;
		pages = new ArrayList<SelectItem>();
		pageSizes = new ArrayList<SelectItem>();
		updatePageSizes();
		psf = new PSFVO();
		selectedPage = 0;
		pageCount = 0;
		// fieldsPerRows = new ArrayList<SelectItem>();
		fieldsPerRow = getDefaultFieldsPerRow();
		// maxFieldsPerRow= getMaxFieldsPerRow();
		// fieldsPerRows.clear();
		showPagesMessage = true;
		isInputFieldForm = true;
		isShowNavigatorButtons = true;
		// pagesEnabled = false;
		// pagesLimit = Settings.getInt(SettingCodes.PAGINATOR_PAGES_LIMIT, Bundle.SETTINGS, DefaultSettings.PAGINATOR_PAGES_LIMIT);
		pageSize = getDefaultPageSize();
		psf.setUpdateRowCount(false);
	}

	protected abstract Long getCount(Long... ids);

	protected int getDefaultFieldsPerRow() {
		return 1;
	}

	protected abstract int getDefaultPageSize();

	public int getFieldsPerRow() {
		return fieldsPerRow;
	}

	public ArrayList<SelectItem> getFieldsPerRows() {
		ArrayList<SelectItem> fieldsPerRows = new ArrayList<SelectItem>();
		for (int i = 1; i <= Math.max(fieldsPerRow, getMaxFieldsPerRow()); i++) {
			fieldsPerRows.add(new SelectItem(Integer.toString(i), Messages.getMessage(MessageCodes.FIELDS_PER_ROW_LABEL, Integer.toString(i))));
		}
		return fieldsPerRows;
	}

	protected abstract String getFirstPageButtonLabel();

	// public long getItemsOnPage() {
	// if ((psf.getFirst() + psf.getPageSize()) > psf.getRowCount()) {
	// return psf.getRowCount() - psf.getFirst();
	// } else {
	// return psf.getPageSize();
	// }
	// }
	protected abstract String getLoadLabel();

	protected int getMaxFieldsPerRow() {
		return getDefaultFieldsPerRow();
	}

	public int getNumOfPages() {
		return pages.size();
	}

	protected abstract String getPageButtonLabel();

	protected String getPageButtonLabelSeriesIndex(int i, int pageSize) {
		return "";
	}

	public ArrayList<SelectItem> getPages() {
		return pages;
	}

	public int getPageSize() {
		return pageSize;
	}

	public ArrayList<SelectItem> getPageSizes() {
		return pageSizes;
	}

	protected abstract ArrayList<String> getPageSizeStrings();

	public PSFVO getPsf() {
		return psf;
	}

	public PSFVO getPsfCopy() {
		return getPsfCopy(true);
	}

	public PSFVO getPsfCopy(boolean updateRowCount) {
		PSFVO psfCopy = new PSFVO(psf);
		psfCopy.setUpdateRowCount(updateRowCount);
		return psfCopy;
	}

	public int getSelectedPage() {
		return selectedPage;
	}

	public void initPages(boolean resetSelectedPage, Long... ids) {
		initPages(getCount(ids), resetSelectedPage);
	}

	private void initPages(Long count, boolean resetSelectedPage) {
		pages.clear();
		if (pageSize > 0) {
			if (count != null && count > 0) {
				psf.setRowCount(count);
				psf.setPageSize(pageSize);
				pageCount = (int) Math.ceil((double) count / (double) pageSize);
				Long pageEnd;
				for (int i = 0; i < pageCount; i++) {
					pageEnd = (long) ((i + 1) * pageSize);
					if (pageEnd > count) {
						pageEnd = count;
					}
					pages.add(new SelectItem(Integer.toString(i), Messages.getMessage(i == 0 ? getFirstPageButtonLabel() : getPageButtonLabel(),
							Integer.toString(i * pageSize + 1), Long.toString(pageEnd), getPageButtonLabelSeriesIndex(i, pageSize))));
				}
			} else {
				pageCount = 0;
				psf.setRowCount(0l);
				psf.setPageSize(pageSize);
				pages.add(new SelectItem(Integer.toString(0), getLoadLabel()));
			}
		} else {
			pageCount = 0;
			psf.setRowCount(0l);
			psf.setPageSize(0);
			pages.add(new SelectItem(Integer.toString(0), getLoadLabel()));
		}
		if (resetSelectedPage || pageCount == 0) {
			selectedPage = 0;
		} else if (selectedPage >= pageCount) {
			selectedPage = pageCount - 1;
		}
		updatePSF();
	}

	public void initPages(PSFVO psf, boolean resetSelectedPage) {
		initPages(psf.getRowCount(), resetSelectedPage);
	}

	public boolean isFirstPage() {
		return selectedPage == 0;
	}

	public boolean isInputFieldForm() {
		return isInputFieldForm;
	}

	public boolean isLastPage() {
		return selectedPage == (pageCount - 1);
	}

	public boolean isPagesEnabled() {
		return false;
	}

	// public void setPagesEnabled(boolean pagesEnabled) {
	// this.pagesEnabled = pagesEnabled;
	// }
	public boolean isShowNavigatorButtons() {
		return isShowNavigatorButtons;
	}

	public boolean isShowPagesMessage() {
		return showPagesMessage;
	}

	public void setFieldsPerRow(int fieldsPerRow) {
		this.fieldsPerRow = fieldsPerRow;
	}

	public void setInputFieldForm(boolean isInputFieldForm) {
		this.isInputFieldForm = isInputFieldForm;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setSelectedPage(int selectedPage) {
		this.selectedPage = selectedPage;
		// try {
		// this.selectedPage = Integer.parseInt(selectedPage);
		// } catch (NumberFormatException e) {
		// System.out.println("cannot convert '" +selectedPage+"' to integer");
		// }
	}

	public void setShowNavigatorButtons(boolean isShowNavigatorButtons) {
		this.isShowNavigatorButtons = isShowNavigatorButtons;
	}

	public void setShowPagesMessage(boolean showPagesMessage) {
		this.showPagesMessage = showPagesMessage;
	}

	public void setToFirstPage() {
		selectedPage = 0;
	}

	public void setToLastPage() {
		selectedPage = pageCount - 1;
	}

	public void setToNextPage() {
		selectedPage = selectedPage < pageCount - 1 ? selectedPage + 1 : pageCount - 1;
	}

	public void setToPrevPage() {
		selectedPage = selectedPage > 0 ? selectedPage - 1 : 0;
	}

	private void updatePageSizes() {
		ArrayList<String> sizeStrings = getPageSizeStrings();
		pageSizes.clear();
		Iterator<String> it = sizeStrings.iterator();
		while (it.hasNext()) {
			String sizeString = it.next();
			pageSizes.add(new SelectItem(sizeString, sizeString));
		}
	}

	public void updatePSF() {
		psf.setFirst(selectedPage * psf.getPageSize());
	}
}
