package com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdPdKhBttDtlReq {
    private Long id;
    private Long idHdr;
    private String maDvi;
    private String diaChi;
    private Long idDxHdr;
    private String soDxuat;
    private LocalDate ngayTao;
    private LocalDate ngayPduyet;
    private String trichYeu;
    private Integer slDviTsan;
    private BigDecimal tongSoLuong;
    private BigDecimal thanhTien;
    private BigDecimal thanhTienDuocDuyet;
    private String donViTinh;
    private LocalDate tgianDkienTu;
    private LocalDate tgianDkienDen;
    private Integer tgianTtoan;
    private String tgianTtoanGhiChu;
    private String pthucTtoan;
    private Integer tgianGnhan;
    private String tgianGnhanGhiChu;
    private String pthucGnhan;
    private String thongBao;
    private List<XhQdPdKhBttDviReq> children;

    // thông tin chào giá
    private Long idDtl;
    private String soQdPd;
    private String soQdDc;
    private String pthucBanTrucTiep;
    private LocalDate ngayNhanCgia;
    private Long idQdKq;
    private String soQdKq;
    private String diaDiemChaoGia;
    private String loaiHinhNx;
    private String kieuNx;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String ghiChuChaoGia;
    private String trangThai;
    private Long idQdNv;
    private String soQdNv;
    private String trangThaiHd;
    private String trangThaiXh;
    private Integer slHdDaKy;
    private Integer slHdChuaKy;
    private BigDecimal tongSlDaKyHdong;
    private BigDecimal tongSlChuaKyHdong;
    private List<FileDinhKemReq> fileDinhKem = new ArrayList<>();
}