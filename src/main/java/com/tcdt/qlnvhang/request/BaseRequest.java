package com.tcdt.qlnvhang.request;

import lombok.Data;

@Data
public class BaseRequest {

	public static final int DEFAULT_PAGE = 0;
	public static final int DEFAULT_LIMIT = 10;
	PaggingReq paggingReq;
	String trangThai;
	String str;
	String orderBy;
	String orderDirection;

	public PaggingReq getPaggingReq() {
		if (this.paggingReq == null) {
			this.paggingReq = new PaggingReq(DEFAULT_LIMIT, DEFAULT_PAGE);
		}
		return this.paggingReq;
	}
}