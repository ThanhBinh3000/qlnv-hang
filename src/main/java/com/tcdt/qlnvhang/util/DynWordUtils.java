package com.tcdt.qlnvhang.util;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

/**
 * Create by IntelliJ Idea 2018.2
 *
 * @author: qyp Date: 2019-10-25 14:48
 */
public class DynWordUtils {

	private final Logger logger = LoggerFactory.getLogger(DynWordUtils.class);

	/**
	 * Paragraphs replaced by list are replaced by oldParagraph
	 */
	private XWPFParagraph oldParagraph;

	/**
	 * Parameters
	 */
	private Map<String, Object> paramMap;

	/**
	 * The position of the current element
	 */
	int n = 0;

	/**
	 * Determine whether the current table is traversed
	 */
	boolean isTable = false;

	/**
	 * Template object
	 */
	XWPFDocument templateDoc;

	/**
	 * Default font size
	 */
	final int DEFAULT_FONT_SIZE = 10;

	/**
	 * The row index where the placeholder of the repeat pattern is located
	 */
	private int currentRowIndex;

	/**
	 * Entrance
	 *
	 * @param paramMap     parameters used in the template
	 * @param templatePaht full path of the template
	 * @param outPath      The local full path where the generated file is stored
	 */
	public static void process(Map<String, Object> paramMap, String templatePaht, String outPath) {
		DynWordUtils dynWordUtils = new DynWordUtils();
		dynWordUtils.setParamMap(paramMap);
		dynWordUtils.createWord(templatePaht, outPath);
	}

	/**
	 * Generate dynamic word
	 * 
	 * @param templatePath
	 * @param outPath
	 */
	public void createWord(String templatePath, String outPath) {
		try (FileOutputStream outStream = new FileOutputStream(outPath)) {
			InputStream inputStream = new ClassPathResource(templatePath).getInputStream();
			templateDoc = new XWPFDocument(OPCPackage.open(inputStream));
			parseTemplateWord();
			templateDoc.write(outStream);
		} catch (Exception e) {
			StackTraceElement[] stackTrace = e.getStackTrace();

			String className = stackTrace[0].getClassName();
			String methodName = stackTrace[0].getMethodName();
			int lineNumber = stackTrace[0].getLineNumber();

			logger.error("Error: line: {}, class name: {}, method name: {}", lineNumber, className, methodName);
			throw new RuntimeException(e.getCause().getMessage());
		}
	}

	/**
	 * Parse word template
	 */
	public void parseTemplateWord() throws Exception {

		List<IBodyElement> elements = templateDoc.getBodyElements();

		for (; n < elements.size(); n++) {
			IBodyElement element = elements.get(n);
			// normal paragraph
			if (element instanceof XWPFParagraph) {

				XWPFParagraph paragraph = (XWPFParagraph) element;
				oldParagraph = paragraph;
				if (paragraph.getParagraphText().isEmpty()) {
					continue;
				}

				delParagraph(paragraph);

			} else if (element instanceof XWPFTable) {
				// form
				isTable = true;
				XWPFTable table = (XWPFTable) element;

				delTable(table, paramMap);
				isTable = false;
			}
		}

	}

	/**
	 * Process paragraph
	 */
	private void delParagraph(XWPFParagraph paragraph) throws Exception {
		List<XWPFRun> runs = oldParagraph.getRuns();
		StringBuilder sb = new StringBuilder();
		for (XWPFRun run : runs) {
			String text = run.getText(0);
			if (text == null) {
				continue;
			}
			sb.append(text);
			run.setText("", 0);
		}
		Placeholder(paragraph, runs, sb);
	}

