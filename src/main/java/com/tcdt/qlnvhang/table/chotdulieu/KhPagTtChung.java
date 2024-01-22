package com.tcdt.qlnvhang.table.chotdulieu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = KhPagTtChung.TABLE_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KhPagTtChung implements Serializable {

    private static final long serialVersionUID = -9158383107212840699L;
    public static final String TABLE_NAME = "KH_PAG_TT_CHUNG";

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KH_PAG_TT_CHUNG_SEQ")
    @SequenceGenerator(sequenceName = "KH_PAG_TT_CHUNG_SEQ", allocationSize = 1, name = "KH_PAG_TT_CHUNG_SEQ")
    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "MA_CHI_CUC")
    private String maChiCuc;

    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    @Column(name = "CLOAI_VTHH")
    private String cloaiVthh;

    @Column(name = "TIEU_CHUAN_CL")
    private String tchuanCluong;

    @Column(name = "SO_LUONG")
    private BigDecimal soLuong;

    @Column(name = "SO_LUONG_CTIEU")
    private BigDecimal soLuongCtieu;

    @Column(name = "DON_VI_TINH")
    private String donViTinh;

    @Column(name = "GIA_DN")
    private BigDecimal giaDn;

    @Column(name = "GIA_DN_VAT")
    private BigDecimal giaDnVat;

    @Column(name = "GIA_QD_BTC")
    private BigDecimal giaQdBtc;

    @Column(name = "GIA_QD_BTC_VAT")
    private BigDecimal giaQdBtcVat;


    @Column(name = "GIA_QD_DC_BTC")
    private BigDecimal giaQdDcBtc;

    @Column(name = "GIA_QD_DC_BTC_VAT")
    private BigDecimal giaQdDcBtcVat;

    @Column(name = "GIA_QD_TCDT")
    private BigDecimal giaQdTcdt;

    @Column(name = "GIA_QD_TCDT_VAT")
    private BigDecimal giaQdTcdtVat;

    @Column(name = "GIA_QD_DC_TCDT")
    private BigDecimal giaQdDcTcdt;

    @Column(name = "GIA_QD_DC_TCDT_VAT")
    private BigDecimal giaQdDcTcdtVat;

    @Column(name = "VAT")
    private BigDecimal vat;
    /**
     * {@link KhPhuongAnGia}
     */
    @Column(name = "PAG_ID")
    private Long phuongAnGiaId;

    @Column(name = "QD_BTC_ID")
    private Long qdBtcId;

    @Column(name = "QD_TCDTNN_ID")
    private Long qdTcdtnnId;


/////

    @Column(name = "GIA_QD_BTC_CU")
    private BigDecimal giaQdBtcCu;

    @Column(name = "GIA_QD_BTC_CU_VAT")
    private BigDecimal giaQdBtcCuVat;

    @Column(name = "GIA_QD_TCDT_CU")
    private BigDecimal giaQdTcdtCu;

    @Column(name = "GIA_QD_TCDT_CU_VAT")
    private BigDecimal giaQdTcdtCuVat;
    @Column(name = "SO_QD_BTC")
    private String soQdBtc;


    @Column(name = "SO_QD_TCDT")
    private String soQdTcdt;

    @Transient
    private String tenLoaiVthh;

    @Transient
    private String tenCloaiVthh;
    @Transient
    String tenChiCuc;

    @Transient
    String tenDiemKho;

    @Transient
     LocalDate ngayKyTcdt;

    public BigDecimal getSoLuong() {
        return ObjectUtils.isEmpty(soLuong) ? BigDecimal.ZERO : soLuong;
    }
}
