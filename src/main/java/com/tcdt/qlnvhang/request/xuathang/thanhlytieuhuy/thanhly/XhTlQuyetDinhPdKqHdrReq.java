package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.entities.FileDKemJoinTable;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhPdKqDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhTlQuyetDinhPdKqHdrReq extends BaseRequest {
    private Long id;
    private String maDvi;
    private Integer nam;
    private String soQd;
    private String trichYeu;
    private LocalDate ngayKy;
    private LocalDate ngayHieuLuc;
    private String loaiHinhNhapXuat;
    private String kieuNhapXuat;
    private Long idThongBao;
    private String maThongBao;
    private String soBienBan;
    private String thongBaoKhongThanh;
    private String loaiVthh;
    private String cloaiVthh;
    private String pthucGnhan;
    private String thoiHanGiaoNhan;
    private String thoiHanGiaoNhanGhiChu;
    private String ghiChu;
    private String trangThai;
    private String hthucDgia;
    private String pthucDgia;
    //  Hợp đồng
    private Long idQdTl;
    private String soQdTl;
    private Integer tongSoDviTsan;
    private Integer soDviTsanThanhCong;
    private BigDecimal tongSlXuatBan;
    private BigDecimal thanhTien;
    private String trangThaiHd;
    private String trangThaiXh;
    private List<XhTlQuyetDinhPdKqDtl> quyetDinhDtl = new ArrayList<>();
    private List<FileDKemJoinTable> fileDinhKem;
    private List<FileDKemJoinTable> canCu;
    private String dvql;
}