	/**
	 * Match incoming information collection and template
	 *
	 * @param placeholder The area to be replaced in the template ()
	 * @param paramMap    incoming information collection
	 * @return template needs to replace the corresponding value of the regional
	 *         information collection
	 */
	public void changeValue(XWPFRun currRun, String placeholder, Map<String, Object> paramMap) throws Exception {

		String placeholderValue = placeholder;
		if (paramMap == null || paramMap.isEmpty()) {
			return;
		}

		Set<Map.Entry<String, Object>> textSets = paramMap.entrySet();
		for (Map.Entry<String, Object> textSet : textSets) {
			// Matching template and replacement value Format ${key}
			String mapKey = textSet.getKey();
			String docKey = PoiWordUtils.getDocKey(mapKey);

			if (placeholderValue.indexOf(docKey) != -1) {
				Object obj = textSet.getValue();
				// Need to add a list
				if (obj instanceof List) {
					placeholderValue = delDynList(placeholder, (List) obj);
				} else {
					placeholderValue = placeholderValue.replaceAll(PoiWordUtils.getPlaceholderReg(mapKey),
							String.valueOf(obj));
				}
			}
		}

		currRun.setText(placeholderValue, 0);
	}

	/**
	 * Processed dynamic paragraphs (parameter is list)
	 *
	 * @param placeholder paragraph placeholder
	 * @param obj
	 * @return
	 */
	private String delDynList(String placeholder, List obj) {
		String placeholderValue = placeholder;
		List dataList = obj;
		Collections.reverse(dataList);
		for (int i = 0, size = dataList.size(); i < size; i++) {
			Object text = dataList.get(i);
			// The placeholder line, no need to recreate a new line
			if (i == 0) {
				placeholderValue = String.valueOf(text);
			} else {
				XWPFParagraph paragraph = createParagraph(String.valueOf(text));
				if (paragraph != null) {
					oldParagraph = paragraph;
				}
				// After adding paragraphs, the size of the element of the doc document will
				// increase (add it above the current line
				// The subtraction operation here is to go back and parse the newly added rows
				// (because the new ones may have placeholders, here to support pictures and
				// tables)
				if (!isTable) {
					n--;
				}
			}
		}
		return placeholderValue;
	}

	/**
	 * Create paragraph
	 * <p>
	 * </p>
	 *
	 * @param texts
	 */
	public XWPFParagraph createParagraph(String... texts) {

		// Use the cursor to create a new row
		XmlCursor cursor = oldParagraph.getCTP().newCursor();
		XWPFParagraph newPar = templateDoc.insertNewParagraph(cursor);
		// Set paragraph style
		newPar.getCTP().setPPr(oldParagraph.getCTP().getPPr());

		copyParagraph(oldParagraph, newPar, texts);

		return newPar;
	}

	/**
	 * Processing table (traversal)
	 *
	 * @param table
	 * @param paramMap The collection of information to be replaced
	 */
	public void delTable(XWPFTable table, Map<String, Object> paramMap) throws Exception {
		List<XWPFTableRow> rows = table.getRows();
		for (int i = 0, size = rows.size(); i < size; i++) {
			XWPFTableRow row = rows.get(i);
			currentRowIndex = i;
			// If it is a dynamically added row, directly process it and terminate it
			if (delAndJudgeRow(table, paramMap, row)) {
				return;
			}
		}
	}

	/**
	 * Determine and whether it is a dynamic row, and process the table placeholder
	 * 
	 * @param table    table object
	 * @param paramMap parameter map
	 * @param row      current row
	 * @return
	 * @throws Exception
	 */
	private boolean delAndJudgeRow(XWPFTable table, Map<String, Object> paramMap, XWPFTableRow row) throws Exception {
		// The current line is a dynamic line flag
		if (PoiWordUtils.isAddRow(row)) {
			List<XWPFTableRow> xwpfTableRows = addAndGetRows(table, row, paramMap);
			// Trace back the added line, here is an attempt to process the dynamically
			// added image
			for (XWPFTableRow tbRow : xwpfTableRows) {
				delAndJudgeRow(table, paramMap, tbRow);
			}
			return true;
		}

		// If it is a line added repeatedly
		if (PoiWordUtils.isAddRowRepeat(row)) {
			List<XWPFTableRow> xwpfTableRows = addAndGetRepeatRows(table, row, paramMap);
			/*
			 * // Backtrack the added line, here is an attempt to process the dynamically
			 * added image for (XWPFTableRow tbRow : xwpfTableRows) { delAndJudgeRow(table,
			 * paramMap, tbRow); }
			 */
			return true;
		}
		// Current row non-dynamic row label
		List<XWPFTableCell> cells = row.getTableCells();
		for (XWPFTableCell cell : cells) {
			// Determine whether the cell needs to be replaced
			if (PoiWordUtils.checkText(cell.getText())) {
				List<XWPFParagraph> paragraphs = cell.getParagraphs();
				for (XWPFParagraph paragraph : paragraphs) {
					List<XWPFRun> runs = paragraph.getRuns();
					StringBuilder sb = new StringBuilder();
					for (XWPFRun run : runs) {
						sb.append(run.toString());
						run.setText("", 0);
					}
					Placeholder(paragraph, runs, sb);
				}
			}
		}
		return false;
	}

