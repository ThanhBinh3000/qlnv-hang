package com.tcdt.qlnvhang.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class DiemKhoEntity {
	@Id
	Long id;
	String maDiemKho;
	String tenDiemKho;
	String diaChi;
	String toaDo;
	String maDvi;
	String maKho;
	String tenKho;
}
