package com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class XhBbLayMauCtRequest {
	private Long id;
	private Long xhBbLayMauId;
	private String loaiDaiDien;
	private String daiDien;
}
