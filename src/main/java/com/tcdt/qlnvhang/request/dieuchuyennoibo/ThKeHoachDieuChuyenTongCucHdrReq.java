package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ThKeHoachDieuChuyenTongCucHdrReq implements Serializable {
    private Long id;

    private LocalDate ngaytao;

    private Long nguoiTaoId;

    private LocalDate ngaySua;

    private Long nguoiSuaId;

    private String maTongHop;

    private LocalDate ngayTongHop;

    private String noiDung;

    private Integer namKeHoach;

    private String loaiDieuChuyen;

    private LocalDate thTuNgay;

    private LocalDate thDenNgay;

    private String loaiHangHoa;

    private String tenLoaiHangHoa;

    private String chungLoaiHangHoa;

    private String trangThai;

    private String maDVi;

    private String tenDVi;

    private LocalDateTime thoiGianTongHop;

    private Long idThTongCuc;

    List<ThKeHoachDieuChuyenTongCucDtlReq> thKeHoachDieuChuyenTongCucDtls;
}
