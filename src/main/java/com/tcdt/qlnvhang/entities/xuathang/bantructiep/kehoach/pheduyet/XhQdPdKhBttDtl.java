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
@Table(name = "XH_QD_PD_KH_BTT_DTL")
@Data
public class XhQdPdKhBttDtl implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BTT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_PD_KH_BTT_DTL_SEQ")
    @SequenceGenerator(sequenceName = "XH_QD_PD_KH_BTT_DTL_SEQ", allocationSize = 1, name = "XH_QD_PD_KH_BTT_DTL_SEQ")

    private Long id;

    private Long idQdHdr;

    private Long idDxHdr;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String soDxuat;

    private LocalDate ngayTao;

    private LocalDate ngayPduyet;

    private LocalDate tgianDkienTu;

    private LocalDate tgianDkienDen;

    private String trichYeu;

    private BigDecimal tongSoLuong;

    private Integer slDviTsan;

    private String moTaHangHoa;

    private String diaChi;

    private Integer tgianTtoan;

    private String tgianTtoanGhiChu;

    private String pthucTtoan;

    private Integer tgianGnhan;

    private String tgianGnhanGhiChu;

    private String pthucGnhan;

    private String thongBaoKh;

    @Transient
    private List<XhQdPdKhBttDvi> children= new ArrayList<>();

// thông tin chào giá
    private String pthucBanTrucTiep;

    private String diaDiemChaoGia;

    private LocalDate ngayNhanCgia;

    private LocalDate ngayMkho;

    private LocalDate ngayKthuc;

    private String ghiChu;

    private String soQdKq;

    private Long idSoQdKq;

    private String soQdPd;

    private String trangThai;
    @Transient
    private String tenTrangThai;

    private LocalDate thoiHanBan;

    private String trangThaiHd;
    @Transient
    private String tenTrangThaiHd;

    private String trangThaiXh;
    @Transient
    private String tenTrangThaiXh;

    private Integer slHdDaKy;

    private Integer slHdChuaKy;

    private Long idQdNv;

    private String soQdNv;

    @Transient
    private List<FileDinhKem> fileDinhKemUyQuyen = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKemMuaLe = new ArrayList<>();

    @Transient
    private XhQdPdKhBttHdr xhQdPdKhBttHdr;

    @Transient
    private String tenLoaiVthh;

    @Transient
    private String tenCloaiVthh;

    @Transient
    private List<XhHopDongBttHdr> listHopDongBtt;
}
