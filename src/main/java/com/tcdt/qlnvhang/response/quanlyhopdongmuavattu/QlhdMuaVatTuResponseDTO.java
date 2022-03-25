package com.tcdt.qlnvhang.response.quanlyhopdongmuavattu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QlhdMuaVatTuResponseDTO {
    private Long id;
    private Long canCuId;
    private String soHopDong;
    private String tenHopDong;
    private Long donViTrungThauId;
    private LocalDate ngayKy;
    private QlhdmvtThongTinChungResponseDTO thongTinChung;
    private List<QlhdmvtDsGoiThauResponseDTO> danhSachGoiThau;
    private QlhdmvtPhuLucHopDongResponseDTO phuLuc;
}
