package com.tcdt.qlnvhang.repository.xuathang.phieuxuatkho;

import com.tcdt.qlnvhang.request.search.xuathang.XhPhieuXuatKhoSearchReq;
import com.tcdt.qlnvhang.response.xuathang.phieuxuatkho.XhPhieuXuatKhoRes;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class XhPhieuXuatKhoRepositoryCustomImpl implements XhPhieuXuatKhoRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<XhPhieuXuatKhoRes> search(XhPhieuXuatKhoSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d.id,d.SO_PHIEU_XUAT_KHO,n.SO_QUYET_DINH,d.SO_HOP_DONG,d.XUAT_KHO,d.MA_DIEMKHO,d.MA_NHAKHO,d.MA_NGANKHO,d.MA_LOKHO,d.TRANG_THAI, ");
        builder.append("dk.TEN_DIEMKHO,nk.TEN_NHAKHO,ngk.TEN_NGANKHO,l.TEN_NGANLO ");
        builder.append(" FROM XH_PHIEU_XUAT_KHO d ");
        builder.append("INNER JOIN XH_QD_GIAO_NVU_XUAT n ON n.ID = d.SO_QD_XUAT_ID ");
        builder.append("inner join KT_DIEM_KHO dk on dk.MA_DIEMKHO = d.MA_DIEMKHO ");
        builder.append("inner join KT_NHA_KHO nk on nk.MA_NHAKHO = d.MA_NHAKHO ");
        builder.append("inner join KT_NGAN_KHO ngk on ngk.MA_NGANKHO = d.MA_NGANKHO ");
        builder.append("inner join KT_NGAN_LO l on l.MA_NGANLO = d.MA_LOKHO ");
        setConditionSearch(req, builder);

        Query query = em.createNativeQuery(builder.toString(), Tuple.class);
        setParameterSearch(req, query);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        //Set pageable
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize());

        List<?> data = query.getResultList();
        List<XhPhieuXuatKhoRes> response = data.stream().map(res -> {
            Tuple item = (Tuple) res;
            XhPhieuXuatKhoRes kh = new XhPhieuXuatKhoRes();
            kh.setId(item.get("id", BigDecimal.class).longValue());
            kh.setSpXuatKho(item.get("SO_PHIEU_XUAT_KHO", String.class));
            kh.setTenSqdx(item.get("SO_QUYET_DINH", String.class));
            kh.setSoHd(item.get("SO_HOP_DONG", String.class));
            Timestamp x = item.get("XUAT_KHO", Timestamp.class);
            kh.setXuatKho(LocalDate.parse(x.toLocalDateTime().toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE)));

            kh.setMaDiemkho(item.get("MA_DIEMKHO", String.class));
            kh.setTenDiemkho(item.get("TEN_DIEMKHO", String.class));
            kh.setMaNhakho(item.get("MA_NHAKHO", String.class));
            kh.setTenNhakho(item.get("TEN_NHAKHO", String.class));
            kh.setMaNgankho(item.get("MA_NGANKHO", String.class));
            kh.setTenNgankho(item.get("TEN_NGANKHO", String.class));
            kh.setMaNganlo(item.get("MA_LOKHO", String.class));
            kh.setTenNganlo(item.get("TEN_NGANLO", String.class));

            kh.setTrangThai(item.get("TRANG_THAI", String.class));
            return kh;
        }).collect(Collectors.toList());

        return response;
    }

    private void setConditionSearch(XhPhieuXuatKhoSearchReq req, StringBuilder builder) {
        builder.append("WHERE 1 = 1 ");

        if (!StringUtils.isEmpty(req.getSoQuyetDinh())) {
            builder.append("AND ").append("n.SO_QUYET_DINH like :soQuyetDinh ");
        }
        if (!StringUtils.isEmpty(req.getSoPhieu())) {
            builder.append("AND ").append("d.SO_PHIEU_XUAT_KHO = :soPhieu ");
        }

        if (!StringUtils.isEmpty(req.getTuNgay())) {
            builder.append("AND ").append("d.XUAT_KHO >= :tuNgay ");
        }
        if (!StringUtils.isEmpty(req.getDenNgay())) {
            builder.append("AND ").append("d.XUAT_KHO <= :denNgay ");
        }

        if (!StringUtils.isEmpty(req.getSoHd())) {
            builder.append("AND ").append("d.SO_HOP_DONG = :soHd ");
        }

    }

    private void setParameterSearch(XhPhieuXuatKhoSearchReq req, Query query) {
        if (!StringUtils.isEmpty(req.getSoQuyetDinh())) {
            query.setParameter("soQuyetDinh", "%" + req.getSoQuyetDinh() + "%");
        }
        if (!StringUtils.isEmpty(req.getSoPhieu())) {
            query.setParameter("soPhieu", req.getSoPhieu());
        }
        if (!StringUtils.isEmpty(req.getTuNgay())) {
            query.setParameter("tuNgay", req.getTuNgay());
        }
        if (!StringUtils.isEmpty(req.getDenNgay())) {
            query.setParameter("denNgay", req.getDenNgay());
        }
        if (!StringUtils.isEmpty(req.getSoHd())) {
            query.setParameter("soHd", req.getSoHd());
        }
    }


    @Override
    public int count(XhPhieuXuatKhoSearchReq req) {
        return 1230;
    }

}
