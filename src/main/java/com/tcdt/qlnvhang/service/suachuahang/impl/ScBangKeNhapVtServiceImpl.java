package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.*;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.suachua.ScBangKeNhapVtReq;
import com.tcdt.qlnvhang.request.suachua.ScBangKeXuatVatTuReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhNhapHangReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.response.suachua.ScBangKeNhapVtDTO;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.suachuahang.ScBangKeNhapVtService;
import com.tcdt.qlnvhang.service.suachuahang.ScPhieuNhapKhoService;
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
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScBangKeNhapVtServiceImpl extends BaseServiceImpl implements ScBangKeNhapVtService {
    @Autowired
    private ScBangKeNhapVtHdrRepository hdrRepository;

    @Autowired
    private ScBangKeNhapVtDtlRepository dtlRepository;
    
    @Autowired
    private ScPhieuNhapKhoHdrRepository scPhieuNhapKhoHdrRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ScQuyetDinhNhapHangRepository scQuyetDinhNhapHangRepository;

    @Autowired
    private ScQuyetDinhNhapHangDtlRepository scQuyetDinhNhapHangDtlRepository;

    @Autowired
    private ScDanhSachServiceImpl scDanhSachServiceImpl;

    @Autowired
    private ScPhieuNhapKhoService scPhieuNhapKhoService;

    @Override
    public Page<ScBangKeNhapVtHdr> searchPage(ScBangKeNhapVtReq req) throws Exception {
        return null;
    }

    private void updatePhieuNhapKho(ScBangKeNhapVtHdr data,Long idPhieuNhapKho,Long idPhieuNhapKhoOld){
        if(!Objects.isNull(idPhieuNhapKhoOld) && !idPhieuNhapKho.equals(idPhieuNhapKhoOld)){
            Optional<ScPhieuNhapKhoHdr> byId = scPhieuNhapKhoHdrRepository.findById(idPhieuNhapKhoOld);
            if(byId.isPresent()){
                byId.get().setIdBangKeCanHang(null);
                byId.get().setSoBangKeCanHang(null);
                scPhieuNhapKhoHdrRepository.save(byId.get());
            }
        }else{
            Optional<ScPhieuNhapKhoHdr> byId = scPhieuNhapKhoHdrRepository.findById(idPhieuNhapKho);
            if(byId.isPresent()){
                byId.get().setIdBangKeCanHang(data.getId());
                byId.get().setSoBangKeCanHang(data.getSoBangKe());
                scPhieuNhapKhoHdrRepository.save(byId.get());
            }
        }
    }

    public List<ScBangKeNhapVtDtl> saveDtl(ScBangKeNhapVtReq req, Long idHdr){
        dtlRepository.deleteAllByIdHdr(idHdr);
        req.getChildren().forEach(item -> {
            item.setId(null);
            item.setIdHdr(idHdr);
            dtlRepository.save(item);
        });
        return req.getChildren();
    }

    @Override
    public ScBangKeNhapVtHdr create(ScBangKeNhapVtReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        String dvql = userInfo.getDvql();
        ScBangKeNhapVtHdr data = new ScBangKeNhapVtHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(dvql);
        data.setIdThuKho(userInfo.getId());
        data.setId(Long.parseLong(req.getSoBangKe().split("/")[0]));
        ScBangKeNhapVtHdr created = hdrRepository.save(data);
        List<ScBangKeNhapVtDtl> ScBangKeNhapVtDtls = saveDtl(req, created.getId());
        created.setChildren(ScBangKeNhapVtDtls);
        this.updatePhieuNhapKho(data,data.getIdPhieuNhapKho(),null);
        return created;
    }

    @Override
    public ScBangKeNhapVtHdr update(ScBangKeNhapVtReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<ScBangKeNhapVtHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        ScBangKeNhapVtHdr data = optional.get();
        this.updatePhieuNhapKho(data,req.getIdPhieuNhapKho(),data.getIdPhieuNhapKho());
        BeanUtils.copyProperties(req, data);
        ScBangKeNhapVtHdr created = hdrRepository.save(data);
        List<ScBangKeNhapVtDtl> ScBangKeNhapVtDtls = saveDtl(req, created.getId());
        created.setChildren(ScBangKeNhapVtDtls);
        return created;
    }

    @Override
    public ScBangKeNhapVtHdr detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<ScBangKeNhapVtHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScBangKeNhapVtHdr data = optional.get();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        data.setChildren(dtlRepository.findByIdHdr(id));
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
        if(!Objects.isNull(data.getIdPhieuNhapKho())){
            data.setScPhieuNhapKhoHdr(scPhieuNhapKhoService.detail(data.getIdPhieuNhapKho()));
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
    public ScBangKeNhapVtHdr approve(ScBangKeNhapVtReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<ScBangKeNhapVtHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScBangKeNhapVtHdr hdr = optional.get();
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
        ScBangKeNhapVtHdr save = hdrRepository.save(hdr);
        return save;
    }

    @Override
    public void delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<ScBangKeNhapVtHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        hdrRepository.delete(optional.get());
        dtlRepository.deleteAllByIdHdr(optional.get().getId());
        Optional<ScPhieuNhapKhoHdr> byId = scPhieuNhapKhoHdrRepository.findById(optional.get().getIdPhieuNhapKho());
        if(byId.isPresent()){
            byId.get().setIdBangKeCanHang(null);
            byId.get().setSoBangKeCanHang(null);
            scPhieuNhapKhoHdrRepository.save(byId.get());
        }
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(ScBangKeNhapVtReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<ScBangKeNhapVtHdr> page = searchPage(req);
        List<ScBangKeNhapVtHdr> data = page.getContent();

        String title = "Danh sách phiếu xuất kho";
        String[] rowsName = new String[]{"STT", "Năm xuất", "Số QĐ giao NVXH", "Ngày ký QĐ giao NVXH", "Thời hạn xuất sửa chữa","Thời hạn nhập sửa chữa","Số QĐ SC hàng DTQG","Trích yếu", "Trạng thái QĐ","Trạng thái xuất để SC"};
        String fileName = "danh-sach.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            ScBangKeNhapVtHdr dx = data.get(i);
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
    public Page<ScQuyetDinhNhapHang> searchBangKeNhapVt(ScBangKeNhapVtReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        ScQuyetDinhNhapHangReq reqQd = new ScQuyetDinhNhapHangReq();
        reqQd.setNam(req.getNam());
        reqQd.setSoQd(req.getSoQdNh());
        reqQd.setTrangThai(TrangThaiAllEnum.BAN_HANH.getId());
        reqQd.setThoiHanNhapTu(req.getNgayTuNh());
        reqQd.setThoiHanNhapDen(req.getNgayDenNh());
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
                        List<ScPhieuNhapKhoHdr> allByIdScDanhSachHdr = scPhieuNhapKhoHdrRepository.findAllByIdScDanhSachHdrAndIdQdNh(detail.getId(),dtl.getIdHdr());
                        if(allByIdScDanhSachHdr != null && !allByIdScDanhSachHdr.isEmpty()){
                            List<Long> ids = allByIdScDanhSachHdr.stream().map(ScPhieuNhapKhoHdr::getId).collect(Collectors.toList());
                            req.setIdPhieuNhapKhoList(ids);
                            List<ScBangKeNhapVtHdr> listBk = hdrRepository.searchList(req);
                            newDtl.setScBangKeNhapVtList(listBk);
                        }
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
    public ReportTemplateResponse preview(ScBangKeNhapVtReq objReq) throws Exception {
        ScBangKeNhapVtHdr optional = detail(objReq.getId());
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
    }


}
