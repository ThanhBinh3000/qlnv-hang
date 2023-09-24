package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlToChucRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.SearchXhTlQuyetDinh;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhSc;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTrinhThamDinhHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhDtl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlToChucHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhTlQuyetDinhService extends BaseServiceImpl {

    @Autowired
    private XhTlQuyetDinhHdrRepository xhTlQuyetDinhHdrRepository;
    @Autowired
    private XhTlQuyetDinhHdrRepository hdrRepository;
    @Autowired
    private XhTlQuyetDinhDtlRepository xhTlQuyetDinhDtlRepository;
    @Autowired
    private XhTlHoSoHdrRepository xhTlHoSoHdrRepository;
    @Autowired
    private XhTlToChucRepository xhTlToChucRepository;

    @Autowired
    private XhTlHoSoService xhTlHoSoService;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhTlQuyetDinhHdr> searchPage(CustomUserDetails currentUser, SearchXhTlQuyetDinh req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql.substring(0, 4));
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhTlQuyetDinhHdr> search = xhTlQuyetDinhHdrRepository.search(req, pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
//        search.getContent().forEach(s -> {
//            s.getQuyetDinhDtl().forEach(f -> {
//                f.setMapDmucDvi(mapDmucDvi);
//                f.setMapVthh(mapVthh);
//                f.setTrangThaiThucHien(f.getTrangThaiThucHien());
//            });
//            s.setMapDmucDvi(mapDmucDvi);
//            s.setTrangThai(s.getTrangThai());
//        });
        return search;
    }

    @Transactional
    public XhTlQuyetDinhHdr create(XhTlQuyetDinhHdrReq req) throws Exception {
        XhTlQuyetDinhHdr hdr = new XhTlQuyetDinhHdr();
        validateData(req);
        BeanUtils.copyProperties(req, hdr);
        hdr.setMaDvi(getUser().getDvql());
        hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        XhTlQuyetDinhHdr created = hdrRepository.save(hdr);
//        this.updateScTongHopHdr(created,false);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), created.getId(), ScQuyetDinhSc.TABLE_NAME + "_CAN_CU");
        created.setFileCanCu(canCu);
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), ScQuyetDinhSc.TABLE_NAME + "_DINH_KEM");
        created.setFileDinhKem(fileDinhKem);
        return created;
    }

