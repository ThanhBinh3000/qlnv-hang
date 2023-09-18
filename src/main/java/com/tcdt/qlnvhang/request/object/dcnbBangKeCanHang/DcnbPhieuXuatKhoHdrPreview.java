package com.tcdt.qlnvhang.request.object.dcnbBangKeCanHang;

import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuXuatKhoDtlDto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DcnbPhieuXuatKhoHdrPreview {
    private String maDvi;
    private String maQhns;
    private int ngayNhap;
    private int thangNhap;
    private int namNhap;
    private String soPhieuXuatKho;
    private Integer taiKhoanNo;
    private Integer taiKhoanCo;
    private String hoVaTenNguoiNhanHang;//Họ và tên người nhận hàng
    private String soCmt;
    private String tenMaDvi;
    private String diaChi;
    private String soQddc;
    private String ngayKyQddc;
    private String maDviCha;
    private String tenNganKho;
    private String tenLoKho;
    private String tenNhaKho;
    private String tenDiemKho;
    private String canBoLapPhieu;
    private String thoiGianGiaoNhan;
    private String tongSoLuongBc;
    private String thanhTienBc;
    private String keToanTruong;
    private String ldChiCuc;
    private BigDecimal tongKinhPhiDcTt;
    private List<DcnbPhieuXuatKhoDtlDto> dcnbPhieuXuatKhoDtlDto;
}
