package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class HhNkPhieuKtclReq {
    private Long id;
    private Integer namKhoach;
    private String maQhns;
    private Long idQdGiaoNvNh;
    private String soQdGiaoNvNh;
    private String soPhieu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTao;
    private String loaiVthh;
    private String cloaiVthh;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String trangThai;
    private String ldoTuChoi;
    private String ketLuan;
    private String nguoiGiaoHang;
    private String donViGiaoHang;
    private String cmtNguoiGiaoHang;
    private String diaChi;
    private String bienSoXe;
    private String soChungThuGiamDinh;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayGdinh;
    private String tchucGdinh;
    private BigDecimal soLuongTheoChungTu;
    private BigDecimal soLuongKhKhaiBao;
    private BigDecimal soLuongTtKtra;
    private String kqDanhGia;
    private String soBbNtBq;
    private List<FileDinhKemReq> fileDinhKemCtgd;
    private List<FileDinhKemReq> fileDinhKemKtcl;
    private List<HhNkPhieuKtclCtReq> chiTieu;
    private String soHieuQuyChuan;
}
