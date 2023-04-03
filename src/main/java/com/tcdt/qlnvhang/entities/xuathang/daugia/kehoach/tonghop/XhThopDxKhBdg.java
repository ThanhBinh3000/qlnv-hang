package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "XH_THOP_DX_KH_BAN_DAU_GIA")
@Data
public class XhThopDxKhBdg extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_THOP_DX_KH_BAN_DAU_GIA";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_THOP_DX_KH_BDG_SEQ")
//    @SequenceGenerator(sequenceName = "XH_THOP_DX_KH_BDG_SEQ", allocationSize = 1, name = "XH_THOP_DX_KH_BDG_SEQ")
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayThop;
    private String loaiVthh;
    private String cloaiVthh;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayDuyetTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayDuyetDen;
    private String noiDungThop;
    private Integer namKh;
    private String maDvi;

    private Long idQdPd;
    private String soQdPd;

    // Transient
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private List<XhThopDxKhBdgDtl> children = new ArrayList<>();

}
