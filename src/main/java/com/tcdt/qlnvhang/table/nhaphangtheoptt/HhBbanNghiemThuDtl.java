package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HH_BB_NGHIEM_THU_DTL")
@Data
public class HhBbanNghiemThuDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_BB_NGHIEM_THU_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_BB_NGHIEM_THU_DTL_SEQ")
    @SequenceGenerator(sequenceName = "HH_BB_NGHIEM_THU_DTL_SEQ", allocationSize = 1, name = "HH_BB_NGHIEM_THU_DTL_SEQ")
    private Long id;
    private Long idHdr;
    private String type;
    private String noiDung;
    private String danhMuc;
    private String nhomHang;
    private String donViTinh;
    private String matHang;
    private String tenMatHang;
    private String donViTinhMh;
    private BigDecimal tongGiaTri;
    private BigDecimal soLuongTrongNam;
    private BigDecimal donGia;
    private BigDecimal thanhTienTrongNam;
    private BigDecimal soLuongNamTruoc;
    private BigDecimal thanhTienNamTruoc;
    private Boolean isParent;
    private String idParent;

    @Transient
    private List<HhBbanNghiemThuDtl> children = new ArrayList<>();
    @Transient
    private String tongGiaTriStr;
    @Transient
    private String thanhTienTrongNamStr;
    @Transient
    private String thanhTienNamTruocStr;

}
