package com.tcdt.qlnvhang.response.banhangdaugia.bienbanbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.bienbanbandaugia.BhBbBanDauGiaCt;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class BhBbBanDauGiaCtRes {
    private Long id;
    private Long stt;
    private String hoTen;
    private String chucVu;
    private String noiCongTac;
    private String loaiTptg;
    private Long bbBanDauGiaId;
    private String mstCmtndCccdHoChieu;
    private String diaChi;

    public BhBbBanDauGiaCtRes(BhBbBanDauGiaCt ct) {
        BeanUtils.copyProperties(ct, this);
    }
}
