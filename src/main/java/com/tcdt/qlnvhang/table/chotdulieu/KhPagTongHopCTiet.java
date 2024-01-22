package com.tcdt.qlnvhang.table.chotdulieu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = KhPagTongHopCTiet.TABLE_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KhPagTongHopCTiet {
    public static final String TABLE_NAME = "KH_PAG_TONG_HOP_CTIET";
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KH_LT_PAG_TONG_HOP_CTIET_SEQ")
    @SequenceGenerator(sequenceName = "KH_LT_PAG_TONG_HOP_CTIET_SEQ", allocationSize = 1, name = "KH_LT_PAG_TONG_HOP_CTIET_SEQ")
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "SO_DX")
    private String soDx;

    @Column(name = "SO_QD_BTC")
    private String soQdBtc;

    @Column(name = "SO_QD_TCDT")
    private String soQdTcdt;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "MA_CHI_CUC")
    private String maChiCuc;

    @Transient
    private String tenDvi;

    @Column(name = "SO_LUONG")
    private BigDecimal soLuong;

    @Column(name = "GIA_KS_TU")
    private BigDecimal giaKsTu;

    @Column(name = "GIA_KS_DEN")
    private BigDecimal giaKsDen;

    @Column(name = "GIA_KS_VAT_TU")
    private BigDecimal giaKsVatTu;

    @Column(name = "GIA_KS_VAT_DEN")
    private BigDecimal giaKsVatDen;

    @Column(name = "GIA_DN")
    private BigDecimal giaDn;

    @Column(name = "GIA_DN_VAT")
    private BigDecimal giaDnVat;

    @Column(name = "GIA_CUC_DN")
    private BigDecimal giaCucDn;

    @Column(name = "GIA_CUC_DN_VAT")
    private BigDecimal giaCucDnVat;

    @Column(name = "GIA_QD_BTC")
    private BigDecimal giaQdBtc;

    @Column(name = "GIA_QD_BTC_VAT")
    private BigDecimal giaQdBtcVat;

    @Column(name = "GIA_QD_DC_BTC")
    private BigDecimal giaQdDcBtc;

    @Column(name = "GIA_QD_DC_BTC_VAT")
    private BigDecimal giaQdDcBtcVat;

    @Column(name = "QD_BTC_ID")
    private Long qdBtcId;

    @Column(name = "QD_TCDTNN_ID")
    private Long qdTcdtnnId;

    @Column(name = "PAG_TH_ID")
    private Long pagThId;

    @Column(name = "PAG_ID")
    private Long pagId;

    @Column(name = "TRANG_THAI")
    private String trangThai;

    @Column(name = "GIA_QD_TCDTNN")
    private BigDecimal giaQdTcdt;

    @Column(name = "GIA_QD_VAT_TCDTNN")
        private BigDecimal giaQdTcdtVat;

    @Column(name = "GIA_QD_DC_TCDTNN")
    private BigDecimal giaQdDcTcdt;

    @Column(name = "GIA_QD_DC_VAT_TCDTNN")
    private BigDecimal giaQdDcTcdtVat;

    @Column(name = "AP_DUNG_TAT_CA")
    private Boolean apDungTatCa;

    @Column(name = "VAT")
    private BigDecimal vat;

    @Column(name = "GIA_QD_BTC_CU")
    private BigDecimal giaQdBtcCu;

    @Column(name = "GIA_QD_BTC_CU_VAT")
    private BigDecimal giaQdBtcCuVat;

    @Column(name = "GIA_QD_TCDT_CU")
    private BigDecimal giaQdTcdtCu;

    @Column(name = "GIA_QD_TCDT_CU_VAT")
    private BigDecimal giaQdTcdtCuVat;
    @Transient
    private String tenVungMien;
    @Transient
    private String tenChiCuc;
    @Transient
     LocalDate ngayKyTcdt;

    public BigDecimal getVat() {
        return ObjectUtils.isEmpty(vat) ? BigDecimal.valueOf(0) : vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = ObjectUtils.isEmpty(vat) ? BigDecimal.valueOf(0) : vat;
    }

    public BigDecimal getSoLuong() {
        return  ObjectUtils.isEmpty(soLuong) ? BigDecimal.valueOf(0) : soLuong;
    }

    public void setSoLuong(BigDecimal soLuong) {
        this.soLuong = ObjectUtils.isEmpty(soLuong) ? BigDecimal.valueOf(0) : soLuong;
    }

    public BigDecimal getGiaKsTu() {
        return ObjectUtils.isEmpty(giaKsTu) ? BigDecimal.valueOf(0) : giaKsTu;
    }

    public BigDecimal getGiaKsDen() {
        return ObjectUtils.isEmpty(giaKsDen) ? BigDecimal.valueOf(0) : giaKsDen;
    }

    public BigDecimal getGiaKsVatTu() {
        return ObjectUtils.isEmpty(giaKsVatTu) ? BigDecimal.valueOf(0) : giaKsVatTu;
    }

    public BigDecimal getGiaKsVatDen() {
        return ObjectUtils.isEmpty(giaKsVatDen) ? BigDecimal.valueOf(0) : giaKsVatDen;
    }

    public BigDecimal getGiaDn() {
        return ObjectUtils.isEmpty(giaDn) ? BigDecimal.valueOf(0) : giaDn;
    }

    public BigDecimal getGiaDnVat() {
        return ObjectUtils.isEmpty(giaDnVat) ? BigDecimal.valueOf(0) : giaDnVat;
    }

    public BigDecimal getGiaCucDn() {
        return ObjectUtils.isEmpty(giaCucDn) ? BigDecimal.valueOf(0) : giaCucDn;
    }

    public BigDecimal getGiaCucDnVat() {
        return ObjectUtils.isEmpty(giaCucDnVat) ? BigDecimal.valueOf(0) : giaCucDnVat;
    }

    public BigDecimal getGiaQdBtc() {
        return ObjectUtils.isEmpty(giaQdBtc) ? BigDecimal.valueOf(0) : giaQdBtc;
    }

    public BigDecimal getGiaQdBtcVat() {
        return ObjectUtils.isEmpty(giaQdBtcVat) ? BigDecimal.valueOf(0) : giaQdBtcVat;
    }

    public BigDecimal getGiaQdDcBtc() {
        return ObjectUtils.isEmpty(giaQdDcBtc) ? BigDecimal.valueOf(0) : giaQdDcBtc;
    }

    public BigDecimal getGiaQdDcBtcVat() {
        return ObjectUtils.isEmpty(giaQdDcBtcVat) ? BigDecimal.valueOf(0) : giaQdDcBtcVat;
    }

    public BigDecimal getGiaQdTcdt() {
        return ObjectUtils.isEmpty(giaQdTcdt) ? BigDecimal.valueOf(0) : giaQdTcdt;
    }

    public BigDecimal getGiaQdTcdtVat() {
        return ObjectUtils.isEmpty(giaQdTcdtVat) ? BigDecimal.valueOf(0) : giaQdTcdtVat;
    }

    public BigDecimal getGiaQdDcTcdt() {
        return ObjectUtils.isEmpty(giaQdDcTcdt) ? BigDecimal.valueOf(0) : giaQdDcTcdt;
    }

    public BigDecimal getGiaQdDcTcdtVat() {
        return ObjectUtils.isEmpty(giaQdDcTcdtVat) ? BigDecimal.valueOf(0) : giaQdDcTcdtVat;
    }


}
