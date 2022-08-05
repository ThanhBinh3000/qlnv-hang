package com.tcdt.qlnvhang.response.banhangdaugia.tochuctrienkhaikehoachbandaugia;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
		if (Objects.nonNull(rawData[8])) this.qdPheDuyetKQBdg = (String) rawData[8];
		if (Objects.nonNull(rawData[9])) this.loaiHangHoa = (String) rawData[9];
		if (Objects.nonNull(rawData[10])) this.bienBanBDG = (String) rawData[10];
	}

	public Object[] toExcel(String[] rowsName, Integer stt) {
		Object[] objs = new Object[rowsName.length];
		objs[0] = stt;
		return objs;
	}
}
