package com.tcdt.qlnvhang.request.search.banhang;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class BhHopDongSearchReq extends BaseRequest {
    String soHd;

    String tenHd;

    @NotNull(message = "Không được để trống")
    @ApiModelProperty(example = Contains.LOAI_VTHH_GAO)
    String loaiVthh;

    String nhaCcap;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date tuNgayKy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    Date denNgayKy;

    String maDvi;
}
