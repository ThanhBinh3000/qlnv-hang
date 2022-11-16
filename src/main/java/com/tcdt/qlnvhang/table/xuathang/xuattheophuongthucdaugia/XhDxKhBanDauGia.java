package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.BaseEntity;
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
@Table(name = "XH_DX_KH_BAN_DAU_GIA ")
@Data
public class XhDxKhBanDauGia  implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_DX_KH_BAN_DAU_GIA ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_DX_KH_BAN_DAU_GIA_SEQ ")
    @SequenceGenerator(sequenceName = "XH_DX_KH_BAN_DAU_GIA_SEQ ", allocationSize = 1, name = "XH_DX_KH_BAN_DAU_GIA_SEQ ")
    private Long id;
    private String maDvi;
    private String tenDvi;
    private String diaChi;
    private String loaiHinhNx;
    private String kieuNx;
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
    @Temporal(TemporalType.DATE)
    private Date tgianDkienTu;
    @Temporal(TemporalType.DATE)
    private Date tgianDkienDen;
    private String loaiHdong;
    private Integer tgianKyHdong;
    private Integer tgianTtoan;
    private Integer tgianGnhan;
    private String pthucTtoan;
    private String pthucGnhan;
    private String thongBaoKh;
    private BigDecimal khoanTienDatTruoc;
    private BigDecimal tongSoLuong;
    private BigDecimal tongTienKdiem;
    private BigDecimal tongTienDatTruoc;
    private String ghiChu;
    @Temporal(TemporalType.DATE)
    private Date ngayTao;
    private String nguoiTaoId;
    @Temporal(TemporalType.DATE)
    private Date ngaySua;
    private String nguoiSuaId;
    @Temporal(TemporalType.DATE)
    private Date ngayGduyet;
    private String nguoiGduyetId;
    @Temporal(TemporalType.DATE)
    private Date ngayPduyet;
    private String nguoiPduyetId;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    private String maThop;
    private String soQdPd;
    private Integer soDviTsan;
    private Integer slHdDaKy;
    private String trangThaiTh;
    @Transient
    private String tenTrangThaiTh;
    private String ldoTuChoi;
    @Temporal(TemporalType.DATE)
    private Date ngayKy;
    private String tgianKyHdongGhiChu;
    private String tgianTtoanGhiChu;
    private String TgianGnhanGhiChu;


    @Transient
    private List<FileDinhKem> fileDinhKems =new ArrayList<>();

    @Transient
    private List<XhDxKhBanDauGiaPhanLo> dsPhanLoList = new ArrayList<>();



}
