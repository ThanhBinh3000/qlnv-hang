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
    private String maQhns;
    private String soPhieuXuatKho;
    private LocalDate ngayXuatKho;
    private Integer soNo;
    private Integer soCo;
    private String soQdXh;
    private Long idQdXh;
    private LocalDate ngayQdXh;
    private Long idScDanhSachHdr;
    private String maDiaDiem;
    private String loaiVthh;
    private String cloaiVthh;
    private String keToanTruong;
    private String nguoiGiaoHang;
    private String soCmt;
    private String dviNguoiGiaoHang;
    private String diaChi;
    private LocalDate thoiGianGiaoNhan;
    private BigDecimal tongSoLuong;
    private String ghiChu;
    private String donViTinh;

    List<FileDinhKemReq> fileDinhKemReq = new ArrayList<>();
    private List<ScPhieuXuatKhoDtl> children = new ArrayList<>();

    //Region search
    private LocalDate ngayTu;
    private LocalDate ngayDen;
    private String maDviSr;


}
