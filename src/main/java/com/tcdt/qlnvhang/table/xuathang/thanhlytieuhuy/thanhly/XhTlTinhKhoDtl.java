package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = XhTlTinhKhoDtl.TABLE_NAME)
@Data
public class XhTlTinhKhoDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_TINH_KHO_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlTinhKhoDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlTinhKhoDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlTinhKhoDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idPhieuKnCl;
    private String soPhieuKnCl;
    private Long idPhieuXuatKho;
    private String soPhieuXuatKho;
    private Long idBangKe;
    private String soBangKe;
    private LocalDate ngayXuatKho;
    private BigDecimal slXuat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idHdr")
    @JsonIgnore
    private XhTlTinhKhoHdr tinhKhoHdr;
}