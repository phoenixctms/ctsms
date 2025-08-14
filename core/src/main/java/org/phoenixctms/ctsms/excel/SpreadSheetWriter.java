package org.phoenixctms.ctsms.excel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.phoenixctms.ctsms.util.CommonUtil;

import jxl.CellView;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class SpreadSheetWriter {

	private WorkbookWriter workbookWriter;
	private HashMap<String, Integer> voFieldColumnIndexMap;
	private String spreadSheetName;
	private Collection VOs;
	private ArrayList<VOColumn> voColumns;
	private ArrayList<String> distinctColumnNames;
	private HashMap<Long, HashMap<String, Object>> distinctFieldRows;
	private int voGraphRecursionDepth;
	private boolean omitFields;
	private boolean autoSize;
	private boolean writeHead;
	private Integer pageBreakAtRow;
	private Integer rowOffsetFirstPage;
	private Integer rowOffsetOtherPages;
	private Integer colOffset;
	private boolean rowColors;
	private ExcelCellFormat headFormat;
	private ExcelCellFormat rowFormat;
	private ExcelCellFormat groupLabelFormat;
	private final static boolean ENUMERATE_REFERENCES = true;
	private final static boolean ENUMERATE_COLLECTIONS = false;
	private final static boolean ENUMERATE_MAPS = false;

	//	public SpreadSheetWriter(WorkbookWriter workbookWriter, HashMap<String, Integer> voFieldColumnIndexMap, int voGraphRecursionDepth, boolean omitFields, boolean autoSize,
	//			boolean writeHead, Integer pageBreakAtRow,
	//			boolean rowColors, ExcelCellFormat headFormat, ExcelCellFormat rowFormat) {
	//		this(workbookWriter, voFieldColumnIndexMap, voGraphRecursionDepth, omitFields, autoSize,
	//				writeHead, pageBreakAtRow,
	//				null,
	//				null,
	//				null,
	//				rowColors, headFormat, rowFormat);
	//	}
	public SpreadSheetWriter(WorkbookWriter workbookWriter, HashMap<String, Integer> voFieldColumnIndexMap, int voGraphRecursionDepth, boolean omitFields, boolean autoSize,
			boolean writeHead, Integer pageBreakAtRow,
			Integer rowOffsetFirstPage,
			Integer rowOffsetOtherPages,
			Integer colOffset,
			boolean rowColors, ExcelCellFormat headFormat, ExcelCellFormat groupLabelFormat, ExcelCellFormat rowFormat) {
		voColumns = new ArrayList<VOColumn>();
		this.workbookWriter = workbookWriter;
		this.voFieldColumnIndexMap = voFieldColumnIndexMap;
		this.voGraphRecursionDepth = voGraphRecursionDepth;
		this.omitFields = omitFields;
		this.autoSize = autoSize;
		this.writeHead = writeHead;
		this.pageBreakAtRow = pageBreakAtRow;
		this.rowOffsetFirstPage = rowOffsetFirstPage;
		this.rowOffsetOtherPages = rowOffsetOtherPages;
		this.colOffset = colOffset;
		this.rowColors = rowColors;
		this.headFormat = headFormat;
		this.groupLabelFormat = groupLabelFormat;
		this.rowFormat = rowFormat;
	}

	private Integer getColumnIndex(String columnName) throws Exception {
		if (voFieldColumnIndexMap != null) {
			return voFieldColumnIndexMap.get(columnName);
		}
		return null;
	}

	public ArrayList<String> getDistinctColumnNames() {
		return distinctColumnNames;
	}

	public HashMap<Long, HashMap<String, Object>> getDistinctFieldRows() {
		return distinctFieldRows;
	}

	public String getSpreadSheetName() {
		return spreadSheetName;
	}

	public Collection getVOs() {
		return VOs;
	}

	public void init() throws Exception {
		voColumns.clear();
		if (VOs != null && VOs.size() > 0) {
			VOColumn.appendColumns(voColumns, VOs.iterator().next().getClass(), new HashMap<Integer, WritableCellFormat>(), voGraphRecursionDepth, omitFields,
					ENUMERATE_REFERENCES,
					ENUMERATE_COLLECTIONS, ENUMERATE_MAPS);
		}
	}

	public void setDistinctColumnNames(ArrayList<String> distinctColumnNames) {
		this.distinctColumnNames = distinctColumnNames;
	}

	public void setDistinctFieldRows(
			HashMap<Long, HashMap<String, Object>> distinctFieldRows) {
		this.distinctFieldRows = distinctFieldRows;
	}

	public void setSpreadSheetName(String spreadSheetName) {
		this.spreadSheetName = spreadSheetName;
	}

	public void setVOs(Collection VOs) {
		this.VOs = VOs;
	}

	private int getColIndex(int c) {
		if (colOffset != null) {
			return c + colOffset;
		}
		return c;
	}

	private void writeFieldRow(WritableSheet spreadSheet, HashMap<String, Object> fieldRow, int maxIndexedColumnIndex, int r, ExcelCellFormat f,
			HashMap<String, WritableCellFormat> cellFormats) throws Exception {
		if (fieldRow != null && distinctColumnNames != null && distinctColumnNames.size() > 0) {
			int c = maxIndexedColumnIndex;
			for (int d = 0; d < distinctColumnNames.size(); d++) {
				String columnName = distinctColumnNames.get(d);
				Object fieldValue = fieldRow.get(columnName);
				Integer columnIndex = getColumnIndex(columnName);
				if (columnIndex == null) {
					c++;
					columnIndex = c;
				}
				if (fieldValue != null) {
					ExcelUtil.writeCell(spreadSheet, getColIndex(columnIndex.intValue()), r, fieldValue.getClass(), fieldValue, f, cellFormats, false);
				} else {
					ExcelUtil.writeCell(spreadSheet, getColIndex(columnIndex.intValue()), r, String.class, null, f, cellFormats, false);
				}
			}
		}
	}

	private int writeGroupLabels(WritableSheet spreadSheet, ArrayList<String> groupLabels, int r, ExcelCellFormat f,
			HashMap<String, WritableCellFormat> cellFormats) throws Exception {
		if (groupLabels != null) {
			Iterator<String> it = groupLabels.iterator();
			while (it.hasNext()) {
				ExcelUtil.writeGroupLabel(spreadSheet, getColIndex(0), r, it.next(), f, cellFormats);
				r++;
			}
			return groupLabels.size();
		}
		return 0;
	}

	private int writeHeadRow(WritableSheet spreadSheet, int r, HashMap<String, WritableCellFormat> cellFormats) throws Exception {
		CellView cellView = null;
		if (autoSize) {
			cellView = new CellView();
			cellView.setAutosize(true);
		}
		int c, d;
		int maxIndexedColumnIndex = -1;
		if (voFieldColumnIndexMap != null) {
			Iterator<Integer> it = voFieldColumnIndexMap.values().iterator();
			while (it.hasNext()) {
				maxIndexedColumnIndex = Math.max(maxIndexedColumnIndex, it.next().intValue());
			}
		}
		for (d = 0; d < voColumns.size(); d++) {
			VOColumn column = voColumns.get(d);
			Integer columnIndex = getColumnIndex(column.getColumnName());
			if (columnIndex != null) {
				if (writeHead) {
					column.writeHead(spreadSheet, getColIndex(columnIndex.intValue()), r, workbookWriter.getColumnTitle(column.getColumnName()), headFormat, cellFormats);
				}
				if (cellView != null) {
					spreadSheet.setColumnView(getColIndex(columnIndex.intValue()), cellView);
				}
			}
		}
		c = maxIndexedColumnIndex;
		if (distinctColumnNames != null && distinctColumnNames.size() > 0 && distinctFieldRows != null && distinctFieldRows.size() > 0) {
			for (d = 0; d < distinctColumnNames.size(); d++) {
				String columnName = distinctColumnNames.get(d);
				Integer columnIndex = getColumnIndex(columnName);
				if (columnIndex == null) {
					c++;
					columnIndex = c;
				}
				if (writeHead) {
					ExcelUtil.writeHead(spreadSheet, getColIndex(columnIndex.intValue()), r, columnName, headFormat, cellFormats);
				}
				if (cellView != null) {
					spreadSheet.setColumnView(getColIndex(columnIndex.intValue()), cellView);
				}
			}
		}
		return maxIndexedColumnIndex;
	}

	public WritableSheet writeSpreadSheet(WritableWorkbook workbook) throws Exception {
		WritableSheet spreadSheet = null;
		if (CommonUtil.isEmptyString(spreadSheetName)) {
			spreadSheet = workbook.getSheet(0);
		} else {
			spreadSheet = workbook.getSheet(spreadSheetName); // The sheet with the specified name, or null if it is not found
		}
		if (spreadSheet == null) {
			spreadSheet = workbook.createSheet(spreadSheetName, workbook.getNumberOfSheets());
		}
		HashMap<String, WritableCellFormat> cellFormats = new HashMap<String, WritableCellFormat>();
		int rPage = rowOffsetFirstPage != null ? rowOffsetFirstPage : 0;
		int r = rPage;
		int maxIndexedColumnIndex = writeHeadRow(spreadSheet, r, cellFormats);
		int c, increment;
		boolean newPage;
		r += writeHead ? 1 : 0;
		rPage += writeHead ? 1 : 0;
		if (VOs != null && VOs.size() > 0) {
			Iterator vosIt = VOs.iterator();
			Object lastVO = null;
			while (vosIt.hasNext()) {
				Object vo = vosIt.next();
				increment = writeGroupLabels(spreadSheet, workbookWriter.getGroupLabels(lastVO, vo), r, groupLabelFormat, cellFormats);
				r += increment;
				rPage += increment;
				lastVO = vo;
				newPage = (pageBreakAtRow == null) ? false : (rPage > 0 && rPage > pageBreakAtRow);
				if (newPage) {
					spreadSheet.addRowPageBreak(r);
					rPage = rowOffsetOtherPages != null ? rowOffsetOtherPages : 0;
					r += rPage;
					writeHeadRow(spreadSheet, r, cellFormats);
					r += 1;
					rPage += 1;
					increment = writeGroupLabels(spreadSheet, workbookWriter.getGroupLabels(null, vo), r, groupLabelFormat, cellFormats);
					r += increment;
					rPage += increment;
				}
				for (c = 0; c < voColumns.size(); c++) {
					VOColumn voColumn = voColumns.get(c);
					Integer columnIndex = getColumnIndex(voColumn.getColumnName());
					if (columnIndex != null) {
						if (rowColors) {
							rowFormat.setBgColor(workbookWriter.voToColor(vo));
						}
						voColumn.writeCell(spreadSheet, getColIndex(columnIndex.intValue()), r, vo, rowFormat, cellFormats);
					}
				}
				if (distinctFieldRows != null && distinctFieldRows.size() > 0) {
					Long id = CommonUtil.getVOId(vo);
					if (id != null && distinctFieldRows.containsKey(id)) {
						if (rowColors) {
							rowFormat.setBgColor(workbookWriter.voToColor(vo));
						}
						writeFieldRow(spreadSheet, distinctFieldRows.get(id), maxIndexedColumnIndex, r, rowFormat, cellFormats);
					}
				}
				r += 1;
				rPage += 1;
			}
		} else if (distinctFieldRows != null && distinctFieldRows.size() > 0) {
			Iterator<HashMap<String, Object>> fieldRowIt = distinctFieldRows.values().iterator();
			HashMap<String, Object> lastRow = null;
			while (fieldRowIt.hasNext()) {
				HashMap<String, Object> row = fieldRowIt.next();
				increment = writeGroupLabels(spreadSheet, workbookWriter.getGroupLabels(lastRow, row), r, groupLabelFormat, cellFormats);
				r += increment;
				rPage += increment;
				lastRow = row;
				newPage = (pageBreakAtRow == null) ? false : (rPage > 0 && rPage > pageBreakAtRow);
				if (newPage) {
					spreadSheet.addRowPageBreak(r);
					rPage = rowOffsetOtherPages != null ? rowOffsetOtherPages : 0;
					r += rPage;
					writeHeadRow(spreadSheet, r, cellFormats);
					r += 1;
					rPage += 1;
					increment = writeGroupLabels(spreadSheet, workbookWriter.getGroupLabels(null, row), r, groupLabelFormat, cellFormats);
					r += increment;
					rPage += increment;
				}
				if (rowColors) {
					rowFormat.setBgColor(null);
				}
				writeFieldRow(spreadSheet, row, maxIndexedColumnIndex, r, rowFormat, cellFormats);
				r += 1;
				rPage += 1;
			}
		}
		return spreadSheet;
	}
}
