package com.tcdt.qlnvhang.request.object.vattu.phieunhapkhotamgui;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class NhPhieuNhapKhoTamGuiReq {
    private Long id;
    private Long qdgnvnxId;
    private String soPhieu;
    private LocalDate ngayNhapKho;
    private BigDecimal no;
    private BigDecimal co;
    private String nguoiGiaoHang;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime thoiGianGiaoNhanHang;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayTaoPhieu;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maNganLo;
    private BigDecimal tongSoLuong;
    private BigDecimal tongSoTien;
    private String loaiVthh;

    private List<NhPhieuNhapKhoTamGuiCtReq> chiTiets = new ArrayList<>();
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
}
