package com.tcdt.qlnvhang.request.object.vattu.hosokythuat;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.request.object.SoBienBanPhieuReq;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
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

    private List<NhHoSoKyThuatCtReq> children = new ArrayList<>();
//
//    private List<FileDinhKemReq> fileDinhKemReqs = new ArrayList<>();
//
//    private List<FileDinhKemReq> fdkCanCus = new ArrayList<>();
}
