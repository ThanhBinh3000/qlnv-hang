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
public class SearchDcnbBienBanTinhKho extends BaseRequest {
    private Integer nam;
    private String soQdinhDcc;
    private String typeQd;
    private String qdinhDccId;
    private String soBbTinhKho;
    private String dViKiemNghiem;
    private LocalDate tuNgayBdXuat;
    private LocalDate denNgayBdXuat;
    private LocalDate tuNgayKtXuat;
    private LocalDate denNgayKtXuat;
    private LocalDate tuNgayXhXuat;
    private LocalDate denNgayXhXuat;
    private String maLoKho;
    private String maNganKho;
    private String maDvi;
    private String loaiDc;
    private String loaiQdinh;
    private String type;
    private Boolean isVatTu = false;
    private Boolean thayDoiThuKho;
    private List<String> dsLoaiHang = new ArrayList<>();
}
