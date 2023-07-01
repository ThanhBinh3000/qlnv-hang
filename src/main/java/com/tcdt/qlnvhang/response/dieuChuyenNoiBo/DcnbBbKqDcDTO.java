package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbKqDcDtl;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class DcnbBbKqDcDTO {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private String maDviNhan;
    private String soBc;
    private LocalDate ngayBc;
    private String tenBc;
    private Long qdDcCucId;
    private String soQdDcCuc;
    private Long qdDcTcId;
    private String soQdDcTc;
    private LocalDate ngayKyQdTc;
    private String noiDung;
    private String trangThai;
    private String lyDoTuChoi;
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    private List<DcnbBbKqDcDtl> danhSachDaiDien = new ArrayList<>();
    private String tenTrangThai;
}
