package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.*;
import com.tcdt.qlnvhang.request.suachua.ScKiemTraChatLuongReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.suachuahang.ScKiemTraChatLuongService;
import com.tcdt.qlnvhang.service.suachuahang.ScPhieuXuatKhoService;
import com.tcdt.qlnvhang.service.suachuahang.ScQuyetDinhScService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.*;
import com.tcdt.qlnvhang.util.Contains;
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
import java.util.stream.Collectors;

@Service
public class ScKiemTraChatLuongImpl extends BaseServiceImpl implements ScKiemTraChatLuongService {
    @Autowired
    private ScKiemTraChatLuongHdrRepository hdrRepository;
    @Autowired
    private ScKiemTraChatLuongDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ScQuyetDinhXuatHangRepository scQuyetDinhXuatHangRepository;

    @Autowired
    private ScQuyetDinhScService scQuyetDinhScService;

    @Autowired
    private ScPhieuXuatKhoHdrRepository scPhieuXuatKhoHdrRepository;

    @Autowired
    private ScQuyetDinhNhapHangRepository scQuyetDinhNhapHangRepository;
    @Autowired
    private ScPhieuXuatKhoService scPhieuXuatKhoService;

    @Override
    public Page<ScKiemTraChatLuongHdr> searchPage(ScKiemTraChatLuongReq req) throws Exception {
        return null;
    }

    @Override
    public ScKiemTraChatLuongHdr create(ScKiemTraChatLuongReq req) throws Exception {
        UserInfo currentUser = SecurityContextService.getUser();
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!currentUser.getCapDvi().equals(Contains.CAP_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        ScKiemTraChatLuongHdr data = new ScKiemTraChatLuongHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setId(Long.parseLong(req.getSoPhieuKtcl().split("/")[0]));
        ScKiemTraChatLuongHdr created = hdrRepository.save(data);
        saveFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScPhieuXuatKhoHdr.TABLE_NAME);
        List<ScKiemTraChatLuongDtl> dtls = saveDtl(req, data.getId());
        created.setChildren(dtls);
        return created;
    }

    public List<ScKiemTraChatLuongDtl> saveDtl(ScKiemTraChatLuongReq req, Long idHdr){
        dtlRepository.deleteAllByIdHdr(idHdr);
        req.getChildren().forEach(item -> {
            item.setId(null);
            item.setIdHdr(idHdr);
            dtlRepository.save(item);
        });
        return req.getChildren();
    }

    @Override
    public ScKiemTraChatLuongHdr update(ScKiemTraChatLuongReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<ScKiemTraChatLuongHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        ScKiemTraChatLuongHdr data = optional.get();
        BeanUtils.copyProperties(req, data);
        fileDinhKemService.delete(data.getId(), Collections.singleton(ScPhieuXuatKhoHdr.TABLE_NAME));
        saveFileDinhKem(req.getFileDinhKemReq(), data.getId(), ScPhieuXuatKhoHdr.TABLE_NAME);
        List<ScKiemTraChatLuongDtl> dtls = saveDtl(req, data.getId());
        data.setChildren(dtls);
        return data;
    }

    @Override
    public ScKiemTraChatLuongHdr detail(Long id) throws Exception {
        Optional<ScKiemTraChatLuongHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        ScKiemTraChatLuongHdr data = optional.get();
        data.setFileDinhKem(fileDinhKemService.search(id, Collections.singleton(ScPhieuXuatKhoHdr.TABLE_NAME)));
        data.setChildren(dtlRepository.findAllByIdHdrOrderByThuTuHt(id));
        if(!Objects.isNull(data.getIdPhieuXuatKho())){
            data.setScPhieuXuatKhoHdr(scPhieuXuatKhoService.detail(data.getIdPhieuXuatKho()));
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
        if(!Objects.isNull(data.getNguoiTaoId())){
            data.setTenNguoiTao(userInfoRepository.findById(data.getNguoiTaoId()).get().getFullName());
        }
        if(!Objects.isNull(data.getIdTruongPhongKtvq())){
            data.setTenTruongPhongKtbq(userInfoRepository.findById(data.getIdTruongPhongKtvq()).get().getFullName());
        }
        return data;
    }

    @Override
    public ScKiemTraChatLuongHdr approve(ScKiemTraChatLuongReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<ScKiemTraChatLuongHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScKiemTraChatLuongHdr hdr = optional.get();
        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            // Re approve : gửi lại duyệt
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_TP:
                break;
            // Arena các cấp duuyệt
            case Contains.DUTHAO + Contains.CHODUYET_TP:
                if(!userInfo.getCapDvi().equals(Contains.CAP_CUC)){
                    throw new Exception("Đơn vị gửi duyệt phải là cấp cục");
                }
                hdr.setIdTruongPhongKtvq(userInfo.getId());
                break;
            case Contains.CHODUYET_TP + Contains.CHODUYET_LDC:
                if(!userInfo.getCapDvi().equals(Contains.CAP_CUC)){
                    throw new Exception("Đơn vị gửi duyệt phải là cấp cục");
                }
                break;
            case Contains.CHODUYET_LDC + Contains.DADUYET_LDC:
                if(!userInfo.getCapDvi().equals(Contains.CAP_CUC)){
                    throw new Exception("Đơn vị gửi duyệt phải là cấp cục");
                }
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
        ScKiemTraChatLuongHdr save = hdrRepository.save(hdr);
        return save;
    }

    @Override
    public void delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<ScKiemTraChatLuongHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        hdrRepository.delete(optional.get());
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(ScPhieuXuatKhoHdr.TABLE_NAME));
        dtlRepository.deleteAllByIdHdr(optional.get().getId());
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(ScKiemTraChatLuongReq req, HttpServletResponse response) throws Exception {

    }

