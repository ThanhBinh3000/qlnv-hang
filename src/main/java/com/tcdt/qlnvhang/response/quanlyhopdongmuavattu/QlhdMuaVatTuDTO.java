package com.tcdt.qlnvhang.response.quanlyhopdongmuavattu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QlhdMuaVatTuDTO {
    private Long id;
    private Long canCuId;
    private String soHopDong;
    private String tenHopDong;
    private Long donViTrungThauId;
    private LocalDate ngayKy;
    private QlhdmvtThongTinChungDTO thongTinChung;
}
