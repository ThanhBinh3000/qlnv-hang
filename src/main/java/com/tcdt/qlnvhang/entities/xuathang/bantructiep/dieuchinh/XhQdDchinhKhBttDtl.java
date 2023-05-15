package com.tcdt.qlnvhang.entities.xuathang.bantructiep.dieuchinh;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhQdDchinhKhBttDtl.TABLE_NAME)
@Data
public class XhQdDchinhKhBttDtl implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_DC_KH_BTT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = TABLE_NAME + "_SEQ", allocationSize = 1, name = TABLE_NAME + "_SEQ")

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
    private List<XhQdDchinhKhBttSl> children= new ArrayList<>();
}
