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
    private Long idDxHdr;
    private Long idQdHdr;
    private String soDxuat;
    private Long idDcHdr;
    private String maDvi;
    private String namKh;
    @Transient
    private String tenDvi;
    private String diaChi;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    private Long idSoQdCc;
    private String soQdCc;
    @Transient
    private String tenCloaiVthh;
    private String moTaHangHoa;
    private String ptMua;
    private String tchuanCluong;
    private String giaMua;
    private BigDecimal donGia;
    private BigDecimal donGiaVat;
    private BigDecimal thueGtgt;
    @Temporal(TemporalType.DATE)
    private Date tgianMkho;
    @Temporal(TemporalType.DATE)
    private Date tgianKthuc;
    private String ghiChu;
    private BigDecimal tongMucDt;
    private BigDecimal tongSl;
    private BigDecimal tongSoLuong;
    private String nguonVon;
    @Transient
    private String tenNguonVon;
    private String tenChuDt;
    @Temporal(TemporalType.DATE)
    private Date ngayPduyet;
    private String trichYeu;
    private String tenDuAn;

    @Transient
    private  List<HhDcQdPduyetKhmttSldd> children=new ArrayList<>();


//    preview
    @Transient
    private String tgianKthucGoc;
    @Transient
    private String tgianKthucStr;
}
