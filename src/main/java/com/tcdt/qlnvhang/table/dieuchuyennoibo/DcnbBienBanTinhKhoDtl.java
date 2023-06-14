package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = DcnbBienBanTinhKhoDtl.TABLE_NAME)
@Getter
@Setter
public class DcnbBienBanTinhKhoDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BIEN_BAN_TINH_KHO_TT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBienBanLayMauDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBienBanLayMauDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbBienBanLayMauDtl.TABLE_NAME + "_SEQ")
    private Long id;

    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;

    @Column(name = "PHIEU_KT_CHAT_LUONG_HDR_ID")
    private Long phieuKtChatLuongHdrId;

    @Column(name = "PHIEU_XUAT_KHO_HDR_ID")
    private Long phieuXuatKhoHdrId;

    @Column(name = "BANG_KE_CAN_HANG_HDR_ID")
    private Long bangKeCanHangHdrId;

    @Column(name = "NGAY_XUAT_KHO")
    private LocalDate ngayXuatKho;

    @Column(name = "SO_LUONG_XUAT")
    private Long soLuongXuat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbBienBanTinhKhoHdr dcnbBienBanTinhKhoHdr;

}
