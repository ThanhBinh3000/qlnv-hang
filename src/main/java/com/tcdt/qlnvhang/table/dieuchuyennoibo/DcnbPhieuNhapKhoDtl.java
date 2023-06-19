package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = DcnbPhieuNhapKhoDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbPhieuNhapKhoDtl extends BaseEntity implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_PHIEU_NHAP_KHO_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbPhieuNhapKhoDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbPhieuNhapKhoDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbPhieuNhapKhoDtl.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;
    private String noiDung;
    private String maSo;
    private String dviTinh;
    private BigDecimal soLuongNhapDc;
    private BigDecimal duToanKinhPhi;
    private BigDecimal thucTeKinhPhi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbPhieuNhapKhoHdr parent;
}