	/**
	 * Process placeholders
	 * 
	 * @param runs the runs of the current paragraph
	 * @param sb   The content of the current paragraph
	 * @throws Exception
	 */
	private void Placeholder(XWPFParagraph currentPar, List<XWPFRun> runs, StringBuilder sb) throws Exception {
		if (runs.size() > 0) {
			String text = sb.toString();
			XWPFRun currRun = runs.get(0);
			changeValue(currRun, text, paramMap);
		}
	}

	/**
	 * Add row The label row is not newly created
	 *
	 * @param table
	 * @param flagRow  flagRow table labeled rows
	 * @param paramMap parameters
	 */
	private List<XWPFTableRow> addAndGetRows(XWPFTable table, XWPFTableRow flagRow, Map<String, Object> paramMap)
			throws Exception {
		List<XWPFTableCell> flagRowCells = flagRow.getTableCells();
		XWPFTableCell flagCell = flagRowCells.get(0);

		String text = flagCell.getText();
		List<List<String>> dataList = (List<List<String>>) PoiWordUtils.getValueByPlaceholder(paramMap, text);

		// newly added line
		List<XWPFTableRow> newRows = new ArrayList<>(dataList.size());
		if (dataList == null || dataList.size() <= 0) {
			return newRows;
		}

		XWPFTableRow currentRow = flagRow;
		int cellSize = flagRow.getTableCells().size();
		for (int i = 0, size = dataList.size(); i < size; i++) {
			if (i != 0) {
				currentRow = table.createRow();
				// Copy style
				if (flagRow.getCtRow() != null) {
					currentRow.getCtRow().setTrPr(flagRow.getCtRow().getTrPr());
				}
			}
			addRow(flagCell, currentRow, cellSize, dataList.get(i));
			newRows.add(currentRow);
		}
		return newRows;
	}

	/**
	 * Add multiple repeated rows, dynamic rows, each row is newly created
	 * 
	 * @param table
	 * @param flagRow
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	private List<XWPFTableRow> addAndGetRepeatRows(XWPFTable table, XWPFTableRow flagRow, Map<String, Object> paramMap)
			throws Exception {
		List<XWPFTableCell> flagRowCells = flagRow.getTableCells();
		XWPFTableCell flagCell = flagRowCells.get(0);
		String text = flagCell.getText();
		List<List<String>> dataList = (List<List<String>>) PoiWordUtils.getValueByPlaceholder(paramMap, text);
		String tbRepeatMatrix = PoiWordUtils.getTbRepeatMatrix(text);
		Assert.notNull(tbRepeatMatrix, "The template matrix cannot be empty");

		// newly added line
		List<XWPFTableRow> newRows = new ArrayList<>(dataList.size());
		if (dataList == null || dataList.size() <= 0) {
			return newRows;
		}

		String[] split = tbRepeatMatrix.split(PoiWordUtils.tbRepeatMatrixSeparator);
		int startRow = Integer.parseInt(split[0]);
		int endRow = Integer.parseInt(split[1]);
		int startCell = Integer.parseInt(split[2]);
		int endCell = Integer.parseInt(split[3]);

		XWPFTableRow currentRow;
		for (int i = 0, size = dataList.size(); i < size; i++) {
			int flagRowIndex = i % (endRow - startRow + 1);
			XWPFTableRow repeatFlagRow = table.getRow(flagRowIndex);
			// Clear the placeholder line
			if (i == 0) {
				table.removeRow(currentRowIndex);
			}
			currentRow = table.createRow();
			// Copy style
			if (repeatFlagRow.getCtRow() != null) {
				currentRow.getCtRow().setTrPr(repeatFlagRow.getCtRow().getTrPr());
			}
			addRowRepeat(startCell, endCell, currentRow, repeatFlagRow, dataList.get(i));

			// Process the newly generated line if there is a placeholder to be processed
			delAndJudgeRow(table, paramMap, currentRow);

			newRows.add(currentRow);
		}
		deleteTemplateRow(startRow, endRow, table);
		return newRows;
	}

	/**
	 * Delete template line
	 */
	private void deleteTemplateRow(int startRowIdx, int endRowIdx, XWPFTable table) {
		for (; startRowIdx <= endRowIdx; startRowIdx++) {
			table.removeRow(0);
		}
	}

