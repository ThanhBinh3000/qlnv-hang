package com.tcdt.qlnvhang.request.bandaugia.thongbaobandaugiakhongthanh;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BhThongBaoBdgKtReq {
    private Long id;
    private Integer nam;
    private String maThongBao;
    private String trichYeu;
    private LocalDate ngayKy;
    private String loaiVthh;
    private String maVatTuCha;
    private Long thongBaoBdgId;
    private String donViThongBao;
    private LocalDate ngayToChuc;
    private String noiDung;

    private List<FileDinhKemReq> fileDinhKemReqs = new ArrayList<>();
}
