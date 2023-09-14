package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.*;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhScReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.suachuahang.ScPhieuXuatKhoService;
import com.tcdt.qlnvhang.service.suachuahang.ScQuyetDinhScService;
import com.tcdt.qlnvhang.service.suachuahang.ScQuyetDinhXuatHangService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.UserUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.*;

@Service
public class ScQuyetDinhXuatHangServiceImpl extends BaseServiceImpl implements ScQuyetDinhXuatHangService {

    @Autowired
    private ScQuyetDinhXuatHangRepository scQuyetDinhXuatHangRepository;

    @Autowired
    private ScQuyetDinhNhapHangRepository scQuyetDinhNhapHangRepository;

    @Autowired
    private ScPhieuXuatKhoHdrRepository scPhieuXuatKhoHdrRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private ScQuyetDinhScImpl scQuyetDinhScImpl;

    @Autowired
    private ScKiemTraChatLuongHdrRepository scKiemTraChatLuongHdrRepository;

    @Autowired
    private ScPhieuXuatKhoService scPhieuXuatKhoService;

    @Autowired
    private ScPhieuNhapKhoHdrRepository scPhieuNhapKhoHdrRepository;



    @Override
    public Page<ScQuyetDinhXuatHang> searchPage(ScQuyetDinhXuatHangReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<ScQuyetDinhXuatHang> search = scQuyetDinhXuatHangRepository.searchPage(req, pageable);
        return search;
    }

