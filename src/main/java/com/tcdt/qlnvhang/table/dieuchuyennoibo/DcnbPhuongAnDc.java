package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = DcnbPhuongAnDc.TABLE_NAME)
@Getter
@Setter
public class DcnbPhuongAnDc implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_PHUONG_AN_DC_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbPhuongAnDc.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbPhuongAnDc.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbPhuongAnDc.TABLE_NAME + "_SEQ")
    private Long id;
    private String maChiCucNhan;
    private String dviVanChuyen;
    private String pthucGiaoHang;
    private String pthucNhanHang;
    private String nguonChi;

    @Transient
    private String tenChiCucNhan;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID")
    @JsonIgnore
    private DcnbKeHoachDcHdr dcnbKeHoachDcHdr;
}
