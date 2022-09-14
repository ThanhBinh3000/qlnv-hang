package com.tcdt.qlnvhang.repository.xuathang.bbtinhkho;

import com.tcdt.qlnvhang.request.search.xuathang.XhBienBanTinhKhoSearchReq;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class XhBienBanTinhKhoRepositoryCustomImpl implements XhBienBanTinhKhoRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Object> search(XhBienBanTinhKhoSearchReq req) {

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT x.id from xh_bb_tinh_kho x ");
        if (checkDateReq(req)) {
            builder.append("inner join xh_phieu_xuat_kho pn ");
            builder.append("on pn.ma_lokho = x.ma_lokho and pn.ma_chung_loai_hang_hoa = x.ma_chung_loai_hang_hoa ");
        }
        setConditionSearch(builder, req);
        builder.append("GROUP BY x.id");
        Query query = em.createNativeQuery(builder.toString());
        setParameterSearch(req, query);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        //Set pageable
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());
        return query.getResultList();
    }

    @Override
    public int count(XhBienBanTinhKhoSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(DISTINCT x.id) from xh_bb_tinh_kho x ");
        if (checkDateReq(req)) {
            builder.append("inner join xh_phieu_xuat_kho pn ");
            builder.append("on pn.ma_lokho = x.ma_lokho and pn.ma_chung_loai_hang_hoa = x.ma_chung_loai_hang_hoa ");
        }
        setConditionSearch(builder, req);
        Query query = em.createNativeQuery(builder.toString());
        setParameterSearch(req, query);
        return Integer.parseInt(query.getSingleResult().toString());
    }

    private void setConditionSearch(StringBuilder builder, XhBienBanTinhKhoSearchReq req) {
        builder.append("WHERE 1 = 1 ");

        if (!StringUtils.isEmpty(req.getQuyetDinhId())) {
            builder.append("AND ").append("x.qdgnvnx_id LIKE :qdId ");
        }
        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            builder.append("AND ").append("x.so_bien_ban LIKE :soBienBan ");
        }
        if (checkDateReq(req)) {
            builder.append("AND ").append("pn.ngay_pduyet > :tuNgay ");
            builder.append("AND ").append("pn.ngay_pduyet < :denNgay ");
        }
    }

    private void setParameterSearch(XhBienBanTinhKhoSearchReq req, Query query) {
        if (!StringUtils.isEmpty(req.getQuyetDinhId())) {
            query.setParameter("qdId", req.getQuyetDinhId());
        }
        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            query.setParameter("soBienBan", req.getSoBienBan());
        }
        if (checkDateReq(req)) {
            query.setParameter("tuNgay", req.getNgayXuatTu());
            query.setParameter("denNgay", req.getNgayXuatDen());
        }
    }

    private boolean checkDateReq(XhBienBanTinhKhoSearchReq req) {
        return !StringUtils.isEmpty(req.getNgayXuatTu()) && !StringUtils.isEmpty(req.getNgayXuatDen());
    }
}
