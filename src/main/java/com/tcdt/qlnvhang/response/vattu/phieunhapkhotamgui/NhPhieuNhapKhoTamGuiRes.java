package com.tcdt.qlnvhang.response.vattu.phieunhapkhotamgui;

import com.tcdt.qlnvhang.entities.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGui;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class NhPhieuNhapKhoTamGuiRes {
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

    private List<NhPhieuNhapKhoTamGuiCtRes> chiTiets = new ArrayList<>();
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
}
