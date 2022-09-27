package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class HhDxKhMttThopDtlReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;
    private Long idThopHdr;
    private Long idDxHdr;
    private String maDvi;
    @Temporal(TemporalType.DATE)
    private Date ngayDxuat;
    private String tenDuAn;
    private BigDecimal soLuong;
    private BigDecimal donGia;
    private BigDecimal tongTien;
}
