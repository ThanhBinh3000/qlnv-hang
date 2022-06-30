package com.tcdt.qlnvhang.repository.vattu.hosokythuat;

import com.tcdt.qlnvhang.request.search.vattu.hosokythuat.NhHoSoKyThuatSearchReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class NhHoSoKyThuatRepositoryCustomImpl implements NhHoSoKyThuatRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Object[]> search(NhHoSoKyThuatSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT p, nx.id, nx.soQd, vatTu.ma, vatTu.ten, vatTuCha.ma, vatTuCha.ten FROM NhHoSoKyThuat p ");
        builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON p.qdgnvnxId = nx.id ");
        builder.append("INNER JOIN QlnvDmVattu vatTu ON vatTu.ma = p.maVatTu ");
        builder.append("INNER JOIN QlnvDmVattu vatTuCha ON vatTuCha.ma = p.maVatTuCha ");
        setConditionSearch(req, builder);
        builder.append("ORDER BY p.id DESC");

        TypedQuery<Object[]> query = em.createQuery(builder.toString(), Object[].class);

        //Set params
        this.setParameterSearch(req, query);

        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        //Set pageable
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }


    private void setConditionSearch(NhHoSoKyThuatSearchReq req, StringBuilder builder) {
        builder.append("WHERE 1 = 1 ");

        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            builder.append("AND ").append("p.soBienBan LIKE :soBienBan ");
        }

        if (req.getNgayKiemTraTu() != null) {
            builder.append("AND ").append("p.ngayKiemTra >= :ngayKiemTraTu ");
        }
        if (req.getNgayKiemTraDen() != null) {
            builder.append("AND ").append("p.ngayKiemTra <= :ngayKiemTraDen ");
        }

        if (!StringUtils.isEmpty(req.getMaDvi())) {
            builder.append("AND ").append("p.maDvi = :maDvi ");
        }

        if (!StringUtils.isEmpty(req.getSoQdNhap())) {
            builder.append("AND ").append("nx.soQd LIKE :soQdNhap ");
        }

        if (!StringUtils.isEmpty(req.getLoaiVthh())) {
            builder.append("AND ").append("p.loaiVthh = :loaiVthh ");
        }

        if (!StringUtils.isEmpty(req.getMaVatTuCha())) {
            builder.append("AND ").append("p.maVatTuCha = :maVatTuCha ");
        }

        if (!StringUtils.isEmpty(req.getMaVatTu())) {
            builder.append("AND ").append("p.maVatTu = :maVatTu ");
        }
    }

    @Override
    public int count(NhHoSoKyThuatSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(DISTINCT p.id) FROM FROM NhHoSoKyThuat p ");
        builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON p.qdgnvnxId = nx.id ");
        builder.append("INNER JOIN QlnvDmVattu vatTu ON vatTu.ma = p.maVatTu ");
        builder.append("INNER JOIN QlnvDmVattu vatTuCha ON vatTuCha.ma = p.maVatTuCha ");

        this.setConditionSearch(req, builder);
        TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);
        this.setParameterSearch(req, query);
        return query.getSingleResult().intValue();
    }

    private void setParameterSearch(NhHoSoKyThuatSearchReq req, Query query) {
        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            query.setParameter("soBienBan", "%" + req.getSoBienBan() + "%");
        }

        if (req.getNgayKiemTraTu() != null) {
            query.setParameter("ngayKiemTraTu", req.getNgayKiemTraTu());
        }

        if (req.getNgayKiemTraDen() != null) {
            query.setParameter("ngayKiemTraDen", req.getNgayKiemTraDen());
        }

        if (!StringUtils.isEmpty(req.getMaDvi())) {
            query.setParameter("maDvi", req.getMaDvi());
        }

        if (!StringUtils.isEmpty(req.getSoQdNhap())) {
            query.setParameter("soQdNhap", "%" + req.getSoQdNhap() + "%");
        }

        if (!StringUtils.isEmpty(req.getLoaiVthh())) {
            query.setParameter("loaiVthh", req.getLoaiVthh());
        }

        if (!StringUtils.isEmpty(req.getMaVatTuCha())) {
            query.setParameter("maVatTuCha", req.getMaVatTuCha());
        }

        if (!StringUtils.isEmpty(req.getMaVatTu())) {
            query.setParameter("maVatTu", req.getMaVatTu());
        }
    }
}
