package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import lombok.Data;

import javax.persistence.Column;
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

    private Long maTongHop;

    private LocalDate ngayTongHop;

    private String trichYeu;

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

    private Long qdDcId;

    private String soQddc;

    private String loaiHinhNhapXuat;

    private String tenLoaiHinhNhapXuat;

    private String kieuNhapXuat;

    private String tenKieuNhapXuat;

    List<ThKeHoachDieuChuyenTongCucDtlReq> thKeHoachDieuChuyenTongCucDtls;
}
