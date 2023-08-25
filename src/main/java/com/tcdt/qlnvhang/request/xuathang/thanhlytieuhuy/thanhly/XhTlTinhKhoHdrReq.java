package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.entities.FileDKemJoinTable;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlTinhKhoDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhTlTinhKhoHdrReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhNs;
    private String soBbTinhKho;
    private LocalDate ngayLapBienBan;
    private Long idBbQd;
    private String soBbQd;
    private LocalDate ngayKyBbQd;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private String maDiaDiem;
    private LocalDate ngayBatDauXuat;
    private LocalDate ngayKetThucXuat;
    private BigDecimal tongSlNhap;
    private BigDecimal tongSlXuat;
    private BigDecimal slConLai;
    private BigDecimal slThucTe;
    private BigDecimal slThua;
    private BigDecimal slThieu;
    private String nguyenNhan;
    private String kienNghi;
    private String ghiChu;
    private String donViTinh;
    private String trangThai;
    private String lyDoTuChoi;
    private String thuKho;
    private List<XhTlTinhKhoDtl> tinhKhoDtl = new ArrayList<>();
    private List<FileDKemJoinTable> fileDinhKem = new ArrayList<>();
    private String dvql;
}