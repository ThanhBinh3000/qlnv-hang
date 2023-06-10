package com.tcdt.qlnvhang.table.report;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReportTemplateRequest {
    String typeFile;
    String fileName;
    String tenBaoCao;
    String trangThai;
    String maDvi;
    String maCuc;
    String maChiCuc;
    Integer nam;
    Integer quy;
    String ngayBatDauQuy;
    String ngayKetThucQuy;
    List<String> cloaiVthh = new ArrayList();
    String loaiVthh;
    String maBieuSo;
    Long idHdr;
}