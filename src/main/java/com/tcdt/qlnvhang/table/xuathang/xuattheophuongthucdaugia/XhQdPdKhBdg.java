package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "XH_QD_PD_KH_BDG")
@Data
public class XhQdPdKhBdg extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BDG";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_PD_KH_BDG_SEQ")
    @SequenceGenerator(sequenceName = "XH_QD_PD_KH_BDG_SEQ", allocationSize = 1, name = "XH_QD_PD_KH_BDG_SEQ")
    private Long id;
    private Integer namKh;
    private String maDvi;
    private String tenDvi;
    private String soQdPd;
    @Temporal(TemporalType.DATE)
    private Date ngayKyQd;
    @Temporal(TemporalType.DATE)
    private Date ngayHluc;
    private Long idThop;
    private String maThop;
    private Long idDxuat;
    private String soDxuat;
    private String trichYeu;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    @Temporal(TemporalType.DATE)
    private Date ngayGduyet;
    private String nguoiGduyetId;
    @Temporal(TemporalType.DATE)
    private Date ngayPduyet;
    private String nguoiPduyetId;
    private Integer soDviTsan;
    private Integer slHdDaKy;

    @Transient
    List<XhQdPdKhBdgDtl> qdPdKhBdgDtlList = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<FileDinhKem> canCuPhapLy = new ArrayList<>();
}
