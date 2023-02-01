package com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinBhHopDong;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.HhDviLquan;
import com.tcdt.qlnvhang.table.HhPhuLucHd;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayKyQdKq;

    private String soQdPd;

    private String maDviTsan;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date tgianNkho;

    private String soHd;

    private String tenHd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    @Column(columnDefinition = "Date")
    private Date ngayHluc;

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

    private String tenNhaThau;

    private String diaChiNhaThau;

    private String mstNhaThau;

    private String tenNguoiDdienNhaThau;

    private String chucVuNhaThau;

    private String sdtNhaThau;

    private String stkNhaThau;

    private String loaiVthh;

    private String cloaiVthh;

    private String moTaHangHoa;

    private Double soLuong;

    private Double tongTien;

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
    private List<XhHopDongDtl> children = new ArrayList<>();
    @Transient
    private List<String> listMaDviTsan = new ArrayList<>();

}
