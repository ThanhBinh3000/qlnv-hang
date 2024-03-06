package com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.phieuktracluong;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = XhPhieuKtraCluongBttDtl.TABLE_NAME)
@Data
public class XhPhieuKtraCluongBttDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_PHIEU_KTRA_CLUONG_BTT_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_PHIEU_KTRA_CLUONG_DTL_SEQ")
    @SequenceGenerator(sequenceName = "XH_PHIEU_KTRA_CLUONG_DTL_SEQ", allocationSize = 1, name = "XH_PHIEU_KTRA_CLUONG_DTL_SEQ")
    private Long id;
    private Long idHdr;
    private String maChiTieu;
    private String tenChiTieu;
    private String mucYeuCauXuat;
    private String ketQua;
    private String phuongPhapXd;
    private String danhGia;
    private String mucYeuCauXuatToiDa;
    private String mucYeuCauXuatToiThieu;
    private String toanTu;
}