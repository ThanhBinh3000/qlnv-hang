package com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.qdpdnk;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhBbNghiemThuNhapKhac;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = HhQdPdNhapKhacDtl.TABLE_NAME)
@Data
public class HhQdPdNhapKhacDtl {
    public static final String TABLE_NAME = "HH_QD_PDNK_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_PDNK_DTL_SEQ")
    @SequenceGenerator(sequenceName = "HH_QD_PDNK_DTL_SEQ", allocationSize = 1, name = "HH_QD_PDNK_DTL_SEQ")
    private Long id;
    private Long idHdr;
    private Long idDxHdr;
    private BigDecimal tongSlNhap;
    private BigDecimal tongThanhTien;
    private String maCuc;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenCuc;
    private String maChiCuc;
    @Transient
    private String tenChiCuc;
    private String maDiemKho;
    @Transient
    private String tenDiemKho;
    private String maNhaKho;
    @Transient
    private String tenNhaKho;
    private String maNganKho;
    @Transient
    private String tenNganKho;
    private String maLoKho;
    @Transient
    private String tenLoKho;
    @Transient
    private String tenNganLoKho;
    @Transient
    private String tenCloaiVthh;
    private String cloaiVthh;
    private BigDecimal slTonKho;
    private BigDecimal slHaoDoiDinhMuc;
    private BigDecimal slDoiThua;
    private BigDecimal donGia;
    @Transient
    List<HhBbNghiemThuNhapKhac> bbNghiemThuNhapKhacList;
}
