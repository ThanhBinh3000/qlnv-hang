package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBangKeCanHangDtl;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class HhNkBangKeCanHangHdrReq {
    private Long id;

    private Integer nam;

    private String soBangKe;

    private LocalDate ngayNhap;

    private String maDvi;

    private String tenDvi;

    private Long qhnsId;

    private String maQhns;

    private Long idQdPdNk;
    String soQdPdNk;
    private LocalDate ngayKyQdinh;

    private LocalDate thoiHanDieuChuyen;

    private LocalDate thoiGianGiaoNhan;

    private Long phieuNhapKhoId;

    private LocalDate ngayNhapKho;

    private String soPhieuNhapKho;

    private String soBbLayMau;

    private Long soBbLayMauId;

    private String loaiVthh;

    private String cloaiVthh;

    private String donViTinh;

    private String maDiemKho;

    private String tenDiemKho;

    private String diaDiemKho;

    private String maNhaKho;

    private String tenNhaKho;

    private String maNganKho;

    private String tenNganKho;

    private String maLoKho;

    private String tenLoKho;

    private String trangThai;

    private String lyDoTuChoi;

    private Long nguoiGDuyet;

    private LocalDate ngayGDuyet;

    private Long nguoiPDuyet;

    private LocalDate ngayPDuyet;

    private String maLanhDaoChiCuc;

    private String tenLanhDaoChiCuc;

    private Long thuKhoId;

    private String tenThuKho;

    private String tenNguoiGiaoHang;

    private String cccd;

    private String donViNguoiGiaoHang;

    private String diaChiDonViNguoiGiaoHang;

    private BigDecimal tongTrongLuongBaoBi;

    private BigDecimal tongTrongLuongCabaoBi;

    private BigDecimal tongTrongLuongTruBi;

    private String tongTrongLuongTruBiText;

    private String tenTrangThai;

    private List<HhNkBangKeCanHangDtl> hhNkBangKeCanHangDtl = new ArrayList<>();
    private ReportTemplateRequest reportTemplateRequest;
}
