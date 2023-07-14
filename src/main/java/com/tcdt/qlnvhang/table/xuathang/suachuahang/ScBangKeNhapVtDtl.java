package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeNhapVTHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanLayMauDtl;
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
@Table(name = ScBangKeNhapVtDtl.TABLE_NAME)
public class ScBangKeNhapVtDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "SC_BANG_KE_NHAP_VT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScBangKeNhapVtDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = ScBangKeNhapVtDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScBangKeNhapVtDtl.TABLE_NAME + "_SEQ")
    private Long idHdr;

    private String soSerial;

    private BigDecimal soLuong;
}
