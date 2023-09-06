package com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.bienbanlaymau;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XH_BB_LAY_MAU_BTT_HDR")
@Data
public class XhBbLayMauBttHdr implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_BB_LAY_MAU_BTT_HDR";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_BB_LAY_MAU_BTT_HDR_SEQ")
//    @SequenceGenerator(sequenceName = "XH_BB_LAY_MAU_BTT_HDR_SEQ", allocationSize = 1, name = "XH_BB_LAY_MAU_BTT_HDR_SEQ")
    private Long id;

    private Integer namKh;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String maQhns;

    private Long idQdNv;

    private String soQdNv;

    private LocalDate ngayQdNv;

    private Long idHd;

    private String soHd;

    private String loaiBienBan;

    private LocalDate ngayKyHd;

    private Long idKtv;
    @Transient
    private String tenKtv;

    private String soBienBan;

    private LocalDate ngayLayMau;

    private String dviKnghiem;

    private String ddiemLayMau;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private Long idDdiemXh;

    private String maDiemKho;
    @Transient
    private String tenDiemKho;

    private String maNhaKho;
    @Transient
    private String tenNhaKho;

    private String maNganKho;
    @Transient
    private String tenNganKho;

    private String maLoKho;
    @Transient
    private String tenLoKho;

    private BigDecimal soLuongLayMau;

    private String ppLayMau;

    private String chiTieuKiemTra;

    private Integer ketQuaNiemPhong;

    private String soBbTinhKho;

    private LocalDate ngayXuatDocKho;

    private String soBbHaoDoi;

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

    private String pthucBanTrucTiep; // 01 : chào giá; 02 : Ủy quyền; 03 : Bán lẻ

    private String phanLoai;

    private Long idBangKeBanLe;

    private String soBangKeBanLe;

    private LocalDate ngayTaoBangKe;

    @Transient
    private String tenDviCungCap;

    @Transient
    private String donViTinh;

    @Transient
    private String tenPpLayMau;

    @Transient
    private String tenDviCha;

    @Transient
    private String tenNguoiPheDuyet;

    @Transient
    private List<XhBbLayMauBttDtl> children = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<FileDinhKem> canCuPhapLy = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileNiemPhong = new ArrayList<>();
}
