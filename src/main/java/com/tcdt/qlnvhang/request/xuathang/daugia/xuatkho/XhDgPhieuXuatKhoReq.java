package com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhDgPhieuXuatKhoReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhNs;
    private String soPhieuXuatKho;
    private LocalDate ngayLapPhieu;
    private BigDecimal taiKhoanNo;
    private BigDecimal taiKhoanCo;
    private Long idQdNv;
    private String soQdNv;
    private LocalDate ngayKyQdNv;
    private Long idQdNvDtl;
    private String loaiHinhNx;
    private String kieuNhapXuat;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private String toChucCaNhan;
    private Long idPhieuKiemNghiem;
    private String soPhieuKiemNghiem;
    private LocalDate ngayKiemNghiemMau;
    private Long idKho;
    private String maDiemKho;
    private String diaDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenHangHoa;
    private String donViTinh;
    private Long idThuKho;
    private Long idKtvBaoQuan;
    private Long idLanhDaoChiCuc;
    private String keToanTruong;
    private LocalDate thoiGianGiaoNhan;
    private Long idBangKeHang;
    private String soBangKeHang;
    private String tenNguoiGiao;
    private String cmtNguoiGiao;
    private String congTyNguoiGiao;
    private String diaChiNguoiGiao;
    private String maSo;
    private BigDecimal theoChungTu;
    private BigDecimal thucXuat;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String ghiChu;
    private String trangThai;
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
    private String dvql;
    private LocalDate ngayLapPhieuTu;
    private LocalDate ngayLapPhieuDen;
    private String maDviCha;
}