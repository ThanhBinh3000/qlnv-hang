package com.tcdt.qlnvhang.repository.bandaugia.bienbanlaymau;

import com.tcdt.qlnvhang.entities.bandaugia.bienbanlaymau.XhBbLayMau;
import com.tcdt.qlnvhang.entities.bandaugia.bienbanlaymau.XhBbLayMau_;
import com.tcdt.qlnvhang.enums.Operator;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauSearchRequest;
import com.tcdt.qlnvhang.response.banhangdaugia.bienbanlaymau.XhBbLayMauSearchResponse;
import com.tcdt.qlnvhang.table.khotang.*;
import com.tcdt.qlnvhang.util.query.QueryUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class XhBbLayMauRepositoryCustomImpl implements XhBbLayMauRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<XhBbLayMauSearchResponse> search(XhBbLayMauSearchRequest req, Pageable pageable) {
		StringBuilder builder = new StringBuilder();
		QueryUtils xhBbLayMau = QueryUtils.builder().clazz(XhBbLayMau.class).alias("xhBbLayMau").build();
		QueryUtils diemKho = QueryUtils.builder().clazz(KtDiemKho.class).alias("diemKho").build();
		QueryUtils nhaKho = QueryUtils.builder().clazz(KtNhaKho.class).alias("nhaKho").build();
		QueryUtils nganKho = QueryUtils.builder().clazz(KtNganKho.class).alias("nganKho").build();
		QueryUtils nganLo = QueryUtils.builder().clazz(KtNganLo.class).alias("nganLo").build();


		log.debug("Build select query");
		builder.append(QueryUtils.SELECT);
		QueryUtils.selectFields(builder, xhBbLayMau, XhBbLayMau_.ID);
		QueryUtils.selectFields(builder, xhBbLayMau, XhBbLayMau_.SO_BIEN_BAN);
		QueryUtils.selectFields(builder, xhBbLayMau, XhBbLayMau_.NGAY_LAY_MAU);
		QueryUtils.selectFields(builder, diemKho, KtDiemKho_.MA_DIEMKHO);
		QueryUtils.selectFields(builder, diemKho, KtDiemKho_.TEN_DIEMKHO);
		QueryUtils.selectFields(builder, nhaKho, KtNhaKho_.MA_NHAKHO);
		QueryUtils.selectFields(builder, nhaKho, KtNhaKho_.TEN_NHAKHO);
		QueryUtils.selectFields(builder, nganKho, KtNganKho_.MA_NGANKHO);
		QueryUtils.selectFields(builder, nganKho, KtNganKho_.TEN_NGANKHO);
		QueryUtils.selectFields(builder, nganLo, KtNganLo_.MA_NGANLO);
		QueryUtils.selectFields(builder, nganLo, KtNganLo_.TEN_NGANLO);
		QueryUtils.selectFields(builder, xhBbLayMau, XhBbLayMau_.TRANG_THAI);

		builder.append(QueryUtils.FROM)
				.append(xhBbLayMau.buildAliasName())
				.append(QueryUtils.buildInnerJoin(xhBbLayMau, diemKho, XhBbLayMau_.MA_DIEM_KHO, KtDiemKho_.MA_DIEMKHO))
				.append(QueryUtils.buildInnerJoin(xhBbLayMau, nhaKho, XhBbLayMau_.MA_NHA_KHO, KtNhaKho_.MA_NHAKHO))
				.append(QueryUtils.buildInnerJoin(xhBbLayMau, nganKho, XhBbLayMau_.MA_NGAN_KHO, KtNganKho_.MA_NGANKHO))
				.append(QueryUtils.buildInnerJoin(xhBbLayMau, nganLo, XhBbLayMau_.MA_NGAN_LO, KtNganLo_.MA_NGANLO));

		log.debug("Set Condition search");
		this.setConditionSearch(req, builder, xhBbLayMau);

		log.debug("Set sort");
		QueryUtils.buildSort(pageable, builder);

		log.debug("Create query");
		TypedQuery<Object[]> query = em.createQuery(QueryUtils.buildQuery(builder), Object[].class);

		log.debug("Set params");
		this.setParameterSearch(req, query, xhBbLayMau);
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

		log.info("Build response");
		List<Object[]> result = query.getResultList();
		List<XhBbLayMauSearchResponse> responses = result.stream()
				.map(XhBbLayMauSearchResponse::new).collect(Collectors.toList());

		return new PageImpl<>(responses, pageable, this.count(req, xhBbLayMau, diemKho, nhaKho, nganKho, nganLo));
	}


	private void setConditionSearch(XhBbLayMauSearchRequest req, StringBuilder builder, QueryUtils xhBbLayMau) {
		QueryUtils.buildWhereClause(builder);
		xhBbLayMau.eq(Operator.AND, XhBbLayMau_.SO_BIEN_BAN, req.getSoBienBan(), builder);
		xhBbLayMau.start(Operator.AND, XhBbLayMau_.NGAY_LAY_MAU, req.getNgayLayMauTuNgay(), builder);
		xhBbLayMau.end(Operator.AND, XhBbLayMau_.NGAY_LAY_MAU, req.getNgayLayMauDenNgay(), builder);
		xhBbLayMau.eq(Operator.AND, XhBbLayMau_.MA_DIEM_KHO, req.getMaDiemKho(), builder);
		xhBbLayMau.eq(Operator.AND, XhBbLayMau_.MA_NHA_KHO, req.getMaNhaKho(), builder);
		xhBbLayMau.eq(Operator.AND, XhBbLayMau_.MA_NGAN_KHO, req.getMaNganKho(), builder);
		xhBbLayMau.eq(Operator.AND, XhBbLayMau_.MA_NGAN_LO, req.getMaNganLo(), builder);
	}

	private int count(XhBbLayMauSearchRequest req, QueryUtils xhBbLayMau, QueryUtils diemKho, QueryUtils nhaKho, QueryUtils nganKho, QueryUtils nganLo) {
		log.debug("Build count query");
		StringBuilder builder = xhBbLayMau.countBy(XhBbLayMau_.ID);

		builder.append(QueryUtils.buildInnerJoin(xhBbLayMau, diemKho, XhBbLayMau_.MA_DIEM_KHO, KtDiemKho_.MA_DIEMKHO))
				.append(QueryUtils.buildInnerJoin(xhBbLayMau, nhaKho, XhBbLayMau_.MA_NHA_KHO, KtNhaKho_.MA_NHAKHO))
				.append(QueryUtils.buildInnerJoin(xhBbLayMau, nganKho, XhBbLayMau_.MA_NGAN_KHO, KtNganKho_.MA_NGANKHO))
				.append(QueryUtils.buildInnerJoin(xhBbLayMau, nganLo, XhBbLayMau_.MA_NGAN_LO, KtNganLo_.MA_NGANLO));

		log.debug("Set condition search");
		this.setConditionSearch(req, builder, xhBbLayMau);

		log.debug("Create query");
		TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);

		log.debug("Set parameter");
		this.setParameterSearch(req, query, xhBbLayMau);

		return query.getSingleResult().intValue();
	}

	private void setParameterSearch(XhBbLayMauSearchRequest req, Query query, QueryUtils xhBbLayMau) {
		xhBbLayMau.setParam(query, XhBbLayMau_.SO_BIEN_BAN, req.getSoBienBan());
		xhBbLayMau.setParamStart(query, XhBbLayMau_.NGAY_LAY_MAU, req.getNgayLayMauTuNgay());
		xhBbLayMau.setParamEnd(query, XhBbLayMau_.NGAY_LAY_MAU, req.getNgayLayMauTuNgay());
		xhBbLayMau.setParam(query, XhBbLayMau_.MA_DIEM_KHO, req.getMaDiemKho());
		xhBbLayMau.setParam(query, XhBbLayMau_.MA_NHA_KHO, req.getMaNhaKho());
		xhBbLayMau.setParam(query, XhBbLayMau_.MA_NGAN_KHO, req.getMaNganKho());
		xhBbLayMau.setParam(query, XhBbLayMau_.MA_NGAN_LO, req.getMaNganLo());
	}
}
