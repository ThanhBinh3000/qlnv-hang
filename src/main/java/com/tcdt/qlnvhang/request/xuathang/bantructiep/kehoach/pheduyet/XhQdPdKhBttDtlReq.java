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
    private Long IdDtl;
    private String pthucBanTrucTiep;
    private LocalDate ngayNhanCgia;
    private String diaDiemChaoGia;
    private String loaiHinhNx;
    private String kieuNx;
    private LocalDate ngayMkho;
    private LocalDate ngayKthuc;
    private String ghiChuChaoGia;
    private BigDecimal tongGiaTriHdong;
    private LocalDate thoiHanBan;
    private Integer lastest;
    private Integer isDieuChinh;
    private String soQdDc;
    private List<FileDinhKemReq> fileUyQuyen = new ArrayList<>();
    private List<FileDinhKemReq> fileBanLe = new ArrayList<>();
}
