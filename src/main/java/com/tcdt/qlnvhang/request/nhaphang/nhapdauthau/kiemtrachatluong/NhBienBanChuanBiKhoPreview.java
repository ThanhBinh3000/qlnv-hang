package com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.kiemtrachatluong;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bienbanchuanbikho.NhBienBanChuanBiKhoCt;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class NhBienBanChuanBiKhoPreview {
    private String soQdGiaoNvNh;
    private String soBienBan;
    private Date ngayNghiemThu;
    private Long idKyThuatVien;
    private String tenKyThuatVien;
    private Long idThuKho;
    private String tenThuKho;
    private String loaiHinhKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maLoKho;
    private String tenLoKho;
    private String pthucBquan;
    private String thucNhap;
    private String hthucBquan;
    private String ketLuan;
    private String maDvi;
    private String tenDvi;
    private BigDecimal tongSo;
    private Integer nam;
    private String loaiVthh;
    private String tenLoaiVthh;
    private String cloaiVthh;
    private String tenCloaiVthh;
    private BigDecimal soLuongDdiemGiaoNvNh;
    private Long idDdiemGiaoNvNh;
    private String ngayTaoFull;
    private String ngayTao;
    private String thangTao;
    private String namTao;
    private BigDecimal tongGiaTri;
    private String tongGiaTriBc;
    private List<NhBienBanChuanBiKhoCt> children = new ArrayList<>();
}
