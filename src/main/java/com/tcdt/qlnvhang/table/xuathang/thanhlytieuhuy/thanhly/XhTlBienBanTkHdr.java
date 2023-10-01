package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBienBanKtDd;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScDanhSachHdr;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = XhTlBienBanTkHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XhTlBienBanTkHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_BIEN_BAN_KT_HDR";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScBienBanKtHdr.TABLE_NAME + "_SEQ")
//    @SequenceGenerator(sequenceName = ScBienBanKtHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScBienBanKtHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhns;
    private String soBienBan;
    private LocalDate ngayLap;
    private String soQdNh;
    private Long idQdNh;
    private Long idScDanhSachHdr;
    private String maDiaDiem;
    private String loaiVthh;
    private String cloaiVthh;
    private Long idThuKho;
    private Long idLanhDaoCc;
    private String lyDoTuChoi;
    private String trangThai;
    private String ghiChu;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private BigDecimal tongSoLuong;
    @Transient
    private List<XhTlBienBanTkDtl> children = new ArrayList<>();
    @Transient
    private List<ScBienBanKtDd> daiDienList = new ArrayList<>();
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenDvi;
    @Transient
    private String tenThuKho;
    @Transient
    private String tenLanhDaoCc;
    @JsonIgnore
    @Transient
    private Map<String, String> mapVthh;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucDvi;
    @Transient
    private String tenCuc;
    @Transient
    private String tenChiCuc;
    @Transient
    private String tenDiemKho;
    @Transient
    private String tenNhaKho;
    @Transient
    private String tenNganKho;
    @Transient
    private String tenLoKho;
    @Transient
    private ScDanhSachHdr scDanhSachHdr;
    @Transient
    private List<FileDinhKem> fileDinhKem = new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileCanCu = new ArrayList<>();

    public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
        this.mapDmucDvi = mapDmucDvi;
        if (!DataUtils.isNullObject(getMaDiaDiem())) {
            String maCuc = getMaDiaDiem().length() >= 6 ? getMaDiaDiem().substring(0, 6) : "";
            String maChiCuc = getMaDiaDiem().length() >= 8 ? getMaDiaDiem().substring(0, 8) : "";
            String maDiemKho = getMaDiaDiem().length() >= 10 ? getMaDiaDiem().substring(0, 10) : "";
            String maNhaKho = getMaDiaDiem().length() >= 12 ? getMaDiaDiem().substring(0, 12) : "";
            String maNganKho = getMaDiaDiem().length() >= 14 ? getMaDiaDiem().substring(0, 14) : "";
            String maLoKho = getMaDiaDiem().length() >= 16 ? getMaDiaDiem().substring(0, 16) : "";
            String tenCuc = mapDmucDvi.containsKey(maCuc) ? mapDmucDvi.get(maCuc) : null;
            String tenChiCuc = mapDmucDvi.containsKey(maChiCuc) ? mapDmucDvi.get(maChiCuc) : null;
            String tenDiemKho = mapDmucDvi.containsKey(maDiemKho) ? mapDmucDvi.get(maDiemKho) : null;
            String tenNhaKho = mapDmucDvi.containsKey(maNhaKho) ? mapDmucDvi.get(maNhaKho) : null;
            String tenNganKho = mapDmucDvi.containsKey(maNganKho) ? mapDmucDvi.get(maNganKho) : null;
            String tenLoKho = mapDmucDvi.containsKey(maLoKho) ? mapDmucDvi.get(maLoKho) : null;
            setTenCuc(tenCuc);
            setTenChiCuc(tenChiCuc);
            setTenDiemKho(tenDiemKho);
            setTenNhaKho(tenNhaKho);
            setTenNganKho(tenNganKho);
            setTenLoKho(tenLoKho);
        }
    }

    public void setMapVthh(Map<String, String> mapVthh) {
        this.mapVthh = mapVthh;
        if (!DataUtils.isNullObject(getLoaiVthh())) {
            setTenLoaiVthh(mapVthh.containsKey(getLoaiVthh()) ? mapVthh.get(getLoaiVthh()) : null);
        }
        if (!DataUtils.isNullObject(getCloaiVthh())) {
            setTenCloaiVthh(mapVthh.containsKey(getCloaiVthh()) ? mapVthh.get(getCloaiVthh()) : null);
        }
    }

    public String getTenTrangThai(){
        return TrangThaiAllEnum.getLabelById(getTrangThai());
    }
}
