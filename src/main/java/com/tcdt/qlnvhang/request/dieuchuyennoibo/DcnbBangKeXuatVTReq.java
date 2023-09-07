package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeNhapVTDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeXuatVTDtl;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class DcnbBangKeXuatVTReq extends BaseRequest {
    @NotNull
    private Integer nam;
    private String maDvi;
    @NotNull
    private String loaiDc;
    @NotNull
    private String loaiQdinh;
    @NotNull
    private String typeQd;
    private Boolean isVatTu = false;
    private List<String> dsLoaiHang = new ArrayList<>();

    private Long id;
    @NotNull
    private String loaiVthh;
    @NotNull
    private String cloaiVthh;
    private String soBangKe;
    @NotNull
    private LocalDate ngayNhap;
    private Long qhnsId;
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
    private Long phieuXuatKhoId;
    @NotNull
    private String soPhieuXuatKho;
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
    private Long phuTrachId;
    private String tenPhuTrach;
    private String tenNguoiGiaoHang;
    private String cccd;
    private String donViNguoiGiaoHang;
    private String diaChiDonViNguoiGiaoHang;
    private LocalDate thoiHanGiaoNhan;
    private String donViTinh;
    private String trangThai;
    private String lyDoTuChoi;
    private Long nguoiGDuyet;
    private LocalDate ngayGDuyet;
    private Boolean thayDoiThuKho;
    private Long nguoiPDuyet;

    private LocalDate ngayPDuyet;
    private String type;
    @Valid
    private List<DcnbBangKeXuatVTDtl> dcnbBangKeXuatVTDtl = new ArrayList<>();

    private LocalDate tuNgayThoiHan;
    private LocalDate denNgayThoiHan;

    private LocalDate tuNgayXuatKho;
    private LocalDate denNgayXuatKho;
}
