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
public class SearchBangKeCanHang extends BaseRequest {
    private Integer nam;
    private String loaiDc;
    private String loaiQdinh;
    private String soQdinhDcc;
    private String typeQd;
    private String soBangKe;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private String maLoKho;
    private String maNganKho;
    private String maDvi;
    private String type;
    private String typeDataLink;
    private Boolean thayDoiThuKho;
    private Boolean isVatTu = false;
    private List<String> dsLoaiHang = new ArrayList<>();
}
