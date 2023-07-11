package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuXuatKhoHdr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = ScPhieuXuatKhoDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScPhieuXuatKhoDtl extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "SC_PHIEU_XUAT_KHO_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScPhieuXuatKhoDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = ScPhieuXuatKhoDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScPhieuXuatKhoDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private String tenMatHang;
    private String maSo;
    private String donViTinh;
    private BigDecimal slDaDuyet;
    private BigDecimal slThucTe;

}
