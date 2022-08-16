package com.tcdt.qlnvhang.table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinBhHopDong;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "BH_HOP_DONG_HDR")
@Data
public class BhHopDongHdr {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "BH_HOP_DONG_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BH_HOP_DONG_HDR_SEQ")
    @SequenceGenerator(sequenceName =  "BH_HOP_DONG_HDR_SEQ", allocationSize = 1, name =  "BH_HOP_DONG_HDR_SEQ")
    private Long id;
    private Long idGoiThau;
    private Long namKh;
    private Long donGiaVat;
    String soHd;
    String tenHd;
    String canCu;
//	String dviTrungThau;

    @Temporal(TemporalType.DATE)
    Date tuNgayHluc;

    @Temporal(TemporalType.DATE)
    Date denNgayHluc;

    Double soNgayThien;

    @Temporal(TemporalType.DATE)
    Date tuNgayTdo;

    @Temporal(TemporalType.DATE)
    Date denNgayTdo;

    Double soNgayTdo;
    String nuocSxuat;
    String tieuChuanCl;
    Double soLuong;
    Double gtriHdTrcVat;
    Double vat;
    Double gtriHdSauVat;
    String loaiVthh;
    @Transient
    String tenVthh;
    String cloaiVthh;
    @Transient
    String tenCloaiVthh;
    String loaiHd;

    @Temporal(TemporalType.DATE)
    Date ngayKy;

    String trangThai;
    Date ngayTao;
    String nguoiTao;
    Date ngaySua;
    String nguoiSua;
    String ldoTuchoi;
    Date ngayGuiDuyet;
    String nguoiGuiDuyet;
    Date ngayPduyet;
    String nguoiPduyet;
    String ghiChu;
    String namKhoach;
    String maDvi;
    @Transient
    String tenDvi;

    String diaChi;

    Integer tgianBhanh;

    String mst;

    String sdt;

    String stk;

    String tenNguoiDdien;

    String chucVu;

    String idNthau;

    @Transient
    String tenNthau;

    @Temporal(TemporalType.DATE)
    Date tgianNkho;

    @Transient
    String donViTinh;

    @Transient
    private List<BhHopDongDdiemNhapKho> BhDdiemNhapKhoList = new ArrayList<>();

    @Transient
    private List<HhPhuLucHd> hhPhuLucHdongList = new ArrayList<>();

    @Transient
    private List<BhHopDongDtl> BhHopDongDtlList = new ArrayList<>();

    @Transient
    private List<HhDviLquan> hhDviLquanList = new ArrayList<>();


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @JsonManagedReference
    @Where(clause = "data_type='" + BhHopDongHdr.TABLE_NAME + "'")
    private List<FileDKemJoinBhHopDong> fileDinhKems = new ArrayList<>();

    public void setFileDinhKems(List<FileDKemJoinBhHopDong> children2) {
        this.fileDinhKems.clear();
        for (FileDKemJoinBhHopDong child2 : children2) {
            child2.setParent(this);
        }
        this.fileDinhKems.addAll(children2);
    }

    public void addFileDinhKems(FileDKemJoinBhHopDong child2) {
        child2.setParent(this);
        this.fileDinhKems.add(child2);
    }

}
