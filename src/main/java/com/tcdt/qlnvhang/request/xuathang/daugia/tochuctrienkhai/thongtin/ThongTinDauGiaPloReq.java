package com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ThongTinDauGiaPloReq extends BaseRequest {
    private Long id;
    private Long idDtl;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String maDviTsan;
    private BigDecimal tonKho;
    private BigDecimal soLuongDeXuat;
    private String donViTinh;
    private BigDecimal donGiaDeXuat;
    private BigDecimal donGiaDuocDuyet;
    private BigDecimal soTienDatTruoc;
    private Integer soLanTraGia;
    private BigDecimal donGiaTraGia;
    private String toChucCaNhan;

    public void setSoLuongDeXuat(BigDecimal soLuongDeXuat) {
        this.soLuongDeXuat = soLuongDeXuat;
    }
}
