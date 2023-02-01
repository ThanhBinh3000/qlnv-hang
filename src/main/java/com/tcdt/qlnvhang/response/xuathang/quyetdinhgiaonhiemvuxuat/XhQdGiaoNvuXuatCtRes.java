package com.tcdt.qlnvhang.response.xuathang.quyetdinhgiaonhiemvuxuat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class XhQdGiaoNvuXuatCtRes {
    private Long id;

    private Integer stt;
    private String maDvi;
    private String tenDvi;
    private String maVatTuCha;
    private String tenVatTuCha;
    private String maVatTu;
    private String tenVatTu;
    private String maDiemKho;
    private String tenDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maNganLo;
    private String tenNganLo;
    private String donViTinh;
    private BigDecimal soLuong;
    private BigDecimal donGiaKhongThue;
    private BigDecimal thanhTien;
    private LocalDate thoiHanXuatBan;
    private Long qdgnvxId;

}
