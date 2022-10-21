package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
    private String soQdPdCg;
    @Temporal(TemporalType.DATE)
    private Date ngayKy;
    @Temporal(TemporalType.DATE)
    private Date ngayHluc;
    private Long idQdPdKh;
    private String soQdPdKh;
    private String trichYeu;
    private String ghiChu;
    private String maDvi;
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
    private Date ngayPheDuyet;
    private String nguoiPduyet;

    @Transient
    private List<FileDinhKem> fileDinhKems =new ArrayList<>();
    @Transient
    private List<HhChiTietTTinChaoGia> hhChiTietTTinChaoGiaList = new ArrayList<>();
}
