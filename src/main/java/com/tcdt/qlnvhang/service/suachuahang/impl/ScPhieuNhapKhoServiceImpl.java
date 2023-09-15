package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.*;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbPhieuNhapKhoHdrReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuNhapKhoReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhNhapHangReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.response.suachua.ScPhieuNhapKhoDTO;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.suachuahang.ScPhieuNhapKhoService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbDataLinkDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbDataLinkHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.UserUtils;
import org.docx4j.wml.Tr;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScPhieuNhapKhoServiceImpl extends BaseServiceImpl implements ScPhieuNhapKhoService {
    @Autowired
    private ScPhieuNhapKhoHdrRepository hdrRepository;
    @Autowired
    private ScPhieuNhapKhoDtlRepository dtlRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ScDanhSachServiceImpl scDanhSachServiceImpl;

    @Autowired
    private ScQuyetDinhNhapHangRepository scQuyetDinhNhapHangRepository;

    @Autowired
    private ScQuyetDinhNhapHangDtlRepository scQuyetDinhNhapHangDtlRepository;
    @Autowired
    private ScBangKeNhapVtHdrRepository scBangKeNhapVtHdrRepository;

    @Override
    public Page<ScPhieuNhapKhoHdr> searchPage(ScPhieuNhapKhoReq req) throws Exception {
        return null;
    }

    @Override
    public ScPhieuNhapKhoHdr create(ScPhieuNhapKhoReq req) throws Exception {
        UserInfo currentUser = SecurityContextService.getUser();
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!currentUser.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        ScPhieuNhapKhoHdr data = new ScPhieuNhapKhoHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setId(Long.parseLong(req.getSoPhieuNhapKho().split("/")[0]));
        data.setIdThuKho(currentUser.getId());
        ScPhieuNhapKhoHdr created = hdrRepository.save(data);
        saveFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScPhieuNhapKhoHdr.TABLE_NAME);
        List<ScPhieuNhapKhoDtl> dtlList = saveDtl(req, data.getId());
        created.setChildren(dtlList);
        return created;
    }

    public List<ScPhieuNhapKhoDtl> saveDtl(ScPhieuNhapKhoReq req,Long idHdr){
        dtlRepository.deleteAllByIdHdr(idHdr);
        req.getChildren().forEach(item -> {
            item.setId(null);
            item.setIdHdr(idHdr);
            dtlRepository.save(item);
        });
        return req.getChildren();
    }

    @Override
    public ScPhieuNhapKhoHdr update(ScPhieuNhapKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<ScPhieuNhapKhoHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        ScPhieuNhapKhoHdr data = optional.get();
        BeanUtils.copyProperties(req, data);
        fileDinhKemService.delete(data.getId(), Collections.singleton(ScPhieuNhapKhoHdr.TABLE_NAME));
        saveFileDinhKem(req.getFileDinhKemReq(), data.getId(), ScPhieuNhapKhoHdr.TABLE_NAME);
        List<ScPhieuNhapKhoDtl> dtlList = saveDtl(req, data.getId());
        data.setChildren(dtlList);
        return data;
    }

    @Override
    public ScPhieuNhapKhoHdr detail(Long id) throws Exception {
        Optional<ScPhieuNhapKhoHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        ScPhieuNhapKhoHdr data = optional.get();
        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singleton(ScPhieuNhapKhoHdr.TABLE_NAME)));
        data.setChildren(dtlRepository.findByIdHdr(id));
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        //set label
        data.setMapDmucDvi(mapDmucDvi);
        data.setMapVthh(mapVthh);
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
        if(!Objects.isNull(data.getIdThuKho())){
            data.setTenThuKho(userInfoRepository.findById(data.getIdThuKho()).get().getFullName());
        }
        if(!Objects.isNull(data.getIdLanhDaoCc())){
            data.setTenLanhDaoCc(userInfoRepository.findById(data.getIdLanhDaoCc()).get().getFullName());
        }
        if(data.getIdScDanhSachHdr() != null){
            ScDanhSachHdr byId = scDanhSachServiceImpl.detail(data.getIdScDanhSachHdr());
            data.setScDanhSachHdr(byId);
        }
        return data;
    }

    @Override
    public ScPhieuNhapKhoHdr approve(ScPhieuNhapKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<ScPhieuNhapKhoHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScPhieuNhapKhoHdr hdr = optional.get();

        if(Objects.isNull(hdr.getIdBangKeCanHang())){
            throw new Exception("Phiếu xuất kho đang chưa khởi tạo bảng kê nhập vật tư. Vui lòng tạo bảng kê nhâp vật tư");
        } else {
            Optional<ScBangKeNhapVtHdr> bkOp = scBangKeNhapVtHdrRepository.findById(hdr.getIdBangKeCanHang());
            if(bkOp.isPresent()){
                ScBangKeNhapVtHdr bk = bkOp.get();
                if(!bk.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDCC.getId())){
                    throw new Exception("Số bảng kê " +bk.getSoBangKe()+" chưa đc phê duyệt. Vui lòng phê duyệt bảng kê");
                }
            }else{
                throw new Exception("Phiếu xuất kho không tìm thấy bảng kê nhập vật tư. Vui lòng tạo bảng kê nhâp vật tư");
            }
        }

        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            // Re approve : gửi lại duyệt
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                break;
            // Arena các cấp duuyệt
            case Contains.DUTHAO + Contains.CHODUYET_LDCC:
                if(!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
                    throw new Exception("Đơn vị gửi duyệt phải là cấp cục");
                }
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                if(!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
                    throw new Exception("Đơn vị gửi duyệt phải là cấp cục");
                }
                hdr.setIdLanhDaoCc(userInfo.getId());
                break;
            // Arena từ chối
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        ScPhieuNhapKhoHdr save = hdrRepository.save(hdr);
        return save;
    }

    @Override
    public void delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<ScPhieuNhapKhoHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        if(!Objects.isNull(optional.get().getIdBangKeCanHang())){
            throw new Exception("Phiếu xuất kho "+optional.get().getSoPhieuNhapKho()+" đã tạo số bảng kê cân hàng. Không thể xóa");
        }
        hdrRepository.delete(optional.get());
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(ScPhieuXuatKhoHdr.TABLE_NAME));
        dtlRepository.deleteAllByIdHdr(optional.get().getId());
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(ScPhieuNhapKhoReq req, HttpServletResponse response) throws Exception {

    }

    @Override
    public Page<ScQuyetDinhNhapHang> searchPhieuNhapKho(ScPhieuNhapKhoReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        UserInfo userInfo = UserUtils.getUserInfo();
        ScQuyetDinhNhapHangReq reqQd = new ScQuyetDinhNhapHangReq();
        reqQd.setNam(req.getNam());
        reqQd.setSoQd(req.getSoQdNh());
        reqQd.setTrangThai(TrangThaiAllEnum.BAN_HANH.getId());
        if(userInfo.getCapDvi().equals(Contains.CAP_CUC)){
            reqQd.setMaDviSr(userInfo.getDvql());
        }
        if(userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
            reqQd.setMaDviSr(userInfo.getDvql().substring(0, 6));
        }
        Page<ScQuyetDinhNhapHang> search = scQuyetDinhNhapHangRepository.searchPageViewFromAnother(reqQd, pageable);
        search.getContent().forEach(item -> {
            try {
                List<ScQuyetDinhNhapHangDtl> dtlList = scQuyetDinhNhapHangDtlRepository.findAllByIdHdr(item.getId());
                dtlList.forEach(dtl -> {
                    try {
                        ScDanhSachHdr newDtl = new ScDanhSachHdr();
                        ScDanhSachHdr detail = scDanhSachServiceImpl.detail(dtl.getIdDsHdr());
                        BeanUtils.copyProperties(detail,newDtl);
//                        List<ScPhieuNhapKhoHdr> allByIdScDanhSachHdr = hdrRepository.findAllByIdScDanhSachHdrAndIdQdNh(detail.getId(),dtl.getIdHdr());
                        req.setIdScDanhSachHdr(detail.getId());
                        req.setIdQdNh(dtl.getIdHdr());
                        List<ScPhieuNhapKhoHdr> allByIdScDanhSachHdr = hdrRepository.searchList(req);
                        newDtl.setScPhieuNhapKhoList(allByIdScDanhSachHdr);
                        dtl.setScDanhSachHdr(newDtl);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                item.setChildren(dtlList);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return search;
    }

    @Override
    public List<ScPhieuNhapKhoHdr> searchDanhSachTaoBangKe(ScPhieuNhapKhoReq req) {
        req.setTrangThai(TrangThaiAllEnum.DU_THAO.getId());
        List<ScPhieuNhapKhoHdr> scPhieuXuatKhoHdrs = hdrRepository.searchListTaoBangKe(req);
        return scPhieuXuatKhoHdrs;
    }

    @Override
    public ReportTemplateResponse preview(ScPhieuNhapKhoReq objReq) throws Exception {
        ScPhieuNhapKhoHdr optional = detail(objReq.getId());
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
    }

//    public Page<ScPhieuNhapKhoDTO> searchPage(CustomUserDetails currentUser, ScPhieuNhapKhoReq req) throws Exception {
//        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//        Page<ScPhieuNhapKhoDTO> searchDto = hdrRepository.searchPage(req, pageable);
//
//        return searchDto;
//    }
//
//    public ScPhieuNhapKhoHdr create(ScPhieuNhapKhoReq req) throws Exception {
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null) {
//            throw new Exception("Access denied.");
//        }
//        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
//            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
//        }
//        Optional<ScPhieuNhapKhoHdr> optional = hdrRepository.findBySoPhieuNhapKho(req.getSoPhieuNhapKho());
//        if (optional.isPresent()) {
//            throw new Exception("Số biên bản đã tồn tại");
//        }
//
//        ScPhieuNhapKhoHdr data = new ScPhieuNhapKhoHdr();
//        BeanUtils.copyProperties(req, data);
//        data.setMaDvi(userInfo.getDvql());
//        if (req.getSoPhieuNhapKho() != null) {
//            data.setId(Long.parseLong(req.getSoPhieuNhapKho().split("/")[0]));
//        }
//        req.getChildren().forEach(e -> {
//            e.setParent(data);
//        });
//        ScPhieuNhapKhoHdr created = hdrRepository.save(data);
//        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScPhieuNhapKhoHdr.TABLE_NAME);
//        created.setFileDinhKems(canCu);
//        return created;
//    }
//
//    public ScPhieuNhapKhoHdr update(ScPhieuNhapKhoReq req) throws Exception {
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null) {
//            throw new Exception("Access denied.");
//        }
//        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
//            throw new Exception("Văn bản này chỉ có thêm ở cấp chi cục");
//        }
//        Optional<ScPhieuNhapKhoHdr> optional = hdrRepository.findById(req.getId());
//        if (!optional.isPresent()) {
//            throw new Exception("Số biên bản không tồn tại");
//        }
//        ScPhieuNhapKhoHdr data = optional.get();
//        BeanUtils.copyProperties(req, data);
//        data.setChildren(req.getChildren());
//        ScPhieuNhapKhoHdr update = hdrRepository.save(data);
//        fileDinhKemService.delete(update.getId(), Lists.newArrayList(ScPhieuNhapKhoHdr.TABLE_NAME));
//        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), update.getId(), ScPhieuNhapKhoHdr.TABLE_NAME);
//        update.setFileDinhKems(canCu);
//        return update;
//    }
//
//    public ScPhieuNhapKhoHdr detail(Long id) throws Exception {
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null) {
//            throw new Exception("Access denied.");
//        }
//        if (Objects.isNull(id)) {
//            throw new Exception("Id is null");
//        }
//        Optional<ScPhieuNhapKhoHdr> optional = hdrRepository.findById(id);
//        if (!optional.isPresent()) {
//            throw new Exception("Số biên bản không tồn tại");
//        }
//        ScPhieuNhapKhoHdr data = optional.get();
//        data.setFileDinhKems(fileDinhKemService.search(id, Collections.singleton(ScPhieuNhapKhoHdr.TABLE_NAME)));
//        return data;
//    }
//
//
//    public ScPhieuNhapKhoHdr approve(StatusReq req) throws Exception {
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null) {
//            throw new Exception("Access denied.");
//        }
//        ScPhieuNhapKhoHdr hdr = detail(req.getId());
//        hdr.setTrangThai(req.getTrangThai());
//        ScPhieuNhapKhoHdr created = hdrRepository.save(hdr);
//        return created;
//    }
//
//
//    public void delete(Long id) throws Exception {
//        Optional<ScPhieuNhapKhoHdr> optional = hdrRepository.findById(id);
//        if (!optional.isPresent()) {
//            throw new Exception("Bản ghi không tồn tại");
//        }
//        ScPhieuNhapKhoHdr data = optional.get();
//        List<ScPhieuNhapKhoDtl> list = dtlRepository.findByHdrId(data.getId());
//        dtlRepository.deleteAll(list);
//        hdrRepository.delete(data);
//    }
//
//
//    public void deleteMulti(List<Long> listMulti) throws Exception {
//        if (listMulti != null && !listMulti.isEmpty()) {
//            listMulti.forEach(i -> {
//                try {
//                    delete(i);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            });
//        } else {
//            throw new Exception("List id is null");
//        }
//    }


}
