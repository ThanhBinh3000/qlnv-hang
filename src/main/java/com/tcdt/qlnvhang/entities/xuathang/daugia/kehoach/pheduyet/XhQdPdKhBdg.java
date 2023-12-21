package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = XhQdPdKhBdg.TABLE_NAME)
@Data
public class XhQdPdKhBdg implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BDG";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhQdPdKhBdg.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhQdPdKhBdg.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhQdPdKhBdg.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private Long idGoc;
    private String soQdPd;
    private LocalDate ngayKyQd;
    private LocalDate ngayHluc;
    private Long idThHdr;
    private Long idTrHdr;
    private String soTrHdr;
    private String trichYeu;
    private String soQdCc;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String loaiHinhNx;
    private String kieuNx;
    private String tchuanCluong;
    private Boolean lastest;
    private Integer slDviTsan;
    private String phanLoai;
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
    private String soCongVan;
    private LocalDate ngayTaoCongVan;
    private Integer lanDieuChinh;
    private String soQdCanDc;
    private String soQdDc;
    private LocalDate ngayKyDc;
    private LocalDate ngayHlucDc;
    private String trichYeuDieuChinh;
    private String noiDungToTrinh;
    private String noiDungDieuChinh;
    private String type;
    private Long idQdPd;
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
        if (!DataUtils.isNullObject(getKieuNx())) {
            setTenKieuNx(mapDmucKieuXuat.getOrDefault(getKieuNx(), null));
        }
    }

    public String getTrangThai() {
        setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
        return trangThai;
    }

    @Transient
    private List<FileDinhKem> canCuPhapLy = new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileDinhKem = new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileDinhKemDc = new ArrayList<>();
    @Transient
    private List<XhQdPdKhBdgDtl> children = new ArrayList<>();
}