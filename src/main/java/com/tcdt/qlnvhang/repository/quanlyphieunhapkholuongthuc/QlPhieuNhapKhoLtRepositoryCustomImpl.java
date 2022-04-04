package com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLt;
import com.tcdt.qlnvhang.entities.quyetdinhpheduyetketqualuachonnhathauvatu.QdPheDuyetKqlcntVt;
import com.tcdt.qlnvhang.enums.QdPheDuyetKqlcntVtStatus;
import com.tcdt.qlnvhang.enums.QlPhieuNhapKhoLtStatus;
import com.tcdt.qlnvhang.request.search.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtSearchReq;
import com.tcdt.qlnvhang.request.search.quyetdinhpheduyetketqualuachonnhathauvatu.QdPheDuyetKqlcntVtSearchReq;
import com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtRes;
import com.tcdt.qlnvhang.response.quyetdinhpheduyetketqualuachonnhathauvatu.QdPheDuyetKqlcntVtRes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class QlPhieuNhapKhoLtRepositoryCustomImpl implements QlPhieuNhapKhoLtRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<QlPhieuNhapKhoLtRes> search(QlPhieuNhapKhoLtSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT p FROM QlPhieuNhapKhoLt p ");
        setConditionSearchCtkhn(req, builder);
        builder.append("ORDER BY p.ngayLap DESC");

        TypedQuery<QlPhieuNhapKhoLt> query = em.createQuery(builder.toString(), QlPhieuNhapKhoLt.class);

        //Set params
        this.setParameterSearchCtkhn(req, query);

        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        //Set pageable
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

        List<QlPhieuNhapKhoLt> data = query.getResultList();

        List<QlPhieuNhapKhoLtRes> responses = new ArrayList<>();
        for (QlPhieuNhapKhoLt qd : data) {
            QlPhieuNhapKhoLtRes response = new QlPhieuNhapKhoLtRes();
            BeanUtils.copyProperties(qd, response);
            response.setTenTrangThai(QlPhieuNhapKhoLtStatus.getTenById(qd.getTrangThai()));
            responses.add(response);
        }

        return new PageImpl<>(responses, pageable, this.countCtkhn(req));
    }


    private void setConditionSearchCtkhn(QlPhieuNhapKhoLtSearchReq req, StringBuilder builder) {
        builder.append("WHERE 1 = 1 ");

        if (!StringUtils.isEmpty(req.getSoPhieu())) {
            builder.append("AND ").append("p.soPhieu = :soPhieu ");
        }
        if (req.getTuNgay() != null) {
            builder.append("AND ").append("p.ngayLap >= :tuNgay ");
        }
        if (req.getDenNgay() != null) {
            builder.append("AND ").append("p.ngayLap <= :denNgay ");
        }
        if (req.getVatTuId() != null) {
            builder.append("AND ").append("p.vatTuId = :vatTuId ");
        }
        if (!StringUtils.isEmpty(req.getMaDonVi())) {
            builder.append("AND ").append("p.maDonVi = :maDonVi ");
        }

        if (!StringUtils.isEmpty(req.getMaKhoNgan())) {
            builder.append("AND ").append("p.maNganLo = :maKhoNgan ");
        }
    }

    private int countCtkhn(QlPhieuNhapKhoLtSearchReq req) {
        int total = 0;
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(p.id) AS totalRecord FROM QlPhieuNhapKhoLt p ");

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

    private void setParameterSearchCtkhn(QlPhieuNhapKhoLtSearchReq req, Query query) {
        if (!StringUtils.isEmpty(req.getSoPhieu())) {
            query.setParameter("soPhieu", req.getSoPhieu());
        }
        if (req.getTuNgay() != null) {
            query.setParameter("tuNgay", req.getTuNgay());
        }
        if (req.getDenNgay() != null) {
            query.setParameter("denNgay", req.getDenNgay());
        }

        if (req.getVatTuId() != null) {
            query.setParameter("vatTuId", req.getVatTuId());
        }

        if (!StringUtils.isEmpty(req.getMaDonVi())) {
            query.setParameter("maDonVi", req.getMaDonVi());
        }

        if (!StringUtils.isEmpty(req.getMaKhoNgan())) {
            query.setParameter("maKhoNgan", req.getMaKhoNgan());
        }
    }
}
