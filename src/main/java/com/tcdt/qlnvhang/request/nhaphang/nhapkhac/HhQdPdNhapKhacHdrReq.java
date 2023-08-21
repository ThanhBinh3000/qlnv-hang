package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class HhQdPdNhapKhacHdrReq {
    private Long id;
    private Long IdDx;
    private Long IdTh;
    private String maTh;
    private Integer namKhoach;
    private Integer tongThanhTien;
    private String maDvi;
    private String dvt;
    private String loaiHinhNx;
    private String kieuNx;
    private String soDxuat;
    private String trichYeu;
    private String phanLoai;
    private String soQd;
    private String loaiVthh;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayDxuat;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHieuLuc;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;
    private String trangThai;
    private Boolean lastest;
    private List<FileDinhKemReq> fileDinhKems;
    private List<HhDxuatKhNhapKhacHdrReq> details;
}
