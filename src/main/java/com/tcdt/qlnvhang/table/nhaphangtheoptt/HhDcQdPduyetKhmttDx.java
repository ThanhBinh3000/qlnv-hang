package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HH_DC_QD_PDUYET_KHMTT_DX")
@Data
public class HhDcQdPduyetKhmttDx implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_DC_QD_PDUYET_KHMTT_DX";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DC_QD_PDUYET_KHMTT_DX_SEQ")
    @SequenceGenerator(sequenceName = "HH_DC_QD_PDUYET_KHMTT_DX_SEQ", allocationSize = 1, name = "HH_DC_QD_PDUYET_KHMTT_DX_SEQ")

    private Long id;
    private Long idDxuat;
    private Long idDcHdr;
    private String maDvi;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String moTaHangHoa;
    private String ptMua;
    private String tchuanCluong;
    private BigDecimal giaMua;
    private BigDecimal giaChuaThue;
    private BigDecimal giaCoThue;
    private BigDecimal thueGtgt;
    @Temporal(TemporalType.DATE)
    private Date tgianMkho;
    @Temporal(TemporalType.DATE)
    private Date tgianKthuc;
    private String ghiChu;
    private BigDecimal tongMucDt;
    private BigDecimal tongSoLuong;
    private String nguonVon;
    private String tenChuDt;

    @Transient
    private  List<HhDcQdPduyetKhmttSldd> hhDcQdPduyetKhmttSlddList=new ArrayList<>();

}
