package com.tcdt.qlnvhang.repository.xuathang.bbhaodoi;

import com.tcdt.qlnvhang.entities.xuathang.bbhaodoi.XhBienBanHaoDoi;
import com.tcdt.qlnvhang.repository.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatRepository;
import com.tcdt.qlnvhang.request.search.xuathang.XhBienBanHaoDoiSearchReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhBienBanTinhKhoSearchReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class XhBienBanHaoDoiRepositoryCustomImpl implements XhBienBanHaoDoiRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    XhQdGiaoNvuXuatRepository xhQdGiaoNvuXuatRepository;

    @Override
    public List<XhBienBanHaoDoi> search(XhBienBanHaoDoiSearchReq req) {

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT x from XhBienBanHaoDoi x ");
        builder.append("inner join XhBienBanTinhKho t on t.id = x.bbTinhkhoId ");
        setConditionSearch(builder, req);
        TypedQuery<XhBienBanHaoDoi> query = em.createQuery(builder.toString(), XhBienBanHaoDoi.class);
        setParameterSearch(req, query);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        //Set pageable
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());
        return query.getResultList();
    }

    @Override
    public int count(XhBienBanHaoDoiSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(x.id) from XhBienBanHaoDoi x ");
        builder.append("inner join XhBienBanTinhKho t on t.id = x.bbTinhkhoId ");
        setConditionSearch(builder, req);
        TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);
        setParameterSearch(req, query);
        return query.getSingleResult().intValue();
    }

    private void setConditionSearch(StringBuilder builder, XhBienBanHaoDoiSearchReq req) {
        builder.append("WHERE 1 = 1 ");

        if (!StringUtils.isEmpty(req.getSoQuyetDinh())) {
            builder.append("AND ").append("t.qdgnvnxId LIKE :qdId ");
        }
        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            builder.append("AND ").append("x.soBienBan LIKE :soBienBan ");
        }
        if (!StringUtils.isEmpty(req.getNgayBienBanTu())) {
            builder.append("AND ").append("x.ngayTao >= :tuNgay ");
        }
        if (!StringUtils.isEmpty(req.getNgayBienBanDen())) {
            builder.append("AND ").append("x.ngayTao <= :denNgay ");
        }
    }

    private void setParameterSearch(XhBienBanHaoDoiSearchReq req, Query query) {
        if (!StringUtils.isEmpty(req.getSoQuyetDinh())) {
            query.setParameter("qdId", xhQdGiaoNvuXuatRepository.findFirstBySoQuyetDinh(req.getSoQuyetDinh()));
        }
        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            query.setParameter("soBienBan", req.getSoBienBan());
        }
        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            query.setParameter("tuNgay", req.getNgayBienBanTu());
        }
        if (!StringUtils.isEmpty(req.getNgayBienBanDen())) {
            query.setParameter("denNgay", req.getNgayBienBanDen());
        }
    }

}
