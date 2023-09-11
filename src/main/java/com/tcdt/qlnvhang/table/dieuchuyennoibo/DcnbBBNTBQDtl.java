package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = DcnbBBNTBQDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    @NotNull
    private String danhMuc;
    private String nhomHang;
    @NotNull
    private String donViTinh;
    private String matHang;
    private String tenMatHang;
    @NotNull
    private Double tongGiaTri;
    @NotNull
    private Double soLuongTrongNam;
    @NotNull
    private Double donGia;
    @NotNull
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
