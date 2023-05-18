package com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class XhQdPdKhBttDtlReq {

    private Long id;

    private Long idQdHdr;

    private Long idDxHdr;

    private String maDvi;

    private String soDxuat;

    private LocalDate ngayTao;

    private LocalDate ngayPduyet;

    private LocalDate tgianDkienTu;

    private LocalDate tgianDkienDen;

    private String trichYeu;

    private BigDecimal tongSoLuong;

    private Integer slDviTsan;

    private String moTaHangHoa;

    private String diaChi;

    private Integer tgianTtoan;

    private String tgianTtoanGhiChu;

    private String pthucTtoan;

    private Integer tgianGnhan;

    private String tgianGnhanGhiChu;

    private String pthucGnhan;

    private String thongBaoKh;

    private List<XhQdPdKhBttDviReq> children;
}
