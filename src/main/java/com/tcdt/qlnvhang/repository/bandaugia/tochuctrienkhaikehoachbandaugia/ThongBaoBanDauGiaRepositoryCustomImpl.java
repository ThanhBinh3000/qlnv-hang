package com.tcdt.qlnvhang.repository.bandaugia.tochuctrienkhaikehoachbandaugia;

import com.tcdt.qlnvhang.entities.bandaugia.bienbanbandaugia.BhBbBanDauGia;
import com.tcdt.qlnvhang.entities.bandaugia.bienbanbandaugia.BhBbBanDauGia_;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdg;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdg_;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetketquabandaugia.BhQdPheDuyetKqbdg;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetketquabandaugia.BhQdPheDuyetKqbdg_;
import com.tcdt.qlnvhang.entities.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGia;
import com.tcdt.qlnvhang.entities.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGia_;
import com.tcdt.qlnvhang.entities.bandaugia.tonghopdexuatkhbdg.BhTongHopDeXuatKhbdg_;
import com.tcdt.qlnvhang.enums.Operator;
import com.tcdt.qlnvhang.request.bandaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaSearchRequest;
import com.tcdt.qlnvhang.response.banhangdaugia.tochuctrienkhaikehoachbandaugia.ThongBaoBanDauGiaSearchResponse;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi_;
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
public class ThongBaoBanDauGiaRepositoryCustomImpl implements ThongBaoBanDauGiaRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<ThongBaoBanDauGiaSearchResponse> search(ThongBaoBanDauGiaSearchRequest req, Pageable pageable) {
		StringBuilder builder = new StringBuilder();
		QueryUtils thongBaoBDG = QueryUtils.builder().clazz(ThongBaoBanDauGia.class).alias("tbBdg").build();
		QueryUtils qdPheDuyetKeHoachBdg = QueryUtils.builder().clazz(BhQdPheDuyetKhbdg.class).alias("qdPheDuyetKhBdg").build();
		QueryUtils qdPheDuyetKetQuaBdg = QueryUtils.builder().clazz(BhQdPheDuyetKqbdg.class).alias("qdPheDuyetKqBdg").build();
		QueryUtils vatTuHangHoa = QueryUtils.builder().clazz(QlnvDmVattu.class).alias("vtc").build();
		QueryUtils bienBanBDG = QueryUtils.builder().clazz(BhBbBanDauGia.class).alias("bbBdg").build();
		QueryUtils dmDonVi = QueryUtils.builder().clazz(QlnvDmDonvi.class).alias("dmDvi").build();

		log.debug("Build select query");
		builder.append(QueryUtils.SELECT).append(thongBaoBDG.selectField(ThongBaoBanDauGia_.ID))
				.append(thongBaoBDG.selectField(ThongBaoBanDauGia_.MA_THONG_BAO))
				.append(qdPheDuyetKeHoachBdg.selectField(BhQdPheDuyetKhbdg_.SO_QUYET_DINH))
				.append(thongBaoBDG.selectField(ThongBaoBanDauGia_.THOI_GIAN_TO_CHUC_DAU_GIA_TU_NGAY))
				.append(thongBaoBDG.selectField(ThongBaoBanDauGia_.TRICH_YEU))
				.append(thongBaoBDG.selectField(ThongBaoBanDauGia_.HINH_THUC_DAU_GIA))
				.append(thongBaoBDG.selectField(ThongBaoBanDauGia_.NAM_KE_HOACH))
				.append(thongBaoBDG.selectField(ThongBaoBanDauGia_.TRANG_THAI))
				.append(qdPheDuyetKetQuaBdg.selectField(BhQdPheDuyetKqbdg_.SO_QUYET_DINH))
				.append(vatTuHangHoa.selectField(QlnvDmVattu_.MA))
				.append(bienBanBDG.selectField(BhBbBanDauGia_.SO_BIEN_BAN))
				.append(thongBaoBDG.selectField(ThongBaoBanDauGia_.PHUONG_THUC_DAU_GIA))
				.append(thongBaoBDG.selectField(ThongBaoBanDauGia_.THOI_GIAN_TO_CHUC_DAU_GIA_DEN_NGAY))
				.append(thongBaoBDG.selectField(ThongBaoBanDauGia_.LOAI_VTHH))
				.append(vatTuHangHoa.selectField(QlnvDmVattu_.TEN))
				.append(dmDonVi.selectField(QlnvDmDonvi_.TEN_DVI));
		builder.append(QueryUtils.FROM)
				.append(thongBaoBDG.buildAliasName())
				.append(QueryUtils.buildInnerJoin(thongBaoBDG, vatTuHangHoa, ThongBaoBanDauGia_.MA_VAT_TU_CHA, QlnvDmVattu_.MA))
				.append(QueryUtils.buildLeftJoin(thongBaoBDG, qdPheDuyetKeHoachBdg, ThongBaoBanDauGia_.QD_PHE_DUYET_KH_BDG_ID, BhQdPheDuyetKhbdg_.ID))
				.append(QueryUtils.buildLeftJoin(thongBaoBDG, bienBanBDG, ThongBaoBanDauGia_.ID, BhBbBanDauGia_.THONG_BAO_BDG_ID))
				.append(QueryUtils.buildLeftJoin(thongBaoBDG, qdPheDuyetKetQuaBdg, ThongBaoBanDauGia_.ID, BhQdPheDuyetKqbdg_.THONG_BAO_BDG_ID))
				.append(QueryUtils.buildLeftJoin(thongBaoBDG, dmDonVi, ThongBaoBanDauGia_.MA_DON_VI, QlnvDmDonvi_.MA_DVI));

