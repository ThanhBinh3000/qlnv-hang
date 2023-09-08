package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanTinhKhoDtl;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbBienBanTinhKhoHdrReq {
    private Long id;
    @NotNull
    private Integer nam;
    private String soBbTinhKho;
    @NotNull
    private Long bangKeCanHangId;
    @NotNull
    private String soBangKe;
    @NotNull
    private LocalDate ngayLap;

    private String maDvi;

    private String tenDvi;

    private Long qhnsId;
    @NotNull
    private String maQhns;
    @NotNull
    private LocalDate ngayBatDauXuat;
    @NotNull
    private LocalDate ngayKeThucXuat;
    @NotNull
    private LocalDate thoiHanXuatHang;
    @NotNull
    private Long qDinhDccId;
    @NotNull
    private String soQdinhDcc;
    @NotNull
    private LocalDate ngayXuatKho;
    @NotNull
    private LocalDate thoiHanDieuChuyen;
    @NotNull
    private LocalDate ngayKyQdDcc;
    @NotNull
    private String loaiVthh;
    @NotNull
    private String cloaiVthh;
    @NotNull
    private String donViTinh;
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
    private String thuKho;

    private Long thuKhoId;

    private Boolean thayDoiThuKho;

    private String trangThai;

    private String lyDoTuChoi;

    private Long nguoiGDuyet;

    private LocalDate ngayGDuyet;

    private String loaiDc;

    private String nguyeNhan;

    private String kienNghi;

    private String ghiChu;

    private LocalDate ngayPduyetKtvBQ;

    private String ktvBaoQuan;

    private Long ktvBaoQuanId;

    private LocalDate ngayPduyetKt;

    private String keToan;

    private Long keToanId;

    private LocalDate ngayPduyetLdcc;

    private String lanhDaoChiCuc;

    private Long lanhDaoChiCucId;

    private BigDecimal tongSlXuatTheoQd;

    private BigDecimal tongSlXuatTheoTt;

    private BigDecimal slConLaiTheoSs;

    private BigDecimal slConLaiTheoTt;

    private BigDecimal chenhLechSlConLai;

    private String type;

    private List<FileDinhKemReq> fileBbTinhKhoDaKy = new ArrayList<>();
    @Valid
    private List<DcnbBienBanTinhKhoDtl> dcnbBienBanTinhKhoDtl = new ArrayList<>();
}
