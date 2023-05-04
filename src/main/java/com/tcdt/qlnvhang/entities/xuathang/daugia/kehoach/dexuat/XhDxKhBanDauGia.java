package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat;

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

    private Long idSoQdCtieu;

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

    private Integer tgianGnhan;

    private String pthucTtoan;

    private String pthucGnhan;

    private String tgianTtoanGhiChu;

    private String tgianGnhanGhiChu;

    private String thongBao;

    private BigDecimal khoanTienDatTruoc;

    private BigDecimal tongSoLuong;

    @Transient
    private BigDecimal tongTienGiaKhoiDiemDx;

    @Transient
    private BigDecimal tongTienGiaKdTheoDgiaDd;

    @Transient
    private BigDecimal tongKhoanTienDatTruocDx;

    @Transient
    private BigDecimal tongKhoanTienDtTheoDgiaDd;

    private String ghiChu;

    private Integer slDviTsan;

    private String trangThaiTh;
    @Transient
    private String tenTrangThaiTh;

    private Long idSoQdPd;

    private String soQdPd;

    private Long idThop;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQd;

    private String donViTinh;

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    private List<XhDxKhBanDauGiaDtl> children = new ArrayList<>();

}
