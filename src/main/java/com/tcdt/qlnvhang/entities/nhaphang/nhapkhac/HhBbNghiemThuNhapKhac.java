package com.tcdt.qlnvhang.entities.nhaphang.nhapkhac;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = HhBbNghiemThuNhapKhac.TABLE_NAME)
@Data
public class HhBbNghiemThuNhapKhac {
    public static final String TABLE_NAME = "HH_BB_NGHIEM_THU_NHAP_KHAC";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_BB_NGHIEM_THU_NHAP_KHAC_SEQ")
    @SequenceGenerator(sequenceName = "HH_BB_NGHIEM_THU_NHAP_KHAC_SEQ", allocationSize = 1, name = "HH_BB_NGHIEM_THU_NHAP_KHAC_SEQ")
    private Long id;
    private String maDvi;
    @Transient
    private String tenDvi;
    private Integer namKhoach;
    private String maQhns;
    private Long idQdGiaoNvNh;
    private String soQdGiaoNvNh;
    private String soBbNtBq;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayTao;
    private String nguoiTao;
    private String tenNguoiTao;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngaySua;
    private String nguoiSua;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayGuiDuyet;
    private String nguoiGuiDuyet;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayPduyet;
    private String nguoiPduyet;
    private String tenNguoiPduyet;
    @Temporal(TemporalType.DATE)
    private Date ngayNghiemThu;
    private String thuKho;
    private String tenThuKho;
    private String keToan;
    private String tenKeToan;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String maDiemKho;
    @Transient
    private String tenDiemKho;
    private String maNhaKho;
    @Transient
    private String tenNhaKho;
    private String maNganKho;
    @Transient
    private String tenNganKho;
    private String maLoKho;
    @Transient
    private String tenLoKho;
    private String pthucBquan;
    private String hthucBquan;
    private BigDecimal dinhMucGiao;
    private BigDecimal dinhMucThucTe;
    private BigDecimal tongKinhPhiThucTe;
    private BigDecimal tongKinhPhiGiao;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    private String ldoTuChoi;
    private String ketLuan;
    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @Transient
    private List<HhBbNghiemThuNhapKhacDtl> dviChuDongThucHien =new ArrayList<>();
    @Transient
    private List<HhBbNghiemThuNhapKhacDtl> dmTongCucPdTruocThucHien =new ArrayList<>();
    @Transient
    private HhNkPhieuKtcl phieuKtcl;

    // Print preview
    @Transient
    private String ngay;
    @Transient
    private String tenBaoCao;
    @Transient
    private String thang;
    public String getNgay() {
        return Objects.isNull(this.getNgayTao()) ? null : String.valueOf(this.getNgayTao().getDate());
    }
    public String getThang() {
        return Objects.isNull(this.getNgayTao()) ? null : String.valueOf(this.getNgayTao().getMonth()+1);
    }

}
