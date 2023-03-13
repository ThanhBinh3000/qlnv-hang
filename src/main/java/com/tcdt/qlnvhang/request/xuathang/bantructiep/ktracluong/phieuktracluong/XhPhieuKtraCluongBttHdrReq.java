package com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.phieuktracluong;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class XhPhieuKtraCluongBttHdrReq extends BaseRequest {

    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Integer namKh;

    private String maDvi;

    private String maQhns;

    private String soBienBan;

    private Long idQd;

    private String soQd;

    private String soPhieu;

    private Long idNgKnghiem;

    private Long idTruongPhong;

    private Long idKtv;

    private Long idDdiemXh;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String hthucBquan;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayLayMau;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKnghiem;

    private String ketQua;

    private String ketLuan;

    @Transient
    private FileDinhKemReq fileDinhKem;

    @Transient
    private List<XhPhieuKtraCluongBttDtlReq> children = new ArrayList<>();
}
