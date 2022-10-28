package com.tcdt.qlnvhang.request.object.quanlybienbannhapdaykholuongthuc;


import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.request.object.SoBienBanPhieuReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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

    private LocalDate ngayHd;

    private LocalDate ngayBatDauNhap;

    private LocalDate ngayKetThucNhap;

    private Long idDdiemGiaoNvNh;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private BigDecimal soLuong;

    private String maDvi;

    private Integer nam;

    private String ghiChu;

    private List<QlBienBanNdkCtLtReq> chiTiets = new ArrayList<>();

    private List<FileDinhKemReq> fileDinhKems;
}
