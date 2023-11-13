package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    @Column(name = "HDR_ID")
    private Long hdrId;
    @NotNull
    private String maCucXuat;
    @NotNull
    private String tenCucXuat;
    @NotNull
    private String maCucNhan;
    @NotNull
    private String tenCucNhan;
    private String soDxuat;
    private LocalDate ngayTrinhTc;
    private BigDecimal tongDuToanKp;
    private String trichYeu;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbQuyetDinhDcTcHdr dcnbQuyetDinhDcTcHdr;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbQuyetDinhDcTcTTDtl> danhSachQuyetDinhChiTiet = new ArrayList<>();
}
