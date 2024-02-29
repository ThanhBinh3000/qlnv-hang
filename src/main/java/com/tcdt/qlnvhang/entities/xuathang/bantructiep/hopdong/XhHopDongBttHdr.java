package com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong;

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
@Table(name = XhHopDongBttHdr.TABLE_NAME)
@Data
public class XhHopDongBttHdr implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_HOP_DONG_BTT_HDR";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = TABLE_NAME + "_SEQ", allocationSize = 1, name = TABLE_NAME + "_SEQ")
    private Long id;
    private String maDvi;
    private Integer namHd;
    private String loaiHinhNx;
    private String kieuNx;
    private Long idQdKq;
    private String soQdKq;
    private LocalDate ngayKyQdKq;
    private LocalDate thoiHanXuatKho;
    private Long idQdPd;
    private String soQdPd;
    private LocalDate ngayKyUyQuyen;
    private String maDviTsan;
    private String soHopDong;
    private String tenHopDong;
    private LocalDate ngayKyHopDong;
    private LocalDate ngayHlucHopDong;
    private String ghiChuNgayHluc;
    private String loaiHopDong;
    private String ghiChuLoaiHdong;
    private Integer tgianThienHdongNgay;
    private LocalDate tgianThienHdong;
    private LocalDate tgianGiaoNhanTu;
    private LocalDate tgianGiaoNhanDen;
    private String ghiChuTgianGiaoNhan;
    private Integer thoiGianBaoHanh;
    private BigDecimal giaTri;
    private LocalDate tgianBaoDamHdong;
    private String ghiChuBaoDam;
    private String dieuKien;
    private String diaChiBenBan;
    private String mstBenBan;
    private String tenNguoiDaiDien;
    private String chucVuBenBan;
    private String sdtBenBan;
    private String faxBenBan;
    private String stkBenBan;
    private String moTaiBenBan;
    private String giayUyQuyen;
    private Long idBenMua;
    private String tenBenMua;
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
    private String donViTinh;
    private BigDecimal soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private BigDecimal slXuatBanQdPd;
    private BigDecimal slXuatBanKyHdong;
    private BigDecimal slXuatBanChuaKyHdong;
    private String moTaHangHoa;
    private String ghiChu;
    private Long idChaoGia;
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
    private Long idQdDc;
    private String soQdDc;
    private String phanLoai;
    private Long idKh;
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

    public String getTrangThaiXh() {
        setTenTrangThaiXh(TrangThaiAllEnum.getLabelById(trangThaiXh));
        return trangThaiXh;
    }

    @Transient
    private List<String> listMaDviTsan = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhHopDongBttHdr.TABLE_NAME + "_CAN_CU'")
    private Set<FileDinhKemJoinTable> fileCanCu = new HashSet<>();

    public void setFileCanCu(List<FileDinhKemJoinTable> fileCanCu) {
        this.fileCanCu.clear();
        if (!DataUtils.isNullObject(fileCanCu)) {
            Set<FileDinhKemJoinTable> uniqueFiles = new HashSet<>(fileCanCu);
            for (FileDinhKemJoinTable file : uniqueFiles) {
                file.setDataType(XhHopDongBttHdr.TABLE_NAME + "_CAN_CU");
                file.setXhHopDongBttHdr(this);
            }
            this.fileCanCu.addAll(uniqueFiles);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhHopDongBttHdr.TABLE_NAME + "_DINH_KEM'")
    private Set<FileDinhKemJoinTable> fileDinhKem = new HashSet<>();

    public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem) {
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            Set<FileDinhKemJoinTable> uniqueFiles = new HashSet<>(fileDinhKem);
            for (FileDinhKemJoinTable file : uniqueFiles) {
                file.setDataType(XhHopDongBttHdr.TABLE_NAME + "_DINH_KEM");
                file.setXhHopDongBttHdr(this);
            }
            this.fileDinhKem.addAll(uniqueFiles);
        }
    }

    //Cấp cục
    @Transient
    private List<XhHopDongBttDtl> children = new ArrayList<>();
    //Cấp chi cục
    @Transient
    private List<XhHopDongBttDvi> xhHopDongBttDviList = new ArrayList<>();

    //Phụ lục
    private Long idHd;
    private String soPhuLuc;
    private LocalDate ngayHlucPhuLuc;
    private String noiDungPhuLuc;
    private LocalDate ngayHlucSauDcTu;
    private LocalDate ngayHlucSauDcDen;
    private Integer tgianThienHdSauDc;
    private String noiDungDcKhac;
    private String ghiChuPhuLuc;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhHopDongBttHdr.TABLE_NAME + "_PHU_LUC'")
    private Set<FileDinhKemJoinTable> filePhuLuc = new HashSet<>();

    public void setFilePhuLuc(List<FileDinhKemJoinTable> filePhuLuc) {
        this.filePhuLuc.clear();
        if (!DataUtils.isNullObject(filePhuLuc)) {
            Set<FileDinhKemJoinTable> uniqueFiles = new HashSet<>(filePhuLuc);
            for (FileDinhKemJoinTable file : uniqueFiles) {
                file.setDataType(XhHopDongBttHdr.TABLE_NAME + "_PHU_LUC");
                file.setXhHopDongBttHdr(this);
            }
            this.filePhuLuc.addAll(uniqueFiles);
        }
    }

    @Transient
    private List<XhHopDongBttHdr> phuLuc = new ArrayList<>();
    @Transient
    private List<XhHopDongBttDtl> phuLucDtl = new ArrayList<>();
}