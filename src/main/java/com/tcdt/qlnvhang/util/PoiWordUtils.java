package com.tcdt.qlnvhang.util;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Create by IntelliJ Idea 2018.2
 *
 * @author: qyp Date: 2019-10-26 2:12
 */
public class PoiWordUtils {

	/**
	 * The first character of the placeholder
	 */
	public static final String PREFIX_FIRST = "$";

	/**
	 * The second character of the placeholder
	 */
	public static final String PREFIX_SECOND = "{";

	/**
	 * Prefix of placeholder
	 */
	public static final String PLACEHOLDER_PREFIX = PREFIX_FIRST + PREFIX_SECOND;

	/**
	 * Placeholder suffix
	 */
	public static final String PLACEHOLDER_END = "}";

	/**
	 * The unique mark that needs to dynamically add rows in the table
	 */
	public static final String addRowText = "tbAddRow:";

	public static final String addRowRepeatText = "tbAddRowRepeat:";

	/**
	 * The beginning of the placeholder in the table ${tbAddRow: For example,
	 * ${tbAddRow:tb1}
	 */
	public static final String addRowFlag = PLACEHOLDER_PREFIX + addRowText;

	/**
	 * The beginning of the placeholder in the table ${tbAddRowRepeat: For example,
	 * ${tbAddRowRepeat:0,2,0,1} Rows 0 to 2, and columns 0 to 1 are template styles
	 */
	public static final String addRowRepeatFlag = PLACEHOLDER_PREFIX + addRowRepeatText;

	/**
	 * The separator of the repeated matrix For example: ${tbAddRowRepeat:0,2,0,1}
	 * The separator is,
	 */
	public static final String tbRepeatMatrixSeparator = ",";

	/**
	 * Suffix of placeholder
	 */
	public static final String PLACEHOLDER_SUFFIX = "}";

	/**
	 * Prefix of picture placeholder
	 */
	public static final String PICTURE_PREFIX = PLACEHOLDER_PREFIX + "image:";

	/**
	 * Determine whether the current row needs to be added to the mark table
	 *
	 * @param row
	 * @return
	 */
	public static boolean isAddRow(XWPFTableRow row) {
		return isDynRow(row, addRowFlag);
	}

	/**
	 * Add repeating template dynamic rows (with multiple behavior templates)
	 * 
	 * @param row
	 * @return
	 */
	public static boolean isAddRowRepeat(XWPFTableRow row) {
		return isDynRow(row, addRowRepeatFlag);
	}

	private static boolean isDynRow(XWPFTableRow row, String dynFlag) {
		if (row == null) {
			return false;
		}
		List<XWPFTableCell> tableCells = row.getTableCells();
		if (tableCells != null) {
			XWPFTableCell cell = tableCells.get(0);
			if (cell != null) {
				String text = cell.getText();
				return text != null && text.startsWith(dynFlag);
			}
		}
		return false;
	}

	/**
	 * Get the value corresponding to the placeholder from the parameter map
	 *
	 * @param paramMap
	 * @param key
	 * @return
	 */
	public static Object getValueByPlaceholder(Map<String, Object> paramMap, String key) {
		if (paramMap != null) {
			if (key != null) {
				return paramMap.get(getKeyFromPlaceholder(key));
			}
		}
		return null;
	}

	/**
	 * Repeated row and column matrix after removing the placeholder
	 * 
	 * @param key placeholder
	 * @return {0,2,0,1}
	 */
	public static String getTbRepeatMatrix(String key) {
		Assert.notNull(key, "The placeholder is empty");
		String $1 = key.replaceAll(
				"\\" + PREFIX_FIRST + "\\" + PREFIX_SECOND + addRowRepeatText + "(.*:)(.*)" + "\\" + PLACEHOLDER_SUFFIX,
				"$2");
		return $1;
	}

	/**
	 * Get the key from the placeholder
	 *
	 * @return
	 */
	public static String getKeyFromPlaceholder(String placeholder) {
		return Optional.ofNullable(placeholder).map(p -> p.replaceAll("[\\$\\{\\}]", "")).get();
	}

	public static void main(String[] args) {
		String s = "${aa}";
		s = s.replaceAll(PLACEHOLDER_PREFIX + PLACEHOLDER_SUFFIX, "");
		System.out.println(s);
//        String keyFromPlaceholder = getKeyFromPlaceholder("${tbAddRow:tb1}");
//        System.out.println(keyFromPlaceholder);
	}

