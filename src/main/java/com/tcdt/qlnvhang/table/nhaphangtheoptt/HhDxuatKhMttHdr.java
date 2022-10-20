package com.tcdt.qlnvhang.table.nhaphangtheoptt;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "HH_DX_KHMTT_HDR")
@Data
public class HhDxuatKhMttHdr  implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_DX_KHMTT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DX_KHMTT_HDR_SEQ")
    @SequenceGenerator(sequenceName = "HH_DX_KHMTT_HDR_SEQ", allocationSize = 1, name = "HH_DX_KHMTT_HDR_SEQ")
    private Long id;
    private String tenDvi;
    private String maDvi;
    private String tenDuAn;
    private String loaiHinhNx;
    private String kieuNx;
    private Integer namKh;
    private String soDxuat;
    private String trichYeu;
    private String soQd;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    private String trangThaiTh;
    @Transient
    private String tenTrangThaiTh;
    @Temporal(TemporalType.DATE)
    private Date ngayTao;
    private String nguoiTao;
    @Temporal(TemporalType.DATE)
    private Date ngaySua;
    private  String nguoiSua;
    private String ldoTuchoi;
    @Temporal(TemporalType.DATE)
    private Date ngayGuiDuyet;
    private String nguoiGuiDuyet;
    @Temporal(TemporalType.DATE)
    private Date ngayPduyet;
    private String nguoiPduyet;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String moTaHangHoa;
    private String ptMua;
    private String tchuanCluong;
    private String giaMua;
    private String giaChuaThue;
    private BigDecimal giaCoThue;
    private String thueGtgt;
    @Temporal(TemporalType.DATE)
    private Date tgianMkho;
    @Temporal(TemporalType.DATE)
    private Date tgianKthuc;
    private String ghiChu;
    private BigDecimal tongMucDt;
    private BigDecimal tongSoLuong;
    private String nguonVon;
    private String tenChuDt;
    private String soQdPduyet;
    private String maThop;
    private String diaChiDvi;


    @Transient
    private List<FileDinhKem> fileDinhKems =new ArrayList<>();

    @Transient
    private List<HhDxuatKhMttSldd> soLuongDiaDiemList = new ArrayList<>();

    @Transient
    private List<HhDxuatKhMttCcxdg> ccXdgList = new ArrayList<>();
}
