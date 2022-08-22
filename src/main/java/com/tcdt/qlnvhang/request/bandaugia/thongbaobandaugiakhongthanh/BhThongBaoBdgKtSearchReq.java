package com.tcdt.qlnvhang.request.bandaugia.thongbaobandaugiakhongthanh;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BhThongBaoBdgKtSearchReq extends BaseRequest {
    private String soBienBan;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayToChucBdgTu;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayToChucBdgDen;
    private String loaiVthh;
    private String maVatTu;
    private String maVatTuCha;
    private Integer nam;
    private String maThongBaoBdg;
    private String trichYeu;
}
