package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhBcanKeHangHdrReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idQdGiaoNvNh;

    private Long idPhieuNhapKho;

    private Long idDdiemGiaoNvNh;



    private Integer namKh;

    private String maDvi;

    private String maQhns;

    private String soBangKeCanHang;

    private String soQuyetDinhNhap;

    private String soHdong;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKiHdong;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private String soPhieuNhapKho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNkho;

    private String diaDiemKho;

    private String hoTenNguoiGiao;

    private String cmt;

    private String donViGiao;

    private String diaChiNguoiGiao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date thoiGianGiaoNhan;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String donViTinh;

    private String soPhieuKtraCluong;

    @Transient
    List<HhBcanKeHangDtlReq> hhBcanKeHangDtlReqList = new ArrayList<>();
    private ReportTemplateRequest reportTemplateRequest;
}
