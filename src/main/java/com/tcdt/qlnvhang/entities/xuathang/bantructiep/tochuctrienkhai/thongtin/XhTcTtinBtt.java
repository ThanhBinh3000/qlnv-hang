package com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = XhTcTtinBtt.TABLE_NAME)
@Data
public class XhTcTtinBtt implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TCTTIN_BTT";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTcTtinBtt.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTcTtinBtt.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTcTtinBtt.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idDviDtl;
    private Long idQdPdDtl;
    private String tochucCanhan;
    private String mst;
    private String diaDiemChaoGia;
    private String sdt;
    private LocalDate ngayChaoGia;
    private BigDecimal soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private Boolean luaChon;
    private String type;
    @Transient
    private FileDinhKem fileDinhKems;
}
