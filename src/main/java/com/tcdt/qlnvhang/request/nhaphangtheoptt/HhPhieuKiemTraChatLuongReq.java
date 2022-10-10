package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhPhieuKiemTraChatLuongReq {
    private Long id;
    private Integer namKh;
    private String maDvi;
    private String soPhieu;
    private String maQhns;
    private String idQdGiaoNvNh;
    private String soQdGiaoNvNh;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayQdGiaoNvNh;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String soHd;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String nguoiGiaoHang;
    private String cmtNguoiGiaoHang;
    private String donViGiaoHang;
    private String diaChi;
    private String bienSoXe;
    private Double soLuongDeNghiKt;
    private Double soLuongNhapKho;
    private String soChungThuGiamDinh;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayGdinh;
    private String tchucGdinh;
    private String ketLuan;
    private String kqDanhGia;
    private String trangThai;

    private List<FileDinhKemReq> fileDinhkems =new ArrayList<>();
    private List<HhPhieuKiemTraChatLuongDtlReq> phieuKiemTraChatLuongDtlList=new ArrayList<>();

}
