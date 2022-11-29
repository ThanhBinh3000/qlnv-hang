package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XH_QD_PD_KH_BDG_PL")
@Data
public class XhQdPdKhBdgPl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BDG_PL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_PD_KH_BDG_PL_SEQ ")
    @SequenceGenerator(sequenceName = "XH_QD_PD_KH_BDG_PL_SEQ ", allocationSize = 1, name = "XH_QD_PD_KH_BDG_PL_SEQ ")
    private Long id;
    private Long idQdDtl;
    private String maDvi;
    @Transient
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

    private String maDviTsan;

    private BigDecimal soLuong;

    private String DviTinh;

    private BigDecimal giaKhongVat;

    private BigDecimal giaKhoiDiem;

    private BigDecimal tienDatTruoc;

    private String trangThai;
    @Transient
    private String tenTrangThai;



    private BigDecimal soLuongChiTieu;

    private BigDecimal soLuongKh;

    private String maNhaKho;
    @Transient
    private String tenNhaKho;




    private String loaiVthh;
   private String cloaiVthh;
    @Transient
   private String tenCloaiVthh;
    @Transient
   private String tenLoaiVthh;
    @Transient
    private XhQdPdKhBdgDtl xhQdPdKhBdgDtl;

    @Transient
    private XhQdPdKhBdg xhQdPdKhBdg;

    @Transient
    List<XhQdPdKhBdgPlDtl> children =new ArrayList<>();
}
