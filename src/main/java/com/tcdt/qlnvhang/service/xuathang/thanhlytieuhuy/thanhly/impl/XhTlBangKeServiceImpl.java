package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.impl;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.*;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.*;
import com.tcdt.qlnvhang.request.suachua.ScBangKeNhapVtReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhNhapHangReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlBangKeReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlQdGiaoNvHdrReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.suachuahang.ScBangKeNhapVtService;
import com.tcdt.qlnvhang.service.suachuahang.ScPhieuNhapKhoService;
import com.tcdt.qlnvhang.service.suachuahang.impl.ScDanhSachServiceImpl;
import com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.XhTlBangKeService;
import com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachService;
import com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.XhTlPhieuXuatKhoService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
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
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhTlBangKeServiceImpl extends BaseServiceImpl implements XhTlBangKeService {
    @Autowired
    private XhTlBangKeHdrRepository hdrRepository;

    @Autowired
    private XhTlBangKeDtlRepository dtlRepository;
    
    @Autowired
    private XhTlPhieuXuatKhoHdrRepository xhTlPhieuXuatKhoHdrRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private XhTlQdGiaoNvHdrRepository xhTlQdGiaoNvHdrRepository;


    @Autowired
    private XhTlQdGiaoNvDtlRepository xhTlQdGiaoNvDtlRepository;

    @Autowired
    private XhTlPhieuXuatKhoService xhTlPhieuXuatKhoService;


    @Autowired
    private XhTlDanhSachService xhTlDanhSachService;

    @Override
    public Page<XhTlBangKeHdr> searchPage(XhTlBangKeReq req) throws Exception {
        return null;
    }

    private void updatePhieuXuatKho(XhTlBangKeHdr data,Long idPhieuNhapKho,Long idPhieuNhapKhoOld){
        if(!Objects.isNull(idPhieuNhapKhoOld) && !idPhieuNhapKho.equals(idPhieuNhapKhoOld)){
            Optional<XhTlPhieuXuatKhoHdr> byId = xhTlPhieuXuatKhoHdrRepository.findById(idPhieuNhapKhoOld);
            if(byId.isPresent()){
                byId.get().setIdBangKeCanHang(null);
                byId.get().setSoBangKeCanHang(null);
                xhTlPhieuXuatKhoHdrRepository.save(byId.get());
            }
        }else{
            Optional<XhTlPhieuXuatKhoHdr> byId = xhTlPhieuXuatKhoHdrRepository.findById(idPhieuNhapKho);
            if(byId.isPresent()){
                byId.get().setIdBangKeCanHang(data.getId());
                byId.get().setSoBangKeCanHang(data.getSoBangKe());
                xhTlPhieuXuatKhoHdrRepository.save(byId.get());
            }
        }
    }

    public List<XhTlBangKeDtl> saveDtl(XhTlBangKeReq req, Long idHdr){
        dtlRepository.deleteAllByIdHdr(idHdr);
        req.getChildren().forEach(item -> {
            item.setId(null);
            item.setIdHdr(idHdr);
            dtlRepository.save(item);
        });
        return req.getChildren();
    }

    @Override
    public XhTlBangKeHdr create(XhTlBangKeReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        String dvql = userInfo.getDvql();
        XhTlBangKeHdr data = new XhTlBangKeHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(dvql);
        data.setIdThuKho(userInfo.getId());
        data.setId(Long.parseLong(req.getSoBangKe().split("/")[0]));
        XhTlBangKeHdr created = hdrRepository.save(data);
        List<XhTlBangKeDtl> ScBangKeNhapVtDtls = saveDtl(req, created.getId());
        created.setChildren(ScBangKeNhapVtDtls);
        this.updatePhieuXuatKho(data,data.getIdPhieuXuatKho(),null);
        return created;
    }

    @Override
    public XhTlBangKeHdr update(XhTlBangKeReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<XhTlBangKeHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        XhTlBangKeHdr data = optional.get();
        this.updatePhieuXuatKho(data,req.getIdPhieuXuatKho(),data.getIdPhieuXuatKho());
        BeanUtils.copyProperties(req, data);
        XhTlBangKeHdr created = hdrRepository.save(data);
        List<XhTlBangKeDtl> ScBangKeNhapVtDtls = saveDtl(req, created.getId());
        created.setChildren(ScBangKeNhapVtDtls);
        return created;
    }

    @Override
    public XhTlBangKeHdr detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<XhTlBangKeHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        XhTlBangKeHdr data = optional.get();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        data.setChildren(dtlRepository.findByIdHdr(id));
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
        data.setMapDmucDvi(mapDmucDvi);
        data.setMapVthh(mapVthh);
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
        if(!Objects.isNull(data.getIdPhieuXuatKho())){
            data.setXhTlPhieuXuatKhoHdr(xhTlPhieuXuatKhoService.detail(data.getIdPhieuXuatKho()));
        }
        if(!Objects.isNull(data.getIdThuKho())){
            data.setTenThuKho(userInfoRepository.findById(data.getIdThuKho()).get().getFullName());
        }
        if(!Objects.isNull(data.getIdLanhDaoCc())){
            data.setTenLanhDaoCc(userInfoRepository.findById(data.getIdLanhDaoCc()).get().getFullName());
        }
        return data;
    }

    @Override
    public XhTlBangKeHdr approve(XhTlBangKeReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<XhTlBangKeHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        if(!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
            throw new Exception("Đơn vị gửi duyệt phải là cấp cục");
        }
        XhTlBangKeHdr hdr = optional.get();
        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            // Re approve : gửi lại duyệt
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                break;
            // Arena các cấp duuyệt
            case Contains.DUTHAO + Contains.CHODUYET_LDCC:
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                hdr.setIdLanhDaoCc(userInfo.getId());
                break;
            // Arena từ chối
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                hdr.setIdLanhDaoCc(userInfo.getId());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        XhTlBangKeHdr save = hdrRepository.save(hdr);
        return save;
    }

    @Override
    public void delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<XhTlBangKeHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        hdrRepository.delete(optional.get());
        dtlRepository.deleteAllByIdHdr(optional.get().getId());
        Optional<XhTlPhieuXuatKhoHdr> byId = xhTlPhieuXuatKhoHdrRepository.findById(optional.get().getIdPhieuXuatKho());
        if(byId.isPresent()){
            byId.get().setIdBangKeCanHang(null);
            byId.get().setSoBangKeCanHang(null);
            xhTlPhieuXuatKhoHdrRepository.save(byId.get());
        }
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(XhTlBangKeReq req, HttpServletResponse response) throws Exception {

    }

    @Override
    public Page<XhTlQdGiaoNvHdr> searchBangKeCanHang(XhTlBangKeReq req) throws Exception {
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
//                List<XhTlQdGiaoNvDtl> byIdHdr = xhTlQdGiaoNvDtlRepository.findAllByIdHdrAndPhanLoai(item.getId(),req.getPhanLoai());
//                byIdHdr.forEach( x -> {
//                    try {
//                        XhTlDanhSachHdr dsHdr = xhTlDanhSachService.detail(x.getIdDsHdr());
//                        Optional<XhTlKtraClHdr> byIdDsHdr = xhTlKtraClHdrRepository.findByIdDsHdr(x.getIdDsHdr());
//                        byIdDsHdr.ifPresent(dsHdr::setXhTlKtraClHdr);
//                        req.setIdDsHdr(x.getIdDsHdr());
//                        req.setIdQdXh(item.getId());
//                        List<XhTlPhieuXuatKhoHdr> allByIdQdXhAndIdDsHdr = hdrRepository.findAllByIdQdXhAndIdDsHdr(req);
//                        dsHdr.setListXhTlPhieuXuatKhoHdr(allByIdQdXhAndIdDsHdr);
//                        x.setXhTlDanhSachHdr(dsHdr);
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//                item.setChildren(byIdHdr);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return search;
    }

