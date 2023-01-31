package com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "BH_HOP_DONG_DDIEM_NHAP_KHO")
@Data
public class BhHopDongDdiemNhapKho {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BH_HD_DDIEM_NHAP_KHO_SEQ")
    @SequenceGenerator(sequenceName = "BH_HD_DDIEM_NHAP_KHO_SEQ", allocationSize = 1, name = "BH_HD_DDIEM_NHAP_KHO_SEQ")
    private Long id;
    String type;
    Long idHdongHdr;

    String maDvi;
    String maDiemKho;
    BigDecimal soLuong;
    BigDecimal donGia;
    String dviTinh;

    @Transient
    String tenDvi;
}
