package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhTlToChucHdr.TABLE_NAME)
@Data
public class XhTlToChucHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_TO_CHUC_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlToChucHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlToChucHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = XhTlToChucHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private String maDvi;
    private Integer lanDauGia;
    private String maThongBao;
    private Long idQdTl;
    private Long idQdTlDtl;
    private String soQdTl;
    private String trichYeuTbao;
    private String tenToChuc;
    private String sdtToChuc;
    private String diaChiToChuc;
    private String taiKhoanToChuc;
    private String nganHangToChuc;
    private String chiNhanhNhangToChuc;
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
    private BigDecimal khoanTienDatTruoc;
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
    private Integer soDviTsan;
    private String thongBaoKhongThanh;
    private String trangThai;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate thoiHanThanhToan;
    private String pthucThanhtoanKhac;
    private LocalDate thoiHanGiaoNhan;
    private String pthucGnhan;

    //  @Transient
    @Transient
    private String tenDvi;
    @Transient
    private String tenTrangThai;
    @Transient
    private List<FileDinhKem> fileCanCu = new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileDinhKem = new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileDinhKemDaKy = new ArrayList<>();

    @OneToMany(mappedBy = "toChucHdr", cascade = CascadeType.ALL)
    private List<XhTlToChucDtl> toChucDtl = new ArrayList<>();

    @OneToMany(mappedBy = "toChucHdr", cascade = CascadeType.ALL)
    private List<XhTlToChucNlq> toChucNlq = new ArrayList<>();

}
