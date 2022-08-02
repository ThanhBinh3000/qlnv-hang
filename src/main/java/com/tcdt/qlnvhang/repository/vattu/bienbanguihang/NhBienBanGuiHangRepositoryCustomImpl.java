package com.tcdt.qlnvhang.repository.vattu.bienbanguihang;

import com.tcdt.qlnvhang.request.search.vattu.bienbanguihang.NhBienBanGuiHangSearchReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class NhBienBanGuiHangRepositoryCustomImpl implements NhBienBanGuiHangRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Object[]> search(NhBienBanGuiHangSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT p, nx.id, nx.soQd FROM NhBienBanGuiHang p ");
        builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON p.qdgnvnxId = nx.id ");
        setConditionSearch(req, builder);
        builder.append("ORDER BY p.id DESC");

        TypedQuery<Object[]> query = em.createQuery(builder.toString(), Object[].class);

        //Set params
        this.setParameterSearch(req, query);

        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        //Set pageable
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }


    private void setConditionSearch(NhBienBanGuiHangSearchReq req, StringBuilder builder) {
        builder.append("WHERE 1 = 1 ");

        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            builder.append("AND ").append("p.soBienBan LIKE :soBienBan ");
        }

        if (req.getNgayGuiHangTu() != null) {
            builder.append("AND ").append("p.ngayGui >= :ngayGuiHangTu ");
        }
        if (req.getNgayGuiHangDen() != null) {
            builder.append("AND ").append("p.ngayGui <= :ngayGuiHangDen ");
        }

        if (!StringUtils.isEmpty(req.getSoQdNhap())) {
            builder.append("AND ").append("nx.soQd LIKE :soQdNhap ");
        }

        if (!CollectionUtils.isEmpty(req.getMaDvis())) {
            builder.append("AND ").append("p.maDvi IN :maDvis ");
        }

        if (!CollectionUtils.isEmpty(req.getTrangThais())) {
            builder.append("AND ").append("p.trangThai IN :trangThais ");
        }
    }

    @Override
    public int count(NhBienBanGuiHangSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(DISTINCT p.id) FROM NhBienBanGuiHang p ");
        builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON p.qdgnvnxId = nx.id ");

        this.setConditionSearch(req, builder);
        TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);
        this.setParameterSearch(req, query);
        return query.getSingleResult().intValue();
    }

    private void setParameterSearch(NhBienBanGuiHangSearchReq req, Query query) {
        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            query.setParameter("soBienBan", "%" + req.getSoBienBan() + "%");
        }

        if (req.getNgayGuiHangTu() != null) {
            query.setParameter("ngayGuiHangTu", req.getNgayGuiHangTu());
        }

        if (req.getNgayGuiHangDen() != null) {
            query.setParameter("ngayGuiHangDen", req.getNgayGuiHangDen());
        }

        if (!StringUtils.isEmpty(req.getSoQdNhap())) {
            query.setParameter("soQdNhap", "%" + req.getSoQdNhap() + "%");
        }

        if (!CollectionUtils.isEmpty(req.getMaDvis())) {
            query.setParameter("maDvis", req.getMaDvis());
        }

        if (!CollectionUtils.isEmpty(req.getTrangThais())) {
            query.setParameter("trangThais", req.getTrangThais());
        }
    }
}
