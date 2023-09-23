package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = DcnbBbChuanBiKhoDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBbChuanBiKhoDtl implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BB_CHUAN_BI_KHO_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBbChuanBiKhoDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBbChuanBiKhoDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbBbChuanBiKhoDtl.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;
    private String nhomHang;
    private String matHang;
    private String noiDung;
    private String dviTinh;
    private BigDecimal soLuongTrongNam;
    private BigDecimal donGiaTrongNam;
    private BigDecimal thanhTienTrongNam;
    private Double soLuongNamTruoc;
    private BigDecimal thanhTienNamTruoc;
    private BigDecimal tongGiaTri;
    private String type;
    private String danhMuc;
    private String donViTinh;
    private String tenMatHang;
    private Double donGia;
    private Boolean isParent;
    private String idParent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbBbChuanBiKhoHdr parent;
}
