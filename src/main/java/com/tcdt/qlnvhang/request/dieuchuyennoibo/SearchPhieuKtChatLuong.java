package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchPhieuKtChatLuong extends BaseRequest {
    private String loaiDc;
    private String loaiQdinh;
    private String typeQd;
    private Integer nam;
    private String soQdinhDcc;
    private String soBbLayMau;
    private String dViKiemNghiem;
    private String soPhieu;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private String maDvi;
    private String soBbXuatDocKho;
    private Boolean isVatTu = false;
    private String maNganKho;
    private String maNganKhoXuat;
    private String maLoKho;
    private String maLoKhoXuat;
    private List<String> dsLoaiHang = new ArrayList<>();
}
