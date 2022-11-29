package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class HhDxuatKhMttSlddReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idHdr;
    private BigDecimal soLuong;
    private String maDvi;
    private String maDiemKho;

    private String diaDiemNhap;
    private BigDecimal donGia;
    private BigDecimal donGiaTamTinh;
    private BigDecimal thanhTien;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;

    private String nguonVon;
    private BigDecimal soLuongChiTieu;

    private BigDecimal soLuongKhDd;

    List<HhDxuatKhMttSlddDtlReq> children = new ArrayList<>();
}
