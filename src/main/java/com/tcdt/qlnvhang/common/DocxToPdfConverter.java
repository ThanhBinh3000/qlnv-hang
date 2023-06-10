package com.tcdt.qlnvhang.common;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;

import javax.transaction.Transactional;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DocxToPdfConverter {

    private Environment env;

    @Transactional
    public void convertDocxToPdf(InputStream inputFile, Map<String, String> fieldValues) {
        long startTime = System.currentTimeMillis();
        try
        {
            OutputStream outputStream = new ByteArrayOutputStream();
            OutputStream out = new ByteArrayOutputStream();
//            List<String> tenCanCuPhapLy = new ArrayList<>();
//            tenCanCuPhapLy.add("Chuỗi 1");
//            tenCanCuPhapLy.add("Chuỗi 2");
//            tenCanCuPhapLy.add("Chuỗi 3");
//
//            StringBuilder tenCanCuPhapLyText = new StringBuilder();
//            for (String ten : tenCanCuPhapLy) {
//                tenCanCuPhapLyText.append("- ").append(ten).append("\n");
//            }
//            String tenCanCuPhapLyTextString = tenCanCuPhapLyText.toString();
//            fieldValues.put("tenDvi", "Tên Đơn vị");
//            fieldValues.put("tongMucDt", "Tổng mức đề tài");
//            fieldValues.put("tenCanCuPhapLy", tenCanCuPhapLyTextString);
//            FileInputStream inputStream = new FileInputStream("/Users/hoangmanhhai/Documents/test.docx");
            // 1) Load docx with POI XWPFDocument
            XWPFDocument document = new XWPFDocument( inputFile );
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                for (XWPFRun run : paragraph.getRuns()) {
                    String text = run.getText(0);

                    if (text != null) {
                        for (Map.Entry<String, String> entry : fieldValues.entrySet()) {
                            String key = "${" + entry.getKey() + "}";
                            String value = entry.getValue();
                            text = text.replace(key, value);
                        }
                        run.setText(text, 0);
                    }
                }
            }
            document.write(outputStream);
//            File outFile = new File( "target/DocxResume.pdf" );
//            outFile.getParentFile().mkdirs();

//            OutputStream out = new FileOutputStream( outFile );
            PdfOptions options = PdfOptions.create();
            options.fontProvider((familyName, encoding, size, style, color) -> {
                try {
                    BaseFont baseFont = BaseFont.createFont(env.getProperty("path.url"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    return new Font(baseFont, size, style, color);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Font was not found" + e);
                }
            });
            PdfConverter.getInstance().convert( document, out, options );
        }
        catch ( Throwable e )
        {
            e.printStackTrace();
        }

        System.out.println( "Generate DocxResume.pdf with " + ( System.currentTimeMillis() - startTime ) + " ms." );
    }

    public Map<String, String> convertObjectToMap(Object object) {
        return Arrays.stream(object.getClass().getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .filter(field -> {
                    try {
                        return field.get(object) != null;
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

    public String convertToBase64(String filePath) throws Exception {
        Path pdfPath = Paths.get(filePath);
        byte[] pdfBytes = Files.readAllBytes(pdfPath);
        String base64String = Base64.getEncoder().encodeToString(pdfBytes);
        System.out.println(base64String);
        return base64String;
    }

}

