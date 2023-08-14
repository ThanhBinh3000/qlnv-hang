package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    @Transient
    private String tenTrangThai;
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

}
