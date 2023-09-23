package com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhKqBdgHdrReq extends BaseRequest {
    private Long id;
    private String maDvi;
    private Integer nam;
    private String soQdKq;
    private String trichYeu;
    private LocalDate ngayHieuLuc;
    private LocalDate ngayKy;
    private String loaiHinhNx;
    private String kieuNhapXuat;
    private Long idQdPd;
    private Long idQdPdDtl;
    private String soQdPd;
    private Long idQdDc;
    private String soQdDc;
    private Long idThongBao;
    private String maThongBao;
    private String soBienBan;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String phuongThucGiaoNhan;
    private Integer tgianGiaoNhanNgay;
    private String tgianGnhanGhiChu;
    private String ghiChu;
    private String hinhThucDauGia;
    private String phuongThucDauGia;
    private String soTbKhongThanh;
    private String trangThai;
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
    private String dvql;
    private LocalDate ngayKyTu;
    private LocalDate ngayKyDen;
    //Hợp đồng
    private Integer tongDviTsan;
    private Integer slDviTsanThanhCong;
    private Integer slDviTsanKhongThanh;
    private Integer slHopDongDaKy;
    private LocalDate thoiHanThanhToan;
    private Long tongSlXuat;
    private Long thanhTien;
    private String trangThaiHd;
    private String trangThaiXh;
}
