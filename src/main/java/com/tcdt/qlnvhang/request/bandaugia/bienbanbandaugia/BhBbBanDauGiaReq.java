package com.tcdt.qlnvhang.request.bandaugia.bienbanbandaugia;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BhBbBanDauGiaReq {
    private Long id;
    private Long nam;
    private String soBienBan;
    private String trichYeu;
    private LocalDate ngayKy;
    private String loaiVthh;
    private String maVatTuCha;
    private Long thongBaoBdgId;
    private String donViThongBao;
    private LocalDate ngayToChuc;
    private String diaDiem;

    private List<BhBbBanDauGiaCtReq> cts = new ArrayList<>();
    private List<BhBbBanDauGiaCt1Req> ct1s = new ArrayList<>();
}
