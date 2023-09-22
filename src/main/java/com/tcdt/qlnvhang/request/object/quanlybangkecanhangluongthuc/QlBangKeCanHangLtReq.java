package com.tcdt.qlnvhang.request.object.quanlybangkecanhangluongthuc;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.request.object.SoBienBanPhieuReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QlBangKeCanHangLtReq extends BaseRequest {

    private Long id;

    private String soBangKe;

    private String soQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr

    private Long idQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr

    private Long idDdiemGiaoNvNh;

    private String maDvi;

    private Integer nam;

    private String soPhieuNhapKho;
    private String maQhns;
    private String nguoiGiamSat;
    private BigDecimal trongLuongBaoBi;
    private List<FileDinhKemReq> fileDinhKems;

    private List<QlBangKeChCtLtReq> chiTiets = new ArrayList<>();

}
