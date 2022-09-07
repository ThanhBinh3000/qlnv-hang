package com.tcdt.qlnvhang.repository.bandaugia.quyetdinhpheduyetketquabandaugia;

import com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetketquabandaugia.BhQdPheDuyetKqbdgSearchReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

public class BhQdPheDuyetKqbdgRepositoryCustomImpl implements BhQdPheDuyetKqbdgRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Object[]> search(BhQdPheDuyetKqbdgSearchReq req, Pageable pageable) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT bb, " +
                "tbBanDauGia.id, tbBanDauGia.maThongBao, tbBanDauGia.hinhThucDauGia, tbBanDauGia.phuongThucDauGia, " +
                "bbBanDauGia.id, bbBanDauGia.soBienBan, bbBanDauGia.ngayToChucTu, " +
                "vatTuCha.ma, vatTuCha.ten," +
                "qdPdKhBdg.id, qdPdKhBdg.soQuyetDinh " +
                "FROM BhQdPheDuyetKqbdg bb ");
        builder.append("INNER JOIN QlnvDmVattu vatTuCha ON bb.maVatTuCha = vatTuCha.ma ");
        builder.append("LEFT JOIN ThongBaoBanDauGia tbBanDauGia ON bb.thongBaoBdgId = tbBanDauGia.id ");
        builder.append("LEFT JOIN BhQdPheDuyetKhbdg qdPdKhBdg ON tbBanDauGia.qdPheDuyetKhBdgId = qdPdKhBdg.id ");
        builder.append("LEFT JOIN BhBbBanDauGia bbBanDauGia ON bb.bienBanBdgId = bbBanDauGia.id ");

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


    private void setConditionSearchCtkhn(BhQdPheDuyetKqbdgSearchReq req, StringBuilder builder) {
        builder.append("WHERE 1 = 1 ");


        if (!StringUtils.isEmpty(req.getSoQuyetDinh())) {
            builder.append("AND ").append("LOWER(bb.soQuyetDinh) LIKE :soQuyetDinh ");
        }

        if (req.getNgayKyTu() != null) {
            builder.append("AND ").append("bb.ngayKy >= :ngayKyTu ");
        }
        if (req.getNgayKyDen() != null) {
            builder.append("AND ").append("bb.ngayKy <= :ngayKyDen ");
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


        if (!StringUtils.isEmpty(req.getTrichYeu())) {
            builder.append("AND ").append("LOWER(bb.trichYeu) LIKE :trichYeu ");
        }
    }

    @Override
    public int count(BhQdPheDuyetKqbdgSearchReq req) {
        int total = 0;
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(DISTINCT bb.id) FROM BhQdPheDuyetKqbdg bb ");
        builder.append("INNER JOIN QlnvDmVattu vatTuCha ON bb.maVatTuCha = vatTuCha.ma ");
        builder.append("LEFT JOIN ThongBaoBanDauGia tbBanDauGia ON bb.thongBaoBdgId = tbBanDauGia.id ");
        builder.append("LEFT JOIN BhQdPheDuyetKhbdg qdPdKhBdg ON tbBanDauGia.qdPheDuyetKhBdgId = qdPdKhBdg.id ");
        builder.append("LEFT JOIN BhBbBanDauGia bbBanDauGia ON bb.bienBanBdgId = bbBanDauGia.id ");
        this.setConditionSearchCtkhn(req, builder);

        TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);

        this.setParameterSearchCtkhn(req, query);
        return query.getSingleResult().intValue();
    }

    private void setParameterSearchCtkhn(BhQdPheDuyetKqbdgSearchReq req, Query query) {

        if (!StringUtils.isEmpty(req.getSoQuyetDinh())) {
            query.setParameter("soQuyetDinh", "%" + req.getSoQuyetDinh() + "%");
        }

        if (req.getNgayKyTu() != null) {
            query.setParameter("ngayKyTu", req.getNgayKyTu());
        }
        if (req.getNgayKyDen() != null) {
            query.setParameter("ngayKyDen", req.getNgayKyDen());
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

        if (!StringUtils.isEmpty(req.getTrichYeu())) {
            query.setParameter("trichYeu", "%" + req.getLoaiVthh().toLowerCase() + "%");
        }
    }
}
