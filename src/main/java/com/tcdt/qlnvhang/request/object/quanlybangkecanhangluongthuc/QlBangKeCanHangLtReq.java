package com.tcdt.qlnvhang.request.object.quanlybangkecanhangluongthuc;

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
public class QlBangKeCanHangLtReq {
    private Long id;

    private String soBangKe;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayNhap;
    private String thuKho;
    private Long qlPhieuNhapKhoLtId;
    private Long qdgnvnxId;
    private String maVatTu;
    private String donViTinh;
    private String tenNguoiGiaoHang;
    private String diaChiNguoiGiao;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maNganLo;
    private String soHd;
    private String diaDiem;
    private BigDecimal tongTrongLuongBaoBi;
    private List<QlBangKeChCtLtReq> chiTiets = new ArrayList<>();
}
