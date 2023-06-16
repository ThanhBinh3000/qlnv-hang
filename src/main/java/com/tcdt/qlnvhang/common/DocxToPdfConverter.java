package com.tcdt.qlnvhang.common;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdrPreview;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;

import javax.transaction.Transactional;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class DocxToPdfConverter {

    @Autowired
    private Environment env;

    @Transactional
    public ReportTemplateResponse convertDocxToPdf(InputStream inputFile, Map<String, String> fieldValues, List<String>  tableValues) {
        try
        {
            ByteArrayOutputStream outputStreamPdf = new ByteArrayOutputStream();
            ByteArrayOutputStream outputStreamWord = new ByteArrayOutputStream();
            ReportTemplateResponse reportTemplateResponse = new ReportTemplateResponse();
            XWPFDocument document = new XWPFDocument( inputFile );
            for (XWPFParagraph p : document.getParagraphs()) {
                replace2(p, fieldValues);
            }
            for (XWPFTable tbl : document.getTables()) {
                List<XWPFTableRow> rows = new ArrayList<>(tbl.getRows());
                for (XWPFTableRow row : rows) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph p : cell.getParagraphs()) {
                            replaceTable(tbl, row, p, tableValues, fieldValues);
                        }
                    }
                }
            }
            document.write(outputStreamWord);
            PdfOptions options = PdfOptions.create();
            options.fontProvider((familyName, encoding, size, style, color) -> {
                try {
                    BaseFont baseFont = BaseFont.createFont(env.getProperty("path.url"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    return new Font(baseFont, size, style, color);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Font was not found" + e);
                }
            });
            PdfConverter.getInstance().convert( document, outputStreamPdf, options );
            byte[] pdfBytes = outputStreamPdf.toByteArray();
            byte[] wordBytes = outputStreamWord.toByteArray();
            reportTemplateResponse.setPdfSrc(convertToBase64(pdfBytes));
            reportTemplateResponse.setWordSrc(convertToBase64(wordBytes));
            return reportTemplateResponse;
        }
        catch ( Throwable e )
        {
            e.printStackTrace();
        }
        return null;
    }

    private void replace2(XWPFParagraph p, Map<String, String> data) {
        String pText = p.getText();
        if (pText.contains("${")) {
            TreeMap<Integer, XWPFRun> posRuns = getPosToRuns(p);
            Pattern pat = Pattern.compile("\\$\\{(.+?)\\}");
            Matcher m = pat.matcher(pText);
            while (m.find()) {
                String g = m.group(1);
                int s = m.start(1);
                int e = m.end(1);
                String key = g;
                String x = data.get(key);
                if (x == null)
                    x = "";
                SortedMap<Integer, XWPFRun> range = posRuns.subMap(s - 2, true, e + 1, true); // get runs which contain the pattern
                boolean found1 = false;
                boolean found2 = false;
                boolean found3 = false;
                XWPFRun prevRun = null;
                XWPFRun found2Run = null;
                int found2Pos = -1;
                for (XWPFRun r : range.values())
                {
                    if (r == prevRun)
                        continue;
                    if (found3)
                        break;
                    prevRun = r;
                    for (int k = 0;; k++) {
                        if (found3)
                            break;
                        String txt = null;
                        try {
                            txt = r.getText(k);
                        } catch (Exception ex) {

                        }
                        if (txt == null)
                            break;
                        if (txt.contains("$") && !found1) {
                            txt = txt.replaceFirst("\\$", x);
                            found1 = true;
                        }
                        if (txt.contains("{") && !found2 && found1) {
                            found2Run = r;
                            found2Pos = txt.indexOf('{');
                            txt = txt.replaceFirst("\\{", "");
                            found2 = true;
                        }
                        if (found1 && found2 && !found3) {
                            if (txt.contains("}"))
                            {
                                if (r == found2Run)
                                {
                                    txt = txt.substring(0, found2Pos)+txt.substring(txt.indexOf('}'));
                                }
                                else
                                    txt = txt.substring(txt.indexOf('}'));
                            }
                            else if (r == found2Run)
                                txt = txt.substring(0,  found2Pos);
                            else
                                txt = "";
                        }
                        if (txt.contains("}") && !found3) {
                            txt = txt.replaceFirst("\\}", "");
                            found3 = true;
                        }
                        r.setText(txt, k);
                    }
                }
            }

        }

    }

    private void replaceTable(XWPFTable tbl, XWPFTableRow row, XWPFParagraph p, List<String> data, Map<String, String> values) throws Exception {
        String pText = p.getText();
        int rowIndex = tbl.getRows().size() - 1;
        if (pText.contains("${loop")) {
            Pattern pat = Pattern.compile("\\$\\{(.+?)\\}");
            Matcher m = pat.matcher(pText);
            while (m.find()) {
                String g = m.group(1);
                String key = g;
                if (data.size() > 0) {
                    for (int i = 0; i < data.size(); i++) {
                        List<String>  str = new ArrayList<>(Arrays.asList(data.get(i).split(",")));
                        XWPFTableRow newRow = tbl.insertNewTableRow(tbl.getRows().indexOf(row) + i);
                        for (int j = 0; j < row.getTableCells().size(); j++) {
                            newRow.createCell();
                            if (j >= 0 && j < str.size()) {
                                newRow.getCell(j).setText(!str.get(j).equals("null") ? str.get(j) : " ");
                            }
                        }
                        newRow.getCtRow().setTrPr(row.getCtRow().getTrPr());
                    }
                    for (XWPFRun run : p.getRuns()) {
                        String text = run.getText(0);
                        if (text != null && text.contains(key)) {
                            // Xóa dòng nếu key được tìm thấy
                            tbl.removeRow(rowIndex);
                            break;
                        }
                    }
                }
            }
        }else{
            replace2(p, values);
        }
    }

    public List<String> convertDataReplaceToTable(List<?> data){
        List<String> tableValues = new ArrayList<>(data.size());
        for (Object res : data) {
            tableValues.add(res.toString());
        }
        System.out.println(tableValues);
        return tableValues;
    }


    private TreeMap<Integer, XWPFRun> getPosToRuns(XWPFParagraph paragraph) {
        int pos = 0;
        TreeMap<Integer, XWPFRun> map = new TreeMap<Integer, XWPFRun>();
        for (XWPFRun run : paragraph.getRuns()) {
            String runText = run.text();
            if (runText != null && runText.length() > 0) {
                for (int i = 0; i < runText.length(); i++) {
                    map.put(pos + i, run);
                }
                pos += runText.length();
            }

        }
        return map;
    }
    public BigDecimal convertNullToZero(BigDecimal number) {
        return number != null ? number : BigDecimal.ZERO;
    }

    public Integer convertNullToZero(Integer number) {
        return number != null ? number : 0;
    }

    public Map<String, String> convertObjectToMap(Object object) {
        return Arrays.stream(object.getClass().getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .filter(field -> {
                    try {
                        return field.get(object) != null && !field.getName().equals("fileDinhKems");
                    } catch (IllegalAccessException e) {
                        return false;
                    }
                })
                .collect(Collectors.toMap(Field::getName, field -> {
                    try {
                        return field.get(object).toString();
                    } catch (IllegalAccessException e) {
                        return null;
                    }
                }));
    }

    private List<String> convertListToString(List<Object> list) {
        return list.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    public String convertToBase64(byte[] byteArray) throws Exception {
//        byte[] byteArray = outputStream.toByteArray();
        String base64String = Base64.getEncoder().encodeToString(byteArray);
        System.out.println(base64String);
        return base64String;
    }

    public String convertListStringToString(List<String> data){
        StringBuilder res = new StringBuilder();
        for (String item : data) {
            res.append(item).append("\n");
        }
        return res.toString();
    }

    public String convertListBigDecimal(List<BigDecimal> data){
        StringBuilder res = new StringBuilder();
        for (BigDecimal item : data) {
            res.append(item).append("\n");
        }
        return res.toString();
    }

}

