package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScBangKeNhapVtDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuNhapKhoHdr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = XhTlBangKeHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XhTlBangKeHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_BANG_KE_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlBangKeHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlBangKeHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlBangKeHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhns;
    private String soBangKe;
    private LocalDate ngayLap;
    private String soQdXh;
    private Long idQdXh;
    private String ngayQdXh;
    private String soPhieuXuatKho;
    private Long idPhieuXuatKho;
    private String diaDiemKho;
    private String nguoiGiamSat;
    private Long idThuKho;
    private Long idLanhDaoCc;
    private String lyDoTuChoi;
    private String trangThai;
    private String loaiVthh;
    private String cloaiVthh;
    private String donViTinh;
    private BigDecimal tongTrongLuong;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenDvi;
    @Transient
    private String tenThuKho;
    @Transient
    private String tenLanhDaoCc;
    @Transient
    private List<XhTlBangKeDtl> children = new ArrayList<>();

    public String getTenTrangThai(){
        return TrangThaiAllEnum.getLabelById(getTrangThai());
    }

    @Transient
    private XhTlPhieuXuatKhoHdr xhTlPhieuXuatKhoHdr;

    // Print preview
    @Transient
    private String ngay;
    @Transient
    private String thang;

    public String getNgay() {
        return Objects.isNull(this.getNgayTao()) ? null : String.valueOf(this.getNgayTao().getDayOfMonth());
    }
    public String getThang() {
        return Objects.isNull(this.getNgayTao()) ? null : String.valueOf(this.getNgayTao().getMonthValue());
    }
}
