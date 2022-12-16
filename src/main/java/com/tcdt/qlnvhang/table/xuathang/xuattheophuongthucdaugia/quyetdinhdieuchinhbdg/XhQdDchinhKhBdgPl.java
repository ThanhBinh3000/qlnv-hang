package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhQdPdKhBdg;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhQdPdKhBdgDtl;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhQdPdKhBdgPlDtl;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhQdDchinhKhBdgPl.TABLE_NAME)
@Data
public class XhQdDchinhKhBdgPl  extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_DC_KH_BDG_PL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = TABLE_NAME + "_SEQ", allocationSize = 1, name = TABLE_NAME + "_SEQ")
    private Long id;
    private Long idQdDtl;
    private String maDvi;
    @Transient
    private String tenDvi;
    private String maDiemKho;
    private String diaDiemKho;
    @Transient
    private String tenDiemKho;

    private String maNhaKho;
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

    private BigDecimal duDau;

    private BigDecimal soLuong;

    private BigDecimal giaKhongVat;

    private BigDecimal giaKhoiDiem;

    private BigDecimal donGiaVat;

    private BigDecimal giaKhoiDiemDduyet;

    private BigDecimal tienDatTruoc;

    private BigDecimal tienDatTruocDduyet;

    private BigDecimal soLuongChiTieu;

    private BigDecimal soLuongKh;

    private BigDecimal tongSoLuong;

    private BigDecimal tongTienDatTruoc;

    private BigDecimal tongTienDatTruocDd;
    private String dviTinh;

    private String trangThai;
    @Transient
    private String tenTrangThai;


    @Transient
    private XhQdDchinhKhBdgDtl xhQdPdKhBdgDtl;

    @Transient
    private XhQdDchinhKhBdgHdr xhQdPdKhBdg;

    @Transient
    List<XhQdDchinhKhBdgPlDtl> children =new ArrayList<>();
}
