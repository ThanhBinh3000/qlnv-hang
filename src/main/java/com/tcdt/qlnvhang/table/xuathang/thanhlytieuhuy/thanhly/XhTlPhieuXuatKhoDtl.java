package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = XhTlPhieuXuatKhoDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XhTlPhieuXuatKhoDtl extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_PHIEU_XUAT_KHO_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlPhieuXuatKhoDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlPhieuXuatKhoDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlPhieuXuatKhoDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private String tenMatHang;
    private String maSo;
    private String donViTinh;
    private BigDecimal slXuatThucTe;
    private BigDecimal kinhPhiThucTe;

}
