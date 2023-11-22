package com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.dexuat;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepDtl;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhDxKhBanTrucTiepHdrReq extends BaseRequest {
    private Long id;
    private String maDvi;
    private String loaiHinhNx;
    private String kieuNx;
    private String diaChi;
    private Integer namKh;
    private String soDxuat;
    private String trichYeu;
    private Long idSoQdCtieu;
    private String soQdCtieu;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String tchuanCluong;
    private LocalDate tgianDkienTu;
    private LocalDate tgianDkienDen;
    private String ghiChuTgianDkien;
    private Integer tgianTtoan;
    private String tgianTtoanGhiChu;
    private String pthucTtoan;
    private Integer tgianGnhan;
    private String tgianGnhanGhiChu;
    private String pthucGnhan;
    private String thongBao;
    private BigDecimal tongSoLuong;
    private String ghiChu;
    private Long idSoQdPd;
    private String soQdPd;
    private LocalDate ngayKyQd;
    private Long idThop;
    private String trangThaiTh;
    private Integer slDviTsan;
    private String donViTinh;
    private String trangThai;
    private LocalDate ngayTao;
    private LocalDate ngayPduyet;
    private BigDecimal thanhTien;
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
    private List<XhDxKhBanTrucTiepDtl> children = new ArrayList<>();
    private String dvql;
    private LocalDate ngayTaoTu;
    private LocalDate ngayTaoDen;
    private LocalDate ngayDuyetTu;
    private LocalDate ngayDuyetDen;
    private LocalDate ngayKyQdTu;
    private LocalDate ngayKyQdDen;
}