	/**
	 * Add new rows according to the template cell
	 *
	 * @param flagCell    template column (the cell that marks the placeholder)
	 * @param row         new row
	 * @param cellSize    The number of columns in each row (used to make up the
	 *                    column complement)
	 * @param rowDataList data for each row
	 */
	private void addRow(XWPFTableCell flagCell, XWPFTableRow row, int cellSize, List<String> rowDataList) {
		for (int i = 0; i < cellSize; i++) {
			XWPFTableCell cell = row.getCell(i);
			cell = cell == null ? row.createCell() : row.getCell(i);
			if (i < rowDataList.size()) {
				PoiWordUtils.copyCellAndSetValue(flagCell, cell, rowDataList.get(i));
			} else {
				// When the data is not full of the entire row, add an empty column
				PoiWordUtils.copyCellAndSetValue(flagCell, cell, "");
			}
		}
	}

	/**
	 * Add duplicate rows according to the template cell
	 * 
	 * @param startCell     The starting position of the template column
	 * @param endCell       The end position of the template column
	 * @param currentRow    creates a new row
	 * @param repeatFlagRow The row where the template column is located
	 * @param rowDataList   data for each row
	 */
	private void addRowRepeat(int startCell, int endCell, XWPFTableRow currentRow, XWPFTableRow repeatFlagRow,
			List<String> rowDataList) {
		int cellSize = repeatFlagRow.getTableCells().size();
		for (int i = 0; i < cellSize; i++) {
			XWPFTableCell cell = currentRow.getCell(i);
			cell = cell == null ? currentRow.createCell() : currentRow.getCell(i);
			int flagCellIndex = i % (endCell - startCell + 1);
			XWPFTableCell repeatFlagCell = repeatFlagRow.getCell(flagCellIndex);
			if (i < rowDataList.size()) {
				PoiWordUtils.copyCellAndSetValue(repeatFlagCell, cell, rowDataList.get(i));
			} else {
				// When the data is not full of the entire row, add an empty column
				PoiWordUtils.copyCellAndSetValue(repeatFlagCell, cell, "");
			}
		}
	}

	/**
	 * Copy paragraph
	 *
	 * @param sourcePar original paragraph
	 * @param targetPar
	 * @param texts
	 */
	private void copyParagraph(XWPFParagraph sourcePar, XWPFParagraph targetPar, String... texts) {

		targetPar.setAlignment(sourcePar.getAlignment());
		targetPar.setVerticalAlignment(sourcePar.getVerticalAlignment());

		// Set the layout
		targetPar.setAlignment(sourcePar.getAlignment());
		targetPar.setVerticalAlignment(sourcePar.getVerticalAlignment());

		if (texts != null && texts.length > 0) {
			String[] arr = texts;
			XWPFRun xwpfRun = sourcePar.getRuns().size() > 0 ? sourcePar.getRuns().get(0) : null;

			for (int i = 0, len = texts.length; i < len; i++) {
				String text = arr[i];
				XWPFRun run = targetPar.createRun();

				run.setText(text);

				run.setFontFamily(xwpfRun.getFontFamily());
				int fontSize = xwpfRun.getFontSize();
				run.setFontSize((fontSize == -1) ? DEFAULT_FONT_SIZE : fontSize);
				run.setBold(xwpfRun.isBold());
				run.setItalic(xwpfRun.isItalic());
			}
		}
	}

	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}
}