	/**
	 * Copy the style of the column and set the value
	 * 
	 * @param sourceCell
	 * @param targetCell
	 * @param text
	 */
	public static void copyCellAndSetValue(XWPFTableCell sourceCell, XWPFTableCell targetCell, String text) {
		// Paragraph attributes
		List<XWPFParagraph> sourceCellParagraphs = sourceCell.getParagraphs();
		if (sourceCellParagraphs == null || sourceCellParagraphs.size() <= 0) {
			return;
		}

		XWPFParagraph sourcePar = sourceCellParagraphs.get(0);

		List<XWPFParagraph> targetCellList = targetCell.getParagraphs();
		if (targetCellList == null || targetCellList.size() <= 0) {
			return;
		}

		XWPFParagraph targetPar = targetCell.getParagraphs().get(0);

		// Set the style of the paragraph
		targetPar.getCTP().setPPr(sourcePar.getCTP().getPPr());

		List<XWPFRun> sourceParRuns = sourcePar.getRuns();
		if (sourceParRuns != null && sourceParRuns.size() > 0) {
			// If there is run in the current cell
			List<XWPFRun> runs = targetPar.getRuns();
			Optional.ofNullable(runs).ifPresent(rs -> rs.stream().forEach(r -> r.setText("", 0)));
			if (runs != null && runs.size() > 0) {
				runs.get(0).setText(text, 0);
			} else {
				XWPFRun cellR = targetPar.createRun();
				cellR.setText(text, 0);
				// Set the style of the column and the style of the template
				targetCell.getCTTc().setTcPr(sourceCell.getCTTc().getTcPr());
			}
			setTypeface(sourcePar, targetPar);
		} else {
			// targetCell.setText(text);
			List<XWPFRun> runs = targetPar.getRuns();
			if (runs != null && runs.size() > 0) {
				runs.get(0).setText(text, 0);
			} else {
				XWPFRun newRun = targetPar.createRun();
				newRun.setText(text, 0);
			}
		}
	}

	/**
	 * Copy font
	 */
	private static void setTypeface(XWPFParagraph sourcePar, XWPFParagraph targetPar) {
		XWPFRun sourceRun = sourcePar.getRuns().get(0);
		String fontFamily = sourceRun.getFontFamily();
		// int fontSize = sourceRun.getFontSize();
		String color = sourceRun.getColor();
//        String fontName = sourceRun.getFontName();
		boolean bold = sourceRun.isBold();
		boolean italic = sourceRun.isItalic();
		int kerning = sourceRun.getKerning();
//        String style = sourcePar.getStyle();
		UnderlinePatterns underline = sourceRun.getUnderline();

		XWPFRun targetRun = targetPar.getRuns().get(0);
		targetRun.setFontFamily(fontFamily);
//        targetRun.setFontSize(fontSize == -1 ? 10 : fontSize);
		targetRun.setBold(bold);
		targetRun.setColor(color);
		targetRun.setItalic(italic);
		targetRun.setKerning(kerning);
		targetRun.setUnderline(underline);
		// targetRun.setFontSize(fontSize);
	}

	/**
	 * When judging the text contains $
	 * 
	 * @param text text
	 * @return includes return true, does not include return false
	 */
	public static boolean checkText(String text) {
		boolean check = false;
		if (text.indexOf(PLACEHOLDER_PREFIX) != -1) {
			check = true;
		}
		return check;
	}

	/**
	 * Get the regular expression for placeholder replacement
	 * 
	 * @return
	 */
	public static String getPlaceholderReg(String text) {
		return "\\" + PREFIX_FIRST + "\\" + PREFIX_SECOND + text + "\\" + PLACEHOLDER_SUFFIX;
	}

	public static String getDocKey(String mapKey) {
		return PLACEHOLDER_PREFIX + mapKey + PLACEHOLDER_SUFFIX;
	}

	/**
	 * Determine if the current placeholder is a picture placeholder
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isPicture(String text) {
		return text.startsWith(PICTURE_PREFIX);
	}

	/**
	 * Delete a row of columns
	 * 
	 * @param row
	 */
	public static void removeCells(XWPFTableRow row) {
		int size = row.getTableCells().size();
		try {
			for (int i = 0; i < size; i++) {
				row.removeCell(i);
			}
		} catch (Exception e) {

		}
	}
}
