package com.tcdt.qlnvhang.entities.nhaphang.dauthau.tochuctrienkhai;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdr;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = QdPdHsmt.TABLE_NAME)
@Data
public class QdPdHsmt implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_QD_PD_HSMT";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_PD_HSMT_SEQ")
    @SequenceGenerator(sequenceName = "HH_QD_PD_HSMT_SEQ", allocationSize = 1, name = "HH_QD_PD_HSMT_SEQ")
    private Long id;
    private Integer namKhoach;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    private String soQd;
    @Temporal(TemporalType.DATE)
    private Date ngayQd;
    @Temporal(TemporalType.DATE)
    private Date ngayHluc;
    private Long idQdPdKhlcnt;
    private Long idQdPdKhlcntDtl;
    private String trichYeu;
    private String noiDungQd;
    private String ghiChu;
    private String loaiVthh;
    private String maDvi;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
    Date tgianDthauTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
    Date tgianMthauTime;
    @Temporal(TemporalType.DATE)
    Date tgianBdauTchuc;
    @Temporal(TemporalType.DATE)
    private Date tgianMthau;
    @Temporal(TemporalType.DATE)
    private Date tgianDthau;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayPduyet;
    String nguoiPduyet;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngayTao;
    String nguoiTao;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date ngaySua;
    String nguoiSua;
    @Transient
    private List<FileDinhKem> listCcPhapLy;
    @Transient
    private HhQdKhlcntHdr qdKhlcntHdr;
}
