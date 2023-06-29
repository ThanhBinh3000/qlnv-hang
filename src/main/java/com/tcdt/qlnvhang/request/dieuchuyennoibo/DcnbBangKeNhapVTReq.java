package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeNhapVTDtl;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class DcnbBangKeNhapVTReq extends BaseRequest {
    private Long id;
    private String loaiDc;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private Integer nam;
    private String loaiQdinh;
    private String soBangKe;
    private LocalDate ngayNhap;
    private String maDvi;
    private Long qhnsId;
    private String maQhns;
    private Long qDinhDccId;
    private String soQdinhDcc;
    private LocalDate ngayKyQdinhDcc;
    private String soHopDong;
    private Long phieuNhapKhoId;
    private String soPhieuNhapKho;
    private String maDiemKho;
    private String tenDiemKho;
    private String diaDaDiemKho;
    private String maNhaKho;
    private String tenNhaKho;
    private String maNganKho;
    private String tenNganKho;
    private String maLoKho;
    private String tenLoKho;
    private String maLanhDaoChiCuc;
    private String tenLanhDaoChiCuc;
    private Long thuKhoId;
    private String tenThuKho;
    private Long phuTrachId;
    private String tenPhuTrach;
    private String tenNguoiGiaoHang;
    private String cccd;
    private String donViNguoiGiaoHang;
    private String diaChiDonViNguoiGiaoHang;
    private LocalDate thoiHanGiaoNhan;
    private String donViTinh;
    private String trangThai;
    private String lyDoTuChoi;
    private Long nguoiGDuyet;
    private LocalDate ngayGDuyet;
    private Long nguoiPDuyetTvqt;
    private LocalDate ngayPDuyetTvqt;
    private Long nguoiPDuyet;
    private LocalDate ngayPDuyet;
    private String type;
    private String typeDataLink;
    private  Boolean thayDoiThuKho;
    private List<DcnbBangKeNhapVTDtl> dcnbbangkenhapvtdtl = new ArrayList<>();
}
