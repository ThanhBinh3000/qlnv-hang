package com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.hosokythuat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhHSoKthuatBttHdrReq extends BaseRequest {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long namKh;

    private String maDvi;

    private String soHoSoKyThuat;

    private String soBienBanKnhap;

    private Long idQd;

    private String soQd;

    private String soHd;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private String soBienBanKxuat;

    private Boolean kqKiemTra;

    private String noiDungKdatYeuCau;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTaoTu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTaoDen;


    @Transient
    private List<XhHSoKthuatBttDtlReq> children = new ArrayList<>();
}
