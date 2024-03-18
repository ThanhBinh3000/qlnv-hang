package com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktluongthuc;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXkLtBaoCaoKqRequest extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maDviNhan;
    private String soBaoCao;
    private String tenBaoCao;
    private LocalDate ngayBaoCao;
    private String idTongHop;
    private String maDanhSach;
    private String tenDanhSach;

    private String dvql;
    private LocalDate ngayBaoCaoTu;
    private LocalDate ngayBaoCaoDen;
    private List<FileDinhKemReq> fileDinhKems =new ArrayList<>();
    private List<FileDinhKemReq> fileDinhKemsBc =new ArrayList<>();
}
