package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class SearchHhHdongBkePmh extends BaseRequest {
    String soHd;

    String tenHd;

    @ApiModelProperty(example = Contains.LOAI_VTHH_GAO)
    String loaiVthh;

    String nhaCcap;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date tuNgayKy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date denNgayKy;

    String maDvi;

    String namHd;

}
