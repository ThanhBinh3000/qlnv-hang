package com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.entities.nhaphang.quanlyphieunhapkholuongthuc.NhPhieuNhapKho;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.request.search.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoSearchReq;
import com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoRes;
import com.tcdt.qlnvhang.table.khotang.KtDiemKho;
import com.tcdt.qlnvhang.table.khotang.KtNganKho;
import com.tcdt.qlnvhang.table.khotang.KtNganLo;
import com.tcdt.qlnvhang.table.khotang.KtNhaKho;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NhPhieuNhapKhoRepositoryCustomImpl implements NhPhieuNhapKhoRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<NhPhieuNhapKhoRes> search(NhPhieuNhapKhoSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT p, nganLo, nx.id, nx.soQd FROM NhPhieuNhapKho p ");
        builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON p.qdgnvnxId = nx.id ");
        builder.append("LEFT JOIN KtNganLo nganLo ON p.maNganLo = nganLo.maNganlo ");
        setConditionSearch(req, builder);
        builder.append("ORDER BY p.id DESC");

        TypedQuery<Object[]> query = em.createQuery(builder.toString(), Object[].class);

        //Set params
        this.setParameterSearch(req, query);

        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        //Set pageable
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

        List<Object[]> data = query.getResultList();

        List<NhPhieuNhapKhoRes> responses = new ArrayList<>();
        for (Object[] o : data) {
            NhPhieuNhapKhoRes response = new NhPhieuNhapKhoRes();
            NhPhieuNhapKho phieu = (NhPhieuNhapKho) o[0];
            KtNganLo nganLo = o[1] != null ? (KtNganLo) o[1] : null;
            Long qdNhapId = (Long) o[2];
            String soQdNhap = (String) o[3];
            BeanUtils.copyProperties(phieu, response);
            response.setTenTrangThai(TrangThaiEnum.getTenById(phieu.getTrangThai()));
            response.setTrangThaiDuyet(TrangThaiEnum.getTrangThaiDuyetById(phieu.getTrangThai()));
            this.thongTinNganLo(response, nganLo);
            response.setQdgnvnxId(qdNhapId);
            response.setSoQuyetDinhNhap(soQdNhap);
            responses.add(response);
        }

        return new PageImpl<>(responses, pageable, this.count(req));
    }

    private void thongTinNganLo(NhPhieuNhapKhoRes phieu, KtNganLo nganLo) {
        if (nganLo != null) {
            phieu.setTenNganLo(nganLo.getTenNganlo());
            KtNganKho nganKho = nganLo.getParent();
            if (nganKho == null)
                return;

            phieu.setTenNganKho(nganKho.getTenNgankho());
            phieu.setMaNganKho(nganKho.getMaNgankho());
            KtNhaKho nhaKho = nganKho.getParent();
            if (nhaKho == null)
                return;

            phieu.setTenNhaKho(nhaKho.getTenNhakho());
            phieu.setMaNhaKho(nhaKho.getMaNhakho());
            KtDiemKho diemKho = nhaKho.getParent();
            if (diemKho == null)
                return;

            phieu.setTenDiemKho(diemKho.getTenDiemkho());
            phieu.setMaDiemKho(diemKho.getMaDiemkho());
        }
    }


    private void setConditionSearch(NhPhieuNhapKhoSearchReq req, StringBuilder builder) {
        builder.append("WHERE 1 = 1 ");

        if (!Objects.isNull(req.getSoPhieu())) {
            builder.append("AND ").append("p.soPhieu = :soPhieu ");
        }
        if (req.getTuNgayNhapKho() != null) {
            builder.append("AND ").append("p.ngayNhapKho >= :tuNgayNhapKho ");
        }
        if (req.getDenNgayNhapKho() != null) {
            builder.append("AND ").append("p.ngayNhapKho <= :denNgayNhapKho ");
        }

        if (req.getTuNgayTaoPhieu() != null) {
            builder.append("AND ").append("p.ngayTaoPhieu >= :tuNgayTaoPhieu ");
        }
        if (req.getDenNgayTaoPhieu() != null) {
            builder.append("AND ").append("p.ngayTaoPhieu <= :denNgayTaoPhieu ");
        }

        if (req.getTuNgayGiaoNhan() != null) {
            builder.append("AND ").append("p.thoiGianGiaoNhan >= :tuNgayGiaoNhan ");
        }
        if (req.getDenNgayGiaoNhan() != null) {
            builder.append("AND ").append("p.thoiGianGiaoNhan <= :denNgayGiaoNhan ");
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

        if (!StringUtils.isEmpty(req.getLoaiVthh())) {
            builder.append("AND ").append("p.loaiVthh = :loaiVthh ");
        }
    }

    @Override
    public int count(NhPhieuNhapKhoSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(p.id) FROM NhPhieuNhapKho p ");
        builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON p.qdgnvnxId = nx.id ");
        builder.append("LEFT JOIN KtNganLo nganLo ON p.maNganLo = nganLo.maNganlo ");
        this.setConditionSearch(req, builder);

        TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);
        this.setParameterSearch(req, query);
        return query.getSingleResult().intValue();
    }

    private void setParameterSearch(NhPhieuNhapKhoSearchReq req, Query query) {
        if (!Objects.isNull(req.getSoPhieu())) {
            query.setParameter("soPhieu", req.getSoPhieu());
        }
        if (req.getTuNgayNhapKho() != null) {
            query.setParameter("tuNgayNhapKho", req.getTuNgayNhapKho());
        }
        if (req.getDenNgayNhapKho() != null) {
            query.setParameter("denNgayNhapKho", req.getDenNgayNhapKho());
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

        if (req.getTuNgayTaoPhieu() != null) {
            query.setParameter("tuNgayTaoPhieu", req.getTuNgayTaoPhieu());
        }
        if (req.getDenNgayTaoPhieu() != null) {
            query.setParameter("denNgayTaoPhieu", req.getDenNgayTaoPhieu());
        }

        if (req.getTuNgayGiaoNhan() != null) {
            query.setParameter("tuNgayGiaoNhan", req.getTuNgayGiaoNhan());
        }
        if (req.getDenNgayGiaoNhan() != null) {
            query.setParameter("denNgayGiaoNhan", req.getDenNgayGiaoNhan());
        }

        if (!StringUtils.isEmpty(req.getLoaiVthh())) {
            query.setParameter("loaiVthh", req.getLoaiVthh());
        }
    }
}
