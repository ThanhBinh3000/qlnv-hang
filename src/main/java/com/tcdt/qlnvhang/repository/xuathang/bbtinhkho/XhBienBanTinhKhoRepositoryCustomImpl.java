package com.tcdt.qlnvhang.repository.xuathang.bbtinhkho;

import com.tcdt.qlnvhang.entities.xuathang.bbtinhkho.XhBienBanTinhKho;
import com.tcdt.qlnvhang.request.search.xuathang.XhBienBanTinhKhoSearchReq;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class XhBienBanTinhKhoRepositoryCustomImpl implements XhBienBanTinhKhoRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<XhBienBanTinhKho> search(XhBienBanTinhKhoSearchReq req) {

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT x from XhBienBanTinhKho x ");
        //TODO: join phiếu xuất theo mã lô kho và mã vthh
        setConditionSearch(builder, req);
        TypedQuery<XhBienBanTinhKho> query = em.createQuery(builder.toString(), XhBienBanTinhKho.class);
        setParameterSearch(req, query);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        //Set pageable
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());
        return query.getResultList();
    }

    @Override
    public int count(XhBienBanTinhKhoSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(DISTINCT p.id) FROM XhBienBanTinhKho p ");
        this.setConditionSearch(builder, req);
        TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);
        this.setParameterSearch(req, query);
        return query.getSingleResult().intValue();
    }

    private void setConditionSearch(StringBuilder builder, XhBienBanTinhKhoSearchReq req) {
        builder.append("WHERE 1 = 1 ");

        if (!StringUtils.isEmpty(req.getQuyetDinhId())) {
            builder.append("AND ").append("x.qdgnvnxId LIKE :qdId ");
        }
        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            builder.append("AND ").append("x.soBienBan LIKE :soBienBan ");
        }

        //TODO: lọc theo ngày xuất cuối
    }

    private void setParameterSearch(XhBienBanTinhKhoSearchReq req, Query query) {
        if (!StringUtils.isEmpty(req.getQuyetDinhId())) {
            query.setParameter(":qdId", req.getQuyetDinhId());
        }
        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            query.setParameter(":soBienBan", req.getSoBienBan());
        }

        //TODO: gán tham số ngày xuất cuối
    }
}
