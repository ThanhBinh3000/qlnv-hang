package com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.phieuxuatkho;

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
public class XhPhieuXkhoBttReq extends BaseRequest {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Integer namKh;

    private String maDvi;

    private String maQhns;

    private String soPhieuXuat;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayXuatKho;

    private BigDecimal no;

    private BigDecimal co;

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

    private Long idPhieuKtraCluong;

    private String soPhieu;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private Long idNguoiLapPhieu;

    private Long idKtv;

    private String keToanTruong;

    private String nguoiGiao;

    private String cmtNguoiGiao;

    private String donViNguoiGiao;

    private String ctyNguoiGiao;

    private String diaChiNguoiGiao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianGiaoNhan;

    private String soBangKe;

    private String  maSo;

    private String dviTinh;

    private BigDecimal soLuongChungTu;

    private BigDecimal soLuongThucNhap;

    private BigDecimal donGia;

    private String ghiChu;

    @Transient
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
}
