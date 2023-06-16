package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SearchPhieuXuatKho extends BaseRequest {
    private Integer nam;
    private String soQdinhDcc;
    private String soBbLayMau;
    private String dViKiemNghiem;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private String maDvi;
    private String loaiDc;
    private String loaiQdinh;
    private String trangThai;
    private Boolean isVatTu = false;
    private Boolean thayDoiThuKho;
    private List<String> dsLoaiHang = new ArrayList<>();
}
