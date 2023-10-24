package com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbantinhkho;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhBbTinhkBttHdrReq extends BaseRequest {
    private Long id;
    private Integer namKh;
    private String maDvi;
    private String maQhNs;
    private String soBbTinhKho;
    private LocalDate ngayLapBienBan;
    private Long idQdNv;
    private String soQdNv;
    private LocalDate ngayKyQdNv;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenHangHoa;
    private String donViTinh;
    private LocalDate ngayBatDauXuat;
    private LocalDate ngayKetThucXuat;
    private Long idPhieuKiemNghiem;
    private String soPhieuKiemNghiem;
    private LocalDate ngayKiemNghiemMau;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private BigDecimal soLuongHopDong;
    private Long idBangKeBanLe;
    private String soBangKeBanLe;
    private LocalDate ngayTaoBkeBanLe;
    private BigDecimal soLuongBkeBanLe;
    private BigDecimal tongSlNhap;
    private BigDecimal tongSlXuat;
    private BigDecimal slConLai;
    private BigDecimal slThucTe;
    private BigDecimal slThua;
    private BigDecimal slThieu;
    private String nguyenNhan;
    private String kienNghi;
    private String ghiChu;
    private Long idThuKho;
    private Long idKtvBaoQuan;
    private Long idKeToan;
    private Long idLanhDaoChiCuc;
    private String loaiHinhNx;
    private String kieuNx;
    private LocalDate tgianGiaoNhan;
    private String pthucBanTrucTiep; // 01 : chào giá; 02 : Ủy quyền; 03 : Bán lẻ
    private String phanLoai;
    private String trangThai;
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
    private List<XhBbTinhkBttDtlReq> children = new ArrayList<>();
    private String dvql;
    private String maDviCha;
    private LocalDate ngayLapBienBanTu;
    private LocalDate ngayLapBienBanDen;
    private LocalDate ngayBatDauXuatTu;
    private LocalDate ngayBatDauXuatDen;
    private LocalDate ngayKetThucXuatTu;
    private LocalDate ngayKetThucXuatDen;
    private LocalDate tgianGiaoNhanTu;
    private LocalDate tgianGiaoNhanDen;
}