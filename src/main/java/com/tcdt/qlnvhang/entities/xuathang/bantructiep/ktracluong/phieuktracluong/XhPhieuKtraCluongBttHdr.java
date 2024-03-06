package com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.phieuktracluong;

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
@Table(name = XhPhieuKtraCluongBttHdr.TABLE_NAME)
@Data
public class XhPhieuKtraCluongBttHdr implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_PHIEU_KTRA_CLUONG_BTT_HDR";
    @Id
//  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_PHIEU_KTRA_CLUONG_BTT_SEQ")
//  @SequenceGenerator(sequenceName = "XH_PHIEU_KTRA_CLUONG_BTT_SEQ", allocationSize = 1, name = "XH_PHIEU_KTRA_CLUONG_BTT_SEQ")
    private Long id;
    private Integer namKh;
    private String maDvi;
    private String maQhNs;
    private String soPhieuKiemNghiem;
    private LocalDate ngayLapPhieu;
    private Long idQdNv;
    private String soQdNv;
    private LocalDate ngayKyQdNv;
    private Long idQdNvDtl;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private LocalDate tgianGiaoNhan;
    private Long idBangKeBanLe;
    private String soBangKeBanLe;
    private LocalDate ngayTaoBkeBanLe;
    private Long idBbLayMau;
    private String soBbLayMau;
    private LocalDate ngayLayMau;
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
    private String loaiHinhNx;
    private String kieuNx;
    private String hinhThucBaoQuan;
    private LocalDate ngayKiemNghiemMau;
    private Long idKtvBaoQuan;
    private Long idThuKho;
    private Long idTphongKtvBaoQuan;
    private Long idLanhDaoCuc;
    private String ketQua;
    private String nhanXet;
    private String pthucBanTrucTiep; // 01 : chào giá; 02 : Ủy quyền; 03 : Bán lẻ
    private String phanLoai;
    private Long idTinhKho;
    private String soBbTinhKho;
    private LocalDate ngayLapTinhKho;
    private String maDviCon;
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
    private String soHieuQuyChuan;
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
    private String tenLoaiHinhNx;
    @Transient
    private String tenKieuNx;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenHinhThucBaoQuan;
    @Transient
    private String tenKtvBaoQuan;
    @Transient
    private String tenThuKho;
    @Transient
    private String tenTphongKtvBaoQuan;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenDviCon;
    @Transient
    private String tenLanhDaoCuc;

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
    private Map<String, String> mapDmucHinhThuc;

    public void setMapDmucHinhThuc(Map<String, String> mapDmucHinhThuc) {
        boolean isNewValue = !Objects.equals(this.mapDmucHinhThuc, mapDmucHinhThuc);
        this.mapDmucHinhThuc = mapDmucHinhThuc;
        if (isNewValue && !DataUtils.isNullObject(getHinhThucBaoQuan())) {
            setTenHinhThucBaoQuan(mapDmucHinhThuc.getOrDefault(getHinhThucBaoQuan(), null));
        }
    }

    public String getTrangThai() {
        setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
        return trangThai;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhPhieuKtraCluongBttHdr.TABLE_NAME + "_DINH_KEM'")
    private Set<FileDinhKemJoinTable> fileDinhKem = new HashSet<>();

    public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem) {
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            Set<FileDinhKemJoinTable> uniqueFiles = new HashSet<>(fileDinhKem);
            for (FileDinhKemJoinTable file : uniqueFiles) {
                file.setDataType(XhPhieuKtraCluongBttHdr.TABLE_NAME + "_DINH_KEM");
                file.setXhPhieuKtraCluongBttHdr(this);
            }
            this.fileDinhKem.addAll(uniqueFiles);
        }
    }

    @Transient
    private List<XhPhieuKtraCluongBttDtl> children = new ArrayList<>();
}