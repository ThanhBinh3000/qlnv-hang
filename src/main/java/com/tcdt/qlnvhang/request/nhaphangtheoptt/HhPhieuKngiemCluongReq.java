package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhPhieuKngiemCluongReq {
    private Long id;
    private String soPhieu;
    private Integer namKh;
    private Long idBienBan;
    private String soBienBan;
    private Long idQdNh;
    private String soQdNh;
    private String maDvi;
    private String maQhns;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String soLuongBq;
    private String hthucBquan;
    private String thuKho;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNhapDay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayLayMau;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKnMau;
    private String ketQuaDanhGia;
    private String trangThai;

    List<HhPhieuKnCluongDtlReq> phieuKnCluongDtlReqList = new ArrayList<>();
}
