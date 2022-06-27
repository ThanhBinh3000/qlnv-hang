package com.tcdt.qlnvhang.response.quanlybienbannhapdaykholuongthuc;


import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
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
public class QlBienBanNhapDayKhoLtRes {
    private Long id;
    private String soBienBan;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayLap;
    private String maDiemKho;
    private String tenDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maNganLo;
    private String tenNganLo;
    private String maVatTu;
    private String tenVatTu;
    private String maVatTuCha;
    private String tenVatTuCha;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayBatDauNhap;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayKetThucNhap;
    private String thuKho;
    private String kyThuatVien;
    private String keToan;
    private String thuTruong;
    private String trangThai;
    private String tenTrangThai;
    private String trangThaiDuyet;
    private Long qdgnvnxId;
    private String soQuyetDinhNhap;
    private String maDvi;
    private String tenDvi;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayNhapDayKho;
    private List<QlBienBanNdkCtLtRes> chiTiets = new ArrayList<>();
    private List<FileDinhKem> fileDinhKems;
}
