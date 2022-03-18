package com.tcdt.qlnvhang.entities.dtvt;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "DT_NHA_THAU_VT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DtNhaThauVt extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 8168403816086814379L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DT_NHA_THAU_VT_SEQ")
    @SequenceGenerator(sequenceName = "DT_NHA_THAU_VT_SEQ", allocationSize = 1, name = "DT_NHA_THAU_VT_SEQ")
    private Long id;

    private Integer stt;
    private String ten;
    private String diaChi;
    private String soDienThoai;
    private Integer diem;
    private Integer xepHang;
    private BigDecimal donGiaDuThau;
    private BigDecimal giaDuThau;
    private String lyDo;
    private String thangThai;
}
