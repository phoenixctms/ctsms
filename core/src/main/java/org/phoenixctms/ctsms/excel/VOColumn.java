package org.phoenixctms.ctsms.excel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;

import org.phoenixctms.ctsms.compare.VOIDComparator;
import org.phoenixctms.ctsms.util.Accessor;
import org.phoenixctms.ctsms.util.GraphEnumerator;
import org.phoenixctms.ctsms.util.MethodTransfilter;
import org.phoenixctms.ctsms.util.OmittedFields;

public class VOColumn extends GraphEnumerator {

	private final static MethodTransfilter EXCEL_VO_METHOD_TRANSFILTER = MethodTransfilter.getVoMethodTransfilter(ExcelUtil.COLUMN_NAME_LOWER_CASE_FIELD_NAMES);
	private final static Comparator EXCEL_VO_COLLECTION_VALUES_COMPARATOR = new VOIDComparator(false);

	public static void appendColumns(ArrayList<VOColumn> columns, Class vo, HashMap<Integer, WritableCellFormat> rowCellFormats, int depth,
			boolean omitFields,
			boolean enumerateReferences,
			boolean enumerateCollections,
			boolean enumerateMaps) throws Exception {
		ArrayList<Accessor> getterChain = new ArrayList<Accessor>();
		getColumns(columns, vo, getterChain, rowCellFormats, depth, omitFields, enumerateReferences, enumerateCollections, enumerateMaps);
	}

	private static void getColumns(ArrayList<VOColumn> columns, Class vo, ArrayList<Accessor> getterChain, HashMap<Integer, WritableCellFormat> rowCellFormats, int depth,
			boolean omitFields,
			boolean enumerateReferences,
			boolean enumerateCollections,
			boolean enumerateMaps) throws Exception {
		appendProperties(org.phoenixctms.ctsms.excel.VOColumn.class, columns, vo, getterChain, rowCellFormats, depth,
				EXCEL_VO_METHOD_TRANSFILTER,
				EXCEL_VO_COLLECTION_VALUES_COMPARATOR,
				omitFields,
				null,
				enumerateReferences,
				enumerateCollections,
				enumerateMaps,
				ExcelUtil.COLUMN_NAME_ASSOCIATION_PATH_SEPARATOR);
	}

	public static ArrayList<VOColumn> getColumns(Class vo, HashMap<Integer, WritableCellFormat> rowCellFormats, int depth,
			boolean omitFields,
			boolean enumerateReferences,
			boolean enumerateCollections,
			boolean enumerateMaps) throws Exception {
		ArrayList<VOColumn> columns = new ArrayList<VOColumn>();
		ArrayList<Accessor> getterChain = new ArrayList<Accessor>();
		getColumns(columns, vo, getterChain, rowCellFormats, depth, omitFields, enumerateReferences, enumerateCollections, enumerateMaps);
		return columns;
	}

	public String getColumnName() throws Exception {
		return getAssociationPaths(null).get(0);
	}

	@Override
	protected boolean isFieldOmitted(Class graph, String field) {
		return OmittedFields.isOmitted(graph, field);
	}

	@Override
	protected boolean isTerminalType(Object passThrough) {
		return ExcelUtil.isWriteableType(returnType, (HashMap<String, WritableCellFormat>) passThrough);
	}

	public void writeCell(WritableSheet spreadSheet, int c, int r, Object vo, ExcelCellFormat f, HashMap<String, WritableCellFormat> cellFormats) throws Exception {
		ExcelUtil.writeCell(spreadSheet, c, r, returnType, getValueOf(vo, null), f, cellFormats);
	}

	public void writeHead(WritableSheet spreadSheet, int c, int r, String columnTitle, ExcelCellFormat f, HashMap<String, WritableCellFormat> cellFormats) throws Exception {
		ExcelUtil.writeHead(spreadSheet, c, r, columnTitle, f, cellFormats);
	}
}
