package com.tcdt.qlnvhang.repository.bandaugia.quyetdinhpheduyetkehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdg;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdg_;
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
		QueryUtils qdPheDuyetKhbdg = QueryUtils.builder().clazz(BhQdPheDuyetKhbdg.class).alias("qdPheDuyetKhbdg").build();
		QueryUtils qdPheDuyet = QueryUtils.builder().clazz(BhQdPheDuyetKhbdg.class).alias("qdPheDuyetKHBDG").build();
		QueryUtils vatTuHangHoa = QueryUtils.builder().clazz(QlnvDmVattu.class).alias("vatTuCha").build();
		log.debug("Build select query");
		builder.append(QueryUtils.SELECT)
				.append(qdPheDuyetKhbdg.selectField(BhQdPheDuyetKhbdg_.ID))
				.append(qdPheDuyetKhbdg.selectField(BhQdPheDuyetKhbdg_.MA_TONG_HOP))
				.append(qdPheDuyetKhbdg.selectField(BhQdPheDuyetKhbdg_.NGAY_TONG_HOP))
				.append(qdPheDuyetKhbdg.selectField(BhQdPheDuyetKhbdg_.NOI_DUNG_TONG_HOP))
				.append(qdPheDuyetKhbdg.selectField(BhQdPheDuyetKhbdg_.NAM_KE_HOACH))
				.append(qdPheDuyetKhbdg.selectField(BhQdPheDuyetKhbdg_.TRANG_THAI))
				.append(qdPheDuyet.selectField(BhQdPheDuyetKhbdg_.ID))
				.append(qdPheDuyet.selectField(BhQdPheDuyetKhbdg_.SO_QUYET_DINH))
				.append(vatTuHangHoa.selectField(QlnvDmVattu_.TEN));
		builder.append(QueryUtils.FROM)
				.append(qdPheDuyetKhbdg.buildAliasName())
				.append(QueryUtils.buildInnerJoin(qdPheDuyetKhbdg, vatTuHangHoa, BhQdPheDuyetKhbdg_.MA_VAT_TU_CHA, QlnvDmVattu_.MA))
				.append(QueryUtils.buildLeftJoin(qdPheDuyetKhbdg, qdPheDuyet, BhQdPheDuyetKhbdg_.QD_PHE_DUYET_KHBDG_ID, BhQdPheDuyetKhbdg_.ID));

		setConditionSearch(req, builder, qdPheDuyetKhbdg);
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

		return new PageImpl<>(responses, pageable, this.count(req, qdPheDuyetKhbdg));
	}


	private void setConditionSearch(BhQdPheDuyetKhbdgSearchRequest req, StringBuilder builder, QueryUtils tongHopDeXuat) {
		QueryUtils.buildWhereClause(builder);
		tongHopDeXuat.eq(Operator.AND, BhQdPheDuyetKhbdg_.NAM_KE_HOACH, req.getNamKeHoach(), builder);
		tongHopDeXuat.eq(Operator.AND, BhQdPheDuyetKhbdg_.MA_VAT_TU_CHA, req.getMaVatTuCha(), builder);
		tongHopDeXuat.eq(Operator.AND, BhQdPheDuyetKhbdg_.NOI_DUNG_TONG_HOP, req.getNoiDungTongHop(), builder);
	}

	private int count(BhQdPheDuyetKhbdgSearchRequest req, QueryUtils tongHopDeXuat) {
		StringBuilder builder = new StringBuilder();
		builder.append(tongHopDeXuat.countBy(BhQdPheDuyetKhbdg_.ID));
		setConditionSearch(req, builder, tongHopDeXuat);

		TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);

		this.setParameterSearch(req, query);
		return query.getSingleResult().intValue();
	}

	private void setParameterSearch(BhQdPheDuyetKhbdgSearchRequest req, Query query) {
		QueryUtils.setParam(query, BhQdPheDuyetKhbdg_.NAM_KE_HOACH, req.getNamKeHoach());
		QueryUtils.setParam(query, BhQdPheDuyetKhbdg_.MA_VAT_TU_CHA, req.getMaVatTuCha());
		QueryUtils.setLikeParam(query, BhQdPheDuyetKhbdg_.NOI_DUNG_TONG_HOP, req.getNoiDungTongHop());
	}
}
