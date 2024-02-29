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
    private String loaiHinhNx;
    private String kieuNx;
    private Long idQdKq;
    private String soQdKq;
    private LocalDate ngayKyQdKq;
    private LocalDate thoiHanXuatKho;
    private Long idQdPd;
    private String soQdPd;
    private LocalDate ngayKyUyQuyen;
    private String maDviTsan;
    private String soHopDong;
    private String tenHopDong;
    private LocalDate ngayKyHopDong;
    private LocalDate ngayHlucHopDong;
    private String ghiChuNgayHluc;
    private String loaiHopDong;
    private String ghiChuLoaiHdong;
    private Integer tgianThienHdongNgay;
    private LocalDate tgianThienHdong;
    private LocalDate tgianGiaoNhanTu;
    private LocalDate tgianGiaoNhanDen;
    private String ghiChuTgianGiaoNhan;
    private Integer thoiGianBaoHanh;
    private BigDecimal giaTri;
    private LocalDate tgianBaoDamHdong;
    private String ghiChuBaoDam;
    private String dieuKien;
    private String diaChiBenBan;
    private String mstBenBan;
    private String tenNguoiDaiDien;
    private String chucVuBenBan;
    private String sdtBenBan;
    private String faxBenBan;
    private String stkBenBan;
    private String moTaiBenBan;
    private String giayUyQuyen;
    private Long idBenMua;
    private String tenBenMua;
    private String diaChiBenMua;
    private String mstBenMua;
    private String tenNguoiDdienMua;
    private String chucVuBenMua;
    private String sdtBenMua;
    private String faxBenMua;
    private String stkBenMua;
    private String moTaiBenMua;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenHangHoa;
    private String donViTinh;
    private BigDecimal soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private BigDecimal slXuatBanQdPd;
    private BigDecimal slXuatBanKyHdong;
    private BigDecimal slXuatBanChuaKyHdong;
    private String moTaHangHoa;
    private String ghiChu;
    private Long idChaoGia;
    private Long idQdNv;
    private String soQdNv;
    private String trangThai;
    private String trangThaiXh;
    private Long idQdDc;
    private String soQdDc;
    private String phanLoai;
    private Long idKh;
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