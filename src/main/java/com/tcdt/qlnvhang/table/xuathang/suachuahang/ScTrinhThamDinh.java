package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = ScTrinhThamDinh.TABLE_NAME)
@Getter
@Setter
public class ScTrinhThamDinh extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "SC_THAM_DINH";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScTrinhThamDinh.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = ScTrinhThamDinh.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScTrinhThamDinh.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    @Column(name = "MA_DON_VI")
    private String maDvi;
    @Column(name = "ID_TONG_HOP")
    private Long idTongHop;
    @Column(name = "ID_DS_HDR")
    private Long idDsHdr;
    @Column(name = "SO_TO_TRINH")
    private String soToTr;
    @Column(name = "MA_DANH_SACH")
    private String maDsSc;
    @Column(name = "NGAY_DUYET")
    private LocalDate ngayDuyet;
    private LocalDate thoiGianXuatDk;
    private LocalDate thoiGianNhapDk;
    private String soQdSc;
    private String trichYeu;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    @Transient
    private List<FileDinhKem> fileDinhKem=new ArrayList<>();;
    @Transient
    private List<FileDinhKem> canCu = new ArrayList<>();


}
