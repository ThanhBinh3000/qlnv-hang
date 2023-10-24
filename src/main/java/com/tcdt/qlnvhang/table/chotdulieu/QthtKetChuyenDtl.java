package com.tcdt.qlnvhang.table.chotdulieu;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = QthtKetChuyenDtl.TABLE_NAME)
public class QthtKetChuyenDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "QTHT_KET_CHUYEN_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = QthtKetChuyenDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = QthtKetChuyenDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = QthtKetChuyenDtl.TABLE_NAME + "_SEQ")
    private Long id;

    private Long idHdr;

    private String maDonVi;

    private String cloaiVthh;

    private String loaiVthh;

    private BigDecimal slHienThoi;

    private String tenDonViTinh;

}
