package com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.kiemtrachatluong;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoKyThuatCt;
import lombok.Data;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@Data
public class NhHoSoKyThuatCtPreview {

    private String tenHoSo;

    private String loaiTaiLieu;

    private Integer soLuong;

    private String ghiChu;

    private String tgianNhap;

}
