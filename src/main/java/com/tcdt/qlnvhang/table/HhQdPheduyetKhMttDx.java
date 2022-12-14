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
    private Long idDxHdr;
    private String maDvi;
    @Transient
    private String tenDvi;
    private String soDxuat;
    @Temporal(TemporalType.DATE)
    private Date ngayPduyet;
    private String tenDuAn;
    private BigDecimal tongSoLuong;
    private Integer namKh;
    private String trichYeu;
    private String diaChiDvi;
    private BigDecimal tongTienVat;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    @Column(name="SO_QD_PD_KQ_Mtt")
    private String soQdPdKqMtt;





    @Transient
    private HhQdPheduyetKhMttHdr hhQdPheduyetKhMttHdr;

    @Transient
    private HhDxuatKhMttHdr dxuatKhMttHdr;


    @Transient
    private List<HhQdPheduyetKhMttSLDD> children = new ArrayList<>();
}
