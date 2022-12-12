package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "XH_QD_PD_KH_BDG_DTL")
@Data
public class XhQdPdKhBdgDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BDG_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_PD_KH_BDG_DTL_SEQ")
    @SequenceGenerator(sequenceName = "XH_QD_PD_KH_BDG_DTL_SEQ", allocationSize = 1, name = "XH_QD_PD_KH_BDG_DTL_SEQ")
    private Long id;

    private Long idQdHdr;

    private Long idDxHdr;

    private String maDvi;
    @Transient
    private String tenDvi;
    private String soDxuat;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTao;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;

    @Temporal(TemporalType.DATE)
    private Date tgianDkienTu;
    @Temporal(TemporalType.DATE)
    private Date tgianDkienDen;

    private String trichYeu;

    private BigDecimal tongSoLuong;

    private Integer soDviTsan;

    private BigDecimal tongTienKdienDonGia;

    private BigDecimal tongTienDatTruocDonGia;

    private String moTaHangHoa;

    private String diaChi;

    @Column(name="SO_QD_PD_KQ_BDG")
    private String soQdPdKqBdg;

    private String trangThai;
    @Transient
    private String tenTrangThai;




    @Transient
    private XhQdPdKhBdg xhQdPdKhBdg;

    @Transient
    private XhDxKhBanDauGia xhDxKhBanDauGia;

    @Transient
    private List<XhQdPdKhBdgPl> children= new ArrayList<>();

}
