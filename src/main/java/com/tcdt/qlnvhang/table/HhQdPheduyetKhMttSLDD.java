package com.tcdt.qlnvhang.table;

import com.tcdt.qlnvhang.entities.bandaugia.kehoachbanhangdaugia.BanDauGiaDiaDiemGiaoNhan;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthauCtiet;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDtl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdPdKhMttSlddDtl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="HH_QD_PHE_DUYET_KHMTT_SLDD")
@Data

public class HhQdPheduyetKhMttSLDD implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_QD_PHE_DUYET_KHMTT_SLDD";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_PHE_DUYET_KHMTT_SLDD_SEQ")
    @SequenceGenerator(sequenceName = "HH_QD_PHE_DUYET_KHMTT_SLDD_SEQ", allocationSize = 1, name = "HH_QD_PHE_DUYET_KHMTT_SLDD_SEQ")

    private Long id;
    private Long idQdDtl;
    private BigDecimal soLuong;

    private String maDvi;
    @Transient
    private String tenDvi;
    private BigDecimal donGiaVat;
    private BigDecimal thanhTien;
    private BigDecimal donGiaTamTinh;
    private String loaiVthh;
    private String cloaiVthh;
    private String maDiemKho;
    private String diaDiemNhap;
    private BigDecimal soLuongChiTieu;
    private BigDecimal soLuongKhDd;
    private String trangThai;
    @Transient
    String tenTrangThai;
    private String lyDoHuy;

    @Transient
    private HhQdPheduyetKhMttDx hhQdPheduyetKhMttDx;

    @Transient
    private HhQdPheduyetKhMttHdr hhQdPheduyetKhMttHdr;


    @Transient
    private List<HhQdPdKhMttSlddDtl> children = new ArrayList<>();

}
