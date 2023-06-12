package com.tcdt.qlnvhang.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReportTemplateResponse {
	private String pdfSrc;
	private String wordSrc;

	public ReportTemplateResponse(String pdfSrc, String wordSrc) {
		this.pdfSrc = pdfSrc;
		this.wordSrc = wordSrc;
	}
}