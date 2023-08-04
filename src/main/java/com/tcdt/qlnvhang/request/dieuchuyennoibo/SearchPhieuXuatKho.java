package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchPhieuXuatKho extends BaseRequest {
    private Integer nam;
    private Long qdinhDccId;
    private String typeQd;
    private String soQdinhDcc;
    private String soBbLayMau;
    private String soPhieuXuatKho;
    private String dViKiemNghiem;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private String maDvi;
    private String loaiDc;
    private String maLoKho;
    private String maNganKho;
    private String loaiQdinh;
    private String trangThai;
    private Boolean isVatTu = false;
    private Boolean thayDoiThuKho;
    private List<String> dsLoaiHang = new ArrayList<>();
}
