package com.tcdt.qlnvhang.request.khoahoccongnghebaoquan;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;
import org.springframework.util.ObjectUtils;


@Data
public class QuyChuanQuocGiaDtlReq {
    private Long id;
    private String stt;
    private Long idHdr;
    private String tenChiTieu;
    private String maChiTieu;
    private String thuTuHt;
    private boolean chiTieuCha;
    private String maDvi;
    private String mucYeuCauNhap;
    private String mucYeuCauNhapToiDa;
    private String mucYeuCauNhapToiThieu;
    private String mucYeuCauXuat;
    private String mucYeuCauXuatToiDa;
    private String mucYeuCauXuatToiThieu;
    private String phuongPhapXd;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenCloaiVthh;
    private String ghiChu;
    private String nhomCtieu;
    private String toanTu;
    private FileDinhKemReq fileDinhKem;

    public String getStt() {
        return ObjectUtils.isEmpty(stt) ? "" : stt;
    }

    public String getTenChiTieu() {
        return ObjectUtils.isEmpty(tenChiTieu) ? "" : tenChiTieu;
    }

    public String getThuTuHt() {
        return   ObjectUtils.isEmpty(thuTuHt) ? "" : thuTuHt;
    }
    public String getMaDvi() {
        return  ObjectUtils.isEmpty(maDvi) ? "" : maDvi;
    }

    public String getMucYeuCauNhap() {
        return  ObjectUtils.isEmpty(mucYeuCauNhap) ? "" : mucYeuCauNhap;
    }

    public String getMucYeuCauNhapToiDa() {
        return  ObjectUtils.isEmpty(mucYeuCauNhapToiDa) ? "" : mucYeuCauNhapToiDa;
    }

    public String getMucYeuCauNhapToiThieu() {
        return  ObjectUtils.isEmpty(mucYeuCauNhapToiThieu) ? "" : mucYeuCauNhapToiThieu;
    }

    public String getMucYeuCauXuat() {
        return  ObjectUtils.isEmpty(mucYeuCauXuat) ? "" : mucYeuCauXuat;
    }

    public String getMucYeuCauXuatToiDa() {
        return  ObjectUtils.isEmpty(mucYeuCauXuatToiDa) ? "" : mucYeuCauXuatToiDa;
    }

    public String getMucYeuCauXuatToiThieu() {
        return   ObjectUtils.isEmpty(mucYeuCauXuatToiThieu) ? "" : mucYeuCauXuatToiThieu;
    }

    public String getPhuongPhapXd() {
        return  ObjectUtils.isEmpty(phuongPhapXd) ? "" : phuongPhapXd;
    }

    public String getLoaiVthh() {
        return  ObjectUtils.isEmpty(loaiVthh) ? "" : loaiVthh;
    }

    public String getCloaiVthh() {
        return  ObjectUtils.isEmpty(cloaiVthh) ? "" : cloaiVthh;
    }

    public String getTenCloaiVthh() {
        return  ObjectUtils.isEmpty(tenCloaiVthh) ? "" : tenCloaiVthh;
    }

}
