package com.tcdt.qlnvhang.request.khoahoccongnghebaoquan;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class QuyChuanQuocGiaHdrReq {

    private Long id;
    private String soVanBan;
    private Long idVanBanThayThe;
    private String soVanBanThayThe;
    private String maDvi;
    private String loaiVthh;
    private String cloaiVthh;
    private LocalDate ngayKy;
    private LocalDate ngayHieuLuc;
    private String soHieuQuyChuan;
    private String apDungTai;
    private String loaiApDung;
    private String danhSachApDung;
    private String trichYeu;
    private int thoiGianLuuKhoToiDa;
    private boolean lastest;
    private String trangThai;
    private String trangThaiHl;
    private String ldoTuChoi;
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    private List<QuyChuanQuocGiaDtlReq> tieuChuanKyThuat = new ArrayList<>();
}
