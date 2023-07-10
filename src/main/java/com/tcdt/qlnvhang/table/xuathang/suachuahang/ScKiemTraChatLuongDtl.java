package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = ScKiemTraChatLuongDtl.TABLE_NAME)
public class ScKiemTraChatLuongDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "SC_KIEM_TRA_CL_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScKiemTraChatLuongDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = ScKiemTraChatLuongDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScKiemTraChatLuongDtl.TABLE_NAME + "_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "HDR_ID",insertable = true, updatable = true)
    private Long hdrId;

    @Column(name = "CHI_TIEU_CL")
    private String chiTieuCl;

    @Column(name = "CHI_SO_CL")
    private String chiSoCl;

    @Column(name = "KET_QUA_PHAN_TICH")
    private String ketQuaPhanTich;

    @Column(name = "PHUONG_PHAP")
    private String phuongPhap;

    @Column(name = "DANH_GIA")
    private String danhGia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private ScKiemTraChatLuongHdr scKiemTraChatLuongHdr;
}
