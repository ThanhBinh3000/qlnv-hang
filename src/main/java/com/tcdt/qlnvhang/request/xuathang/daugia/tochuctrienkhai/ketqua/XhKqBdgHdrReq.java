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
    private LocalDate ngayHluc;
    private LocalDate ngayKy;
    private String loaiHinhNx;
    private String kieuNx;
    private Long idQdPd;
    private String soQdPd;
    private Long idThongTin;
    private Long idPdKhDtl;
    private String maThongBao;
    private String soBienBan;
    private String loaiVthh;
    private String cloaiVthh;
    private String pthucGnhan;
    private Integer tgianGnhan;
    private String tgianGnhanGhiChu;
    private String ghiChu;
    private String hinhThucDauGia;
    private String pthucDauGia;
    private String soTbKhongThanh;
    private String trangThai;
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
    private String dvql;
    private LocalDate ngayKyTu;
    private LocalDate ngayKyDen;
    private String trangThaiHd;
    //Hợp đồng
    private Long tongDvts;
    private Long soDvtsDgTc;
    private Long tongSlXuat;
    private Long thanhTien;
}
