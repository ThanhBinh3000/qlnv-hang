package com.tcdt.qlnvhang.request.object.vattu.bienbangiaonhan;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKho;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class NhBbGiaoNhanVtReq extends BaseRequest {

    private Long id;

    private Long nam;

    private String soBbGiaoNhan;

    private String soQdGiaoNvNh;

    private Long idQdGiaoNvNh;

    private String soHd;

    private LocalDate ngayHd;

    private String soHoSoKyThuat;

    private String soBbNhapDayKho;

    private Long idDdiemGiaoNvNh;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private String lyDoTuChoi;

    private String ghiChu;

    private String ketLuan;

    private String trangThai;

    private String maDvi;

    private Long nguoiGuiDuyetId;

    private LocalDate ngayGuiDuyet;

    private Long nguoiPheDuyetId;

    private LocalDate ngayPheDuyet;

    private List<NhBbGiaoNhanVtCtReq> chiTiets = new ArrayList<>();

    private List<FileDinhKemReq> fileDinhKemReqs = new ArrayList<>();

    private List<FileDinhKemReq> canCus = new ArrayList<>();
    private ReportTemplateRequest reportTemplateRequest;
}
