package com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.bienbanlaymau;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhBbLayMauBttHdrReq extends BaseRequest {
    private Long id;
    private Integer namKh;
    private String maDvi;
    private String loaiBienBan;
    private String maQhNs;
    private String soBbLayMau;
    private LocalDate ngayLayMau;
    private Long idQdNv;
    private String soQdNv;
    private LocalDate ngayKyQdNv;
    private Long idQdNvDtl;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private Long idBangKeBanLe;
    private String soBangKeBanLe;
    private LocalDate ngayTaoBkeBanLe;
    private Long idThuKho;
    private Long idKtvBaoQuan;
    private Long idLanhDaoChiCuc;
    private Long idKho;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private BigDecimal soLuong;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenHangHoa;
    private String donViTinh;
    private LocalDate tgianGiaoNhan;
    private String donViKnghiem;
    private String diaDiemLayMau;
    private BigDecimal soLuongKiemTra;
    private Boolean ketQuaNiemPhong;
    private String pthucBanTrucTiep; // 01 : chào giá; 02 : Ủy quyền; 03 : Bán lẻ
    private String phanLoai;
    private String loaiHinhNx;
    private String kieuNx;
    private Long idHaoDoi;
    private String soBbHaoDoi;
    private Long idTinhKho;
    private String soBbTinhKho;
    private LocalDate ngayXuatDocKho;
    private String tchuanCluong;
    private String trangThai;
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
    private List<FileDinhKemJoinTable> fileNiemPhong = new ArrayList<>();
    private List<XhBbLayMauBttDtlReq> children = new ArrayList<>();
    private String dvql;
    private LocalDate ngayLayMauTu;
    private LocalDate ngayLayMauDen;
    private String maDviCha;
}