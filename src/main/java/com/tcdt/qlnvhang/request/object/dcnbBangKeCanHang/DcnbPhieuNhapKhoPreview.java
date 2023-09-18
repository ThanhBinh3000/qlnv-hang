package com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang;

import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuNhapKhoDtlDto;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DcnbPhieuNhapKhoPreview {
    private String tenDvi;
    private String maQhns;
    private int ngayNhap;
    private int thangNhap;
    private int namNhap;
    private String soPhieuNhapKho;
    private BigDecimal soNo;
    private BigDecimal soCo;
    private String hoVaTenNguoiGiao;
    private String cmndNguoiGiao;
    private String donViNguoiGiao;
    private String diaChi;
    private String theoHopDongSo; // Theo hợp đồng số
    private String ngayKyHopDong; //Ngày ký hợp đồng
    private String donViCungCapHang;
    private String soQdGiaoVnNhapHang;
    private String ngayKyQdGiaoNvNhapHang;
    private String donViCapChaCuaTruongDonVi;
    private String tenNganKho;
    private String tenLoKho;
    private String tenNhaKho;
    private String tenDiemKho;
    private String tenThuKho;
    private String tgianGiaoNhanHang;
    private String tongSoLuongBc;
    private String tongKinhPhiBc;
    private String tenNguoiLap;
    private String keToanTruong;
    private String tenLanhDao;
    private List<FileDinhKem> chungTuDinhKem;
    List<DcnbPhieuNhapKhoDtlDto> dcnbPhieuNhapKhoDtl;
    private BigDecimal tongSoLuongTheoChungTu;
    private BigDecimal tongSoLuongThucNhap;
    private BigDecimal tongSoTien;
}
