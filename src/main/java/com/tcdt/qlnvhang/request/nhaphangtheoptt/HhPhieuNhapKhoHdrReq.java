package com.tcdt.qlnvhang.request.nhaphangtheoptt;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhPhieuNhapKhoHdrReq  {

    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Integer namKh;

    private Long idQdGiaoNvNh;

    private Long idDdiemGiaoNvNh;

    private String maDvi;

    private String maQhns;

    private String soPhieuNhapKho;
    private String soBangKe;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNkho;

    private BigDecimal no;

    private BigDecimal co;

    private String soQuyetDinhNhap;

    private String soHdong;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKiHdong;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private String soPhieuKtraCluong;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String canBoLapPhieu;

    private String lanhDaoChiCuc;

    private String ktvBaoQuan;

    private String keToanTruong;

    private String hoTenNguoiGiao;

    private String cmt;

    private String donViGiao;

    private String diaChiNguoiGiao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date thoiGianGiaoNhan;

    private String soBangKeCanHang;

    private String ghiChu;

    private String lyDoTuChoi;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayGdinh;

    @Transient
    private List<HhPhieuNhapKhoCtReq> PhieuNhapKhoCtList = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    private ReportTemplateRequest reportTemplateRequest;
}
