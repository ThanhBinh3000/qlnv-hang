package com.tcdt.qlnvhang.request.xuathang.xuatkhac.xuathangkhoidm;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkKhXuatHangDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm.XhXkDsHangDtqgDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXkDsHangDtqgRequest extends BaseRequest {
    private Long id;
    private String maDs;
    private String tenDs;
    private String trangThai;
    private String lyDoTuChoi;
    private Long nguoiDuyetId;
    private LocalDate ngayDuyet;
    private Boolean loai;
    private List<XhXkDsHangDtqgDtl> xhXkDsHangDtqgDtl = new ArrayList<>();
    private List<FileDinhKem> fileDinhKems;
    //search params
    private LocalDate ngayCapNhatTu;
    private LocalDate ngayCapNhatDen;
}
