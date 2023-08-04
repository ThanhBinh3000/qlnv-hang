package com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhQdXuatGiamVtDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXkVtBhQdXuatGiamVtRequest extends BaseRequest {

    private Long id;
    private Integer namKeHoach;
    private String maDvi;
    private String maDviNhan;
    private String soQuyetDinh;
    private String loai;
    private String trichYeu;
    private LocalDate ngayKy;
    private LocalDate thoiHanXuatGiam;
    private String soCanCu;
    private Long idCanCu;
    private String trangThai;
    private String lyDoTuChoi;
    private Long nguoiDuyetId;
    private LocalDate ngayDuyet;
    private String listSoQdGiaoNvXh;
    private String listIdQdGiaoNvXh;
    private List<XhXkVtBhQdXuatGiamVtDtl> qdXuatGiamVtDtl = new ArrayList<>();
    //search params
    LocalDate ngayQuyetDinhTu;
    LocalDate ngayQuyetDinhDen;
    String dvql;
}
