package com.tcdt.qlnvhang.request.xuathang.bantructiep.dieuchinh;
import lombok.Data;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdDchinhKhBttDtlReq  {

    private Long id;

    private Long idDcHdr;

    private Long idQdGoc;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String diaChi;

    private String soDxuat;

    private LocalDate ngayPduyet;

    private String trichYeu;

    private String tenDuAn;

    private BigDecimal tongSoLuong;

    private LocalDate tgianDkienTu;

    private LocalDate tgianDkienDen;

    private Integer tgianTtoan;

    private String tgianTtoanGhiChu;

    private String pthucTtoan;

    private Integer tgianGnhan;

    private String tgianGnhanGhiChu;

    private String pthucGnhan;

    private String thongBaoKh;

    @Transient
    List<XhQdDchinhKhBttSlReq> children = new ArrayList<>();
}
