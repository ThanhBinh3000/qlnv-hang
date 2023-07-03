package com.tcdt.qlnvhang.request.suachua;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuXuatKhoDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Data
public class ScPhieuXuatKhoReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhns;
    private String soPhieuXuatKho;
    private LocalDate ngayTaoPhieu;
    private LocalDate NgayXuatKho;
    private Integer no;
    private String co;
    private String soQdGiaoNv;
    private LocalDate ngayKyQdGiaoNv;
    private String maLoKho;
    private String tenLoKho;
    private String maNganKho;
    private String tenNganKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String soPhieuKiemDinhChatLuong;
    private String loaiHang;
    private String chungLoaiHang;
    private String thuKho;
    private String lanhDaoCc;
    private String ktvBaoQuan;
    private String keToanTruong;
    private String nguoiGiaoHang;
    private String soCmt;
    private String dviNguoiGiaoHang;
    private String diaChi;
    private LocalDate thoiGianGiaoNhan;
    private String soBangKeCanHang;
    private String maSo;
    private String donViTinh;
    private BigDecimal soLuongXuatThucTe;
    private String tongSoLuong;
    private String tongSoTien;
    private String ghiChu;
    private String trangThai;
    private String ngayXuatKhoTu;
    private String ngayXuatKhoDen;

    List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    private List<ScPhieuXuatKhoDtl> children = new ArrayList<>();
}
