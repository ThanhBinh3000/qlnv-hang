package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanTinhKhoDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbBienBanTinhKhoHdrReq {
    private Long id;
    private Integer nam;
    private String soBbTinhKho;

    private Long bangKeCanHangId;

    private String soBangKe;

    private LocalDate ngayLap;

    private String maDvi;

    private String tenDvi;

    private Long qhnsId;

    private String maQhns;

    private LocalDate ngayBatDauXuat;

    private LocalDate ngayKeThucXuat;

    private LocalDate thoiHanXuatHang;

    private Long qDinhDccId;

    private String soQdinhDcc;

    private LocalDate ngayXuatKho;

    private LocalDate thoiHanDieuChuyen;

    private LocalDate ngayKyQdDcc;

    private String loaiVthh;

    private String cloaiVthh;

    private String donViTinh;

    private String maDiemKho;

    private String tenDiemKho;

    private String diaDaDiemKho;

    private String maNhaKho;

    private String tenNhaKho;

    private String maNganKho;

    private String tenNganKho;

    private String maLoKho;

    private String tenLoKho;
    private String thuKho;

    private Long thuKhoId;

    private Boolean thayDoiThuKho;

    private String trangThai;

    private String lyDoTuChoi;

    private Long nguoiGDuyet;

    private LocalDate ngayGDuyet;

    private String loaiDc;

    private String nguyeNhan;

    private String kienNghi;

    private String ghiChu;

    private LocalDate ngayPduyetKtvBQ;

    private String ktvBaoQuan;

    private Long ktvBaoQuanId;

    private LocalDate ngayPduyetKt;

    private String keToan;

    private Long keToanId;

    private LocalDate ngayPduyetLdcc;

    private String lanhDaoChiCuc;

    private Long lanhDaoChiCucId;

    private BigDecimal tongSlXuatTheoQd;

    private BigDecimal tongSlXuatTheoTt;

    private BigDecimal slConLaiTheoSs;

    private BigDecimal slConLaiTheoTt;

    private BigDecimal chenhLechSlConLai;

    private String type;

    private List<FileDinhKemReq> fileBbTinhKhoDaKy = new ArrayList<>();

    private List<DcnbBienBanTinhKhoDtl> dcnbBienBanTinhKhoDtl = new ArrayList<>();
}
