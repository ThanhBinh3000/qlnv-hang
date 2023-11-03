package com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl;

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
@Table(name = XhPhieuKnghiemCluong.TABLE_NAME)
@Data
public class XhPhieuKnghiemCluong implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_PHIEU_KNGHIEM_CLUONG";
    @Id
//  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhPhieuKnghiemCluong.TABLE_NAME + "_SEQ")
//  @SequenceGenerator(sequenceName = XhPhieuKnghiemCluong.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhPhieuKnghiemCluong.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhNs;
    private String soPhieuKiemNghiem;
    private LocalDate ngayLapPhieu;
    private Long idQdNv;
    private String soQdNv;
    private LocalDate ngayKyQdNv;
    private Long idQdNvDtl;
    private LocalDate tgianGiaoHang;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private Long idBbLayMau;
    private String soBbLayMau;
    private LocalDate ngayLayMau;
    private Long idKho;
    private String maDiemKho;
    private String diaDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private BigDecimal soLuong;
    private String loaiHinhNx;
    private String kieuNhapXuat;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenHangHoa;
    private String donViTinh;
    private String hinhThucBaoQuan;
    private LocalDate ngayKiemNghiemMau;
    private Long idNguoiKiemNghiem;
    private Long idThuKho;
    private Long idTruongPhongKtvbq;
    private Long idLanhDaoCuc;
    private String ketQua;
    private String nhanXet;
    private Long idTinhKho;
    private String soBbTinhKho;
    private LocalDate ngayLapTinhKho;
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
    private String maDviCon;
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
    private String tenHinhThucBaoQuan;
    @Transient
    private String tenNguoiKiemNghiem;
    @Transient
    private String tenThuKho;
    @Transient
    private String tenTruongPhongKtvbq;
    @Transient
    private String tenLanhDaoCuc;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenDviCon;
    @Transient
    private BigDecimal soLuongHangbaoQuan;

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucDvi;

    public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
        this.mapDmucDvi = mapDmucDvi;
        if (!DataUtils.isNullObject(getMaDvi())) {
            setTenDvi(mapDmucDvi.containsKey(getMaDvi()) ? mapDmucDvi.get(getMaDvi()) : null);
        }
        if (!DataUtils.isNullObject(getMaDviCon())) {
            setTenDviCon(mapDmucDvi.containsKey(getMaDviCon()) ? mapDmucDvi.get(getMaDviCon()) : null);
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

    @JsonIgnore
    @Transient
    private Map<String, String> mapHinhThucBaoQuan;

    public void setMapHinhThucBaoQuan(Map<String, String> mapHinhThucBaoQuan) {
        this.mapHinhThucBaoQuan = mapHinhThucBaoQuan;
        if (!DataUtils.isNullObject(getHinhThucBaoQuan())) {
            setTenHinhThucBaoQuan(mapHinhThucBaoQuan.containsKey(getHinhThucBaoQuan()) ? mapHinhThucBaoQuan.get(getHinhThucBaoQuan()) : null);
        }
    }

    public String getTrangThai() {
        setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
        return trangThai;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhPhieuKnghiemCluong.TABLE_NAME + "_DINH_KEM'")
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();

    public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem) {
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            fileDinhKem.forEach(s -> {
                s.setDataType(XhPhieuKnghiemCluong.TABLE_NAME + "_DINH_KEM");
                s.setXhPhieuKnghiemCluong(this);
            });
            this.fileDinhKem.addAll(fileDinhKem);
        }
    }

    @Transient
    private List<XhPhieuKnghiemCluongCt> children = new ArrayList<>();
}