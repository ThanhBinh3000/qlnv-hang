package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "XH_QD_PD_KH_BDG")
@Data
public class XhQdPdKhBdg  implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BDG";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_PD_KH_BDG_SEQ")
    @SequenceGenerator(sequenceName = "XH_QD_PD_KH_BDG_SEQ", allocationSize = 1, name = "XH_QD_PD_KH_BDG_SEQ")
    private Long id;

    private  Integer namKh;

    private String maDvi;

    @Transient
    private String tenDvi;

    private String soQdPd;

    @Temporal(TemporalType.DATE)
    private Date ngayKyQd;

    @Temporal(TemporalType.DATE)
    private Date ngayHluc;

    private Long idThHdr;

    private String soTrHdr;

    private Long idTrHdr;

    private String trichYeu;

    private String loaiVthh;

    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;

    @Transient
    private  String tenCloaiVthh;

    private String  moTaHangHoa;

    private String soQdCc;

    private String tchuanCluong;

    @Temporal(TemporalType.DATE)
    private Date tgianDkienTu;
    @Temporal(TemporalType.DATE)
    private Date tgianDkienDen;

    private Integer tgianTtoan;

    private String tgianTtoanGhiChu;

    private String pthucTtoan;

    private Integer tgianGnhan;

    private String tgianGnhanGhiChu;

    private String pthucGnhan;

    private String thongBaoKh;

    private BigDecimal khoanTienDatTruoc;

    private BigDecimal tongSoLuong;

    private BigDecimal tongTienKdienDonGia;

    private BigDecimal tongTienDatTruocDonGia;

    private String trangThai;

    @Transient
    private String tenTrangThai;

    @Temporal(TemporalType.DATE)
    private Date ngayTao;

    private String nguoiTao;

    @Temporal(TemporalType.DATE)
    private Date ngaySua;

    private String nguoiSua;

    @Temporal(TemporalType.DATE)
    private Date ngayGuiDuyet;

    private String nguoiGuiDuyet;

    private Integer soDviTsan;

    private Integer slHdDaKy;

    private String soQdPdKqBdg;

    private String ldoTuchoi;

    private Date ngayPduyet;

    private String nguoiPduyet;

    private Boolean lastest;

    private String phanLoai;

    private Long idGoc;


    @Transient
    private String soDxuatKhBdg;

    @Transient
    List<XhQdPdKhBdgDtl> children = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<FileDinhKem> canCuPhapLy = new ArrayList<>();


    public String getTenTrangThai(){
        return NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(this.getTrangThai());
    }
}
