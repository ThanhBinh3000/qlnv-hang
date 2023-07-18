package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

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
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlHopDongHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlHopDongHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = XhTlHopDongHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private Long idQdKqTl;
    private String soQdKqTl;
    private LocalDate ngayKyQdKqTl;
    private String soQdTl;
    private String ToChucCaNhan;
    private String maDviTsan;
    private LocalDate thoiHanXuatKho;
    private String loaiHinhNx;
    private String kieuNx;
    private Long idHd;
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
    private String stkBanBan;
    private String moTaiBenBan;
    private String thongTinUyQuyen;
    private String dviBenMua;
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
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    @Transient
    private String tenDvi;
    @Transient
    private String tenTrangThai;
    @JsonIgnore
    @Transient
    private Map<String, String> mapVthh;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;

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
    private List<FileDinhKem> fileCanCu = new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileDinhKem = new ArrayList<>();
    @OneToMany(mappedBy = "hopDongHdr", cascade = CascadeType.ALL)
    private List<XhTlHopDongDtl> hopDongDtl = new ArrayList<>();

}
