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
    private String maDvi;
    @Transient
    private String tenDvi;

    private String maDiemKho;

    @Transient
    private String tenDiemKho;

    private String diaDiemNhap;

    private BigDecimal soLuongChiTieu;

    private BigDecimal soLuongKhDd;

    private BigDecimal soLuong;

    private BigDecimal donGia;

    private BigDecimal thanhTien;

    private String loaiVthh;

    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;

    @Transient
    private String tenCloaiVthh;

    private BigDecimal donGiaVat;

    private BigDecimal thanhTienVat;

    private BigDecimal tongSoLuong;

    private BigDecimal tongDonGia;

    private BigDecimal tongThanhTien;

    private String trangThai;
    @Transient
    String tenTrangThai;


    @Transient
    private HhQdPheduyetKhMttDx hhQdPheduyetKhMttDx;

    @Transient
    private HhQdPheduyetKhMttHdr hhQdPheduyetKhMttHdr;


    @Transient
    private List<HhQdPdKhMttSlddDtl> children = new ArrayList<>();

}
