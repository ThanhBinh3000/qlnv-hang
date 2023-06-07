package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = DcnbBienBanLayMauTtDtl.TABLE_NAME)
@Getter
@Setter
public class DcnbBienBanLayMauTtDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BIEN_BAN_LAY_MAU_TT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DCNB_BIEN_BAN_LAY_MAU_TT_DTL_SEQ")
    @SequenceGenerator(sequenceName =  "DCNB_BIEN_BAN_LAY_MAU_TT_DTL_SEQ", allocationSize = 1, name = "DCNB_BIEN_BAN_LAY_MAU_TT_DTL_SEQ")
    private Long id;

    @Column(name = "KH_DC_DTL_ID")
    private Long khDcDtlId;

    @Column(name = "BB_LAY_MAU_ID")
    private Long bBLayMauId;

    @Column(name = "PHIEU_KN_CHAT_LUONG_HDR_ID")
    private Long phieuKnChatLuongHdrId;

    @Column(name = "BANG_KE_CAN_HANG_ID")
    private Long bangKeCanHangId;

    @Column(name = "BIEN_BAN_TINH_KHO_ID")
    private Long bBTinhKhoId;

    @Column(name = "QDINH_DC_HDR_ID")
    private Long qDinhDcHdrId;
}
