package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "XH_DX_KH_BAN_DAU_GIA")
@Data
public class XhDxKhBanDauGia extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_DX_KH_BAN_DAU_GIA";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_DX_KH_BAN_DAU_GIA_SEQ")
    @SequenceGenerator(sequenceName = "XH_DX_KH_BAN_DAU_GIA_SEQ", allocationSize = 1, name = "XH_DX_KH_BAN_DAU_GIA_SEQ")
    private Long id;
    private String maDvi;
    @Transient
    private String tenDvi;
    private String loaiHinhNx;
    private String kieuNx;
    private String diaChi;
    private Integer namKh;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianDkienTu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tgianDkienDen;
    private Integer tgianTtoan;
    private String tgianTtoanGhiChu;
    private String pthucTtoan;
    private Integer tgianGnhan;
    private String pthucGnhan;
    private String tgianGnhanGhiChu;
    private String thongBaoKh;
    private BigDecimal khoanTienDatTruoc;
    private BigDecimal tongSoLuong;
    private BigDecimal tongTienKdiem;
    private BigDecimal tongTienKdienDonGia;
    private BigDecimal tongTienDatTruoc;
    private BigDecimal tongTienDatTruocDonGia;
    private String ghiChu;
    private String maThop;
    private String soQdPd;
    private Integer soDviTsan;
    private Integer slHdDaKy;
    private String trangThaiTh;
    @Transient
    private String tenTrangThaiTh;
    @Temporal(TemporalType.DATE)
    private Date ngayKyQd;

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<XhDxKhBanDauGiaPhanLo> children = new ArrayList<>();


}
