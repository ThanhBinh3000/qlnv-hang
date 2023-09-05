package com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat;

import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhDxKhBanTrucTiepDtl.TABLE_NAME)
@Data
public class XhDxKhBanTrucTiepDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_DX_KH_BAN_TRUC_TIEP_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhDxKhBanTrucTiepDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhDxKhBanTrucTiepDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhDxKhBanTrucTiepDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private BigDecimal soLuongChiCuc;
    private String maDvi;
    private String diaChi;
    private BigDecimal soLuongChiTieu;
    private BigDecimal soLuongKhDaDuyet;
    private String donViTinh;
    @Transient
    private String tenDvi;
    @Transient
    private BigDecimal donGiaDeXuat;
    @Transient
    private BigDecimal thanhTienDeXuat;
    @Transient
    private List<XhDxKhBanTrucTiepDdiem> children = new ArrayList<>();

    public void setChildren(List<XhDxKhBanTrucTiepDdiem> children) {
        this.children = children;
        if (!DataUtils.isNullOrEmpty(children)) {
            this.donGiaDeXuat = children.get(0).getDonGiaDeXuat();
            this.thanhTienDeXuat = children.stream().map(XhDxKhBanTrucTiepDdiem::getThanhTienDeXuat).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
}
