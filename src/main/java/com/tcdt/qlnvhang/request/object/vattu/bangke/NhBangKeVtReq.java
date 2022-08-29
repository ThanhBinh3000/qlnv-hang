package com.tcdt.qlnvhang.request.object.vattu.bangke;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class NhBangKeVtReq {
    private Long id;
    private Long qdgnvnxId;
    private String soBangKe;
    private Long phieuNhapKhoId;
    private String maVatTuCha;
    private String maVatTu;
    private String donViTinh;
    private String diaChiNguoiGiao;
    private Long hopDongId;
    private LocalDate ngayNhap;
    private LocalDate ngayTaoBangKe;
    private String trangThai;
    private String lyDoTuChoi;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;

    private List<NhBangKeVtCtReq> chiTiets = new ArrayList<>();
}
