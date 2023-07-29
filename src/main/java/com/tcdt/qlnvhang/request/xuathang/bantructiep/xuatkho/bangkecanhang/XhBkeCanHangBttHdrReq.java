package com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bangkecanhang;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhBkeCanHangBttHdrReq extends BaseRequest {

    private Long id;

    private Integer namKh;

    private String maDvi;

    private String maQhns;

    private String soBangKe;

    private Long idQdNv;

    private String soQdNv;

    private LocalDate ngayQdNv;

    private Long idHd;

    private String soHd;

    private LocalDate ngayKyHd;

    private Long idDdiemXh;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private Long idPhieuXuat;

    private String soPhieuXuat;

    private LocalDate ngayXuatKho;

    private String diaDiemKho;

    private Long idThuKho;

    private String nguoiGiao;

    private String cmtNguoiGiao;

    private String ctyNguoiGiao;

    private String diaChiNguoiGiao;

    private LocalDate tgianGiaoNhan;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String donViTinh;

    private LocalDate ngayXuatKhoTu;

    private LocalDate ngayXuatKhoDen;

    private String phanLoai;

    private String pthucBanTrucTiep;

    private Long idBangKeBanLe;

    private String soBangKeBanLe;

    private LocalDate ngayTaoBangKe;

    @Transient
    private List<XhBkeCanHangBttDtlReq> children = new ArrayList<>();
}
