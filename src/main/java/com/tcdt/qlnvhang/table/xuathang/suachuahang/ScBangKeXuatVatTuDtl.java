package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeXuatVTDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeXuatVTHdr;
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
@Table(name = ScBangKeXuatVatTuDtl.TABLE_NAME)
public class ScBangKeXuatVatTuDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "SC_BANG_KE_XUAT_VT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScBangKeXuatVatTuDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = ScBangKeXuatVatTuDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScBangKeXuatVatTuDtl.TABLE_NAME + "_SEQ")
    @Column(name = "ID")
    private Long id;

    private Long idHdr;

    @Column(name = "SO_SERIAL")
    private String soSerial;

    @Column(name = "SO_LUONG")
    private BigDecimal soLuong;


}
