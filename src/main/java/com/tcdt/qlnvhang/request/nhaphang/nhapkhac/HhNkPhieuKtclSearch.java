package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.util.Date;
@Data
public class HhNkPhieuKtclSearch extends BaseRequest {
    private Long id;
    private Integer namKhoach;
    private String soQd;
    private String soPhieu;
    private String maDvi;
    private String maDviChiCuc;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    private Date tuNgayLP;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    private Date denNgayLP;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    private Date tuNgayGD;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
    private Date denNgayGD;
    private String tuNgayLPStr;
    private String denNgayLPStr;
    private String tuNgayGDStr;
    private String denNgayGDStr;
    private String kqDanhGia;
    private String loaiVthh;
    private String maNganKho;
    private String maLoKho;
    private Long idQdGiaoNvnh;
}
