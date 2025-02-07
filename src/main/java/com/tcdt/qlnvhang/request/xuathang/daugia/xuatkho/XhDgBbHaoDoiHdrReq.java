package com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBbHaoDoiDtl;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhDgBbHaoDoiHdrReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhNs;
    private String soBbHaoDoi;
    private LocalDate ngayLapBienBan;
    private Long idQdNv;
    private String soQdNv;
    private LocalDate ngayKyQdNv;
    private LocalDate thoiGianGiaoNhan;
    private Long idQdNvDtl;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private Long idBbTinhKho;
    private String soBbTinhKho;
    private LocalDate ngayLapBbTinhKho;
    private Long idKho;
    private String maDiemKho;
    private String diaDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String loaiHinhKho;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenHangHoa;
    private String donViTinh;
    private LocalDate ngayBatDauXuat;
    private LocalDate ngayKetThucXuat;
    private Long idThuKho;
    private Long idKtvBaoQuan;
    private Long idKeToan;
    private Long idLanhDaoChiCuc;
    private BigDecimal tongSlNhap;
    private LocalDate thoiGianKthucNhap;
    private BigDecimal tongSlXuat;
    private LocalDate thoiGianKthucXuat;
    private BigDecimal slHaoThucTe;
    private BigDecimal tileHaoThucTe;
    private BigDecimal slHaoVuotMuc;
    private BigDecimal tileHaoVuotMuc;
    private BigDecimal slHaoThanhLy;
    private BigDecimal tileHaoThanhLy;
    private BigDecimal slHaoDuoiMuc;
    private BigDecimal tileHaoDuoiMuc;
    private BigDecimal dinhMucHaoHut;
    private BigDecimal slHaoTheoDinhMuc;
    private String nguyenNhan;
    private String kienNghi;
    private String ghiChu;
    private Long idPhieuKiemNghiem;
    private String soPhieuKiemNghiem;
    private LocalDate ngayKiemNghiemMau;
    private String trangThai;
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
    private List<XhDgBbHaoDoiDtl> children = new ArrayList<>();
    private String dvql;
    private String maDviCha;
    private LocalDate ngayLapBienBanTu;
    private LocalDate ngayLapBienBanDen;
}