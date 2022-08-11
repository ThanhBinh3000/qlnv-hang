package com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetkehoachbandaugia;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.response.IdAndNameDto;
import com.tcdt.qlnvhang.util.LocalDateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BhQdPheDuyetKhbdgSearchResponse {
	private Long id;
	private String maTongHop;
	private LocalDate ngayTongHop;
	private String noiDungTongHop;
	private Integer namKeHoach;
	private IdAndNameDto qdPheDuyetKhbdg = new IdAndNameDto();
	private IdAndNameDto trangThai = new IdAndNameDto();

	private IdAndNameDto vatTuCha = new IdAndNameDto();

	private IdAndNameDto vatTu = new IdAndNameDto();


	public BhQdPheDuyetKhbdgSearchResponse(Object[] rawData) {
		if (Objects.nonNull(rawData[0])) this.id = (Long) rawData[0];
		if (Objects.nonNull(rawData[1])) this.maTongHop = (String) rawData[1];
		if (Objects.nonNull(rawData[2])) this.ngayTongHop = (LocalDate) rawData[2];
		if (Objects.nonNull(rawData[3])) this.noiDungTongHop = (String) rawData[3];
		if (Objects.nonNull(rawData[4])) this.namKeHoach = (Integer) rawData[4];
		if (Objects.nonNull(rawData[5])) {
			this.trangThai.setMa((String) rawData[5]);
			this.trangThai.setName(TrangThaiEnum.getTenById(this.trangThai.getMa()));
		}

		if (Objects.nonNull(rawData[6])) this.qdPheDuyetKhbdg.setId((Long) rawData[6]);
		if (Objects.nonNull(rawData[7])) this.qdPheDuyetKhbdg.setName((String) rawData[7]);
		if (Objects.nonNull(rawData[8])) this.vatTuCha.setName((String) rawData[8]);
	}

	public Object[] toExcel(String[] rowsName, Integer stt) {
		Object[] objs = new Object[rowsName.length];
		objs[0] = stt;
		objs[1] = LocalDateTimeUtils.localDateToString(this.ngayTongHop);
		objs[2] = this.noiDungTongHop;
		objs[3] = this.namKeHoach;
		objs[4] = Optional.ofNullable(this.qdPheDuyetKhbdg).map(IdAndNameDto::getName).orElse("");
		objs[5] =Optional.ofNullable(this.vatTuCha).map(IdAndNameDto::getName).orElse("");
		objs[6] =Optional.ofNullable(this.trangThai).map(IdAndNameDto::getName).orElse("");
		return objs;
	}

}
