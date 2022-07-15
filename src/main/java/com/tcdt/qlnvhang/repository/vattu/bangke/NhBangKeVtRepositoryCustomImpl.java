package com.tcdt.qlnvhang.repository.vattu.bangke;

import com.tcdt.qlnvhang.request.search.vattu.bangke.NhBangKeVtSearchReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class NhBangKeVtRepositoryCustomImpl implements NhBangKeVtRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Object[]> search(NhBangKeVtSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT p, nx.id, nx.soQd, nganLo FROM NhBangKeVt p ");
        builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON p.qdgnvnxId = nx.id ");
        builder.append("INNER JOIN NhPhieuNhapKho phieuNhapKho ON p.phieuNhapKhoId = phieuNhapKho.id ");
        builder.append("LEFT JOIN KtNganLo nganLo ON phieuNhapKho.maNganLo = nganLo.maNganlo ");
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


    private void setConditionSearch(NhBangKeVtSearchReq req, StringBuilder builder) {
        builder.append("WHERE 1 = 1 ");

        if (!StringUtils.isEmpty(req.getSoBangKe())) {
            builder.append("AND ").append("p.soBangKe LIKE :soBangKe ");
        }

        if (req.getNgayTaoBangKeTu() != null) {
            builder.append("AND ").append("p.ngayTao >= :ngayTaoBangKeTu ");
        }
        if (req.getNgayTaoBangKeDen() != null) {
            builder.append("AND ").append("p.ngayTao <= :ngayTaoBangKeDen ");
        }

        if (!StringUtils.isEmpty(req.getSoQdNhap())) {
            builder.append("AND ").append("nx.soQd LIKE :soQdNhap ");
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
    }

    @Override
    public int count(NhBangKeVtSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(DISTINCT p.id) FROM NhBangKeVt p ");
        builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON p.qdgnvnxId = nx.id ");
        builder.append("INNER JOIN NhPhieuNhapKho phieuNhapKho ON p.phieuNhapKhoId = phieuNhapKho.id ");
        builder.append("LEFT JOIN KtNganLo nganLo ON phieuNhapKho.maNganLo = nganLo.maNganlo ");

        this.setConditionSearch(req, builder);
        TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);
        this.setParameterSearch(req, query);
        return query.getSingleResult().intValue();
    }

    private void setParameterSearch(NhBangKeVtSearchReq req, Query query) {
        if (!StringUtils.isEmpty(req.getSoBangKe())) {
            query.setParameter("soBangKe", "%" + req.getSoBangKe() + "%");
        }

        if (req.getNgayTaoBangKeTu() != null) {
            query.setParameter("ngayTaoBangKeTu", req.getNgayTaoBangKeTu());
        }

        if (req.getNgayTaoBangKeDen() != null) {
            query.setParameter("ngayTaoBangKeDen", req.getNgayTaoBangKeDen());
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
    }
}
