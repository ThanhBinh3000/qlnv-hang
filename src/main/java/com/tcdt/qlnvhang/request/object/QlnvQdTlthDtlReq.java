package com.tcdt.qlnvhang.request.object;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Data
public class QlnvQdTlthDtlReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    Long idHdr;
    
    @Size(max = 50, message = "Số đề xuất không được vượt quá 50 ký tự")
    @ApiModelProperty(example = "XXXSDX123")
    String soDxuat;

    @NotNull(message = "Không được để trống")
    @Size(max = 20, message = "Mã đơn vị không được vượt quá 20 ký tự")
    String maDvi;

    BigDecimal soLuongDxuat;

    BigDecimal soLuongDuyet;

    private List<QlnvQdTlthDtlCtietReq> detailListReq;
}
