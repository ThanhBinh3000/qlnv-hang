package com.tcdt.qlnvhang.request.suachua;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuNhapKhoDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class ScPhieuNhapKhoReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maQhns;
    private String soPhieuNhapKho;
    private LocalDate ngayNhapKho;
    private Integer soNo;
    private Integer soCo;
    private String soQdNh;
    private Long idQdNh;
    private LocalDate ngayQdNh;
    private Long idScDanhSachHdr;
    private String maDiaDiem;
    private String loaiVthh;
    private String cloaiVthh;
    private String soPhieuKtcl;
    private String kyThuatVien;
    private String keToanTruong;
    private String nguoiGiaoHang;
    private String soCmt;
    private String dviNguoiGiaoHang;
    private String diaChi;
    private LocalDate thoiGianGiaoNhan;
    private String soBangKeCanHang;
    private BigDecimal tongSoLuong;
    private BigDecimal tongKinhPhiThucTe;
    private String ghiChu;
    private String trangThai;
    private String donViTinh;
    private List<FileDinhKemReq> fileDinhKemReq = new ArrayList<>();
    private List<ScPhieuNhapKhoDtl> children = new ArrayList<>();

    //Search

    private LocalDate ngayTu;
    private LocalDate ngayDen;

}
