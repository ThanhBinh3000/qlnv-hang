package com.tcdt.qlnvhang.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {
	private LocalDate ngayTao;
	private Long nguoiTaoId;
	private LocalDate ngaySua;
	private Long nguoiSuaId;
}