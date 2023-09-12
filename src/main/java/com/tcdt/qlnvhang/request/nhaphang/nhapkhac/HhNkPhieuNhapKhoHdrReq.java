package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkPhieuNhapKhoDtl;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class HhNkPhieuNhapKhoHdrReq extends BaseRequest {

    private Long id;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String soPhieuNhapKho;
    private LocalDate ngayLap;
    private BigDecimal soNo;
    private BigDecimal soCo;
    private String soQdGiaoNv;
    private Long qdGiaoNvId;
    private LocalDate ngayQdGiaoNv;
    private String soBbCbKho;
    private Long bBCbKhoId;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String tenDiemKho;
    private String tenNhaKho;
    private String tenNganKho;
    private String tenLoKho;
    private String soPhieuKtraCluong;
    private Long idPhieuKtraCluong;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private Long idThuKho;
    private String tenThuKho;
    private Long idNguoiLap;
    private String tenNguoiLap;
    private Long idLanhDao;
    private String tenLanhDao;
    private Long idKyThuatVien;
    private String tenKyThuatVien;
    private Long idKeToanTruong;
    private String keToanTruong;
    private String hoVaTenNguoiGiao;
    private String cmndNguoiGiao;
    private String donViNguoiGiao;
    private String diaChi;
    private LocalDate tgianGiaoNhanHang;
    private String loaiHinhNx;
    private String kieuNx;
    private String bbNghiemThuBqldIds;
    private String bbNghiemThuBqld;
    private String soBangKeCanHang;
    private Long bangKeCanHangId;
    private String soBangKeNhapVt;
    private Long bangKeNhapVtId;
    private String soBbKtNk;
    private Long bbKtNkId;
    private BigDecimal tongSoLuong;
    private String tongSoLuongBc;
    private String ghiChu;
    private String trangThai;
    private String lyDoTuChoi;
    private String donViTinh;
    private List<FileDinhKem> chungTuDinhKem = new ArrayList<>();
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    private List<HhNkPhieuNhapKhoDtl> children = new ArrayList<>();
    private String tenTrangThai;
}
