package com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bienbantinhkho;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhBbTinhkBttHdrReq extends BaseRequest {

    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Integer namKh;

    private String maDvi;

    private String maQhns;

    private String soBbTinhKho;

    private Long idQd;

    private String soQd;

    private Long idHd;

    private String soHd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyHd;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayBdauXuat;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKthucXuat;

    private BigDecimal tongSlNhap;

    private BigDecimal tongSlXuat;

    private BigDecimal slConLai;

    private BigDecimal slThucTe;

    private BigDecimal slChenhLech;

    private String nguyenNhan;

    private String kienNghi;

    private String ghiChu;

    private Long idThuKho;

    private Long idKtv;

    private Long idKeToan;

    @Transient
    private List<XhBbTinhkBttDtlReq> children = new ArrayList<>();

    @Transient
    private FileDinhKemReq fileDinhKem;
}
