package com.tcdt.qlnvhang.repository.xuathang.bbtinhkho;

import com.tcdt.qlnvhang.request.search.xuathang.XhBienBanTinhKhoSearchReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhQdGiaoNvuXuatSearchReq;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class XhBienBanTinhKhoRepositoryCustomImpl implements XhBienBanTinhKhoRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Object[]> search(XhBienBanTinhKhoSearchReq req) {

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * from xh_bb_tinh_kho x ");
        if(checkDateReq(req)){
            builder.append("inner join ( SELECT ngay_pduyet as tuNgay, ngay_pduyet as denNgay, ma_lokho as lokho, ma_chung_loai_hang_hoa as clh from xh_phieu_xuat_kho order by id fetch first 1 rows only ) pn ");
            builder.append("on pn.lokho = x.ma_lokho and pn.clh = x.ma_chung_loai_hang_hoa ");
        }
        setConditionSearch(builder, req);
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
        if(checkDateReq(req)){
            builder.append("inner join ( SELECT ngay_pduyet as tuNgay, ngay_pduyet as denNgay, ma_lokho as lokho, ma_chung_loai_hang_hoa as clh from xh_phieu_xuat_kho order by id fetch first 1 rows only ) pn ");
            builder.append("on pn.lokho = x.ma_lokho and pn.clh = x.ma_chung_loai_hang_hoa ");
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
        if(checkDateReq(req)){
            builder.append("AND ").append("pn.tuNgay > :tuNgay ");
            builder.append("AND ").append("pn.denNgay < :denNgay ");
        }
    }

    private void setParameterSearch(XhBienBanTinhKhoSearchReq req, Query query) {
        if (!StringUtils.isEmpty(req.getQuyetDinhId())) {
            query.setParameter("qdId", req.getQuyetDinhId());
        }
        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            query.setParameter("soBienBan", req.getSoBienBan());
        }
        if(checkDateReq(req)){
            query.setParameter("tuNgay", req.getNgayXuatTu());
            query.setParameter("denNgay", req.getNgayXuatDen());
        }
    }

    private boolean checkDateReq(XhBienBanTinhKhoSearchReq req){
        return !StringUtils.isEmpty(req.getNgayXuatTu())&&!StringUtils.isEmpty(req.getNgayXuatDen());
    }
}
