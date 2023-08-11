package com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuKdclDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuKtclDtl;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhXkVtBhPhieuKtclRequest extends BaseRequest {
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhNs;
    private Long idQdGiaoNvXh;
    private String soQdGiaoNvXh;
    private Long idQdGiaoNvNh;
    private String soQdGiaoNvNh;
    private Long idBbBaoHanh;
    private String soBbBaoHanh;
    private LocalDate thoiGianBh;

    private String soPhieu;
    private LocalDate ngayLapPhieu;
    private LocalDate ngayKiemTra;
    private String loaiVthh;
    private String cloaiVthh;
    private String donViTinh;
    private BigDecimal slTonKho;
    private BigDecimal slBaoHanh;
    private String maDiaDiem;
    private String ppLayMau;
    private String nhanXetKetLuan;
    private Boolean isDat;
    private Boolean mauBiHuy;
    private String trangThai;
    private String trangThaiNh;
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    private String lyDoTuChoi;
    private List<FileDinhKemReq> fileDinhKems = new ArrayList<>();
    private List<XhXkVtBhPhieuKtclDtl> phieuKtclDtl = new ArrayList<>();

    //search params
    private LocalDate ngayKiemTraTu;
    private LocalDate ngayKiemTraDen;
    private String dvql;
}
