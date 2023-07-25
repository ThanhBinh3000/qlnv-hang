package com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = XhXkVtBhPhieuKdclDtl.TABLE_NAME)
@Data
public class XhXkVtBhPhieuKdclDtl implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_XK_VT_BH_PHIEU_KDCL_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXkVtBhPhieuKdclDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhXkVtBhPhieuKdclDtl.TABLE_NAME
        + "_SEQ", allocationSize = 1, name = XhXkVtBhPhieuKdclDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private String tenChiTieu;
    private String maChiTieu;
    private String chiSo;
    private String ketQua;
    private String ppKiemTra;
    private Integer danhGia; // Đạt/ Không đạt

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idHdr")
    @JsonIgnore
    private XhXkVtBhPhieuKdclHdr phieuKdclHdr;
}
