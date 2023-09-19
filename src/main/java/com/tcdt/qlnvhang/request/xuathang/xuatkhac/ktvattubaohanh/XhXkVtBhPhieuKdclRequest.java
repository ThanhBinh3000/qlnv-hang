package com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtPhieuKdclDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuKdclDtl;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXkVtBhPhieuKdclRequest extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhNs;
    private Long idQdGiaoNvXh;
    private String soQdGiaoNvXh;
    private Long idQdGiaoNvNh;
    private String soQdGiaoNvNh;
    private Long idBbLayMau;
    private Integer soLanLm;
    private Integer soLuongMau;
    private Integer slTonKho;
    private String soBbLayMau;
    private LocalDate ngayLayMau;
    private String soPhieu;
    private LocalDate ngayLapPhieu;
    private LocalDate ngayKiemDinh;
    private String dviKiemDinh;
    private String loaiVthh;
    private String cloaiVthh;
    private String donViTinh;
    private String maDiaDiem;
    private String ppLayMau;
    private String nhanXetKetLuan;
    private Boolean isDat;
    private Boolean mauBiHuy;
    private String trangThai;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String lyDoTuChoi;
    private String type;
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    private List<XhXkVtBhPhieuKdclDtl> phieuKdclDtl = new ArrayList<>();

    //search params
    private LocalDate ngayKiemDinhTu;
    private LocalDate ngayKiemDinhDen;
    private String dvql;
}
