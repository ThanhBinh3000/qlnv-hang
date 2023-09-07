package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuXuatKhoDtl;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbPhieuXuatKhoHdrReq {
    private Long id;
    @NotNull
    private Integer nam;
    private String maDvi;
    private String tenMaDvi;
    @NotNull
    private String maQhns;
    private String soPhieuXuatKho;
    @NotNull
    private LocalDate ngayTaoPhieu;
    @NotNull
    private LocalDate ngayXuatKho;
    @NotNull
    private Integer taiKhoanNo;
    @NotNull
    private Integer taiKhoanCo;
    @NotNull
    private Long qddcId;
    @NotNull
    private String soQddc;
    @NotNull
    private LocalDate ngayKyQddc;
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
    @NotNull
    private Long phieuKnChatLuongHdrId;
    @NotNull
    private String soPhieuKnChatLuong;
    @NotNull
    private LocalDate ngayKyPhieuKnChatLuong;
    @NotNull
    private String loaiVthh;
    @NotNull
    private String tenLoaiVthh;
    @NotNull
    private String cloaiVthh;
    @NotNull
    private String tenCloaiVthh;
    private String canBoLapPhieu;
    private Long canBoLapPhieuId;
    private String ldChiCuc;
    private Long ldChiCucId;
    private String ktvBaoQuan;
    private Long ktvBaoQuanId;
    private String keToanTruong;
    private Long keToanTruongId;
    private String nguoiGiaoHang;
    private String soCmt;
    private String ctyNguoiGh;
    private String diaChi;
    private LocalDate thoiGianGiaoNhan;
    private BigDecimal soLuongCanDc;
    private String soBangKeCh;
    private Long bangKeChId;
    private String soBangKeVt;
    private Long bangKeVtId;
    private String donViTinh;
    private BigDecimal tongSoLuong;
    private String tongSoLuongBc;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String thanhTienBc;
    private String ghiChu;
    private String trangThai;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String lyDoTuChoi;
    private String type;

    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    @Valid
    private List<DcnbPhieuXuatKhoDtl> dcnbPhieuXuatKhoDtl = new ArrayList<>();
}
