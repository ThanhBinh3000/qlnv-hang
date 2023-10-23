package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "HH_CTIET_TTIN_CHAO_GIA")
@Data
public class HhChiTietTTinChaoGia implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_CTIET_TTIN_CHAO_GIA";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_CTIET_TTIN_CHAO_GIA_SEQ")
    @SequenceGenerator(sequenceName = "HH_CTIET_TTIN_CHAO_GIA_SEQ", allocationSize = 1, name = "HH_CTIET_TTIN_CHAO_GIA_SEQ")
    private Long id;

    private Long idQdPdSldd;

    private String canhanTochuc;

    private String mst;

    private String diaChi;

    private String sdt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayChaoGia;

    private BigDecimal soLuong;

    private BigDecimal donGia;
    @Transient
    private BigDecimal donGiaVat;

    private BigDecimal thueGtgt;

    private BigDecimal thanhTien;

    private Boolean luaChon;

    @Transient
    private FileDinhKem fileDinhKems ;
}
