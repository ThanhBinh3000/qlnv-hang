package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = ScPhieuNhapKhoHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScPhieuNhapKhoHdr extends BaseEntity implements Serializable {
    public static final String TABLE_NAME = "SC_PHIEU_NHAP_KHO_HDR";
    private static final long serialVersionUID = 1L;

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScPhieuNhapKhoHdr.TABLE_NAME + "_SEQ")
//    @SequenceGenerator(sequenceName = ScPhieuNhapKhoHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScPhieuNhapKhoHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhns;
    private String soPhieuNhapKho;
    private LocalDate ngayNhapKho;
    private Integer soNo;
    private Integer soCo;
    private String soQdNh;
    private Long idQdNh;
    private LocalDate ngayQdNh;
    private Long idScDanhSachHdr;
    private String maDiaDiem;
    private String loaiVthh;
    private String cloaiVthh;
    private String soPhieuKtcl;
    private Long idThuKho;
    private Long idLanhDaoCc;
    private String kyThuatVien;
    private String keToanTruong;
    private String nguoiGiaoHang;
    private String soCmt;
    private String dviNguoiGiaoHang;
    private String diaChi;
    private LocalDate thoiGianGiaoNhan;
    private String soBangKeCanHang;
    private Long idBangKeCanHang;
    private BigDecimal tongSoLuong;
    private BigDecimal tongKinhPhiThucTe;
    private String ghiChu;
    private String trangThai;
    private String donViTinh;
    private String lyDoTuChoi;
    @Transient
    private String tenDvi;
    @Transient
    List<FileDinhKem> fileDinhKems = new ArrayList<>();
    @Transient
    List<FileDinhKem> fileCanCu = new ArrayList<>();
    @Transient
    private List<ScPhieuNhapKhoDtl> children = new ArrayList<>();
    @JsonIgnore
    @Transient
    private Map<String, String> mapVthh;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenTrangThai;
    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucDvi;
    @Transient
    private String tenCuc;
    @Transient
    private String tenChiCuc;
    @Transient
    private String tenDiemKho;
    @Transient
    private String tenNhaKho;
    @Transient
    private String tenNganKho;
    @Transient
    private String tenLoKho;
    @Transient
    private String tenThuKho;
    @Transient
    private String tenLanhDaoCc;
    @Transient
    private ScDanhSachHdr scDanhSachHdr;

    public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
        this.mapDmucDvi = mapDmucDvi;
        if (!DataUtils.isNullObject(getMaDiaDiem())) {
            String maCuc = getMaDiaDiem().length() >= 6 ? getMaDiaDiem().substring(0, 6) : "";
            String maChiCuc = getMaDiaDiem().length() >= 8 ? getMaDiaDiem().substring(0, 8) : "";
            String maDiemKho = getMaDiaDiem().length() >= 10 ? getMaDiaDiem().substring(0, 10) : "";
            String maNhaKho = getMaDiaDiem().length() >= 12 ? getMaDiaDiem().substring(0, 12) : "";
            String maNganKho = getMaDiaDiem().length() >= 14 ? getMaDiaDiem().substring(0, 14) : "";
            String maLoKho = getMaDiaDiem().length() >= 16 ? getMaDiaDiem().substring(0, 16) : "";
            String tenCuc = mapDmucDvi.containsKey(maCuc) ? mapDmucDvi.get(maCuc) : null;
            String tenChiCuc = mapDmucDvi.containsKey(maChiCuc) ? mapDmucDvi.get(maChiCuc) : null;
            String tenDiemKho = mapDmucDvi.containsKey(maDiemKho) ? mapDmucDvi.get(maDiemKho) : null;
            String tenNhaKho = mapDmucDvi.containsKey(maNhaKho) ? mapDmucDvi.get(maNhaKho) : null;
            String tenNganKho = mapDmucDvi.containsKey(maNganKho) ? mapDmucDvi.get(maNganKho) : null;
            String tenLoKho = mapDmucDvi.containsKey(maLoKho) ? mapDmucDvi.get(maLoKho) : null;
            setTenCuc(tenCuc);
            setTenChiCuc(tenChiCuc);
            setTenDiemKho(tenDiemKho);
            setTenNhaKho(tenNhaKho);
            setTenNganKho(tenNganKho);
            setTenLoKho(tenLoKho);
        }
    }

    public void setMapVthh(Map<String, String> mapVthh) {
        this.mapVthh = mapVthh;
        if (!DataUtils.isNullObject(getLoaiVthh())) {
            setTenLoaiVthh(mapVthh.containsKey(getLoaiVthh()) ? mapVthh.get(getLoaiVthh()) : null);
        }
        if (!DataUtils.isNullObject(getCloaiVthh())) {
            setTenCloaiVthh(mapVthh.containsKey(getCloaiVthh()) ? mapVthh.get(getCloaiVthh()) : null);
        }
    }

    public String getTenTrangThai(){
        return TrangThaiAllEnum.getLabelById(getTrangThai());
    }

    // Print preview
    @Transient
    private String ngay;
    @Transient
    private String thang;
    @Transient
    private String thoiGianGiaoNhanFormat;
    @Transient
    private String ngayQdNhFormat;
    @Transient
    private String canCuPhapLy;

    public String getNgay() {
        return Objects.isNull(this.getNgayTao()) ? null : String.valueOf(this.getNgayTao().getDayOfMonth());
    }
    public String getThang() {
        return Objects.isNull(this.getNgayTao()) ? null : String.valueOf(this.getNgayTao().getMonthValue());
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public String getThoiGianGiaoNhanFormat() {
        return Objects.isNull(this.thoiGianGiaoNhan) ? null : thoiGianGiaoNhan.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getNgayQdNhFormat() {
        return Objects.isNull(this.ngayQdNh) ? null : ngayQdNh.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getCanCuPhapLy() {
        if(!Objects.isNull(fileCanCu)){
            List<String> collect = fileCanCu.stream().map(FileDinhKem::getFileName).collect(Collectors.toList());
            canCuPhapLy = "- "+String.join(" - ",collect);
        }
        return canCuPhapLy;
    }

}
