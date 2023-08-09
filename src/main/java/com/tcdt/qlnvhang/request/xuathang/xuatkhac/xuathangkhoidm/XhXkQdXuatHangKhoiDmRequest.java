package com.tcdt.qlnvhang.request.xuathang.xuatkhac.xuathangkhoidm;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm.XhXkDsHangDtqgDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXkQdXuatHangKhoiDmRequest extends BaseRequest {
    private Long id;
    private String soQd;
    private LocalDate ngayKy;
    private LocalDate ngayHieuLuc;
    private LocalDate ngayDuyet;
    private String maCanCu;
    private Long idCanCu;
    private String trichYeu;
    private String trangThai;
    private String lyDoTuChoi;
    private Long nguoiDuyetId;
    private List<XhXkDsHangDtqgDtl> listDtl = new ArrayList<>();
    private List<FileDinhKem> fileDinhKems;
    //search params
    private LocalDate ngayKyTu;
    private LocalDate ngayKyDen;
    private LocalDate ngayHieuLucTu;
    private LocalDate ngayHieuLucDen;
}
