package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class HhBienBanDayKhoDtlReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idHdr;

    private String soPhieuKtraCl;

    private String soPhieuNhapKho;

    private String soBangKeCanHang;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNkho;

    private BigDecimal soLuong;

    @Transient
    private List<HhBcanKeHangHdrReq> bcanKeHangHdr;
}
