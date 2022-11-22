package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "XH_DX_KH_BAN_DAU_GIA_PHAN_LO ")
@Data
public class XhDxKhBanDauGiaPhanLo implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_DX_KH_BAN_DAU_GIA_PHAN_LO ";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_DX_KH_BAN_DAU_GIA_PL_SEQ")
    @SequenceGenerator(sequenceName = "XH_DX_KH_BAN_DAU_GIA_PL_SEQ", allocationSize = 1, name = "XH_DX_KH_BAN_DAU_GIA_PL_SEQ")
    private Long id;
    @Transient
    private Long idVirtual;

    private String maDvi;
    @Transient
    private String tenDvi;
    private String maDiemKho;
    @Transient
    private String tenDiemKho;
    private String maNhakho;
    @Transient
    private String tenNhakho;
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
    private BigDecimal soLuong;
    private String dviTinh;
    private BigDecimal giaKhongVat;
    private BigDecimal giaKhoiDiem;
    private BigDecimal tienDatTruoc;
    private Long idDxKhbdg;
    private BigDecimal  soLuongChiTieu;
    private BigDecimal soLuongKh;


    @Transient
    private List<XhDxKhBanDauGiaDtl> children = new ArrayList<>();
}
