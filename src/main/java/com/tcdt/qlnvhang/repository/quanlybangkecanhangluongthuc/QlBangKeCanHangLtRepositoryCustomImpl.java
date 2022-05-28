package com.tcdt.qlnvhang.repository.quanlybangkecanhangluongthuc;

import com.tcdt.qlnvhang.entities.quanlybangkecanhangluongthuc.QlBangKeCanHangLt;
import com.tcdt.qlnvhang.request.search.quanlybangkecanhangluongthuc.QlBangKeCanHangLtSearchReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class QlBangKeCanHangLtRepositoryCustomImpl implements QlBangKeCanHangLtRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<QlBangKeCanHangLt> search(QlBangKeCanHangLtSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT p FROM QlBangKeCanHangLt p ");
        setConditionSearch(req, builder);
        builder.append("ORDER BY p.ngayNhapXuat DESC");

        TypedQuery<QlBangKeCanHangLt> query = em.createQuery(builder.toString(), QlBangKeCanHangLt.class);

        //Set params
        this.setParameterSearch(req, query);

        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        //Set pageable
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }


    private void setConditionSearch(QlBangKeCanHangLtSearchReq req, StringBuilder builder) {
        builder.append("WHERE 1 = 1 ");

        if (!StringUtils.isEmpty(req.getSoBangKe())) {
            builder.append("AND ").append("p.soBangKe = :soBangKe ");
        }
        if (req.getTuNgay() != null) {
            builder.append("AND ").append("p.ngayNhapXuat >= :tuNgay ");
        }
        if (req.getDenNgay() != null) {
            builder.append("AND ").append("p.ngayNhapXuat <= :denNgay ");
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
    public int count(QlBangKeCanHangLtSearchReq req) {
        int total = 0;
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(p.id) FROM QlBangKeCanHangLt p ");

        this.setConditionSearch(req, builder);

        TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);
        this.setParameterSearch(req, query);
        return query.getSingleResult().intValue();
    }

    private void setParameterSearch(QlBangKeCanHangLtSearchReq req, Query query) {
        if (!StringUtils.isEmpty(req.getSoBangKe())) {
            query.setParameter("soBangKe", req.getSoBangKe());
        }
        if (req.getTuNgay() != null) {
            query.setParameter("tuNgay", req.getTuNgay());
        }
        if (req.getDenNgay() != null) {
            query.setParameter("denNgay", req.getDenNgay());
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
