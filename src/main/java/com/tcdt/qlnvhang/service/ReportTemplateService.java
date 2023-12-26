package com.tcdt.qlnvhang.service;

import com.tcdt.qlnvhang.jasper.JasperReport;
import com.tcdt.qlnvhang.jasper.JasperReportManager;
import com.tcdt.qlnvhang.repository.ReportTemplateRepository;
import com.tcdt.qlnvhang.table.ReportTemplate;
import com.tcdt.qlnvhang.util.OutputExcelPdf;
import com.tcdt.qlnvhang.util.ReportPrint;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;


@Service
@Log4j2
public class ReportTemplateService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReportTemplateRepository reportTemplateRepository;
    @Value("${path.base-jasper-folder}")
    public String baseJasperFolder;
    @Value("${path.base-jasper-folder}")
    public String baseTemplateFolder;


    public void exportReport(String fileName, Map params, String tenBaoCao, String type, HttpServletResponse response) throws Exception {
        ReportTemplate model = reportTemplateRepository.findByTenFile(fileName);
        if (model == null) {
            throw new Exception("Không tìm thấy mẫu báo cáo");
        }
        DataSource dataSource = jdbcTemplate.getDataSource();
        OutputExcelPdf outputExcelPdf = new OutputExcelPdf(fileName, params, tenBaoCao, model.getFileUpload(), dataSource, response);
        if (type.toLowerCase().equals("pdf")) {
            outputExcelPdf.printPdf();
        } else {
            outputExcelPdf.printExel();
        }
        outputExcelPdf.download(response);
        outputExcelPdf.delete();
    }

    public <T> void exportReport(Map params, String typeFile, String fileName, List<T> list, HttpServletResponse response) throws Exception {
        try {
            ReportTemplate model = reportTemplateRepository.findByTenFile(fileName);
            if (model == null) {
                throw new Exception("Không tìm thấy mẫu báo cáo");
            }
            ReportPrint reportPrint = new ReportPrint(params, typeFile, list, fileName, response, model);
            reportPrint.jasperReport(params, typeFile, list, fileName, response, model);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi: " + e.getMessage());
        }
    }

    public <T> void exportReport(Map params, String typeFile, String fileName, List<T> list, JasperReport jasperReport, HttpServletResponse response) throws Exception {
        try {
            ReportPrint reportPrint = new ReportPrint(params, typeFile, list, fileName, response);
            String xml = JasperReportManager.buildReport(jasperReport);
            reportPrint.jasperReport(params, typeFile, list, fileName, response, new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi: " + e.getMessage());
        }
    }

    public <T> void exportReportResource(Map params, String typeFile, String fileName, List<T> list, HttpServletResponse response) throws Exception {
        try {
            ReportTemplate model = new ReportTemplate();
            FileInputStream inputStream = new FileInputStream(baseJasperFolder + fileName);
            model.setFileUpload(IOUtils.toByteArray(inputStream));
            ReportPrint reportPrint = new ReportPrint(params, typeFile, list, fileName, response, model);
            reportPrint.jasperReport(params, typeFile, list, fileName, response, model);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi: " + e.getMessage());
        }
    }

    public <T> byte[] exportReport(Map params, List<T> list, JasperReport jasperReport) throws Exception {
        try {
            ReportPrint reportPrint = new ReportPrint(params, list);
            String xml = JasperReportManager.buildReport(jasperReport);
            return reportPrint.jasperReport(params, list, new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi: " + e.getMessage());
        }
    }

    public ResponseEntity<byte[]> exportTemplateResource(String tenFile) throws Exception {
        try {
            File file = new File(baseTemplateFolder + "template/" + tenFile);
            byte[] fileContent = new byte[(int) file.length()];
            try (FileInputStream fis = new FileInputStream(file)) {
                fis.read(fileContent);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", file.getName());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileContent);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi: " + e.getMessage());
        }
    }
}
