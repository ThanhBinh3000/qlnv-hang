package com.tcdt.qlnvhang.request;

import lombok.Data;

@Data
public class BaseRequest {
	PaggingReq paggingReq;
	String trangThai;
	String str;
	String orderBy;
	String orderDirection;
}