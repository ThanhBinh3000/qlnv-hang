package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
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
@Table(name = XhTlHopDongHdr.TABLE_NAME)
@Data
public class XhTlHopDongHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_HOP_DONG_HDR";
    public static final String FILE_DINH_KEM = "XH_TL_HOP_DONG_HDR_DK";
    public static final String FILE_CAN_CU = "XH_TL_HOP_DONG_HDR_CC";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlHopDongHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlHopDongHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = XhTlHopDongHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private Long idQdNv;
    private String soQdNv;
    private Long idQdKqTl;
    private String soQdKqTl;
    private LocalDate ngayKyQdkqTl;
    private String soQdTl;
    private String toChucCaNhan;
    private String maDviTsan;
    private LocalDate thoiHanXuatKho;
    private String loaiHinhNx;
    private String kieuNx;
    private String soHd;
    private String tenHd;
    private LocalDate ngayHieuLuc;
    private String ghiChuNgayHluc;
    private String loaiHdong;
    private String ghiChuLoaiHdong;
    private Integer tgianThienHd;
    private Integer tgianBhanh;
    private String diaChiBenBan;
    private String mstBenBan;
    private String daiDienBenBan;
    private String chucVuBenBan;
    private String sdtBenBan;
    private String faxBenBan;
    private String stkBenBan;
    private String moTaiBenBan;
    private String thongTinUyQuyen;
    private String tenDviBenMua;
    private String diaChiBenMua;
    private String mstBenMua;
    private String daiDienBenMua;
    private String chucVuBenMua;
    private String sdtBenMua;
    private String faxBenMua;
    private String stkBenMua;
    private String moTaiBenMua;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String donViTinh;
    private BigDecimal soLuong;
    private BigDecimal thanhTien;
    private String ghiChu;
    private String trangThai;
    private String trangThaiXh;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    @Transient
    private String tenDvi;
    @JsonIgnore
    @Transient
    private Map<String, String> mapVthh;
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
    @Transient
    private List<String> listMaDviTsan = new ArrayList<>();

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
    private Map<String, String> mapDmucDvi;

    public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
        this.mapDmucDvi = mapDmucDvi;
        if (!DataUtils.isNullObject(getMaDvi())) {
            setTenDvi(mapDmucDvi.containsKey(getMaDvi()) ? mapDmucDvi.get(getMaDvi()) : null);
        }
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }

    public void setTrangThaiXh(String trangThaiXh) {
        this.trangThaiXh = trangThaiXh;
        this.tenTrangThaiXh = TrangThaiAllEnum.getLabelById(this.trangThaiXh);
    }

    @Transient
    private List<FileDinhKem> fileCanCu = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKem = new ArrayList<>();

    @Transient
    private List<XhTlHopDongDtl> children = new ArrayList<>();

}
