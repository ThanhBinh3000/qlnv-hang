package com.tcdt.qlnvhang.repository.xuathang.bbtinhkho;

import com.tcdt.qlnvhang.entities.xuathang.bbtinhkho.XhBienBanTinhKho;
import com.tcdt.qlnvhang.request.search.xuathang.XhBienBanTinhKhoSearchReq;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class XhBienBanTinhKhoRepositoryCustomImpl implements XhBienBanTinhKhoRepositoryCustom{
    @PersistenceContext
    private EntityManager em;
    @Override
    public List<XhBienBanTinhKho> search(XhBienBanTinhKhoSearchReq req) {

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * from xhBienBanTinhKho ");
        return null;
    }

    @Override
    public int count(XhBienBanTinhKhoSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(DISTINCT p.id) FROM xhBienBanTinhKho p ");

        TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);
        return query.getSingleResult().intValue();
    }
}
