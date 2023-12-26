package com.tcdt.qlnvhang.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.*;

import com.tcdt.qlnvhang.table.ReportTemplate;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;

import javax.servlet.http.HttpServletResponse;

public class ReportPrint {
    private Connection conn = null;
    Locale vietnameseLocale = new Locale("vi", "VN");
    HttpServletResponse response;
    ReportTemplate model;
    private Map params;

    private String typeFile;

    private List listData;

    private String fileName;

    private static final Map<String, String> reportDirs = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put("DxuatXdungKho", "/reports/DxuatXdungKho.jrxml");
        }
    };

    public ReportPrint(Map params, String typeFile, List listData, String fileName, HttpServletResponse response, ReportTemplate model) {
        this.params = params;
        this.typeFile = typeFile;
        this.listData = listData;
        this.fileName = fileName;
        this.response = response;
        this.model = model;
    }

    public ReportPrint(Map params, String typeFile, List listData, String fileName, HttpServletResponse response) {
        this.params = params;
        this.typeFile = typeFile;
        this.listData = listData;
        this.fileName = fileName;
        this.response = response;
    }

    public ReportPrint(Map params, String typeFile, List listData, String fileName) {
        this.params = params;
        this.typeFile = typeFile;
        this.listData = listData;
        this.fileName = fileName;
    }

    public ReportPrint(Map params, List listData) {
        this.params = params;
        this.listData = listData;
    }
    public ReportPrint(Map params, String typeFile, List listData, String fileName, HttpServletResponse response, ReportTemplate model, Connection conn) {
        this.params = params;
        this.typeFile = typeFile;
        this.listData = listData;
        this.fileName = fileName;
        this.response = response;
        this.model = model;
        if (DataUtils.isNullObject(this.conn)) {
            this.conn = conn;
        }
    }

    public static <T> JasperPrint jasperReport(Map<String, Object> parameters, Class<T> type, Collection<T> report)
            throws Exception {

        String dir = reportDirs.get(type.getSimpleName());
        if (StringUtils.isEmpty(dir))
            throw new UnsupportedOperationException("Report type unknown");
        InputStream inputStream = new ClassPathResource(dir).getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        JRBeanCollectionDataSource jrBean = new JRBeanCollectionDataSource(report);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrBean);

        return jasperPrint;
    }

    public static <T> JasperPrint jasperReportCus(Map<String, Object> parameters, String type, Collection<T> report)
            throws Exception {
        String dir = reportDirs.get(type);
        if (StringUtils.isEmpty(dir))
            throw new UnsupportedOperationException("Report type unknown");
        InputStream inputStream = new ClassPathResource(dir).getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        JRBeanCollectionDataSource jrBean = new JRBeanCollectionDataSource(report);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrBean);

        return jasperPrint;
    }

    public void jasperReport(Map<String, Object> parameters, String type, List list, String fileName, HttpServletResponse response, ReportTemplate model) {
        try {
            InputStream inputStream = new ByteArrayInputStream(model.getFileUpload());
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            JasperPrint jasperPrint;
            parameters.put(JRParameter.REPORT_LOCALE, new Locale("vi", "VN"));
            if (!list.isEmpty()) {
                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
                jasperPrint.setLocaleCode("vi");
            } else {
                if (DataUtils.isNullObject(conn)) {
                    jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
                } else {
                    jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
                }
            }
            jasperPrint.setProperty("net.sf.jasperreports.export.xls.remove.empty.space.between.rows", "true");
            jasperPrint.setProperty("net.sf.jasperreports.export.xls.remove.empty.space.between.columns", "true");
            jasperPrint.setProperty("net.sf.jasperreports.export.xls.detect.cell.type", "true");
            jasperPrint.setProperty("net.sf.jasperreports.export.xls.wrap.text", "true");

            if ("pdf".equals(type)) {
                response.setContentType("application/x-download");
                response.setHeader("Content-Disposition", String.format("attachment; filename=" + fileName));

                OutputStream out = response.getOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            } else {
                String filename = fileName.replaceAll(".jrxml", "") + ".xlsx";
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition", String.format("attachment; filename=" + filename));
                OutputStream out = response.getOutputStream();
                JRXlsxExporter exporter = new JRXlsxExporter();
                // Set input and output ...
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
                SimpleXlsxReportConfiguration reportConfig = new SimpleXlsxReportConfiguration();
                reportConfig.setSheetNames(new String[]{"Data"});
                reportConfig.setWhitePageBackground(false);
                exporter.setConfiguration(reportConfig);
                exporter.exportReport();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String toHtmlString(JasperPrint jasperPrint) throws Exception {
        // Export the report to a HTML file
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        Exporter<ExporterInput, HtmlReportConfiguration, HtmlExporterConfiguration, HtmlExporterOutput> exporter;
        // HTML exporter
        exporter = new HtmlExporter();
        // Set output to byte array
        exporter.setExporterOutput(new SimpleHtmlExporterOutput(byteArray));
        // Set input source
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        // Export to HTML
        exporter.exportReport();
        byte[] bytes = byteArray.toByteArray();
        return new String(bytes, "UTF-8");
    }

    public static <T> byte[] jasperReportPdf(Map<String, Object> parameters, String type, Collection<T> report)
            throws Exception {
        String dir = reportDirs.get(type);
        if (StringUtils.isEmpty(dir))
            throw new UnsupportedOperationException("Report type unknown");
        InputStream inputStream = new ClassPathResource(dir).getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        JRBeanCollectionDataSource jrBean = new JRBeanCollectionDataSource(report);
        return JasperExportManager.exportReportToPdf(JasperFillManager.fillReport(jasperReport, parameters, jrBean));
    }

    public void jasperReport(Map<String, Object> parameters, String type, List list, String fileName, HttpServletResponse response, InputStream inputStream) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            JasperPrint jasperPrint;
            parameters.put(JRParameter.REPORT_LOCALE, new Locale("vi", "VN"));
            if (!list.isEmpty()) {
                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
                jasperPrint.setLocaleCode("vi");
            } else {
                if (DataUtils.isNullObject(conn)) {
                    jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
                } else {
                    jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
                }
            }
            jasperPrint.setProperty("net.sf.jasperreports.export.xls.remove.empty.space.between.rows", "true");
            jasperPrint.setProperty("net.sf.jasperreports.export.xls.remove.empty.space.between.columns", "true");
            jasperPrint.setProperty("net.sf.jasperreports.export.xls.detect.cell.type", "true");
            jasperPrint.setProperty("net.sf.jasperreports.export.xls.wrap.text", "true");

            if ("pdf".equals(type)) {
                response.setContentType("application/x-download");
                response.setHeader("Content-Disposition", String.format("attachment; filename=" + fileName));

                OutputStream out = response.getOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            } else {
                String filename = fileName.replaceAll(".jrxml", "") + ".xlsx";
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition", String.format("attachment; filename=" + filename));
                OutputStream out = response.getOutputStream();
                JRXlsxExporter exporter = new JRXlsxExporter();
                // Set input and output ...
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
                SimpleXlsxReportConfiguration reportConfig = new SimpleXlsxReportConfiguration();
                reportConfig.setSheetNames(new String[]{"Data"});
                reportConfig.setWhitePageBackground(false);
                exporter.setConfiguration(reportConfig);
                exporter.exportReport();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] jasperReport(Map<String, Object> parameters, List list, InputStream inputStream) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            JasperPrint jasperPrint;
            parameters.put(JRParameter.REPORT_LOCALE, new Locale("vi", "VN"));
            if (!list.isEmpty()) {
                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
                jasperPrint.setLocaleCode("vi");
            } else {
                if (DataUtils.isNullObject(conn)) {
                    jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
                } else {
                    jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
                }
            }
            jasperPrint.setProperty("net.sf.jasperreports.export.xls.remove.empty.space.between.rows", "true");
            jasperPrint.setProperty("net.sf.jasperreports.export.xls.remove.empty.space.between.columns", "true");
            jasperPrint.setProperty("net.sf.jasperreports.export.xls.detect.cell.type", "true");
            jasperPrint.setProperty("net.sf.jasperreports.export.xls.wrap.text", "true");

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
