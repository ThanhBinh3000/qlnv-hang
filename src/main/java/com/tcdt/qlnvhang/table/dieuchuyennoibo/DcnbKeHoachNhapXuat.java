package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = DcnbKeHoachNhapXuat.TABLE_NAME)
@Getter
@Setter
public class DcnbKeHoachNhapXuat {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_KE_HOACH_XUAT_NHAP";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DCNB_KE_HOACH_XUAT_NHAP_SEQ")
    @SequenceGenerator(sequenceName =  "DCNB_KE_HOACH_XUAT_NHAP_SEQ", allocationSize = 1, name = "DCNB_KE_HOACH_XUAT_NHAP_SEQ")
    private Long id;

    @Column(name = "KH_DC_DTL_ID")
    private Long khDcDtlId;

    @Column(name = "ID_HDR")
    private Long idHdr;

    @Column(name = "TABLE_NAME")
    private String tableName;

}
