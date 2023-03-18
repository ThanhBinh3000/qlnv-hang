package com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.hosokythuat;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;

@Data
public class XhHSoKthuatBttDtlReq {

    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idHdr;

    private String tenHoSo;

    private String loaiTaiLieu;

    private BigDecimal soLuong;

    private String ghiChu;

    @Transient
    private FileDinhKemReq fileDinhKems;
}
