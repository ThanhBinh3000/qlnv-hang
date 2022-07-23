package com.tcdt.qlnvhang.request.bandaugia.kehoachbanhangdaugia;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BanDauGiaDiaDiemGiaoNhanRequest {
	private Long id;
	private Long bhDgKehoachId;
	private Long stt;
	private String tenChiCuc;
	private String diaChi;
	private BigDecimal soLuong;
}
