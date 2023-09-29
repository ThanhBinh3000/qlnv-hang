package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = XhTlBienBanTkDtl.TABLE_NAME)
public class XhTlBienBanTkDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_BIEN_BAN_KT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlBienBanTkDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlBienBanTkDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlBienBanTkDtl.TABLE_NAME + "_SEQ")
    private Long id;

    private Long idHdr;

    private String soPhieuNhapKho;

    private Long idPhieuNhapKho;

    private LocalDate ngayNhapKho;

    private String soBangKeCanHang;

    private Long idBangKeCanHang;

    private BigDecimal tongSoLuong;

}
