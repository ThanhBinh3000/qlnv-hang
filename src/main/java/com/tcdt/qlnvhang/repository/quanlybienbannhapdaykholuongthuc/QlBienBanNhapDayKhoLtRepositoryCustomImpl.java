package com.tcdt.qlnvhang.repository.quanlybienbannhapdaykholuongthuc;

import com.tcdt.qlnvhang.entities.quanlybangkecanhangluongthuc.QlBangKeCanHangLt;
import com.tcdt.qlnvhang.entities.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLt;
import com.tcdt.qlnvhang.enums.QlPhieuNhapKhoLtStatus;
import com.tcdt.qlnvhang.request.search.quanlybangkecanhangluongthuc.QlBangKeCanHangLtSearchReq;
import com.tcdt.qlnvhang.request.search.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtSearchReq;
import com.tcdt.qlnvhang.response.quanlybangkecanhangluongthuc.QlBangKeCanHangLtRes;
import com.tcdt.qlnvhang.response.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtRes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class QlBienBanNhapDayKhoLtRepositoryCustomImpl implements QlBienBanNhapDayKhoLtRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<QlBienBanNhapDayKhoLtRes> search(QlBienBanNhapDayKhoLtSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT p FROM QlBienBanNhapDayKhoLt p ");
        setConditionSearchCtkhn(req, builder);
        builder.append("ORDER BY p.ngayLap DESC");

        TypedQuery<QlBienBanNhapDayKhoLt> query = em.createQuery(builder.toString(), QlBienBanNhapDayKhoLt.class);

        //Set params
        this.setParameterSearchCtkhn(req, query);

        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        //Set pageable
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

        List<QlBienBanNhapDayKhoLt> data = query.getResultList();

        List<QlBienBanNhapDayKhoLtRes> responses = new ArrayList<>();
        for (QlBienBanNhapDayKhoLt qd : data) {
            QlBienBanNhapDayKhoLtRes response = new QlBienBanNhapDayKhoLtRes();
            BeanUtils.copyProperties(qd, response);
            response.setTenTrangThai(QlPhieuNhapKhoLtStatus.getTenById(qd.getTrangThai()));
            responses.add(response);
        }

        return new PageImpl<>(responses, pageable, this.countCtkhn(req));
    }


    private void setConditionSearchCtkhn(QlBienBanNhapDayKhoLtSearchReq req, StringBuilder builder) {
        builder.append("WHERE 1 = 1 ");

        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            builder.append("AND ").append("p.soBienBan = :soBienBan ");
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

    private int countCtkhn(QlBienBanNhapDayKhoLtSearchReq req) {
        int total = 0;
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(p.id) AS totalRecord FROM QlBienBanNhapDayKhoLt p ");

        this.setConditionSearchCtkhn(req, builder);

        Query query = em.createNativeQuery(builder.toString(), Tuple.class);

        this.setParameterSearchCtkhn(req, query);

        List<?> dataCount = query.getResultList();

        if (CollectionUtils.isEmpty(dataCount)) {
            return total;
        }
        Tuple result = (Tuple) dataCount.get(0);
        return result.get("totalRecord", BigDecimal.class).intValue();
    }

    private void setParameterSearchCtkhn(QlBienBanNhapDayKhoLtSearchReq req, Query query) {
        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            query.setParameter("soBienBan", req.getSoBienBan());
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
