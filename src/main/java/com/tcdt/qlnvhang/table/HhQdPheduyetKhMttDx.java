package com.tcdt.qlnvhang.table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttHdr;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HH_QD_PHE_DUYET_KHMTT_DX")
@Data

public class HhQdPheduyetKhMttDx implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_QD_PHE_DUYET_KHMTT_DX";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_PHE_DUYET_KHMTT_DX_SEQ")
    @SequenceGenerator(sequenceName = "HH_QD_PHE_DUYET_KHMTT_DX_SEQ", allocationSize = 1, name = "HH_QD_PHE_DUYET_KHMTT_DX_SEQ")

    private Long id;
    private Long idQdHdr;
    private String maDvi;
    @Transient
    private String tenDvi;
    private String soDxuat;
    @Temporal(TemporalType.DATE)
    private Date ngayTao;
    @Temporal(TemporalType.DATE)
    private Date ngayPduyet;
    private String tenDuAn;
    private BigDecimal soLuong;
    private BigDecimal donGiaVat;
    private BigDecimal donGiaTamTinh;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String namKh;
    private Long idDxHdr;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    private String diaChiDvi;
    private String trichYeu;
    @Column(name="SO_QD_PD_KQ_Mtt")
    private String soQdPdKqMtt;

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
    private HhQdPheduyetKhMttHdr hhQdPheduyetKhMttHdr;

    @Transient
    private HhDxuatKhMttHdr dxuatKhMttHdr;


    @Transient
    private List<HhQdPheduyetKhMttSLDD> children = new ArrayList<>();
}
