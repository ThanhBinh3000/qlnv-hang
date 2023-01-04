package com.tcdt.qlnvhang.table.nhaphangtheoptt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "HH_PHIEU_NHAP_KHO_CT")
public class HhPhieuNhapKhoCt implements Serializable {
    private static final long serialVersionUID = 3529822360093876437L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_PHIEU_NHAP_KHO_CT_SEQ")
    @SequenceGenerator(sequenceName = "HH_PHIEU_NHAP_KHO_CT_SEQ", allocationSize = 1, name = "HH_PHIEU_NHAP_KHO_CT_SEQ")
    @Column(name = "ID")

    private Long id;

    private Long idHdr;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String donViTinh;

    private BigDecimal soLuongChungTu;

    private BigDecimal soLuongThucNhap;

    private BigDecimal donGia;

    private String  maSo;
}
