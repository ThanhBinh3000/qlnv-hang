package com.tcdt.qlnvhang.request.object.vattu.bienbanguihang;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.SoBienBanPhieuReq;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class NhBienBanGuiHangReq extends BaseRequest {

    private Long id;

    private String maDvi;

    private String soQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr

    private Long idQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr

    private String soBienBanGuiHang;

    private String soHd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHd;

    private String loaiVthh;

    private String cloaiVthh;

    private String donViTinh;

    private String tinhTrang;

    private String chatLuong;

    private String phuongPhap;

    private String ghiChu;

    private String benNhan;

    private String benGiao;

    private String trachNhiemBenNhan;

    private String trachNhiemBenGiao;

    private Integer nam;

    private Long idDdiemGiaoNvNh;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private BigDecimal soLuongDdiemGiaoNvNh;

    private List<NhBienBanGuiHangCtReq> children = new ArrayList<>();
    private ReportTemplateRequest reportTemplateRequest;
}
