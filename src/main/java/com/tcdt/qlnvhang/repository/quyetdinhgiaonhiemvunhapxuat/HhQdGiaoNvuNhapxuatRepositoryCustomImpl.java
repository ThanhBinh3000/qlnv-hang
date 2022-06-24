package com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat;

import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.util.Contains;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class HhQdGiaoNvuNhapxuatRepositoryCustomImpl implements HhQdGiaoNvuNhapxuatRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<HhQdGiaoNvuNhapxuatHdr> search(HhQdNhapxuatSearchReq req, String capDvi) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT qd FROM HhQdGiaoNvuNhapxuatHdr qd ");
        if (Contains.CAP_CHI_CUC.equalsIgnoreCase(capDvi)) {
            builder.append("INNER JOIN HhQdGiaoNvuNhapxuatDtl qdCt ON qdCt.parent = qd ");
        }
        setConditionSearch(req, builder, capDvi);
        builder.append("ORDER BY qd.id DESC");

        TypedQuery<HhQdGiaoNvuNhapxuatHdr> query = em.createQuery(builder.toString(), HhQdGiaoNvuNhapxuatHdr.class);

        //Set params
        this.setParameterSearch(req, query);

        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        //Set pageable
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }


    private void setConditionSearch(HhQdNhapxuatSearchReq req, StringBuilder builder, String capDvi) {
        builder.append("WHERE 1 = 1 ");

        if (req.getNamNhap() != null) {
            builder.append("AND ").append("qd.namNhap = :namNhap ");
        }

        if (!StringUtils.isEmpty(req.getVeViec())) {
            builder.append("AND ").append("qd.veViec LIKE :veViec ");
        }
        if (!StringUtils.isEmpty(req.getSoQd())) {
            builder.append("AND ").append("qd.soQd LIKE :soQd ");
        }

        if (req.getTuNgayQd() != null) {
            builder.append("AND ").append("qd.ngayQdinh >= :tuNgayQd ");
        }
        if (req.getDenNgayQd() != null) {
            builder.append("AND ").append("qd.ngayQdinh <= :denNgayQd ");
        }

        if (!StringUtils.isEmpty(req.getTrangThai())) {
            builder.append("AND ").append("qd.trangThai = :trangThai ");
        }

        if (!StringUtils.isEmpty(req.getTrichYeu())) {
            builder.append("AND ").append("qd.trichYeu LIKE :trichYeu ");
        }

        if (!StringUtils.isEmpty(req.getMaDvi())) {
            if (Contains.CAP_CHI_CUC.equalsIgnoreCase(capDvi)) {
                builder.append("AND ").append("qdCt.maDvi = :maDvi ");
            } else {
                builder.append("AND ").append("qd.maDvi = :maDvi ");
            }
        }
    }

    @Override
    public int count(HhQdNhapxuatSearchReq req, String capDvi) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(qd.id) FROM HhQdGiaoNvuNhapxuatHdr qd ");
        if (Contains.CAP_CHI_CUC.equalsIgnoreCase(capDvi)) {
            builder.append("INNER JOIN HhQdGiaoNvuNhapxuatDtl qdCt ON qdCt.parent = qd ");
        }
        this.setConditionSearch(req, builder, capDvi);
        TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);
        this.setParameterSearch(req, query);
        return query.getSingleResult().intValue();
    }

    private void setParameterSearch(HhQdNhapxuatSearchReq req, Query query) {
        if (req.getNamNhap() != null) {
            query.setParameter("namNhap", req.getNamNhap());
        }

        if (!StringUtils.isEmpty(req.getVeViec())) {
            query.setParameter("veViec", "%" + req.getVeViec() + "%");
        }
        if (!StringUtils.isEmpty(req.getSoQd())) {
            query.setParameter("soQd", "%" + req.getSoQd() + "%");
        }

        if (req.getTuNgayQd() != null) {
            query.setParameter("tuNgayQd", req.getTuNgayQd());
        }

        if (req.getDenNgayQd() != null) {
            query.setParameter("denNgayQd", req.getDenNgayQd());
        }

        if (!StringUtils.isEmpty(req.getMaDvi())) {
            query.setParameter("maDvi", req.getMaDvi());
        }

        if (!StringUtils.isEmpty(req.getTrangThai())) {
            query.setParameter("trangThai", req.getTrangThai());
        }

        if (!StringUtils.isEmpty(req.getTrichYeu())) {
            query.setParameter("trichYeu", "%" + req.getTrichYeu() + "%");
        }
    }
}
