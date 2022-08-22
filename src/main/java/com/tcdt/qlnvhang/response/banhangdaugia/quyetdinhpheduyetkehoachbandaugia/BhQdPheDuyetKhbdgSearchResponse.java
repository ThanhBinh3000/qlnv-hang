package com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetkehoachbandaugia;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.util.LocalDateTimeUtils;
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
public class BhQdPheDuyetKhbdgSearchResponse {
	private Long id;
	private String soQuyetDinh;
	private LocalDate ngayKy;
	private String trichYeu;
	private Integer namKeHoach;
	private String maTongHop;
	private String loaiHangHoa;
	private String trangThai;


	public BhQdPheDuyetKhbdgSearchResponse(Object[] rawData) {
		if (Objects.nonNull(rawData[0])) this.soQuyetDinh = (String) rawData[0];
		if (Objects.nonNull(rawData[1])) this.ngayKy = (LocalDate) rawData[1];
		if (Objects.nonNull(rawData[2])) this.trichYeu = (String) rawData[2];
		if (Objects.nonNull(rawData[3])) this.maTongHop = (String) rawData[3];
		if (Objects.nonNull(rawData[4])) this.namKeHoach = (Integer) rawData[4];
		if (Objects.nonNull(rawData[5])) this.loaiHangHoa = (String) rawData[5];
		if (Objects.nonNull(rawData[6])) {
			String trangThaiId = (String) rawData[6];
			this.trangThai = NhapXuatHangTrangThaiEnum.getTenById(trangThaiId);
		}
		this.id = (Long) rawData[7];
	}

	public Object[] toExcel(String[] rowsName, Integer stt) {
		Object[] objs = new Object[rowsName.length];
		objs[0] = stt;
		objs[1] = this.soQuyetDinh;
		objs[2] = LocalDateTimeUtils.localDateToString(this.ngayKy);
		objs[3] = this.trichYeu;
		objs[4] = this.maTongHop;
		objs[5] = this.namKeHoach;
		objs[6] = this.loaiHangHoa;
		objs[7] = this.trangThai;
		return objs;
	}

}
