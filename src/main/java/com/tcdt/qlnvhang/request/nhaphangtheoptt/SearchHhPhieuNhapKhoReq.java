package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lowagie.text.pdf.PRIndirectReference;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SearchHhPhieuNhapKhoReq extends BaseRequest {
    Integer namKh;

    private String soQuyetDinhNhap;

    private String soPhieuNhapKho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNhapKhoTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNhapKhoDen;

    private String trangThai;

    private String maDvi;


}
