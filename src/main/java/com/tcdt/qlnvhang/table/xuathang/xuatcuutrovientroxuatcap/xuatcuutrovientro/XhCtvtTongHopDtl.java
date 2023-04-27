package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcHdr;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = XhCtvtTongHopDtl.TABLE_NAME)
@Data
public class XhCtvtTongHopDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_CTVT_TONG_HOP_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtTongHopDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhCtvtTongHopDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = XhCtvtTongHopDtl.TABLE_NAME + "_SEQ")
    private Long id;
//    private Long idHdr;
    private Long idDx;
    private String soDx;
    private String maDviDx;
    private LocalDate ngayPduyetDx;
    private String trichYeuDx;
    private BigDecimal tongSoLuongDx;
    private BigDecimal soLuongXuatCap;
    private BigDecimal soLuongDeXuat;
    private BigDecimal thanhTienDx;
    private LocalDate ngayDx;
    private LocalDate ngayKetThucDx;
    private String type;
    @Transient
    private String tenDviDx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idHdr")
    @JsonIgnore
    private XhCtvtTongHopHdr xhCtvtTongHopHdr;

}
