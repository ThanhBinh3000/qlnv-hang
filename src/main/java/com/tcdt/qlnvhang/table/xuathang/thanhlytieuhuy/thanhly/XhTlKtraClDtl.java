package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = XhTlKtraClDtl.TABLE_NAME)
public class XhTlKtraClDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_KTRA_CL_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlKtraClDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlKtraClDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlKtraClDtl.TABLE_NAME + "_SEQ")
    @Column(name = "ID")
    private Long id;

    private Long idHdr;

    private Integer thuTuHt;

    private String tenChiTieu;

    private String mucYeuCauNhap;

    private String mucYeuCauNhapToiDa;

    private String mucYeuCauNhapToiThieu;

    private String ketQua;

    private String phuongPhap;

    private String danhGia;

}
