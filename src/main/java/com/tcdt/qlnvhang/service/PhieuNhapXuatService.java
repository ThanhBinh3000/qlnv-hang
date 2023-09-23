package com.tcdt.qlnvhang.service;

import com.tcdt.qlnvhang.repository.PhieuNhapXuatRepository;
import com.tcdt.qlnvhang.request.search.QlnvQdKQDGHangSearchReq;
import com.tcdt.qlnvhang.response.luukho.TheKhoCtResponse;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.PhieuNhapXuatHistory;
import com.tcdt.qlnvhang.util.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class PhieuNhapXuatService extends BaseServiceImpl {
    @Autowired
    private PhieuNhapXuatRepository phieuNhapXuatRepository;

    public List<TheKhoCtResponse> listPhieuTheKhoCt(QlnvQdKQDGHangSearchReq req) throws Exception {
        List<TheKhoCtResponse> listResult = new ArrayList<>();
        List<PhieuNhapXuatHistory> dsPhieuNhapXuat = phieuNhapXuatRepository.selectPage(req.getLoaiVthh(), req.getCloaiVthh(),convertDateToString(req.getTuNgay()),convertDateToString(req.getDenNgay()));
        if (!CollectionUtils.isEmpty(dsPhieuNhapXuat)) {
            dsPhieuNhapXuat.forEach(phieuNx -> {
                TheKhoCtResponse theKhoCtResponse = new TheKhoCtResponse();
                theKhoCtResponse.setNgay(phieuNx.getNgayDuyet());
                theKhoCtResponse.setLoaiPhieu(phieuNx.getLoaiNhapXuat());
                theKhoCtResponse.setSoPhieu(phieuNx.getSoPhieu());
                theKhoCtResponse.setSlThucTe(DataUtils.safeToBigDecimal(phieuNx.getSoLuong()));
                theKhoCtResponse.setSlChungTu(phieuNx.getSoLuongChungTu());
                theKhoCtResponse.setMaBanGhi(phieuNx.getIdPhieu());
                listResult.add(theKhoCtResponse);
            });
        }
        return listResult;
    }
}
