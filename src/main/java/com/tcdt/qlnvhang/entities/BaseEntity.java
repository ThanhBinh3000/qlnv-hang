package com.tcdt.qlnvhang.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.MappedSuperclass;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity {
	private LocalDate ngayTao;
	private Long nguoiTaoId;
	private LocalDate ngaySua;
	private Long nguoiSuaId;
}