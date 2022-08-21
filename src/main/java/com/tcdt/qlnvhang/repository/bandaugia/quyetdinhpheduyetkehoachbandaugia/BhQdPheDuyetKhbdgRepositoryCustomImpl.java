package com.tcdt.qlnvhang.repository.bandaugia.quyetdinhpheduyetkehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdg;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdg_;
import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdg;
import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdg_;
import com.tcdt.qlnvhang.enums.Operator;
import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetkehochbandaugia.BhQdPheDuyetKhbdgSearchRequest;
import com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdgSearchResponse;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu_;
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
public class BhQdPheDuyetKhbdgRepositoryCustomImpl implements BhQdPheDuyetKhbdgRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<BhQdPheDuyetKhbdgSearchResponse> search(BhQdPheDuyetKhbdgSearchRequest req, Pageable pageable) {
		StringBuilder builder = new StringBuilder();
		QueryUtils qdPheDuyet = QueryUtils.builder().clazz(BhQdPheDuyetKhbdg.class).alias("qdPheDuyetKHBDG").build();
		QueryUtils vatTuHangHoa = QueryUtils.builder().clazz(QlnvDmVattu.class).alias("vatTuCha").build();
		QueryUtils tongHopDeXuat = QueryUtils.builder().clazz(BhTongHopDeXuatKhbdg.class).alias("tongHopDeXuat").build();
		log.debug("Build select query");
		builder.append(QueryUtils.SELECT)
				.append(qdPheDuyet.selectField(BhQdPheDuyetKhbdg_.SO_QUYET_DINH))
				.append(qdPheDuyet.selectField(BhQdPheDuyetKhbdg_.NGAY_KY))
				.append(qdPheDuyet.selectField(BhQdPheDuyetKhbdg_.TRICH_YEU))
				.append(tongHopDeXuat.selectField(BhTongHopDeXuatKhbdg_.MA_TONG_HOP))
				.append(qdPheDuyet.selectField(BhQdPheDuyetKhbdg_.NAM_KE_HOACH))
				.append(vatTuHangHoa.selectField(QlnvDmVattu_.TEN))
				.append(vatTuHangHoa.selectField(QlnvDmVattu_.TRANG_THAI))
				.append(qdPheDuyet.selectField(BhQdPheDuyetKhbdg_.ID));
		builder.append(QueryUtils.FROM)
				.append(qdPheDuyet.buildAliasName())
				.append(QueryUtils.buildInnerJoin(qdPheDuyet, vatTuHangHoa, BhQdPheDuyetKhbdg_.MA_VAT_TU_CHA, QlnvDmVattu_.MA))
				.append(QueryUtils.buildLeftJoin(qdPheDuyet, tongHopDeXuat, BhQdPheDuyetKhbdg_.TONG_HOP_DE_XUAT_KHBDG_ID, BhTongHopDeXuatKhbdg_.ID));

		this.setConditionSearch(req, builder, qdPheDuyet);
		log.debug("Set sort");
		QueryUtils.buildSort(pageable, builder);

		TypedQuery<Object[]> query = em.createQuery(QueryUtils.buildQuery(builder), Object[].class);

		log.debug("Set params");
		this.setParameterSearch(req, query);

		log.info("Set pageable");
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());
		List<Object[]> result = query.getResultList();

		List<BhQdPheDuyetKhbdgSearchResponse> responses = result.stream()
				.map(BhQdPheDuyetKhbdgSearchResponse::new).collect(Collectors.toList());

		return new PageImpl<>(responses, pageable, this.count(req, qdPheDuyet));
	}


	private void setConditionSearch(BhQdPheDuyetKhbdgSearchRequest req, StringBuilder builder, QueryUtils qdPheDuyet) {
		QueryUtils.buildWhereClause(builder);
		qdPheDuyet.eq(Operator.AND, BhQdPheDuyetKhbdg_.NAM_KE_HOACH, req.getNamKeHoach(), builder);
		qdPheDuyet.eq(Operator.AND, BhQdPheDuyetKhbdg_.SO_QUYET_DINH, req.getSoQuyetDinh(), builder);
		qdPheDuyet.like(Operator.AND, BhQdPheDuyetKhbdg_.TRICH_YEU, req.getTrichYeu(), builder);
		qdPheDuyet.start(Operator.AND, BhQdPheDuyetKhbdg_.NGAY_KY, req.getNgayKyTuNgay(), builder);
		qdPheDuyet.end(Operator.AND, BhQdPheDuyetKhbdg_.NGAY_KY, req.getNgayKyDenNgay(), builder);
	}

	private int count(BhQdPheDuyetKhbdgSearchRequest req, QueryUtils qdPheDuyet) {
		StringBuilder builder = new StringBuilder();
		builder.append(qdPheDuyet.countBy(BhQdPheDuyetKhbdg_.ID));
		setConditionSearch(req, builder, qdPheDuyet);

		TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);

		this.setParameterSearch(req, query);
		return query.getSingleResult().intValue();
	}

	private void setParameterSearch(BhQdPheDuyetKhbdgSearchRequest req, Query query) {
		QueryUtils.setParam(query, BhQdPheDuyetKhbdg_.NAM_KE_HOACH, req.getNamKeHoach());
		QueryUtils.setParam(query, BhQdPheDuyetKhbdg_.SO_QUYET_DINH, req.getSoQuyetDinh());
		QueryUtils.setLikeParam(query, BhQdPheDuyetKhbdg_.TRICH_YEU, req.getTrichYeu());
		QueryUtils.setParamStart(query, BhQdPheDuyetKhbdg_.NGAY_KY, req.getNgayKyTuNgay());
		QueryUtils.setParamEnd(query, BhQdPheDuyetKhbdg_.NGAY_KY, req.getNgayKyDenNgay());
	}
}
