package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdr;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XH_QD_PD_KH_BDG_DTL")
@Data
public class XhQdPdKhBdgDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BDG_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_PD_KH_BDG_DTL_SEQ")
    @SequenceGenerator(sequenceName = "XH_QD_PD_KH_BDG_DTL_SEQ", allocationSize = 1, name = "XH_QD_PD_KH_BDG_DTL_SEQ")
    private Long id;

    private Long idQdHdr;

    private Long idDxHdr;

    private String maDvi;

    private String loaiVthh;

    private String cloaiVthh;

    private String soDxuat;

    private LocalDate ngayTao;

    private LocalDate ngayPduyet;

    private LocalDate tgianDkienTu;

    private LocalDate tgianDkienDen;

    private String trichYeu;

    private BigDecimal tongSoLuong;

    @Transient
    private BigDecimal tongKhoanTienDtTheoDgiaDd;

    private Integer slDviTsan;

    private String moTaHangHoa;

    private String diaChi;

    private String trangThai;

    private Integer tgianTtoan;

    private String tgianTtoanGhiChu;

    private String pthucTtoan;

    private Integer tgianGnhan;

    private String tgianGnhanGhiChu;

    private String pthucGnhan;

    private String thongBaoKh;

    private BigDecimal khoanTienDatTruoc;

    private String donViTinh;

    // Transient

    @Transient
    private String tenDvi;


    @Transient
    private String tenTrangThai;

    @Transient
    private XhQdPdKhBdg xhQdPdKhBdg;

    @Transient
    private XhDxKhBanDauGia xhDxKhBanDauGia;

    @Transient
    private List<XhQdPdKhBdgPl> children= new ArrayList<>();

    @Transient
    List<XhTcTtinBdgHdr> listTtinDg = new ArrayList<>();

    public String getTenTrangThai() {
        return NhapXuatHangTrangThaiEnum.getTenById(this.getTrangThai());
    }

//    Thông tin đấu giá
    @Column(name="SO_QD_PD_KQ_BDG")
    private String soQdPdKqBdg;

    private Long idQdPdKqBdg;

    @Column(name="NGAY_KY_QD_PD_KQ_BDG")
    private LocalDate ngayKyQdPdKqBdg;

    private Integer soDviTsanThanhCong;

    private Integer soDviTsanKhongThanh;

    private String ketQuaDauGia;

    private String soQdDcBdg;

}
