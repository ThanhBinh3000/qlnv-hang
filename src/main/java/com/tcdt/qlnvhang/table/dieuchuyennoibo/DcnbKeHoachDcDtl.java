package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = DcnbKeHoachDcDtl.TABLE_NAME)
@Getter
@Setter
public class DcnbKeHoachDcDtl implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_KE_HOACH_DC_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbKeHoachDcDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbKeHoachDcDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbKeHoachDcDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long parentId;
    private String maChiCucNhan;
    private String tenChiCucNhan;
    private LocalDate thoiGianDkDc;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private String donViTinh;
    private String tenDonViTinh;
    private BigDecimal tonKho;
    private BigDecimal soLuongDc;
    private BigDecimal duToanKphi;
    private BigDecimal tichLuongKd;
    private BigDecimal soLuongPhanBo;
    private BigDecimal slDcConLai;
    private Boolean coLoKho;
    private String maDiemKho;
    private String tenDiemKho;
    @Access(value=AccessType.PROPERTY)
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maLoKho;
    private String tenLoKho;
    private String maDiemKhoNhan;
    private String tenDiemKhoNhan;
    private String maNhaKhoNhan;
    private String tenNhaKhoNhan;
    private String maNganKhoNhan;
    private String tenNganKhoNhan;
    private String maLoKhoNhan;
    private String tenLoKhoNhan;
    private Boolean coLoKhoNhan;
    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbKeHoachDcHdr dcnbKeHoachDcHdr;
}
