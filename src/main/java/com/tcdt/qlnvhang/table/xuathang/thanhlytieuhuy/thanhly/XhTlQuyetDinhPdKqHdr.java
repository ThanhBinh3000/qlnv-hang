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

    @Transient
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();

    @Transient
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();

    @Transient
    private List<XhTlHopDongHdr> listHopDong = new ArrayList<>();

    @Transient
    private XhTlToChucHdr xhTlToChucHdr;

    @Transient
    private XhTlQuyetDinhHdr xhTlQuyetDinhHdr;
}
