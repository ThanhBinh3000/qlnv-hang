package com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.bienbanlaymau;

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
@Table(name = XhBbLayMauBttHdr.TABLE_NAME)
@Data
public class XhBbLayMauBttHdr implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_BB_LAY_MAU_BTT_HDR";
    @Id
//  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhBbLayMauBttHdr.TABLE_NAME + "_SEQ")
//  @SequenceGenerator(sequenceName = XhBbLayMauBttHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhBbLayMauBttHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer namKh;
    private String maDvi;
    private String loaiBienBan;
    private String maQhNs;
    private String soBbLayMau;
    private LocalDate ngayLayMau;
    private Long idQdNv;
    private String soQdNv;
    private LocalDate ngayKyQdNv;
    private Long idQdNvDtl;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private Long idBangKeBanLe;
    private String soBangKeBanLe;
    private LocalDate ngayTaoBkeBanLe;
    private Long idThuKho;
    private Long idKtvBaoQuan;
    private Long idLanhDaoChiCuc;
    private Long idKho;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private BigDecimal soLuong;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenHangHoa;
    private String donViTinh;
    private LocalDate tgianGiaoNhan;
    private String donViKnghiem;
    private String diaDiemLayMau;
    private BigDecimal soLuongKiemTra;
    private Boolean ketQuaNiemPhong;
    private String pthucBanTrucTiep; // 01 : chào giá; 02 : Ủy quyền; 03 : Bán lẻ
    private String phanLoai;
    private String loaiHinhNx;
    private String kieuNx;
    private Long idHaoDoi;
    private String soBbHaoDoi;
    private Long idTinhKho;
    private String soBbTinhKho;
    private LocalDate ngayXuatDocKho;
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
    private String tchuanCluong;
    @Transient
    private String tenDvi;
    @Transient
    private String tenDiemKho;
    @Transient
    private String tenNhaKho;
    @Transient
    private String tenNganKho;
    @Transient
    private String tenLoKho;
    @Transient
    private String tenNganLoKho;
    @Transient
    private String tenThuKho;
    @Transient
    private String tenKtvBaoQuan;
    @Transient
    private String tenLanhDaoChiCuc;
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
    private String tenBenMua;
    @Transient
    private String phuongPhapLayMau;
    @Transient
    private String chiTieuKiemTra;
    @Transient
    private String maDviCha;
    @Transient
    private String tenDviCha;

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucDvi;

    public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
        boolean isNewValue = !Objects.equals(this.mapDmucDvi, mapDmucDvi);
        this.mapDmucDvi = mapDmucDvi;
        if (isNewValue && !DataUtils.isNullObject(getMaDvi())) {
            setTenDvi(mapDmucDvi.getOrDefault(getMaDvi(), null));
        }
        if (isNewValue && !DataUtils.isNullObject(getMaDiemKho())) {
            setTenDiemKho(mapDmucDvi.getOrDefault(getMaDiemKho(), null));
        }
        if (isNewValue && !DataUtils.isNullObject(getMaNhaKho())) {
            setTenNhaKho(mapDmucDvi.getOrDefault(getMaNhaKho(), null));
        }
        if (isNewValue && !DataUtils.isNullObject(getMaNganKho())) {
            setTenNganKho(mapDmucDvi.getOrDefault(getMaNganKho(), null));
            if (getTenNganKho() != null) {
                setTenNganLoKho(getTenNganKho());
            }
        }
        if (isNewValue && !DataUtils.isNullObject(getMaLoKho())) {
            setTenLoKho(mapDmucDvi.getOrDefault(getMaLoKho(), null));
            if (getTenLoKho() != null) {
                setTenNganLoKho(getTenLoKho() + " - " + getTenNganKho());
            }
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhBbLayMauBttHdr.TABLE_NAME + "_CAN_CU'")
    private Set<FileDinhKemJoinTable> fileCanCu = new HashSet<>();

    public void setFileCanCu(List<FileDinhKemJoinTable> fileCanCu) {
        this.fileCanCu.clear();
        if (!DataUtils.isNullObject(fileCanCu)) {
            Set<FileDinhKemJoinTable> uniqueFiles = new HashSet<>(fileCanCu);
            for (FileDinhKemJoinTable file : uniqueFiles) {
                file.setDataType(XhBbLayMauBttHdr.TABLE_NAME + "_CAN_CU");
                file.setXhBbLayMauBttHdr(this);
            }
            fileCanCu.addAll(uniqueFiles);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhBbLayMauBttHdr.TABLE_NAME + "_DINH_KEM'")
    private Set<FileDinhKemJoinTable> fileDinhKem = new HashSet<>();

    public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem) {
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            Set<FileDinhKemJoinTable> uniqueFiles = new HashSet<>(fileDinhKem);
            for (FileDinhKemJoinTable file : uniqueFiles) {
                file.setDataType(XhBbLayMauBttHdr.TABLE_NAME + "_DINH_KEM");
                file.setXhBbLayMauBttHdr(this);
            }
            fileDinhKem.addAll(uniqueFiles);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhBbLayMauBttHdr.TABLE_NAME + "_NIEM_PHONG'")
    private Set<FileDinhKemJoinTable> fileNiemPhong = new HashSet<>();

    public void setFileNiemPhong(List<FileDinhKemJoinTable> fileNiemPhong) {
        this.fileNiemPhong.clear();
        if (!DataUtils.isNullObject(fileNiemPhong)) {
            Set<FileDinhKemJoinTable> uniqueFiles = new HashSet<>(fileNiemPhong);
            for (FileDinhKemJoinTable file : uniqueFiles) {
                file.setDataType(XhBbLayMauBttHdr.TABLE_NAME + "_NIEM_PHONG");
                file.setXhBbLayMauBttHdr(this);
            }
            fileNiemPhong.addAll(uniqueFiles);
        }
    }

    @Transient
    private List<XhBbLayMauBttDtl> children = new ArrayList<>();
}