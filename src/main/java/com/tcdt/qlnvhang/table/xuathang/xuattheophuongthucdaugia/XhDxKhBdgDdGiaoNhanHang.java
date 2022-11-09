package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "XH_DX_KH_BDG_DD_GIAO_NHAN_HANG  ")
@Data
public class XhDxKhBdgDdGiaoNhanHang implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_DX_KH_BDG_DD_GIAO_NHAN_HANG  ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_DX_KH_BDG_DD_GNH_SEQ ")
    @SequenceGenerator(sequenceName = "XH_DX_KH_BDG_DD_GNH_SEQ ", allocationSize = 1, name = "XH_DX_KH_BDG_DD_GNH_SEQ ")
    private Long id;
    private Long idHdr;
    private String maDvi;
    private String tenDvi;
    private String diaChi;
}
