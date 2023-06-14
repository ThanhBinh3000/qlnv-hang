package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class SearchDcnbBienBanLayMau extends BaseRequest {
    private Integer nam;
    private Long qDinhDccId;
    private String soQdinhDcc;
    private String soBbLayMau;
    private String dViKiemNghiem;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private String maDvi;
    private String loaiDc;
    private List<String> loaiDcs = new ArrayList<>();
    private String trangThai;
    private String type;
    private String typeDataLink;
}
