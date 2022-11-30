package com.tcdt.qlnvhang.table.khoahoccongnghebaoquan;

import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "KH_CN_TIEN_DO_THUC_HIEN")
@Data
public class KhCnTienDoThucHien implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "KH_CN_TIEN_DO_THUC_HIEN";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KH_CN_TIEN_DO_THUC_HIEN_SEQ")
    @SequenceGenerator(sequenceName = "KH_CN_TIEN_DO_THUC_HIEN_SEQ", allocationSize = 1, name = "KH_CN_TIEN_DO_THUC_HIEN_SEQ")

    private Long id;
    private Long idHdr;
    private String noiDung;
    private String sanPham;
    @Temporal(TemporalType.DATE)
    private Date tuNgay;
    @Temporal(TemporalType.DATE)
    private Date denNgay;
    private String nguoiThucHien;
    private String trangThaiTd;
    @Transient
    private String tenTrangThaiTd;

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();

}
