package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = XhTlHaoDoiDtl.TABLE_NAME)
@Data
public class XhTlHaoDoiDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_HAO_DOI_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlHaoDoiDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlHaoDoiDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlHaoDoiDtl.TABLE_NAME + "_SEQ")
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
    private XhTlHaoDoiHdr haoDoiHdr;
}
