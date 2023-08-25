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
@Table(name = XhTlToChucHdr.TABLE_NAME)
@Data
public class XhTlToChucHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_TO_CHUC_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlToChucHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlToChucHdr.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = XhTlToChucHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private String maDvi;
    private Integer lanDauGia;
    private String maThongBao;
    private Long idQdTl;
    private Long idQdTlDtl;
    private String soQdTl;
    private String trichYeuTbao;
    private String tenToChuc;
    private String sdtToChuc;
    private String diaChiToChuc;
    private String taiKhoanToChuc;
    private String nganHangToChuc;
    private String chiNhanhNhangToChuc;
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
    private BigDecimal khoanTienDatTruoc;
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
    private Integer soDviTsan;
    private String thongBaoKhongThanh;
    private String trangThai;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private Integer thoiHanThanhToan;
    private String pthucThanhtoanKhac;
    private Integer thoiHanGiaoNhan;
    private String thoiHanGiaoNhanGhiChu;
    private String pthucGnhan;
    private Long idQdPdKq;
    private String soQdPdKq;
    private Long idHopDong;
    private String soHopDong;
    private Long idQdGnv;
    private String soQdGnv;

    //  @Transient
    @Transient
    private String tenDvi;
    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucDvi;

    public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
        this.mapDmucDvi = mapDmucDvi;
        if (!DataUtils.isNullObject(getMaDvi())) {
            setTenDvi(mapDmucDvi.containsKey(getMaDvi()) ? mapDmucDvi.get(getMaDvi()) : null);
        }
    }

    @Transient
    private String tenTrangThai;

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhTlToChucHdr.TABLE_NAME + "_CAN_CU'")
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();

    public void setFileCanCu(List<FileDinhKemJoinTable> fileCanCu) {
        this.fileCanCu.clear();
        if (!DataUtils.isNullObject(fileCanCu)) {
            fileCanCu.forEach(f -> {
                f.setDataType(XhTlToChucHdr.TABLE_NAME + "_CAN_CU");
                f.setXhTlToChucHdr(this);
            });
            this.fileCanCu.addAll(fileCanCu);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhTlToChucHdr.TABLE_NAME + "_DINH_KEM'")
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();

    public void setFileDinhKem(List<FileDinhKemJoinTable> fileCanCu) {
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileCanCu)) {
            fileCanCu.forEach(f -> {
                f.setDataType(XhTlToChucHdr.TABLE_NAME + "_DINH_KEM");
                f.setXhTlToChucHdr(this);
            });
            this.fileDinhKem.addAll(fileCanCu);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhTlToChucHdr.TABLE_NAME + "_DA_KY'")
    private List<FileDinhKemJoinTable> fileDinhKemDaKy = new ArrayList<>();

    public void setFileDinhKemDaKy(List<FileDinhKemJoinTable> fileCanCu) {
        this.fileDinhKemDaKy.clear();
        if (!DataUtils.isNullObject(fileCanCu)) {
            fileCanCu.forEach(f -> {
                f.setDataType(XhTlToChucHdr.TABLE_NAME + "_DA_KY");
                f.setXhTlToChucHdr(this);
            });
            this.fileDinhKemDaKy.addAll(fileCanCu);
        }
    }

    @OneToMany(mappedBy = "toChucHdr", cascade = CascadeType.ALL)
    private List<XhTlToChucDtl> toChucDtl = new ArrayList<>();

    @OneToMany(mappedBy = "toChucHdr", cascade = CascadeType.ALL)
    private List<XhTlToChucNlq> toChucNlq = new ArrayList<>();
}
