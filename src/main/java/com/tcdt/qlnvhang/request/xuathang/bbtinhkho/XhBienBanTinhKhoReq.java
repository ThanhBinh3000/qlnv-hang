package com.tcdt.qlnvhang.request.xuathang.bbtinhkho;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class XhBienBanTinhKhoReq {
    private Long id;
    private Long qdId;
    private String loaiHangHoa;
    private String chungLoaiHangHoa;
    private String maDvi;
    private String capDvi;
    private String maDiemkho;
    private String maNhakho;
    private String maNgankho;
    private String maNganlo;
    private double soLuongXuat;
    private double soLuongThucTeConLai;
    private String nguyenNhan;
    private String kienNghi;
    List<XhBienBanTinhKhoCtReq> ds = new ArrayList<>();

}
