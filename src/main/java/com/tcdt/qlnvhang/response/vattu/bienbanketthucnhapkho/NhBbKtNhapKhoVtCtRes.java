package com.tcdt.qlnvhang.response.vattu.bienbanketthucnhapkho;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbanketthucnhapkho.NhBbKtNhapKhoVtCt;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class NhBbKtNhapKhoVtCtRes {
    private Long id;
    private Integer stt;
    private BigDecimal soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String ghiChu;
    private Long bbKtNhapKhoId;

    public NhBbKtNhapKhoVtCtRes(NhBbKtNhapKhoVtCt item) {
        BeanUtils.copyProperties(item, this);
    }
}
