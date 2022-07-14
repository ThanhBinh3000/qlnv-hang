package com.tcdt.qlnvhang.repository.vattu.bienbanketthucnhapkho;

import com.tcdt.qlnvhang.request.search.vattu.bangke.NhBangKeVtSearchReq;
import com.tcdt.qlnvhang.request.search.vattu.bienbanketthucnhapkho.NhBbKtNhapKhoVtSearchReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class NhBbKtNhapKhoVtRepositoryCustomImpl implements NhBbKtNhapKhoVtRepositoryCustom{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Object[]> search(NhBbKtNhapKhoVtSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT p, nx.id, nx.soQd, nganLo FROM NhBbKtNhapKhoVt p ");
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


    private void setConditionSearch(NhBbKtNhapKhoVtSearchReq req, StringBuilder builder) {
        builder.append("WHERE 1 = 1 ");

        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            builder.append("AND ").append("p.soBienBan LIKE :soBienBan ");
        }

        if (req.getNgayNhapDayKhoTu() != null) {
            builder.append("AND ").append("p.ngayNhapDayKho >= :ngayNhapDayKhoTu ");
        }
        if (req.getNgayNhapDayKhoDen() != null) {
            builder.append("AND ").append("p.ngayNhapDayKho <= :ngayNhapDayKhoDen ");
        }

        if (req.getNgayKetThucTu() != null) {
            builder.append("AND ").append("p.ngayKetThucNhap >= :ngayKetThucTu ");
        }
        if (req.getNgayKetThucDen() != null) {
            builder.append("AND ").append("p.ngayKetThucNhap <= :ngayKetThucDen ");
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
    public int count(NhBbKtNhapKhoVtSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(DISTINCT p.id) FROM NhBbKtNhapKhoVt p ");
        builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON p.qdgnvnxId = nx.id ");
        builder.append("LEFT JOIN KtNganLo nganLo ON p.maNganLo = nganLo.maNganlo ");

        this.setConditionSearch(req, builder);
        TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);
        this.setParameterSearch(req, query);
        return query.getSingleResult().intValue();
    }

    private void setParameterSearch(NhBbKtNhapKhoVtSearchReq req, Query query) {
        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            query.setParameter("soBienBan", "%" + req.getSoBienBan() + "%");
        }

        if (req.getNgayNhapDayKhoTu() != null) {
            query.setParameter("ngayNhapDayKhoTu", req.getNgayNhapDayKhoTu());
        }

        if (req.getNgayNhapDayKhoDen() != null) {
            query.setParameter("ngayNhapDayKhoDen", req.getNgayNhapDayKhoDen());
        }

        if (req.getNgayNhapDayKhoTu() != null) {
            query.setParameter("ngayKetThucTu", req.getNgayKetThucTu());
        }

        if (req.getNgayKetThucDen() != null) {
            query.setParameter("ngayKetThucDen", req.getNgayKetThucDen());
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
