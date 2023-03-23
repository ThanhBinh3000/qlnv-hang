package com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbantinhkho;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class XhBbTinhkBttDtlReq {

    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idHdr;

    private String soPhieu;

    private String soPhieuXuat;

    private String soBangKe;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayXuatKho;

    private BigDecimal soLuongThucXuat;
}
