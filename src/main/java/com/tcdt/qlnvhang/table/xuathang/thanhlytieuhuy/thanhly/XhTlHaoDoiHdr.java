package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.FileDKemJoinHoSoKyThuatDtl;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.olap4j.impl.ArrayMap;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = XhTlHaoDoiHdr.TABLE_NAME)
@Data
public class XhTlHaoDoiHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_HAO_DOI_HDR";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlHaoDoiHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlHaoDoiHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlHaoDoiHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhNs;
    private String soBbHaoDoi;
    private LocalDate ngayLapBienBan;
    private Long idBbQd;
    private String soBbQd;
    private LocalDate ngayKyBbQd;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private String maDiaDiem;
    private Long idBbTinhKho;
    private String soBbTinhKho;
    private BigDecimal tongSlNhap;
    private LocalDate tgianKetThucNhap;
    private BigDecimal tongSlXuat;
    private BigDecimal tgianKetThucXuat;
    private BigDecimal slHaoThucTe;
    private String tiLeHaoThucTe;
    private BigDecimal slHaoVuotMuc;
    private String tiLeHaoVuotMuc;
    private BigDecimal slHaoThanhLy;
    private String tiLeHaoThanhLy;
    private BigDecimal slHaoDuoiDmuc;
    private String tiLeHaoDuoiDmuc;
    private String nguyenNhan;
    private String kienNghi;
    private String ghiChu;
    private String donViTinh;
    private String trangThai;
    private String lyDoTuChoi;
    private String thuKho;
    private Long idKtvBaoQuan;
    private LocalDate ngayPduyetKtvBq;
    private Long idKeToan;
    private LocalDate ngayPduyetKt;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;

    //@Transient
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
    private String tenKtvBaoQuan;
    @Transient
    private String tenKeToan;
    @Transient
    private String tenLanhDaoChiCuc;

    @Transient
    private String tenTrangThai;

    public String getTrangThai() {
        setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
        return trangThai;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhTlHaoDoiHdr.TABLE_NAME + "_DINH_KEM'")
    private List<FileDKemJoinHoSoKyThuatDtl> fileDinhKem = new ArrayList<>();

    public void setFileDinhKem(List<FileDKemJoinHoSoKyThuatDtl> fileDinhKem) {
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            fileDinhKem.forEach(s -> {
                s.setDataType(XhTlHaoDoiHdr.TABLE_NAME + "_DINH_KEM");
                s.setXhTlHaoDoiHdr(this);
            });
            this.fileDinhKem.addAll(fileDinhKem);
        }
    }

    @OneToMany(mappedBy = "haoDoiHdr", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<XhTlHaoDoiDtl> haoDoiDtl = new ArrayList<>();

    public void setHaoDoiDtl(List<XhTlHaoDoiDtl> haoDoiDtl) {
        this.getHaoDoiDtl().clear();
        if (!DataUtils.isNullObject(haoDoiDtl)) {
            haoDoiDtl.forEach(f -> {
                f.setId(null);
                f.setHaoDoiHdr(this);
            });
            this.haoDoiDtl.addAll(haoDoiDtl);
        }
    }
}