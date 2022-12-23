package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinHopDong;
import com.tcdt.qlnvhang.entities.FileDKemJoinHopDongMtt;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongHdr;
import com.tcdt.qlnvhang.table.FileDinhKem;
import io.swagger.models.auth.In;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

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
    private Integer namHd;
    private String soQdKqMtt;
    private Long idQdKqMtt;

    @Temporal(TemporalType.DATE)
    Date ngayQdKqMtt;
    @Temporal(TemporalType.DATE)
    Date tgianNkho;
    private String soQdPdKhMtt;
    private String soHd;
    private String tenHd;
    @Temporal(TemporalType.DATE)
    Date ngayKy;
    private String ghiChuNgayKy;
    private String loaiHdong;
    private String ghiChuLoaiHdong;
    private Integer soNgayThien;
    @Temporal(TemporalType.DATE)
    Date tgianGnhanTu;
    @Temporal(TemporalType.DATE)
    Date tgianGnhanDen;
    private Long idChaoGia;

    private String ghiChuTgianGnhan;
    private String noiDung;
    private  String dieuKien;

    private String maDvi;
    @Transient
    private String tenDvi;

    private String diaChi;

    private String mst;

    private String tenNguoiDdien;

    private String chucVu;

    private String sdt;

    private String fax;

    private String stk;

    private String moLai;

    private String thongTinGiayUyQuyen;

    private String tenCongTy;

    private String diaChiCongTy;

    private String mstCongTy;

    private String tenNguoiDdienCongTy;

    private String chucVuCongTy;

    private String sdtCongTy;

    private String faxCongTy;

    private String stkCongTy;

    private String moLaiCongTy;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    private String moTaHangHoa;



    private Double soLuong;
    private Double donGia;

    private Double thanhTien;

    private String ghiChu;

    private BigDecimal soLuongQdKhMtt;

    private String trangThai;
    @Transient
    private String tenTrangThai;

    private Date ngayGuiDuyet;
    private String nguoiGuiDuyet;
    private Date ngayPduyet;
    private String nguoiPduyet;
    private Integer namKh;

    @Transient
    private String donViTinh;


    @Transient
    private List<HhThongTinDviDtuCcap> details;

    @Transient
    private List<HhPhuLucHopDongMtt>  hhPhuLucHdongList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @JsonManagedReference
    @Where(clause = "data_type='" + HhHdongBkePmuahangHdr.TABLE_NAME + "'")
    private List<FileDKemJoinHopDongMtt> fileDinhKems = new ArrayList<>();

    public void setFileDinhKems(List<FileDKemJoinHopDongMtt> children2) {
        this.fileDinhKems.clear();
        for (FileDKemJoinHopDongMtt child2 : children2) {
            child2.setParent(this);
        }
        this.fileDinhKems.addAll(children2);
    }

    public void addFileDinhKems(FileDKemJoinHopDongMtt child2) {
        child2.setParent(this);
        this.fileDinhKems.add(child2);
    }

}
