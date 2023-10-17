package com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bienbantinhkho;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = XhBbTinhkBttDtl.TABLE_NAME)
@Data
public class XhBbTinhkBttDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_BB_TINHK_BTT_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = TABLE_NAME + "_SEQ", allocationSize = 1, name = TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private Long idPhieuXuatKho;
    private String soPhieuXuatKho;
    private LocalDate ngayXuatKho;
    private Long idBangKeHang;
    private String soBangKeHang;
    private BigDecimal soLuongXuat;
}