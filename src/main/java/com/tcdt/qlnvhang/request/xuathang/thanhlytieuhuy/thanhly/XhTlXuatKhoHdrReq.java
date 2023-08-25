package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.entities.FileDKemJoinTable;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhTlXuatKhoHdrReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhNs;
    private String soPhieuXuatKho;
    private LocalDate ngayTaoPhieu;
    private LocalDate ngayXuatKho;
    private BigDecimal taiKhoanNo;
    private BigDecimal taiKhoanCo;
    private Long idBbQd;
    private String soBbQd;
    private LocalDate ngayKyBbQd;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private String maDiaDiem;
    private Long idPhieuKnCl;
    private String soPhieuKnCl;
    private LocalDate ngayKnCl;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenVthh;
    private String canBoLapPhieu;
    private Long idKtv;
    private String keToanTruong;
    private String tenNguoiGiao;
    private String cmtNguoiGiao;
    private String congTyNguoiGiao;
    private String diaChiNguoiGiao;
    private LocalDate thoiGianGiaoNhan;
    private String loaiHinhNx;
    private String kieuNx;
    private Long idBangCanKeHang;
    private String soBangCanKeHang;
    private String maSo;
    private String donViTinh;
    private BigDecimal theoChungTu;
    private BigDecimal thucXuat;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String ghiChu;
    private String trangThai;
    private List<FileDKemJoinTable> fileDinhKem = new ArrayList<>();
    private String dvql;
    private LocalDate ngayXuatKhoTu;
    private LocalDate ngayXuatKhoDen;
}