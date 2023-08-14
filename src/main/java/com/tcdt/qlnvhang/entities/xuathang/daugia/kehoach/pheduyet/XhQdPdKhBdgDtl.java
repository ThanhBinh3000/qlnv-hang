package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet;

import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdr;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhQdPdKhBdgDtl.TABLE_NAME)
@Data
public class XhQdPdKhBdgDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BDG_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhQdPdKhBdgDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhQdPdKhBdgDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhQdPdKhBdgDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idQdHdr;
    private String maDvi;
    private String diaChi;
    private Long idDxHdr;
    private String soDxuat;
    private LocalDate ngayTao;
    private LocalDate ngayPduyet;
    private String trichYeu;
    private Integer slDviTsan;
    private BigDecimal tongSoLuong;
    private LocalDate tgianDkienTu;
    private LocalDate tgianDkienDen;
    private String loaiHopDong;
    private Integer thoiGianKyHdong;
    private String thoiGianKyHdongGhiChu;
    private Integer tgianTtoan;
    private String tgianTtoanGhiChu;
    private String pthucTtoan;
    private Integer tgianGnhan;
    private String tgianGnhanGhiChu;
    private String pthucGnhan;
    private String thongBao;
    private BigDecimal khoanTienDatTruoc;
    private String donViTinh;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String trangThai;
    @Transient
    private String tenDvi;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenTrangThai;
    @Transient
    private List<XhQdPdKhBdgPl> children = new ArrayList<>();

    //    Thông tin đấu giá
    @Column(name = "ID_QD_PD_KQ_BDG")
    private Long idQdPdKqBdg;
    @Column(name = "SO_QD_PD_KQ_BDG")
    private String soQdPdKqBdg;
    @Column(name = "NGAY_KY_QD_PD_KQ_BDG")
    private LocalDate ngayKyQdPdKqBdg;
    private Integer soDviTsanThanhCong;
    private Integer soDviTsanKhongThanh;
    private String ketQuaDauGia;
    private String soQdDcBdg;
    @Transient
    private Integer nam;
    @Transient
    private String soQdPd;
    @Transient
    private XhQdPdKhBdg xhQdPdKhBdg;
    @Transient
    List<XhTcTtinBdgHdr> listTtinDg = new ArrayList<>();
}
