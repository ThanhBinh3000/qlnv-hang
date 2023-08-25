package com.tcdt.qlnvhang.request.khoahoccongnghebaoquan;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class QuyChuanQuocGiaHdrReq {

    private Long id;
    private String soVanBan;
    private String idVanBanThayThe;
    private String soVanBanThayThe;
    private String maDvi;
    private String loaiVthh;
    private String cloaiVthh;
    private LocalDate ngayKy;
    private LocalDate ngayHieuLuc;
    private LocalDate ngayHetHieuLuc;
    private String soHieuQuyChuan;
    private String apDungTai;
    private String loaiApDung;
    private String danhSachApDung;
    private String trichYeu;
    private String type;
    private int thoiGianLuuKhoToiDa;
    private Boolean lastest;
    private String trangThai;
    private String trangThaiHl;
    private String ldoTuChoi;
    private Boolean apDungCloaiVthh;
    private Boolean isMat;
    private String listTenLoaiVthh;
    private String maBn;
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    private List<QuyChuanQuocGiaDtlReq> tieuChuanKyThuat = new ArrayList<>();
    private ReportTemplateRequest reportTemplateRequest;
}
