package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanTinhKhoDtl;
import lombok.Data;

import javax.persistence.Column;
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

    private LocalDate ngayNhap;

    private String maDvi;

    private Long qhnsId;

    private String maQhns;

    private Long qDinhDccId;

    private String soQdinhDcc;

    private LocalDate ngayXuatKho;

    private LocalDate thoiHanDieuChuyen;

    private LocalDate ngayKyQdDcc;

    private Long phieuXuatKhoId;

    private String soPhieuXuatKho;

    private String soBbLayMau;

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

    private Boolean thayDoiThuKho;

    private String trangThai;

    private String lyDoTuChoi;

    private Long nguoiGDuyet;

    private LocalDate ngayGDuyet;

    private Long nguoiPDuyet;

    private LocalDate ngayPDuyet;

    private String loaiDc;

    private String nguyeNhan;

    private String kienNghi;

    private String ghiChu;

    private String ktvBaoQuan;

    private Long ktvBaoQuanId;

    private String keToan;

    private Long keToanId;

    private String lanhDaoChiCuc;

    private Long lanhDaoChiCucId;

    private String type;

    private List<FileDinhKemReq> fileBbTinhKhoDaKy = new ArrayList<>();

    private List<DcnbBienBanTinhKhoDtl> dcnbBienBanTinhKhoDtl = new ArrayList<>();
}
