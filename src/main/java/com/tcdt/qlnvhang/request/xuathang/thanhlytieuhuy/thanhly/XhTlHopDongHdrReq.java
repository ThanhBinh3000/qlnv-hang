package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlHopDongDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhTlHopDongHdrReq extends BaseRequest {
    private Long id;
    private String dvql;
    private Integer nam;
    private String maDvi;
    private Long idQdKqTl;
    private String soQdKqTl;
    private LocalDate ngayKyQdkqTl;
    private String soQdTl;
    private String toChucCaNhan;
    private String maDviTsan;
    private LocalDate thoiHanXuatKho;
    private String loaiHinhNx;
    private String kieuNx;
    private String soHd;
    private String tenHd;
    private LocalDate ngayHieuLuc;
    private String ghiChuNgayHluc;
    private String loaiHdong;
    private String ghiChuLoaiHdong;
    private Integer tgianThienHd;
    private Integer tgianBhanh;
    private String diaChiBenBan;
    private String mstBenBan;
    private String daiDienBenBan;
    private String chucVuBenBan;
    private String sdtBenBan;
    private String faxBenBan;
    private String stkBenBan;
    private String moTaiBenBan;
    private String thongTinUyQuyen;
    private String tenDviBenMua;
    private String diaChiBenMua;
    private String mstBenMua;
    private String daiDienBenMua;
    private String chucVuBenMua;
    private String sdtBenMua;
    private String faxBenMua;
    private String stkBenMua;
    private String moTaiBenMua;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String donViTinh;
    private BigDecimal soLuong;
    private BigDecimal thanhTien;
    private String ghiChu;
    private String trangThai;
    private String trangThaiXh;
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
    private List<XhTlHopDongDtl> children = new ArrayList<>();
}
