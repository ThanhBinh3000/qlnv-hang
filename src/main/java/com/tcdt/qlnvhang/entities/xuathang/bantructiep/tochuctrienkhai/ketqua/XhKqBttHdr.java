package com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = XhKqBttHdr.TABLE_NAME)
@Data
public class XhKqBttHdr implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_KQ_BTT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_KQ_BTT_HDR_SEQ")
    @SequenceGenerator(sequenceName = "XH_KQ_BTT_HDR_SEQ", allocationSize = 1, name = "XH_KQ_BTT_HDR_SEQ")

    private Long id;

    private Long idPdKhDtl;

    private Long idPdKhHdr;

    private Integer namKh;

    private String soQdKq;

    private LocalDate ngayKy;

    private LocalDate ngayHluc;

    private String soQdPd;

    private String trichYeu;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String diaDiemChaoGia;

    private LocalDate ngayMkho;

    private LocalDate ngayKthuc;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private String ghiChu;

    private String trangThaiHd;
    @Transient
    private String tenTrangThaiHd;

    private String trangThaiXh;
    @Transient
    private String tenTrangThaiXh;

    private String loaiHinhNx;

    private String kieuNx;

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
    private List<FileDinhKem> fileCanCu = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileQdDaKy = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileQd = new ArrayList<>();

    @Transient
    private List<XhHopDongBttHdr> listHopDongBtt;

    @Transient
    private List<XhKqBttDtl> children = new ArrayList<>();
}
