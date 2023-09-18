package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbNhapDayKhoDtl;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbBbNhapDayKhoHdrReq extends BaseRequest {
    private Long id;
    private String loaiQdinh;
    private Boolean thayDoiThuKho;
    private String typeQd;
    @NotNull
    private String loaiDc;
    @NotNull
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    @NotNull
    private String maQhns;
    private String soBb;
    @NotNull
    private LocalDate ngayLap;
    @NotNull
    private String soQdDcCuc;
    @NotNull
    private Long qdDcCucId;
    @NotNull
    private LocalDate ngayQdDcCuc;
    private Long idDiaDiemKho;
    @NotNull
    private String maDiemKho;
    @NotNull
    private String maNhaKho;
    @NotNull
    private String maNganKho;
    private String maLoKho;
    @NotNull
    private String loaiVthh;
    @NotNull
    private String cloaiVthh;
    @NotNull
    private String tenDiemKho;
    @NotNull
    private String tenNhaKho;
    @NotNull
    private String tenNganKho;
    private String tenLoKho;
    @NotNull
    private String tenLoaiVthh;
    @NotNull
    private String tenCloaiVthh;
    @NotNull
    private String dviTinh;
    @NotNull
    private LocalDate ngayBdNhap;
    @NotNull
    private LocalDate ngayKtNhap;
    @NotNull
    private BigDecimal soLuongQdDcCuc;
    private Long idThuKho;
    private Long idKyThuatVien;
    private Long idKeToan;
    private Long idLanhDao;
    private String ghiChu;
    private String trangThai;
    private String lyDoTuChoi;
    @Valid
    private List<DcnbBbNhapDayKhoDtl> children = new ArrayList<>();
    private LocalDate tuNgayBdNhap;
    private LocalDate denNgayBdNhap;
    private LocalDate tuNgayKtNhap;
    private LocalDate denNgayKtNhap;
    private LocalDate tuNgayThoiHanNh;
    private LocalDate denNgayThoiHanNh;

    private Boolean isVatTu = false;
    private List<String> dsLoaiHang = new ArrayList<>();
    private ReportTemplateRequest reportTemplateRequest;
    @NotNull
    private Long keHoachDcDtlId;
}
