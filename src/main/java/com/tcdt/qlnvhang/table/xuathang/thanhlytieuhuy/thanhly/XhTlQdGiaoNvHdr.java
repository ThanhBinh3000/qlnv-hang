package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Getter;
import lombok.Setter;
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
@Table(name = XhTlQdGiaoNvHdr.TABLE_NAME)
@Getter
@Setter
public class XhTlQdGiaoNvHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_QD_GIAO_NV_HDR";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlQdGiaoNvHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlQdGiaoNvHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlQdGiaoNvHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String soBbQd;
    private LocalDate ngayKy;
    private LocalDate ngayKyQd;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private String maDviTsan;
    private String toChucCaNhan;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenVthh;
    private BigDecimal soLuong;
    private String donViTinh;
    private LocalDate thoiGianGiaoNhan;
    private String loaiHinhNx;
    private String kieuNx;
    private String trichYeu;
    private String lyDoTuChoi;
    private String trangThai;
    private String trangThaiXh;
    private Long idBbTinhKho;
    private String soBbTinhKho;
    private Long idBbHaoDoi;
    private String soBbHaoDoi;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;

    @Transient
    private String tenDvi;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenLoaiHinhNx;
    @Transient
    private String tenKieuNx;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenTrangThaiXh;
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

    public String getTrangThaiXh() {
        setTenTrangThaiXh(TrangThaiAllEnum.getLabelById(trangThaiXh));
        return trangThaiXh;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhTlQdGiaoNvHdr.TABLE_NAME + "_CAN_CU'")
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();

    public void setFileCanCu(List<FileDinhKemJoinTable> fileCanCu) {
        this.fileCanCu.clear();
        if (!DataUtils.isNullObject(fileCanCu)) {
            fileCanCu.forEach(f -> {
                f.setDataType(XhTlQdGiaoNvHdr.TABLE_NAME + "_CAN_CU");
                f.setXhTlQdGiaoNvHdr(this);
            });
            this.fileCanCu.addAll(fileCanCu);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhTlQdGiaoNvHdr.TABLE_NAME + "_DINH_KEM'")
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();

    public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem) {
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            fileDinhKem.forEach(s -> {
                s.setDataType(XhTlQdGiaoNvHdr.TABLE_NAME + "_DINH_KEM");
                s.setXhTlQdGiaoNvHdr(this);
            });
            this.fileDinhKem.addAll(fileDinhKem);
        }
    }

    @OneToMany(mappedBy = "quyetDinhHdr", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<XhTlQdGiaoNvDtl> quyetDinhDtl = new ArrayList<>();

    public void setQuyetDinhDtl(List<XhTlQdGiaoNvDtl> quyetDinhDtl) {
        this.getQuyetDinhDtl().clear();
        if (!DataUtils.isNullOrEmpty(quyetDinhDtl)) {
            quyetDinhDtl.forEach(f -> {
                f.setId(null);
                f.setQuyetDinhHdr(this);
            });
            this.quyetDinhDtl.addAll(quyetDinhDtl);
        }
    }
}