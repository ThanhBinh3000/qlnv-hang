package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuXuatKhoDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbPhieuXuatKhoHdrReq {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String tenMaDvi;
    private String maQhns;
    private String soPhieuXuatKho;
    private LocalDate ngayTaoPhieu;
    private LocalDate ngayXuatKho;
    private Integer taiKhoanNo;
    private Integer taiKhoanCo;
    private Long qddcId;
    private String soQddc;
    private LocalDate ngayKyQddc;
    private String maDiemKho;
    private String tenDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maLoKho;
    private String tenLoKho;
    private Long phieuKnChatLuongHdrId;
    private String soPhieuKnChatLuong;
    private LocalDate ngayKyPhieuKnChatLuong;
    private String loaiVthh;
    private String tenLoaiVthh;
    private String cloaiVthh;
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

    private List<DcnbPhieuXuatKhoDtl> dcnbPhieuXuatKhoDtl = new ArrayList<>();
}
