package com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class XhQdPdKhBdgDtlReq extends BaseRequest {
    private Long id;
    private Long idHdr;
    private Integer nam;
    private Long idDxHdr;
    private String maDvi;
    private String diaChi;
    private String soDxuat;
    private LocalDate ngayTao;
    private LocalDate ngayPduyet;
    private String trichYeu;
    private Integer slDviTsan;
    private BigDecimal tongSoLuong;
    private LocalDate tgianDkienTu;
    private LocalDate tgianDkienDen;
    private String loaiHopDong;
    private Integer thoiGianKyHdong;
    private String thoiGianKyHdongGhiChu;
    private Integer tgianTtoan;
    private String tgianTtoanGhiChu;
    private String pthucTtoan;
    private Integer tgianGnhan;
    private String tgianGnhanGhiChu;
    private String pthucGnhan;
    private String thongBao;
    private BigDecimal khoanTienDatTruoc;
    private String donViTinh;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private BigDecimal tongTienKhoiDiemDx;
    private BigDecimal tongTienDatTruocDx;
    private BigDecimal tongTienDuocDuyet;
    private BigDecimal tongKtienDtruocDduyet;
    private String trangThai;
    private List<XhQdPdKhBdgPlReq> children;
    //thông tin bán đấu giá
    private Long idQdKq;
    private String soQdKq;
    private LocalDate ngayKyQdKq;
    private String soQdDc;
    private String soQdPd;
    private BigDecimal soDviTsanThanhCong;
    private BigDecimal soDviTsanKhongThanh;
    private String ketQuaDauGia;
    private String dvql;
    private LocalDate ngayKyQdKqTu;
    private LocalDate ngayKyQdKqDen;
    private Integer lastest;
    private String trangThaiHdr;
    private Integer lanDieuChinh;
}