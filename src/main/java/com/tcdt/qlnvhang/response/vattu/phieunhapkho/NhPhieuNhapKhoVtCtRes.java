package com.tcdt.qlnvhang.response.vattu.phieunhapkho;

import com.tcdt.qlnvhang.entities.vattu.phieunhapkho.NhPhieuNhapKhoVtCt;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class NhPhieuNhapKhoVtCtRes {
    private Long id;
    private Long phieuNkTgId;
    private String maSo;
    private String donViTinh;
    private BigDecimal soLuongChungTu;
    private BigDecimal soLuongThucNhap;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String vthh;
    private Integer stt;

    public NhPhieuNhapKhoVtCtRes(NhPhieuNhapKhoVtCt item) {
        BeanUtils.copyProperties(item, this);
    }

}
