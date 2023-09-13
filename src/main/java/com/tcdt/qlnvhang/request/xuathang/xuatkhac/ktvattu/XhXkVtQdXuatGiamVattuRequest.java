package com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatNhapKho;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXkVtQdXuatGiamVattuRequest extends BaseRequest {

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
    private List<XhXkVtPhieuXuatNhapKho> xhXkVtPhieuXuatNhapKho = new ArrayList<>();
    private List<FileDinhKemReq> fileDinhKems =new ArrayList<>();
    //search params
    LocalDate ngayQuyetDinhTu;
    LocalDate ngayQuyetDinhDen;
    String dvql;
}
