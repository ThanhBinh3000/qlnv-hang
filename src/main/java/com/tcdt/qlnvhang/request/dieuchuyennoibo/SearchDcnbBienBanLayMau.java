package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class SearchDcnbBienBanLayMau extends BaseRequest {
    private Long id;
    private Integer nam;
    private Long qdDcCucId;
    private String typeQd;
    private String soQdinhDcc;
    private String soBbLayMau;
    private String dViKiemNghiem;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private String maDvi;
    private String loaiDc;
    private String loaiQdinh;
    private List<String> loaiDcs = new ArrayList<>();
    private String trangThai;
    private String type;
    private String typeDataLink;
    private String maNganKho;
    private String maLoKho;
    private Boolean isVatTu = false;
    private Boolean thayDoiThuKho;
    private List<String> dsLoaiHang = new ArrayList<>();
}
