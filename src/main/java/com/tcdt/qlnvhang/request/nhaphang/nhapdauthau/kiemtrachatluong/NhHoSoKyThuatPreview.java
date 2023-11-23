package com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.kiemtrachatluong;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoBienBan;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoKyThuatCt;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NhHoSoKyThuatPreview {

    private String soHoSoKyThuat;

    private String tenCloaiVthh;

    private Integer nam;

    private String tenNganLoKho;

    private String tenDiemKho;
    private String tenDvi;

    private String ngayTao;

    private String tenNguoiTao;

    private String ngayPduyet;

    private List<NhHoSoBienBanPreview> listHoSoBienBan = new ArrayList<>();

    private List<NhHoSoKyThuatCtPreview> children = new ArrayList<>();


}
