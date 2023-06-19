package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbQuyetDinhDcTcDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbQuyetDinhDcTcDtl implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_QUYET_DINH_DC_TC_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbQuyetDinhDcTcDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbQuyetDinhDcTcDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbQuyetDinhDcTcDtl.TABLE_NAME + "_SEQ")
    private Long id;

    @Column(name = "DCNB_KE_HOACH_DC_HDR_ID")
    private Long keHoachDcHdrId;
    @Column(name = "HDR_ID")
    private Long hdrId;
    @Transient
    private DcnbKeHoachDcHdr dcnbKeHoachDcHdr;
    @Transient
    private List<DcnbKeHoachDcDtl> danhSachKeHoach = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbQuyetDinhDcTcHdr dcnbQuyetDinhDcTcHdr;
}
