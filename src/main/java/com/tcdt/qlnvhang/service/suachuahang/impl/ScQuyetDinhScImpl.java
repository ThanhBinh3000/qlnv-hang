package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScQuyetDinhScRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScTongHopHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScTrinhThamDinhRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhScReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.request.suachua.ScTongHopReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.suachuahang.ScQuyetDinhScService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhSc;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhXuatHang;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTongHopHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTrinhThamDinhHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ScQuyetDinhScImpl extends BaseServiceImpl implements ScQuyetDinhScService {
    @Autowired
    private ScQuyetDinhScRepository scQuyetDinhScRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private ScTrinhThamDinhServiceImpl scTrinhThamDinhServiceImpl;

    @Autowired
    private ScTrinhThamDinhRepository scTrinhThamDinhRepository;

    @Override
    public Page<ScQuyetDinhSc> searchPage(ScQuyetDinhScReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<ScQuyetDinhSc> search = scQuyetDinhScRepository.searchPage(req, pageable);
        return search;
    }

    @Override
    public ScQuyetDinhSc create(ScQuyetDinhScReq req) throws Exception {
        ScQuyetDinhSc hdr = new ScQuyetDinhSc();
        validateData(req);
        BeanUtils.copyProperties(req, hdr);
        hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        ScQuyetDinhSc created = scQuyetDinhScRepository.save(hdr);
        this.updateScTongHopHdr(created,false);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), created.getId(), ScQuyetDinhSc.TABLE_NAME + "_CAN_CU");
        created.setFileCanCu(canCu);
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScQuyetDinhSc.TABLE_NAME + "_DINH_KEM");
        created.setFileDinhKem(fileDinhKem);
        return created;
    }

    void updateScTongHopHdr(ScQuyetDinhSc sc,boolean isDelete) throws Exception {
        Optional<ScTrinhThamDinhHdr> byId = scTrinhThamDinhRepository.findById(sc.getIdTtr());
        if(byId.isPresent()){
            ScTrinhThamDinhHdr data = byId.get();
            if(isDelete){
                data.setIdQdSc(null);
                data.setSoQdSc(null);
            }else{
                data.setIdQdSc(sc.getId());
                data.setSoQdSc(sc.getSoQd());
            }
            scTrinhThamDinhRepository.save(data);
        }else{
            throw new Exception("Không tìm thấy số tờ trình cần sửa chữa");
        }
    }

    void validateData(ScQuyetDinhScReq req) throws Exception {
        Optional<ScQuyetDinhSc> bySoQd = scQuyetDinhScRepository.findBySoQd(req.getSoQd());
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
    public ScQuyetDinhSc update(ScQuyetDinhScReq req) throws Exception {
        Optional<ScQuyetDinhSc> optional = scQuyetDinhScRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        validateData(req);
        ScQuyetDinhSc hdr = optional.get();
        BeanUtils.copyProperties(req, hdr);
        ScQuyetDinhSc created = scQuyetDinhScRepository.save(hdr);
        this.updateScTongHopHdr(created,false);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(ScQuyetDinhSc.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), created.getId(), ScQuyetDinhSc.TABLE_NAME + "_CAN_CU");
        created.setFileCanCu(canCu);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(ScQuyetDinhSc.TABLE_NAME + "_TAI_LIEU_DINH_KEM"));
        List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScQuyetDinhSc.TABLE_NAME + "_DINH_KEM");
        created.setFileDinhKem(fileDinhKemList);

        return created;
    }

    public ScQuyetDinhSc detail(Long id) throws Exception {
        Optional<ScQuyetDinhSc> optional = scQuyetDinhScRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScQuyetDinhSc data = optional.get();
        List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Collections.singleton(ScQuyetDinhSc.TABLE_NAME + "_CAN_CU"));
        data.setFileCanCu(canCu);

        List<FileDinhKem> fileDinhKemList = fileDinhKemService.search(data.getId(), Collections.singleton(ScQuyetDinhSc.TABLE_NAME + "_DINH_KEM"));
        data.setFileDinhKem(fileDinhKemList);

        data.setScTrinhThamDinhHdr(scTrinhThamDinhServiceImpl.detail(data.getIdTtr()));
        return data;
    }

    @Override
    public ScQuyetDinhSc approve(ScQuyetDinhScReq req) throws Exception {
        Optional<ScQuyetDinhSc> optional = scQuyetDinhScRepository.findById(req.getId());
        if(!optional.isPresent()){
            throw new Exception("Thông tin tổng hợp không tồn tại");
        }
        String status = req.getTrangThai() + optional.get().getTrangThai();
        if ((TrangThaiAllEnum.BAN_HANH.getId() + TrangThaiAllEnum.DU_THAO.getId()).equals(status)) {
            optional.get().setTrangThai(req.getTrangThai());
            optional.get().setNgayKy(LocalDate.now());
        } else {
            throw new Exception("Gửi duyệt không thành công");
        }
        scQuyetDinhScRepository.save(optional.get());

        return optional.get();
    }

    @Transient
    public void delete(Long id) throws Exception {
        Optional<ScQuyetDinhSc> optional = scQuyetDinhScRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        fileDinhKemService.delete(id, Lists.newArrayList(ScQuyetDinhSc.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.delete(id, Lists.newArrayList(ScQuyetDinhSc.TABLE_NAME + "_DINH_KEM"));
        this.updateScTongHopHdr(optional.get(),true);
        scQuyetDinhScRepository.delete(optional.get());
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(ScQuyetDinhScReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<ScQuyetDinhSc> page = searchPage(req);
        List<ScQuyetDinhSc> data = page.getContent();

        String title = "Danh sách quyết định sửa chữa hàng DTQG";
        String[] rowsName = new String[]{"STT", "Số quyết định", "Trích yếu", "Ngày ký", "Số tờ trình", "Trạng thái"};
        String fileName = "danh-sach.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            ScQuyetDinhSc dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = dx.getSoQd();
            objs[2] = dx.getTrichYeu();
            objs[3] = dx.getNgayKy();
            objs[4] = dx.getSoTtr();
            objs[5] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public List<ScQuyetDinhSc> dsQuyetDinhXuatHang(ScQuyetDinhScReq req) throws Exception {
        UserInfo currentUser = SecurityContextService.getUser();
        if (currentUser == null){
            throw new Exception("Access denied.");
        }
        String dvql = currentUser.getDvql();
//        if (currentUser.getCapDvi().equals(Contains.CAP_CUC)) {
//            req.setMaDviSr(dvql);
//        }
        req.setTrangThai(TrangThaiAllEnum.BAN_HANH.getId());
        List<ScQuyetDinhSc> list = scQuyetDinhScRepository.listQuyetDinhXuatHang(req);
        return list;
    }
}
