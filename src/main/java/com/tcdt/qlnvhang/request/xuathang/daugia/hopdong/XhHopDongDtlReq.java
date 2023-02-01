package com.tcdt.qlnvhang.request.xuathang.daugia.hopdong;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhHopDongDtlReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private String maDvi;

    private List<XhDdiemNhapKhoReq> children = new ArrayList<>();

}
