package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuKtChatLuongDtl;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbPhieuKtChatLuongHdrReq {
    private Long id;
    @NotNull
    private Integer nam;

    private String maDvi;
    @NotNull
    private String maQhns;
    @NotNull
    private Long qdDcId;
    @NotNull
    private String soQdinhDc;

    private String soPhieu;
    @NotNull
    private LocalDate ngayLapPhieu;

    private String nguoiKt;

    private Long nguoiKtId;

    private Long tpNguoiKtId;

    private String tpNguoiKt;
    @NotNull
    private String maDiemKho;
    @NotNull
    private String tenDiemKho;
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

    private String tenThuKho;

    private Long thuKhoId;
    @NotNull
    private String maDiemKhoXuat;
    @NotNull
    private String tenDiemKhoXuat;
    @NotNull
    private String maNhaKhoXuat;
    @NotNull
    private String tenNhaKhoXuat;
    @NotNull
    private String maNganKhoXuat;
    @NotNull
    private String tenNganKhoXuat;

    private String maLoKhoXuat;

    private String tenLoKhoXuat;

    private String soBbLayMau;

    private Long bbLayMauId;

    private LocalDate ngayLayMau;

    private LocalDate ngayKiem;
    @NotNull
    private String loaiVthh;
    @NotNull
    private String tenLoaiVthh;
    @NotNull
    private String cloaiVthh;
    @NotNull
    private String tenCloaiVthh;

    private String hinhThucBq;

    private String danhGiaCamQuan;

    private String nhanXetKetLuan;

    private Long nguoiPDuyet;

    private LocalDate ngayPDuyet;

    private String trangThai;

    private Long nguoiGDuyet;

    private LocalDate ngayGDuyet;

    private String lyDoTuChoi;

    private Boolean thayDoiThuKho;

    private LocalDate ngayDuyetTp;

    private Long nguoiDuyetTp;

    private Long bbTinhKhoId;

    private String soBbTinhKho;

    private LocalDate ngayXuatDocKho;

    private Long bbHaoDoiId;

    private String soBbHaoDoi;
    private String nguoiGiaoHang;
    private String soCmt;
    private String dVGiaoHang;
    private String diaChiDonViGiaoHang;
    private String bienSoXe;
    private BigDecimal slNhapTheoQd;
    private BigDecimal slNhapTheoKb;
    private BigDecimal slNhapTheoKt;
    private String soChungThuGiamDinh;
    private LocalDate ngayGiamDinh;
    private String toChucGiamDinh;
    private String donViTinh;

    private List<FileDinhKemReq> bienBanLayMauDinhKem = new ArrayList<>();
    @Valid
    private List<DcnbPhieuKtChatLuongDtl> dcnbPhieuKtChatLuongDtl = new ArrayList<>();
    private ReportTemplateRequest reportTemplateRequest;
}
