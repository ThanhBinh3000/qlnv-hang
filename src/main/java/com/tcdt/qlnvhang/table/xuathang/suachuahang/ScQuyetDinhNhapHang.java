package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
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
    private String soQd;
    private LocalDate ngayKyQd;
    private String ketQuaKiemDinh;
    private LocalDate ngayKiemDinh;
    private Long soQdGiaoNvXhId;
    private String soQdGiaoNvXh;
    private LocalDate thoiHanXuat;
    private LocalDate thoiHanNhap;
    private String duToanChiPhi;
    private String loaiHinhNhapXuat;
    private String kieuNhapXuat;
    private String trichYeu;
    private String trangThai;
    @Transient
    private List<FileDinhKem> canCu;

    @Transient
    private List<FileDinhKem> fileDinhKems;

}
