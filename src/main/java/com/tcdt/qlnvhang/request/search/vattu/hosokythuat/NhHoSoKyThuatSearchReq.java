package com.tcdt.qlnvhang.request.search.vattu.hosokythuat;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhHoSoKyThuatSearchReq extends BaseRequest {
    private String soBienBan;
    private String soQdNhap;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayKiemTraTu;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayKiemTraDen;
    private String maVatTuCha;
    private String maVatTu;
    private String loaiVthh;
}
