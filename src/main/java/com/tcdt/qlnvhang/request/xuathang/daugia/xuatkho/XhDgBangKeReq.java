package com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBangKeDtl;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhDgBangKeReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhNs;
    private String soBangKeHang;
    private LocalDate ngayLapBangKe;
    private Long idQdNv;
    private String soQdNv;
    private LocalDate ngayKyQdNv;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private String maDiemKho;
    private String diaDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String loaiHinhKho;
    private String nguoiGiamSat;
    private Long idThuKho;
    private Long idLanhDaoChiCuc;
    private Long idPhieuXuatKho;
    private String soPhieuXuatKho;
    private Long idPhieuKiemNghiem;
    private String soPhieuKiemNghiem;
    private LocalDate ngayKiemNghiemMau;
    private String tenNguoiGiao;
    private String cmtNguoiGiao;
    private String congTyNguoiGiao;
    private String diaChiNguoiGiao;
    private LocalDate thoiGianGiaoNhan;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenHangHoa;
    private String donViTinh;
    private BigDecimal tongTrongLuongBi;
    private BigDecimal tongTrongLuongCaBi;
    private BigDecimal tongTrongTruBi;
    private String loaiHinhNx;
    private String kieuNhapXuat;
    private BigDecimal soLuong;
    private BigDecimal donGia;
    private String trangThai;
    private List<XhDgBangKeDtl> children = new ArrayList<>();
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
    private String dvql;
    private LocalDate ngayLapBangKeTu;
    private LocalDate ngayLapBangKeDen;
}
