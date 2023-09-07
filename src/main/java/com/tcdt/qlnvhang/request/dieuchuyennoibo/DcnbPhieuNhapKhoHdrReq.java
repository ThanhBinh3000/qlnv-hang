package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoDtl;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbPhieuNhapKhoHdrReq extends BaseRequest {

    private Long id;
    @NotNull
    private String loaiQdinh;
    @NotNull
    private String typeQd;
    @NotNull
    private String loaiDc;
    private Boolean thayDoiThuKho;
    @NotNull
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    @NotNull
    private String maQhns;
    @NotNull
    private String soPhieuNhapKho;
    @NotNull
    private LocalDate ngayLap;
    @NotNull
    private BigDecimal soNo;
    @NotNull
    private BigDecimal soCo;
    @NotNull
    private String soBbCbKho;
    @NotNull
    private Long bBCbKhoId;
    @NotNull
    private String soQdDcCuc;
    @NotNull
    private Long qdinhDccId;
    @NotNull
    private Long qdDcCucId;
    @NotNull
    private LocalDate ngayQdDcCuc;
    private Long idKeHoachDtl;
    @NotNull
    private String maDiemKho;
    @NotNull
    private String maNhaKho;
    @NotNull
    private String maNganKho;
    private String maLoKho;
    @NotNull
    private String tenDiemKho;
    @NotNull
    private String tenNhaKho;
    @NotNull
    private String tenNganKho;
    private String tenLoKho;
    @NotNull
    private String soPhieuKtraCluong;
    @NotNull
    private Long idPhieuKtraCluong;
    @NotNull
    private String loaiVthh;
    @NotNull
    private String cloaiVthh;
    @NotNull
    private String tenLoaiVthh;
    @NotNull
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
    private BigDecimal tongSoLuong;
    private String tongSoLuongBc;
    private BigDecimal tongKinhPhi;
    private String tongKinhPhiBc;
    private String ghiChu;
    private String trangThai;
    private String lyDoTuChoi;
    private List<FileDinhKem> chungTuDinhKem = new ArrayList<>();
    @Valid
    private List<DcnbPhieuNhapKhoDtl> children = new ArrayList<>();
    private Boolean isVatTu = false;
    private List<String> dsLoaiHang = new ArrayList<>();
}
