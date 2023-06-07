package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = DcnbBBNTBQDtl.TABLE_NAME)
@Getter
@Setter
public class DcnbBBNTBQDtl implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BB_NT_BQ_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBBNTBQDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBBNTBQDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbBBNTBQDtl.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "HDR_ID")
    private Long hdrId;
    @Column(name = "DANH_MUC_NHOM_HANG")
    private String dmNhomHang;
    @Column(name = "MAT_HANG")
    private String matHang;
    @Column(name = "DON_VI_TINH")
    private String donViTinh;
    @Column(name = "SO_LUONG_TRONG_NAM")
    private Double slTrongNam;
    @Column(name = "DON_GIA")
    private Double donGia;
    @Column(name = "THANH_TIEN_TRONG_NAM")
    private Double thanhTienTrongNam;
    @Column(name = "SO_LUONG_NAM_TRUOC")
    private Double slNamTruoc;
    @Column(name = "THANH_TIEN_NAM_TRUOC")
    private Double thanhTienNamTruoc;
    @Column(name = "TONG_GIA_TRI")
    private Double tongGiaTri;
    @Column(name = "TYPE")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbBBNTBQHdr dcnbBBNTBQHdr;
}
