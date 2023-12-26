package com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
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
@Table(name = XhQdNvXhBttHdr.TABLE_NAME)
@Data
public class XhQdNvXhBttHdr implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_NV_XH_BTT_HDR";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhQdNvXhBttHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhQdNvXhBttHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhQdNvXhBttHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private String maDvi;
    private Integer namKh;
    private String soQdNv;
    private LocalDate ngayKyQdNv;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private String soBangKeBanLe;
    private Long idQdPd;
    private String soQdPd;
    private Long idQdDc;
    private String soQdDc;
    private Long idChaoGia;
    private String maDviTsan;
    private String tenBenMua;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenHangHoa;
    private String donViTinh;
    private BigDecimal soLuong;
    private LocalDate tgianGiaoNhan;
    private String loaiHinhNx;
    private String kieuNx;
    private String trichYeu;
    private String pthucBanTrucTiep; // 01 : chào giá; 02 : Ủy quyền; 03 : Bán lẻ
    private String phanLoai;
    private Long idTinhKho;
    private String soTinhKho;
    private Long idHaoDoi;
    private String soHaoDoi;
    private String trangThaiXh;
    private String trangThai;
    private String lyDoTuChoi;
    private LocalDate ngayTao;
    private Long nguoiTaoId;
    private LocalDate ngaySua;
    private Long nguoiSuaId;
    private LocalDate ngayGuiDuyet;
    private Long nguoiGuiDuyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    @Transient
    private String tenDvi;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenLoaiHinhNx;
    @Transient
    private String tenKieuNx;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenTrangThaiXh;
    @Transient
    private String tenLanhDaoCuc;
    @Transient
    private List<String> listMaDviTsan = new ArrayList<>();

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

    public String getTrangThaiXh() {
        setTenTrangThaiXh(TrangThaiAllEnum.getLabelById(trangThaiXh));
        return trangThaiXh;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhQdNvXhBttHdr.TABLE_NAME + "_CAN_CU'")
    private Set<FileDinhKemJoinTable> fileCanCu = new HashSet<>();

    public void setFileCanCu(List<FileDinhKemJoinTable> fileCanCu) {
        this.fileCanCu.clear();
        if (!DataUtils.isNullObject(fileCanCu)) {
            Set<FileDinhKemJoinTable> uniqueFiles = new HashSet<>(fileCanCu);
            for (FileDinhKemJoinTable file : uniqueFiles) {
                file.setDataType(XhQdNvXhBttHdr.TABLE_NAME + "_CAN_CU");
                file.setXhQdNvXhBttHdr(this);
            }
            this.fileCanCu.addAll(uniqueFiles);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhQdNvXhBttHdr.TABLE_NAME + "_DINH_KEM'")
    private Set<FileDinhKemJoinTable> fileDinhKem = new HashSet<>();

    public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem) {
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            Set<FileDinhKemJoinTable> uniqueFiles = new HashSet<>(fileDinhKem);
            for (FileDinhKemJoinTable file : uniqueFiles) {
                file.setDataType(XhQdNvXhBttHdr.TABLE_NAME + "_DINH_KEM");
                file.setXhQdNvXhBttHdr(this);
            }
            this.fileDinhKem.addAll(uniqueFiles);
        }
    }

    @Transient
    private List<XhQdNvXhBttDtl> children = new ArrayList<>();
}