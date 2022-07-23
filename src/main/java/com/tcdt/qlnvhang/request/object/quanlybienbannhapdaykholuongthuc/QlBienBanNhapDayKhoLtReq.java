package com.tcdt.qlnvhang.request.object.quanlybienbannhapdaykholuongthuc;


import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.request.object.SoBienBanPhieuReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QlBienBanNhapDayKhoLtReq extends SoBienBanPhieuReq {
    private Long id;

    @NotNull(message = "Không được để trống")
    private Long qdgnvnxId;
    private Long bbNghiemThuId;
    private String soBienBan;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayNhapDayKho;
    private String maVatTu;
    private String maVatTuCha;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayBatDauNhap;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayKetThucNhap;
    private String thuKho;
    private String kyThuatVien;
    private String keToan;
    private String thuTruong;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maNganLo;

    private List<QlBienBanNdkCtLtReq> chiTiets = new ArrayList<>();

    private List<FileDinhKemReq> fileDinhKems;
}
