package com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XH_QD_PD_KH_BTT_HDR")
@Data
public class XhQdPdKhBttHdr implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BTT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_PD_KH_BTT_HDR_SEQ")
    @SequenceGenerator(sequenceName = "XH_QD_PD_KH_BTT_HDR_SEQ", allocationSize = 1, name = "XH_QD_PD_KH_BTT_HDR_SEQ")
    private Long id;

    private Integer namKh;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String soQdPd;

    private LocalDate ngayKyQd;

    private LocalDate ngayHluc;

    private Long idThHdr;

    private String soTrHdr;

    private Long idTrHdr;

    private String trichYeu;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String  moTaHangHoa;

    private String tchuanCluong;

    private Boolean lastest;

    private String phanLoai;

    private Long idGoc;

    private Integer slDviTsan;

    private String soHopDong;

    private String soQdCc;

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
    private List<XhQdPdKhBttDtl> children = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKem = new ArrayList<>();
}
