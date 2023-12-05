package com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.kiemtrachatluong;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau.BienBanLayMau;
import lombok.Data;

import java.util.List;

@Data
public class BienBanLayMauPreview {
    private BienBanLayMau hdr;
    private String ngayHd;
    private String ngayTao;
    private String ppLayMau;
    private String tenDviUpper;
    private List<String> chiTieuKiemTra;
}
