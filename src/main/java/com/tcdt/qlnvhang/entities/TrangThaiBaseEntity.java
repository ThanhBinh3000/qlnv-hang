package com.tcdt.qlnvhang.entities;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class TrangThaiBaseEntity {

	private String trangThai;

	@Transient
	private String tenTrangThai;

	private LocalDate ngayTao;

	private Long nguoiTaoId;

	@Transient
	private String tenNguoiTao;

	private LocalDate ngaySua;

	private Long nguoiSuaId;

	private Long nguoiGuiDuyetId;

	private LocalDate ngayGuiDuyet;

	private Long nguoiPduyetId;

	@Transient
	private String tenNguoiPduyet;

	private LocalDate ngayPduyet;

	private String lyDoTuChoi;

	public String getTenTrangThai() {
		return NhapXuatHangTrangThaiEnum.getTenById(trangThai);
	}
}