//    void updateScTongHopHdr(ScQuyetDinhSc sc,boolean isDelete) throws Exception {
//        Optional<ScTrinhThamDinhHdr> byId = scTrinhThamDinhRepository.findById(sc.getIdTtr());
//        if(byId.isPresent()){
//            ScTrinhThamDinhHdr data = byId.get();
//            if(isDelete){
//                data.setIdQdSc(null);
//                data.setSoQdSc(null);
//            }else{
//                data.setIdQdSc(sc.getId());
//                data.setSoQdSc(sc.getSoQd());
//            }
//            scTrinhThamDinhRepository.save(data);
//        }else{
//            throw new Exception("Không tìm thấy số tờ trình cần sửa chữa");
//        }
//    }

    void validateData(XhTlQuyetDinhHdrReq req) throws Exception {
        Optional<XhTlQuyetDinhHdr> bySoQd = hdrRepository.findBySoQd(req.getSoQd());
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

    @Transactional
    public XhTlQuyetDinhHdr update(CustomUserDetails currentUser, XhTlQuyetDinhHdrReq req) throws Exception {
        Optional<XhTlQuyetDinhHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        validateData(req);
        XhTlQuyetDinhHdr hdr = optional.get();
        BeanUtils.copyProperties(req, hdr);
        XhTlQuyetDinhHdr created = hdrRepository.save(hdr);
//        this.updateScTongHopHdr(created,false);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(XhTlQuyetDinhHdr.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), created.getId(), XhTlQuyetDinhHdr.TABLE_NAME + "_CAN_CU");
        created.setFileCanCu(canCu);
        fileDinhKemService.delete(req.getId(), Lists.newArrayList(XhTlQuyetDinhHdr.TABLE_NAME + "_TAI_LIEU_DINH_KEM"));
        List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), XhTlQuyetDinhHdr.TABLE_NAME + "_DINH_KEM");
        created.setFileDinhKem(fileDinhKemList);
        return created;
    }

    public XhTlQuyetDinhHdr detail(Long id) throws Exception {
        Optional<XhTlQuyetDinhHdr> optional = xhTlQuyetDinhHdrRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        XhTlQuyetDinhHdr data = optional.get();
        List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Collections.singleton(XhTlQuyetDinhHdr.TABLE_NAME + "_CAN_CU"));
        data.setFileCanCu(canCu);

        List<FileDinhKem> fileDinhKemList = fileDinhKemService.search(data.getId(), Collections.singleton(XhTlQuyetDinhHdr.TABLE_NAME + "_DINH_KEM"));
        data.setFileDinhKem(fileDinhKemList);

        data.setXhTlHoSoHdr(xhTlHoSoService.detail(data.getIdHoSo()));
        return data;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhTlQuyetDinhHdr> optional = xhTlQuyetDinhHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) throw new Exception("Bản ghi không tồn tại");
        XhTlQuyetDinhHdr data = optional.get();
        if (!DataUtils.isNullObject(data.getIdHoSo())) {
            Optional<XhTlHoSoHdr> hoSo = xhTlHoSoHdrRepository.findById(data.getIdHoSo());
            if (hoSo.isPresent()) {
                hoSo.get().setIdQd(null);
                hoSo.get().setSoQd(null);
                xhTlHoSoHdrRepository.save(hoSo.get());
            }
        }
        xhTlQuyetDinhHdrRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhTlQuyetDinhHdr> list = xhTlQuyetDinhHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) throw new Exception("Bản ghi không tồn tại");
        List<Long> listHoSo = list.stream().map(XhTlQuyetDinhHdr::getIdHoSo).collect(Collectors.toList());
        List<XhTlHoSoHdr> listObjQdPd = xhTlHoSoHdrRepository.findByIdIn(listHoSo);
        listObjQdPd.forEach(s -> {
            s.setIdQd(null);
            s.setSoQd(null);
        });
        xhTlHoSoHdrRepository.saveAll(listObjQdPd);
        xhTlQuyetDinhHdrRepository.deleteAll(list);
    }

    public XhTlQuyetDinhHdr approve(StatusReq req) throws Exception {
        Optional<XhTlQuyetDinhHdr> optional = hdrRepository.findById(req.getId());
        if(!optional.isPresent()){
            throw new Exception("Thông tin tổng hợp không tồn tại");
        }
        XhTlQuyetDinhHdr hdr = optional.get();
        String status = hdr.getTrangThai() + req.getTrangThai();
        switch (status) {
            // Re approve : gửi lại duyệt
            case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
            case Contains.TUCHOI_LDTC + Contains.CHODUYET_LDV:
                break;
            // Arena các cấp duuyệt
            case Contains.DUTHAO + Contains.CHODUYET_LDV:
            case Contains.CHODUYET_LDV + Contains.CHODUYET_LDTC:
            case Contains.CHODUYET_LDTC + Contains.BAN_HANH:
                break;
            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
            case Contains.CHODUYET_LDTC + Contains.TUCHOI_LDTC:
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        XhTlQuyetDinhHdr save = hdrRepository.save(hdr);
        return save;
    }

    public void export(CustomUserDetails currentUser, SearchXhTlQuyetDinh objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<XhTlQuyetDinhHdr> page = this.searchPage(currentUser, objReq);
        List<XhTlQuyetDinhHdr> data = page.getContent();
        String title = "Danh sách quyết định thanh lý hàng DTQG ";
        String[] rowsName = new String[]{"STT", "Số quyết định", "Trích yếu", "Ngày ký",
                "Hồ sơ đề nghị thanh lý", "Trạng thái"};
        String fileName = "danh-sach-quyet-dinh-thanh-ly-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhTlQuyetDinhHdr qd = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = qd.getSoQd();
            objs[2] = qd.getTrichYeu();
            objs[3] = qd.getNgayKy();
            objs[4] = qd.getSoHoSo();
            objs[5] = qd.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public XhTlQuyetDinhDtl approveDtl(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) throw new Exception("Không tìm thấy dữ liệu");
        Optional<XhTlQuyetDinhDtl> optional = xhTlQuyetDinhDtlRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu");
        String status = statusReq.getTrangThai() + optional.get().getTrangThaiThucHien();
        switch (status) {
            case Contains.HOANTHANHCAPNHAT + Contains.DANGCAPNHAT:
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThaiThucHien(statusReq.getTrangThai());
        xhTlQuyetDinhDtlRepository.save(optional.get());
        return optional.get();
    }

    public List<XhTlQuyetDinhHdr> dsTaoBaoCaoThanhLy(XhTlHoSoReq req) throws Exception {
        UserInfo currentUser = SecurityContextService.getUser();
        if (currentUser == null){
            throw new Exception("Access denied.");
        }
        req.setTrangThai(TrangThaiAllEnum.BAN_HANH.getId());
        List<XhTlQuyetDinhHdr> list = hdrRepository.listTaoBaoCaoThanhLy(req);
        return list;
    }
}
