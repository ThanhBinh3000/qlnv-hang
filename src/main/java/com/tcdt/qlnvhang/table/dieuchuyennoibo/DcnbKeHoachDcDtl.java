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
    private String maChiCucNhan;
    private String tenChiCucNhan;
    private LocalDate thoiGianDkDc;
    @Access(value=AccessType.PROPERTY)
    private String maKho;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private String donViTinh;
    private String tenDonViTinh;
    private BigDecimal tonKho;
    private BigDecimal soLuongDc;
    private BigDecimal duToanKphi;
    @Access(value=AccessType.PROPERTY)
    private String maKhoNhan;
    private BigDecimal tichLuongKd;
    private BigDecimal soLuongPhanBo;
    private String maDiemKho;
    private String tenDiemKho;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID")
    @JsonIgnore
    private DcnbKeHoachDcHdr dcnbKeHoachDcHdr;

    public void setMaKho(String maKho) {
        this.maKho = maKho;
        setMaDiemKho(maKho.length() >= 10 ? maKho.substring(0, 10) : "");
        setMaNhaKho(maKho.length() >= 12 ? maKho.substring(0, 12) : "");
        setMaNganKho(maKho.length() >= 14 ? maKho.substring(0, 14) : "");
        setMaLoKho(maKho.length() >= 16 ? maKho.substring(0, 16) : "");
    }

    public void setMaKhoNhan(String maKhoNhan) {
        this.maKhoNhan = maKhoNhan;
        setMaDiemKhoNhan(maKhoNhan.length() >= 10 ? maKhoNhan.substring(0, 10) : "");
        setMaNhaKhoNhan(maKhoNhan.length() >= 12 ? maKhoNhan.substring(0, 12) : "");
        setMaNganKhoNhan(maKhoNhan.length() >= 14 ? maKhoNhan.substring(0, 14) : "");
        setMaLoKhoNhan(maKhoNhan.length() >= 16 ? maKhoNhan.substring(0, 16) : "");
    }
}
