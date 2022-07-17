package com.tcdt.qlnvhang.response.vattu.bienbangiaonhan;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.response.SoBienBanPhieuRes;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class NhBbGiaoNhanVtRes extends SoBienBanPhieuRes {

    private Long id;

    private Long qdgnvnxId;
    private String soQuyetDinhNhap;

    private Long bbKtNhapKhoId;
    private String soBbKtNk;

    private String maDvi;
    private String tenDvi;
    private String maQhns;
    private String capDvi;

    private String soBienBan;

    private LocalDate ngayKy;

    private Long hopDongId;
    private String soHopDong;
    private LocalDate ngayHopDong;

    private Long bbGuiHangId;
    private String soBbGh;
    private LocalDate ngayKyBbGh;

    private Long hoSKyThuatId;
    private String soBbHskt;
    private LocalDate ngayKyHskt;

    private String maVatTuCha;
    private String tenVatTuCha;
    private String maVatTu;
    private String tenVatTu;
    private BigDecimal soLuong;

    private String ghiChu;

    private String ketLuan;

    private String trangThai;
    private String tenTrangThai;
    private String trangThaiDuyet;

    private String lyDoTuChoi;
    private String benGiao;

    private List<NhBbGiaoNhanVtCtRes> chiTiets = new ArrayList<>();

    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
}
