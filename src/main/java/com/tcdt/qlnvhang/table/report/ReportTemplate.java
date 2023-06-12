package com.tcdt.qlnvhang.table.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportTemplate  {
	private Long id;
	private String ten;
	private String tenFile;
	private String fileUpload;
	private String trangThai;
	private String moTa;

	public ReportTemplate(String tenFile, String ten, Long nguoiTaoId) {
		this.tenFile = tenFile;
		this.ten = ten;
	}
}