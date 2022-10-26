package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HH_HDONG_BKE_PMUAHANG_HDR")
@Data
public class HhHdongBkePmuahangHdr implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_HDONG_BKE_PMUAHANG_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_HDONG_BKE_PMUAHANG_SEQ")
    @SequenceGenerator(sequenceName = "HH_HDONG_BKE_PMUAHANG_SEQ", allocationSize = 1, name = "HH_HDONG_BKE_PMUAHANG_SEQ")
    private Long id;
    private Integer  namHd;
    private Long idQdCgia;
    private String soQdCgia;
    @Temporal(TemporalType.DATE)
    private Date ngayKyQdCgia;
    @Temporal(TemporalType.DATE)
    private Date tgianKthuc;
    private String dviCcap;
    private Long idQdMtt;
    private String soQdMtt;
    private String soHdong;
    private String tenHdong;
    @Temporal(TemporalType.DATE)
    private Date ngayHluc;
    private String ghiChuNgayHd;
    private String loaiHdong;
    private String ghiChuLoaiHd;
    private Integer tgianThucHien;
    @Temporal(TemporalType.DATE)
    private Date tgianNhanHangTu;
    @Temporal(TemporalType.DATE)
    private Date tgianNhanHangDen;
    private String ghiChuNhanHang;
    private String noiDung;
    private String dieuKien;
    @Temporal(TemporalType.DATE)
    private Date ngayTao;
    private String nguoiTao;
    private Date ngaySua;
    private  String nguoiSua;
    @Temporal(TemporalType.DATE)
    private Date ngayKy;
    private  String nguoiKy;
    private String maDvi;
    @Transient
    private String tenDvi;

    private String trangThaiHd;
    @Transient
    private String tenTrangThaiHd;
    private String trangThaiNh;
    @Transient
    private String tentrangThaiNh;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String moTaHangHoa;
    private String donViTinh;
    private BigDecimal soLuong;
    private BigDecimal giaCoThue;
    private BigDecimal thanhTien;
    private String ghiChu;
    private BigDecimal soLuongTheoQdpdKh;

    @Transient
    private String benBan;
    @Transient
    private String benMua;
    @Transient
    private BigDecimal giaTriHd;
    @Transient
    private String dDiemBmua;

    @Transient
    private List<FileDinhKem> FileDinhKems =new ArrayList<>();

    @Transient
    private List<FileDinhKem> canCuPhapLy = new ArrayList<>();

    @Transient
    private List<HhThongTinDviDtuCcap> thongTinChuDauTu;

    @Transient
    private List<HhThongTinDviDtuCcap> thongTinDviCungCap;

    @Transient
    private List<HhDiaDiemGiaoNhanHang> diaDiemGiaoNhanHangList=new ArrayList<>();

}
