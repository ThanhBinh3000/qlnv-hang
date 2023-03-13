package com.tcdt.qlnvhang.request.xuathang.bangkecanhang;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class XhBangKeCanHangReq extends BaseRequest {
    private Long id;
    // Số quyết định xuất id
    private Long sqdxId;
    private Long phieuXuatKhoId;

    private String maDvi;
    private String maQHNS;
    private String soBangKe;

    private String thuKhoId;
    private String maLokho;
    private String maNgankho;
    private String maNhakho;
    private String maDiemkho;
    private String maChungLoaiHangHoa;
    private String maLoaiHangHoa;
    private String diaDiem;
    private String trangThai;

    private Long nguoiTaoId;
    private LocalDate ngayTao;
    private Long nguoiSuaId;
    private LocalDate ngaySua;
    private Long nguoiGuiDuyetId;
    private LocalDate ngayGuiDuyet;
    private Long nguoiPduyetId;
    private LocalDate ngayPduyet;

    private Integer nam;
    private String lyDoTuChoi;
    private String soHopDong;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
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

    private List<XhBangKeCanHangCtReq> ds = new ArrayList<>();
}
