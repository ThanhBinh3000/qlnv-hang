package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    private String soQd;
    private LocalDate ngayKy;
    private Long qdScTcId;
    @Column(name = "CAN_CU_QD_SC_TC")
    private String qdScTc;
    @Column(name = "NGAY_KY_QD_SC")
    private LocalDate ngayKyQdScTc;
    private LocalDate thoiHanXuat;
    private LocalDate thoiHanNhap;
    private BigDecimal duToanKinhPhi;
    private String loaiHinhNhapXuat;
    private String kieuNhapXuat;
    private String trichYeu;
    private String trangThai;

    @Transient
    private List<FileDinhKem> canCu;
    @Transient
    private List<FileDinhKem> fileDinhKem;


}
