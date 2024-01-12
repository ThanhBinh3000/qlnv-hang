package com.tcdt.qlnvhang.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode()
@Entity
@Table(name ="REPORT_TEMPLATE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportTemplate {

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REPORT_TEMPLATE_SEQ")
	@SequenceGenerator(sequenceName = "REPORT_TEMPLATE_SEQ", allocationSize = 1, name = "REPORT_TEMPLATE_SEQ")
	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "TEN")
	private String ten;

	@Column(name = "TEN_FILE")
	private String tenFile;

	@Column(name = "FILE_UPLOAD")
	@Lob()
	private byte[] fileUpload;

	@Column(name = "TRANG_THAI")
	private String trangThai;

	@Column(name = "MO_TA")
	private String moTa;

	public ReportTemplate(String tenFile, String ten, Long nguoiTaoId) {
		this.tenFile = tenFile;
		this.ten = ten;

	}
}