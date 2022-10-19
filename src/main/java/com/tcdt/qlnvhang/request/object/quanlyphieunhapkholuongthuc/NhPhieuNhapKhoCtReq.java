package com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhPhieuNhapKhoCtReq {
    private Long id;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String donViTinh;

    private BigDecimal soLuongChungTu;

    private BigDecimal soLuongThucNhap;

    private BigDecimal donGia;

    private String maSo;

}
