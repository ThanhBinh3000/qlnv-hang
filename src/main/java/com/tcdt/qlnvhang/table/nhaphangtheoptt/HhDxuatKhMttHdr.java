package com.tcdt.qlnvhang.table.nhaphangtheoptt;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinDxKhLcntHdr;
import com.tcdt.qlnvhang.entities.FileDKemJoinDxKhMttHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxKhlcntDsgthau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntCcxdgDtl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "HH_DX_KHMTT_HDR")
@Data
public class HhDxuatKhMttHdr  implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_DX_KHMTT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DX_KHMTT_HDR_SEQ")
    @SequenceGenerator(sequenceName = "HH_DX_KHMTT_HDR_SEQ", allocationSize = 1, name = "HH_DX_KHMTT_HDR_SEQ")
    private Long id;
    private String maDvi;
    @Transient
    private String tenDvi;

    private String loaiHinhNx;
    private String kieuNx;
    private String diaChiDvi;
    private Integer namKh;
    private String soDxuat;
    private String trichYeu;
    @Temporal(TemporalType.DATE)
    private Date ngayTao;
    private String nguoiTao;
    @Temporal(TemporalType.DATE)
    private Date ngayPduyet;
    private String nguoiPduyet;
    @Temporal(TemporalType.DATE)
    private Date ngaySua;
    private  String nguoiSua;
    @Temporal(TemporalType.DATE)
    private Date ngayGuiDuyet;
    private String nguoiGuiDuyet;

    private String tenDuAn;
    private String soQd;
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
    private String thueGtgt;
    @Temporal(TemporalType.DATE)
    private Date tgianMkho;
    @Temporal(TemporalType.DATE)
    private Date tgianKthuc;
    private String ghiChu;
    private BigDecimal tongMucDt;
    private BigDecimal tongSoLuong;
    private BigDecimal tongTienVat;
    private String nguonVon;

    private String trangThai;
    @Transient
    private String tenTrangThai;

    private String trangThaiTh;
    @Transient
    private String tenTrangThaiTh;
    private String ldoTuchoi;

    @Transient
    private String soQdPdkqMtt;
    @Transient
    private Long idQdPdkqMtt;
    private String maThop;
    private BigDecimal donGia;
    private BigDecimal donGiaVat;
    private String soQdPduyet;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @JsonManagedReference
    @Where(clause = "data_type='" + HhDxuatKhMttHdr.TABLE_NAME + "'")
    private List<FileDKemJoinDxKhMttHdr> fileDinhKems = new ArrayList<>();

    public void setFileDinhKems(List<FileDKemJoinDxKhMttHdr> children) {
        this.fileDinhKems.clear();
        for (FileDKemJoinDxKhMttHdr child : children) {
            child.setParent(this);
        }
        this.fileDinhKems.addAll(children);
    }
    @Transient
    private List<HhDxuatKhMttSldd> dsSlddDtlList = new ArrayList<>();

    @Transient
    private List<HhDxuatKhMttCcxdg> ccXdgDtlList = new ArrayList<>();


}
