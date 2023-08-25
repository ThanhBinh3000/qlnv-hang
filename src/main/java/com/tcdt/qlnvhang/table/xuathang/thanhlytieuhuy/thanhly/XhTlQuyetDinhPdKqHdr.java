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
@Table(name = XhTlQuyetDinhPdKqHdr.TABLE_NAME)
@Getter
@Setter
public class XhTlQuyetDinhPdKqHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_QUYET_DINH_PD_KQ_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlQuyetDinhPdKqHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlQuyetDinhPdKqHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlQuyetDinhPdKqHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private String maDvi;
    private Integer nam;
    private String soQd;
    private String trichYeu;
    private LocalDate ngayKy;
    private LocalDate ngayHieuLuc;
    private String loaiHinhNhapXuat;
    private String kieuNhapXuat;
    private Long idThongBao;
    private String maThongBao;
    private String soBienBan;
    private String thongBaoKhongThanh;
    private String loaiVthh;
    private String cloaiVthh;
    private String pthucGnhan;
    private String thoiHanGiaoNhan;
    private String thoiHanGiaoNhanGhiChu;
    private String ghiChu;
    private String trangThai;
    private String lyDoTuChoi;
    private Long nguoiKyQdId;
    private LocalDate ngayKyQd;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String hthucDgia;
    private String pthucDgia;

    //  Hợp đồng
    private Long idQdTl;
    private String soQdTl;
    private Integer tongSoDviTsan;
    private Integer soDviTsanThanhCong;
    private BigDecimal tongSlXuatBan;
    private BigDecimal thanhTien;
    private String trangThaiHd;
    private String trangThaiXh;

    @Transient
    private String tenLoaiHinhNx;
    @Transient
    private String tenKieuNx;
    @Transient
    private String tenHthucDgia;
    @Transient
    private String tenPthucDgia;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenDvi;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenTrangThaiHd;
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

    public String getTrangThaiHd() {
        setTenTrangThaiHd(TrangThaiAllEnum.getLabelById(trangThaiHd));
        return trangThaiHd;
    }

    public String getTrangThaiXh() {
        setTenTrangThaiXh(TrangThaiAllEnum.getLabelById(trangThaiXh));
        return trangThaiXh;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhTlQuyetDinhPdKqHdr.TABLE_NAME + "_DINH_KEM'")
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();

    public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem) {
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            fileDinhKem.forEach(s -> {
                s.setDataType(XhTlQuyetDinhPdKqHdr.TABLE_NAME + "_DINH_KEM");
                s.setXhTlQuyetDinhPdKqHdr(this);
            });
            this.fileDinhKem.addAll(fileDinhKem);
        }
    }


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhTlQuyetDinhPdKqHdr.TABLE_NAME + "_CAN_CU'")
    private List<FileDinhKemJoinTable> canCu = new ArrayList<>();

    public void setCanCu(List<FileDinhKemJoinTable> fileDinhKem) {
        this.canCu.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            fileDinhKem.forEach(s -> {
                s.setDataType(XhTlQuyetDinhPdKqHdr.TABLE_NAME + "_CAN_CU");
                s.setXhTlQuyetDinhPdKqHdr(this);
            });
            this.canCu.addAll(fileDinhKem);
        }
    }

    @OneToMany(mappedBy = "quyetDinhHdr", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<XhTlQuyetDinhPdKqDtl> quyetDinhDtl = new ArrayList<>();

    public void setQuyetDinhDtl(List<XhTlQuyetDinhPdKqDtl> quyetDinhDtl) {
        this.getQuyetDinhDtl().clear();
        if (!DataUtils.isNullOrEmpty(quyetDinhDtl)) {
            quyetDinhDtl.forEach(s -> {
                s.setId(null);
                s.setQuyetDinhHdr(this);
            });
            this.quyetDinhDtl.addAll(quyetDinhDtl);
        }
    }

    @Transient
    private List<XhTlHopDongHdr> listHopDong;
}
