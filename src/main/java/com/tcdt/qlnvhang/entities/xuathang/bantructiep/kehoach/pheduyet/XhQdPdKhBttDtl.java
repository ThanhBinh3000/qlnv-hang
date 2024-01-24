package com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = XhQdPdKhBttDtl.TABLE_NAME)
@Data
public class XhQdPdKhBttDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BTT_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhQdPdKhBttDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhQdPdKhBttDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhQdPdKhBttDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private Integer namKh;
    private String maDvi;
    private String diaChi;
    private Long idDxHdr;
    private String soDxuat;
    private LocalDate ngayTao;
    private LocalDate ngayPduyet;
    private String trichYeu;
    private Integer slDviTsan;
    private BigDecimal tongSoLuong;
    private BigDecimal thanhTien;
    private BigDecimal thanhTienDuocDuyet;
    private String donViTinh;
    private LocalDate tgianDkienTu;
    private LocalDate tgianDkienDen;
    private Integer tgianTtoan;
    private String tgianTtoanGhiChu;
    private String pthucTtoan;
    private Integer tgianGnhan;
    private String tgianGnhanGhiChu;
    private String pthucGnhan;
    private String thongBao;
    @Transient
    private List<XhQdPdKhBttDvi> children = new ArrayList<>();

    // thông tin chào giá
    private String soQdPd;
    private String soQdDc;
    private String pthucBanTrucTiep;
    private LocalDate ngayNhanCgia;
    private Long idQdKq;
    private String soQdKq;
    private String diaDiemChaoGia;
    private String loaiHinhNx;
    private String kieuNx;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String ghiChuChaoGia;
    private String trangThai;
    private Long idQdNv;
    private String soQdNv;
    private String trangThaiHd;
    private String trangThaiXh;
    private Integer slHdDaKy;
    private Integer slHdChuaKy;
    private BigDecimal tongSlDaKyHdong;
    private BigDecimal tongSlChuaKyHdong;
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
    @Transient
    private String tenTrangThaiHd;
    @Transient
    private String tenPthucTtoan;

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

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucThanhToan;

    public void setMapDmucThanhToan(Map<String, String> mapDmucThanhToan) {
        boolean isNewValue = !Objects.equals(this.mapDmucThanhToan, mapDmucThanhToan);
        this.mapDmucThanhToan = mapDmucThanhToan;
        if (isNewValue && !DataUtils.isNullObject(getPthucTtoan())) {
            setTenPthucTtoan(mapDmucThanhToan.getOrDefault(getPthucTtoan(), null));
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

    public String getTrangThaiHd() {
        setTenTrangThaiHd(TrangThaiAllEnum.getLabelById(trangThaiHd));
        return trangThaiHd;
    }

    @Transient
    private XhQdPdKhBttHdr xhQdPdKhBttHdr;
    @Transient
    private List<FileDinhKem> fileDinhKem = new ArrayList<>();
    @Transient
    private List<XhHopDongBttHdr> listHopDongBtt;
}