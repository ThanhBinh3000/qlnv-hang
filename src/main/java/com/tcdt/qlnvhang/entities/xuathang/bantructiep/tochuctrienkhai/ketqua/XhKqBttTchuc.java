package com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "XH_KQ_BTT_TCHUC")
@Data
public class XhKqBttTchuc implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_KQ_BTT_TCHUC";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_KQ_BTT_TCHUC_SEQ")
    @SequenceGenerator(sequenceName = "XH_KQ_BTT_TCHUC_SEQ", allocationSize = 1, name = "XH_KQ_BTT_TCHUC_SEQ")
    private Long id;

    private Long idDdiem;

    private String tochucCanhan;

    private String mst;

    private String diaDiemChaoGia;

    private String sdt;

    private LocalDate ngayChaoGia;

    private BigDecimal soLuong;

    private BigDecimal donGia;

    private BigDecimal thueGtgt;

    private Boolean luaChon;

    @Transient
    private FileDinhKem fileDinhKems;

    @Transient
    private  XhKqBttDdiem xhKqBttDdiem;
}