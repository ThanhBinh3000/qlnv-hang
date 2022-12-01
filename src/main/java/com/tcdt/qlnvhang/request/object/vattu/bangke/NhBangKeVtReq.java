package com.tcdt.qlnvhang.request.object.vattu.bangke;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class NhBangKeVtReq {
    private Long id;

    private String soBangKe;

    private String diaChiNguoiGiao;

    private String trangThai;

    private String lyDoTuChoi;

    private Long nam;

    private String loaiVthh;

    private String cloaiVthh;

    private LocalDate ngayNhapKho;

    private String soQdGiaoNvNh;

    private Long idQdGiaoNvNh;

    private String soHd;

    private LocalDate ngayHd;

    private String soPhieuNhapKho;

    private Long idDdiemGiaoNvNh;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private String nguoiGiaoHang;

    private String cmtNguoiGiaoHang;

    private String donViGiaoHang;

    private LocalDate thoiGianGiapNhan;

    private String maDvi;


    private List<NhBangKeVtCtReq> chiTiets = new ArrayList<>();
}
