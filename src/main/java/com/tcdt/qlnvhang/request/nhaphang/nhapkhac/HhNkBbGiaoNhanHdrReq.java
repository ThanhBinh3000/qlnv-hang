package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBbGiaoNhanDtl;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBbGiaoNhanTTDtl;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class HhNkBbGiaoNhanHdrReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String soBb;
    private LocalDate ngayLap;
    private LocalDate ngayTaoBk;
    private Long qdPdNkId;
    private String soQdPdNk;
    private LocalDate ngayQdPdNk;
    private String soBbKtNhapKho;
    private Long idBbKtNhapKho;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String tenDiemKho;
    private String tenNhaKho;
    private String tenNganKho;
    private String tenLoKho;
    private String soHoSoKyThuat;
    private Long idHoSoKyThuat;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private String donViTinh;
    private LocalDate ngayBdNhap;
    private LocalDate ngayKtNhap;
    private BigDecimal soLuongQd;
    private String ghiChu;
    private String ketLuan;
    private Long idCanBo;
    private String tenCanBo;
    private Long idLanhDao;
    private String tenLanhDao;
    private Long nguoiGDuyet;
    private LocalDate ngayGDuyet;
    private Long nguoiPDuyet;
    private LocalDate ngayPDuyet;
    private String trangThai;
    private String lyDoTuChoi;
    private Boolean thayDoiThuKho;
    private Boolean isVatTu = false;
    private List<String> dsLoaiHang;
    private List<FileDinhKemReq> fileCanCuReq = new ArrayList<>();

    private List<HhNkBbGiaoNhanDtl> danhSachDaiDien = new ArrayList<>();
    private List<HhNkBbGiaoNhanTTDtl> danhSachBangKe = new ArrayList<>();

    private LocalDate tuNgayKtnk;
    private LocalDate denNgayKtnk;
    private LocalDate tuNgayThoiHanNhap;
    private LocalDate denNgayThoiHanNhap;
}
