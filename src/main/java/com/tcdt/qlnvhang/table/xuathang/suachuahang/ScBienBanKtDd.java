package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = ScBienBanKtDd.TABLE_NAME)
public class ScBienBanKtDd {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "SC_BIEN_BAN_KT_DD";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScBienBanKtDd.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = ScBienBanKtDd.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScBienBanKtDd.TABLE_NAME + "_SEQ")
    private Long id;

    private Long idHdr;

    private String hoVaTen;

    private String chucVu;
}
