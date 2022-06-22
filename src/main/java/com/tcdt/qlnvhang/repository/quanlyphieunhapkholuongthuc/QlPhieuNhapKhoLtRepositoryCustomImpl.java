package com.tcdt.qlnvhang.repository.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLt;
import com.tcdt.qlnvhang.enums.QlPhieuNhapKhoLtStatus;
import com.tcdt.qlnvhang.request.search.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtSearchReq;
import com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoLtRes;
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

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QlPhieuNhapKhoLtRepositoryCustomImpl implements QlPhieuNhapKhoLtRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<QlPhieuNhapKhoLtRes> search(QlPhieuNhapKhoLtSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT p, pktcl.id, pktcl.soPhieu, nganLo, nx.id, nx.soQd FROM QlPhieuNhapKhoLt p ");
        builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON p.qdgnvnxId = nx.id ");
        builder.append("INNER JOIN QlpktclhPhieuKtChatLuong pktcl ON p.phieuKtClId = pktcl.id ");
        builder.append("LEFT JOIN KtNganLo nganLo ON p.maNganLo = nganLo.maNganlo ");
        setConditionSearch(req, builder);
        builder.append("ORDER BY p.ngayLap DESC");

        TypedQuery<Object[]> query = em.createQuery(builder.toString(), Object[].class);

        //Set params
        this.setParameterSearch(req, query);

        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        //Set pageable
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

        List<Object[]> data = query.getResultList();

        List<QlPhieuNhapKhoLtRes> responses = new ArrayList<>();
        for (Object[] o : data) {
            QlPhieuNhapKhoLtRes response = new QlPhieuNhapKhoLtRes();
            QlPhieuNhapKhoLt phieu = (QlPhieuNhapKhoLt) o[0];
            Long pktclId = (Long) o[1];
            String soPhieuKtcl = (String) o[2];
            KtNganLo nganLo = o[3] != null ? (KtNganLo) o[3] : null;
            Long qdNhapId = (Long) o[4];
            String soQdNhap = (String) o[5];
            BeanUtils.copyProperties(phieu, response);
            response.setTenTrangThai(QlPhieuNhapKhoLtStatus.getTenById(phieu.getTrangThai()));
            response.setTrangThaiDuyet(QlPhieuNhapKhoLtStatus.getTrangThaiDuyetById(phieu.getTrangThai()));
            response.setPhieuKtClId(pktclId);
            response.setSoPhieuKtCl(soPhieuKtcl);
            this.thongTinNganLo(response, nganLo);
            response.setQdgnvnxId(qdNhapId);
            response.setSoQuyetDinhNhap(soQdNhap);
            responses.add(response);
        }

        return new PageImpl<>(responses, pageable, this.count(req));
    }

    private void thongTinNganLo(QlPhieuNhapKhoLtRes phieu, KtNganLo nganLo) {
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


    private void setConditionSearch(QlPhieuNhapKhoLtSearchReq req, StringBuilder builder) {
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

        if (!StringUtils.isEmpty(req.getMaDvi())) {
            builder.append("AND ").append("p.maDvi = :maDvi ");
        }

        if (!StringUtils.isEmpty(req.getSoQdNhap())) {
            builder.append("AND ").append("nx.soQd LIKE :soQdNhap ");
        }
    }

    @Override
    public int count(QlPhieuNhapKhoLtSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(p.id) FROM QlPhieuNhapKhoLt p ");
        builder.append("INNER JOIN HhQdGiaoNvuNhapxuatHdr nx ON p.qdgnvnxId = nx.id ");
        builder.append("INNER JOIN QlpktclhPhieuKtChatLuong pktcl ON p.phieuKtClId = pktcl.id ");
        builder.append("LEFT JOIN KtNganLo nganLo ON p.maNganLo = nganLo.maNganlo ");
        this.setConditionSearch(req, builder);

        TypedQuery<Long> query = em.createQuery(builder.toString(), Long.class);
        this.setParameterSearch(req, query);
        return query.getSingleResult().intValue();
    }

    private void setParameterSearch(QlPhieuNhapKhoLtSearchReq req, Query query) {
        if (!Objects.isNull(req.getSoPhieu())) {
            query.setParameter("soPhieu", req.getSoPhieu());
        }
        if (req.getTuNgayNhapKho() != null) {
            query.setParameter("tuNgayNhapKho", req.getTuNgayNhapKho());
        }
        if (req.getDenNgayNhapKho() != null) {
            query.setParameter("denNgayNhapKho", req.getDenNgayNhapKho());
        }


        if (!StringUtils.isEmpty(req.getMaDvi())) {
            query.setParameter("maDvi", req.getMaDvi());
        }

        if (!StringUtils.isEmpty(req.getSoQdNhap())) {
            query.setParameter("soQdNhap", "%" + req.getSoQdNhap() + "%");
        }
    }
}
