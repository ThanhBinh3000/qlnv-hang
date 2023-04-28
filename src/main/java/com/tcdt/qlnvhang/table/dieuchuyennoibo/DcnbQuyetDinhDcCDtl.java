package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbQuyetDinhDcCDtl.TABLE_NAME)
@Getter
@Setter
public class DcnbQuyetDinhDcCDtl implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_QUYET_DINH_DC_C_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbQuyetDinhDcCDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbQuyetDinhDcCDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbQuyetDinhDcCDtl.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "DCNB_KE_HOACH_DC_HDR_ID", insertable = false, updatable = false)
    private Long keHoachDcHdrId;
    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbQuyetDinhDcCHdr dcnbQuyetDinhDcCHdr;

    @Transient
    private DcnbKeHoachDcHdr dcnbKeHoachDcHdr;
    @Transient
    private List<DcnbKeHoachDcDtl> danhSachKeHoach = new ArrayList<>();
}
