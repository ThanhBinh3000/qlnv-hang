package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = ScBienBanKtDtl.TABLE_NAME)
public class ScBienBanKtDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "SC_BIEN_BAN_KT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScBienBanKtDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = ScBienBanKtDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScBienBanKtDtl.TABLE_NAME + "_SEQ")
    private Long id;

    private Long idHdr;

    private String soPhieuNhapKho;

    private Long idPhieuNhapKho;

    private LocalDate ngayNhapKho;

    private String soBangKeCanHang;

    private Long idBangKeCanHang;

    private BigDecimal tongSoLuong;

}
