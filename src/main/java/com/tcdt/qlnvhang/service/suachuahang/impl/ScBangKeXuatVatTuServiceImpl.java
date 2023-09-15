package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.*;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.suachua.ScBangKeXuatVatTuReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeNhapVTHdrDTO;
import com.tcdt.qlnvhang.response.suachua.ScBangKeXuatVtDTO;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.suachuahang.ScBangKeXuatVatTuService;
import com.tcdt.qlnvhang.service.suachuahang.ScPhieuXuatKhoService;
import com.tcdt.qlnvhang.service.suachuahang.ScQuyetDinhScService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.UserUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScBangKeXuatVatTuServiceImpl extends BaseServiceImpl implements ScBangKeXuatVatTuService {
    @Autowired
    private ScBangKeXuatVatTuHdrRepository hdrRepository;

    @Autowired
    private ScBangKeXuatVatTuDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private ScQuyetDinhXuatHangRepository scQuyetDinhXuatHangRepository;

    @Autowired
    private ScQuyetDinhScService scQuyetDinhScService;

    @Autowired
    private ScPhieuXuatKhoHdrRepository scPhieuXuatKhoHdrRepository;

    @Autowired
    private ScPhieuXuatKhoService scPhieuXuatKhoService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public Page<ScBangKeXuatVatTuHdr> searchPage(ScBangKeXuatVatTuReq req) throws Exception {
        return null;
    }

    @Override
    public ScBangKeXuatVatTuHdr create(ScBangKeXuatVatTuReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        String dvql = userInfo.getDvql();
        ScBangKeXuatVatTuHdr data = new ScBangKeXuatVatTuHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(dvql);
        data.setIdThuKho(userInfo.getId());
        data.setId(Long.parseLong(req.getSoBangKe().split("/")[0]));
        ScBangKeXuatVatTuHdr created = hdrRepository.save(data);
        List<ScBangKeXuatVatTuDtl> scBangKeXuatVatTuDtls = saveDtl(req, created.getId());
        created.setChildren(scBangKeXuatVatTuDtls);
        this.updatePhieuXuatKho(data,data.getIdPhieuXuatKho(),null);
        return created;
    }

    private void updatePhieuXuatKho(ScBangKeXuatVatTuHdr data,Long idPhieuXuatKho,Long idPhieuXuatKhoOld){
        if(!Objects.isNull(idPhieuXuatKhoOld) && !idPhieuXuatKho.equals(idPhieuXuatKhoOld)){
            Optional<ScPhieuXuatKhoHdr> byId = scPhieuXuatKhoHdrRepository.findById(idPhieuXuatKhoOld);
            if(byId.isPresent()){
                byId.get().setIdBangKeCanHang(null);
                byId.get().setSoBangKeCanHang(null);
                scPhieuXuatKhoHdrRepository.save(byId.get());
            }
        }else{
            Optional<ScPhieuXuatKhoHdr> byId = scPhieuXuatKhoHdrRepository.findById(idPhieuXuatKho);
            if(byId.isPresent()){
                byId.get().setIdBangKeCanHang(data.getId());
                byId.get().setSoBangKeCanHang(data.getSoBangKe());
                scPhieuXuatKhoHdrRepository.save(byId.get());
            }
        }
    }

    public List<ScBangKeXuatVatTuDtl> saveDtl(ScBangKeXuatVatTuReq req, Long idHdr){
        dtlRepository.deleteAllByIdHdr(idHdr);
        req.getChildren().forEach(item -> {
            item.setId(null);
            item.setIdHdr(idHdr);
            dtlRepository.save(item);
        });
        return req.getChildren();
    }



    @Override
    public ScBangKeXuatVatTuHdr update(ScBangKeXuatVatTuReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<ScBangKeXuatVatTuHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        ScBangKeXuatVatTuHdr data = optional.get();
        this.updatePhieuXuatKho(data,req.getIdPhieuXuatKho(),data.getIdPhieuXuatKho());
        BeanUtils.copyProperties(req, data);
        ScBangKeXuatVatTuHdr created = hdrRepository.save(data);
        List<ScBangKeXuatVatTuDtl> scBangKeXuatVatTuDtls = saveDtl(req, created.getId());
        created.setChildren(scBangKeXuatVatTuDtls);
        return created;
    }

    @Override
    public ScBangKeXuatVatTuHdr detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<ScBangKeXuatVatTuHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScBangKeXuatVatTuHdr data = optional.get();
        if(!Objects.isNull(data.getIdPhieuXuatKho())){
            data.setScPhieuXuatKho(scPhieuXuatKhoService.detail(data.getIdPhieuXuatKho()));
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        data.setChildren(dtlRepository.findByIdHdr(id));
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
        if(!Objects.isNull(data.getIdThuKho())){
            data.setTenThuKho(userInfoRepository.findById(data.getIdThuKho()).get().getFullName());
        }
        if(!Objects.isNull(data.getIdLanhDaoCc())){
            data.setTenLanhDaoCc(userInfoRepository.findById(data.getIdLanhDaoCc()).get().getFullName());
        }
        return data;
    }

    @Override
    public ScBangKeXuatVatTuHdr approve(ScBangKeXuatVatTuReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<ScBangKeXuatVatTuHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScBangKeXuatVatTuHdr hdr = optional.get();
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
        ScBangKeXuatVatTuHdr save = hdrRepository.save(hdr);
        return save;
    }

    @Override
    public void delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<ScBangKeXuatVatTuHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        hdrRepository.delete(optional.get());
        dtlRepository.deleteAllByIdHdr(optional.get().getId());
        Optional<ScPhieuXuatKhoHdr> byId = scPhieuXuatKhoHdrRepository.findById(optional.get().getIdPhieuXuatKho());
        if(byId.isPresent()){
            byId.get().setIdBangKeCanHang(null);
            byId.get().setSoBangKeCanHang(null);
            scPhieuXuatKhoHdrRepository.save(byId.get());
        }
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(ScBangKeXuatVatTuReq req, HttpServletResponse response) throws Exception {

    }

    @Override
    public Page<ScQuyetDinhXuatHang> searchBangKeCanHang(ScBangKeXuatVatTuReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        ScQuyetDinhXuatHangReq reqQd = new ScQuyetDinhXuatHangReq();
        reqQd.setNam(req.getNam());
        reqQd.setSoQd(req.getSoQdXh());
        reqQd.setTrangThai(TrangThaiAllEnum.BAN_HANH.getId());
        reqQd.setNgayTu(req.getNgayTuXh());
        reqQd.setNgayDen(req.getNgayDenXh());
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
                    req.setIdScDanhSachHdr(scDanhSachHdr.getId());
                    if(userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
                        req.setMaDviSr(userInfo.getDvql());
                    }
                    // Lấy bảng kê theo từng danh sách sửa chữa ( địa điểm )
                    List<ScBangKeXuatVatTuHdr> scBangKeXuatVatTuHdrs = hdrRepository.searchList(req);
                    scDanhSachHdr.setScBangKeXuatVatTuList(scBangKeXuatVatTuHdrs);
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
    public ReportTemplateResponse preview(ScBangKeXuatVatTuReq objReq) throws Exception {
        ScBangKeXuatVatTuHdr optional = detail(objReq.getId());
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
    }
}
