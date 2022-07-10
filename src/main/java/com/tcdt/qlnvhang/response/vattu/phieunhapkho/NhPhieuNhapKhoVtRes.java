package com.tcdt.qlnvhang.response.vattu.phieunhapkho;

import com.tcdt.qlnvhang.response.SoBienBanPhieuRes;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class NhPhieuNhapKhoVtRes extends SoBienBanPhieuRes {
    private Long id;
    private Long qdgnvnxId;
    private String soQuyetDinhNhap;
    private String soPhieu;
    private LocalDate ngayNhapKho;
    private BigDecimal no;
    private BigDecimal co;
    private String nguoiGiaoHang;
    private LocalDateTime thoiGianGiaoNhanHang;
    private LocalDate ngayTaoPhieu;

    private String tenDiemKho;
    private String maDiemKho;
    private String tenNhaKho;
    private String maNhaKho;
    private String tenNganKho;
    private String maNganKho;
    private String tenNganLo;
    private String maNganLo;

    private BigDecimal tongSoLuong;
    private BigDecimal tongSoTien;
    private String loaiVthh;
    private String maDvi;
    private String tenDvi;

    private String trangThai;
    private String tenTrangThai;
    private String trangThaiDuyet;

    private List<NhPhieuNhapKhoVtCtRes> chiTiets = new ArrayList<>();
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
}
