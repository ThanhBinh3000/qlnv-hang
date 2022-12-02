package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia;

import com.tcdt.qlnvhang.contraints.CompareDate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhDxKhBanDauGiaPhanLoReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;
    private Long idHdr;
    private String maDvi;
    private String tenDvi;
    private String maDiemKho;
    private String diaDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String maDviTsan;
    private BigDecimal duDau;
    private BigDecimal soLuong;
    private BigDecimal giaKhongVat;
    private BigDecimal giaKhoiDiem;
    private BigDecimal donGiaVat;
    private BigDecimal giaKhoiDiemDduyet;
    private BigDecimal tienDatTruoc;
    private BigDecimal tienDatTruocDduyet;

    private BigDecimal  soLuongChiTieu;
    private BigDecimal soLuongKh;

    private String dviTinh;

    private List<XhDxKhBanDauGiaDtlReq> children = new ArrayList<>();
}
