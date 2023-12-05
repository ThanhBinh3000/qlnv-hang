package com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.kiemtrachatluong;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoBienBanCt;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class NhHoSoBienBanPreview {

    private String tenBb;
    private String soBienBan;
    private String soHd;
    private String ngayHd;
    private String tenDvi;
    private BigDecimal soLuongNhap;
    private String ngayTao;
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private String tgianNhap;
    private String diaDiemKiemTra;
    private String noiDung;
    private String ppLayMau;
    private String ketLuan;

    private List<NhHoSoBienBanCt> listCuc = new ArrayList<>();
    private List<NhHoSoBienBanCt> listChiCuc = new ArrayList<>();



}
