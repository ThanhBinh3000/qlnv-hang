package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlBangKeDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhTlBangKeHdrReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhNs;
    private String soBangKe;
    private Long idBbQd;
    private String soBbQd;
    private LocalDate ngayKyBbQd;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private String maDiaDiem;
    private Long idPhieuXuatKho;
    private String soPhieuXuatKho;
    private LocalDate ngayXuatKho;
    private String diaDiemKho;
    private String thuKho;
    private String tenNguoiGiao;
    private String cmtNguoiGiao;
    private String congTyNguoiGiao;
    private String diaChiNguoiGiao;
    private LocalDate thoiGianGiaoNhan;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenVthh;
    private String donViTinh;
    private BigDecimal soLuong;
    private BigDecimal tongTrongLuongBaoBi;
    private String trangThai;
    private String lyDoTuChoi;
    private List<XhTlBangKeDtl> bangKeDtl = new ArrayList<>();
    private String dvql;
    private LocalDate ngayXuatKhoTu;
    private LocalDate ngayXuatKhoDen;
}