package com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XH_THOP_DX_KH_BTT_HDR")
@Data
public class XhThopDxKhBttHdr implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_THOP_DX_KH_BTT_HDR";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_THOP_DX_KH_MTT_SEQ")
//    @SequenceGenerator(sequenceName = "XH_THOP_DX_KH_MTT_SEQ", allocationSize = 1, name = "XH_THOP_DX_KH_MTT_SEQ")
    private Long id;

    private LocalDate ngayThop;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private LocalDate ngayDuyetTu;

    private LocalDate ngayDuyetDen;

    private String noiDungThop;

    private Integer namKh;

    private String maDvi;

    private Long idSoQdPd;

    private String soQdPd;

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
    private List<XhThopDxKhBttDtl> children = new ArrayList<>();
}
