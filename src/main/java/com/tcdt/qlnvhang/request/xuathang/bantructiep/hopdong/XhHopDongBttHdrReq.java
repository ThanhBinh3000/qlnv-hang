package com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhHopDongBttHdrReq extends BaseRequest {

    private Long id;

    private String maDvi;

    private Integer namHd;

    private Long idQdKq;

    private String soQdKq;

    private LocalDate ngayKyQdKq;

    private Long idQdNv;

    private String soQdNv;

    private LocalDate thoiHanXuatKho;

    private Long idQdPd;

    private String soQdPd;

    private Long idQdPdDtl;

    private LocalDate ngayKyQdPd;

    private String tenDviMua;

    private String maDviTsan;

    private String loaiHinhNx;

    private String kieuNx;

    private String soHd;

    private String tenHd;

    private LocalDate ngayHluc;

    private String ghiChuNgayHluc;

    private String loaiHdong;

    private String ghiChuLoaiHdong;

    private Integer tgianThienHd;

    private LocalDate tgianGnhanTu;

    private LocalDate tgianGnhanDen;

    private String ghiChuTgianGnhan;

    private String noiDungHdong;

    private String dkienHanTtoan;

    private String diaChiDvi;

    private String mst;

    private String tenNguoiDdien;

    private String chucVu;

    private String sdt;

    private String fax;

    private String stk;

    private String moTai;

    private String ttinGiayUyQuyen;

    private Long idDviMua;

    private String diaChiDviMua;

    private String mstDviMua;

    private String tenNguoiDdienDviMua;

    private String chucVuDviMua;

    private String sdtDviMua;

    private String faxDviMua;

    private String stkDviMua;

    private String moTaiDviMua;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String donViTinh;

    private BigDecimal soLuongBanTrucTiep;

    private BigDecimal donGiaBanTrucTiep;

    private BigDecimal thanhTien;

    private String ghiChu;

    private BigDecimal tongSlXuatBanQdKh;

    private BigDecimal tongSlBanttQdkhDakyHd;

    private BigDecimal tongSlBanttQdkhChuakyHd;

    private String trangThaiXh;

    private String trangThai;

    private LocalDate ngayTao;

    private Long nguoiTaoId;

    private LocalDate ngaySua;

    private Long nguoiSuaId;

    private LocalDate ngayGuiDuyet;

    private Long nguoiGuiDuyetId;

    private LocalDate ngayPduyet;

    private Long nguoiPduyetId;

    private String lyDoTuChoi;

    @Transient
    private List<String> listMaDviTsan = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> canCuPhapLy = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();


//    Cấp cục
    @Transient
    private List<XhHopDongBttDtlReq> children = new ArrayList<>();


//    Cấp chi cục
    @Transient
    private List<XhHopDongBttDviReq> xhHopDongBttDviList = new ArrayList<>();


//    Phụ lục
    private Long idHd;

    private String soPhuLuc;

    private LocalDate ngayHlucPhuLuc;

    private String noiDungPhuLuc;

    private LocalDate ngayHlucSauDcTu;

    private LocalDate ngayHlucSauDcDen;

    private Integer tgianThienHdSauDc;

    private String noiDungDcKhac;

    private String ghiChuPhuLuc;

    @Transient
    private List<XhHopDongBttHdrReq> phuLuc = new ArrayList<>();

    @Transient
    private List<XhHopDongBttDtlReq> phuLucDtl = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> filePhuLuc = new ArrayList<>();
}
