package com.tcdt.qlnvhang.request.suachua;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuNhapKhoDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class ScPhieuNhapKhoReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String soPhieuNhapKho;
    private LocalDate ngayLap;
    private LocalDate ngayXuatKho;
    private BigDecimal soNo;
    private BigDecimal soCo;
    private String soQdGiaoNvNhap;
    private Long qdGiaoNvNhapId;
    private LocalDate ngayKyQdGiaoNvNhap;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String soPhieuKtraCluong;
    private Long idPhieuKtraCluong;
    private String maLoaiHang;
    private String maChungLoaiHang;
    private String tenLoaiHang;
    private String tenChungLoaiHang;
    private Long idNguoiLap;
    private String tenNguoiLap;
    private Long idLanhDao;
    private String tenLanhDao;
    private Long idThuKho;
    private String tenThuKho;
    private Long idKyThuatVien;
    private String tenKyThuatVien;
    private Long idKeToanTruong;
    private String keToanTruong;

    private String hoVaTenNguoiGiao;
    private String cmndNguoiGiao;
    private String donViNguoiGiao;
    private String diaChi;
    private LocalDate tgianGiaoNhanHang;
    private BigDecimal tongSoLuong;
    private String tongSoLuongBc;
    private BigDecimal tongKinhPhi;
    private String tongKinhPhiBc;
    private String ghiChu;
    private String trangThai;
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    private List<ScPhieuNhapKhoDtl> children = new ArrayList<>();
    private List<String> dsLoaiHang = new ArrayList<>();

    private LocalDate ngayNhapKhoTu;
    private LocalDate ngayNhapKhoDen;
}
