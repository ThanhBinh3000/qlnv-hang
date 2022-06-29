package com.tcdt.qlnvhang.response.vattu.bienbanguihang;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class NhBienBanGuiHangRes {
    private Long id;
    private String maDvi;
    private String tenDvi;
    private String capDvi;

    private Long phieuNkTgId;
    private String soPhieuNkTg;

    private Long hopDongId;
    private String soHopDong;

    private Long qdgnvnxId;
    private String soQuyetDinhNhap;

    private String soBienBan;
    private LocalDate ngayHopDong;
    private String donViCungCap;
    private LocalDate ngayGui;
    private String loaiVthh;
    private String tenVatTuCha;
    private String maVatTuCha;
    private String tenVatTu;
    private String maVatTu;
    private Long soLuong;
    private String donViTinh;
    private String tinhTrang;
    private String chatLuong;
    private String ghiChu;
    private String trangThai;
    private String tenTrangThai;
    private String trangThaiDuyet;

    private String lyDoTuChoi;
    private String benNhan;
    private String benGiao;
    private String trachNhiemBenNhan;
    private String trachNhiemBenGiao;
    private LocalDateTime thoiGian;
    private Integer namNhap;

    private List<NhBienBanGuiHangCtRes> chiTiets = new ArrayList<>();
}
