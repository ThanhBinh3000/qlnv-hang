package com.tcdt.qlnvhang.response.banhangdaugia.bienbanlaymau;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.util.DataUtils;
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
public class XhBbLayMauSearchResponse {
	private Long id;
	private String soBienBan;
	private LocalDate ngayLayMau;
	private String maDiemKho;
	private String tenDiemKho;
	private String maNhaKho;
	private String tenNhaKho;
	private String maNganKho;
	private String tenNganKho;
	private String maNganLo;
	private String tenNganLo;
	private String trangThaiId;
	private String tenTrangThai;


	public XhBbLayMauSearchResponse(Object[] rawData) {
		if (Objects.nonNull(rawData[0])) this.id = (Long) rawData[0];
		if (Objects.nonNull(rawData[1])) this.soBienBan = (String) rawData[1];
		if (Objects.nonNull(rawData[2])) this.ngayLayMau = (LocalDate) rawData[2];
		if (Objects.nonNull(rawData[3])) this.maDiemKho = (String) rawData[3];
		if (Objects.nonNull(rawData[4])) this.tenDiemKho = (String) rawData[4];
		if (Objects.nonNull(rawData[5])) this.maNhaKho = (String) rawData[5];
		if (Objects.nonNull(rawData[6])) this.tenNhaKho = (String) rawData[6];
		if (Objects.nonNull(rawData[7])) this.maNganKho = (String) rawData[7];
		if (Objects.nonNull(rawData[8])) this.tenNganKho = (String) rawData[8];
		if (Objects.nonNull(rawData[9])) this.maNganLo = (String) rawData[9];
		if (Objects.nonNull(rawData[10])) this.tenNganLo = (String) rawData[10];
		if (Objects.nonNull(rawData[11])) {
			this.trangThaiId = (String) rawData[11];
			this.tenTrangThai = NhapXuatHangTrangThaiEnum.getTenById(this.trangThaiId);
		}
	}

	public Object[] toExcel(String[] rowsName, Integer stt) {
		Object[] objs = new Object[rowsName.length];
		objs[0] = stt;
		objs[1] = DataUtils.toStringValue(this.soBienBan);
		objs[2] = DataUtils.toStringValue(LocalDateTimeUtils.localDateToString(this.ngayLayMau));
		objs[3] = DataUtils.toStringValue(this.tenDiemKho);
		objs[4] = DataUtils.toStringValue(this.tenNhaKho);
		objs[5] = DataUtils.toStringValue(this.tenNganKho);
		objs[6] = DataUtils.toStringValue(this.tenNganLo);
		objs[7] = DataUtils.toStringValue(this.tenTrangThai);

		return objs;
	}
}
