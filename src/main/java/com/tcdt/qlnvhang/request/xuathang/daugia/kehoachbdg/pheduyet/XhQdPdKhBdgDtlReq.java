package com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class XhQdPdKhBdgDtlReq {

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

    private String thongBaoKh;

    private BigDecimal khoanTienDatTruoc;

    private String donViTinh;

    private List<XhQdPdKhBdgPlReq> children;
}
