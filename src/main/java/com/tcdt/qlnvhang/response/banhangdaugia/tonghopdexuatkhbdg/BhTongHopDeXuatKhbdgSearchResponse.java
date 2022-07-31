package com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.response.IdAndNameDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BhTongHopDeXuatKhbdgSearchResponse {
	private Long id;
	private String maTongHop;
	private LocalDate ngayTongHop;
	private String noiDungTongHop;
	private Integer namKeHoach;
	private IdAndNameDto soQdPheDuyetKhbdg;
	private String loaiHangHoa;
	private String trangThai;
}
