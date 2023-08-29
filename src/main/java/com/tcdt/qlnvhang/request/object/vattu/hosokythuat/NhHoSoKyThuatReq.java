package com.tcdt.qlnvhang.request.object.vattu.hosokythuat;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.hosokythuat.NhHoSoBienBanNk;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.kiemtrachatluong.NhHoSoBienBanReq;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.request.object.SoBienBanPhieuReq;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class NhHoSoKyThuatReq extends BaseRequest {

    private Long id;

    private Long idQdGiaoNvNh;

    private String soQdGiaoNvNh;

    private String soBbLayMau;

    private String soHd;

    private String maDvi;

    private String soHoSoKyThuat;

    private Integer nam;
    private Long idBbLayMau;
    private String tenNguoiTao;
    private Integer idBbLayMauXuat;
    private Boolean kqKiemTra;
    private String soBbKtraNgoaiQuan;
    private String soBbKtraVanHanh;
    private String soBbKtraHskt;

    private List<NhHoSoKyThuatCtReq> children = new ArrayList<>();

    @Transient
    private List<NhHoSoBienBanReq> listHoSoBienBan = new ArrayList<>();
//
//    private List<FileDinhKemReq> fileDinhKemReqs = new ArrayList<>();
//
//    private List<FileDinhKemReq> fdkCanCus = new ArrayList<>();
private ReportTemplateRequest reportTemplateRequest;
}
