package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeCanHangDtl;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbBangKeCanHangHdrReq {
    private Long id;
    @NotNull
    private Integer nam;

    private String soBangKe;
    @NotNull
    private LocalDate ngayNhap;

    private String maDvi;

    private String tenDvi;

    private Long qhnsId;
    @NotNull
    private String maQhns;
    @NotNull
    private Long qDinhDccId;
    @NotNull
    private String soQdinhDcc;
    @NotNull
    private LocalDate ngayKyQdinh;
    @NotNull
    private LocalDate thoiHanDieuChuyen;
    @NotNull
    private LocalDate thoiGianGiaoNhan;
    @NotNull
    private LocalDate ngayKyQdDcc;
    private Long phieuXuatKhoId;
    private LocalDate ngayXuatKho;
    private String soPhieuXuatKho;

    private Long phieuNhapKhoId;

    private LocalDate ngayNhapKho;

    private String soPhieuNhapKho;
    @NotNull
    private String soBbLayMau;
    @NotNull
    private String loaiVthh;
    @NotNull
    private String cloaiVthh;
    @NotNull
    private String donViTinh;
    @NotNull
    private String tenDonViTinh;
    @NotNull
    private String maDiemKho;
    @NotNull
    private String tenDiemKho;

    private String diaDaDiemKho;
    @NotNull
    private String maNhaKho;
    @NotNull
    private String tenNhaKho;
    @NotNull
    private String maNganKho;
    @NotNull
    private String tenNganKho;

    private String maLoKho;

    private String tenLoKho;
    @NotNull
    private Boolean thayDoiThuKho;

    private String trangThai;

    private String lyDoTuChoi;

    private Long nguoiGDuyet;

    private LocalDate ngayGDuyet;

    private Long nguoiPDuyet;

    private LocalDate ngayPDuyet;
    @NotNull
    private String loaiDc;
    @NotNull
    private String type;

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
    @Valid
    private List<DcnbBangKeCanHangDtl> dcnbBangKeCanHangDtl = new ArrayList<>();
}
