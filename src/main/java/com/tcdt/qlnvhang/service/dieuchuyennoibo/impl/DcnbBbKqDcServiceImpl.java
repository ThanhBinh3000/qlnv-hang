package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBbKqDcDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbBbKqDcHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbKqDcSearch;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbKqDcDTO;
import com.tcdt.qlnvhang.service.dieuchuyennoibo.DcnbBbKqDcService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbKqDcDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbKqDcHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DcnbBbKqDcServiceImpl implements DcnbBbKqDcService {
    @Autowired
    private DcnbBbKqDcHdrRepository hdrRepository;
    @Autowired
    private DcnbBbKqDcDtlRepository dtlRepository;
    @Autowired
    private QlnvDmDonviRepository qlnvDmDonviRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Autowired
    private FileDinhKemRepository fileDinhKemRepository;

    @Override
    public Page<DcnbBbKqDcHdr> search(CustomUserDetails currentUser, DcnbBbKqDcSearch req) {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbBbKqDcHdr> searchDto = hdrRepository.searchPage(req, pageable);
        return searchDto;
    }

    @Override
    public List<DcnbBbKqDcHdr> searchList(CustomUserDetails currentUser, DcnbBbKqDcSearch req) {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        return hdrRepository.searchList(req);
    }

    @Override
    public DcnbBbKqDcHdr create(DcnbBbKqDcDTO objReq) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        QlnvDmDonvi cqt = qlnvDmDonviRepository.findByMaDvi(currentUser.getDvql());
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
//        Optional<DcnbBbKqDcHdr> optional = hdrRepository.findFirstBySoBc(objReq.getSoBc());
//        if (optional.isPresent() && objReq.getSoBc() != null && objReq.getSoBc().split("/").length == 1) {
//            throw new Exception("số biên bản lấy mẫu đã tồn tại");
//        }
        DcnbBbKqDcHdr data = new DcnbBbKqDcHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(cqt.getMaDvi());
        data.setTenDvi(cqt.getTenDvi());
        // Biên bản lấy mẫu thì auto thay đổi thủ kho
        data.setTrangThai(Contains.DUTHAO);
        data.setNgayTao(LocalDateTime.now());
        data.setNguoiTaoId(currentUser.getUser().getId());
        objReq.getDanhSachDaiDien().forEach(e -> {
            e.setDcnbBbKqDcHdr(data);
        });
        DcnbBbKqDcHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) +"/BCKQNX-"+ currentUser.getUser().getDvqlTenVietTat();
        created.setSoBc(so);
        hdrRepository.save(created);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), DcnbBbKqDcHdr.TABLE_NAME);
        created.setFileDinhKems(canCu);
        return created;
    }

    @Override
    public DcnbBbKqDcHdr update(DcnbBbKqDcDTO objReq) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbBbKqDcHdr> optional = hdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
//        Optional<DcnbBbKqDcHdr> soDxuat = hdrRepository.findFirstBySoBc(objReq.getSoBc());
//        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoBc())) {
//            if (soDxuat.isPresent() && objReq.getSoBc().split("/").length == 1) {
//                if (!soDxuat.get().getId().equals(objReq.getId())) {
//                    throw new Exception("số biên bản lấy mẫu đã tồn tại");
//                }
//            }
//        }
        DcnbBbKqDcHdr data = optional.get();
        BeanUtils.copyProperties(objReq, data);
        data.setDanhSachDaiDien(objReq.getDanhSachDaiDien());
        DcnbBbKqDcHdr created = hdrRepository.save(data);
        String so = created.getId() + "/" + (new Date().getYear() + 1900) +"/BCKQNX-"+ currentUser.getUser().getDvqlTenVietTat();
        created.setSoBc(so);
        hdrRepository.save(created);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbBbKqDcHdr.TABLE_NAME));
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), DcnbBbKqDcHdr.TABLE_NAME);
        created.setFileDinhKems(fileDinhKem);
        return created;
    }

    @Override
    public DcnbBbKqDcHdr detail(Long id) throws Exception {
        Optional<DcnbBbKqDcHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Dữ liệu không tồn tại");
        }
        List<FileDinhKem> canCu = fileDinhKemRepository.findByDataIdAndDataTypeIn(id, Collections.singleton(DcnbBbKqDcHdr.TABLE_NAME));
        optional.get().setFileDinhKems(canCu);
        return optional.get();
    }

    @Override
    public void approve(StatusReq statusReq) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbBbKqDcHdr details = detail(Long.valueOf(statusReq.getId()));
        Optional<DcnbBbKqDcHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }

    public DcnbBbKqDcHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbBbKqDcHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.DUTHAO + Contains.CHODUYET_TP:
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_TP:
                optional.get().setNgayGDuyet(LocalDate.now());
                optional.get().setNguoiGDuyet(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
                optional.get().setNgayDuyetTp(LocalDate.now());
                optional.get().setNguoiDuyetTp(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_TP + Contains.CHODUYET_LDC:
                optional.get().setNgayDuyetTp(LocalDate.now());
                optional.get().setNguoiDuyetTp(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_LDC + Contains.TUCHOI_LDC:
                optional.get().setNgayPDuyet(LocalDate.now());
                optional.get().setNguoiPDuyet(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDC + Contains.DADUYET_LDC:
                optional.get().setNgayPDuyet(LocalDate.now());
                optional.get().setNguoiPDuyet(currentUser.getUser().getId());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbBbKqDcHdr created = hdrRepository.save(optional.get());
        return created;
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<DcnbBbKqDcHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        DcnbBbKqDcHdr data = optional.get();
        List<DcnbBbKqDcDtl> list = dtlRepository.findByHdrId(data.getId());
        dtlRepository.deleteAll(list);
        fileDinhKemService.delete(id, Lists.newArrayList(DcnbBbKqDcDtl.TABLE_NAME));
        hdrRepository.delete(data);
    }

    @Override
    public void export(DcnbBbKqDcSearch objReq, HttpServletResponse response) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<DcnbBbKqDcHdr> page = this.search(currentUser, objReq);
        List<DcnbBbKqDcHdr> data = page.getContent();

        String title = "Danh sách phương án xuất cứu trợ, viện trợ ";
        String[] rowsName = new String[]{"STT", "Năm kH", "Số công văn/đề xuất", "Ngày duyệt LĐ Cục", "Loại điều chuyển", "Đơn vị đề xuất", "Trạng thái",};
        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbBbKqDcHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[2] = dx.getNam();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public void deleteMulti(List<Long> ids) {

    }
}