//    @Override
//    public Page<ScQuyetDinhNhapHang> searchBangKeNhapVt(ScBangKeNhapVtReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//        ScQuyetDinhNhapHangReq reqQd = new ScQuyetDinhNhapHangReq();
//        reqQd.setNam(req.getNam());
//        reqQd.setSoQd(req.getSoQdNh());
//        reqQd.setTrangThai(TrangThaiAllEnum.BAN_HANH.getId());
//        reqQd.setThoiHanNhapTu(req.getNgayTuNh());
//        reqQd.setThoiHanNhapDen(req.getNgayDenNh());
//        if(userInfo.getCapDvi().equals(Contains.CAP_CUC)){
//            reqQd.setMaDviSr(userInfo.getDvql());
//        }
//        if(userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
//            reqQd.setMaDviSr(userInfo.getDvql().substring(0, 6));
//        }
//        Page<ScQuyetDinhNhapHang> search = scQuyetDinhNhapHangRepository.searchPageViewFromAnother(reqQd, pageable);
//        search.getContent().forEach(item -> {
//            try {
//                List<ScQuyetDinhNhapHangDtl> dtlList = scQuyetDinhNhapHangDtlRepository.findAllByIdHdr(item.getId());
//                dtlList.forEach(dtl -> {
//                    try {
//                        ScDanhSachHdr newDtl = new ScDanhSachHdr();
//                        ScDanhSachHdr detail = scDanhSachServiceImpl.detail(dtl.getIdDsHdr());
//                        BeanUtils.copyProperties(detail,newDtl);
//                        List<ScPhieuNhapKhoHdr> allByIdScDanhSachHdr = scPhieuNhapKhoHdrRepository.findAllByIdScDanhSachHdrAndIdQdNh(detail.getId(),dtl.getIdHdr());
//                        if(allByIdScDanhSachHdr != null && !allByIdScDanhSachHdr.isEmpty()){
//                            List<Long> ids = allByIdScDanhSachHdr.stream().map(ScPhieuNhapKhoHdr::getId).collect(Collectors.toList());
//                            req.setIdPhieuNhapKhoList(ids);
//                            List<ScBangKeNhapVtHdr> listBk = hdrRepository.searchList(req);
//                            newDtl.setScBangKeNhapVtList(listBk);
//                        }
//                        dtl.setScDanhSachHdr(newDtl);
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//                item.setChildren(dtlList);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//        return search;
//    }
//
//    @Override
//    public ReportTemplateResponse preview(ScBangKeNhapVtReq objReq) throws Exception {
//        ScBangKeNhapVtHdr optional = detail(objReq.getId());
//        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
//        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
//        return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
//    }


}
