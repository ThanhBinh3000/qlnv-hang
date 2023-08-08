package com.tcdt.qlnvhang.request;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.Data;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
public class BaseRequest {

	public static final int DEFAULT_PAGE = 0;
	public static final int DEFAULT_LIMIT = Integer.MAX_VALUE;

	public static final String ORDER_BY = "id";

	public static final String ORDER_TYPE = "asc";

	PaggingReq paggingReq;
	String trangThai;
	String str;
	Set<String> maDvis = new HashSet<>();
	Set<String> trangThais = new HashSet<>();
	Set<String> capDvis = new HashSet<>();
	String lyDoTuChoi;

	private List<FileDinhKemReq> fileDinhKemReq =new ArrayList<>();

	private List<Long> ids = new ArrayList<>();

	public PaggingReq getPaggingReq() {
		if (this.paggingReq == null) {
			this.paggingReq = new PaggingReq(DEFAULT_LIMIT, DEFAULT_PAGE,ORDER_BY,ORDER_TYPE);
		}
		return this.paggingReq;
	}

	public Set<String> getTrangThais() {
		if (!StringUtils.isEmpty(trangThai)) {
			this.trangThais.add(trangThai);
		}
		return this.trangThais;
	}

	public Pageable getPageable() {
		return PageRequest.of(this.getPaggingReq().page, this.getPaggingReq().limit);
	}
}