package com.tcdt.qlnvhang.request;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Data
public class BaseRequest {

	public static final int DEFAULT_PAGE = 0;
	public static final int DEFAULT_LIMIT = 10;

	public static final String ORDER_BY = "id";

	public static final String ORDER_TYPE = "asc";

	PaggingReq paggingReq;
	String trangThai;
	String str;
	String orderBy;
	String orderDirection;

	public PaggingReq getPaggingReq() {
		if (this.paggingReq == null) {
			this.paggingReq = new PaggingReq(DEFAULT_LIMIT, DEFAULT_PAGE,ORDER_BY,ORDER_TYPE);
		}
		return this.paggingReq;
	}
}