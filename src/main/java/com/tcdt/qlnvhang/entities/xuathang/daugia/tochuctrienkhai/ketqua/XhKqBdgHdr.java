package com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.ketqua;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongHdr;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = XhKqBdgHdr.TABLE_NAME)
@Data
public class XhKqBdgHdr implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_KQ_BDG_HDR";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhKqBdgHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhKqBdgHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhKqBdgHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private String maDvi;
    private Integer nam;
    private String soQdKq;
    private String trichYeu;
    private LocalDate ngayHieuLuc;
    private LocalDate ngayKy;
    private String loaiHinhNx;
    private String kieuNhapXuat;
    private Long idQdPd;
    private Long idQdPdDtl;
    private String soQdPd;
    private Long idQdDc;
    private String soQdDc;
    private Long idThongBao;
    private String maThongBao;
    private String soBienBan;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String phuongThucGiaoNhan;
    private Integer tgianGiaoNhanNgay;
    private String tgianGnhanGhiChu;
    private String ghiChu;
    private String hinhThucDauGia;
    private String phuongThucDauGia;
    private String soTbKhongThanh;
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
    private String tenLoaiHinhNx;
    @Transient
    private String tenKieuNhapXuat;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenHinhThucDauGia;
    @Transient
    private String tenPhuongThucDauGia;
    @Transient
    private String tenTrangThai;

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

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucHinhThuc;

    public void setMapDmucHinhThuc(Map<String, String> mapDmucHinhThuc) {
        this.mapDmucHinhThuc = mapDmucHinhThuc;
        if (!DataUtils.isNullObject(getHinhThucDauGia())) {
            setTenHinhThucDauGia(mapDmucHinhThuc.getOrDefault(getHinhThucDauGia(), null));
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucPhuongThuc;

    public void setMapDmucPhuongThuc(Map<String, String> mapDmucPhuongThuc) {
        this.mapDmucPhuongThuc = mapDmucPhuongThuc;
        if (!DataUtils.isNullObject(getPhuongThucDauGia())) {
            setTenPhuongThucDauGia(mapDmucPhuongThuc.getOrDefault(getPhuongThucDauGia(), null));
        }
    }

    public String getTrangThai() {
        setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
        return trangThai;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhKqBdgHdr.TABLE_NAME + "_CAN_CU'")
    private Set<FileDinhKemJoinTable> fileCanCu = new HashSet<>();

    public void setFileCanCu(List<FileDinhKemJoinTable> fileCanCu) {
        this.fileCanCu.clear();
        if (!DataUtils.isNullObject(fileCanCu)) {
            Set<FileDinhKemJoinTable> uniqueFiles = new HashSet<>(fileCanCu);
            for (FileDinhKemJoinTable file: uniqueFiles){
                file.setDataType(XhKqBdgHdr.TABLE_NAME + "_CAN_CU");
                file.setXhKqBdgHdr(this);
            }
            this.fileCanCu.addAll(uniqueFiles);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhKqBdgHdr.TABLE_NAME + "_DINH_KEM'")
    private Set<FileDinhKemJoinTable> fileDinhKem = new HashSet<>();

    public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem) {
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            Set<FileDinhKemJoinTable> uniqueFiles = new HashSet<>(fileDinhKem);
            for (FileDinhKemJoinTable file: uniqueFiles){
                file.setDataType(XhKqBdgHdr.TABLE_NAME + "_DINH_KEM");
                file.setXhKqBdgHdr(this);
            }
            this.fileDinhKem.addAll(uniqueFiles);
        }
    }


    //Hợp đồng
    private Integer tongDviTsan;
    private Integer slDviTsanThanhCong;
    private Integer slDviTsanKhongThanh;
    private Integer slHopDongDaKy;
    private LocalDate thoiHanThanhToan;
    private Long tongSlXuat;
    private Long thanhTien;
    private String trangThaiHd;
    private String trangThaiXh;
    @Transient
    private String tenTrangThaiHd;
    @Transient
    private String tenTrangThaiXh;

    public String getTrangThaiHd() {
        setTenTrangThaiHd(TrangThaiAllEnum.getLabelById(trangThaiHd));
        return trangThaiHd;
    }

    public String getTrangThaiXh() {
        setTenTrangThaiXh(TrangThaiAllEnum.getLabelById(trangThaiXh));
        return trangThaiXh;
    }

    @Transient
    private List<XhHopDongHdr> listHopDong;
}