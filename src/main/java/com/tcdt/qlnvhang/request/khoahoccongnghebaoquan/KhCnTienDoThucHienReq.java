package com.tcdt.qlnvhang.request.khoahoccongnghebaoquan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;


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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date tuNgay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date denNgay;
    private String nguoiThucHien;
    private String trangThaiTd;
    @Transient
    private String tenTrangThaiTd;

}
