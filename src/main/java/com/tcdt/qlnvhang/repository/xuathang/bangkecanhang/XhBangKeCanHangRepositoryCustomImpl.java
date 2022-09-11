package com.tcdt.qlnvhang.repository.xuathang.bangkecanhang;

import com.tcdt.qlnvhang.request.search.xuathang.XhBangKeCanHangSearchReq;
import com.tcdt.qlnvhang.response.xuathang.bangkecanhang.XhBangKeCanHangRes;
import org.joda.time.LocalDate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class XhBangKeCanHangRepositoryCustomImpl implements XhBangKeCanHangRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<XhBangKeCanHangRes> search(XhBangKeCanHangSearchReq req) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d.id,d.SO_BANG_KE,n.SO_QUYET_DINH,k.SO_PHIEU_XUAT_KHO,d.NGAY_NHAP,d.MA_DIEMKHO,d.MA_NHAKHO,d.MA_NGANKHO,d.MA_LOKHO,d.TRANG_THAI, ");
        builder.append("dk.TEN_DIEMKHO,nk.TEN_NHAKHO,ngk.TEN_NGANKHO,l.TEN_NGANLO ");
        builder.append(" FROM XH_PHIEU_XUAT_KHO d ");
        builder.append("INNER JOIN XH_QD_GIAO_NVU_XUAT n ON n.ID = d.SO_QD_XUAT_ID ");
        builder.append("INNER JOIN XH_PHIEU_XUAT_KHO k ON k.ID = d.PHIEU_XUAT_KHO_ID ");
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
        List<XhBangKeCanHangRes> response = data.stream().map(res -> {
            Tuple item = (Tuple) res;
            XhBangKeCanHangRes kh = new XhBangKeCanHangRes();
            kh.setId(item.get("id", BigDecimal.class).longValue());
            kh.setSoBangKe(item.get("SO_BANG_KE", String.class));
            kh.setSoSqdx(item.get("SO_QUYET_DINH", String.class));

            kh.setSoPhieuXuatKho(item.get("SO_PHIEU_XUAT_KHO", String.class));

            kh.setNgayNhap(item.get("NGAY_NHAP", LocalDate.class));

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

    private void setConditionSearch(XhBangKeCanHangSearchReq req, StringBuilder builder) {
        builder.append("WHERE 1 = 1 ");

        if (!StringUtils.isEmpty(req.getSoQuyetDinh())) {
            builder.append("AND ").append("n.SO_QUYET_DINH like :soQuyetDinh ");
        }
        if (!StringUtils.isEmpty(req.getSoBangKe())) {
            builder.append("AND ").append("d.SO_BANG_KE like :soBangKe ");
        }

        if (!StringUtils.isEmpty(req.getTuNgay())) {
            builder.append("AND ").append("d.NGAY_NHAP >= :tuNgay ");
        }
        if (!StringUtils.isEmpty(req.getDenNgay())) {
            builder.append("AND ").append("d.NGAY_NHAP <= :denNgay ");
        }

    }

    private void setParameterSearch(XhBangKeCanHangSearchReq req, Query query) {
        if (!StringUtils.isEmpty(req.getSoQuyetDinh())) {
            query.setParameter("soQuyetDinh", "%" + req.getSoQuyetDinh() + "%");
        }
        if (!StringUtils.isEmpty(req.getSoBangKe())) {
            query.setParameter("soBangKe", "%" + req.getSoBangKe() + "%");
        }
        if (!StringUtils.isEmpty(req.getTuNgay())) {
            query.setParameter("tuNgay", req.getTuNgay());
        }
        if (!StringUtils.isEmpty(req.getDenNgay())) {
            query.setParameter("denNgay", req.getDenNgay());
        }
    }

}
