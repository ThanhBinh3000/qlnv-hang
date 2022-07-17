package com.tcdt.qlnvhang.request.search.vattu.bienbangiaonhan;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NhBbGiaoNhanVtSearchReq extends BaseRequest {
    private String soBienBan;
    private String soQdNhap;
    private String soHopDong;
    private Integer nam;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayHopDongTu;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayHopDongDen;
    private String loaiVthh;
}
