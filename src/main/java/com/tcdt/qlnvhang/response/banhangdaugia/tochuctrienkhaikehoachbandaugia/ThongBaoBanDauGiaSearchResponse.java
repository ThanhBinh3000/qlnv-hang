package com.tcdt.qlnvhang.response.banhangdaugia.tochuctrienkhaikehoachbandaugia;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.LocalDateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThongBaoBanDauGiaSearchResponse {
	private Long id;
	private String maThongBao;
	private String qdPheDuyetKhbdg;
	private LocalDate ngayToChuc;
	private String trichYeu;
	private String hinhThucDauGia;
	private String phuongThucDauGia;
	private String loaiHangHoa;
	private Integer namKeHoach;
	private String thongBaoDGKhongThanhCong;
	private String bienBanBDG;
	private String qdPheDuyetKQBdg;
	private String trangThai;

	public ThongBaoBanDauGiaSearchResponse(Object[] rawData) {
		if (Objects.nonNull(rawData[0])) this.id = (Long) rawData[0];
		if (Objects.nonNull(rawData[1])) this.maThongBao = (String) rawData[1];
		if (Objects.nonNull(rawData[2])) this.qdPheDuyetKhbdg = (String) rawData[2];
		if (Objects.nonNull(rawData[3])) this.ngayToChuc = (LocalDate) rawData[3];
		if (Objects.nonNull(rawData[4])) this.trichYeu = (String) rawData[4];
		if (Objects.nonNull(rawData[5])) this.hinhThucDauGia = (String) rawData[5];
		if (Objects.nonNull(rawData[6])) this.namKeHoach = (Integer) rawData[6];
		if (Objects.nonNull(rawData[7])) this.trangThai = (String) rawData[7];
		String trangThaiId = (String) rawData[7];
		if (!StringUtils.isEmpty(trangThaiId)) {
			this.trangThai = TrangThaiEnum.getTenById(trangThaiId);
		}
		if (Objects.nonNull(rawData[8])) this.qdPheDuyetKQBdg = (String) rawData[8];
		if (Objects.nonNull(rawData[9])) this.loaiHangHoa = (String) rawData[9];
		if (Objects.nonNull(rawData[10])) this.bienBanBDG = (String) rawData[10];
	}

	public Object[] toExcel(String[] rowsName, Integer stt) {
		Object[] objs = new Object[rowsName.length];
		objs[0] = stt;
		objs[1] = DataUtils.toStringValue(this.maThongBao);
		objs[2] = DataUtils.toStringValue(this.qdPheDuyetKhbdg);
		objs[3] = DataUtils.toStringValue(LocalDateTimeUtils.localDateToString(this.ngayToChuc));
		objs[4] = DataUtils.toStringValue(this.trichYeu);
		objs[5] = DataUtils.toStringValue(this.hinhThucDauGia);
		objs[6] = DataUtils.toStringValue(this.phuongThucDauGia);
		objs[7] = DataUtils.toStringValue(this.loaiHangHoa);
		objs[8] = DataUtils.toStringValue(this.namKeHoach);
		objs[9] = DataUtils.toStringValue(this.thongBaoDGKhongThanhCong);
		objs[10] = DataUtils.toStringValue(this.bienBanBDG);
		objs[11] = DataUtils.toStringValue(this.qdPheDuyetKQBdg);
		objs[12] = DataUtils.toStringValue(this.trangThai);
		return objs;
	}
}
