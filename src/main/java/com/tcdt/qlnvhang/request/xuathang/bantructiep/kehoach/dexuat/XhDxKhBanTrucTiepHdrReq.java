package com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.dexuat;

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
public class XhDxKhBanTrucTiepHdrReq extends BaseRequest {

    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private String maDvi;

    private String loaiHinhNx;

    private String kieuNx;

    private String diaChi;

    private Integer namKh;

    private String soDxuat;

    private String trichYeu;

    private String soQdCtieu;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String tchuanCluong;

    private String trangThaiTh;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianDkienTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianDkienDen;
    private Integer tgianTtoan;

    private String tgianTtoanGhiChu;

    private String pthucTtoan;

    private Integer tgianGnhan;

    private String tgianGnhanGhiChu;

    private String pthucGnhan;

    private String thongBaoKh;

    private BigDecimal tongSoLuong;
    private String soQdPd;

    private String ghiChu;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTaoTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTaoDen;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayDuyetTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayDuyetDen;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQdTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQdDen;



    @Transient
    private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();

    @Transient
    private List<XhDxKhBanTrucTiepDtlReq> children = new ArrayList<>();
}
