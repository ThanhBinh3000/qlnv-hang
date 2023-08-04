package com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.dexuat;

import com.tcdt.qlnvhang.entities.FileDKemJoinHoSoKyThuatDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaDtl;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhDxKhBanDauGiaReq extends BaseRequest {
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
    private Integer tgianTtoan;
    private Integer tgianGnhan;
    private String pthucTtoan;
    private String pthucGnhan;
    private String tgianTtoanGhiChu;
    private String tgianGnhanGhiChu;
    private String thongBao;
    private BigDecimal khoanTienDatTruoc;
    private BigDecimal tongSoLuong;
    private BigDecimal tongTienKhoiDiemDx;
    private BigDecimal tongTienDatTruocDx;
    private String ghiChu;
    private Integer slDviTsan;
    private String trangThaiTh;
    private Long idSoQdPd;
    private String soQdPd;
    private Long idThop;
    private LocalDate ngayKyQd;
    private String donViTinh;
    private LocalDate ngayTao;
    private LocalDate ngayPduyet;
    private List<String> trangThaiList = new ArrayList<>();
    private List<FileDKemJoinHoSoKyThuatDtl> fileDinhKem = new ArrayList<>();
    private List<XhDxKhBanDauGiaDtl> children = new ArrayList<>();
    private LocalDate ngayTaoTu;
    private LocalDate ngayTaoDen;
    private LocalDate ngayDuyetTu;
    private LocalDate ngayDuyetDen;
    private String dvql;
}
