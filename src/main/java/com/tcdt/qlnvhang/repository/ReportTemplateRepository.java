package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.ReportTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportTemplateRepository extends JpaRepository<ReportTemplate, Long> {

    ReportTemplate findByTenFile(String tenFile);
    ReportTemplate findByTenFileAndTrangThai(String tenFile, String trangThai);

}
