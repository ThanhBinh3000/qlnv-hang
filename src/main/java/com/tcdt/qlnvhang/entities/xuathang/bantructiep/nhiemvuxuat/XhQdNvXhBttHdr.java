package com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat;
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
@Table(name = "XH_QD_NV_XH_BTT_HDR")
@Data
public class XhQdNvXhBttHdr implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_NV_XH_BTT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_NV_XH_BTT_HDR_SEQ")
    @SequenceGenerator(sequenceName = "XH_QD_NV_XH_BTT_HDR_SEQ", allocationSize = 1, name = "XH_QD_NV_XH_BTT_HDR_SEQ")

    private Long id;

    private String maDvi;
    @Transient
    private String tenDvi;

    private Integer namKh;

    private String soQdNv;

    private Long idHd;

    private String soHd;

    private Long idQdPd;

    private Long idQdPdDtl;

    private String soQdPd;

    private String maDviTsan;

    private String tenTccn;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;

    private String loaiHinhNx;

    private String kieuNx;

    private BigDecimal soLuongBanTrucTiep;

    private String donViTinh;

    private LocalDate tgianGnhan;

    private String trichYeu;

    private String trangThaiXh;
    @Transient
    private String tenTrangThaiXh;

    private String pthucBanTrucTiep; // 01 : chào giá; 02 : Ủy quyền; 03 : Bán lẻ

    private LocalDate ngayKyHd;

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

    private String phanLoai;

    @Transient
    private List<XhQdNvXhBttDtl> children = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKem = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<String> listMaDviTsan = new ArrayList<>();

    @Transient
    private List<XhHopDongBttHdr> listHopDongBtt;
}
