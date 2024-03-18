package com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
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
import java.util.*;


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
    private String donViTinh;
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
        boolean isNewValue = !Objects.equals(this.mapDmucDvi, mapDmucDvi);
        this.mapDmucDvi = mapDmucDvi;
        if (isNewValue && !DataUtils.isNullObject(getMaDvi())) {
            setTenDvi(mapDmucDvi.getOrDefault(getMaDvi(), null));
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucVthh;

    public void setMapDmucVthh(Map<String, String> mapDmucVthh) {
        boolean isNewValue = !Objects.equals(this.mapDmucVthh, mapDmucVthh);
        this.mapDmucVthh = mapDmucVthh;
        if (isNewValue && !DataUtils.isNullObject(getLoaiVthh())) {
            setTenLoaiVthh(mapDmucVthh.getOrDefault(getLoaiVthh(), null));
        }
        if (isNewValue && !DataUtils.isNullObject(getCloaiVthh())) {
            setTenCloaiVthh(mapDmucVthh.getOrDefault(getCloaiVthh(), null));
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucLoaiXuat;

    public void setMapDmucLoaiXuat(Map<String, String> mapDmucLoaiXuat) {
        boolean isNewValue = !Objects.equals(this.mapDmucLoaiXuat, mapDmucLoaiXuat);
        this.mapDmucLoaiXuat = mapDmucLoaiXuat;
        if (isNewValue && !DataUtils.isNullObject(getLoaiHinhNx())) {
            setTenLoaiHinhNx(mapDmucLoaiXuat.getOrDefault(getLoaiHinhNx(), null));
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucKieuXuat;

    public void setMapDmucKieuXuat(Map<String, String> mapDmucKieuXuat) {
        boolean isNewValue = !Objects.equals(this.mapDmucKieuXuat, mapDmucKieuXuat);
        this.mapDmucKieuXuat = mapDmucKieuXuat;
        if (isNewValue && !DataUtils.isNullObject(getKieuNx())) {
            setTenKieuNx(mapDmucKieuXuat.getOrDefault(getKieuNx(), null));
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
    private Set<FileDinhKemJoinTable> fileCanCu = new HashSet<>();

    public void setFileCanCu(List<FileDinhKemJoinTable> fileCanCu) {
        this.fileCanCu.clear();
        if (!DataUtils.isNullObject(fileCanCu)) {
            Set<FileDinhKemJoinTable> uniqueFiles = new HashSet<>(fileCanCu);
            for (FileDinhKemJoinTable file : uniqueFiles) {
                file.setDataType(XhKqBttHdr.TABLE_NAME + "_CAN_CU");
                file.setXhKqBttHdr(this);
            }
            this.fileCanCu.addAll(uniqueFiles);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhKqBttHdr.TABLE_NAME + "_DA_KY'")
    private Set<FileDinhKemJoinTable> fileDaKy = new HashSet<>();

    public void setFileDaKy(List<FileDinhKemJoinTable> fileDaKy) {
        this.fileDaKy.clear();
        if (!DataUtils.isNullObject(fileDaKy)) {
            Set<FileDinhKemJoinTable> uniqueFiles = new HashSet<>(fileDaKy);
            for (FileDinhKemJoinTable file : uniqueFiles) {
                file.setDataType(XhKqBttHdr.TABLE_NAME + "_DA_KY");
                file.setXhKqBttHdr(this);
            }
            this.fileDaKy.addAll(uniqueFiles);
        }
    }

    @Transient
    private List<XhHopDongBttHdr> listHopDongBtt;
}