package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbChuanBiKhoDtl;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbBbChuanBiKhoHdrReq extends BaseRequest {

    private Long id;
    @NotNull
    private String loaiDc;
    private String typeQd;
    @NotNull
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    @NotNull
    private String maQhns;
    @NotNull
    private String soQdDcCuc;
    @NotNull
    private Long qdDcCucId;
    private LocalDate ngayQdDcCuc;
    private BigDecimal soLuongQdDcCuc;
    private String soBban;
    @NotNull
    private LocalDate ngayLap;
    @NotNull
    private LocalDate ngayKetThucNt;
    @NotNull
    private Long keHoachDcDtlId;
    @NotNull
    private String maDiemKho;
    @NotNull
    private String maNhaKho;
    @NotNull
    private String maNganKho;
    private String maLoKho;
    @NotNull
    private String tenDiemKho;
    @NotNull
    private String tenNhaKho;
    @NotNull
    private String tenNganKho;
    private String tenLoKho;
    private String loaiHinhKho;
    @NotNull
    private String loaiVthh;
    @NotNull
    private String cloaiVthh;
    @NotNull
    private String tenLoaiVthh;
    @NotNull
    private String tenCloaiVthh;
    @NotNull
    private String donViTinh;
    private BigDecimal duToanKphi;
    private BigDecimal tichLuong;
    private BigDecimal tongKinhPhiDaTh;
    private String tongKinhPhiDaThBc;
    private Long idPhieuNhapKho;
    private String soPhieuNhapKho;
    private BigDecimal soLuongPhieuNhapKho;
    private String hthucKlot;
    private String pthucBquan;
    private BigDecimal dinhMucGiao;
    private BigDecimal dinhMucThucTe;
    private String nhanXet;
    private String trangThai;
    private String lyDoTuChoi;
    private Boolean thayDoiThuKho;
    private String loaiQdinh;
    @Valid
    private List<DcnbBbChuanBiKhoDtl> children = new ArrayList<>();
    private LocalDate tuNgayLapBb;
    private LocalDate denNgayLapBb;
    private LocalDate tuNgayKtnk;
    private LocalDate denNgayKtnk;

    private Boolean isVatTu = false;
    private List<String> dsLoaiHang = new ArrayList<>();
    private ReportTemplateRequest reportTemplateRequest;
}
