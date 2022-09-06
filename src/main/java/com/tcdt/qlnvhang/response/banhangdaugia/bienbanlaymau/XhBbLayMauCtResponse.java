package com.tcdt.qlnvhang.response.banhangdaugia.bienbanlaymau;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcdt.qlnvhang.response.CommonResponse;
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
public class XhBbLayMauCtResponse extends CommonResponse {
	private Long id;
	private Long xhBbLayMauId;
	private String loaiDaiDien;
	private String daiDien;
}
