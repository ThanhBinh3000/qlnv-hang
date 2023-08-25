package com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin;

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
@Table(name = XhTcTtinBdgHdr.TABLE_NAME)
@Data
public class XhTcTtinBdgHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TC_TTIN_BDG_HDR";
    @Id
//  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_TC_TTIN_BDG_HDR_SEQ")
//  @SequenceGenerator(sequenceName = "XH_TC_TTIN_BDG_HDR_SEQ", allocationSize = 1, name = "XH_TC_TTIN_BDG_HDR_SEQ")
    private Long id;
    private String maDvi;
    private Integer nam;
    private Integer lanDauGia;
    private String maThongBao;
    private Long idQdPd;
    private String soQdPd;
    private Long idQdPdDtl;
    private String trichYeuTbao;
    private String tenToChuc;
    private String sdtToChuc;
    private String diaChiToChuc;
    private String taiKhoanToChuc;
    private String soHd;
    private LocalDate ngayKyHd;
    private String hthucTchuc;
    private LocalDate tgianDkyTu;
    private LocalDate tgianDkyDen;
    private String ghiChuTgianDky;
    private String diaDiemDky;
    private String dieuKienDky;
    private String tienMuaHoSo;
    private String buocGia;
    private String ghiChuBuocGia;
    private LocalDate tgianXemTu;
    private LocalDate tgianXemDen;
    private String ghiChuTgianXem;
    private String diaDiemXem;
    private LocalDate tgianNopTienTu;
    private LocalDate tgianNopTienDen;
    private String pthucTtoan;
    private String ghiChuTgianNopTien;
    private String donViThuHuong;
    private String stkThuHuong;
    private String nganHangThuHuong;
    private String chiNhanhNganHang;
    private LocalDate tgianDauGiaTu;
    private LocalDate tgianDauGiaDen;
    private String diaDiemDauGia;
    private String hthucDgia;
    private String pthucDgia;
    private String dkienCthuc;
    private String ghiChu;
    private Integer ketQua; // 0 : Trượt 1 Trúng
    private String soBienBan;
    private String trichYeuBban;
    private LocalDate ngayKyBban;
    private String ketQuaSl;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private BigDecimal khoanTienDatTruoc;
    private String thongBaoKhongThanh;
    private Integer soDviTsan;
    private String trangThai;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    @Transient
    private String tenDvi;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
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
    @Where(clause = "data_type='" + XhTcTtinBdgHdr.TABLE_NAME + "_CAN_CU'")
    private List<FileDinhKemJoinTable> canCu = new ArrayList<>();

    public void setCanCu(List<FileDinhKemJoinTable> fileDinhKem) {
        this.canCu.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            fileDinhKem.forEach(s -> {
                s.setDataType(XhTcTtinBdgHdr.TABLE_NAME + "_CAN_CU");
                s.setXhTcTtinBdgHdr(this);
            });
            this.canCu.addAll(fileDinhKem);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhTcTtinBdgHdr.TABLE_NAME + "_DINH_KEM'")
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();

    public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem) {
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            fileDinhKem.forEach(s -> {
                s.setDataType(XhTcTtinBdgHdr.TABLE_NAME + "_DINH_KEM");
                s.setXhTcTtinBdgHdr(this);
            });
            this.fileDinhKem.addAll(fileDinhKem);
        }
    }

    @Transient
    private List<XhTcTtinBdgDtl> children = new ArrayList<>();
    @Transient
    private List<XhTcTtinBdgNlq> listNguoiTgia = new ArrayList<>();
}
