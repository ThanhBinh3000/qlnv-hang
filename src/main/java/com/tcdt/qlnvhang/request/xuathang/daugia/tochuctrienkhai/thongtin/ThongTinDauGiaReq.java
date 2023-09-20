package com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class ThongTinDauGiaReq extends BaseRequest {
    private Long id;
    private String maDvi;
    private Integer nam;
    private Integer lanDauGia;
    private String maThongBao;
    private Long idQdPd;
    private String soQdPd;
    private Long idQdPdDtl;
    private String trichYeuTbao;
    private String tenToChuc;
    private String sdtToChuc;
    private String diaChiToChuc;
    private String taiKhoanToChuc;
    private String soHd;
    private LocalDate ngayKyHd;
    private String hthucTchuc;
    private LocalDate tgianDkyTu;
    private LocalDate tgianDkyDen;
    private String ghiChuTgianDky;
    private String diaDiemDky;
    private String dieuKienDky;
    private String tienMuaHoSo;
    private String buocGia;
    private String ghiChuBuocGia;
    private LocalDate tgianXemTu;
    private LocalDate tgianXemDen;
    private String ghiChuTgianXem;
    private String diaDiemXem;
    private LocalDate tgianNopTienTu;
    private LocalDate tgianNopTienDen;
    private String pthucTtoan;
    private String ghiChuTgianNopTien;
    private String donViThuHuong;
    private String stkThuHuong;
    private String nganHangThuHuong;
    private String chiNhanhNganHang;
    private LocalDate tgianDauGiaTu;
    private LocalDate tgianDauGiaDen;
    private String diaDiemDauGia;
    private String hthucDgia;
    private String pthucDgia;
    private String dkienCthuc;
    private String ghiChu;
    private Integer ketQua; // 0 : Trượt 1 Trúng
    private String soBienBan;
    private String trichYeuBban;
    private LocalDate ngayKyBban;
    private String ketQuaSl;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private BigDecimal khoanTienDatTruoc;
    private String thongBaoKhongThanh;
    private Integer soDviTsan;
    private String trangThai;
    private Long idQdDc;
    private String soQdDc;
    private List<FileDinhKemJoinTable> canCu;
    private List<FileDinhKemJoinTable> fileDinhKem;
    private List<ThongTinDauGiaDtlReq> children = new ArrayList<>();
    private List<ThongTinDauGiaNtgReq> listNguoiTgia = new ArrayList<>();
    private String dvql;
}
