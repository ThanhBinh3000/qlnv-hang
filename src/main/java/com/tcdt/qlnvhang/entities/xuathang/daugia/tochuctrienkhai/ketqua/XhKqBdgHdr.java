package com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.ketqua;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = XhKqBdgHdr.TABLE_NAME)
@Data
public class XhKqBdgHdr extends BaseEntity implements Serializable {
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
    private LocalDate ngayHluc;
    private LocalDate ngayKy;
    private String loaiHinhNx;
    private String kieuNx;
    private Long idQdPd;
    private String soQdPd;
    private Long idThongTin;
    private Long idPdKhDtl;
    private String maThongBao;
    private String soBienBan;
    private String loaiVthh;
    private String cloaiVthh;
    private String pthucGnhan;
    private Integer tgianGnhan;
    private String tgianGnhanGhiChu;
    private String ghiChu;
    private String hinhThucDauGia;
    private String pthucDauGia;
    private String soTbKhongThanh;
    private String trangThai;
    private String lyDoTuChoi;
    private LocalDate ngayGuiDuyet;
    private Long nguoiGuiDuyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
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
    private String tenHinhThucDauGia;
    @Transient
    private String tenPthucDauGia;
    @Transient
    private String tenTrangThai;

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

    @JsonIgnore
    @Transient
    private Map<String, String> mapHinhThucDg;

    public void setMapHinhThucDg(Map<String, String> mapHinhThucDg) {
        this.mapHinhThucDg = mapHinhThucDg;
        if (!DataUtils.isNullObject(getHinhThucDauGia())) {
            setTenHinhThucDauGia(mapHinhThucDg.containsKey(getHinhThucDauGia()) ? mapHinhThucDg.get(getHinhThucDauGia()) : null);
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapPthucDauGia;

    public void setMapPthucDauGia(Map<String, String> mapPthucDauGia) {
        this.mapPthucDauGia = mapPthucDauGia;
        if (!DataUtils.isNullObject(getPthucDauGia())) {
            setTenPthucDauGia(mapPthucDauGia.containsKey(getPthucDauGia()) ? mapPthucDauGia.get(getPthucDauGia()) : null);
        }
    }

    public String getTrangThai() {
        setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
        return trangThai;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhKqBdgHdr.TABLE_NAME + "CAN_CU'")
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();

    public void setFileCanCu(List<FileDinhKemJoinTable> fileCanCu) {
        this.fileCanCu.clear();
        if (!DataUtils.isNullObject(fileCanCu)) {
            fileCanCu.forEach(s -> {
                s.setDataType(XhKqBdgHdr.TABLE_NAME + "CAN_CU");
                s.setXhKqBdgHdr(this);
            });
            this.fileCanCu.addAll(fileCanCu);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhKqBdgHdr.TABLE_NAME + "_DINH_KEM'")
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();

    public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem) {
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            fileDinhKem.forEach(s -> {
                s.setDataType(XhKqBdgHdr.TABLE_NAME + "_DINH_KEM");
                s.setXhKqBdgHdr(this);
            });
            this.fileDinhKem.addAll(fileDinhKem);
        }
    }

    //Hợp đồng
    private Long tongDvts;
    private Long soDvtsDgTc;
    private Long slHdDaKy;
    private LocalDate thoiHanTt;
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
