package com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdGiaoNvuXuatReq extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String soQdNv;
    private LocalDate ngayKy;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private String toChucCaNhan;
    private String loaiHinhNx;
    private String kieuNhapXuat;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenHangHoa;
    private BigDecimal soLuong;
    private String donViTinh;
    private LocalDate tgianGiaoHang;
    private Long idCanBoPhong;
    private Long idTruongPhong;
    private Long idLanhDaoCuc;
    private String trichYeu;
    private Long idTinhKho;
    private String BienBanTinhKho;
    private Long idHaoDoi;
    private String BienBanHaoDoi;
    private String trangThai;
    private String trangThaiXh;
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
    private List<XhQdGiaoNvuXuatCtReq> children = new ArrayList<>();
    private String dvql;
    private LocalDate ngayKyTu;
    private LocalDate ngayKyDen;
    private String maDviCon;
}
