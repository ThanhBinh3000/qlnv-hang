package com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = XhDgBbHaoDoiHdr.TABLE_NAME)
@Data
public class XhDgBbHaoDoiHdr implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_DG_BB_HAO_DOI_HDR";
    @Id
//  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhDgBbHaoDoiHdr.TABLE_NAME + "_SEQ")
//  @SequenceGenerator(sequenceName = XhDgBbHaoDoiHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhDgBbHaoDoiHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhNs;
    private String soBbHaoDoi;
    private LocalDate ngayLapBienBan;
    private Long idQdNv;
    private String soQdNv;
    private LocalDate ngayKyQdNv;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private Long idBbTinhKho;
    private String soBbTinhKho;
    private LocalDate ngayLapBbTinhKho;
    private String maDiemKho;
    private String diaDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String loaiHinhKho;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenHangHoa;
    private String donViTinh;
    private LocalDate ngayBatDauXuat;
    private LocalDate ngayKetThucXuat;
    private Long idThuKho;
    private Long idKtvBaoQuan;
    private Long idKeToan;
    private Long idLanhDaoChiCuc;
    private BigDecimal tongSlNhap;
    private LocalDate thoiGianKthucNhap;
    private BigDecimal tongSlXuat;
    private LocalDate thoiGianKthucXuat;
    private BigDecimal slHaoThucTe;
    private BigDecimal tileHaoThucTe;
    private BigDecimal slHaoVuotMuc;
    private BigDecimal tileHaoVuotMuc;
    private BigDecimal slHaoThanhLy;
    private BigDecimal tileHaoThanhLy;
    private BigDecimal slHaoDuoiMuc;
    private BigDecimal tileHaoDuoiMuc;
    private BigDecimal dinhMucHaoHut;
    private BigDecimal slHaoTheoDinhMuc;
    private String nguyenNhan;
    private String kienNghi;
    private String ghiChu;
    private Long idPhieuKiemNghiem;
    private String soPhieuKiemNghiem;
    private LocalDate ngayKiemNghiemMau;
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
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenThuKho;
    @Transient
    private String tenKtvBaoQuan;
    @Transient
    private String tenKeToan;
    @Transient
    private String tenLanhDaoChiCuc;
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
        if (!DataUtils.isNullObject(getMaDiemKho())) {
            setTenDiemKho(mapDmucDvi.containsKey(getMaDiemKho()) ? mapDmucDvi.get(getMaDiemKho()) : null);
        }
        if (!DataUtils.isNullObject(getMaNhaKho())) {
            setTenNhaKho(mapDmucDvi.containsKey(getMaNhaKho()) ? mapDmucDvi.get(getMaNhaKho()) : null);
        }
        if (!DataUtils.isNullObject(getMaNganKho())) {
            setTenNganKho(mapDmucDvi.containsKey(getMaNganKho()) ? mapDmucDvi.get(getMaNganKho()) : null);
            if (getTenNganKho() != null) {
                setTenNganLoKho(getTenNganKho());
            }
        }
        if (!DataUtils.isNullObject(getMaLoKho())) {
            setTenLoKho(mapDmucDvi.containsKey(getMaLoKho()) ? mapDmucDvi.get(getMaLoKho()) : null);
            if (getTenLoKho() != null) {
                setTenNganLoKho(getTenLoKho() + " - " + getTenNganKho());
            }
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

    public String getTrangThai() {
        setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
        return trangThai;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhDgBbHaoDoiHdr.TABLE_NAME + "_DINH_KEM'")
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();

    public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem) {
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            fileDinhKem.forEach(s -> {
                s.setDataType(XhDgBbHaoDoiHdr.TABLE_NAME + "_DINH_KEM");
                s.setXhDgBbHaoDoiHdr(this);
            });
            this.fileDinhKem.addAll(fileDinhKem);
        }
    }

    @Transient
    private List<XhDgBbHaoDoiDtl> children = new ArrayList<>();
}