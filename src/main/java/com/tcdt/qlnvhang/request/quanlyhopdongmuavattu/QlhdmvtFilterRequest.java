package com.tcdt.qlnvhang.request.quanlyhopdongmuavattu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QlhdmvtFilterRequest {
	private String soHopDong;
	private LocalDate hopDongKyTuNgay;
	private LocalDate denNgay;
	private Long loaiHangId;
	private Long canCuId;
	private Long benBId;
	private Pageable pageable;
}
