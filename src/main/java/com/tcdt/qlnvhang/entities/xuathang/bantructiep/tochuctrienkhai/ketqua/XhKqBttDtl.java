package com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "XH_KQ_BTT_DTL")
@Data
public class XhKqBttDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_KQ_BTT_DTL ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_KQ_BTT_DTL_SEQ")
    @SequenceGenerator(sequenceName = "XH_KQ_BTT_DTL_SEQ", allocationSize = 1, name = "XH_KQ_BTT_DTL_SEQ")
    private Long id;

    private Long idHdr;

    private String tochucCanhan;

    private String mst;

    private String diaDiemChaoGia;

    private String sdt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayChaoGia;

    private BigDecimal soLuong;

    private BigDecimal donGia;

    private String thueGtgt;

    private Boolean luaChon;

    @Transient
    private FileDinhKem fileDinhKems;
}
