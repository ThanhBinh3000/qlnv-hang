package com.tcdt.qlnvhang.util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class OutputExcelPdf {
    private File currentFile = null;
    private String temp = "/tmp";

    {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
            temp = System.getProperty("user.home") + "/";

        } else if (os.indexOf("mac") >= 0) {
            temp = System.getProperty("user.home") + "/Library/Caches/";
        } else if (os.indexOf("win") >= 0) {
            temp = System.getProperty("user.home") + "\\";
        } else {
            temp = System.getProperty("user.home") + "/";
        }
    }

    HttpServletResponse response;

    private String tenFile;

    private DataSource dataSource;

    private String tenBaoCao;
    private byte[] fileIn;

    private Map params;



    public OutputExcelPdf(String tenFile, Map params, String tenBaoCao, byte[] fileIn, DataSource dataSource, HttpServletResponse response) {
        this.tenFile = tenFile;
        this.response = response;
        this.fileIn = fileIn;
        this.dataSource = dataSource;
        this.params = params;
        this.tenBaoCao = tenBaoCao;
    }

    private JasperPrint getPrinObject(InputStream in, Map params,Connection conn) throws Exception {
        JasperPrint jp = null;
        try {
            JasperReport report = JasperCompileManager.compileReport(in);
            if(conn == null){
                throw new Exception("Mất kết nối đến cơ sở dữ liệu.");
            }
            jp = JasperFillManager.fillReport(report, params,conn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jp;
    }

    public File printPdf() {
        try {
            InputStream in = null;
            in =new ByteArrayInputStream(fileIn);
            currentFile = output(in, "pdf");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return currentFile;
    }


    private File output(File fin,  String type) throws Exception {
        InputStream in = null;
        OutputStream out = null;
        File fout = null;
        JRXlsExporter export;
        JRPdfExporter exporterPdf;

        SimpleDateFormat f = new SimpleDateFormat("ddMMyyyyHHmmss");
        tenBaoCao = f.format(Calendar.getInstance().getTime()) + "_" + tenBaoCao;
        JasperPrint print;
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            in = new FileInputStream(fin);
            print = getPrinObject(in, params,conn);
            if (type.equals("xls")) {
                fout = getAutoFileOut(fin.getAbsolutePath(), "xls");
                out = new FileOutputStream(fout);
                export = new JRXlsExporter();
                export.setParameter(JRExporterParameter.JASPER_PRINT, print);
                export.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
                export.exportReport();
            } else if (type.equals("pdf")) {
                exporterPdf = new JRPdfExporter();
                JRDesignStyle normalStyle = new JRDesignStyle();
                normalStyle.setPdfFontName("Times-Roman");
                normalStyle.setPdfEncoding("Identity-H");
                normalStyle.setPdfEmbedded(true);
                fout = getAutoFileOut(fin.getAbsolutePath(), "pdf");
                out = new FileOutputStream(fout);
                print.addStyle(normalStyle);
                JasperExportManager.exportReportToPdfStream(print, out);
                fout = getAutoFileOut(temp + tenBaoCao, "pdf");
                out = new FileOutputStream(fout);
                exporterPdf.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
                exporterPdf.setParameter(JRPdfExporterParameter.JASPER_PRINT, print);
                exporterPdf.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
                exporterPdf.exportReport();
            }
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            out.close();
            in.close();
            conn.close();
        }
        export = null;
        exporterPdf = null;
        print = null;
        conn = null;
        return fout;
    }


    private File output(InputStream in,String type) throws Exception {
        OutputStream out = null;
        File fout = null;
        JRPdfExporter exporterPdf = null;
        JRXlsExporter export = null;
        SimpleDateFormat f = new SimpleDateFormat("ddMMyyyyHHmmss");
        tenBaoCao = f.format(Calendar.getInstance().getTime()) + "_" + tenBaoCao;
        JasperPrint print;
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            print = getPrinObject(in, params,conn);
            if (type.equals("xls")) {
                fout = getAutoFileOut(temp + tenBaoCao, "xls");
                out = new FileOutputStream(fout);
                export = new JRXlsExporter();
                export.setParameter(JRExporterParameter.JASPER_PRINT, print);
                export.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
                export.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
                export.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                export.exportReport();
            } else if (type.equals("pdf")) {
                JRDesignStyle normalStyle = new JRDesignStyle();
                normalStyle.setPdfFontName("Times-Roman");
                normalStyle.setPdfEncoding("Identity-H");
                normalStyle.setPdfEmbedded(true);
                fout = getAutoFileOut(temp + tenBaoCao, "pdf");
                out = new FileOutputStream(fout);
                print.addStyle(normalStyle);
                JasperExportManager.exportReportToPdfStream(print, out);
                exporterPdf = new JRPdfExporter();
                fout = getAutoFileOut(temp + tenBaoCao, "pdf");
                out = new FileOutputStream(fout);
                exporterPdf.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
                exporterPdf.setParameter(JRPdfExporterParameter.JASPER_PRINT, print);
                exporterPdf.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
                exporterPdf.exportReport();
            }
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            out.close();
            in.close();
            conn.close();
        }
        export = null;
        exporterPdf = null;
        print = null;
        conn= null;
        return fout;
    }

    private File getAutoFileOut(String name, String ext) {
        File fout = null;
//        String s = name.substring(0, name.indexOf("."));
        name += "." + ext;
        fout = new File(name);
        return fout;
    }

    public File printExel() {
        try {
            InputStream in = null;
            in = new ByteArrayInputStream(fileIn);
            currentFile = output(in,  "xls");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return currentFile;
    }
    public void download(HttpServletResponse response) throws Exception {
        downloadFromFile(this.currentFile,response);
    }
    public void downloadFromFile(File fin,HttpServletResponse response) throws Exception {
        if (fin == null || !fin.exists())
            throw new Exception("File can download khong ton tai.");
         InputStream in = null;
        try {
            in = new FileInputStream(fin);
            download(in, fin.getName(), (int)fin.length(),response);
        } catch (Exception ex) {
        } finally {
            in.close();
            fin.delete();
        }
    }

    public boolean delete(){
        if(this.currentFile != null && this.currentFile.exists()){
            return this.currentFile.delete();
        }
        return false;
    }

    private void download(InputStream in, String filename,int length, HttpServletResponse response) throws Exception {
        response.setContentType(get(filename));
        response.setHeader("Content-Type", "application/pdf;filename=\"" + filename + "\"");
        if(filename.endsWith("xls")){
            response.setHeader("Content-Type", "application/vnd.ms-excel;filename=\"" + filename + "\"");
            response.setHeader("Content-Disposition","attachment;filename=\"" + filename + "\"");
        }
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            int l = -1;
            byte[] b = new byte[4096];
            while ((l = in.read(b)) != -1) {
                if (length < 4096) {
                    out.write(b, 0, length);
                } else {
                    out.write(b);
                    length -= 4096;
                }
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                close(out);
                close(in);
            }
        }
    }

    public static String get(String fileName){
        String mime = null;
        String ext = fileName.toLowerCase();
        if(ext.endsWith(".pdf")) { mime = "application/pdf"; }
        else if(ext.endsWith(".doc")) { mime = "application/msword"; }
        else if(ext.endsWith(".ppt")) { mime = "application/vnd.ms-powerpoint"; }
        else if(ext.endsWith(".rar")) { mime = "application/octet-stream"; }
        else if(ext.endsWith(".zip")) { mime = "application/zip"; }
        else if(ext.endsWith(".jpg")) { mime = "image/jpeg"; }
        else if(ext.endsWith(".jpeg")){ mime = "image/jpeg"; }
        else if(ext.endsWith(".xls")){ mime = "application/vnd.ms-excel"; }
        else if(ext.endsWith(".gif")) { mime = "image/gif"; }
        return mime;
    }

    private void close(Closeable resource){
        if(resource != null){
            try {
                resource.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
