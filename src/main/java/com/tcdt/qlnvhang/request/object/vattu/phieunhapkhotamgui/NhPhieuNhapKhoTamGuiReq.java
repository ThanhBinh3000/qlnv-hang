package com.tcdt.qlnvhang.request.object.vattu.phieunhapkhotamgui;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private LocalDate thoiGianGiaoNhanHang;
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
