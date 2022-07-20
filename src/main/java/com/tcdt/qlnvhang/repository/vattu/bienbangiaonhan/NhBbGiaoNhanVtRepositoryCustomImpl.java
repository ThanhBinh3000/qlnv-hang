package com.tcdt.qlnvhang.repository.vattu.bienbangiaonhan;

import com.tcdt.qlnvhang.request.search.vattu.bienbangiaonhan.NhBbGiaoNhanVtSearchReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class NhBbGiaoNhanVtRepositoryCustomImpl implements NhBbGiaoNhanVtRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Object[]> search(NhBbGiaoNhanVtSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT p, nx.id, nx.soQd, hopDong.id, hopDong.soHd FROM NhBbGiaoNhanVt p ");
        builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON p.qdgnvnxId = nx.id ");
        builder.append("INNER JOIN HhHopDongHdr hopDong ON p.hopDongId = hopDong.id ");
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


    private void setConditionSearch(NhBbGiaoNhanVtSearchReq req, StringBuilder builder) {
        builder.append("WHERE 1 = 1 ");

        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            builder.append("AND ").append("p.soBienBan LIKE :soBienBan ");
        }

        if (req.getNgayHopDongTu() != null) {
            builder.append("AND ").append("p.ngayHopDong >= :ngayHopDongTu ");
        }
        if (req.getNgayHopDongDen() != null) {
            builder.append("AND ").append("p.ngayHopDong <= :ngayHopDongDen ");
        }

        if (!StringUtils.isEmpty(req.getSoQdNhap())) {
            builder.append("AND ").append("nx.soQd LIKE :soQdNhap ");
        }

        if (!StringUtils.isEmpty(req.getSoHopDong())) {
            builder.append("AND ").append("hopDong.soHd LIKE :soHopDong ");
        }

        if (!StringUtils.isEmpty(req.getLoaiVthh())) {
            builder.append("AND ").append("p.loaiVthh = :loaiVthh ");
        }

        if (!CollectionUtils.isEmpty(req.getMaDvis())) {
            builder.append("AND ").append("p.maDvi IN :maDvis ");
        }

        if (!CollectionUtils.isEmpty(req.getTrangThais())) {
            builder.append("AND ").append("p.trangThai IN :trangThais ");
        }

        if (req.getNam() != null) {
            builder.append("AND ").append("p.nam = :nam ");
        }
    }

    @Override
    public int count(NhBbGiaoNhanVtSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(DISTINCT p.id) FROM NhBbGiaoNhanVt p ");
        builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON p.qdgnvnxId = nx.id ");
        builder.append("INNER JOIN HhHopDongHdr hopDong ON p.hopDongId = hopDong.id ");

        this.setConditionSearch(req, builder);
        TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);
        this.setParameterSearch(req, query);
        return query.getSingleResult().intValue();
    }

    private void setParameterSearch(NhBbGiaoNhanVtSearchReq req, Query query) {
        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            query.setParameter("soBienBan", "%" + req.getSoBienBan() + "%");
        }

        if (!StringUtils.isEmpty(req.getSoHopDong())) {
            query.setParameter("soHopDong", "%" + req.getSoHopDong() + "%");
        }

        if (req.getNgayHopDongTu() != null) {
            query.setParameter("ngayHopDongTu", req.getNgayHopDongTu());
        }

        if (req.getNgayHopDongDen() != null) {
            query.setParameter("ngayHopDongDen", req.getNgayHopDongDen());
        }

        if (!StringUtils.isEmpty(req.getSoQdNhap())) {
            query.setParameter("soQdNhap", "%" + req.getSoQdNhap() + "%");
        }

        if (!StringUtils.isEmpty(req.getLoaiVthh())) {
            query.setParameter("loaiVthh", req.getLoaiVthh());
        }

        if (!CollectionUtils.isEmpty(req.getMaDvis())) {
            query.setParameter("maDvis", req.getMaDvis());
        }

        if (!CollectionUtils.isEmpty(req.getTrangThais())) {
            query.setParameter("trangThais", req.getTrangThais());
        }

        if (req.getNam() != null) {
            query.setParameter("nam", req.getNam());
        }
    }
}
