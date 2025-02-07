package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScDanhSachRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScTrinhThamDinhDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScTrinhThamDinhRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.suachua.ScTrinhThamDinhDtlReq;
import com.tcdt.qlnvhang.request.suachua.ScTrinhThamDinhHdrReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeXuatVTHdrDTO;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.suachuahang.ScTrinhThamDinhService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
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
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class ScTrinhThamDinhServiceImpl extends BaseServiceImpl implements ScTrinhThamDinhService {

    @Autowired
    private ScTrinhThamDinhRepository hdrRepository;

    @Autowired
    private ScTrinhThamDinhDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private ScDanhSachRepository scDanhSachRepository;

    @Autowired
    private ScDanhSachServiceImpl scDanhSachServiceImpl;

    @Override
    public Page<ScTrinhThamDinhHdr> searchPage(ScTrinhThamDinhHdrReq req) throws Exception {
        UserInfo currentUser = SecurityContextService.getUser();
        if (currentUser == null) {
            throw new Exception("Access denied.");
        }
        String dvql = currentUser.getDvql();
        if (!currentUser.getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setMaDvi(dvql.substring(0, 6));
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<ScTrinhThamDinhHdr> search = hdrRepository.searchPage(req, pageable);
        return search;
    }

    @Transactional
    @Override
    public ScTrinhThamDinhHdr create(ScTrinhThamDinhHdrReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if(!userInfo.getCapDvi().equals(Contains.CAP_CUC)){
            throw new Exception("Đơn vị lưu phải là cấp cục");
        }
        validateData(req);
        ScTrinhThamDinhHdr hdr = new ScTrinhThamDinhHdr();
        BeanUtils.copyProperties(req, hdr);
        hdr.setNam(LocalDate.now().getYear());
        hdr.setMaDvi(userInfo.getDvql());
        hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        ScTrinhThamDinhHdr created = hdrRepository.save(hdr);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), created.getId(), ScTrinhThamDinhHdr.TABLE_NAME + "_CAN_CU");
        created.setFileCanCu(canCu);
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScTrinhThamDinhHdr.TABLE_NAME + "_DINH_KEM");
        created.setFileDinhKem(fileDinhKem);
        List<ScTrinhThamDinhDtl> scTrinhThamDinhDtls = this.saveDtl(req, created.getId());
        created.setChildren(scTrinhThamDinhDtls);
        return created;
    }

    void validateData(ScTrinhThamDinhHdrReq req) throws Exception {
        Optional<ScTrinhThamDinhHdr> bySoQd = hdrRepository.findBySoTtr(req.getSoTtr());
        if(bySoQd.isPresent()){
            if(ObjectUtils.isEmpty(req.getId())){
                throw new Exception("Số tờ trình " + bySoQd.get().getSoTtr() +" đã tồn tại");
            }else{
                if(!req.getId().equals(bySoQd.get().getId())){
                    throw new Exception("Số tờ trình " + bySoQd.get().getSoTtr() +" đã tồn tại");
                }
            }
        }
    }

    private List<ScTrinhThamDinhDtl> saveDtl(ScTrinhThamDinhHdrReq req,Long idHdr) throws Exception {
        List<ScTrinhThamDinhDtl> listDtl = new ArrayList<>();
        dtlRepository.deleteAllByIdHdr(idHdr);
        req.getChildren().forEach( item -> {
            ScTrinhThamDinhDtl dtl = new ScTrinhThamDinhDtl();
            dtl.setIdHdr(idHdr);
            dtl.setIdDsHdr(item.getId());
            dtlRepository.save(dtl);
            // Update lại data vào danh sách gốc
            Optional<ScDanhSachHdr> dsHdr = scDanhSachRepository.findById(item.getId());
            if(dsHdr.isPresent()){
                dsHdr.get().setKetQua(item.getKetQua());
                dsHdr.get().setDonGiaDk(item.getDonGiaDk());
                dsHdr.get().setDonGiaPd(item.getDonGiaPd());
                dsHdr.get().setSlDaDuyet(item.getSlDaDuyet());
                ScDanhSachHdr save = scDanhSachRepository.save(dsHdr.get());
                dtl.setScDanhSachHdr(save);
            }else{
                throw new RuntimeException("Không tìm thấy danh sách hàng cần sửa chữa");
            }
            listDtl.add(dtl);
        });
        return listDtl;
    }

    @Transactional
    @Override
    public ScTrinhThamDinhHdr update(ScTrinhThamDinhHdrReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<ScTrinhThamDinhHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        validateData(req);
        ScTrinhThamDinhHdr hdr = optional.get();
        BeanUtils.copyProperties(req, hdr);

        hdr.setNam(LocalDate.now().getYear());
        hdr.setMaDvi(userInfo.getDvql());
        if(hdr.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDC.getId())){
            hdr.setTrangThai(TrangThaiAllEnum.DANG_DUYET_CB_VU.getId());
        }
        ScTrinhThamDinhHdr created = hdrRepository.save(hdr);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(ScTrinhThamDinhHdr.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), created.getId(), ScTrinhThamDinhHdr.TABLE_NAME + "_CAN_CU");
        created.setFileCanCu(canCu);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(ScTrinhThamDinhHdr.TABLE_NAME + "_DINH_KEM"));
        List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScTrinhThamDinhHdr.TABLE_NAME + "_DINH_KEM");
        created.setFileDinhKem(fileDinhKemList);
        List<ScTrinhThamDinhDtl> scTrinhThamDinhDtls = this.saveDtl(req, created.getId());
        created.setChildren(scTrinhThamDinhDtls);
        return created;
    }

    @Override
    public ScTrinhThamDinhHdr detail(Long id) throws Exception {
        Optional<ScTrinhThamDinhHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScTrinhThamDinhHdr data = optional.get();
        List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Collections.singleton(ScTrinhThamDinhHdr.TABLE_NAME + "_CAN_CU"));
        data.setFileCanCu(canCu);
        List<FileDinhKem> dinhKem = fileDinhKemService.search(data.getId(), Collections.singleton(ScTrinhThamDinhHdr.TABLE_NAME + "_DINH_KEM"));
        data.setFileCanCu(dinhKem);
        HashMap<Long, List<ScTrinhThamDinhDtl>> dataChilren = getDataChilren(Collections.singletonList(data.getId()));
        data.setChildren(dataChilren.get(data.getId()));
        return data;
    }

    private HashMap<Long,List<ScTrinhThamDinhDtl>> getDataChilren(List<Long> idHdr){
        HashMap<Long,List<ScTrinhThamDinhDtl>> hashMap = new HashMap<>();
        idHdr.forEach(item -> {
            List<ScTrinhThamDinhDtl> dtl = dtlRepository.findAllByIdHdr(item);
            dtl.forEach( dataChilren -> {
                try {
                    dataChilren.setScDanhSachHdr(scDanhSachServiceImpl.detail(dataChilren.getIdDsHdr()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            hashMap.put(item,dtl);
        });
        return hashMap;
    }

    @Override
    public ScTrinhThamDinhHdr approve(ScTrinhThamDinhHdrReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<ScTrinhThamDinhHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        ScTrinhThamDinhHdr hdr = optional.get();
        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            // Re approve : gửi lại duyệt
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDV + Contains.CHODUYET_TP:
            case Contains.TU_CHOI_CBV + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDTC + Contains.CHODUYET_TP:
                break;
            // Arena các cấp duuyệt
            case Contains.DUTHAO + Contains.CHODUYET_TP:
            case Contains.CHODUYET_TP + Contains.CHODUYET_LDC:
                if(!userInfo.getCapDvi().equals(Contains.CAP_CUC)){
                    throw new Exception("Đơn vị gửi duyệt phải là cấp cục");
                }
                break;
            case Contains.CHODUYET_LDC + Contains.DADUYET_LDC:
                if(!userInfo.getCapDvi().equals(Contains.CAP_CUC)){
                    throw new Exception("Đơn vị gửi duyệt phải là cấp cục");
                }
                hdr.setNgayDuyetLdc(LocalDate.now());
                break;
            case Contains.DANG_DUYET_CB_VU + Contains.CHODUYET_LDV:
                if(!userInfo.getCapDvi().equals(Contains.CAP_TONG_CUC)){
                    throw new Exception("Đơn vị gửi duyệt phải là cấp Tổng cục");
                }
                break;
            case Contains.CHODUYET_LDV + Contains.CHODUYET_LDTC:
                if(!userInfo.getCapDvi().equals(Contains.CAP_TONG_CUC)){
                    throw new Exception("Đơn vị gửi duyệt phải là cấp Tổng cục");
                }
                hdr.setNgayDuyetLdv(LocalDate.now());
                break;
            case Contains.CHODUYET_LDTC + Contains.DADUYET_LDTC:
                if(!userInfo.getCapDvi().equals(Contains.CAP_TONG_CUC)){
                    throw new Exception("Đơn vị gửi duyệt phải là cấp Tổng cục");
                }
                hdr.setNgayDuyetLdtc(LocalDate.now());
                break;
            // Arena từ chối
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_LDC + Contains.TUCHOI_LDC:
            case Contains.DA_DUYET_LDC + Contains.TU_CHOI_CBV:
            case Contains.DANG_DUYET_CB_VU + Contains.TU_CHOI_CBV:
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
                hdr.setNgayDuyetLdv(LocalDate.now());
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDTC + Contains.TUCHOI_LDTC:
                hdr.setNgayDuyetLdtc(LocalDate.now());
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                this.rollBackDataDsHdr();
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        ScTrinhThamDinhHdr save = hdrRepository.save(hdr);
        return save;
    }

    // Khi lãnh đạo tồng cục từ chối thì coi như phải làm lại luồng này từ đầu cho lần sau chẳng hạn
    void rollBackDataDsHdr(){

    }

    @Transient
    @Override
    public void delete(Long id) throws Exception {
        Optional<ScTrinhThamDinhHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        fileDinhKemService.delete(id, Lists.newArrayList(ScTrinhThamDinhHdr.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.delete(id, Lists.newArrayList(ScTrinhThamDinhHdr.TABLE_NAME + "_DINH_KEM"));
        hdrRepository.delete(optional.get());
        List<ScTrinhThamDinhDtl> dtl = dtlRepository.findAllByIdHdr(id);
        if(dtl != null && !dtl.isEmpty()){
            // Update lại data gốc ds về giá trị ban đầu
            dtl.forEach(item -> {
                Optional<ScDanhSachHdr> ds = scDanhSachRepository.findById(item.getIdDsHdr());
                if(ds.isPresent()){
                    ds.get().setDonGiaDk(null);
                    ds.get().setKetQua(null);
                    ds.get().setDonGiaPd(null);
                    ds.get().setSlDaDuyet(null);
                    scDanhSachRepository.save(ds.get());
                }else{
                    throw new RuntimeException("Không tìm thấy danh sách sửa chữa hàng");
                }
            });
        }
    }

    @Override
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
    public void export(ScTrinhThamDinhHdrReq req, HttpServletResponse response) throws Exception {
        CustomUserDetails currentUser = UserUtils.getUserLoginInfo();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        req.setMaDvi(currentUser.getDvql());
        Page<ScTrinhThamDinhHdr> page = searchPage(req);
        List<ScTrinhThamDinhHdr> data = page.getContent();

        String title = "Thẩm định danh sách hàng dữ trữ quốc gia cần sửa chữa";
        String[] rowsName = new String[]{"STT", "Số tờ trình", "Trích yếu", "Ngày duyệt LĐ Cục", "Ngày duyệt LĐ Vụ", "Ngày duyệt LĐ TC", "Số QĐ SC", "Trạng thái thẩm định"};
        String fileName = "danh-sach.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            ScTrinhThamDinhHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = dx.getSoTtr();
            objs[2] = dx.getTrichYeu();
            objs[3] = dx.getNgayDuyetLdc();
            objs[4] = dx.getNgayDuyetLdv();
            objs[5] = dx.getNgayDuyetLdtc();
            objs[6] = dx.getSoQdSc();
            objs[7] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public List<ScTrinhThamDinhHdr> dsQuyetDinhSuaChua(ScTrinhThamDinhHdrReq req) throws Exception {

        UserInfo currentUser = SecurityContextService.getUser();
        if (currentUser == null){
            throw new Exception("Access denied.");
        }
        req.setTrangThai(TrangThaiAllEnum.DA_DUYET_LDTC.getId());
        List<ScTrinhThamDinhHdr> list = hdrRepository.listQuyetDinhSuaChua(req);
        return list;
    }
}
