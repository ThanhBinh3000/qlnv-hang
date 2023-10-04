package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlTinhKhoDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhTlTinhKhoReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maQhns;
    private String soBbTinhKho;
    private LocalDate ngayLapBienBan;
    private Long idQdXh;
    private String soQdXh;
    private LocalDate ngayQdXh;
    private String maDiaDiem;
    private Long idDsHdr;
    private String loaiVthh;
    private String cloaiVthh;
    private LocalDate ngayBatDauXuat;
    private LocalDate ngayKetThucXuat;
    private Long idPhieuKtcl;
    private String soPhieuKtcl;
    private BigDecimal slQuyetDinh;
    private BigDecimal slThucTe;
    private BigDecimal slConLai;
    private BigDecimal slTteConLaiKhiXk;
    private BigDecimal slThua;
    private BigDecimal slThieu;
    private String nguyenNhan;
    private String kienNghi;
    private String ghiChu;
    private String donViTinh;
    private String trangThai;
    private String lyDoTuChoi;

    private List<XhTlTinhKhoDtl> children = new ArrayList<>();
    private List<FileDinhKemReq> fileDinhKemReq = new ArrayList<>();

    private String phanLoai;
    private String maDviSr;
}