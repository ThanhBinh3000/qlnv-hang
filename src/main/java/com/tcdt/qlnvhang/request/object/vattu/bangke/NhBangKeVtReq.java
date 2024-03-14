package com.tcdt.qlnvhang.request.object.vattu.bangke;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class NhBangKeVtReq {
    private Long id;

    private String soBangKe;

    private String diaChiNguoiGiao;

    private String trangThai;

    private String lyDoTuChoi;

    private Long nam;

    private String loaiVthh;

    private String cloaiVthh;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNhapKho;

    private String soQdGiaoNvNh;

    private Long idQdGiaoNvNh;

    private String soHd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHd;

    private String soPhieuNhapKho;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNhapPhieuNk;

    private Long idDdiemGiaoNvNh;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private String nguoiGiaoHang;

    private String cmtNguoiGiaoHang;

    private String donViGiaoHang;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
    private Date thoiGianGiaoNhan;

    private String maDvi;

    private List<NhBangKeVtCtReq> children = new ArrayList<>();
    private ReportTemplateRequest reportTemplateRequest;
}
