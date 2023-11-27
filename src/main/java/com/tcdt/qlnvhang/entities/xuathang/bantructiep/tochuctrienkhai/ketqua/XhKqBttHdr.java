package com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttDvi;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Entity
@Table(name = XhKqBttHdr.TABLE_NAME)
@Data
public class XhKqBttHdr implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_KQ_BTT_HDR";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhKqBttHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhKqBttHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhKqBttHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer namKh;
    private String soQdKq;
    private LocalDate ngayKy;
    private LocalDate ngayHluc;
    private Long idQdPd;
    private String soQdPd;
    private Long idChaoGia;
    private String loaiHinhNx;
    private String kieuNx;
    private String trichYeu;
    private String maDvi;
    private String diaDiemChaoGia;
    private LocalDate ngayMkho;
    private LocalDate ngayKthuc;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String ghiChu;
    private Integer slHdDaKy;
    private Integer slHdChuaKy;
    private BigDecimal tongSlDaKyHdong;
    private BigDecimal tongSlChuaKyHdong;
    private BigDecimal tongSoLuong;
    private BigDecimal tongGiaTriHdong;
    private String trangThai;
    private String trangThaiHd;
    private String trangThaiXh;
    private String lyDoTuChoi;
    private LocalDate ngayTao;
    private Long nguoiTaoId;
    private LocalDate ngaySua;
    private Long nguoiSuaId;
    private LocalDate ngayGuiDuyet;
    private Long nguoiGuiDuyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private Long idQdDc;
    private String soQdDc;
    @Transient
    private String tenDvi;
    @Transient
    private String tenLoaiHinhNx;
    @Transient
    private String tenKieuNx;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenTrangThaiHd;
    @Transient
    private String tenTrangThaiXh;

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucDvi;

    public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
        this.mapDmucDvi = mapDmucDvi;
        if (!DataUtils.isNullObject(getMaDvi())) {
            setTenDvi(mapDmucDvi.containsKey(getMaDvi()) ? mapDmucDvi.get(getMaDvi()) : null);
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapLoaiHinhNx;

    public void setMapLoaiHinhNx(Map<String, String> mapLoaiHinhNx) {
        this.mapLoaiHinhNx = mapLoaiHinhNx;
        if (!DataUtils.isNullObject(getLoaiHinhNx())) {
            setTenLoaiHinhNx(mapLoaiHinhNx.containsKey(getLoaiHinhNx()) ? mapLoaiHinhNx.get(getLoaiHinhNx()) : null);
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapKieuNx;

    public void setMapKieuNx(Map<String, String> mapKieuNx) {
        this.mapKieuNx = mapKieuNx;
        if (!DataUtils.isNullObject(getKieuNx())) {
            setTenKieuNx(mapKieuNx.containsKey(getKieuNx()) ? mapKieuNx.get(getKieuNx()) : null);
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapVthh;

    public void setMapVthh(Map<String, String> mapVthh) {
        this.mapVthh = mapVthh;
        if (!DataUtils.isNullObject(getLoaiVthh())) {
            setTenLoaiVthh(mapVthh.containsKey(getLoaiVthh()) ? mapVthh.get(getLoaiVthh()) : null);
        }
        if (!DataUtils.isNullObject(getCloaiVthh())) {
            setTenCloaiVthh(mapVthh.containsKey(getCloaiVthh()) ? mapVthh.get(getCloaiVthh()) : null);
        }
    }

    public String getTrangThai() {
        setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
        return trangThai;
    }

    public String getTrangThaiHd() {
        setTenTrangThaiHd(TrangThaiAllEnum.getLabelById(trangThaiHd));
        return trangThaiHd;
    }

    public String getTrangThaiXh() {
        setTenTrangThaiXh(TrangThaiAllEnum.getLabelById(trangThaiXh));
        return trangThaiXh;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhKqBttHdr.TABLE_NAME + "_CAN_CU'")
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();

    public void setFileCanCu(List<FileDinhKemJoinTable> fileCanCu) {
        this.fileCanCu.clear();
        if (!DataUtils.isNullObject(fileCanCu)) {
            fileCanCu.forEach(s -> {
                s.setDataType(XhKqBttHdr.TABLE_NAME + "_CAN_CU");
                s.setXhKqBttHdr(this);
            });
            this.fileCanCu.addAll(fileCanCu);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhKqBttHdr.TABLE_NAME + "_DA_KY'")
    private List<FileDinhKemJoinTable> fileDaKy = new ArrayList<>();

    public void setFileDaKy(List<FileDinhKemJoinTable> fileDaKy) {
        this.fileDaKy.clear();
        if (!DataUtils.isNullObject(fileDaKy)) {
            fileDaKy.forEach(s -> {
                s.setDataType(XhKqBttHdr.TABLE_NAME + "_DA_KY");
                s.setXhKqBttHdr(this);
            });
            this.fileDaKy.addAll(fileDaKy);
        }
    }
    @Transient
    private List<XhHopDongBttHdr> listHopDongBtt;
    @Transient
    private List<XhQdPdKhBttDvi> children = new ArrayList<>();
}