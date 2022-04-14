package com.tcdt.qlnvhang.repository.quyetdinhpheduyetketqualuachonnhathauvatu;

import com.tcdt.qlnvhang.entities.quyetdinhpheduyetketqualuachonnhathauvatu.QdPheDuyetKqlcntVt;
import com.tcdt.qlnvhang.enums.QdPheDuyetKqlcntVtStatus;
import com.tcdt.qlnvhang.request.search.quyetdinhpheduyetketqualuachonnhathauvatu.QdPheDuyetKqlcntVtSearchReq;
import com.tcdt.qlnvhang.response.quyetdinhpheduyetketqualuachonnhathauvatu.QdPheDuyetKqlcntVtRes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class QdPheDuyetKqlcntVtRepositoryCustomImpl implements QdPheDuyetKqlcntVtRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<QdPheDuyetKqlcntVtRes> search(QdPheDuyetKqlcntVtSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT qd FROM QdPheDuyetKqlcntVt qd ");
        setConditionSearch(req, builder);
        builder.append("ORDER BY qd.ngayQuyetDinh DESC");

        TypedQuery<QdPheDuyetKqlcntVt> query = em.createQuery(builder.toString(), QdPheDuyetKqlcntVt.class);

        //Set params
        this.setParameterSearch(req, query);

        Pageable pageable = req.getPageable();
        //Set pageable
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

        List<QdPheDuyetKqlcntVt> data = query.getResultList();

        List<QdPheDuyetKqlcntVtRes> responses = new ArrayList<>();
        for (QdPheDuyetKqlcntVt qd : data) {
            QdPheDuyetKqlcntVtRes response = new QdPheDuyetKqlcntVtRes();
            BeanUtils.copyProperties(qd, response);
            response.setTenTrangThai(QdPheDuyetKqlcntVtStatus.getTenById(qd.getTrangThai()));
            responses.add(response);
        }

        return new PageImpl<>(responses, pageable, this.count(req));
    }


    private void setConditionSearch(QdPheDuyetKqlcntVtSearchReq req, StringBuilder builder) {
        builder.append("WHERE 1 = 1 ");

        if (!StringUtils.isEmpty(req.getSoQd())) {
            builder.append("AND ").append("qd.soQuyetDinh = :soQD ");
        }
        if (req.getTuNgay() != null) {
            builder.append("AND ").append("qd.ngayQuyetDinh >= :tuNgay ");
        }
        if (req.getDenNgay() != null) {
            builder.append("AND ").append("qd.ngayQuyetDinh <= :denNgay ");
        }
        if (req.getVatTuId() != null) {
            builder.append("AND ").append("qd.vatTuId = :vatTuId ");
        }
        if (req.getNamKeHoach() != null) {
            builder.append("AND ").append("qd.namKeHoach = :namKeHoach ");
        }
    }

    private int count(QdPheDuyetKqlcntVtSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(qd.id) FROM QdPheDuyetKqlcntVt qd");

        this.setConditionSearch(req, builder);

        TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);
        this.setParameterSearch(req, query);
        return query.getSingleResult().intValue();
    }

    private void setParameterSearch(QdPheDuyetKqlcntVtSearchReq req, Query query) {
        if (!StringUtils.isEmpty(req.getSoQd())) {
            query.setParameter("soQd", req.getSoQd());
        }
        if (req.getTuNgay() != null) {
            query.setParameter("tuNgay", req.getTuNgay());
        }
        if (req.getDenNgay() != null) {
            query.setParameter("denNgay", req.getDenNgay());
        }

        if (req.getVatTuId() != null) {
            query.setParameter("vatTuId", req.getVatTuId());
        }

        if (req.getNamKeHoach() != null) {
            query.setParameter("namKeHoach", req.getNamKeHoach());
        }
    }
}
