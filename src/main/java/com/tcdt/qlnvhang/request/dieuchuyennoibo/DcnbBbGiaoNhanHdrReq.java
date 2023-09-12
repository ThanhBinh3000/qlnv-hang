package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbGiaoNhanDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbGiaoNhanTTDtl;
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
public class DcnbBbGiaoNhanHdrReq extends BaseRequest {

    private Long id;
    private String loaiQdinh;
    @NotNull
    private String loaiDc;
    private String typeQd;
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
    @NotNull
    private String soBbKtNhapKho;
    @NotNull
    private Long idBbKtNhapKho;
    private Long idKeHoachDtl;
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
    private String soHoSoKyThuat;
    @NotNull
    private String loaiVthh;
    @NotNull
    private String cloaiVthh;
    @NotNull
    private String tenLoaiVthh;
    @NotNull
    private String tenCloaiVthh;
    @NotNull
    private String dviTinh;
    private LocalDate ngayBdNhap;
    private LocalDate ngayKtNhap;
    private BigDecimal soLuongQdDcCuc;
    private String ghiChu;
    private String ketLuan;
    private Long idCanBo;
    private Long idLanhDao;
    private String trangThai;
    private String lyDoTuChoi;
    private Boolean thayDoiThuKho;
    private Boolean isVatTu = false;
    private List<String> dsLoaiHang;
    private List<FileDinhKemReq> fileCanCuReq = new ArrayList<>();
    @Valid
    private List<DcnbBbGiaoNhanDtl> danhSachDaiDien = new ArrayList<>();
    @Valid
    private List<DcnbBbGiaoNhanTTDtl> danhSachBangKe = new ArrayList<>();

    private LocalDate tuNgayKtnk;
    private LocalDate denNgayKtnk;
    private LocalDate tuNgayThoiHanNhap;
    private LocalDate denNgayThoiHanNhap;
    private ReportTemplateRequest reportTemplateRequest;
}
