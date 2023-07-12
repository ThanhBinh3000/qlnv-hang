package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBBKetThucNKDtl;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class HhNkBBKetThucNKReq extends BaseRequest {
    private Long id;
    private String loaiVthh;
    private String cloaiVthh;
    private Integer nam;
    private String soBb;
    private LocalDate ngayLap;
    private String maDvi;
    private String maQhns;
    private Long qdPdNkId;
    private String soQdPdNk;
    private LocalDate ngayQdPdNk;
    private String maDiemKho;
    private String tenDiemKho;
    private String diaDaDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maLoKho;
    private String tenLoKho;
    private LocalDate ngayBatDauNhap;
    private LocalDate ngayKetThucNhap;
    private BigDecimal tongSlTheoQd;
    private String maLanhDaoChiCuc;
    private String tenLanhDaoChiCuc;
    private Long thuKhoId;
    private String tenThuKho;
    private Long ktvBQuan;
    private String tenKtvBQuan;
    private Long keToanTruong;
    private String tenKeToanTruong;
    private String donViTinh;
    private String trangThai;
    private String lyDoTuChoi;
    private Long nguoiGDuyet;
    private LocalDate ngayGDuyet;
    private Long nguoiPDuyetKtv;
    private LocalDate ngayPDuyetKtv;
    private Long nguoiPDuyetTvqt;
    private LocalDate ngayPDuyetTvqt;
    private Long nguoiPDuyetKt;
    private LocalDate ngayPDuyetKt;
    private Long nguoiPDuyet;
    private LocalDate ngayPDuyet;
    private List<HhNkBBKetThucNKDtl> hhNkBBKetThucNKDtl = new ArrayList<>();
    private LocalDate tuNgayKtnk;
    private LocalDate denNgayKtnk;
    private LocalDate tuNgayThoiHanNhap;
    private LocalDate denNgayThoiHanNhap;
}
