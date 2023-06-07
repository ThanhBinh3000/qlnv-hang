package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = DcnbBbNhapDayKhoDtl.TABLE_NAME)
@Getter
@Setter
public class DcnbBbNhapDayKhoDtl extends BaseEntity implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BB_NHAP_DAY_KHO_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBbNhapDayKhoDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBbNhapDayKhoDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbBbNhapDayKhoDtl.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;
    private String phieuKnghiemCluong;
    private Long idPhieuKnghiemCluong;
    private String phieuNhapKho;
    private Long idPhieuNhapKho;
    private String soBangKeCh;
    private Long idBangKeCh;
    private BigDecimal soLuong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbBbNhapDayKhoHdr parent;
}
