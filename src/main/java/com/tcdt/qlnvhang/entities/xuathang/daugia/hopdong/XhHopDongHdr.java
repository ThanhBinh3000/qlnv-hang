package com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong;

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
@Table(name = XhHopDongHdr.TABLE_NAME)
@Data
public class XhHopDongHdr implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_HOP_DONG_HDR";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhHopDongHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhHopDongHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhHopDongHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String loaiHinhNx;
    private String kieuNhapXuat;
    private Long idQdKq;
    private String soQdKq;
    private LocalDate ngayKyQdKq;
    private Long idQdPd;
    private String soQdPd;
    private String toChucCaNhan;
    private String maDviTsan;
    private LocalDate tgianXuatKho;
    private String soHopDong;
    private String tenHopDong;
    private LocalDate ngayKyHopDong;
    private LocalDate ngayHieuLuc;
    private String ghiChuNgayHluc;
    private String loaiHopDong;
    private String ghiChuLoaiHdong;
    private Integer tgianThienHdongNgay;
    private LocalDate tgianThienHdong;
    private Integer tgianBaoHanh;
    private LocalDate tgianGiaoHang;
    private Integer tgianTinhPhat;
    private BigDecimal soLuongHangCham;
    private Integer soTienTinhPhat;
    private BigDecimal giaTri;
    private LocalDate tgianBaoDamHdong;
    private String ghiChuBaoDam;
    private String dieuKien;
    private String maDvi;
    private String diaChiBenBan;
    private String mstBenBan;
    private String tenNguoiDaiDien;
    private String chucVuBenBan;
    private String sdtBenBan;
    private String faxBenBan;
    private String stkBenBan;
    private String moTaiBenBan;
    private String giayUyQuyen;
    private String tenDviBenMua;
    private String diaChiBenMua;
    private String mstBenMua;
    private String tenNguoiDdienMua;
    private String chucVuBenMua;
    private String sdtBenMua;
    private String faxBenMua;
    private String stkBenMua;
    private String moTaiBenMua;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenHangHoa;
    private String moTaHangHoa;
    private BigDecimal soLuong;
    private String donViTinh;
    private BigDecimal thanhTien;
    private String ghiChu;
    private Long idQdNv;
    private String soQdNv;
    private String trangThai;
    private String trangThaiXh;
    private LocalDate ngayTao;
    private Long nguoiTaoId;
    private LocalDate ngaySua;
    private Long nguoiSuaId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    @Transient
    private String tenDvi;
    @Transient
    private String tenLoaiHinhNx;
    @Transient
    private String tenKieuNhapXuat;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenTrangThaiXh;

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucDvi;

    public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
        this.mapDmucDvi = mapDmucDvi;
        if (!DataUtils.isNullObject(getMaDvi())) {
            setTenDvi(mapDmucDvi.getOrDefault(getMaDvi(), null));
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucVthh;

    public void setMapDmucVthh(Map<String, String> mapDmucVthh) {
        this.mapDmucVthh = mapDmucVthh;
        if (!DataUtils.isNullObject(getLoaiVthh())) {
            setTenLoaiVthh(mapDmucVthh.getOrDefault(getLoaiVthh(), null));
        }
        if (!DataUtils.isNullObject(getCloaiVthh())) {
            setTenCloaiVthh(mapDmucVthh.getOrDefault(getCloaiVthh(), null));
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucLoaiXuat;

    public void setMapDmucLoaiXuat(Map<String, String> mapDmucLoaiXuat) {
        this.mapDmucLoaiXuat = mapDmucLoaiXuat;
        if (!DataUtils.isNullObject(getLoaiHinhNx())) {
            setTenLoaiHinhNx(mapDmucLoaiXuat.getOrDefault(getLoaiHinhNx(), null));
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucKieuXuat;

    public void setMapDmucKieuXuat(Map<String, String> mapDmucKieuXuat) {
        this.mapDmucKieuXuat = mapDmucKieuXuat;
        if (!DataUtils.isNullObject(getKieuNhapXuat())) {
            setTenKieuNhapXuat(mapDmucKieuXuat.getOrDefault(getKieuNhapXuat(), null));
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

    @Transient
    private List<String> listMaDviTsan = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhHopDongHdr.TABLE_NAME + "_DINH_KEM'")
    private Set<FileDinhKemJoinTable> fileDinhKem = new HashSet<>();

    public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem) {
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            Set<FileDinhKemJoinTable> uniqueFiles = new HashSet<>(fileDinhKem);
            for (FileDinhKemJoinTable file : uniqueFiles) {
                file.setDataType(XhHopDongHdr.TABLE_NAME + "_DINH_KEM");
                file.setXhHopDongHdr(this);
            }
            this.fileDinhKem.addAll(uniqueFiles);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhHopDongHdr.TABLE_NAME + "_CAN_CU'")
    private Set<FileDinhKemJoinTable> fileCanCu = new HashSet<>();

    public void setFileCanCu(List<FileDinhKemJoinTable> fileCanCu) {
        this.fileCanCu.clear();
        if (!DataUtils.isNullObject(fileCanCu)) {
            Set<FileDinhKemJoinTable> uniqueFiles = new HashSet<>(fileCanCu);
            for (FileDinhKemJoinTable file : uniqueFiles) {
                file.setDataType(XhHopDongHdr.TABLE_NAME + "_CAN_CU");
                file.setXhHopDongHdr(this);
            }
            this.fileCanCu.addAll(uniqueFiles);
        }
    }

    @Transient
    private List<XhHopDongDtl> children = new ArrayList<>();


    //    Phụ lục
    private Long idHopDong;
    private String soPhuLuc;
    private LocalDate ngayHlucPhuLuc;
    private String veViecPhuLuc;
    private LocalDate ngayHlucSauDcTu;
    private LocalDate ngayHlucSauDcDen;
    private Integer soNgayThienSauDc;
    private String noiDungPhuLuc;
    private String ghiChuPhuLuc;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhHopDongHdr.TABLE_NAME + "_PHU_LUC'")
    private Set<FileDinhKemJoinTable> filePhuLuc = new HashSet<>();

    public void setFilePhuLuc(List<FileDinhKemJoinTable> filePhuLuc) {
        this.filePhuLuc.clear();
        if (!DataUtils.isNullObject(filePhuLuc)) {
            Set<FileDinhKemJoinTable> uniqueFiles = new HashSet<>(filePhuLuc);
            for (FileDinhKemJoinTable file : uniqueFiles) {
                file.setDataType(XhHopDongHdr.TABLE_NAME + "_PHU_LUC");
                file.setXhHopDongHdr(this);
            }
            this.filePhuLuc.addAll(uniqueFiles);
        }
    }

    @Transient
    private List<XhHopDongHdr> phuLuc = new ArrayList<>();
}