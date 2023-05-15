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

    private Integer namHd;

    private Long idQdKq;

    private String soQdKq;

    private LocalDate ngayKyQd;

    private LocalDate ngayMkho;

    private LocalDate ngayKyQdPd;

    private String soQdPd;

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

    private String maDvi;

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

    private String tenDviMua;

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

    private BigDecimal thanhTien;

    private String ghiChu;

    private BigDecimal tongSlXuatBanQdKh;

    private BigDecimal tongSlBanttQdkhDakyHd;

    private BigDecimal tongSlBanttQdkhChuakyHd;

    private String trangThaiXh;

    @Transient
    private List<String> listMaDviTsan = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> canCuPhapLy = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();

    @Transient
    private List<FileDinhKemReq> filePhuLuc = new ArrayList<>();

    @Transient
    private List<XhHopDongBttDtlReq> children = new ArrayList<>();

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

    private String trangThaiPhuLuc;

    @Transient
    private List<XhHopDongBttHdrReq> phuLuc = new ArrayList<>();

    @Transient
    private List<XhHopDongBttDtlReq> phuLucDtl = new ArrayList<>();

    private BigDecimal donGiaBanTrucTiep;

    private String soQdNv;

    private Long idQdNv;

//    cấp chi cục
   @Transient
   private List<XhHopDongBttDviReq> xhHopDongBttDviList = new ArrayList<>();
}
