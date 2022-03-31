package com.tcdt.qlnvhang.repository.quanlybangkecanhangluongthuc;

import com.tcdt.qlnvhang.entities.quanlybangkecanhangluongthuc.QlBangKeCanHangLt;
import com.tcdt.qlnvhang.enums.QlBangKeCanHangLtStatus;
import com.tcdt.qlnvhang.enums.QlPhieuNhapKhoLtStatus;
import com.tcdt.qlnvhang.request.search.quanlybangkecanhangluongthuc.QlBangKeCanHangLtSearchReq;
import com.tcdt.qlnvhang.response.quanlybangkecanhangluongthuc.QlBangKeCanHangLtRes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class QlBangKeCanHangLtRepositoryCustomImpl implements QlBangKeCanHangLtRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<QlBangKeCanHangLtRes> search(QlBangKeCanHangLtSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT p FROM QlBangKeCanHangLt p ");
        setConditionSearchCtkhn(req, builder);
        builder.append("ORDER BY p.ngayLap DESC");

        TypedQuery<QlBangKeCanHangLt> query = em.createQuery(builder.toString(), QlBangKeCanHangLt.class);

        //Set params
        this.setParameterSearchCtkhn(req, query);

        Pageable pageable = req.getPageable();
        //Set pageable
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

        List<QlBangKeCanHangLt> data = query.getResultList();

        List<QlBangKeCanHangLtRes> responses = new ArrayList<>();
        for (QlBangKeCanHangLt qd : data) {
            QlBangKeCanHangLtRes response = new QlBangKeCanHangLtRes();
            BeanUtils.copyProperties(qd, response);
            response.setTenTrangThai(QlBangKeCanHangLtStatus.getTenById(qd.getTrangThai()));
            responses.add(response);
        }

        return new PageImpl<>(responses, pageable, this.countCtkhn(req));
    }


    private void setConditionSearchCtkhn(QlBangKeCanHangLtSearchReq req, StringBuilder builder) {
        builder.append("WHERE 1 = 1 ");

        if (!StringUtils.isEmpty(req.getSoBangKe())) {
            builder.append("AND ").append("p.soBangKe = :soBangKe ");
        }
        if (req.getTuNgay() != null) {
            builder.append("AND ").append("p.ngayLap >= :tuNgay ");
        }
        if (req.getDenNgay() != null) {
            builder.append("AND ").append("p.ngayLap <= :denNgay ");
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

    private int countCtkhn(QlBangKeCanHangLtSearchReq req) {
        int total = 0;
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(p.id) AS totalRecord FROM QlBangKeCanHangLt p ");

        this.setConditionSearchCtkhn(req, builder);

        Query query = em.createNativeQuery(builder.toString(), Tuple.class);

        this.setParameterSearchCtkhn(req, query);

        List<?> dataCount = query.getResultList();

        if (!CollectionUtils.isEmpty(dataCount)) {
            return total;
        }
        Tuple result = (Tuple) dataCount.get(0);
        return result.get("totalRecord", BigInteger.class).intValue();
    }

    private void setParameterSearchCtkhn(QlBangKeCanHangLtSearchReq req, Query query) {
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
