package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private String canhanTochuc;
    private String mst;
    private String diaChi;
    private String sdt;
    @Temporal(TemporalType.DATE)
    private Date ngayChaoGia;
    private BigDecimal soLuong;
    private BigDecimal dgiaChuaThue;
    private BigDecimal thueGtgt;
    private BigDecimal thanhTien;
    private Boolean luaChon;
    private Boolean luaChonPduyet;
    private Long idSoQdPduyetCgia;
    private Long idTkhaiKh;
    @Transient
    private FileDinhKem fileDinhKems ;
}
