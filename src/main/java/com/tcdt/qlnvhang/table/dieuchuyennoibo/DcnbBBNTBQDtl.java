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
    private String danhMuc;
    private String nhomHang;
    private String donViTinh;
    private String matHang;
    private String donViTinhMh;
    private Double tongGiaTri;
    private Double soLuongTrongNam;
    private Double donGia;
    private Double thanhTienTrongNam;
    private Double soLuongNamTruoc;
    private Double thanhTienNamTruoc;
    private String type;
    private Boolean isParent;
    private String idParent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbBBNTBQHdr dcnbBBNTBQHdr;
}
