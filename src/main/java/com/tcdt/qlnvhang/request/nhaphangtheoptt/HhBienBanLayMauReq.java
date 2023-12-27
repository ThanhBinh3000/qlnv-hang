package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhBienBanLayMauReq {
    private Long id;
    private Integer namKh;
    private String loaiBienBan;
    private String maDvi;
    private String soBienBan;
    private String soBangKe;
    private String soBbLayMau;
    private String maQhns;
    private Long idQdGiaoNvNh;
    private String soQdGiaoNvNh;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayQdNh;
    private String soHd;
    private String soBbNhapDayKho;
    private Long idBbNhapDayKho;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKetThucNhap;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayLayMau;
    private String dviKiemNghiem;
    private String diaDiemLayMau;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private Integer soLuongMau;
    private String ppLayMau;
    private String chiTieuKiemTra;
    private Boolean ketQuaNiemPhong;
    private String trangThai;
    private String truongBpKtbq;
    private String tenDviCcHang;

    private FileDinhKemReq fileDinhKem;

    private List<FileDinhKemReq> fileDinhKems =new ArrayList<>();

    private List<HhBbanLayMauDtlReq> bbanLayMauDtlList = new ArrayList<>();
    private ReportTemplateRequest reportTemplateRequest;
}
