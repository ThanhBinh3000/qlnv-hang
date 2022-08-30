package com.tcdt.qlnvhang.response;

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
public class IdAndNameDto {
	private Long id;
	private String name;
	private String ma;
	private String soQuyetDinh;

	public IdAndNameDto(Long id, String name) {
		this.id = id;
		this.name = name;
	}
}
