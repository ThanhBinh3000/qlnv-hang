package com.tcdt.qlnvhang.response.quanlyhopdongmuavattu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QlhdmvtPhuLucHopDongResponseDTO {
	private Long id;
	private String stt;
	private String soPhuLuc;
	private LocalDate ngayKy;
	private LocalDate ngayHieuLuc;
	private String veViec;
	private LocalDate ngayTao;
	private Long nguoiTaoId;
	private LocalDate ngaySua;
	private Long nguoiSuaId;
}
