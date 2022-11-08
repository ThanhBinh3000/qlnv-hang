package com.tcdt.qlnvhang.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.util.Contains;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class TrangThaiBaseEntity {

	private String trangThai;

	@Transient
	private String tenTrangThai;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngayTao;

	private Long nguoiTaoId;

	@Transient
	private String tenNguoiTao;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngaySua;

	private Long nguoiSuaId;

	private Long nguoiGuiDuyetId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngayGuiDuyet;

	private Long nguoiPduyetId;

	@Transient
	private String tenNguoiPduyet;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngayPduyet;

	private String lyDoTuChoi;

	public String getTenTrangThai() {
		return NhapXuatHangTrangThaiEnum.getTenById(trangThai);
	}
}