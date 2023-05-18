package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = DcnbPhieuKtChatLuongDtl.TABLE_NAME)
@Getter
@Setter
public class DcnbPhieuKtChatLuongDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_PHIEU_KT_CHAT_LUONG_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DCNB_PHIEU_KT_CHLUONG_DTL_SEQ")
    @SequenceGenerator(sequenceName ="DCNB_PHIEU_KT_CHLUONG_DTL_SEQ", allocationSize = 1, name = "DCNB_PHIEU_KT_CHLUONG_DTL_SEQ")
    private Long id;

    @Column(name = "HDR_ID")
    private Long hdrId;

    @Column(name = "CHI_TIEU_CL")
    private String chiTieuCl;

    @Column(name = "CHI_SO_CL")
    private String chiSoCl;

    @Column(name = "KET_QUA_PT")
    private String ketQuaPt;

    @Column(name = "PHUONG_PHAP")
    private String phuongPhap;

    @Column(name = "DANH_GIA")
    private Long danhGia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbPhieuKtChatLuongHdr dcnbPhieuKtChatLuongHdr;
}
