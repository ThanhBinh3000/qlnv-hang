package com.tcdt.qlnvhang.request.khoahoccongnghebaoquan;

import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class KhCnTienDoThucHienReq {
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
