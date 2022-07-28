package com.tcdt.qlnvhang.repository.bandaugia.bienbanbandaugia;

import com.tcdt.qlnvhang.request.bandaugia.bienbanbandaugia.BhBbBanDauGiaSearchReq;
import com.tcdt.qlnvhang.request.search.BienBanLayMauSearchReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

public class BhBbBanDauGiaRepositoryCustomImpl implements BhBbBanDauGiaRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Object[]> search(BhBbBanDauGiaSearchReq req, Pageable pageable) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT bb, tbBanDauGia.id, tbBanDauGia.maThongBao, tbBanDauGia.hinhThucDauGia, tbBanDauGia.phuongThucDauGia FROM BhBbBanDauGia bb ");
        builder.append("LEFT JOIN ThongBaoBanDauGia tbBanDauGia ON bb.thongBaoBdgId = tbBanDauGia.id ");

        setConditionSearchCtkhn(req, builder);

        //Sort
        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            builder.append("ORDER BY ").append(sort.get()
                    .map(o -> o.getProperty() + " " + o.getDirection()).collect(Collectors.joining(", ")));
        }

        TypedQuery<Object[]> query = em.createQuery(builder.toString(), Object[].class);

        //Set params
        this.setParameterSearchCtkhn(req, query);
        //Set pageable
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }


    private void setConditionSearchCtkhn(BhBbBanDauGiaSearchReq req, StringBuilder builder) {
        builder.append("WHERE 1 = 1 ");


        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            builder.append("AND ").append("LOWER(bb.soBienBan) LIKE :soBienBan ");
        }

        if (req.getNgayToChucBdgTu() != null) {
            builder.append("AND ").append("bb.ngayToChuc >= :ngayToChucBdgTu ");
        }
        if (req.getNgayToChucBdgDen() != null) {
            builder.append("AND ").append("bb.ngayToChuc <= :ngayToChucBdgDen ");
        }

        if (!StringUtils.isEmpty(req.getMaVatTuCha())) {
            builder.append("AND ").append("bb.maVatTuCha = :maVatTuCha ");
        }

        if (!StringUtils.isEmpty(req.getMaVatTu())) {
            builder.append("AND ").append("bb.maVatTu = :maVatTu ");
        }

        if (!CollectionUtils.isEmpty(req.getMaDvis())) {
            builder.append("AND ").append("bb.maDvi IN :maDvis ");
        }

        if (!CollectionUtils.isEmpty(req.getTrangThais())) {
            builder.append("AND ").append("bb.trangThai IN :trangThais ");
        }

        if (!StringUtils.isEmpty(req.getLoaiVthh())) {
            builder.append("AND ").append("bb.loaiVthh = :loaiVthh ");
        }

        if (req.getNam() != null) {
            builder.append("AND ").append("bb.nam = :nam ");
        }

        if (!StringUtils.isEmpty(req.getMaThongBaoBdg())) {
            builder.append("AND ").append("LOWER(tbBanDauGia.maThongBao) LIKE :maThongBaoBdg ");
        }

        if (!StringUtils.isEmpty(req.getTrichYeu())) {
            builder.append("AND ").append("LOWER(bb.trichYeu) LIKE :trichYeu ");
        }
    }

    @Override
    public int count(BhBbBanDauGiaSearchReq req) {
        int total = 0;
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(DISTINCT bb.id) FROM BhBbBanDauGia bb ");
        builder.append("LEFT JOIN ThongBaoBanDauGia tbBanDauGia ON bb.thongBaoBdgId = tbBanDauGia.id ");
        this.setConditionSearchCtkhn(req, builder);

        TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);

        this.setParameterSearchCtkhn(req, query);
        return query.getSingleResult().intValue();
    }

    private void setParameterSearchCtkhn(BhBbBanDauGiaSearchReq req, Query query) {

        if (!StringUtils.isEmpty(req.getSoBienBan())) {
            query.setParameter("soBienBan", "%" + req.getSoBienBan() + "%");
        }

        if (req.getNgayToChucBdgTu() != null) {
            query.setParameter("ngayToChucBdgTu", req.getNgayToChucBdgTu());
        }
        if (req.getNgayToChucBdgDen() != null) {
            query.setParameter("ngayToChucBdgDen", req.getNgayToChucBdgDen());
        }

        if (!StringUtils.isEmpty(req.getMaVatTuCha())) {
            query.setParameter("maVatTuCha", req.getMaVatTuCha());
        }

        if (!StringUtils.isEmpty(req.getMaVatTu())) {
            query.setParameter("maVatTu", req.getMaVatTu());
        }

        if (!CollectionUtils.isEmpty(req.getMaDvis())) {
            query.setParameter("maDvis", req.getMaDvis());
        }

        if (!CollectionUtils.isEmpty(req.getTrangThais())) {
            query.setParameter("trangThais", req.getTrangThais());
        }

        if (!StringUtils.isEmpty(req.getLoaiVthh())) {
            query.setParameter("loaiVthh", req.getLoaiVthh());
        }

        if (req.getNam() != null) {
            query.setParameter("nam", req.getNam());
        }

        if (!StringUtils.isEmpty(req.getMaThongBaoBdg())) {
            query.setParameter("maThongBaoBdg", "%" + req.getMaThongBaoBdg().toLowerCase() + "%");
        }

        if (!StringUtils.isEmpty(req.getTrichYeu())) {
            query.setParameter("trichYeu", "%" + req.getLoaiVthh().toLowerCase() + "%");
        }
    }
}
