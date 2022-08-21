package com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetkehoachbandaugia;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhQdPheDuyetKhBdgThongTinTaiSanResponse;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhTongHopDeXuatCtResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BhQdPheDuyetKhbdgCtResponse {
	private Long id;
	private Long bhDgKeHoachId;
	private Long quyetDinhPheDuyetId;
	private String maDonVi;
	private LocalDate ngayKy;
	private String trichYeu;
	private BigDecimal soLuongDvTaiSan;
	private BigDecimal giaKhoiDiem;
	private BigDecimal khoanTienDatTruoc;
	private Long bhTongHopDeXuatId;
	private String tenDonVi;
	private String soKeHoach;
	List<BhQdPheDuyetKhBdgThongTinTaiSanResponse> thongTinTaiSans = new ArrayList<>();
	public BhQdPheDuyetKhbdgCtResponse(BhTongHopDeXuatCtResponse entry) {
		BeanUtils.copyProperties(entry, this, "id");
	}
}
