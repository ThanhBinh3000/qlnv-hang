package com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XH_DX_KH_BAN_TRUC_TIEP_HDR")
@Data
public class XhDxKhBanTrucTiepHdr implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_DX_KH_BAN_TRUC_TIEP_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_DX_KH_BAN_TRUC_TIEP_HDR_SEQ")
    @SequenceGenerator(sequenceName = "XH_DX_KH_BAN_TRUC_TIEP_HDR_SEQ", allocationSize = 1, name = "XH_DX_KH_BAN_TRUC_TIEP_HDR_SEQ")

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

    private String tgianTtoanGhiChu;

    private String pthucTtoan;

    private Integer tgianGnhan;

    private String tgianGnhanGhiChu;

    private String pthucGnhan;

    private String thongBaoKh;

    private BigDecimal tongSoLuong;

    private String ghiChu;

    private Long idSoQdPd;

    private String soQdPd;

    private LocalDate ngayKyQd;

    private String trangThaiTh;
    @Transient
    private String tenTrangThaiTh;

    private Long idThop;

    private Integer slDviTsan;

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
    private List<XhDxKhBanTrucTiepDtl> children = new ArrayList<>();
}
