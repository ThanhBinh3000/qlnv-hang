package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;
import org.olap4j.impl.ArrayMap;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = XhTlBbLayMauHdr.TABLE_NAME)
@Data
public class XhTlBbLayMauHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_BB_LAY_MAU_HDR";
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlBbLayMauHdr.TABLE_NAME + "_SEQ")
//    @SequenceGenerator(sequenceName = XhTlBbLayMauHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlBbLayMauHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private String loaiBienBan;
    private Integer nam;
    private String maDvi;
    private String maQhns;
    private String soBienBan;
    private String ngayLayMau;
    private Long idQdXh;
    private String soQdXh;
    private String dviKnghiem;
    private String diaDiemLayMau;
    private String idDiaDiemXh;
    private String maDiaDiem;
    private String loaiVthh;
    private String cloaiVthh;
    private Long idThuKho;
    private Long idKtv;
    private String truongBpKtbq;
    private Long idLdcc;
    private BigDecimal soLuongMau;
    private Boolean ketQuaNiemPhong;
    private String ghiChu;
    private String trangThai;
    private String lyDoTuChoi;
    @Transient
    private List<XhTlBbLayMauDtl> children = new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileDinhKem = new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileCanCu = new ArrayList<>();


    //@Transient
    @Transient
    private String tenLdcc;
    @Transient
    private String tenKtv;
    @Transient
    private String tenDvi;
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
    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucDvi = new ArrayMap<>();

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
            String tenDvi = mapDmucDvi.containsKey(maDvi) ? mapDmucDvi.get(maDvi) : null;
            setTenCuc(tenCuc);
            setTenChiCuc(tenChiCuc);
            setTenDiemKho(tenDiemKho);
            setTenNhaKho(tenNhaKho);
            setTenNganKho(tenNganKho);
            setTenLoKho(tenLoKho);
            setTenDvi(tenDvi);
        }
    }

    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
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

    @Transient
    private String tenNguoiPduyet;

    @Transient
    private String tenTrangThai;

    public String getTenTrangThai() {
        return TrangThaiAllEnum.getLabelById(trangThai);
    }

}