    @Override
    public ScQuyetDinhXuatHang create(ScQuyetDinhXuatHangReq req) throws Exception {
        UserInfo currentUser = SecurityContextService.getUser();
        if (!currentUser.getCapDvi().equals(Contains.CAP_CUC)) {
            throw new Exception("Quyết định xuất hàng chỉ được thực hiện ở cấp cục");
        }
        validateData(req);
        ScQuyetDinhXuatHang hdr = new ScQuyetDinhXuatHang();
        BeanUtils.copyProperties(req, hdr);
        hdr.setMaDvi(currentUser.getDvql());
        hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        ScQuyetDinhXuatHang created = scQuyetDinhXuatHangRepository.save(hdr);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScQuyetDinhXuatHang.TABLE_NAME + "_CAN_CU");
        created.setFileCanCu(canCu);
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScQuyetDinhXuatHang.TABLE_NAME + "_DINH_KEM");
        created.setFileDinhKem(fileDinhKem);
        return created;
    }

    void validateData(ScQuyetDinhXuatHangReq req) throws Exception {
        Optional<ScQuyetDinhXuatHang> bySoQd = scQuyetDinhXuatHangRepository.findBySoQd(req.getSoQd());
        if(bySoQd.isPresent()){
            if(ObjectUtils.isEmpty(req.getId())){
                throw new Exception("Số quyết định " + bySoQd.get().getSoQd() +" đã tồn tại");
            }else{
                if(!req.getId().equals(bySoQd.get().getId())){
                    throw new Exception("Số quyết định " + bySoQd.get().getSoQd() +" đã tồn tại");
                }
            }
        }
    }

    @Override
    public ScQuyetDinhXuatHang update(ScQuyetDinhXuatHangReq req) throws Exception {
        UserInfo currentUser = SecurityContextService.getUser();
        if (!currentUser.getCapDvi().equals(Contains.CAP_CUC)) {
            throw new Exception("Quyết định xuất hàng chỉ được thực hiện ở cấp cục");
        }
        Optional<ScQuyetDinhXuatHang> optional = scQuyetDinhXuatHangRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        validateData(req);
        ScQuyetDinhXuatHang hdr = optional.get();
        BeanUtils.copyProperties(req, hdr);
        ScQuyetDinhXuatHang created = scQuyetDinhXuatHangRepository.save(hdr);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(ScQuyetDinhXuatHang.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), created.getId(), ScQuyetDinhXuatHang.TABLE_NAME + "_CAN_CU");
        created.setFileCanCu(canCu);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(ScQuyetDinhXuatHang.TABLE_NAME + "_DINH_KEM"));
        List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScQuyetDinhXuatHang.TABLE_NAME + "_DINH_KEM");
        created.setFileDinhKem(fileDinhKemList);

        return created;
    }

    public ScQuyetDinhXuatHang detail(Long id) throws Exception {
        Optional<ScQuyetDinhXuatHang> optional = scQuyetDinhXuatHangRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScQuyetDinhXuatHang data = optional.get();
        List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Collections.singleton(ScQuyetDinhXuatHang.TABLE_NAME + "_CAN_CU"));
        data.setFileCanCu(canCu);
        List<FileDinhKem> fileDinhKemList = fileDinhKemService.search(data.getId(), Collections.singleton(ScQuyetDinhXuatHang.TABLE_NAME + "_DINH_KEM"));
        data.setFileDinhKem(fileDinhKemList);
        data.setScQuyetDinhSc(scQuyetDinhScImpl.detail(data.getIdQdSc()));
        List<ScKiemTraChatLuongHdr> allByIdQdXh = scKiemTraChatLuongHdrRepository.findAllByIdQdXh(id);
        if(!allByIdQdXh.isEmpty()){
            allByIdQdXh.forEach(item ->{
                try {
                    ScPhieuXuatKhoHdr byId = scPhieuXuatKhoService.detail(item.getIdPhieuXuatKho());
                    item.setScPhieuXuatKhoHdr(byId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            data.setScKiemTraChatLuongHdrList(allByIdQdXh);
        }
        return data;
    }

    @Override
    public ScQuyetDinhXuatHang approve(ScQuyetDinhXuatHangReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<ScQuyetDinhXuatHang> optional = scQuyetDinhXuatHangRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScQuyetDinhXuatHang hdr = optional.get();
        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            // Re approve : gửi lại duyệt
            case Contains.TUCHOI_TP + Contains.DUTHAO:
            case Contains.TUCHOI_LDC + Contains.DUTHAO:
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
        ScQuyetDinhXuatHang save = scQuyetDinhXuatHangRepository.save(hdr);
        return save;
    }

    @Transient
    public void delete(Long id) throws Exception {
        Optional<ScQuyetDinhXuatHang> optional = scQuyetDinhXuatHangRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        fileDinhKemService.delete(id, Lists.newArrayList(ScQuyetDinhXuatHang.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.delete(id, Lists.newArrayList(ScQuyetDinhXuatHang.TABLE_NAME + "_DINH_KEM"));
        scQuyetDinhXuatHangRepository.delete(optional.get());
    }

    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (CollectionUtils.isNotEmpty(listMulti)) {
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
    public void export(ScQuyetDinhXuatHangReq req, HttpServletResponse response) throws Exception {

    }


    @Override
    public List<ScQuyetDinhXuatHang> dsTaoPhieuXuatKho(ScQuyetDinhXuatHangReq req) throws Exception {
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
        List<ScQuyetDinhXuatHang> list = scQuyetDinhXuatHangRepository.listTaoPhieuXuatKho(req);
        return list;
    }

    @Override
    public List<ScQuyetDinhXuatHang> searchDanhSachTaoBaoCao(ScQuyetDinhXuatHangReq req) throws Exception {
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
        List<ScQuyetDinhXuatHang> list = scQuyetDinhXuatHangRepository.listTaoBaoCao(req);
        return list;
    }

    @Override
    public List<ScDanhSachHdr> getDetailBaoCao(Long idQdXh) throws Exception {
        List<ScDanhSachHdr> listDs = new ArrayList<>();
        // get danh sách sữa chữa
        ScQuyetDinhXuatHang detail = detail(idQdXh);
        detail.getScQuyetDinhSc().getScTrinhThamDinhHdr().getChildren().forEach((item)->{
            listDs.add(item.getScDanhSachHdr());
        });


        // get qd nhập hang
        List<ScPhieuNhapKhoHdr> listMaster = new ArrayList<>();
        List<ScQuyetDinhNhapHang> listQdNh = scQuyetDinhNhapHangRepository.findAllByIdQdXh(idQdXh);
        listQdNh.forEach( qdNh -> {
            List<ScPhieuNhapKhoHdr> listPnk = scPhieuNhapKhoHdrRepository.findAllByIdQdNh(qdNh.getId());
            listMaster.addAll(listPnk);
        });

        listDs.forEach(item -> {
            // Binding tổng số lượng xuất
            List<ScPhieuXuatKhoHdr> listPxk = scPhieuXuatKhoHdrRepository.findAllByIdQdXhAndIdScDanhSachHdr(idQdXh,item.getId());
            int sumSoLongXuat = listPxk.stream().mapToInt(o -> o.getTongSoLuong().intValue()).sum();
            item.setSoLuongXuat(sumSoLongXuat);

            int sumSoLongNhap = listMaster.stream().filter(o -> o.getIdScDanhSachHdr().equals(item.getId())).mapToInt(o -> o.getTongSoLuong().intValue()).sum();
            item.setSoLuongNhap(sumSoLongNhap);

            int kinhPhiThucTe = listMaster.stream().filter(o -> o.getIdScDanhSachHdr().equals(item.getId())).mapToInt(o -> o.getTongKinhPhiThucTe().intValue()).sum();
            item.setTongKinhPhiThucTe(kinhPhiThucTe);

        });
        return listDs;
    }

    @Override
    public ReportTemplateResponse preview(ScQuyetDinhXuatHangReq objReq) throws Exception {
        ScQuyetDinhXuatHang optional = detail(objReq.getId());
        ReportTemplate model = findByTenFile(objReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
//        String filePath = "/Users/vunt/Downloads/Print/"+objReq.getReportTemplateRequest().getFileName();
//        byte[] byteArray = Files.readAllBytes(Paths.get(filePath));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, optional);
    }

}
