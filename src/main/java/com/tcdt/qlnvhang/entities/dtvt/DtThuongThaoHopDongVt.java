package com.tcdt.qlnvhang.entities.dtvt;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "DT_THUONG_THAO_HOP_DONG_VT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DtThuongThaoHopDongVt extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -2794531624425781518L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DT_THUONG_THAO_HOP_DONG_VT_SEQ")
    @SequenceGenerator(sequenceName = "DT_THUONG_THAO_HOP_DONG_VT_SEQ", allocationSize = 1, name = "DT_THUONG_THAO_HOP_DONG_VT_SEQ")
    private Long id;
    private LocalDate ngayKy;
    private Long nhaThauId; // DtNhaThauVt
    private BigDecimal donGiaTruocThue;
    private BigDecimal giaHopDongTruocThue;
    private Double vat;
    private BigDecimal donGiaSauThue;
    private BigDecimal giaHopDongSauThue;
    private String trangThai;
}
