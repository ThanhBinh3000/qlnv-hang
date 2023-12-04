package com.tcdt.qlnvhang.response.dauthauvattu;

import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.HhDthauNthauDuthau;
import lombok.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ChiTietGoiThauRes {
    private String ghiChuTtdt;
    private Date tgianTrinhKqTcg;
    private Date tgianTrinhTtd;
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
    private List<HhDthauNthauDuthau> dsNhaThauDthau = new ArrayList<>();
}
