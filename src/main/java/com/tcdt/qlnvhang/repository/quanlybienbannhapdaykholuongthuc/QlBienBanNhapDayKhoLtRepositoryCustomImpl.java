package com.tcdt.qlnvhang.repository.quanlybienbannhapdaykholuongthuc;

import com.tcdt.qlnvhang.request.search.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtSearchReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.*;
import java.util.List;

public class QlBienBanNhapDayKhoLtRepositoryCustomImpl implements QlBienBanNhapDayKhoLtRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Object[]> search(QlBienBanNhapDayKhoLtSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT p, nganLo, nx.id, nx.soQd, vatTu.ma, vatTu.ten FROM QlBienBanNhapDayKhoLt p ");
        builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON p.qdgnvnxId = nx.id ");
        builder.append("INNER JOIN QlnvDmVattu vatTu ON p.maVatTu = vatTu.ma ");
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


    private void setConditionSearch(QlBienBanNhapDayKhoLtSearchReq req, StringBuilder builder) {
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

        if (req.getNgayKetThucNhapTu() != null) {
            builder.append("AND ").append("p.ngayKetThucNhap >= :ngayKetThucNhapTu ");
        }
        if (req.getNgayKetThucNhapDen() != null) {
            builder.append("AND ").append("p.ngayKetThucNhap <= :ngayKetThucNhapDen ");
        }

        if (!StringUtils.isEmpty(req.getMaDvi())) {
            builder.append("AND ").append("p.maDvi = :maDvi ");
        }

        if (!StringUtils.isEmpty(req.getSoQdNhap())) {
            builder.append("AND ").append("nx.soQd LIKE :soQdNhap ");
        }

        if (!StringUtils.isEmpty(req.getMaVatTuCha())) {
            builder.append("AND ").append("p.maVatTuCha = :maVatTuCha ");
        }
    }

    @Override
    public int count(QlBienBanNhapDayKhoLtSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(DISTINCT p.id) FROM QlBienBanNhapDayKhoLt p ");
        builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON p.qdgnvnxId = nx.id ");
        builder.append("INNER JOIN QlnvDmVattu vatTu ON p.maVatTu = vatTu.ma ");
        builder.append("LEFT JOIN KtNganLo nganLo ON p.maNganLo = nganLo.maNganlo ");

        this.setConditionSearch(req, builder);
        TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);
        this.setParameterSearch(req, query);
        return query.getSingleResult().intValue();
    }

    private void setParameterSearch(QlBienBanNhapDayKhoLtSearchReq req, Query query) {
        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            query.setParameter("soBienBan", "%" + req.getSoBienBan() + "%");
        }

        if (req.getNgayNhapDayKhoTu() != null) {
            query.setParameter("ngayNhapDayKhoTu", req.getNgayNhapDayKhoTu());
        }
        if (req.getNgayNhapDayKhoDen() != null) {
            query.setParameter("ngayNhapDayKhoDen", req.getNgayNhapDayKhoDen());
        }
        if (req.getNgayKetThucNhapTu() != null) {
            query.setParameter("ngayKetThucNhapTu", req.getNgayKetThucNhapTu());
        }

        if (req.getNgayKetThucNhapDen() != null) {
            query.setParameter("ngayKetThucNhapDen", req.getNgayKetThucNhapDen());
        }

        if (!StringUtils.isEmpty(req.getMaDvi())) {
            query.setParameter("maDvi", req.getMaDvi());
        }

        if (!StringUtils.isEmpty(req.getSoQdNhap())) {
            query.setParameter("soQdNhap", "%" + req.getSoQdNhap() + "%");
        }

        if (!StringUtils.isEmpty(req.getMaVatTuCha())) {
            query.setParameter("maVatTuCha", req.getMaVatTuCha());
        }
    }
}
