package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoHdr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = ScPhieuNhapKhoDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScPhieuNhapKhoDtl extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "SC_PHIEU_NHAP_KHO_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScPhieuNhapKhoDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = ScPhieuNhapKhoDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = ScPhieuNhapKhoDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private String tenMatHang;
    private String maSo;
    private String donViTinh;
    private BigDecimal slDaDuyet;
    private BigDecimal slThucTe;
    private BigDecimal donGiaPd;
    private BigDecimal kinhPhiThucTe;

}
