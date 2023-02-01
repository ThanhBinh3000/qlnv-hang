package com.tcdt.qlnvhang.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.util.Contains;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
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
	@Column(columnDefinition = "Date")
	private Date ngayTao;

	@Column(columnDefinition = "Number")
	private Long nguoiTaoId;

	@Transient
	private String tenNguoiTao;

	@Column(columnDefinition = "Date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	private Date ngaySua;

	@Column(columnDefinition = "Number")
	private Long nguoiSuaId;

	@Column(columnDefinition = "Number")
	private Long nguoiGuiDuyetId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	@Column(columnDefinition = "Date")
	private Date ngayGuiDuyet;

	@Column(columnDefinition = "Number")
	private Long nguoiPduyetId;

	@Transient
	private String tenNguoiPduyet;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	@Column(columnDefinition = "Date")
	private Date ngayPduyet;

	private String lyDoTuChoi;

	public String getTenTrangThai() {
		return NhapXuatHangTrangThaiEnum.getTenById(trangThai);
	}
}