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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = ScQuyetDinhXuatHang.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScQuyetDinhXuatHang extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "SC_QUYET_DINH_XUAT_HANG";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScQuyetDinhXuatHang.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = ScQuyetDinhXuatHang.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScQuyetDinhXuatHang.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String soQd;
    private LocalDate ngayKy;
    private Long idQdSc;
    private String soQdSc;
    private LocalDate ngayKyQdSc;
    private LocalDate thoiHanXuat;
    private LocalDate thoiHanNhap;
    private BigDecimal duToanKinhPhi;
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
    private List<FileDinhKem> fileCanCu;
    @Transient
    private List<FileDinhKem> fileDinhKem;
    @Transient
    private ScQuyetDinhSc scQuyetDinhSc;
    @Transient
    private List<ScDanhSachHdr> scDanhSachHdrList = new ArrayList<>();
    @Transient
    private List<ScPhieuXuatKhoHdr> scPhieuXuatKhoHdrList= new ArrayList<>();
    @Transient
    private List<ScKiemTraChatLuongHdr> scKiemTraChatLuongHdrList = new ArrayList<>();

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
        if(this.scQuyetDinhSc != null){
            if(this.scQuyetDinhSc.getScTrinhThamDinhHdr() != null){
                if(!this.scQuyetDinhSc.getScTrinhThamDinhHdr().getChildren().isEmpty()){
                    ScTrinhThamDinhDtl scTrinhThamDinhDtl = this.scQuyetDinhSc.getScTrinhThamDinhHdr().getChildren().get(0);
                    if(scTrinhThamDinhDtl.getScDanhSachHdr() != null){
                        this.donViTinh = scTrinhThamDinhDtl.getScDanhSachHdr().getDonViTinh();
                        return scTrinhThamDinhDtl.getScDanhSachHdr().getTenLoaiVthh();
                    }
                }
            }
        }
        return tenLoaiVthh;
    }

    public String getSoLuong() {
        BigDecimal soLuong = BigDecimal.ZERO;
        if(this.scQuyetDinhSc != null){
            if(this.scQuyetDinhSc.getScTrinhThamDinhHdr() != null){
                if(!this.scQuyetDinhSc.getScTrinhThamDinhHdr().getChildren().isEmpty()){
                    List<ScTrinhThamDinhDtl> children = this.scQuyetDinhSc.getScTrinhThamDinhHdr().getChildren();
                    for (ScTrinhThamDinhDtl e : children) {
                        if(e.getScDanhSachHdr() != null){
                            soLuong = soLuong.add(e.getScDanhSachHdr().getSlDaDuyet());
                        }
                    }
                    return String.valueOf(soLuong);
                }
            }
        }
        return String.valueOf(soLuong);
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public String getThoiHanXuatFormat() {
        return Objects.isNull(this.thoiHanXuat) ? null : thoiHanXuat.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getCanCuPhapLy() {
        if(!Objects.isNull(fileCanCu)){
            List<String> collect = fileCanCu.stream().map(FileDinhKem::getFileName).collect(Collectors.toList());
            canCuPhapLy = "- "+String.join(" - ",collect);
        }
        return canCuPhapLy;
    }
}
