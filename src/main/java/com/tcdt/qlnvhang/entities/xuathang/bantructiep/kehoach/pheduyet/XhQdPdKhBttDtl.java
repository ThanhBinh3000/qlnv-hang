package com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBtt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "XH_QD_PD_KH_BTT_DTL")
@Data
public class XhQdPdKhBttDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BTT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_PD_KH_BTT_DTL_SEQ")
    @SequenceGenerator(sequenceName = "XH_QD_PD_KH_BTT_DTL_SEQ", allocationSize = 1, name = "XH_QD_PD_KH_BTT_DTL_SEQ")
    private Long id;

    private Long idQdHdr;

    private Long idDxHdr;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String soDxuat;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;

    @Temporal(TemporalType.DATE)
    private Date tgianDkienTu;

    @Temporal(TemporalType.DATE)
    private Date tgianDkienDen;

    private String trichYeu;

    private BigDecimal tongSoLuong;

    private Integer soDviTsan;

    private String moTaHangHoa;

    private String diaChi;

    private Integer tgianTtoan;

    private String tgianTtoanGhiChu;

    private String pthucTtoan;

    private Integer tgianGnhan;

    private String tgianGnhanGhiChu;

    private String pthucGnhan;

    private String thongBaoKh;


    @Transient
    private List<XhQdPdKhBttDvi> children= new ArrayList<>();

    @Transient
    private XhQdPdKhBttHdr hdr;


    // trường của chào giá, ủy quyền, mua lẻ.
    private String trangThai;
    @Transient
    private String tenTrangThai;

    private String pthucBanTrucTiep;

    private String diaDiemChaoGia;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNhanCgia;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayMkho;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKthuc;

    private String ghiChu;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    @Transient
    private List<FileDinhKem> fileDinhKemUyQuyen = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKemMuaLe = new ArrayList<>();

    @Transient
    private List<XhTcTtinBtt> xhTcTtinBttList = new ArrayList<>();


    public String getTenTrangThai() {
        return NhapXuatHangTrangThaiEnum.getTenById(this.getTrangThai());
    }
}
