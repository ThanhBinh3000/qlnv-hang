package com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatNhapKho;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXkVtBckqKiemDinhMauRequest extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maDviNhan;
    private String soBaoCao;
    private String tenBaoCao;
    private String idQdGiaoNvXh;
    private String soQdGiaoNvXh;
    private LocalDate ngayBaoCao;
    private String trangThai;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String lyDoTuChoi;
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    private List<XhXkVtPhieuXuatNhapKho> listDetailPxk = new ArrayList<>();

    //search params
    private LocalDate ngayBaoCaoTu;
    private LocalDate ngayBaoCaoDen;
    private String dvql;
}
