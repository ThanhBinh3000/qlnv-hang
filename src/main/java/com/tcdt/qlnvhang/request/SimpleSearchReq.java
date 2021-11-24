package com.tcdt.qlnvhang.request;

import lombok.Data;

@Data
public class SimpleSearchReq {
	
	Integer limit;
	Integer page;
	String name;
	String code;
}
