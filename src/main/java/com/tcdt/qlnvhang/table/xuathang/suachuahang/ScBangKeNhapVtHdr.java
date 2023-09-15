package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeNhapVTDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeNhapVTHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanLayMauHdr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = ScBangKeNhapVtHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScBangKeNhapVtHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "SC_BANG_KE_NHAP_VT_HDR";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScBangKeNhapVtHdr.TABLE_NAME + "_SEQ")
//    @SequenceGenerator(sequenceName = ScBangKeNhapVtHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScBangKeNhapVtHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhns;
    private String soBangKe;
    private LocalDate ngayNhap;
    private String soQdNh;
    private Long idQdNh;
    private String soPhieuNhapKho;
    private LocalDate ngayNhapKho;
    private Long idPhieuNhapKho;
    private Long idThuKho;
    private Long idLanhDaoCc;
    private String lyDoTuChoi;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenDvi;
    @Transient
    private String tenThuKho;
    @Transient
    private String tenLanhDaoCc;
    @Transient
    private List<ScBangKeNhapVtDtl> children = new ArrayList<>();

    public String getTenTrangThai(){
        return TrangThaiAllEnum.getLabelById(getTrangThai());
    }

    @Transient
    private ScPhieuNhapKhoHdr scPhieuNhapKhoHdr;

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
