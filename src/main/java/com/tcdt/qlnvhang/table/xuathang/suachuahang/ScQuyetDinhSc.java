package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = ScQuyetDinhSc.TABLE_NAME)
@Data
public class ScQuyetDinhSc extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "SC_QUYET_DINH";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScQuyetDinhSc.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = ScQuyetDinhSc.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScQuyetDinhSc.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "SO_QUYET_DINH")
    private String soQd;
    private String trichYeu;
    private LocalDate ngayKy;
    private Long idToTrinh;
    private String soToTrinh;
    private LocalDate ngayDuyetToTrinh;
    private LocalDate thoiHanXuatSc;
    private LocalDate thoiHanNhapSc;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    @Transient
    private List<FileDinhKem> fileDinhKem=new ArrayList<>();
    @Transient
    private List<FileDinhKem> canCu = new ArrayList<>();

}
