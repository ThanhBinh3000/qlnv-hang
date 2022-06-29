package com.tcdt.qlnvhang.request.object.vattu.bienbanguihang;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class NhBienBanGuiHangReq {
    private Long id;
    private String maDvi;
    private String capDvi;

    private Long phieuNkTgId;

    private Long hopDongId;

    private Long qdgnvnxId;

    private String soBienBan;
    private LocalDate ngayHopDong;
    private String donViCungCap;
    private LocalDate ngayGui;
    private String loaiVthh;
    private String maVatTuCha;
    private String maVatTu;
    private Long soLuong;
    private String donViTinh;
    private String tinhTrang;
    private String chatLuong;
    private String ghiChu;
    private String trangThai;

    private String lyDoTuChoi;
    private String benNhan;
    private String benGiao;
    private String trachNhiemBenNhan;
    private String trachNhiemBenGiao;
    private LocalDateTime thoiGian;

    private List<NhBienBanGuiHangCtReq> chiTiets = new ArrayList<>();
}
