package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "XH_THOP_DX_KH_BAN_DAU_GIA")
@Data
public class XhThopDxKhBdg implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_THOP_DX_KH_BAN_DAU_GIA";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_THOP_DX_KH_BDG_SEQ")
    @SequenceGenerator(sequenceName = "XH_THOP_DX_KH_BDG_SEQ", allocationSize = 1, name = "XH_THOP_DX_KH_BDG_SEQ")
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date ngayThop;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String moTaHangHoa;
    private String loaiHdong;
    @Transient
    private String tenLoaiHdong;
    @Transient
    private Long phuongAnId;

    @Temporal(TemporalType.DATE)
    private Date tgianDkienTu;
    @Temporal(TemporalType.DATE)
    private Date tgianDkienDen;

    @Temporal(TemporalType.DATE)
    private Date ngayTao;

    private String noiDungThop;
    private String ghiChu;
    private String nguoiTao;
    private Integer namKh;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    private String maDvi;

    private String soQdPd;

    @Transient
    private List<XhThopDxKhBdgDtl> thopDxKhBdgDtlList= new ArrayList<>();
}
