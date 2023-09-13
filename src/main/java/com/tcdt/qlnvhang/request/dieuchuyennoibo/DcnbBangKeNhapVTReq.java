package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeNhapVTDtl;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class DcnbBangKeNhapVTReq extends BaseRequest {
    private Long id;
    @NotNull
    private String loaiDc;
    private String loaiQdinh;
    private String typeQd;
    @NotNull
    private String loaiVthh;
    @NotNull
    private String cloaiVthh;
    @NotNull
    private String tenLoaiVthh;
    @NotNull
    private String tenCloaiVthh;
    @NotNull
    private Integer nam;
    private String soBangKe;
    @NotNull
    private LocalDate ngayNhap;
    private String maDvi;
    private String tenDvi;
    @NotNull
    private String maQhns;
    @NotNull
    private Long qDinhDccId;
    @NotNull
    private String soQdinhDcc;
    @NotNull
    private LocalDate ngayKyQdinhDcc;
    private String soHopDong;
    @NotNull
    private Long phieuNhapKhoId;
    @NotNull
    private String soPhieuNhapKho;
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
    private String maLanhDaoChiCuc;
    private String tenLanhDaoChiCuc;
    private Long thuKhoId;
    private String tenThuKho;
    private String tenNguoiGiaoHang;
    private String cccd;
    private String donViNguoiGiaoHang;
    private String diaChiDonViNguoiGiaoHang;
    private LocalDate thoiHanGiaoNhan;
    @NotNull
    private String donViTinh;
    private String trangThai;
    private String lyDoTuChoi;
    private Long nguoiGDuyet;
    private LocalDate ngayGDuyet;
    private Long nguoiPDuyetTvqt;
    private LocalDate ngayPDuyetTvqt;
    private Long nguoiPDuyet;
    private LocalDate ngayPDuyet;
    private String type;
    private String typeDataLink;
    private  Boolean thayDoiThuKho;
    @Valid
    private List<DcnbBangKeNhapVTDtl> dcnbbangkenhapvtdtl = new ArrayList<>();

    private LocalDate tuNgayThoiHan;
    private LocalDate denNgayThoiHan;

    private LocalDate tuNgayNhapKho;
    private LocalDate denNgayNhapKho;

    private Boolean isVatTu = false;
    private List<String> dsLoaiHang = new ArrayList<>();
    private ReportTemplateRequest reportTemplateRequest;
}
