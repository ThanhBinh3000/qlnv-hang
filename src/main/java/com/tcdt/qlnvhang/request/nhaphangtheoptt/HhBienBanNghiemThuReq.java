package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhBbanNghiemThuDtl;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhBienBanNghiemThuReq {
    private Long id;
    private String soBb;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNghiemThu;
    private String thuTruong;
    private String keToan;
    private String kyThuatVien;
    private String thuKho;
    private String lhKho;
    private Double slCanNhap;
    private Long idPhieuNhapKho;
    private String soPhieuNhapKho;
    private Double slThucNhap;
    private Double tichLuong;
    private String pthucBquan;
    private String hthucBquan;
    private Double dinhMuc;
    private String ketLuan;
    private String trangThai;
    private String ldoTuChoi;
    private String capDvi;
    private String maDvi;
    private Integer namKh;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private Long hopDongId;
    private String maQhns;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;

    private Long idQdGiaoNvNh;

    private String tenThuKho;
    private String tenKeToan;
    private String tenNguoiPduyet;
    private String soQdGiaoNvNh;

    private Long idDdiemGiaoNvNh;

    private BigDecimal kinhPhiThucTe;

    private BigDecimal kinhPhiTcPd;

    private FileDinhKemReq fileDinhKem;

    private List<HhBbanNghiemThuDtlReq> dviChuDongThucHien =new ArrayList<>();

    private List<HhBbanNghiemThuDtlReq> dmTongCucPdTruocThucHien =new ArrayList<>();

    private List<FileDinhKemReq> fileDinhkems =new ArrayList<>();
    private ReportTemplateRequest reportTemplateRequest;
}
