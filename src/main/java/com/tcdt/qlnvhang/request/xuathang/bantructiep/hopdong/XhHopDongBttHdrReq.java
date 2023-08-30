package com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

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
    private LocalDate thoiHanXuatKho;
    private Long idQdPd;
    private String soQdPd;
    private LocalDate NgayKyUyQuyen;
    private String tenDviMua;
    private String maDviTsan;
    private String loaiHinhNx;
    private String kieuNx;
    private String soHd;
    private String tenHd;
    private LocalDate ngayHluc; //Ngày ký hợp đồng
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
    private String trangThai;
    private String trangThaiXh;
    private Long idQdNv;
    private String soQdNv;
    private Long idChaoGia;
    private List<String> listMaDviTsan = new ArrayList<>();
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
    //Cấp cục
    private List<XhHopDongBttDtlReq> children = new ArrayList<>();
    //Cấp chi cục
    private List<XhHopDongBttDviReq> xhHopDongBttDviList = new ArrayList<>();

    //Phụ lục
    private Long idHd;
    private String soPhuLuc;
    private LocalDate ngayHlucPhuLuc;
    private String noiDungPhuLuc;
    private LocalDate ngayHlucSauDcTu;
    private LocalDate ngayHlucSauDcDen;
    private Integer tgianThienHdSauDc;
    private String noiDungDcKhac;
    private String ghiChuPhuLuc;
    private List<FileDinhKemJoinTable> filePhuLuc = new ArrayList<>();
    private List<XhHopDongBttHdrReq> phuLuc = new ArrayList<>();
    private List<XhHopDongBttDtlReq> phuLucDtl = new ArrayList<>();
    private String dvql;
}
