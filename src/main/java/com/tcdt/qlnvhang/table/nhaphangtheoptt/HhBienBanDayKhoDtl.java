package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "HH_BIEN_BAN_DAY_KHO_DTL")
public class HhBienBanDayKhoDtl implements Serializable {
    private static final long serialVersionUID = 3529822360093876437L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_BIEN_BAN_DAY_KHO_DTL_SEQ")
    @SequenceGenerator(sequenceName = "HH_BIEN_BAN_DAY_KHO_DTL_SEQ", allocationSize = 1, name = "HH_BIEN_BAN_DAY_KHO_DTL_SEQ")
    private Long id;

    private Long idHdr;

    private String soPhieuKtraCl;

    private String soPhieuNhapKho;

    private String soBangKeCanHang;

    @Temporal(TemporalType.DATE)
    private Date ngayNkho;

    private BigDecimal soLuong;

}
