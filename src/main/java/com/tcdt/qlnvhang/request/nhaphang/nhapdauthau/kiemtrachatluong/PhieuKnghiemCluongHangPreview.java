package com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.kiemtrachatluong;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuknghiemcl.KquaKnghiem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PhieuKnghiemCluongHangPreview {
    private Integer nam;
    private String maDvi;
    private String maQhns;
    private String tenDvi;
    private String soBbLayMau;
    private String soQdGiaoNvNh;
    private String soBbNhapDayKho;
    private String soPhieuKiemNghiemCl;
    private Long idKyThuatVien;
    private String tenKyThuatVien;
    private Long idTruongPhong;
    private String tenTruongPhong;
    private String maDiemKho;
    private String tenDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maLoKho;
    private String tenLoKho;
    private Long idDdiemGiaoNvNh;
    private Long idQdGiaoNvNh;
    private String loaiVthh;
    private String tenLoaiVthh;
    private String cloaiVthh;
    private String tenCloaiVthh;
    private String moTaHangHoa;
    private String hthucBquan;
    private BigDecimal soLuongNhapDayKho;

    private String ngayNhapDayKho;
    private String ngayLayMau;
    private String ngayKnghiem;

    private String ketLuan;
    private String ketQuaDanhGia;
    private String ngayTao;
    private String thangTao;
    private String namTao;
    List<KquaKnghiem> listKquaKngiem;
}
