//package com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
//import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtl;
//import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
//import com.tcdt.qlnvhang.util.DataUtils;
//import lombok.Data;
//import org.hibernate.annotations.Fetch;
//import org.hibernate.annotations.FetchMode;
//import org.hibernate.annotations.Where;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@Entity
//@Table(name = XhQdDchinhKhBdgHdr.TABLE_NAME)
//@Data
//public class XhQdDchinhKhBdgHdr implements Serializable {
//    private static final long serialVersionUID = 1L;
//    public static final String TABLE_NAME = "XH_QD_DC_KH_BDG_HDR";
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhQdDchinhKhBdgHdr.TABLE_NAME + "_SEQ")
//    @SequenceGenerator(sequenceName = XhQdDchinhKhBdgHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhQdDchinhKhBdgHdr.TABLE_NAME + "_SEQ")
//    private Long id;
//    private String maDvi;
//    private Integer nam;
//    private String soCongVan;
//    private LocalDate ngayTaoCongVan;
//    private String trichYeu;
//    private Long idQdPd;
//    private String soQdPd;
//    private Integer lanDieuChinh;
//    private LocalDate ngayKyQd;
//    private String soQdCc;
//    private String soQdDc;
//    private String soQdCanDc;
//    private Long idDcGoc;
//    private LocalDate ngayKyDc;
//    private LocalDate ngayHlucDc;
//    private String noiDungDieuChinh;
//    private String loaiHinhNx;
//    private String kieuNx;
//    private String loaiVthh;
//    private String cloaiVthh;
//    private String moTaHangHoa;
//    private String tchuanCluong;
//    private Integer slDviTsan;
//    private Integer slHdongDaKy;
//    private Integer thoiHanGiaoNhan;
//    private String trangThai;
//    private String lyDoTuChoi;
//    private LocalDate ngayTao;
//    private Long nguoiTaoId;
//    private LocalDate ngaySua;
//    private Long nguoiSuaId;
//    private LocalDate ngayGuiDuyet;
//    private Long nguoiGuiDuyetId;
//    private LocalDate ngayPduyet;
//    private Long nguoiPduyetId;
//    @Transient
//    private String tenDvi;
//    @Transient
//    private String tenLoaiHinhNx;
//    @Transient
//    private String tenKieuNx;
//    @Transient
//    private String tenLoaiVthh;
//    @Transient
//    private String tenCloaiVthh;
//    @Transient
//    private String tenTrangThai;
//
//    @JsonIgnore
//    @Transient
//    private Map<String, String> mapDmucDvi;
//
//    public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
//        this.mapDmucDvi = mapDmucDvi;
//        if (!DataUtils.isNullObject(getMaDvi())) {
//            setTenDvi(mapDmucDvi.containsKey(getMaDvi()) ? mapDmucDvi.get(getMaDvi()) : null);
//        }
//    }
//
//    @JsonIgnore
//    @Transient
//    private Map<String, String> mapLoaiHinhNx;
//
//    public void setMapLoaiHinhNx(Map<String, String> mapLoaiHinhNx) {
//        this.mapLoaiHinhNx = mapLoaiHinhNx;
//        if (!DataUtils.isNullObject(getLoaiHinhNx())) {
//            setTenLoaiHinhNx(mapLoaiHinhNx.containsKey(getLoaiHinhNx()) ? mapLoaiHinhNx.get(getLoaiHinhNx()) : null);
//        }
//    }
//
//    @JsonIgnore
//    @Transient
//    private Map<String, String> mapKieuNx;
//
//    public void setMapKieuNx(Map<String, String> mapKieuNx) {
//        this.mapKieuNx = mapKieuNx;
//        if (!DataUtils.isNullObject(getKieuNx())) {
//            setTenKieuNx(mapKieuNx.containsKey(getKieuNx()) ? mapKieuNx.get(getKieuNx()) : null);
//        }
//    }
//
//    @JsonIgnore
//    @Transient
//    private Map<String, String> mapVthh;
//
//    public void setMapVthh(Map<String, String> mapVthh) {
//        this.mapVthh = mapVthh;
//        if (!DataUtils.isNullObject(getLoaiVthh())) {
//            setTenLoaiVthh(mapVthh.containsKey(getLoaiVthh()) ? mapVthh.get(getLoaiVthh()) : null);
//        }
//        if (!DataUtils.isNullObject(getCloaiVthh())) {
//            setTenCloaiVthh(mapVthh.containsKey(getCloaiVthh()) ? mapVthh.get(getCloaiVthh()) : null);
//        }
//    }
//
//    public String getTrangThai() {
//        setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
//        return trangThai;
//    }
//
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @Fetch(value = FetchMode.SUBSELECT)
//    @JoinColumn(name = "dataId")
//    @Where(clause = "data_type='" + XhQdDchinhKhBdgHdr.TABLE_NAME + "_TO_TRINH'")
//    private List<FileDinhKemJoinTable> fileToTrinh = new ArrayList<>();
//
//    public void setFileToTrinh(List<FileDinhKemJoinTable> fileToTrinh) {
//        this.fileToTrinh.clear();
//        if (!DataUtils.isNullObject(fileToTrinh)) {
//            fileToTrinh.forEach(s -> {
//                s.setDataType(XhQdDchinhKhBdgHdr.TABLE_NAME + "_TO_TRINH");
//                s.setXhQdDchinhKhBdgHdr(this);
//            });
//            this.fileToTrinh.addAll(fileToTrinh);
//        }
//    }
//
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @Fetch(value = FetchMode.SUBSELECT)
//    @JoinColumn(name = "dataId")
//    @Where(clause = "data_type='" + XhQdDchinhKhBdgHdr.TABLE_NAME + "_DINH_KEM'")
//    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();
//
//    public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem) {
//        this.fileDinhKem.clear();
//        if (!DataUtils.isNullObject(fileDinhKem)) {
//            fileDinhKem.forEach(s -> {
//                s.setDataType(XhQdDchinhKhBdgHdr.TABLE_NAME + "_DINH_KEM");
//                s.setXhQdDchinhKhBdgHdr(this);
//            });
//            this.fileDinhKem.addAll(fileDinhKem);
//        }
//    }
//
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @Fetch(value = FetchMode.SUBSELECT)
//    @JoinColumn(name = "dataId")
//    @Where(clause = "data_type='" + XhQdDchinhKhBdgHdr.TABLE_NAME + "_CAN_CU'")
//    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();
//
//    public void setFileCanCu(List<FileDinhKemJoinTable> fileCanCu) {
//        this.fileCanCu.clear();
//        if (!DataUtils.isNullObject(fileCanCu)) {
//            fileCanCu.forEach(s -> {
//                s.setDataType(XhQdDchinhKhBdgHdr.TABLE_NAME + "_CAN_CU");
//                s.setXhQdDchinhKhBdgHdr(this);
//            });
//            this.fileCanCu.addAll(fileCanCu);
//        }
//    }
//
//    @Transient
//    List<XhQdPdKhBdgDtl> children = new ArrayList<>();
//}