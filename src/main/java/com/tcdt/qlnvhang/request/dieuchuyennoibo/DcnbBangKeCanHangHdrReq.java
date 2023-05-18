package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeCanHangDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbQuyetDinhDcCDtl;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DcnbBangKeCanHangHdrReq {
    private Long id;

    private Integer nam;

    private String soBangKe;

    private LocalDate ngayNhap;

    private String maDvi;

    private Long qhnsId;

    private String maQhns;

    private Long qDinhDccId;

    private String soQdinhDcc;

    private LocalDate thoiHanDieuChuyen;

    private LocalDate ngayKyQdDcc;

    private Long phieuXuatKhoId;

    private String soPhieuXuatKho;

    private String loaiHang;

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

    private String type;

    private String maLanhDaoChiCuc;

    private String tenLanhDaoChiCuc;

    private Long thuKhoId;

    private String tenThuKho;

    private String tenNguoiGiaoHang;

    private String cccd;

    private String donViNguoiGiaoHang;

    private String diaChiDonViNguoiGiaoHang;

    private BigDecimal tongTrongLuongBaoBi;

    private BigDecimal tongTrongLuongCabaoBi;

    private BigDecimal tongTrongLuongTruBi;

    private String tongTrongLuongTruBiText;

    private List<DcnbBangKeCanHangDtl> dcnbBangKeCanHangDtl = new ArrayList<>();
}
