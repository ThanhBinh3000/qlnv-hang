package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XH_QD_PD_KH_BDG")
@Data
public class XhQdPdKhBdg implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BDG";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_PD_KH_BDG_SEQ")
    @SequenceGenerator(sequenceName = "XH_QD_PD_KH_BDG_SEQ", allocationSize = 1, name = "XH_QD_PD_KH_BDG_SEQ")

    private Long id;

    private  Integer nam;

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

    private String cloaiVthh;

    private String  moTaHangHoa;

    private String tchuanCluong;

    private Boolean lastest;

    private String phanLoai;

    private Long idGoc;

    private String soQdCc;

    private Integer slDviTsan;

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

    // Transient

    @Transient
    private String tenLoaiVthh;

    @Transient
    private  String tenCloaiVthh;

    @Transient
    List<XhQdPdKhBdgDtl> children = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKem = new ArrayList<>();
}
