package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBKetThucNKDtl;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class DcnbBBKetThucNKReq extends BaseRequest {
    private Long id;
    @NotNull
    private String loaiDc;
    @NotNull
    private String typeQd;
    @NotNull
    private String loaiQdinh;
    private Boolean thayDoiThuKho;
    @NotNull
    private String loaiVthh;
    @NotNull
    private String cloaiVthh;
    @NotNull
    private Integer nam;
    private String soBb;
    @NotNull
    private LocalDate ngayLap;
    private String maDvi;
    private Long qhnsId;
    @NotNull
    private String maQhns;
    @NotNull
    private Long qDinhDccId;
    @NotNull
    private String soQdinhDcc;
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
    private LocalDate ngayBatDauNhap;
    @NotNull
    private LocalDate ngayKetThucNhap;
    @NotNull
    private BigDecimal tongSlTheoQd;
    private String maLanhDaoChiCuc;
    private String tenLanhDaoChiCuc;
    private Long thuKhoId;
    private String tenThuKho;
    private Long ktvBQuan;
    private String tenKtvBQuan;
    private Long keToanTruong;
    private String tenKeToanTruong;
    private String donViTinh;
    private String trangThai;
    private String lyDoTuChoi;
    private Long nguoiGDuyet;

    private LocalDate ngayGDuyet;

    private Long nguoiPDuyetKtv;

    private LocalDate ngayPDuyetKtv;
    private Long nguoiPDuyetKt;

    private LocalDate ngayPDuyetKt;

    private Long nguoiPDuyet;

    private LocalDate ngayPDuyet;
    private String type;
    @Valid
    private List<DcnbBBKetThucNKDtl> dcnbBBKetThucNKDtl = new ArrayList<>();
    private LocalDate tuNgayKtnk;
    private LocalDate denNgayKtnk;
    private LocalDate tuNgayThoiHanNhap;
    private LocalDate denNgayThoiHanNhap;

    private Boolean isVatTu = false;
    private List<String> dsLoaiHang = new ArrayList<>();
}
