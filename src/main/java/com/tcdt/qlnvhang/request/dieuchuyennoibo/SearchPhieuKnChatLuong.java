package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SearchPhieuKnChatLuong extends BaseRequest {
    private Integer nam;
    private String soQdinhDcc;
    private String soBbLayMau;
    private String dViKiemNghiem;
    private String soPhieu;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private String maDvi;
    private String soBbXuatDocKho;
    private String loaiDc;
}
