package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBbNhapDayKhoDtl;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HhNkBbNhapDayKhoHdrReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String soBb;
    private LocalDate ngayLap;
    private String soQdPdNk;
    private Long qdPdNkId;
    private LocalDate ngayQdPdNk;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenDiemKho;
    private String tenNhaKho;
    private String tenNganKho;
    private String tenLoKho;
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private String donViTinh;
    private LocalDate ngayBdNhap;
    private LocalDate ngayKtNhap;
    private BigDecimal soLuongQd;
    private String ghiChu;
    private Long idThuKho;
    private Long idKyThuatVien;
    private Long idKeToan;
    private Long idLanhDao;

    private String tenThuKho;
    private String tenKyThuatVien;
    private String tenKeToan;
    private String tenLanhDao;
    private String trangThai;
    private String lyDoTuChoi;
    private List<HhNkBbNhapDayKhoDtl> children = new ArrayList<>();
    private LocalDate tuNgayBdNhap;
    private LocalDate denNgayBdNhap;
    private LocalDate tuNgayKtNhap;
    private LocalDate denNgayKtNhap;
    private LocalDate tuNgayThoiHanNh;
    private LocalDate denNgayThoiHanNh;
}
