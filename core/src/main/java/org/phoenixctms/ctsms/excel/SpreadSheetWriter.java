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
	private boolean rowColors;
	private ExcelCellFormat headFormat;
	private ExcelCellFormat rowFormat;
	private final static boolean ENUMERATE_REFERENCES = true;
	private final static boolean ENUMERATE_COLLECTIONS = false;
	private final static boolean ENUMERATE_MAPS = false;

	public SpreadSheetWriter(WorkbookWriter workbookWriter, HashMap<String, Integer> voFieldColumnIndexMap, int voGraphRecursionDepth, boolean omitFields, boolean autoSize,
			boolean writeHead, Integer pageBreakAtRow, boolean rowColors, ExcelCellFormat headFormat, ExcelCellFormat rowFormat) {
		voColumns = new ArrayList<VOColumn>();
		this.workbookWriter = workbookWriter;
		this.voFieldColumnIndexMap = voFieldColumnIndexMap;
		this.voGraphRecursionDepth = voGraphRecursionDepth;
		this.omitFields = omitFields;
		this.autoSize = autoSize;
		this.writeHead = writeHead;
		this.pageBreakAtRow = pageBreakAtRow;
		this.rowColors = rowColors;
		this.headFormat = headFormat;
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
					ExcelUtil.writeCell(spreadSheet, columnIndex.intValue(), r, fieldValue.getClass(), fieldValue, f, cellFormats, false);
				} else {
					ExcelUtil.writeCell(spreadSheet, columnIndex.intValue(), r, String.class, null, f, cellFormats, false);
				}
			}
		}
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
					column.writeHead(spreadSheet, columnIndex.intValue(), r, workbookWriter.getColumnTitle(column.getColumnName()), headFormat, cellFormats);
				}
				if (cellView != null) {
					spreadSheet.setColumnView(columnIndex.intValue(), cellView);
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
					ExcelUtil.writeHead(spreadSheet, columnIndex.intValue(), r, columnName, headFormat, cellFormats);
				}
				if (cellView != null) {
					spreadSheet.setColumnView(columnIndex.intValue(), cellView);
				}
			}
		}
		return maxIndexedColumnIndex;
	}

	public WritableSheet writeSpreadSheet(WritableWorkbook workbook) throws Exception {
		WritableSheet spreadSheet = workbook.getSheet(spreadSheetName); // The sheet with the specified name, or null if it is not found
		if (spreadSheet == null) {
			spreadSheet = workbook.createSheet(spreadSheetName, workbook.getNumberOfSheets());
		}
		HashMap<String, WritableCellFormat> cellFormats = new HashMap<String, WritableCellFormat>();
		int maxIndexedColumnIndex = writeHeadRow(spreadSheet, 0, cellFormats);
		int c;
		int r = writeHead ? 1 : 0;
		if (VOs != null && VOs.size() > 0) {
			Iterator vosIt = VOs.iterator();
			while (vosIt.hasNext()) {
				Object vo = vosIt.next();
				boolean newPage = ((pageBreakAtRow == null) ? false : (r > 0 && (r % pageBreakAtRow.intValue()) == 0));
				if (newPage) {
					spreadSheet.addRowPageBreak(r);
					writeHeadRow(spreadSheet, r, cellFormats);
					r++;
				}
				for (c = 0; c < voColumns.size(); c++) {
					VOColumn voColumn = voColumns.get(c);
					Integer columnIndex = getColumnIndex(voColumn.getColumnName());
					if (columnIndex != null) {
						if (rowColors) {
							rowFormat.setBgColor(workbookWriter.voToColor(vo));
						}
						voColumn.writeCell(spreadSheet, columnIndex.intValue(), r, vo, rowFormat, cellFormats);
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
				r++;
			}
		} else if (distinctFieldRows != null && distinctFieldRows.size() > 0) {
			Iterator<HashMap<String, Object>> fieldRowIt = distinctFieldRows.values().iterator();
			while (fieldRowIt.hasNext()) {
				boolean newPage = ((pageBreakAtRow == null) ? false : (r > 0 && (r % pageBreakAtRow.intValue()) == 0));
				if (newPage) {
					spreadSheet.addRowPageBreak(r);
					writeHeadRow(spreadSheet, r, cellFormats);
					r++;
				}
				if (rowColors) {
					rowFormat.setBgColor(null);
				}
				writeFieldRow(spreadSheet, fieldRowIt.next(), maxIndexedColumnIndex, r, rowFormat, cellFormats);
				r++;
			}
		}
		return spreadSheet;
	}
}
