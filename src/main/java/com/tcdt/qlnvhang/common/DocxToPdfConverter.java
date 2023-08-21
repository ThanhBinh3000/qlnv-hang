package com.tcdt.qlnvhang.common;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdrPreview;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.MathTool;
import org.apache.velocity.tools.generic.NumberTool;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class DocxToPdfConverter {

    @Autowired
    private Environment env;


    @Transactional
    public ReportTemplateResponse convertDocxToPdf(InputStream inputFile, Object data) {
        try
        {
            ByteArrayOutputStream outputStreamPdf = new ByteArrayOutputStream();
            ByteArrayOutputStream outputStreamWord = new ByteArrayOutputStream();
            ReportTemplateResponse reportTemplateResponse = new ReportTemplateResponse();
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(inputFile, TemplateEngineKind.Velocity);
            IContext context = report.createContext();
            context.put("data", data);
            context.put("numberTool", new NumberTool());
            context.put("dateTool", new DateTool());
            context.put("mathTool", new MathTool());
            context.put("locale",new Locale("vi", "VN"));
            report.process(context, outputStreamWord);
            Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.XWPF);
            PdfOptions pdfOptions = PdfOptions.create();
            pdfOptions.fontProvider((familyName, encoding, size, style, color) -> {
                try {
                    BaseFont baseFont = BaseFont.createFont(env.getProperty("path.url"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    return new Font(baseFont, size, style, color);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Font was not found" + e);
                }
            });
            options.subOptions(pdfOptions);
            report.convert(context, options, outputStreamPdf);
            byte[] pdfBytes = outputStreamPdf.toByteArray();
            byte[] wordBytes = outputStreamWord.toByteArray();
            reportTemplateResponse.setPdfSrc(convertToBase64(pdfBytes));
            reportTemplateResponse.setWordSrc(convertToBase64(wordBytes));
            outputStreamPdf.close();
            outputStreamWord.close();
            return reportTemplateResponse;
        }
        catch ( Throwable e )
        {
            e.printStackTrace();
        }
        return null;
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

    public String convertToBase64(byte[] byteArray) throws Exception {
//        byte[] byteArray = outputStream.toByteArray();
        String base64String = Base64.getEncoder().encodeToString(byteArray);
        System.out.println(base64String);
        return base64String;
    }

    public String convertBigDecimalToStr(BigDecimal num){
        if (num != null) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            DecimalFormat df = new DecimalFormat("#,##0.00", symbols);
            return df.format(num);
        }
        return "";
    }

    public String convertBigDecimalToStrNotDecimal(BigDecimal num){
        if (num != null) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            DecimalFormat df = new DecimalFormat("#,##0", symbols);
            return df.format(num);
        }
        return "";
    }
}

