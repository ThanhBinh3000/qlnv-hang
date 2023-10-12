package com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = XhHopDongBttHdr.TABLE_NAME)
@Data
public class XhHopDongBttHdr implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_HOP_DONG_BTT_HDR";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = TABLE_NAME + "_SEQ", allocationSize = 1, name = TABLE_NAME + "_SEQ")
    private Long id;
    private String maDvi;
    private Integer namHd;
    private String loaiHinhNx;
    private String kieuNx;
    private Long idQdKq;
    private String soQdKq;
    private LocalDate ngayKyQdKq;
    private LocalDate thoiHanXuatKho;
    private Long idQdPd;
    private String soQdPd;
    private LocalDate ngayKyUyQuyen;
    private String maDviTsan;
    private String soHopDong;
    private String tenHopDong;
    private LocalDate ngayKyHopDong;
    private LocalDate ngayHlucHopDong;
    private String ghiChuNgayHluc;
    private String loaiHopDong;
    private String ghiChuLoaiHdong;
    private Integer tgianThienHdongNgay;
    private LocalDate tgianThienHdong;
    private LocalDate tgianGiaoNhanTu;
    private LocalDate tgianGiaoNhanDen;
    private String ghiChuTgianGiaoNhan;
    private Integer thoiGianBaoHanh;
    private BigDecimal giaTri;
    private LocalDate tgianBaoDamHdong;
    private String ghiChuBaoDam;
    private String dieuKien;
    private String diaChiBenBan;
    private String mstBenBan;
    private String tenNguoiDaiDien;
    private String chucVuBenBan;
    private String sdtBenBan;
    private String faxBenBan;
    private String stkBenBan;
    private String moTaiBenBan;
    private String giayUyQuyen;
    private Long idBenMua;
    private String tenBenMua;
    private String diaChiBenMua;
    private String mstBenMua;
    private String tenNguoiDdienMua;
    private String chucVuBenMua;
    private String sdtBenMua;
    private String faxBenMua;
    private String stkBenMua;
    private String moTaiBenMua;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenHangHoa;
    private String donViTinh;
    private BigDecimal soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private BigDecimal slXuatBanQdPd;
    private BigDecimal slXuatBanKyHdong;
    private BigDecimal slXuatBanChuaKyHdong;
    private String moTaHangHoa;
    private String ghiChu;
    private Long idChaoGia;
    private Long idQdNv;
    private String soQdNv;
    private String trangThai;
    private String trangThaiXh;
    private LocalDate ngayTao;
    private Long nguoiTaoId;
    private LocalDate ngaySua;
    private Long nguoiSuaId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    @Transient
    private String tenDvi;
    @Transient
    private String tenLoaiHinhNx;
    @Transient
    private String tenKieuNx;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
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
    private Map<String, String> mapLoaiHinhNx;

    public void setMapLoaiHinhNx(Map<String, String> mapLoaiHinhNx) {
        this.mapLoaiHinhNx = mapLoaiHinhNx;
        if (!DataUtils.isNullObject(getLoaiHinhNx())) {
            setTenLoaiHinhNx(mapLoaiHinhNx.containsKey(getLoaiHinhNx()) ? mapLoaiHinhNx.get(getLoaiHinhNx()) : null);
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapKieuNx;

    public void setMapKieuNx(Map<String, String> mapKieuNx) {
        this.mapKieuNx = mapKieuNx;
        if (!DataUtils.isNullObject(getKieuNx())) {
            setTenKieuNx(mapKieuNx.containsKey(getKieuNx()) ? mapKieuNx.get(getKieuNx()) : null);
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

    @Transient
    private List<String> listMaDviTsan = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhHopDongBttHdr.TABLE_NAME + "_CAN_CU'")
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();

    public void setFileCanCu(List<FileDinhKemJoinTable> fileCanCu) {
        this.fileCanCu.clear();
        if (!DataUtils.isNullObject(fileCanCu)) {
            fileCanCu.forEach(s -> {
                s.setDataType(XhHopDongBttHdr.TABLE_NAME + "_CAN_CU");
                s.setXhHopDongBttHdr(this);
            });
            this.fileCanCu.addAll(fileCanCu);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhHopDongBttHdr.TABLE_NAME + "_DINH_KEM'")
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();

    public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem) {
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            fileDinhKem.forEach(s -> {
                s.setDataType(XhHopDongBttHdr.TABLE_NAME + "_DINH_KEM");
                s.setXhHopDongBttHdr(this);
            });
            this.fileDinhKem.addAll(fileDinhKem);
        }
    }

    //Cấp cục
    @Transient
    private List<XhHopDongBttDtl> children = new ArrayList<>();
    //Cấp chi cục
    @Transient
    private List<XhHopDongBttDvi> xhHopDongBttDviList = new ArrayList<>();

    //Phụ lục
    private Long idHd;
    private String soPhuLuc;
    private LocalDate ngayHlucPhuLuc;
    private String noiDungPhuLuc;
    private LocalDate ngayHlucSauDcTu;
    private LocalDate ngayHlucSauDcDen;
    private Integer tgianThienHdSauDc;
    private String noiDungDcKhac;
    private String ghiChuPhuLuc;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhHopDongBttHdr.TABLE_NAME + "_PHU_LUC'")
    private List<FileDinhKemJoinTable> filePhuLuc = new ArrayList<>();

    public void setFilePhuLuc(List<FileDinhKemJoinTable> filePhuLuc) {
        this.filePhuLuc.clear();
        if (!DataUtils.isNullObject(filePhuLuc)) {
            filePhuLuc.forEach(s -> {
                s.setDataType(XhHopDongBttHdr.TABLE_NAME + "_PHU_LUC");
                s.setXhHopDongBttHdr(this);
            });
            this.filePhuLuc.addAll(filePhuLuc);
        }
    }

    @Transient
    private List<XhHopDongBttHdr> phuLuc = new ArrayList<>();
    @Transient
    private List<XhHopDongBttDtl> phuLucDtl = new ArrayList<>();
}