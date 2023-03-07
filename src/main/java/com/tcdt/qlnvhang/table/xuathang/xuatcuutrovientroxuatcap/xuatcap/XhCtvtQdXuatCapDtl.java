package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtDeXuatHdr;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = XhCtvtQdXuatCapDtl.TABLE_NAME)
@Data
public class XhCtvtQdXuatCapDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_CTVT_QD_XUAT_CAP_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtDeXuatHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhCtvtDeXuatHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = XhCtvtDeXuatHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Long hdrId;
    private String noiDung;
    private BigDecimal soLuongXuatCap;
    private String maDviCuc;
    private BigDecimal tonKhoCuc;
    private BigDecimal soLuongXuatCuc;
    private String maDviChiCuc;
    private BigDecimal tonKhoChiCuc;
    private BigDecimal tonKhoCloaiVthh;
    private String cloaiVthh;
    private BigDecimal soLuongXuatChiCuc;
    private String donViTinh;
    private BigDecimal donGiaKhongVat;
    private BigDecimal thanhTien;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "hdrId", updatable = false, insertable = false)
    private XhCtvtQdXuatCapHdr xhCtvtQdXuatCapHdr;
}
