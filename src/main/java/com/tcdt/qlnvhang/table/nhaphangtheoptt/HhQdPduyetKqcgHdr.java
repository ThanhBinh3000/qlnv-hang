package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinKquaLcntHdr;
import com.tcdt.qlnvhang.entities.FileDKemJoinKquaMttHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdr;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.HhQdPduyetKqlcntHdr;
import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttDx;
import com.tcdt.qlnvhang.table.HhQdPheduyetKhMttHdr;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HH_QD_PDUYET_KQCG_HDR")
@Getter
@Setter
public class HhQdPduyetKqcgHdr implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_QD_PDUYET_KQCG_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_PDUYET_KQCG_SEQ")
    @SequenceGenerator(sequenceName = "HH_QD_PDUYET_KQCG_SEQ", allocationSize = 1, name = "HH_QD_PDUYET_KQCG_SEQ")
    private Long id;
    private Integer namKh;
    private String soQd;
    @Temporal(TemporalType.DATE)
    private Date ngayKy;
    @Temporal(TemporalType.DATE)
    private Date ngayHluc;
    private Long idQdPdKh;
    private Long idQdPdKhDtl;
    private String soQdPdKh;
    private String trichYeu;
    private String ghiChu;
    private String maDvi;
    @Transient
    private String tenDvi;
    private String diaChiCgia;
    @Temporal(TemporalType.DATE)
    private Date tgianMkho;
    @Temporal(TemporalType.DATE)
    private Date tgianKthuc;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String moTaHangHoa;
    private String trangThai;
    private String trangThaiHd;
    @Transient
    private String tenTrangThai;
    @Temporal(TemporalType.DATE)
    private Date ngayTao;
    private String nguoiTao;
    @Temporal(TemporalType.DATE)
    private Date ngaySua;
    private  String nguoiSua;
    private String ldoTuchoi;
    @Temporal(TemporalType.DATE)
    private Date ngayGduyet;
    private String nguoiGduyet;
    @Temporal(TemporalType.DATE)
    @Column(name="ngayPduyet")
    private Date ngayPduyet;
    private String nguoiPduyet;

    @Transient
    HhQdPheduyetKhMttHdr hhQdPheduyetKhMttHdr;

    @Transient
    HhQdPheduyetKhMttDx hhQdPheduyetKhMttDx;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @JsonManagedReference
    @Where(clause = "data_type='" + HhQdPduyetKqcgHdr.TABLE_NAME + "'")
    private List<FileDKemJoinKquaMttHdr> children = new ArrayList<>();

    public void setChildren(List<FileDKemJoinKquaMttHdr> children) {
        this.children.clear();
        for (FileDKemJoinKquaMttHdr child : children) {
            child.setParent(this);
        }
        this.children.addAll(children);
    }

    public void addChild(FileDKemJoinKquaMttHdr child) {
        child.setParent(this);
        this.children.add(child);
    }
}
