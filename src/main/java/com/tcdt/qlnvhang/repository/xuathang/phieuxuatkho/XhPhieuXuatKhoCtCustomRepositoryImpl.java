package com.tcdt.qlnvhang.repository.xuathang.phieuxuatkho;

import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class XhPhieuXuatKhoCtCustomRepositoryImpl implements XhPhieuXuatKhoCtCustomRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Object[]> searchCt(Long hangThanhLyId) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT l.ten, c.ten, d.slTon, d.slYeuCau, d.lyDo, nl.tenNganlo, nk.tenNgankho, n.tenNhakho, dk.tenDiemkho, c.maDviTinh from DsHangThanhLyCt d ");
        builder.append("inner join QlnvDmVattu l on d.maLoaiHang = l.ma ");
        builder.append("inner join QlnvDmVattu c on d.maChungLoaiHang = c.ma ");
        builder.append("inner join KtNganLo nl on nl.maNganlo = d.maLoKho ");
        builder.append("inner join KtNganKho nk on nk.maNgankho = d.maNganKho ");
        builder.append("inner join KtNhaKho n on n.maNhakho = d.maNhaKho ");
        builder.append("inner join KtDiemKho dk on dk.maDiemkho = d.maDiemKho ");
        setConditionSearchCt(hangThanhLyId, builder);
        TypedQuery<Object[]> query = em.createQuery(builder.toString(), Object[].class);
        setParameterSearchCt(hangThanhLyId, query);

        return query.getResultList();
    }

    private void setParameterSearchCt(Long id, TypedQuery<Object[]> query) {
        if (!StringUtils.isEmpty(id)) {
            query.setParameter("dsHangThanhLyId", id);
        }
    }

    private void setConditionSearchCt(Long id, StringBuilder builder) {
        builder.append("WHERE 1 = 1 ");

        if (!StringUtils.isEmpty(id)) {
            builder.append("AND ").append("d.dsHangThanhLyId = :dsHangThanhLyId ");
        }
    }


}
