package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia;

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
    private Long idQdPd;
    private String maDvi;
    private String tenDvi;
    private String diaChi;
    private Long idDxuat;
    private String soDxuat;
    private String trichYeu;
    private String soQdCtieu;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String moTaHangHoa;
    private String tchuanCluong;
    @Temporal(TemporalType.DATE)
    private Date tgianDkienTu;
    @Temporal(TemporalType.DATE)
    private Date tgianDkienDen;
    private String loaiHdong;
    private Integer tgianKyHdong;
    private Integer tgianTtoan;
    private Integer tgianGnhan;
    private String tgianKyHdongGhiChu;
    private String tgianTtoanGhiChu;
    private String tgianGnhanGhiChu;
    private String thongBaoKh;
    private BigDecimal khoanTienDatTruoc;
    private BigDecimal tongSoLuong;
    private BigDecimal tongTienKdiem;
    private BigDecimal tongTienDatTruoc;
    @Temporal(TemporalType.DATE)
    private Date ngayTao;
    @Temporal(TemporalType.DATE)
    private Date ngayPduyet;
    private Integer soDviTsan;
    private Integer slHdDaKy;

    @Transient
    private List<XhQdPdKhBdgPl> xhQdPdKhBdgPL= new ArrayList<>();

}
