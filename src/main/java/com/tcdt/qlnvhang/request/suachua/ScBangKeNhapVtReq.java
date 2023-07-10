package com.tcdt.qlnvhang.request.suachua;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeNhapVtDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class ScBangKeNhapVtReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private Long qhnsId;
    private String maQhns;
    private String soBangKe;
    private LocalDate ngayNhapBangKe;
    private Long qdGiaoNvNhapId;
    private String soQdGiaoNvNhap;
    private LocalDate ngayKyQdGiaoNvNhap;
    private Long phieuNhapKhoId;
    private String soPhieuNhapKho;
    private String maLoKho;
    private String tenLoKho;
    private String maNganKho;
    private String tenNganKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String diaDiemKho;
    private LocalDate thoiGianGiaoNhanHang;
    private String hoTenNguoiGiao;
    private String cmt;
    private String dviNguoiGiao;
    private String dchiDviNguoiGiao;
    private String maLoaiHang;
    private String tenLoaiHang;
    private String maChungLoaiHang;
    private String tenChungLoaiHang;
    private String dviTinh;
    private Long ldaoChiCucId;
    private String tenLdaoChiCuc;
    private Long idThuKho;
    private String tenThuKho;
    private String trangThai;
    private List<ScBangKeNhapVtDtl> scBangKeNhapVtDtls = new ArrayList<>();
}
