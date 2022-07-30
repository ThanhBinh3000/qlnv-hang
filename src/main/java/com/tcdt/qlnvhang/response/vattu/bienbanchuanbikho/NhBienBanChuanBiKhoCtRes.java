package com.tcdt.qlnvhang.response.vattu.bienbanchuanbikho;

import com.tcdt.qlnvhang.entities.nhaphang.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoCt;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class NhBienBanChuanBiKhoCtRes {
    private Long id;
    private Long bbChuanBiKhoId;
    private String noiDung;
    private String donViTinh;
    private BigDecimal soLuongTrongNam;
    private BigDecimal donGiaTrongNam;
    private BigDecimal thanhTienTrongNam;
    private BigDecimal soLuongQt;
    private BigDecimal thanhTienQt;
    private BigDecimal tongGiaTri;

    public NhBienBanChuanBiKhoCtRes(NhBienBanChuanBiKhoCt item) {
        BeanUtils.copyProperties(item, this);
    }
}
