package com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhQdPdKhBttDtl.TABLE_NAME)
@Data
public class XhQdPdKhBttDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BTT_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhQdPdKhBttDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhQdPdKhBttDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhQdPdKhBttDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private Integer namKh;
    private String maDvi;
    private String diaChi;
    private Long idDxHdr;
    private String soDxuat;
    private LocalDate ngayTao;
    private LocalDate ngayPduyet;
    private String trichYeu;
    private Integer slDviTsan;
    private BigDecimal tongSoLuong;
    private BigDecimal thanhTien;
    private BigDecimal thanhTienDuocDuyet;
    private String donViTinh;
    private LocalDate tgianDkienTu;
    private LocalDate tgianDkienDen;
    private Integer tgianTtoan;
    private String tgianTtoanGhiChu;
    private String pthucTtoan;
    private Integer tgianGnhan;
    private String tgianGnhanGhiChu;
    private String pthucGnhan;
    private String thongBao;
    @Transient
    private String tenDvi;
    @Transient
    private List<XhQdPdKhBttDvi> children = new ArrayList<>();

    // thông tin chào giá
    private String soQdPd;
    private String soQdDc;
    private String pthucBanTrucTiep;
    private LocalDate ngayNhanCgia;
    private Long idQdKq;
    private String soQdKq;
    private String diaDiemChaoGia;
    private String loaiHinhNx;
    private String kieuNx;
    private LocalDate ngayMkho;
    private LocalDate ngayKthuc;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String ghiChuChaoGia;
    private LocalDate thoiHanBan;
    private String trangThai;
    private Long idQdNv;
    private String soQdNv;
    private String trangThaiHd;
    private String trangThaiXh;
    private Integer slHdDaKy;
    private Integer slHdChuaKy;
    private BigDecimal tongSlDaKyHdong;
    private BigDecimal tongSlChuaKyHdong;
    @Transient
    private String tenLoaiHinhNx;
    @Transient
    private String tenKieuNx;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenTrangThaiXh;
    @Transient
    private String tenTrangThaiHd;
    @Transient
    private String tenPthucTtoan;
    @Transient
    private XhQdPdKhBttHdr xhQdPdKhBttHdr;
    @Transient
    private List<FileDinhKem> fileUyQuyen = new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileBanLe = new ArrayList<>();
    @Transient
    private List<XhHopDongBttHdr> listHopDongBtt;
}