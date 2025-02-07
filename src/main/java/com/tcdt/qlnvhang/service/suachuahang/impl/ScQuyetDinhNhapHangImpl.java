package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScKiemTraChatLuongHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScQuyetDinhNhapHangDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScQuyetDinhNhapHangRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhNhapHangReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.suachuahang.ScQuyetDinhNhapHangService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.*;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Service
public class ScQuyetDinhNhapHangImpl extends BaseServiceImpl implements ScQuyetDinhNhapHangService {
    @Autowired
    private ScQuyetDinhNhapHangRepository hdrRepository;

    @Autowired
    private ScQuyetDinhNhapHangDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private ScDanhSachServiceImpl scDanhSachServiceImpl;
    
    @Autowired
    private ScKiemTraChatLuongHdrRepository scKiemTraChatLuongHdrRepository;

    @Override
    public Page<ScQuyetDinhNhapHang> searchPage(ScQuyetDinhNhapHangReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        UserInfo userInfo = UserUtils.getUserInfo();
        if (userInfo.getCapDvi().equals(Contains.CAP_CUC)) {
            req.setMaDviSr(userInfo.getDvql());
        }
        Page<ScQuyetDinhNhapHang> searchPage = hdrRepository.searchPage(req, pageable);
        return searchPage;
    }

