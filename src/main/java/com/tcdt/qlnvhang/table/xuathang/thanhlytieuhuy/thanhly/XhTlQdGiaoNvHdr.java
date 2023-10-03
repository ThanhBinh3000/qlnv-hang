package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
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
    private Long idHopDong;
    private String soHopDong;
    private String phanLoai;
    private String maDviTsan;
    private String toChucCaNhan;
    private LocalDate thoiGianGiaoNhan;
    private String loaiHinhNx;
    private String kieuNx;
    private String trichYeu;
    private String lyDoTuChoi;
    private String trangThai;
    private String trangThaiXh;
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


    public String getTrangThai() {
        setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
        return trangThai;
    }

    public String getTrangThaiXh() {
        setTenTrangThaiXh(TrangThaiAllEnum.getLabelById(trangThaiXh));
        return trangThaiXh;
    }

    @Transient
    private List<FileDinhKem> fileCanCu = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKem = new ArrayList<>();


    @Transient
    private List<XhTlQdGiaoNvDtl> children = new ArrayList<>();

}