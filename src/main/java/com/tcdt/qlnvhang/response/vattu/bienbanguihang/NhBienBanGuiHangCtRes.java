package com.tcdt.qlnvhang.response.vattu.bienbanguihang;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbanguihang.NhBienBanGuiHangCt;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class NhBienBanGuiHangCtRes {
    private Long id;
    private String chucVu;
    private String daiDien;
    private Long bienBanGuiHangId;
    private String loaiBen;
    private String stt;

    public NhBienBanGuiHangCtRes(NhBienBanGuiHangCt item) {
        BeanUtils.copyProperties(item, this);
    }
}
