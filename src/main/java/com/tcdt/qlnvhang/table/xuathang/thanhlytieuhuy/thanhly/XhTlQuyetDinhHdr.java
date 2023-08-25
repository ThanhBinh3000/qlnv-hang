package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
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
@Table(name = XhTlQuyetDinhHdr.TABLE_NAME)
@Data
public class XhTlQuyetDinhHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_QUYET_DINH_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlQuyetDinhHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlQuyetDinhHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = XhTlQuyetDinhHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private String maDvi;
    private Integer nam;
    private String soQd;
    private LocalDate ngayKy;
    private Long idHoSo;
    private String soHoSo;
    private Long idKq;
    private String soKq;
    private LocalDate thoiGianTlTu;
    private LocalDate thoiGianTlDen;
    private String trichYeu;
    private String trangThai;
    private BigDecimal tongSoLuongTl;
    private BigDecimal tongSoLuongCon;
    private BigDecimal tongThanhTien;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private String lyDoTuChoi;
    @Transient
    private String tenDvi;
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

    public String getTrangThai() {
        setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
        return trangThai;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhTlQuyetDinhHdr.TABLE_NAME + "_DINH_KEM'")
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();

    public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem){
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            fileDinhKem.forEach(f ->{
                f.setDataType(XhTlQuyetDinhHdr.TABLE_NAME + "_DINH_KEM");
                f.setXhTlQuyetDinhHdr(this);
            });
            this.fileDinhKem.addAll(fileDinhKem);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhTlQuyetDinhHdr.TABLE_NAME + "_CAN_CU'")
    private List<FileDinhKemJoinTable> canCu = new ArrayList<>();

    public void setCanCu(List<FileDinhKemJoinTable> canCu){
        this.canCu.clear();
        if (!DataUtils.isNullObject(canCu)) {
            canCu.forEach(f ->{
                f.setDataType(XhTlQuyetDinhHdr.TABLE_NAME + "_CAN_CU");
                f.setXhTlQuyetDinhHdr(this);
            });
            this.canCu.addAll(canCu);
        }
    }

    @OneToMany(mappedBy = "quyetDinhHdr", cascade = CascadeType.ALL)
    private List<XhTlQuyetDinhDtl> quyetDinhDtl = new ArrayList<>();
}
