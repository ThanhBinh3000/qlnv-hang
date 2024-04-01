package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhBienBanDayKhoHdrReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idQdGiaoNvNh;

    private Long idPhieuNhapKho;

    private Long idBangCanKeHang;

    private Long idDdiemGiaoNvNh;

    private Integer namKh;
    private Integer thanLuuKho;

    private String maDvi;

    private String maQhns;

    private String soBbNhapDayKho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayLapBban;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHetHanLk;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTao;

    private String soQuyetDinhNhap;

    private String soHdong;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKiHdong;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;
    private String soBangKe;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayBdauNhap;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKthucNhap;

    private BigDecimal tongSoLuongNhap;

    private BigDecimal donGia;

    private BigDecimal thanhTien;

    private String ghiChu;

    private String ktvBanQuan;

    private String keToanTruong;

    private String soPhieuNhapKho;

    private String soBangKeCanHang;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNkho;



    @Transient
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    @Transient
    private List<HhBienBanDayKhoDtlReq> hhBienBanDayKhoDtlReqList = new ArrayList<>();
    private ReportTemplateRequest reportTemplateRequest;
}
