package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = ScQuyetDinhNhapHang.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScQuyetDinhNhapHang extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "SC_QUYET_DINH_NHAP_HANG";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScQuyetDinhNhapHang.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = ScQuyetDinhNhapHang.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScQuyetDinhNhapHang.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String soQd;
    private LocalDate ngayKy;
    private String soPhieuKtcl;
    private String idPhieuKtcl;
    private LocalDate ngayKiemDinh;
    private Long idQdXh;
    private String soQdXh;
    private LocalDate thoiHanXuat;
    private LocalDate thoiHanNhap;
    private String duToanChiPhi;
    private String loaiHinhNhapXuat;
    private String kieuNhapXuat;
    private String trichYeu;
    private String trangThai;
    private String lyDoTuChoi;
    private Long idLdc;
    private String tenLdc;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenDvi;
    @Transient
    private List<FileDinhKem> fileCanCu = new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileDinhKem = new ArrayList<>();
    @Transient
    private List<ScQuyetDinhNhapHangDtl> children = new ArrayList<>();

    @Transient
    private List<ScBienBanKtHdr> scBienBanKtList = new ArrayList<>();

    public String getTenTrangThai(){
        return TrangThaiAllEnum.getLabelById(getTrangThai());
    }

    // Print preview
    @Transient
    private String ngay;
    @Transient
    private String thang;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String soLuong;
    @Transient
    private String donViTinh;
    @Transient
    private String thoiHanXuatFormat;
    @Transient
    private String canCuPhapLy;

    public String getNgay() {
        return Objects.isNull(this.getNgayKy()) ? null : String.valueOf(this.getNgayKy().getDayOfMonth());
    }
    public String getThang() {
        return Objects.isNull(this.getNgayKy()) ? null : String.valueOf(this.getNgayKy().getMonthValue());
    }

    public String getTenLoaiVthh() {
        if(this.children != null){
            ScQuyetDinhNhapHangDtl scQuyetDinhNhapHangDtl = this.children.get(0);
            if(scQuyetDinhNhapHangDtl.getScDanhSachHdr() != null){
                this.donViTinh = scQuyetDinhNhapHangDtl.getScDanhSachHdr().getDonViTinh();
                return scQuyetDinhNhapHangDtl.getScDanhSachHdr().getTenLoaiVthh();
            }
        }
        return tenLoaiVthh;
    }

    public String getSoLuong() {
        BigDecimal soLuong = BigDecimal.ZERO;
        if(this.children != null){
            for (ScQuyetDinhNhapHangDtl e : children) {
                if(e.getScDanhSachHdr() != null){
                    soLuong = soLuong.add(e.getSoLuongNhap());
                }
            }
            return String.valueOf(soLuong);
        }
        return String.valueOf(soLuong);
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public String getThoiHanNhapFormat() {
        return Objects.isNull(this.thoiHanNhap) ? null : thoiHanNhap.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getCanCuPhapLy() {
        if(!Objects.isNull(fileCanCu)){
            List<String> collect = fileCanCu.stream().map(FileDinhKem::getFileName).collect(Collectors.toList());
            canCuPhapLy = "- "+String.join(" - ",collect);
        }
        return canCuPhapLy;
    }

}
