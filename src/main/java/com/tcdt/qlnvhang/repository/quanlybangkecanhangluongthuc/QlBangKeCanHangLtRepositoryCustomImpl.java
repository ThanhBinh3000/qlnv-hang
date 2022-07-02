package com.tcdt.qlnvhang.repository.quanlybangkecanhangluongthuc;

import com.tcdt.qlnvhang.request.search.quanlybangkecanhangluongthuc.QlBangKeCanHangLtSearchReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.List;

public class QlBangKeCanHangLtRepositoryCustomImpl implements QlBangKeCanHangLtRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Object[]> search(QlBangKeCanHangLtSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT p, pnk.id, pnk.soPhieu, nganLo, nx.id, nx.soQd, vatTu.ma, vatTu.ten FROM QlBangKeCanHangLt p ");
        builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON p.qdgnvnxId = nx.id ");
        builder.append("INNER JOIN QlPhieuNhapKhoLt pnk ON p.qlPhieuNhapKhoLtId = pnk.id ");
        builder.append("INNER JOIN QlnvDmVattu vatTu ON p.maVatTu = vatTu.ma ");
        builder.append("LEFT JOIN KtNganLo nganLo ON p.maNganLo = nganLo.maNganlo ");
        setConditionSearch(req, builder);
        builder.append("ORDER BY p.ngayNhap DESC");

        TypedQuery<Object[]> query = em.createQuery(builder.toString(), Object[].class);

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
            builder.append("AND ").append("p.soBangKe LIKE :soBangKe ");
        }
        if (req.getTuNgayNhap() != null) {
            builder.append("AND ").append("p.ngayNhap >= :tuNgayNhap ");
        }
        if (req.getDenNgayNhap() != null) {
            builder.append("AND ").append("p.ngayNhap <= :denNgayNhap ");
        }

        if (!StringUtils.isEmpty(req.getSoQdNhap())) {
            builder.append("AND ").append("nx.soQd LIKE :soQdNhap ");
        }

        if (!CollectionUtils.isEmpty(req.getMaDvis())) {
            builder.append("AND ").append("p.maDvi IN :maDvis ");
        }

        if (!CollectionUtils.isEmpty(req.getTrangThais())) {
            builder.append("AND ").append("p.trangThai IN :trangThais ");
        }
    }

    @Override
    public int count(QlBangKeCanHangLtSearchReq req) {
        int total = 0;
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(p.id) FROM QlBangKeCanHangLt p ");
        builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON p.qdgnvnxId = nx.id ");
        builder.append("INNER JOIN QlPhieuNhapKhoLt pnk ON p.qlPhieuNhapKhoLtId = pnk.id ");
        builder.append("INNER JOIN QlnvDmVattu vatTu ON p.maVatTu = vatTu.ma ");
        builder.append("LEFT JOIN KtNganLo nganLo ON p.maNganLo = nganLo.maNganlo ");
        this.setConditionSearch(req, builder);

        TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);
        this.setParameterSearch(req, query);
        return query.getSingleResult().intValue();
    }

    private void setParameterSearch(QlBangKeCanHangLtSearchReq req, Query query) {
        if (!StringUtils.isEmpty(req.getSoBangKe())) {
            query.setParameter("soBangKe", "%" + req.getSoBangKe() + "%");
        }
        if (req.getTuNgayNhap() != null) {
            query.setParameter("tuNgayNhap", req.getTuNgayNhap());
        }
        if (req.getDenNgayNhap() != null) {
            query.setParameter("denNgayNhap", req.getDenNgayNhap());
        }

        if (!StringUtils.isEmpty(req.getSoQdNhap())) {
            query.setParameter("soQdNhap", "%" + req.getSoQdNhap() + "%");
        }

        if (!CollectionUtils.isEmpty(req.getMaDvis())) {
            query.setParameter("maDvis", req.getMaDvis());
        }

        if (!CollectionUtils.isEmpty(req.getTrangThais())) {
            query.setParameter("trangThais", req.getTrangThais());
        }
    }
}
