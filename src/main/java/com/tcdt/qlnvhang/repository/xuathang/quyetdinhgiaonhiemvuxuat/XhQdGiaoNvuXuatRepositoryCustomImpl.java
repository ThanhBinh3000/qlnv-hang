package com.tcdt.qlnvhang.repository.xuathang.quyetdinhgiaonhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.XhQdGiaoNvuXuat;
import com.tcdt.qlnvhang.request.search.xuathang.XhQdGiaoNvuXuatSearchReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class XhQdGiaoNvuXuatRepositoryCustomImpl implements XhQdGiaoNvuXuatRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<XhQdGiaoNvuXuat> search(XhQdGiaoNvuXuatSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT p FROM XhQdGiaoNvuXuat p ");
        setConditionSearch(req, builder);
        builder.append("ORDER BY p.id DESC");

        TypedQuery<XhQdGiaoNvuXuat> query = em.createQuery(builder.toString(), XhQdGiaoNvuXuat.class);

        //Set params
        this.setParameterSearch(req, query);

        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        //Set pageable
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }


    private void setConditionSearch(XhQdGiaoNvuXuatSearchReq req, StringBuilder builder) {
        builder.append("WHERE 1 = 1 ");

        if (!StringUtils.isEmpty(req.getSoQuyetDinh())) {
            builder.append("AND ").append("LOWER(p.soQuyetDinh) LIKE :soQuyetDinh ");
        }

        if (!StringUtils.isEmpty(req.getTrichyeu())) {
            builder.append("AND ").append("LOWER(p.trichYeu) LIKE :trichYeu ");
        }

        if (req.getNgayKyTu() != null) {
            builder.append("AND ").append("p.ngayQuyetDinh >= :ngayKyTu ");
        }
        if (req.getNgayKyDen() != null) {
            builder.append("AND ").append("p.ngayQuyetDinh <= :ngayKyDen ");
        }

        if (!CollectionUtils.isEmpty(req.getMaDvis())) {
            builder.append("AND ").append("p.maDvi IN :maDvis ");
        }

        if (!CollectionUtils.isEmpty(req.getTrangThais())) {
            builder.append("AND ").append("p.trangThai IN :trangThais ");
        }

        if (req.getNamXuat() != null) {
            builder.append("AND ").append("p.namXuat = :namXuat ");
        }

        if (!StringUtils.isEmpty(req.getLoaiVthh())) {
            builder.append("AND ").append("p.loaiVthh = :loaiVthh ");
        }
    }

    @Override
    public int count(XhQdGiaoNvuXuatSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(DISTINCT p.id) FROM XhQdGiaoNvuXuat p ");

        this.setConditionSearch(req, builder);
        TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);
        this.setParameterSearch(req, query);
        return query.getSingleResult().intValue();
    }

    private void setParameterSearch(XhQdGiaoNvuXuatSearchReq req, Query query) {
        if (!StringUtils.isEmpty(req.getSoQuyetDinh())) {
            query.setParameter("soQuyetDinh", "%" + req.getSoQuyetDinh().toLowerCase() + "%");
        }

        if (!StringUtils.isEmpty(req.getTrichyeu())) {
            query.setParameter("trichYeu", "%" + req.getTrichyeu().toLowerCase() + "%");
        }

        if (req.getNgayKyTu() != null) {
            query.setParameter("ngayKyTu", req.getNgayKyTu());
        }

        if (req.getNgayKyDen() != null) {
            query.setParameter("ngayKyDen", req.getNgayKyDen());
        }

        if (!CollectionUtils.isEmpty(req.getMaDvis())) {
            query.setParameter("maDvis", req.getMaDvis());
        }

        if (!CollectionUtils.isEmpty(req.getTrangThais())) {
            query.setParameter("trangThais", req.getTrangThais());
        }

        if (req.getNamXuat() != null) {
            query.setParameter("namXuat", req.getNamXuat());
        }

        if (!StringUtils.isEmpty(req.getLoaiVthh())) {
            query.setParameter("loaiVthh", req.getLoaiVthh());
        }
    }
}
