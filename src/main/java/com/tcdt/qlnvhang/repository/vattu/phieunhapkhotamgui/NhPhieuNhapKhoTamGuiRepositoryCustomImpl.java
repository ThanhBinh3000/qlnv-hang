package com.tcdt.qlnvhang.repository.vattu.phieunhapkhotamgui;

import com.tcdt.qlnvhang.request.search.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiSearchReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class NhPhieuNhapKhoTamGuiRepositoryCustomImpl implements NhPhieuNhapKhoTamGuiRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Object[]> search(NhPhieuNhapKhoTamGuiSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT p, nx.id, nx.soQd, nganLo FROM NhPhieuNhapKhoTamGui p ");
        builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON p.qdgnvnxId = nx.id ");
        builder.append("LEFT JOIN KtNganLo nganLo ON p.maNganLo = nganLo.maNganlo ");
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


    private void setConditionSearch(NhPhieuNhapKhoTamGuiSearchReq req, StringBuilder builder) {
        builder.append("WHERE 1 = 1 ");

        if (!StringUtils.isEmpty(req.getSoPhieu())) {
            builder.append("AND ").append("p.soPhieu LIKE :soPhieu ");
        }

        if (req.getNgayNhapKhoTu() != null) {
            builder.append("AND ").append("p.ngayNhapKho >= :ngayNhapKhoTu ");
        }
        if (req.getNgayNhapKhoDen() != null) {
            builder.append("AND ").append("p.ngayNhapKho <= :ngayNhapKhoDen ");
        }

        if (!StringUtils.isEmpty(req.getMaDvi())) {
            builder.append("AND ").append("p.maDvi = :maDvi ");
        }

        if (!StringUtils.isEmpty(req.getSoQdNhap())) {
            builder.append("AND ").append("nx.soQd LIKE :soQdNhap ");
        }

        if (!StringUtils.isEmpty(req.getLoaiVthh())) {
            builder.append("AND ").append("p.loaiVthh = :loaiVthh ");
        }
    }

    @Override
    public int count(NhPhieuNhapKhoTamGuiSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(DISTINCT p.id) FROM NhPhieuNhapKhoTamGui p ");
        builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON p.qdgnvnxId = nx.id ");
        builder.append("LEFT JOIN KtNganLo nganLo ON p.maNganLo = nganLo.maNganlo ");

        this.setConditionSearch(req, builder);
        TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);
        this.setParameterSearch(req, query);
        return query.getSingleResult().intValue();
    }

    private void setParameterSearch(NhPhieuNhapKhoTamGuiSearchReq req, Query query) {
        if (!StringUtils.isEmpty(req.getSoPhieu())) {
            query.setParameter("soPhieu", "%" + req.getSoPhieu() + "%");
        }

        if (req.getNgayNhapKhoTu() != null) {
            query.setParameter("ngayNhapKhoTu", req.getNgayNhapKhoTu());
        }

        if (req.getNgayNhapKhoDen() != null) {
            query.setParameter("ngayNhapKhoDen", req.getNgayNhapKhoDen());
        }

        if (!StringUtils.isEmpty(req.getMaDvi())) {
            query.setParameter("maDvi", req.getMaDvi());
        }

        if (!StringUtils.isEmpty(req.getSoQdNhap())) {
            query.setParameter("soQdNhap", "%" + req.getSoQdNhap() + "%");
        }

        if (!StringUtils.isEmpty(req.getLoaiVthh())) {
            query.setParameter("loaiVthh", req.getLoaiVthh());
        }
    }
}
