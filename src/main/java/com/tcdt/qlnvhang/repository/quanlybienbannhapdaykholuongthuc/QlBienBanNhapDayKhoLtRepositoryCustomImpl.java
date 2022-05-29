package com.tcdt.qlnvhang.repository.quanlybienbannhapdaykholuongthuc;

import com.tcdt.qlnvhang.entities.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLt;
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
    public List<QlBienBanNhapDayKhoLt> search(QlBienBanNhapDayKhoLtSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT p FROM QlBienBanNhapDayKhoLt p ");
        setConditionSearch(req, builder);
        builder.append("ORDER BY p.id DESC");

        TypedQuery<QlBienBanNhapDayKhoLt> query = em.createQuery(builder.toString(), QlBienBanNhapDayKhoLt.class);

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
            builder.append("AND ").append("p.soBienBan = :soBienBan ");
        }
        if (req.getNgayNhapDayKhoTu() != null) {
            builder.append("AND ").append("p.ngayNhapDayKho >= :ngayNhapDayKhoTu ");
        }
        if (req.getNgayNhapDayKhoDen() != null) {
            builder.append("AND ").append("p.ngayNhapDayKho <= :ngayNhapDayKhoDen ");
        }

        if (!StringUtils.isEmpty(req.getMaHang())) {
            builder.append("AND ").append("p.maHang = :maHang ");
        }

        if (!StringUtils.isEmpty(req.getMaDonViLap())) {
            builder.append("AND ").append("p.maDonViLap = :maDonViLap ");
        }

        if (!StringUtils.isEmpty(req.getMaDonVi())) {
            builder.append("AND ").append("p.maDonVi = :maDonVi ");
        }
    }

    @Override
    public int count(QlBienBanNhapDayKhoLtSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(p.id) FROM QlBienBanNhapDayKhoLt p ");

        this.setConditionSearch(req, builder);
        TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);
        this.setParameterSearch(req, query);
        return query.getSingleResult().intValue();
    }

    private void setParameterSearch(QlBienBanNhapDayKhoLtSearchReq req, Query query) {
        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            query.setParameter("soBienBan", req.getSoBienBan());
        }

        if (req.getNgayNhapDayKhoTu() != null) {
            query.setParameter("ngayNhapDayKhoTu", req.getNgayNhapDayKhoTu());
        }
        if (req.getNgayNhapDayKhoDen() != null) {
            query.setParameter("ngayNhapDayKhoDen", req.getNgayNhapDayKhoDen());
        }

        if (!StringUtils.isEmpty(req.getMaHang())) {
            query.setParameter("maHang", req.getMaHang());
        }

        if (!StringUtils.isEmpty(req.getMaDonViLap())) {
            query.setParameter("maDonViLap", req.getMaDonViLap());
        }

        if (!StringUtils.isEmpty(req.getMaDonVi())) {
            query.setParameter("maDonVi", req.getMaDonVi());
        }
    }
}
