package com.tcdt.qlnvhang.response.xuathang.bangkecanhang;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class XhBangKeCanHangRes {
    private Long id;
    // Số quyết định xuất id
    private Long sqdxId;
    private String soSqdx;
    private Long phieuXuatKhoId;
    private String soPhieuXuatKho;

    private String maDvi;
    private String tenDvi;
    private String maQHNS;
    private String soBangKe;
    private String thuKhoId;

    private String maDiemkho;
    private String tenDiemkho;
    private String maNhakho;
    private String tenNhakho;
    private String maNgankho;
    private String tenNgankho;
    private String maNganlo;
    private String tenNganlo;
    private String maLoaiHangHoa;
    private String tenLoaiHangHoa;
    private String maChungLoaiHangHoa;
    private String tenChungLoaiHangHoa;

    private String diaDiem;
    private String trangThai;
    private String tenTrangThai;

    private Integer nam;
    private String lyDoTuChoi;
    private String soHopDong;
    private LocalDate ngayNhap;
    private String dviTinhId;
    // trọng lượng bao bì
    private Integer tlBb;
    // trọng lượng trừ bao bì
    private Integer tlTruBb;
    // trọng lượng kể cả bao bì
    private Integer tlKeCaBb;

    private String tenNguoiNhan;
    private String diaChiNguoiNhan;

    private List<XhBangKeCanHangCtRes> ds = new ArrayList<>();
}