    @Override
    public ScQuyetDinhNhapHang create(ScQuyetDinhNhapHangReq req) throws Exception {
        UserInfo currentUser = SecurityContextService.getUser();
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!currentUser.getCapDvi().equals(Contains.CAP_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        ScQuyetDinhNhapHang hdr = new ScQuyetDinhNhapHang();
        BeanUtils.copyProperties(req, hdr);
        hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        hdr.setMaDvi(currentUser.getDvql());
        ScQuyetDinhNhapHang created = hdrRepository.save(hdr);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScQuyetDinhNhapHang.TABLE_NAME + "_CAN_CU");
        created.setFileCanCu(canCu);
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScQuyetDinhNhapHang.TABLE_NAME + "_DINH_KEM");
        created.setFileDinhKem(fileDinhKem);
        created.setChildren(saveDtl(req,created.getId()));
        return created;
    }

    public List<ScQuyetDinhNhapHangDtl> saveDtl(ScQuyetDinhNhapHangReq req, Long idHdr){
        dtlRepository.deleteAllByIdHdr(idHdr);
        req.getChildren().forEach(item -> {
            item.setId(null);
            item.setIdHdr(idHdr);
            dtlRepository.save(item);
        });
        return req.getChildren();
    }

    @Override
    public ScQuyetDinhNhapHang update(ScQuyetDinhNhapHangReq req) throws Exception {
        UserInfo currentUser = SecurityContextService.getUser();
        if (!currentUser.getCapDvi().equals(Contains.CAP_CUC)) {
            throw new Exception("Quyết định xuất hàng chỉ được thực hiện ở cấp cục");
        }
        Optional<ScQuyetDinhNhapHang> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        ScQuyetDinhNhapHang hdr = optional.get();
        BeanUtils.copyProperties(req, hdr);
        ScQuyetDinhNhapHang created = hdrRepository.save(hdr);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(ScQuyetDinhNhapHang.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), created.getId(), ScQuyetDinhNhapHang.TABLE_NAME + "_CAN_CU");
        created.setFileCanCu(canCu);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(ScQuyetDinhNhapHang.TABLE_NAME + "_DINH_KEM"));
        List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScQuyetDinhNhapHang.TABLE_NAME + "_DINH_KEM");
        created.setFileDinhKem(fileDinhKemList);
        if(req.getChildren() != null && !req.getChildren().isEmpty()){
            List<ScQuyetDinhNhapHangDtl> scPhieuXuatKhoDtls = saveDtl(req, created.getId());
            created.setChildren(scPhieuXuatKhoDtls);
        }
        return created;
    }

    @Override
    public ScQuyetDinhNhapHang detail(Long id) throws Exception {
        Optional<ScQuyetDinhNhapHang> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScQuyetDinhNhapHang data = optional.get();
        List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Collections.singleton(ScQuyetDinhNhapHang.TABLE_NAME + "_CAN_CU"));
        data.setFileCanCu(canCu);
        List<FileDinhKem> fileDinhKemList = fileDinhKemService.search(data.getId(), Collections.singleton(ScQuyetDinhNhapHang.TABLE_NAME + "_DINH_KEM"));
        data.setFileDinhKem(fileDinhKemList);
        Map<String, String> mapDmucDvi = getMapTenDvi();
        data.setTenDvi(mapDmucDvi.getOrDefault(data.getMaDvi(),null));
        List<ScQuyetDinhNhapHangDtl> allByIdHdr = dtlRepository.findAllByIdHdr(id);
        allByIdHdr.forEach( item -> {
            try {
                item.setScDanhSachHdr(scDanhSachServiceImpl.detail(item.getIdDsHdr()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        data.setChildren(allByIdHdr);
        return data;
    }

    @Override
    public ScQuyetDinhNhapHang approve(ScQuyetDinhNhapHangReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<ScQuyetDinhNhapHang> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScQuyetDinhNhapHang hdr = optional.get();
        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            // Re approve : gửi lại duyệt
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_TP:
                break;
            // Arena các cấp duuyệt
            case Contains.DUTHAO + Contains.CHODUYET_TP:
            case Contains.CHODUYET_TP + Contains.CHODUYET_LDC:
                if(!userInfo.getCapDvi().equals(Contains.CAP_CUC)){
                    throw new Exception("Đơn vị gửi duyệt phải là cấp cục");
                }
                break;
            case Contains.CHODUYET_LDC + Contains.BAN_HANH:
                if(!userInfo.getCapDvi().equals(Contains.CAP_CUC)){
                    throw new Exception("Đơn vị gửi duyệt phải là cấp cục");
                }
                hdr.setIdLdc(userInfo.getId());
                hdr.setTenLdc(userInfo.getFullName());
                hdr.setNgayKy(LocalDate.now());
                break;
            // Arena từ chối
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_LDC + Contains.TUCHOI_LDC:
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        ScQuyetDinhNhapHang save = hdrRepository.save(hdr);
        return save;
    }

    @Override
    public void delete(Long id) throws Exception {
        UserInfo currentUser = SecurityContextService.getUser();
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!currentUser.getCapDvi().equals(Contains.CAP_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<ScQuyetDinhNhapHang> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        fileDinhKemService.delete(id, Lists.newArrayList(ScQuyetDinhXuatHang.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.delete(id, Lists.newArrayList(ScQuyetDinhXuatHang.TABLE_NAME + "_DINH_KEM"));
        hdrRepository.delete(optional.get());
        dtlRepository.deleteAllByIdHdr(id);
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(ScQuyetDinhNhapHangReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<ScQuyetDinhNhapHang> page = searchPage(req);
        List<ScQuyetDinhNhapHang> data = page.getContent();

        String title = "Danh sách phiếu xuất kho";
        String[] rowsName = new String[]{"STT", "Năm xuất", "Số QĐ giao NVXH", "Ngày ký QĐ giao NVXH", "Thời hạn xuất sửa chữa","Thời hạn nhập sửa chữa","Số QĐ SC hàng DTQG","Trích yếu", "Trạng thái QĐ","Trạng thái xuất để SC"};
        String fileName = "danh-sach.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            ScQuyetDinhNhapHang dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = dx.getNam();
//            objs[2] = dx.getSoQdXh();
            objs[3] = dx.getNam();
//            objs[4] = dx.getNgayXuatKho();
//            objs[5] = dx.getTenDiemKho()+"/"+dx.getTenNhaKho()+"/"+dx.getTenNganKho()+"/"+dx.getTenLoKho();
//            objs[6] = dx.getSoPhieuXuatKho();
//            objs[7] = dx.getNgayXuatKho();
            objs[8] = null;
            objs[9] = null;
            objs[10] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public List<ScQuyetDinhNhapHang> dsTaoPhieuNhapKho(ScQuyetDinhNhapHangReq req) throws Exception {
        UserInfo currentUser = SecurityContextService.getUser();
        if (currentUser == null){
            throw new Exception("Access denied.");
        }
        String dvql = currentUser.getDvql();
        if (currentUser.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            req.setMaDviSr(dvql.substring(0,6));
        }else{
            req.setMaDviSr(dvql);
        }
        req.setTrangThai(TrangThaiAllEnum.BAN_HANH.getId());
        List<ScQuyetDinhNhapHang> list = hdrRepository.listTaoPhieuNhapKho(req);
        return list;
    }

    @Override
    public ReportTemplateResponse preview(ScQuyetDinhNhapHangReq objReq) throws Exception {
        ScQuyetDinhNhapHang optional = detail(objReq.getId());
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
    }
}
