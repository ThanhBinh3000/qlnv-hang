package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class SearchDcnbBienBanHaoDoi extends BaseRequest {
    private Integer nam;
    private String soQdinhDcc;
    private String typeQd;
    private String soBbHaoDoi;
    private LocalDate tuNgayLapBb;
    private LocalDate denNgayLapBb;
    private LocalDate tuNgayBdXuat;
    private LocalDate denNgayBdXuat;
    private LocalDate tuNgayKtXuat;
    private LocalDate denNgayKtXuat;
    private LocalDate tuNgayXhXuat;
    private LocalDate denNgayXhXuat;
    private String maDvi;
    private String maLoKho;
    private String maNganKho;
    private String loaiDc;
    private String loaiQdinh;
    private Boolean isVatTu = false;
    private Boolean thayDoiThuKho;
    private List<String> dsLoaiHang = new ArrayList<>();
}
