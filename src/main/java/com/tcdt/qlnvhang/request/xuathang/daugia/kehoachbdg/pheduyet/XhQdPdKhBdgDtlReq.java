package com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class XhQdPdKhBdgDtlReq extends BaseRequest {
    private Long id;
    private Long idQdHdr;
    private Long idDxHdr;
    private String maDvi;
    private String loaiVthh;
    private String cloaiVthh;
    private String soDxuat;
    private LocalDate ngayTao;
    private LocalDate ngayPduyet;
    private LocalDate tgianDkienTu;
    private LocalDate tgianDkienDen;
    private String trichYeu;
    private BigDecimal tongSoLuong;
    private BigDecimal tongTienDatTruocDd;
    private Integer slDviTsan;
    private String moTaHangHoa;
    private String diaChi;
    private String trangThai;
    private Integer tgianTtoan;
    private String tgianTtoanGhiChu;
    private String pthucTtoan;
    private Integer tgianGnhan;
    private String tgianGnhanGhiChu;
    private String pthucGnhan;
    private String thongBao;
    private BigDecimal khoanTienDatTruoc;
    private String donViTinh;
    private List<XhQdPdKhBdgPlReq> children;
    //thông tin bán đấu giá
    private String soQdPdKqBdg;
    private Long idQdPdKqBdg;
    private LocalDate ngayKyQdPdKqBdg;
    private Integer soDviTsanThanhCong;
    private Integer soDviTsanKhongThanh;
    private String ketQuaDauGia;
    private String soQdDcBdg;
    private String dvql;
    private Integer nam;
    private String soQdPd;
    private LocalDate ngayKyQdPdKqBdgTu;
    private LocalDate ngayKyQdPdKqBdgDen;
    private Integer lastest;
}
