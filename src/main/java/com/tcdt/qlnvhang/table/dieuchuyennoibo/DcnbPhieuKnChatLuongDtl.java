package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = DcnbPhieuKnChatLuongDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbPhieuKnChatLuongDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_PHIEU_KN_CHAT_LUONG_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DCNB_PHIEU_KN_CLUONG_DTL_SEQ")
    @SequenceGenerator(sequenceName ="DCNB_PHIEU_KN_CLUONG_DTL_SEQ", allocationSize = 1, name = "DCNB_PHIEU_KN_CLUONG_DTL_SEQ")
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
    private DcnbPhieuKnChatLuongHdr dcnbPhieuKnChatLuongHdr;
}
