package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.*;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.suachuahang.ScPhieuXuatKhoService;
import com.tcdt.qlnvhang.service.suachuahang.ScQuyetDinhScService;
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
public class ScPhieuXuatKhoServiceImpl extends BaseServiceImpl implements ScPhieuXuatKhoService {

    @Autowired
    private ScPhieuXuatKhoHdrRepository hdrRepository;

    @Autowired
    private ScPhieuXuatKhoDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ScQuyetDinhXuatHangRepository scQuyetDinhXuatHangRepository;

    @Autowired
    private ScQuyetDinhScService scQuyetDinhScService;

    @Autowired
    private ScBangKeXuatVatTuHdrRepository scBangKeXuatVatTuHdrRepository;

    @Autowired
    private ScDanhSachRepository scDanhSachRepository;

    @Autowired
    private ScDanhSachServiceImpl scDanhSachServiceImpl;
    @Autowired
    private ScBangKeNhapVtHdrRepository scBangKeNhapVtHdrRepository;

    @Override
    public Page<ScPhieuXuatKhoHdr> searchPage(ScPhieuXuatKhoReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<ScPhieuXuatKhoHdr> searchPage = hdrRepository.searchPage(req, pageable);

        return searchPage;
    }

    @Override
    public ScPhieuXuatKhoHdr create(ScPhieuXuatKhoReq req) throws Exception {
        UserInfo currentUser = SecurityContextService.getUser();
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!currentUser.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        ScPhieuXuatKhoHdr data = new ScPhieuXuatKhoHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setId(Long.parseLong(req.getSoPhieuXuatKho().split("/")[0]));
        data.setIdThuKho(currentUser.getId());
        ScPhieuXuatKhoHdr created = hdrRepository.save(data);
        saveFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScPhieuXuatKhoHdr.TABLE_NAME);
        List<ScPhieuXuatKhoDtl> scPhieuXuatKhoDtls = saveDtl(req, data.getId());
        created.setChildren(scPhieuXuatKhoDtls);
        return created;
    }

    public List<ScPhieuXuatKhoDtl> saveDtl(ScPhieuXuatKhoReq req,Long idHdr){
        dtlRepository.deleteAllByIdHdr(idHdr);
        req.getChildren().forEach(item -> {
            item.setId(null);
            item.setIdHdr(idHdr);
            dtlRepository.save(item);
        });
        return req.getChildren();
    }

    @Override
    public ScPhieuXuatKhoHdr update(ScPhieuXuatKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<ScPhieuXuatKhoHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        ScPhieuXuatKhoHdr data = optional.get();
        BeanUtils.copyProperties(req, data);
        fileDinhKemService.delete(data.getId(), Collections.singleton(ScPhieuXuatKhoHdr.TABLE_NAME));
        saveFileDinhKem(req.getFileDinhKemReq(), data.getId(), ScPhieuXuatKhoHdr.TABLE_NAME);
        List<ScPhieuXuatKhoDtl> scPhieuXuatKhoDtls = saveDtl(req, data.getId());
        data.setChildren(scPhieuXuatKhoDtls);
        return data;
    }

    @Override
    public ScPhieuXuatKhoHdr detail(Long id) throws Exception {
        Optional<ScPhieuXuatKhoHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        ScPhieuXuatKhoHdr data = optional.get();
        data.setFileDinhKem(fileDinhKemService.search(id, Collections.singleton(ScPhieuXuatKhoHdr.TABLE_NAME)));
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
    public ScPhieuXuatKhoHdr approve(ScPhieuXuatKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<ScPhieuXuatKhoHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScPhieuXuatKhoHdr hdr = optional.get();

        if(Objects.isNull(hdr.getIdBangKeCanHang())){
            throw new Exception("Phiếu xuất kho đang chưa khởi tạo bảng kê xuất vật tư. Vui lòng tạo bảng kê xuất vật tư");
        } else {
            Optional<ScBangKeXuatVatTuHdr> bkOp = scBangKeXuatVatTuHdrRepository.findById(hdr.getIdBangKeCanHang());
            if(bkOp.isPresent()){
                ScBangKeXuatVatTuHdr bk = bkOp.get();
                if(!bk.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDCC.getId())){
                    throw new Exception("Số bảng kê " +bk.getSoBangKe()+" chưa đc phê duyệt. Vui lòng phê duyệt bảng kê");
                }
            }else{
                throw new Exception("Phiếu xuất kho không tìm thấy bảng kê xuất vật tư. Vui lòng tạo bảng kê xuất vật tư");
            }
        }

        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            // Re approve : gửi lại duyệt
            case Contains.TUCHOI_LDCC + Contains.TUCHOI_LDCC:
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
        ScPhieuXuatKhoHdr save = hdrRepository.save(hdr);
        return save;
    }

    @Override
    public void delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<ScPhieuXuatKhoHdr> optional = hdrRepository.findById(id);
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
    public void export(ScPhieuXuatKhoReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<ScPhieuXuatKhoHdr> page = searchPage(req);
        List<ScPhieuXuatKhoHdr> data = page.getContent();

        String title = "Danh sách phiếu xuất kho";
        String[] rowsName = new String[]{"STT", "Năm xuất", "Số QĐ giao NVXH", "Ngày ký QĐ giao NVXH", "Thời hạn xuất sửa chữa","Thời hạn nhập sửa chữa","Số QĐ SC hàng DTQG","Trích yếu", "Trạng thái QĐ","Trạng thái xuất để SC"};
        String fileName = "danh-sach.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            ScPhieuXuatKhoHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = dx.getNam();
            objs[2] = dx.getSoQdXh();
            objs[3] = dx.getNam();
            objs[4] = dx.getNgayXuatKho();
            objs[5] = dx.getTenDiemKho()+"/"+dx.getTenNhaKho()+"/"+dx.getTenNganKho()+"/"+dx.getTenLoKho();
            objs[6] = dx.getSoPhieuXuatKho();
            objs[7] = dx.getNgayXuatKho();
            objs[8] = null;
            objs[9] = null;
            objs[10] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public Page<ScQuyetDinhXuatHang> searchPhieuXuatKho(ScPhieuXuatKhoReq req) throws Exception {
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
                    // Filter chi cục
                    if(scDanhSachHdr.getMaDiaDiem().startsWith(userInfo.getDvql())){
                        req.setIdScDanhSachHdr(scDanhSachHdr.getId());
                        if(userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
                            req.setMaDviSr(userInfo.getDvql());
                        }
                        // Lấy phiếu xuất kho theo từng danh sách sửa chữa ( địa điểm )
                        List<ScPhieuXuatKhoHdr> scPhieuXuatKhoHdrs = hdrRepository.searchList(req);
                        scDanhSachHdr.setScPhieuXuatKhoList(scPhieuXuatKhoHdrs);
                        scDanhSachHdrList.add(scDanhSachHdr);
                    }
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
    public List<ScPhieuXuatKhoHdr> searchDanhSachTaoBangKe(ScPhieuXuatKhoReq req) {
        req.setTrangThai(TrangThaiAllEnum.DU_THAO.getId());
        List<ScPhieuXuatKhoHdr> scPhieuXuatKhoHdrs = hdrRepository.searchListTaoBangKe(req);
        return scPhieuXuatKhoHdrs;
    }

    @Override
    public List<ScPhieuXuatKhoHdr> searchDanhSachTaoKiemTraCl(ScPhieuXuatKhoReq req) {
        req.setTrangThai(TrangThaiAllEnum.DA_DUYET_LDCC.getId());
        List<ScPhieuXuatKhoHdr> scPhieuXuatKhoHdrs = hdrRepository.searchListTaoKiemTraCl(req);
        return scPhieuXuatKhoHdrs;
    }

    @Override
    public ReportTemplateResponse preview(ScPhieuXuatKhoReq objReq) throws Exception {
        ScPhieuXuatKhoHdr optional = detail(objReq.getId());
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
    }


}
