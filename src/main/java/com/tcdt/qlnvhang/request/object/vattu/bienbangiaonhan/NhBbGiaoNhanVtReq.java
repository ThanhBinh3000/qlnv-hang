package com.tcdt.qlnvhang.request.object.vattu.bienbangiaonhan;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class NhBbGiaoNhanVtReq {

    private Long id;

    private Long qdgnvnxId;

    private Long bbKtNhapKhoId;

    private String soBienBan;

    private LocalDate ngayKy;

    private Long hopDongId;

    private LocalDate ngayHopDong;

    private Long bbGuiHangId;

    private LocalDate ngayKyBbGh;

    private Long hoSKyThuatId;

    private LocalDate ngayKyHskt;

    private String maVatTuCha;

    private String maVatTu;

    private BigDecimal soLuong;

    private String ghiChu;

    private String ketLuan;

    private List<NhBbGiaoNhanVtCtReq> chiTiets = new ArrayList<>();
    private List<FileDinhKemReq> fileDinhKemReqs = new ArrayList<>();
    private List<FileDinhKemReq> canCus = new ArrayList<>();
}
