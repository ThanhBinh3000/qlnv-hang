package com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhPhieuKnghiemCluongReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhNs;
    private String soPhieuKiemNghiem;
    private LocalDate ngayLapPhieu;
    private Long idQdNv;
    private String soQdNv;
    private LocalDate ngayKyQdNv;
    private Long idQdNvDtl;
    private LocalDate tgianGiaoHang;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private Long idBbLayMau;
    private String soBbLayMau;
    private LocalDate ngayLayMau;
    private Long idKho;
    private String maDiemKho;
    private String diaDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private BigDecimal soLuong;
    private String loaiHinhNx;
    private String kieuNhapXuat;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenHangHoa;
    private String donViTinh;
    private String hinhThucBaoQuan;
    private LocalDate ngayKiemNghiemMau;
    private Long idNguoiKiemNghiem;
    private Long idThuKho;
    private Long idTruongPhongKtvbq;
    private String ketQua;
    private String nhanXet;
    private Long idTinhKho;
    private String soBbTinhKho;
    private LocalDate ngayLapTinhKho;
    private String trangThai;
    private String maDviCon;
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
    private List<XhPhieuKnghiemCluongCtReq> children = new ArrayList<>();
    private String dvql;
    private LocalDate ngayKiemNghiemMauTu;
    private LocalDate ngayKiemNghiemMauDen;
}