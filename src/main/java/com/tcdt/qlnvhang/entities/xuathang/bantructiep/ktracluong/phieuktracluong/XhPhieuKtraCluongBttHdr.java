package com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.phieuktracluong;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XH_PHIEU_KTRA_CLUONG_BTT_HDR")
@Data
public class XhPhieuKtraCluongBttHdr implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_PHIEU_KTRA_CLUONG_BTT_HDR";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_PHIEU_KTRA_CLUONG_BTT_SEQ")
//    @SequenceGenerator(sequenceName = "XH_PHIEU_KTRA_CLUONG_BTT_SEQ", allocationSize = 1, name = "XH_PHIEU_KTRA_CLUONG_BTT_SEQ")

    private Long id;

    private Integer namKh;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String maQhns;

    private Long idBienBan;

    private String soBienBan;

    private Long idQdNv;

    private String soQdNv;

    private LocalDate ngayQdNv;

    private String soPhieu;

    private Long idNgKnghiem;
    @Transient
    private String tenNguoiKiemNghiem;

    private Long idTruongPhong;
    @Transient
    private String tenTruongPhong;

    private Long idKtv;
    @Transient
    private String tenKtv;

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

    private BigDecimal soLuong;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private String hthucBquan;

    private LocalDate ngayLayMau;

    private LocalDate ngayKnghiem;

    private String ketQua;

    private String ketLuan;

    private String soBbXuatDoc;

    private LocalDate ngayXuatDocKho;

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

    private String soHd;

    private Long idHd;

    @Transient
    private String tenChiCuc;

    @Transient
    private String tenNguoiPheDuyet;

    @Transient
    private String tenHthucBquan;

    @Transient
    private BigDecimal soLuongBaoQuan;

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<XhPhieuKtraCluongBttDtl> children = new ArrayList<>();
}
