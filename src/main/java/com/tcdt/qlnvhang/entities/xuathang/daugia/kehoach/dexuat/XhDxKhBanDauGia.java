package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
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

    private String diaChi;

    private String loaiHinhNx;

    private String kieuNx;

    private Integer namKh;

    private String soDxuat;

    private String trichYeu;

    private String soQdCtieu;

    private String loaiVthh;

    private String cloaiVthh;

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

    private BigDecimal tongTienGiaKhoiDiemDx;

    private BigDecimal tongKhoanTienDatTruocDx;

    private String ghiChu;

    private Integer slDviTsan;

    private Long idSoQdCtieu;
    private String trangThaiTh;

    private Long idSoQdPd;

    private String soQdPd;

    private Long idThop;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayKyQd;

    private String dviTinh;

    @Transient
    private BigDecimal tongTienDatTruocDuocDuyet;

    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenTrangThaiTh;
    @Transient
    private BigDecimal tongTienGiaKdTheoDgiaDd;
    @Transient
    private BigDecimal tongKhoanTienDtTheoDgiaDd;
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @Transient
    private List<XhDxKhBanDauGiaDtl> children = new ArrayList<>();

    // Getter & Setter
    public String getTenTrangThaiTh() {
        return NhapXuatHangTrangThaiEnum.getTenById(trangThaiTh);
    }
}
