package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuKnChatLuongDtl;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbPhieuKnChatLuongHdrReq {
    private Long id;
    @NotNull
    private Integer nam;

    private String maDvi;

    private String tenDvi;
    @NotNull
    private String maQhns;
    @NotNull
    private Long qdDcId;
    @NotNull
    private String soQdinhDc;
    @NotNull
    private LocalDate ngayQdinhDc;
    @NotNull
    private LocalDate ngayHieuLuc;
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
    @NotNull
    private String donViTinh;

    private String maLoKho;

    private String tenLoKho;

    private String tenThuKho;

    private Long thuKhoId;
    @NotNull
    private String soBbLayMau;
    @NotNull
    private Long bbLayMauId;

    private LocalDate ngayLayMau;

    private LocalDate ngayKiem;

    private String loaiVthh;

    private String tenLoaiVthh;

    private String cloaiVthh;

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

    private Long bbNhapDayKhoId;

    private String soNhapDayKho;

    private LocalDate ngayNhapDayKho;

    private Long bbHaoDoiId;

    private String soBbHaoDoi;

    private Long nguoiDuyetLdCuc;

    private LocalDate ngayDuyetLdCuc;

    private String type;

    private String loaiDc;
    private BigDecimal slHangBQ;

    private List<FileDinhKemReq> dinhKems = new ArrayList<>();
    @Valid
    private List<DcnbPhieuKnChatLuongDtl> dcnbPhieuKnChatLuongDtl = new ArrayList<>();
    private ReportTemplateRequest reportTemplateRequest;

    @NotNull
    private Long keHoachDcDtlId;
}
