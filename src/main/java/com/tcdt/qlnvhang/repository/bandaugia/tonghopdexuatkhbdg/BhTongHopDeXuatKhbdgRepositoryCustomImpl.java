package com.tcdt.qlnvhang.repository.bandaugia.tonghopdexuatkhbdg;

import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdg_;
import com.tcdt.qlnvhang.enums.Operator;
import com.tcdt.qlnvhang.request.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdgSearchRequest;
import com.tcdt.qlnvhang.util.query.QueryUtils;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Log4j2
public class BhTongHopDeXuatKhbdgRepositoryCustomImpl implements BhTongHopDeXuatKhbdgRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

//	@Override
//	public Page<BhTongHopDeXuatKhbdgSearchResponse> search(BhTongHopDeXuatKhbdgSearchRequest req, Pageable pageable) {
//		StringBuilder builder = new StringBuilder();
//		QueryUtils tongHopDeXuat = QueryUtils.builder().clazz(BhTongHopDeXuatKhbdg.class).alias("tongHopDeXuatKHBDG").build();
//		QueryUtils qdPheDuyet = QueryUtils.builder().clazz(BhQdPheDuyetKhbdg.class).alias("qdPheDuyetKHBDG").build();
//		QueryUtils loaiVthh = QueryUtils.builder().clazz(QlnvDmVattu.class).alias("loaiVthh").build();
//		log.debug("Build select query");
//		builder.append(QueryUtils.SELECT)
//				.append(tongHopDeXuat.selectField(BhTongHopDeXuatKhbdg_.ID))
//				.append(tongHopDeXuat.selectField(BhTongHopDeXuatKhbdg_.MA_TONG_HOP))
//				.append(tongHopDeXuat.selectField(BhTongHopDeXuatKhbdg_.NGAY_TONG_HOP))
//				.append(tongHopDeXuat.selectField(BhTongHopDeXuatKhbdg_.NOI_DUNG_TONG_HOP))
//				.append(tongHopDeXuat.selectField(BhTongHopDeXuatKhbdg_.NAM_KE_HOACH))
//				.append(tongHopDeXuat.selectField(BhTongHopDeXuatKhbdg_.TRANG_THAI))
//				.append(qdPheDuyet.selectField(BhQdPheDuyetKhbdg_.ID))
//				.append(qdPheDuyet.selectField(BhQdPheDuyetKhbdg_.SO_QUYET_DINH))
//				.append(loaiVthh.selectField(QlnvDmVattu_.TEN));
//		builder.append(QueryUtils.FROM)
//				.append(tongHopDeXuat.buildAliasName())
//				.append(QueryUtils.buildInnerJoin(tongHopDeXuat, loaiVthh, BhTongHopDeXuatKhbdg_.LOAI_VTHH, QlnvDmVattu_.MA))
//				.append(QueryUtils.buildLeftJoin(tongHopDeXuat, qdPheDuyet, BhTongHopDeXuatKhbdg_.ID, BhQdPheDuyetKhbdg_.TONG_HOP_DE_XUAT_KHBDG_ID));
//
//		setConditionSearch(req, builder, tongHopDeXuat);
//		log.debug("Set sort");
//		QueryUtils.buildSort(pageable, builder);
//
//		TypedQuery<Object[]> query = em.createQuery(QueryUtils.buildQuery(builder), Object[].class);
//
//		log.debug("Set params");
//		this.setParameterSearch(req, query, tongHopDeXuat);
//
//		log.info("Set pageable");
//		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());
//		List<Object[]> result = query.getResultList();
//
//		List<BhTongHopDeXuatKhbdgSearchResponse> responses = result.stream()
//				.map(BhTongHopDeXuatKhbdgSearchResponse::new).collect(Collectors.toList());
//
//		return new PageImpl<>(responses, pageable, this.count(req, tongHopDeXuat));
//	}


	private void setConditionSearch(BhTongHopDeXuatKhbdgSearchRequest req, StringBuilder builder, QueryUtils tongHopDeXuat) {
		QueryUtils.buildWhereClause(builder);
		tongHopDeXuat.eq(Operator.AND, BhTongHopDeXuatKhbdg_.NAM_KE_HOACH, req.getNamKeHoach(), builder);
		tongHopDeXuat.eq(Operator.AND, BhTongHopDeXuatKhbdg_.LOAI_VTHH, req.getLoaiVthh(), builder);
		tongHopDeXuat.like(Operator.AND, BhTongHopDeXuatKhbdg_.NOI_DUNG_TONG_HOP, req.getNoiDungTongHop(), builder);
		tongHopDeXuat.start(Operator.AND, BhTongHopDeXuatKhbdg_.NGAY_TONG_HOP, req.getNgayTongHopTuNgay(), builder);
		tongHopDeXuat.end(Operator.AND, BhTongHopDeXuatKhbdg_.NGAY_TONG_HOP, req.getNgayTongHopDenNgay(), builder);

	}

	private int count(BhTongHopDeXuatKhbdgSearchRequest req, QueryUtils tongHopDeXuat) {
		StringBuilder builder = new StringBuilder();
		builder.append(tongHopDeXuat.countBy(BhTongHopDeXuatKhbdg_.ID));
		setConditionSearch(req, builder, tongHopDeXuat);

		TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);

		this.setParameterSearch(req, query, tongHopDeXuat);
		return query.getSingleResult().intValue();
	}

	private void setParameterSearch(BhTongHopDeXuatKhbdgSearchRequest req, Query query, QueryUtils tongHopDeXuat) {
		tongHopDeXuat.setParam(query, BhTongHopDeXuatKhbdg_.NAM_KE_HOACH, req.getNamKeHoach());
		tongHopDeXuat.setParam(query, BhTongHopDeXuatKhbdg_.LOAI_VTHH, req.getLoaiVthh());
		tongHopDeXuat.setLikeParam(query, BhTongHopDeXuatKhbdg_.NOI_DUNG_TONG_HOP, req.getNoiDungTongHop());
		tongHopDeXuat.setParamStart(query, BhTongHopDeXuatKhbdg_.NGAY_TONG_HOP, req.getNgayTongHopTuNgay());
		tongHopDeXuat.setParamEnd(query, BhTongHopDeXuatKhbdg_.NGAY_TONG_HOP, req.getNgayTongHopDenNgay());
	}
}
