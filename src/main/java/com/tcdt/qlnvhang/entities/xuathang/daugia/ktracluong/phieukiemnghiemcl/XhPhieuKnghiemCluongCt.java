package com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = XhPhieuKnghiemCluongCt.TABLE_NAME)
@Data
public class XhPhieuKnghiemCluongCt implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_PHIEU_KNGHIEM_CLUONG_CT";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhPhieuKnghiemCluongCt.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhPhieuKnghiemCluongCt.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhPhieuKnghiemCluongCt.TABLE_NAME + "_SEQ")
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
