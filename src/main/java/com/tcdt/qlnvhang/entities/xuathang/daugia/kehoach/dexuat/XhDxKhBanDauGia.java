package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XH_DX_KH_BAN_DAU_GIA")
@Data
public class XhDxKhBanDauGia  implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_DX_KH_BAN_DAU_GIA";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_DX_KH_BAN_DAU_GIA_SEQ")
    @SequenceGenerator(sequenceName = "XH_DX_KH_BAN_DAU_GIA_SEQ", allocationSize = 1, name = "XH_DX_KH_BAN_DAU_GIA_SEQ")
    private Long id;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String loaiHinhNx;

    private String kieuNx;

    private String diaChi;

    private Integer namKh;

    private String soDxuat;

    private String trichYeu;

    private Long idSoQdCtieu;

    private String soQdCtieu;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private String tchuanCluong;

    private LocalDate tgianDkienTu;

    private LocalDate tgianDkienDen;

    private Integer tgianTtoan;

    private Integer tgianGnhan;

    private String pthucTtoan;

    private String pthucGnhan;

    private String tgianTtoanGhiChu;

    private String tgianGnhanGhiChu;

    private String thongBao;

    private BigDecimal khoanTienDatTruoc;

    private BigDecimal tongSoLuong;

    @Transient
    private BigDecimal tongTienGiaKhoiDiemDx;

    @Transient
    private BigDecimal tongTienGiaKdTheoDgiaDd;

    @Transient
    private BigDecimal tongKhoanTienDatTruocDx;

    @Transient
    private BigDecimal tongKhoanTienDtTheoDgiaDd;

    private String ghiChu;

    private Integer slDviTsan;

    private String trangThaiTh;
    @Transient
    private String tenTrangThaiTh;

    private Long idSoQdPd;

    private String soQdPd;

    private Long idThop;

    private LocalDate ngayKyQd;

    private String donViTinh;

    private String trangThai;
    @Transient
    private String tenTrangThai;

    private LocalDate ngayTao;

    private Long nguoiTaoId;

    private LocalDate ngaySua;

    private Long nguoiSuaId;

    private LocalDate ngayGuiDuyet;

    private Long nguoiGuiDuyetId;

    private LocalDate ngayPduyet;

    private Long nguoiPduyetId;

    private String lyDoTuChoi;

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<XhDxKhBanDauGiaDtl> children = new ArrayList<>();
}
