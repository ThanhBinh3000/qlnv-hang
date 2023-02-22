package com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.thongtin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "XH_TCTTIN_BTT")
@Data
public class XhTcTtinBtt implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TCTTIN_BTT";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_TCTTIN_BTT_SEQ")
    @SequenceGenerator(sequenceName = "XH_TCTTIN_BTT_SEQ", allocationSize = 1, name = "XH_TCTTIN_BTT_SEQ")
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
