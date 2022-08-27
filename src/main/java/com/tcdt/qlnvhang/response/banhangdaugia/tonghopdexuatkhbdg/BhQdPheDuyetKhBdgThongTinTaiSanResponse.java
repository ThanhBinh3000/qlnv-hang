package com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg;

import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhBdgThongTinTaiSan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BhQdPheDuyetKhBdgThongTinTaiSanResponse {
	private Long id;
	private Long stt;
	private String maDiemKho;
	private String tenDiemKho;
	private String maNganKho;
	private String tenNganKho;
	private String maLoKho;
	private String tenLoKho;
	private String maNhaKho;
	private String tenNhaKho;
	private String chungLoaiHh;
	private String maDvTaiSan;
	private BigDecimal tonKho;
	private BigDecimal soLuong;
	private BigDecimal donGia;
	private String maChiCuc;
	private String tenChiCuc;
	private Long bhDgKehoachId;
	private BigDecimal giaKhoiDiem;
	private BigDecimal soTienDatTruoc;
	private String donViTinh;
	private String ghiChu;
	private Long qdPheDuyetKhbdgChiTietId;

	public BhQdPheDuyetKhBdgThongTinTaiSanResponse(BhQdPheDuyetKhBdgThongTinTaiSan item) {
		BeanUtils.copyProperties(item, this);
	}
}
