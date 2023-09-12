package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = DcnbBbThuaThieuKiemKeDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBbThuaThieuKiemKeDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BB_THUA_THIEU_KK_DTL";


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBbThuaThieuKiemKeDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBbThuaThieuKiemKeDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbBbThuaThieuKiemKeDtl.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;
    @NotNull
    private String hoVaTen;
    @NotNull
    private String chucVu;
    @NotNull
    private String daiDien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbBbThuaThieuHdr dcnbBbThuaThieuHdr;
}
