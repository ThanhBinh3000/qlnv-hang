package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.phieunhapkho;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoReq;
import com.tcdt.qlnvhang.request.object.sokho.LkPhieuNhapKhoReq;
import com.tcdt.qlnvhang.request.search.HhDxKhLcntThopSearchReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

public interface NhPhieuNhapKhoService extends BaseService<NhPhieuNhapKho,NhPhieuNhapKhoReq,Long> {

    List<NhPhieuNhapKho> findAllByIdQdGiaoNvNh(Long idQdGiaoNvNh);

    List<NhPhieuNhapKho> findAllByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh);

    Page<NhPhieuNhapKho> search(LkPhieuNhapKhoReq req) throws Exception;
    Page<NhQdGiaoNvuNhapxuatHdr> timKiem(NhPhieuNhapKhoReq req) throws Exception;
//    ReportTemplateResponse preview(NhPhieuNhapKhoReq objReq) throws Exception;
    ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception;

//    NhPhieuNhapKhoRes create(NhPhieuNhapKhoReq req) throws Exception;
//    NhPhieuNhapKhoRes update(NhPhieuNhapKhoReq req) throws Exception;
//    boolean delete(Long id) throws Exception;
//    NhPhieuNhapKhoRes detail(Long id) throws Exception;
//    boolean updateStatusQd(StatusReq req) throws Exception;
//
//    Page<NhPhieuNhapKhoRes> search(NhPhieuNhapKhoSearchReq req) throws Exception;
//
//    BaseNhapHangCount count(Set<String> maDvis) throws Exception;
//
//    @Transactional(rollbackOn = Exception.class)
//    boolean deleteMultiple(DeleteReq req) throws Exception;
//
    void exportToExcel(NhPhieuNhapKhoReq objReq, HttpServletResponse response) throws Exception;
//
//    Integer getSo() throws Exception;
}
