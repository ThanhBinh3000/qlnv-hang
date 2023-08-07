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
public class SearchPhieuKnChatLuong extends BaseRequest {
    private Integer nam;
    private String soQdinhDcc;
    private String typeQd;
    private String soBbLayMau;
    private String dViKiemNghiem;
    private String soPhieu;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private String maDvi;
    private String soBbXuatDocKho;
    private String loaiDc;
    private String loaiQdinh;
    private String type;
    private String typeDataLink;
    private String maNganKho;
    private String maLoKho;
    private Boolean isVatTu = false;
    private Boolean thayDoiThuKho;
    private List<String> dsLoaiHang = new ArrayList<>();
}
