package com.tcdt.qlnvhang.entities;

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
	private LocalDate ngaySua;
	private Long nguoiSuaId;
	private Long nguoiGuiDuyetId;
	private LocalDate ngayGuiDuyet;
	private Long nguoiPduyetId;
	private LocalDate ngayPduyet;
	private String lyDoTuChoi;
}