package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = DcnbQuyetDinhDcTcDtl.TABLE_NAME)
@Getter
@Setter
public class DcnbQuyetDinhDcTcDtl implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_QUYET_DINH_DC_TC_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbQuyetDinhDcTcDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbQuyetDinhDcTcDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbQuyetDinhDcTcDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private String maCucDxuat;
    private String tenCucDxuat;
    private String maCucNhan;
    private String tenCucNhan;
    private String soDxuatCongVan;
    private LocalDate ngayTrinhDuyetTc;
    private BigDecimal tongDuToanKp;
    private String trichYeu;

    @Column(name = "DCNB_KE_HOACH_DC_HDR_ID")
    private Long keHoachDcHdrId;
    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbQuyetDinhDcTcHdr dcnbQuyetDinhDcTcHdr;
}
