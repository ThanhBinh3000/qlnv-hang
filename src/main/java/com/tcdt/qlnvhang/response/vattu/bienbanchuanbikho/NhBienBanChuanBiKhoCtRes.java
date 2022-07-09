package com.tcdt.qlnvhang.response.vattu.bienbanchuanbikho;

import com.tcdt.qlnvhang.entities.vattu.bienbanchuanbikho.NhBienBanChuanBiKhoCt;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class NhBienBanChuanBiKhoCtRes {
    private Long id;
    private Long bbChuanBiKhoId;
    private String noiDung;
    private String donViTinh;
    private Long soLuongTrongNam;
    private Long donGiaTrongNam;
    private Long thanhTienTrongNam;
    private Long soLuongQt;
    private Long thanhTienQt;
    private Long tongGiaTri;

    public NhBienBanChuanBiKhoCtRes(NhBienBanChuanBiKhoCt item) {
        BeanUtils.copyProperties(item, this);
    }
}
