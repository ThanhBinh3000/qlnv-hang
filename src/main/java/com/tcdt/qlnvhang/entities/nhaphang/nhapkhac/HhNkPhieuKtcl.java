package com.tcdt.qlnvhang.entities.nhaphang.nhapkhac;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = HhNkPhieuKtcl.TABLE_NAME)
@Data
public class HhNkPhieuKtcl {
    public static final String TABLE_NAME = "HH_NK_PHIEU_KTCL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_NK_PHIEU_KTCL_SEQ")
    @SequenceGenerator(sequenceName = "HH_NK_PHIEU_KTCL_SEQ", allocationSize = 1, name = "HH_NK_PHIEU_KTCL_SEQ")
    private Long id;
    private String maDvi;
    @Transient
    private String tenDvi;
    private Integer namKhoach;
    private String maQhns;
    private Long idQdGiaoNvNh;
    private String soQdGiaoNvNh;
    private String soPhieu;
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
    private String trangThai;
    @Transient
    private String tenTrangThai;
    private String ldoTuChoi;
    private String ketLuan;
    private String nguoiGiaoHang;
    private String donViGiaoHang;
    private String cmtNguoiGiaoHang;
    private String diaChi;
    private String bienSoXe;
    private String soChungThuGiamDinh;
    @Temporal(TemporalType.DATE)
    private Date ngayGdinh;
    private String tchucGdinh;
    private BigDecimal soLuongTheoChungTu;
    private BigDecimal soLuongKhKhaiBao;
    private BigDecimal soLuongTtKtra;
    private String kqDanhGia;
    @Transient
    private List<HhNkPhieuKtclCt> listChiTieu;
    @Transient
    private List<FileDinhKem> fileDinhKemCtgd;
    @Transient
    private List<FileDinhKem> fileDinhKemKtcl;
    private String soHieuQuyChuan;

    @Transient
    private String ngay;
    @Transient
    private String thang;
    public String getNgay() {
        return Objects.isNull(this.getNgayTao()) ? null : String.valueOf(this.getNgayTao().getDate());
    }
    public String getThang() {
        return Objects.isNull(this.getNgayTao()) ? null : String.valueOf(this.getNgayTao().getMonth()+1);
    }

}
