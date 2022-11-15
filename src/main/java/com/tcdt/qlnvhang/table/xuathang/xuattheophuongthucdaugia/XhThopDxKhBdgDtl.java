package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "XH_THOP_DX_KH_BDG_DTL  ")
@Data
public class XhThopDxKhBdgDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_THOP_DX_KH_BDG_DTL  ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_THOP_DX_KH_BDG_DTL_SEQ  ")
    @SequenceGenerator(sequenceName = "XH_THOP_DX_KH_BDG_DTL_SEQ  ", allocationSize = 1, name = "XH_THOP_DX_KH_BDG_DTL_SEQ  ")
    private Long id;
    private Long idHdr;
    private String maDvi;
    @Transient
    private String tenDvi;
    private Long idDxuat;
    private String soDxuat;
    @Temporal(TemporalType.DATE)
    private Date ngayKy;
    private String trichYeu;
    private Integer soDviTsan;
    private BigDecimal tongTienKdiem;
    private BigDecimal tongTienDatTruoc;

    @Transient
    private XhDxKhBanDauGia dxKhBanDauGia ;
}
