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

    private Long idDxHdr;

    private String maDvi;

    private String soDxuat;

    private LocalDate ngayTao;

    private LocalDate ngayPduyet;

    private String trichYeu;

    private BigDecimal tongSoLuong;

    private Integer slDviTsan;

    private String moTaHangHoa;

    private String diaChi;

    private LocalDate tgianDkienTu;

    private LocalDate tgianDkienDen;

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

//    thông tin bán đấu giá

    private Integer nam;

    private String soQdPd;

    private String soQdPdKqBdg;

    private LocalDate ngayKyQdPdKqBdgTu;

    private LocalDate ngayKyQdPdKqBdgDen;

    private String loaiVthh;

    private Integer lastest ;
}
