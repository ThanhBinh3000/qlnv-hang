package com.tcdt.qlnvhang.table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDsgthau;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhChiTietTTinChaoGia;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdPduyetKqcgHdr;
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

    private String diaChiDvi;

    private String soDxuat;

    @Temporal(TemporalType.DATE)
    private Date ngayPduyet;

    private String trichYeu;

    private String tenDuAn;

    private BigDecimal tongSoLuong;

    private Integer namKh;

    private BigDecimal tongTienVat;

    private String trangThaiTkhai;
    @Transient
    private String tenTrangThaiTkhai;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private String ptMua;

    private String tchuanCluong;

    private String giaMua;

    private BigDecimal donGia;

    private BigDecimal thueGtgt;

    private BigDecimal donGiaVat;

    @Temporal(TemporalType.DATE)
    private Date tgianMkho;

    @Temporal(TemporalType.DATE)
    private Date tgianKthuc;

    private String ghiChu;

    private String nguonVon;

    private BigDecimal tongMucDt;

    @Column(name="SO_QD_PD_KQ_MTT")
    String soQdPdKqMtt;

    private String ptMuaTrucTiep;

    @Transient
    private HhQdPheduyetKhMttHdr hhQdPheduyetKhMttHdr;

    @Transient
    private HhDxuatKhMttHdr dxuatKhMttHdr;

    @Transient
    private HhQdPduyetKqcgHdr hhQdPduyetKqcgHdr;

    @Transient
    private List<HhChiTietTTinChaoGia> hhChiTietTTinChaoGiaList = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKemUyQuyen = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKemMuaLe = new ArrayList<>();



    @Transient
    private List<HhQdPheduyetKhMttSLDD> children = new ArrayList<>();
}
