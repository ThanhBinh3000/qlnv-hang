package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoDtl;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbPhieuNhapKhoHdrReq extends BaseRequest {

    private Long id;
    private String loaiDc;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String soPhieuNhapKho;
    private LocalDate ngayLap;
    private BigDecimal soNo;
    private BigDecimal soCo;
    @Column(name = "SO_BB_CB_KHO")
    private String soBbCbKho;
    @Column(name = "BB_CB_KHO_ID")
    private Long bBCbKhoId;
    private String soQdDcCuc;
    private Long qdDcCucId;
    private LocalDate ngayQdDcCuc;
    private Long idKeHoachDtl;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String soPhieuKtraCluong;
    private Long idPhieuKtraCluong;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private Long idNguoiLap;
    private String tenNguoiLap;
    private Long idLanhDao;
    private String tenLanhDao;
    private Long idThuKho;
    private String tenThuKho;
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
    private String bbNghiemThuBqld;
    private BigDecimal soLuongQdDcCuc;
    private String ghiChu;
    private String trangThai;
    private String lyDoTuChoi;
    private List<FileDinhKem> chungTuDinhKem = new ArrayList<>();

    private List<DcnbPhieuNhapKhoDtl> children = new ArrayList<>();
}
