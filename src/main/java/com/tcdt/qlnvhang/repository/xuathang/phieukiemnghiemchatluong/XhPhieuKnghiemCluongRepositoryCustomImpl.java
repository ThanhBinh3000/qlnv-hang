package com.tcdt.qlnvhang.repository.xuathang.phieukiemnghiemchatluong;

import com.tcdt.qlnvhang.request.search.xuathang.XhPhieuKnghiemCluongSearchReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class XhPhieuKnghiemCluongRepositoryCustomImpl implements XhPhieuKnghiemCluongRepositoryCustom{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Object[]> search(XhPhieuKnghiemCluongSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT phieu, nganLo, nx.id, nx.soQd FROM XhPhieuKnghiemCluong phieu ");
        builder.append("INNER JOIN XhQdGiaoNvuXuat nx ON phieu.qdgnvxId = nx.id ");
        //TODO: Biên bản lấy mẫu
        builder.append("LEFT JOIN KtNganLo nganLo ON phieu.maNganLo = nganLo.maNganlo ");
        setConditionSearchCtkhn(req, builder);

        TypedQuery<Object[]> query = em.createQuery(builder.toString(), Object[].class);

        //Set params
        this.setParameterSearch(req, query);

        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        //Set pageable
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }


    private void setConditionSearchCtkhn(XhPhieuKnghiemCluongSearchReq req, StringBuilder builder) {
        builder.append("WHERE 1 = 1 ");

//        if (!StringUtils.isEmpty(req.getSoBbLayMau())) {
//            builder.append("AND ").append("LOWER(bbBanGiao.soBienBan) LIKE :soBbBanGiao ");
//        }
        if (!StringUtils.isEmpty(req.getSoPhieu())) {
            builder.append("AND ").append("LOWER(phieu.soPhieu) LIKE :soPhieu ");
        }

        if (req.getNgayKnghiemTu() != null) {
            builder.append("AND ").append("phieu.ngayKnghiem >= :ngayKnghiemTu ");
        }

        if (req.getNgayKnghiemDen() != null) {
            builder.append("AND ").append("phieu.ngayKnghiem <= :ngayKnghiemDen ");
        }


        if (!StringUtils.isEmpty(req.getSoQuyetDinh())) {
            builder.append("AND ").append("LOWER(nx.soQd) LIKE :soQuyetDinh ");
        }

        if (!StringUtils.isEmpty(req.getLoaiVthh())) {
            builder.append("AND ").append("phieu.loaiVthh = :loaiVthh ");
        }

        if (!CollectionUtils.isEmpty(req.getMaDvis())) {
            builder.append("AND ").append("phieu.maDvi IN :maDvis ");
        }

        if (!CollectionUtils.isEmpty(req.getTrangThais())) {
            builder.append("AND ").append("phieu.trangThai IN :trangThais ");
        }
    }

    @Override
    public int count(XhPhieuKnghiemCluongSearchReq req) {
        int total = 0;
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(1) FROM XhPhieuKnghiemCluong phieu ");
        builder.append("INNER JOIN XhQdGiaoNvuXuat nx ON phieu.qdgnvnxId = nx.id ");
        builder.append("LEFT JOIN KtNganLo nganLo ON phieu.maNganLo = nganLo.maNganlo ");
        this.setConditionSearchCtkhn(req, builder);

        TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);

        this.setParameterSearch(req, query);
        return query.getSingleResult().intValue();
    }

    private void setParameterSearch(XhPhieuKnghiemCluongSearchReq req, Query query) {

//        if (!StringUtils.isEmpty(req.getSoBbLayMau())) {
//            query.setParameter("soBbBanGiao", "%" + req.getSoBbLayMau().toLowerCase() + "%");
//        }

        if (!StringUtils.isEmpty(req.getSoPhieu())) {
            query.setParameter("soPhieu", "%" + req.getSoPhieu().toLowerCase() + "%");
        }

        if (req.getNgayKnghiemTu() != null) {
            query.setParameter("ngayKnghiemTu", req.getNgayKnghiemTu());
        }

        if (req.getNgayKnghiemDen() != null) {
            query.setParameter("ngayKnghiemDen", req.getNgayKnghiemDen());
        }

        if (!StringUtils.isEmpty(req.getSoQuyetDinh())) {
            query.setParameter("soQuyetDinh", "%" + req.getSoQuyetDinh().toLowerCase() + "%");
        }

        if (!StringUtils.isEmpty(req.getLoaiVthh())) {
            query.setParameter("loaiVthh", req.getLoaiVthh());
        }

        if (!CollectionUtils.isEmpty(req.getMaDvis())) {
            query.setParameter("maDvis", req.getMaDvis());
        }

        if (!CollectionUtils.isEmpty(req.getTrangThais())) {
            query.setParameter("trangThais", req.getTrangThais());
        }
    }
}
