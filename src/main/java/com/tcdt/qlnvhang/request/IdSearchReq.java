package com.tcdt.qlnvhang.request;

import javax.validation.constraints.NotNull;

import lombok.Data;

import java.util.List;

@Data
public class IdSearchReq {
	Long id;
	String maDvi;
	List<Long> idList;
	List<Long> ids;
}
