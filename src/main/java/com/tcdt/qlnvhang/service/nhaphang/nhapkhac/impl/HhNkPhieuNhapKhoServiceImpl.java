package com.tcdt.qlnvhang.service.nhaphang.nhapkhac.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkPhieuNhapKhoDtl;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.nvnhap.HhQdGiaoNvuNhapHangKhacHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhNkPhieuNhapKhoDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdrReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhQdGiaoNvuNhapKhacSearch;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdrDTO;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdrListDTO;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.nhaphang.nhapkhac.HhNkPhieuNhapKhoService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.*;

@Service
public class HhNkPhieuNhapKhoServiceImpl extends BaseServiceImpl implements HhNkPhieuNhapKhoService {
    @Autowired
    private HhNkPhieuNhapKhoHdrRepository hdrRepository;
    @Autowired
    private HhNkPhieuNhapKhoDtlRepository dtlRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<HhNkPhieuNhapKhoHdrDTO> search(HhNkPhieuNhapKhoHdrReq req) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        req.setMaDvi(currentUser.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<HhNkPhieuNhapKhoHdrDTO> searchDto = hdrRepository.searchPage(req, pageable);
        return searchDto;
    }

    @Override
    public Page<HhNkPhieuNhapKhoHdr> searchPage(HhNkPhieuNhapKhoHdrReq req) throws Exception {
        return null;
    }

    @Override
    public HhNkPhieuNhapKhoHdr create(HhNkPhieuNhapKhoHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
//        Optional<HhNkPhieuNhapKhoHdr> optional = hdrRepository.findBySoPhieuNhapKho(req.getSoPhieuNhapKho());
//        if (optional.isPresent()) {
//            throw new Exception("Số biên bản đã tồn tại");
//        }

        HhNkPhieuNhapKhoHdr data = new HhNkPhieuNhapKhoHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(userInfo.getDvql());
        if (req.getSoPhieuNhapKho() != null) {
            data.setId(Long.parseLong(req.getSoPhieuNhapKho().split("/")[0]));
        }
        req.getChildren().forEach(e -> {
            e.setParent(data);
        });
        HhNkPhieuNhapKhoHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) + "/PNK-" + userInfo.getDvqlTenVietTat();
        created.setSoPhieuNhapKho(so);
        hdrRepository.save(created);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), HhNkPhieuNhapKhoHdr.TABLE_NAME);
        created.setFileDinhKems(canCu);
        return created;
    }

    @Override
    public HhNkPhieuNhapKhoHdr update(HhNkPhieuNhapKhoHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
        }
        Optional<HhNkPhieuNhapKhoHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        HhNkPhieuNhapKhoHdr data = optional.get();
        BeanUtils.copyProperties(req, data);
        data.setChildren(req.getChildren());
        HhNkPhieuNhapKhoHdr update = hdrRepository.save(data);
        String so = update.getId() + "/" + (new Date().getYear() + 1900) + "/PNK-" + userInfo.getDvqlTenVietTat();
        update.setSoPhieuNhapKho(so);
        hdrRepository.save(update);
        fileDinhKemService.delete(update.getId(), Lists.newArrayList(HhNkPhieuNhapKhoHdr.TABLE_NAME));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), update.getId(), HhNkPhieuNhapKhoHdr.TABLE_NAME);
        update.setFileDinhKems(canCu);
        return update;
    }

    @Override
    public HhNkPhieuNhapKhoHdr detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        if (Objects.isNull(id)) {
            throw new Exception("Id is null");
        }
        Optional<HhNkPhieuNhapKhoHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Số biên bản không tồn tại");
        }
        HhNkPhieuNhapKhoHdr data = optional.get();
        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singleton(HhNkPhieuNhapKhoHdr.TABLE_NAME)));
        return data;
    }

    @Override
    public HhNkPhieuNhapKhoHdr approve(HhNkPhieuNhapKhoHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Access denied.");
        }
        HhNkPhieuNhapKhoHdr hdr = detail(req.getId());
        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            // Arena các roll back approve
            case Contains.DUTHAO + Contains.CHODUYET_LDCC:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                hdr.setNguoiGDuyet(userInfo.getId());
                hdr.setNgayGDuyet(LocalDate.now());
                break;
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                if(hdr.getIdLanhDao() ==null){
                    hdr.setIdLanhDao(userInfo.getId());
                    hdr.setTenLanhDao(userInfo.getFullName());
                }
                hdr.setNguoiPDuyet(userInfo.getId());
                hdr.setNgayPDuyet(LocalDate.now());
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                if(hdr.getIdLanhDao() ==null){
                    hdr.setIdLanhDao(userInfo.getId());
                    hdr.setTenLanhDao(userInfo.getFullName());
                }
                hdr.setNguoiPDuyet(userInfo.getId());
                hdr.setNgayPDuyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        HhNkPhieuNhapKhoHdr created = hdrRepository.save(hdr);
        return created;
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<HhNkPhieuNhapKhoHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        HhNkPhieuNhapKhoHdr data = optional.get();
        List<HhNkPhieuNhapKhoDtl> list = dtlRepository.findByHdrId(data.getId());
        dtlRepository.deleteAll(list);
        hdrRepository.delete(data);
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (listMulti != null && !listMulti.isEmpty()) {
            listMulti.forEach(i -> {
                try {
                    delete(i);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            throw new Exception("List id is null");
        }
    }

    @Override
    public void export(HhNkPhieuNhapKhoHdrReq objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhNkPhieuNhapKhoHdrDTO> page = this.search(objReq);
        List<HhNkPhieuNhapKhoHdrDTO> data = page.getContent();

        String title = "Danh sách Phiếu nhập kho ";
        String[] rowsName = new String[]{"STT", "Năm kH", "Số công văn/đề xuất", "Ngày duyệt LĐ Cục", "Loại điều chuyển", "Đơn vị đề xuất", "Trạng thái",};
        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            HhNkPhieuNhapKhoHdrDTO dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getSoQdPdNk();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public List<HhNkPhieuNhapKhoHdrListDTO> searchList(HhNkPhieuNhapKhoHdrReq objReq) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        objReq.setMaDvi(currentUser.getDvql());
        List<HhNkPhieuNhapKhoHdrListDTO> searchDto = hdrRepository.searchList(objReq);
        return searchDto;
    }

    @Override
    public List<HhNkPhieuNhapKhoHdrListDTO> searchListChung(HhNkPhieuNhapKhoHdrReq objReq) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        objReq.setMaDvi(currentUser.getDvql());
        List<HhNkPhieuNhapKhoHdrListDTO> searchDto = hdrRepository.searchListChung(objReq);
        return searchDto;
    }

    public ReportTemplateResponse preview(HhNkPhieuNhapKhoHdrReq objReq) throws Exception {
        HhNkPhieuNhapKhoHdr optional = detail(objReq.getId());
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
    }
}
