package com.tcdt.qlnvhang.dto;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.*;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;

@Data
public class HopDongMttChildrenDtlDTO {
    private Long id;
    private Long idDiaDiem;
    private String maDiemKho;
    private String tenDiemKho;
    private String diaDiemNhap;
    private BigDecimal soLuong;
    private BigDecimal soLuongHd;
    private BigDecimal donGia;
    private BigDecimal donGiaVat;
    private String tongThanhTienStr;
    private Long idDtl;
    private String maCuc;
    private String maChiCuc;
    private String MaDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String tenCuc;
    private String tenChiCuc;
    private String tenNhaKho;
    private String tenNganKho;
    private String tenLoKho;
    private String tongThanhTien;
}
