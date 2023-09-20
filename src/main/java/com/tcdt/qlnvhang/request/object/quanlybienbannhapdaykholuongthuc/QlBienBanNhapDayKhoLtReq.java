package com.tcdt.qlnvhang.request.object.quanlybienbannhapdaykholuongthuc;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.request.object.SoBienBanPhieuReq;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QlBienBanNhapDayKhoLtReq extends BaseRequest {
    private Long id;

    private String soBienBanNhapDayKho;

    private String soQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr

    private Long idQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr

    private String soHd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayBatDauNhap;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKetThucNhap;

    private Long idDdiemGiaoNvNh;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private BigDecimal soLuong;

    private String maDvi;

    private Integer nam;

    private String ghiChu;
    private String maQhns;

    private List<QlBienBanNdkCtLtReq> chiTiets = new ArrayList<>();

    private List<FileDinhKemReq> fileDinhKems;
    private ReportTemplateRequest reportTemplateRequest;
}
