package com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "BH_HOP_DONG_DTL")
@Data
public class BhHopDongDtl {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BH_HOP_DONG_DTL_SEQ")
    @SequenceGenerator(sequenceName = "BH_HOP_DONG_DTL_SEQ", allocationSize = 1, name = "BH_HOP_DONG_DTL_SEQ")
    private Long id;
    private Long idHdr;
    String shgt;
    String tenGthau;
    BigDecimal soLuong;
    BigDecimal donGia;
    Long vat;
    BigDecimal giaTruocVat;
    BigDecimal giaSauVat;
    String type;

}
