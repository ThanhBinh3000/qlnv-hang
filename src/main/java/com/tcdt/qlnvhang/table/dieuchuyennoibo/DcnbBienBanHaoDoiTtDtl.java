package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = DcnbBienBanHaoDoiTtDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBienBanHaoDoiTtDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BIEN_BAN_HAO_DOI_TT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DCNB_BB_HAO_DOI_TT_DTL_SEQ")
    @SequenceGenerator(sequenceName = "DCNB_BB_HAO_DOI_TT_DTL_SEQ", allocationSize = 1, name = "DCNB_BB_HAO_DOI_TT_DTL_SEQ")
    private Long id;

    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;
    @NotNull
    @Column(name = "PHIEU_KT_CHAT_LUONG_HDR_ID")
    private Long phieuKtChatLuongHdrId;
    @NotNull
    @Column(name = "PHIEU_XUAT_KHO_HDR_ID")
    private Long phieuXuatKhoHdrId;

    @Column(name = "BANG_KE_CAN_HANG_HDR_ID")
    private Long bangKeCanHangHdrId;
    @NotNull
    @Column(name = "NGAY_XUAT_KHO")
    private LocalDate ngayXuatKho;
    @NotNull
    @Column(name = "SO_LUONG_XUAT")
    private Double soLuongXuat;
    @NotNull
    @Column(name = "SO_PHIEU_KT_CHAT_LUONG")
    private String soPhieuKtChatLuong;
    @NotNull
    @Column(name = "SO_PHIEU_XUAT_KHO")
    private String soPhieuXuatKho;
    @NotNull
    @Column(name = "SO_BANG_KE_CAN_HANG")
    private String soBangKeCanHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbBienBanHaoDoiHdr dcnbBienBanHaoDoiHdr;

}
