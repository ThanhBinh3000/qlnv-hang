package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = HhSlNhapHang.TABLE_NAME)
@Data
public class HhSlNhapHang extends BaseEntity {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_SL_NHAP_HANG";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_SL_NHAP_HANG_SEQ")
    @SequenceGenerator(sequenceName = "HH_SL_NHAP_HANG_SEQ", allocationSize = 1, name = "HH_SL_NHAP_HANG_SEQ")
    private Long id;
    private Long idDxKhlcnt;
    private Long idQdKhlcnt;
    private Integer namKhoach;
    private String maDvi;
    private String loaiVthh;
    private String cloaiVthh;
    private String kieuNhap;
    private BigDecimal soLuong;

    @Transient
    private String tenDvi;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
}