    @Override
    public Page<ScQuyetDinhXuatHang> searchKiemTraChatLuong(ScKiemTraChatLuongReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        ScQuyetDinhXuatHangReq reqQd = new ScQuyetDinhXuatHangReq();
        reqQd.setNam(req.getNam());
        reqQd.setSoQd(req.getSoQdXh());
        reqQd.setTrangThai(TrangThaiAllEnum.BAN_HANH.getId());
        if(userInfo.getCapDvi().equals(Contains.CAP_CUC)){
            reqQd.setMaDviSr(userInfo.getDvql());
        }
        if(userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
            reqQd.setMaDviSr(userInfo.getDvql().substring(0, 6));
        }
        Page<ScQuyetDinhXuatHang> search = scQuyetDinhXuatHangRepository.searchPageViewFromPhieuXuatKho(reqQd, pageable);
        search.getContent().forEach(item -> {
            try {
                List<ScDanhSachHdr> scDanhSachHdrList = new ArrayList<>();
                ScQuyetDinhSc scQuyetDinhSc = scQuyetDinhScService.detail(item.getIdQdSc());
                // lấy Danh sách sửa chữa hdr
                scQuyetDinhSc.getScTrinhThamDinhHdr().getChildren().forEach(( dsHdr)->{
                    ScDanhSachHdr scDanhSachHdr = dsHdr.getScDanhSachHdr();
                    // Từ danh sách lấy ra phiếu xuất kho
                    ScPhieuXuatKhoReq scPhieuXuatKhoReq = new ScPhieuXuatKhoReq();
                    scPhieuXuatKhoReq.setNam(req.getNam());
                    scPhieuXuatKhoReq.setIdScDanhSachHdr(scDanhSachHdr.getId());
                    if(userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
                        scPhieuXuatKhoReq.setMaDviSr(userInfo.getDvql());
                    }
                    List<ScPhieuXuatKhoHdr> scPhieuXuatKhoHdrs = scPhieuXuatKhoHdrRepository.searchList(scPhieuXuatKhoReq);
                    scDanhSachHdr.setScPhieuXuatKhoList(scPhieuXuatKhoHdrs);

                    // Từ phiếu xuất kho -> lấy ra phiếu kiểm nghiệm chất lượng sau sc
                    // get idlist phiếu xuất kho
                    List<Long> listIdPxk = scPhieuXuatKhoHdrs.stream().map(ScPhieuXuatKhoHdr::getId).collect(Collectors.toList());
                    req.setIds(listIdPxk);
                    List<ScKiemTraChatLuongHdr> allByIdPhieuXuatKhoIn = hdrRepository.findAllByIdPhieuXuatKho(req);
                    scDanhSachHdr.setScKiemTraChatLuongList(allByIdPhieuXuatKhoIn);
                    scDanhSachHdrList.add(scDanhSachHdr);
                });
                item.setScQuyetDinhSc(scQuyetDinhScService.detail(item.getIdQdSc()));
                item.setScDanhSachHdrList(scDanhSachHdrList);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return search;
    }

    @Override
    public List<ScKiemTraChatLuongHdr> searchDanhSachTaoQuyetDinhNhapHang(ScKiemTraChatLuongReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        req.setTrangThai(TrangThaiAllEnum.DA_DUYET_LDC.getId());
        req.setMaDviSr(userInfo.getDvql());
        List<ScKiemTraChatLuongHdr> scPhieuXuatKhoHdrs = hdrRepository.searchListTaoQuyetDinhNhapHang(req);

        List<ScQuyetDinhNhapHang> allByIdQdXh = scQuyetDinhNhapHangRepository.findAllByIdQdXh(req.getIdQdXh());

        List<Long> listIdUsed = new ArrayList<>();
        allByIdQdXh.forEach( item ->{
            String[] split = item.getIdPhieuKtcl().split(",");
            // Check danh sách kiểm tra chất lượng đã sử dụng và check nếu trùng id với request thì không thêm vào danh sách đã sử dụng
            if(req.getId() == null || !req.getId().equals(item.getId())){
                for (String s : split) {
                    listIdUsed.add(Long.parseLong(s));
                }
            }
        });

        List<ScKiemTraChatLuongHdr> collect = scPhieuXuatKhoHdrs.stream().filter(item -> !listIdUsed.contains(item.getId())).collect(Collectors.toList());
        collect.forEach(item -> {
            try {
                item.setScPhieuXuatKhoHdr(scPhieuXuatKhoService.detail(item.getIdPhieuXuatKho()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return collect;
    }

    @Override
    public ReportTemplateResponse preview(ScKiemTraChatLuongReq objReq) throws Exception {
        ScKiemTraChatLuongHdr optional = detail(objReq.getId());
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
    }

}
