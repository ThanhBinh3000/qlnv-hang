package com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.response.IdAndNameDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

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
	private IdAndNameDto qdPheDuyetKhbdg;
	private String loaiHangHoa;
	private String trangThai;

	public BhTongHopDeXuatKhbdgSearchResponse(Object[] rawData) {
		if (Objects.nonNull(rawData[0])) this.id = (Long) rawData[0];
		if (Objects.nonNull(rawData[1])) this.maTongHop = (String) rawData[1];
		if (Objects.nonNull(rawData[2])) this.ngayTongHop = (LocalDate) rawData[2];
		if (Objects.nonNull(rawData[3])) this.noiDungTongHop = (String) rawData[3];
		if (Objects.nonNull(rawData[4])) this.namKeHoach = (Integer) rawData[4];
		if (Objects.nonNull(rawData[5])) this.trangThai = (String) rawData[5];

		if (Objects.nonNull(rawData[6])) this.qdPheDuyetKhbdg.setId((Long) rawData[6]);
		if (Objects.nonNull(rawData[7])) this.qdPheDuyetKhbdg.setName((String) rawData[7]);
		if (Objects.nonNull(rawData[8])) this.loaiHangHoa = (String) rawData[8];
	}

}
