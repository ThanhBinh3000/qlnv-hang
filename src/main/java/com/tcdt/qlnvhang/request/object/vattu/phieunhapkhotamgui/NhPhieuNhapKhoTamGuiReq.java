package com.tcdt.qlnvhang.request.object.vattu.phieunhapkhotamgui;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.request.object.SoBienBanPhieuReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class NhPhieuNhapKhoTamGuiReq extends BaseRequest {

    private Long id;

    private Long idQdGiaoNvNh;

    private String soQdGiaoNvNh;

    private String soPhieuNhapKhoTamGui;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNhapKho;

    private String soHd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHd;

    private BigDecimal soNo;

    private BigDecimal soCo;

    private String nguoiGiaoHang;

    private String cmtNguoiGiaoHang;

    private String donViGiaoHang;

    private String diaChiNguoiGiao;

    private String keToanTruong;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_STR)
    private Date thoiGianGiaoNhan;

    private Long idDdiemGiaoNvNh;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private BigDecimal soLuongDdiemGiaoNvNh;

    private String loaiVthh;

    private String cloaiVthh;

    private String maDvi;

    private Integer nam;

    private String ghiChu;

    private List<NhPhieuNhapKhoTamGuiCtReq> children = new ArrayList<>();
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    private ReportTemplateRequest reportTemplateRequest;
}
