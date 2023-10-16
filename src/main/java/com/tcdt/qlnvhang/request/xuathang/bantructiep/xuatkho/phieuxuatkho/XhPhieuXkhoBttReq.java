package com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.phieuxuatkho;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhPhieuXkhoBttReq extends BaseRequest {
    private Long id;
    private Integer namKh;
    private String maDvi;
    private String maQhNs;
    private String soPhieuXuatKho;
    private LocalDate ngayLapPhieu;
    private BigDecimal taiKhoanNo;
    private BigDecimal taiKhoanCo;
    private Long idQdNv;
    private String soQdNv;
    private LocalDate ngayKyQdNv;
    private Long idPhieuKiemNghiem;
    private String soPhieuKiemNghiem;
    private LocalDate ngayKiemNghiemMau;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String loaiHinhNx;
    private String kieuNx;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenHangHoa;
    private String donViTinh;
    private Long idBangKeHang;
    private String soBangKeHang;
    private Long idThuKho;
    private Long idKtvBaoQuan;
    private Long idLanhDaoChiCuc;
    private String keToanTruong;
    private LocalDate tgianGiaoNhan;
    private String tenNguoiGiao;
    private String cmtNguoiGiao;
    private String congTyNguoiGiao;
    private String diaChiNguoiGiao;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private BigDecimal soLuongHopDong;
    private Long idBangKeBanLe;
    private String soBangKeBanLe;
    private LocalDate ngayTaoBkeBanLe;
    private BigDecimal soLuongBkeBanLe;
    private String maSo;
    private BigDecimal theoChungTu;
    private BigDecimal thucXuat;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String ghiChu;
    private String pthucBanTrucTiep; // 01 : chào giá; 02 : Ủy quyền; 03 : Bán lẻ
    private String phanLoai;
    private String trangThai;
    private String lyDoTuChoi;
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
    private String dvql;
    private LocalDate ngayLapPhieuTu;
    private LocalDate ngayLapPhieuDen;
    private String maDviCha;
}