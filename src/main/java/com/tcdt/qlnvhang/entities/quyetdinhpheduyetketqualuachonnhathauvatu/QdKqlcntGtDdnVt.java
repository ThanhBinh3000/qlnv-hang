package com.tcdt.qlnvhang.entities.quyetdinhpheduyetketqualuachonnhathauvatu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "QD_KQLCNT_GT_DDN_VT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QdKqlcntGtDdnVt implements Serializable {

    private static final long serialVersionUID = 9097427316502901182L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QD_KQLCNT_GT_DDN_VT_SEQ")
    @SequenceGenerator(sequenceName = "QD_KQLCNT_GT_DDN_VT_SEQ", allocationSize = 1, name = "QD_KQLCNT_GT_DDN_VT_SEQ")
    private Long id;
    private Long goiThauId; // QD_KQLCNT_GOI_THAU_VT
    private Long donViId; //QLNV_DM_DONVI
    private String maDonVi;
    private BigDecimal soLuongNhap;
    private Integer stt;
}
