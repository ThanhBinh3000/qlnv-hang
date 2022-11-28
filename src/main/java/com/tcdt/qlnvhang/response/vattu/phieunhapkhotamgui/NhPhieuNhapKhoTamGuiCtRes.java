package com.tcdt.qlnvhang.response.vattu.phieunhapkhotamgui;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiCt;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class NhPhieuNhapKhoTamGuiCtRes {
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

    public NhPhieuNhapKhoTamGuiCtRes (NhPhieuNhapKhoTamGuiCt item) {
        BeanUtils.copyProperties(item, this);
    }

}
