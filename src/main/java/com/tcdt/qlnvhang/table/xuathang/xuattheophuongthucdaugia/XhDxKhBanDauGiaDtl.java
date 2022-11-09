package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "XH_DX_KH_BAN_DAU_GIA_DTL ")
@Data
public class XhDxKhBanDauGiaDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_DX_KH_BAN_DAU_GIA_DTL ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_DX_KH_BAN_DAU_GIA_DTL_SEQ")
    @SequenceGenerator(sequenceName = "XH_DX_KH_BAN_DAU_GIA_DTL_SEQ", allocationSize = 1, name = "XH_DX_KH_BAN_DAU_GIA_DTL_SEQ")
    private Long id;
    private Long idHdr;
    private String maDvi;
    private String tenDvi;
    private String maDiemKho;
    @Transient
    private String tenDiemKho;
    private String maNganKho;
    @Transient
    private String tenNganKho;
    private String maLoKho;
    @Transient
    private String tenLoKho;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String maDviTsan;
    private String soLuong;
    private String DviTinh;
    private String giaKhongVat;
    private String giaKhoiDiem;
    private String tienDatTruoc;

}
