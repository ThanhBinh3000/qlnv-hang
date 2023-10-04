package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.impl;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.*;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.*;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlPhieuXuatKhoReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlQdGiaoNvHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.suachuahang.ScQuyetDinhScService;
import com.tcdt.qlnvhang.service.suachuahang.impl.ScDanhSachServiceImpl;
import com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachService;
import com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.XhTlPhieuXuatKhoService;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.*;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class XhTlPhieuXuatKhoServiceImpl extends BaseServiceImpl implements XhTlPhieuXuatKhoService {

    @Autowired
    private XhTlPhieuXuatKhoHdrRepository hdrRepository;

    @Autowired
    private XhTlPhieuXuatKhoDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private XhTlBangKeHdrRepository xhTlBangKeHdrRepository;

    @Autowired
    private XhTlQdGiaoNvHdrRepository xhTlQdGiaoNvHdrRepository;

    @Autowired
    private XhTlQdGiaoNvDtlRepository xhTlQdGiaoNvDtlRepository;

    @Autowired
    private XhTlDanhSachService xhTlDanhSachService;
    @Autowired
    private XhTlKtraClHdrRepository xhTlKtraClHdrRepository;


    @Override
    public Page<XhTlPhieuXuatKhoHdr> searchPage(XhTlPhieuXuatKhoReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhTlPhieuXuatKhoHdr> searchPage = hdrRepository.searchPage(req, pageable);
        return searchPage;
    }

    @Override
    public XhTlPhieuXuatKhoHdr create(XhTlPhieuXuatKhoReq req) throws Exception {
        UserInfo currentUser = SecurityContextService.getUser();
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!currentUser.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        XhTlPhieuXuatKhoHdr data = new XhTlPhieuXuatKhoHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setId(Long.parseLong(req.getSoPhieuXuatKho().split("/")[0]));
        data.setIdThuKho(currentUser.getId());
        XhTlPhieuXuatKhoHdr created = hdrRepository.save(data);
        saveFileDinhKem(req.getFileDinhKemReq(), created.getId(), XhTlPhieuXuatKhoHdr.TABLE_NAME);
        List<XhTlPhieuXuatKhoDtl> scPhieuXuatKhoDtls = saveDtl(req, data.getId());
        created.setChildren(scPhieuXuatKhoDtls);
        return created;
    }

    public List<XhTlPhieuXuatKhoDtl> saveDtl(XhTlPhieuXuatKhoReq req, Long idHdr){
        dtlRepository.deleteAllByIdHdr(idHdr);
        req.getChildren().forEach(item -> {
            item.setId(null);
            item.setIdHdr(idHdr);
            dtlRepository.save(item);
        });
        return req.getChildren();
    }

    @Override
    public XhTlPhieuXuatKhoHdr update(XhTlPhieuXuatKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<XhTlPhieuXuatKhoHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhTlPhieuXuatKhoHdr data = optional.get();
        BeanUtils.copyProperties(req, data);
        fileDinhKemService.delete(data.getId(), Collections.singleton(XhTlPhieuXuatKhoHdr.TABLE_NAME));
        saveFileDinhKem(req.getFileDinhKemReq(), data.getId(), XhTlPhieuXuatKhoHdr.TABLE_NAME);
        List<XhTlPhieuXuatKhoDtl> scPhieuXuatKhoDtls = saveDtl(req, data.getId());
        data.setChildren(scPhieuXuatKhoDtls);
        return data;
    }

    @Override
    public XhTlPhieuXuatKhoHdr detail(Long id) throws Exception {
        Optional<XhTlPhieuXuatKhoHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhTlPhieuXuatKhoHdr data = optional.get();
        data.setFileDinhKem(fileDinhKemService.search(id, Collections.singleton(XhTlPhieuXuatKhoHdr.TABLE_NAME)));
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
        if(!Objects.isNull(data.getIdKtv())){
            data.setTenKtv(userInfoRepository.findById(data.getIdKtv()).get().getFullName());
        }
        if(!Objects.isNull(data.getIdLanhDaoCc())){
            data.setTenLanhDaoCc(userInfoRepository.findById(data.getIdLanhDaoCc()).get().getFullName());
        }
//        if(data.getIdScDanhSachHdr() != null){
//            ScDanhSachHdr byId = scDanhSachServiceImpl.detail(data.getIdScDanhSachHdr());
//            data.setScDanhSachHdr(byId);
//        }
        return data;
    }

    @Override
    public XhTlPhieuXuatKhoHdr approve(XhTlPhieuXuatKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<XhTlPhieuXuatKhoHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        XhTlPhieuXuatKhoHdr hdr = optional.get();

        if(Objects.isNull(hdr.getIdBangKeCanHang())){
            throw new Exception("Phiếu xuất kho đang chưa khởi tạo bảng kê cân hàng. Vui lòng tạo bảng kê cân hàng");
        } else {
            Optional<XhTlBangKeHdr> bkOp = xhTlBangKeHdrRepository.findById(hdr.getIdBangKeCanHang());
            if(bkOp.isPresent()){
                XhTlBangKeHdr bk = bkOp.get();
                if(!bk.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDCC.getId())){
                    throw new Exception("Số bảng kê " +bk.getSoBangKe()+" chưa đc phê duyệt. Vui lòng phê duyệt bảng kê");
                }
            }else{
                throw new Exception("Phiếu xuất kho không tìm thấy bảng kê cân hàng. Vui lòng tạo bảng kê cân hàng");
            }
        }

        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            // Re approve : gửi lại duyệt
            case Contains.TUCHOI_LDCC + Contains.DUTHAO:
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
        XhTlPhieuXuatKhoHdr save = hdrRepository.save(hdr);
        return save;
    }

    @Override
    public void delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<XhTlPhieuXuatKhoHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        if(!Objects.isNull(optional.get().getIdBangKeCanHang())){
            throw new Exception("Phiếu xuất kho "+optional.get().getSoPhieuXuatKho()+" đã tạo số bảng kê cân hàng. Không thể xóa");
        }
        hdrRepository.delete(optional.get());
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(ScPhieuXuatKhoHdr.TABLE_NAME));
        dtlRepository.deleteAllByIdHdr(optional.get().getId());
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(XhTlPhieuXuatKhoReq req, HttpServletResponse response) throws Exception {

    }

    @Override
    public List<XhTlPhieuXuatKhoHdr> searchDanhSachTaoBangKe(XhTlPhieuXuatKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        req.setMaDviSr(userInfo.getDvql());
        List<XhTlPhieuXuatKhoHdr> scPhieuXuatKhoHdrs = hdrRepository.searchListTaoBangKe(req);
        return scPhieuXuatKhoHdrs;
    }

    @Override
    public Page<XhTlQdGiaoNvHdr> searchPhieuXuatKho(XhTlPhieuXuatKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        XhTlQdGiaoNvHdrReq reqQd = new XhTlQdGiaoNvHdrReq();
        reqQd.setNam(req.getNam());
        reqQd.setTrangThai(TrangThaiAllEnum.BAN_HANH.getId());
        reqQd.setPhanLoai(req.getPhanLoai());
        if(userInfo.getCapDvi().equals(Contains.CAP_CUC)){
            reqQd.setMaDviSr(userInfo.getDvql());
            req.setMaDviSr(userInfo.getDvql());
        }
        if(userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
            reqQd.setMaDviSr(userInfo.getDvql().substring(0, 6));
            req.setMaDviSr(userInfo.getDvql().substring(0, 6));
        }
        Page<XhTlQdGiaoNvHdr> search = xhTlQdGiaoNvHdrRepository.searchPageViewFromAnother(reqQd, pageable);
        search.getContent().forEach(item -> {
            try {
                // Lấy toàn bộ địa điểm kho trong dtl
                List<XhTlQdGiaoNvDtl> byIdHdr = xhTlQdGiaoNvDtlRepository.findAllByIdHdrAndPhanLoai(item.getId(),req.getPhanLoai());
                byIdHdr.forEach( x -> {
                    try {
                        // Lấy danh sách gốc
                        XhTlDanhSachHdr dsHdr = xhTlDanhSachService.detail(x.getIdDsHdr());
                        Optional<XhTlKtraClHdr> byIdDsHdr = xhTlKtraClHdrRepository.findByIdDsHdr(x.getIdDsHdr());
                        byIdDsHdr.ifPresent(dsHdr::setXhTlKtraClHdr);
                        req.setIdDsHdr(x.getIdDsHdr());
                        req.setIdQdXh(item.getId());
                        // Lấy toàn bộ pxk theo địa điểm danh sách
                        List<XhTlPhieuXuatKhoHdr> allByIdQdXhAndIdDsHdr = hdrRepository.findAllByIdQdXhAndIdDsHdr(req);
                        allByIdQdXhAndIdDsHdr.forEach( pxk -> {
                            // Lấy toàn bộ biên bản cân hàng theo địa điểm danh sách
                            if(!Objects.isNull(pxk.getIdBangKeCanHang())){
                                Optional<XhTlBangKeHdr> byId = xhTlBangKeHdrRepository.findById(pxk.getIdBangKeCanHang());
                                byId.ifPresent(pxk::setXhTlBangKeHdr);
                            }
                        });
                        dsHdr.setListXhTlPhieuXuatKhoHdr(allByIdQdXhAndIdDsHdr);
                        x.setXhTlDanhSachHdr(dsHdr);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                item.setChildren(byIdHdr);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return search;
    }

//    @Override
//    public Page<ScQuyetDinhXuatHang> searchPhieuXuatKho(XhTlPhieuXuatKhoReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//        ScQuyetDinhXuatHangReq reqQd = new ScQuyetDinhXuatHangReq();
//        reqQd.setNam(req.getNam());
//        reqQd.setSoQd(req.getSoQdXh());
//        reqQd.setTrangThai(TrangThaiAllEnum.BAN_HANH.getId());
//        if(userInfo.getCapDvi().equals(Contains.CAP_CUC)){
//            reqQd.setMaDviSr(userInfo.getDvql());
//        }
//        if(userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
//            reqQd.setMaDviSr(userInfo.getDvql().substring(0, 6));
//        }
//        Page<ScQuyetDinhXuatHang> search = scQuyetDinhXuatHangRepository.searchPageViewFromPhieuXuatKho(reqQd, pageable);
//        search.getContent().forEach(item -> {
//            try {
//                List<ScDanhSachHdr> scDanhSachHdrList = new ArrayList<>();
//                ScQuyetDinhSc scQuyetDinhSc = scQuyetDinhScService.detail(item.getIdQdSc());
//                // lấy Danh sách sửa chữa hdr
//                scQuyetDinhSc.getScTrinhThamDinhHdr().getChildren().forEach(( dsHdr)->{
//                    ScDanhSachHdr scDanhSachHdr = dsHdr.getScDanhSachHdr();
//                    req.setIdScDanhSachHdr(scDanhSachHdr.getId());
//                    if(userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
//                        req.setMaDviSr(userInfo.getDvql());
//                    }
//                    // Lấy phiếu xuất kho theo từng danh sách sửa chữa ( địa điểm )
//                    List<ScPhieuXuatKhoHdr> scPhieuXuatKhoHdrs = hdrRepository.searchList(req);
//                    scDanhSachHdr.setScPhieuXuatKhoList(scPhieuXuatKhoHdrs);
//                    scDanhSachHdrList.add(scDanhSachHdr);
//                });
//                item.setScQuyetDinhSc(scQuyetDinhScService.detail(item.getIdQdSc()));
//                item.setScDanhSachHdrList(scDanhSachHdrList);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//        return search;
//    }

//    @Override
//    public List<ScPhieuXuatKhoHdr> searchDanhSachTaoBangKe(XhTlPhieuXuatKhoReq req) {
//        req.setTrangThai(TrangThaiAllEnum.DU_THAO.getId());
//        List<ScPhieuXuatKhoHdr> scPhieuXuatKhoHdrs = hdrRepository.searchListTaoBangKe(req);
//        return scPhieuXuatKhoHdrs;
//    }
//
//    @Override
//    public List<ScPhieuXuatKhoHdr> searchDanhSachTaoKiemTraCl(XhTlPhieuXuatKhoReq req) {
//        req.setTrangThai(TrangThaiAllEnum.DA_DUYET_LDCC.getId());
//        List<ScPhieuXuatKhoHdr> scPhieuXuatKhoHdrs = hdrRepository.searchListTaoKiemTraCl(req);
//        return scPhieuXuatKhoHdrs;
//    }
//
//    @Override
//    public ReportTemplateResponse preview(XhTlPhieuXuatKhoReq objReq) throws Exception {
//        ScPhieuXuatKhoHdr optional = detail(objReq.getId());
//        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
//        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
//        return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
//    }


}
