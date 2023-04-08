package com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong;

import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XH_HOP_DONG_HDR")
@Data
public class XhHopDongHdr extends TrangThaiBaseEntity {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_HOP_DONG_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_HOP_DONG_HDR_SEQ")
    @SequenceGenerator(sequenceName =  "XH_HOP_DONG_HDR_SEQ", allocationSize = 1, name =  "XH_HOP_DONG_HDR_SEQ")
    private Long id;

    private String soQdKq;

    private Integer nam;

    private LocalDate ngayKyQdKq;

    private String soQdPd;

    private String maDviTsan;

    private LocalDate tgianNkho;

    private String soHd;

    private String tenHd;

    private LocalDate ngayHluc;

    private String ghiChuNgayHluc;

    private String loaiHdong;

    private String ghiChuLoaiHdong;

    private Integer tgianThienHd;

    private Integer tgianBhanh;

    private String maDvi;

    private String diaChi;

    private String mst;

    private String tenNguoiDdien;

    private String chucVu;

    private String sdt;

    private String stk;

    private String fax;

    private String moTai;

    private String uyQuyen;

    private String tenNhaThau;

    private String diaChiNhaThau;

    private String mstNhaThau;

    private String tenNguoiDdienNhaThau;

    private String chucVuNhaThau;

    private String sdtNhaThau;

    private String stkNhaThau;

    private String faxNhaThau;

    private String moTaiNhaThau;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private String donViTinh;

    private Double soLuong;

    private Double tongTien;

    private String ghiChu;

    // Transient
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenDvi;
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @Transient
    private List<FileDinhKem> canCu = new ArrayList<>();
    @Transient
    private List<XhHopDongDtl> children = new ArrayList<>();
    @Transient
    private List<String> listMaDviTsan = new ArrayList<>();

    //    Phụ lục

    private Long idHd;

    private String soPhuLuc;

    private LocalDate ngayHlucPhuLuc;

    private String noiDungPhuLuc;

    private LocalDate ngayHlucSauDcTu;

    private LocalDate ngayHlucSauDcDen;

    private Integer tgianThienHdSauDc;

    private String noiDungDcKhac;

    private String ghiChuPhuLuc;

    private String trangThaiPhuLuc;
    @Transient
    private String tenTrangThaiPhuLuc;

    @Transient
    private List<XhHopDongHdr> phuLuc = new ArrayList<>();

    @Transient
    private List<XhHopDongDtl> phuLucDtl = new ArrayList<>();

}
