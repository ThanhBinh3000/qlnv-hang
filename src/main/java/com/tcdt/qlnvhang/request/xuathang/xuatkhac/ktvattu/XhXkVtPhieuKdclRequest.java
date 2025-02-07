package com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtBbLayMauDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtPhieuKdclDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXkVtPhieuKdclRequest extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhNs;
    private Long idQdGiaoNvXh;
    private String soQdGiaoNvXh;
    private Long idBbLayMau;
    private String soBbLayMau;
    private String soPhieu;
    private LocalDate ngayLapPhieu;
    private LocalDate ngayLayMau;
    private LocalDate ngayKiemDinh;
    private String dviKiemNghiem;
    private String loaiVthh;
    private String cloaiVthh;
    private String maDiaDiem;
    private String ppLayMau;
    private String nhanXetKetLuan;
    private Boolean isDat;
    private String trangThai;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String lyDoTuChoi;
    private String tenNguoiTao;
    private String type;
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    private List<XhXkVtPhieuKdclDtl> xhXkVtPhieuKdclDtl = new ArrayList<>();

    //search params
    private LocalDate ngayKiemDinhTu;
    private LocalDate ngayKiemDinhDen;
    private String dvql;
}