		log.debug("Set Condition search");
		this.setConditionSearch(req, builder, thongBaoBDG);

		log.debug("Set sort");
		QueryUtils.buildSort(pageable, builder);

		log.debug("Create query");
		TypedQuery<Object[]> query = em.createQuery(QueryUtils.buildQuery(builder), Object[].class);

		log.debug("Set params");
		this.setParameterSearch(req, query, thongBaoBDG);
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

		log.info("Build response");
		List<Object[]> result = query.getResultList();
		List<ThongBaoBanDauGiaSearchResponse> responses = result.stream()
				.map(ThongBaoBanDauGiaSearchResponse::new).collect(Collectors.toList());

		return new PageImpl<>(responses, pageable, this.count(req, thongBaoBDG));
	}


	private void setConditionSearch(ThongBaoBanDauGiaSearchRequest req, StringBuilder builder, QueryUtils thongBaoBDG) {
		QueryUtils.buildWhereClause(builder);
		thongBaoBDG.eq(Operator.AND, ThongBaoBanDauGia_.NAM_KE_HOACH, req.getNamKeHoach(), builder);
		thongBaoBDG.eq(Operator.AND, ThongBaoBanDauGia_.MA_VAT_TU_CHA, req.getMaVatTuCha(), builder);
		thongBaoBDG.eq(Operator.AND, ThongBaoBanDauGia_.TRANG_THAI, req.getTrangThai(), builder);
		thongBaoBDG.eq(Operator.AND, BhQdPheDuyetKhbdg_.SO_QUYET_DINH, req.getSoQuyetDinhPheDuyetKHBDG(), builder);
		thongBaoBDG.like(Operator.AND, ThongBaoBanDauGia_.MA_THONG_BAO, req.getMaThongBaoBDG(), builder);
		thongBaoBDG.like(Operator.AND, ThongBaoBanDauGia_.TRICH_YEU, req.getTrichYeu(), builder);
		thongBaoBDG.eq(Operator.AND, ThongBaoBanDauGia_.LOAI_VTHH, req.getLoaiVthh(), builder);
		thongBaoBDG.eq(Operator.AND, ThongBaoBanDauGia_.MA_DON_VI, req.getMaDvi(), builder);
		thongBaoBDG.start(Operator.AND, ThongBaoBanDauGia_.THOI_GIAN_TO_CHUC_DAU_GIA_TU_NGAY, req.getNgayToChucBDGTuNgay(), builder);
		thongBaoBDG.end(Operator.AND, ThongBaoBanDauGia_.THOI_GIAN_TO_CHUC_DAU_GIA_DEN_NGAY, req.getNgayToChucBDGDenNgay(), builder);
	}

	private int count(ThongBaoBanDauGiaSearchRequest req, QueryUtils thongBaoBDG) {
		log.debug("Build count query");
		StringBuilder builder = thongBaoBDG.countBy(BhTongHopDeXuatKhbdg_.ID);

		log.debug("Set condition search");
		this.setConditionSearch(req, builder, thongBaoBDG);

		log.debug("Create query");
		TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);

		log.debug("Set parameter");
		this.setParameterSearch(req, query, thongBaoBDG);

		return query.getSingleResult().intValue();
	}

	private void setParameterSearch(ThongBaoBanDauGiaSearchRequest req, Query query, QueryUtils thongBaoBDG) {
		thongBaoBDG.setParam(query, ThongBaoBanDauGia_.NAM_KE_HOACH, req.getNamKeHoach());
		thongBaoBDG.setParam(query, ThongBaoBanDauGia_.MA_VAT_TU_CHA, req.getMaVatTuCha());
		thongBaoBDG.setParam(query, BhQdPheDuyetKhbdg_.SO_QUYET_DINH, req.getSoQuyetDinhPheDuyetKHBDG());
		thongBaoBDG.setLikeParam(query, ThongBaoBanDauGia_.MA_THONG_BAO, req.getMaThongBaoBDG());
		thongBaoBDG.setLikeParam(query, ThongBaoBanDauGia_.TRICH_YEU, req.getTrichYeu());
		thongBaoBDG.setParam(query, ThongBaoBanDauGia_.LOAI_VTHH, req.getLoaiVthh());
		thongBaoBDG.setParam(query, ThongBaoBanDauGia_.TRANG_THAI, req.getTrangThai());
		thongBaoBDG.setParam(query, ThongBaoBanDauGia_.MA_DON_VI, req.getMaDvi());
		thongBaoBDG.setParamStart(query, ThongBaoBanDauGia_.THOI_GIAN_TO_CHUC_DAU_GIA_TU_NGAY, req.getNgayToChucBDGTuNgay());
		thongBaoBDG.setParamEnd(query, ThongBaoBanDauGia_.THOI_GIAN_TO_CHUC_DAU_GIA_DEN_NGAY, req.getNgayToChucBDGDenNgay());
	}
}
