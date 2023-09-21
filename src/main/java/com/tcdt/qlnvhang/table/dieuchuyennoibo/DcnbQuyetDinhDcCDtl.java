package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbQuyetDinhDcCDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbQuyetDinhDcCDtl implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_QUYET_DINH_DC_C_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbQuyetDinhDcCDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbQuyetDinhDcCDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbQuyetDinhDcCDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long parentId;
    @Column(name = "DCNB_KE_HOACH_DC_HDR_ID")
    private Long keHoachDcHdrId;
    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbQuyetDinhDcCHdr dcnbQuyetDinhDcCHdr;

    @Transient
    @JsonIgnore
    private DcnbKeHoachDcHdr dcnbKeHoachDcHdr;
    @Transient
    private List<DcnbKeHoachDcDtl> danhSachKeHoach = new ArrayList<>();
}
