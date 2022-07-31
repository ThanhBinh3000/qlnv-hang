package com.tcdt.qlnvhang.repository.bandaugia.tonghopdexuatkhbdg;

import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdg;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdg_;
import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdg;
import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdg_;
import com.tcdt.qlnvhang.request.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgSearchRequest;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgSearchResponse;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class BhTongHopDeXuatKhbdgRepositoryCustomImpl implements BhTongHopDeXuatKhbdgRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<BhTongHopDeXuatKhbdgSearchResponse> search(BhTongHopDeXuatKhbdgSearchRequest req, Pageable pageable) {
		StringBuilder builder = new StringBuilder();
		QueryUtils tongHopDeXuat = QueryUtils.builder().clazz(BhTongHopDeXuatKhbdg.class).alias("tongHopDeXuatKhbdg").build();
		QueryUtils qdPheDuyet = QueryUtils.builder().clazz(BhQdPheDuyetKhbdg.class).alias("QdPheDuyetKhbdg").build();
		QueryUtils vatTuHangHoa = QueryUtils.builder().clazz(QlnvDmVattu.class).alias("vatTuCha").build();
		log.debug("Build select query");
		builder.append(QueryUtils.SELECT)
				.append(tongHopDeXuat.getAlias())
				.append(tongHopDeXuat.getField(BhTongHopDeXuatKhbdg_.MA_TONG_HOP))
				.append(tongHopDeXuat.getField(BhTongHopDeXuatKhbdg_.NGAY_TONG_HOP))
				.append(tongHopDeXuat.getField(BhTongHopDeXuatKhbdg_.NOI_DUNG_TONG_HOP))
				.append(tongHopDeXuat.getField(BhTongHopDeXuatKhbdg_.NAM_KE_HOACH))
				.append(tongHopDeXuat.getField(BhTongHopDeXuatKhbdg_.TRANG_THAI))
				.append(qdPheDuyet.getField(BhQdPheDuyetKhbdg_.SO_QUYET_DINH))
				.append(vatTuHangHoa.getField(QlnvDmVattu_.TEN));
		builder.append(QueryUtils.FROM)
				.append(tongHopDeXuat.buildAliasString())
				.append(QueryUtils.buildInnerJoin(tongHopDeXuat, vatTuHangHoa, BhTongHopDeXuatKhbdg_.LOAI_HANG_HOA, QlnvDmVattu_.MA))
				.append(QueryUtils.buildLeftJoin(tongHopDeXuat, qdPheDuyet, BhTongHopDeXuatKhbdg_.QD_PHE_DUYET_KHBDG_ID, BhQdPheDuyetKhbdg_.ID));

		setConditionSearch(req, builder, tongHopDeXuat);
		log.debug("Set sort");
		QueryUtils.buildSort(pageable, builder);

		TypedQuery<Object[]> query = em.createQuery(builder.toString(), Object[].class);

		log.debug("Set params");
		this.setParameterSearch(req, query);

		log.info("Set pageable");
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());
		List<Object[]> result = query.getResultList();

		List<BhTongHopDeXuatKhbdgSearchResponse> responses = result.stream()
				.flatMap(Arrays::stream)
				.filter(object -> object instanceof BhTongHopDeXuatKhbdgSearchResponse)
				.map(object -> (BhTongHopDeXuatKhbdgSearchResponse) object)
				.collect(Collectors.toList());

		return new PageImpl<>(responses, pageable, this.count(req, tongHopDeXuat));
	}


	private void setConditionSearch(BhTongHopDeXuatKhbdgSearchRequest req, StringBuilder builder, QueryUtils tongHopDeXuat) {
		builder.append(QueryUtils.WHERE).append(QueryUtils.DEFAULT_CONDITION);
		builder.append(QueryUtils.AND);
		tongHopDeXuat.eq(BhTongHopDeXuatKhbdg_.NAM_KE_HOACH, req.getNamKeHoach(), builder);
		builder.append(QueryUtils.AND);
		tongHopDeXuat.eq(BhTongHopDeXuatKhbdg_.LOAI_HANG_HOA, req.getLoaiHangHoa(), builder);
		builder.append(QueryUtils.AND);
		tongHopDeXuat.eq(BhTongHopDeXuatKhbdg_.NOI_DUNG_TONG_HOP, req.getNoiDungTongHop(), builder);
	}

	private int count(BhTongHopDeXuatKhbdgSearchRequest req, QueryUtils tongHopDeXuat) {
		StringBuilder builder = new StringBuilder();
		builder.append(tongHopDeXuat.countBy(BhTongHopDeXuatKhbdg_.ID));
		setConditionSearch(req, builder, tongHopDeXuat);

		TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);

		this.setParameterSearch(req, query);
		return query.getSingleResult().intValue();
	}

	private void setParameterSearch(BhTongHopDeXuatKhbdgSearchRequest req, Query query) {
		QueryUtils.setParam(query, BhTongHopDeXuatKhbdg_.NAM_KE_HOACH, req.getNamKeHoach());
		QueryUtils.setParam(query, BhTongHopDeXuatKhbdg_.LOAI_HANG_HOA, req.getLoaiHangHoa());
		QueryUtils.setLikeParam(query, BhTongHopDeXuatKhbdg_.NOI_DUNG_TONG_HOP, req.getNoiDungTongHop());
	}
}
