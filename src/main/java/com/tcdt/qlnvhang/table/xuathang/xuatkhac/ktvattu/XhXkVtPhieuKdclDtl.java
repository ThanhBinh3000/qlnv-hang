package com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = XhXkVtPhieuKdclDtl.TABLE_NAME)
@Data
public class XhXkVtPhieuKdclDtl implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_XK_VT_PHIEU_KDCL_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXkVtPhieuKdclDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhXkVtPhieuKdclDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = XhXkVtPhieuKdclDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private String tenChiTieu;
    private String maChiTieu;
    private String chiSo;
    private String ketQua;
    private String ppKiemTra;
    private String danhGia; // Đạt/ Không đạt

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idHdr")
    @JsonIgnore
    private XhXkVtPhieuKdclHdr xhXkVtPhieuKdclHdr;
}
