package com.tcdt.qlnvhang.service.xuathang.daugia.xuatkho.phieuxuatkho;

import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.phieuxuatkho.XhPhieuXuatKho;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.phieuxuatkho.XhPhieuXuatKhoRepository;
import com.tcdt.qlnvhang.request.xuathang.xuatkho.XhPhieuXuatKhoReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.*;

@Log4j2
@Service
public class XhPhieuXuatKhoServiceImpl  extends BaseServiceImpl implements XhPhieuXuatKhoService {

    @Autowired
    XhPhieuXuatKhoRepository mainRepository;

    @Override
    public Page<XhPhieuXuatKho> searchPage(XhPhieuXuatKhoReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhPhieuXuatKho> data = mainRepository.searchPage( req,pageable);
        return data;
    }

    @Override
    public XhPhieuXuatKho create(XhPhieuXuatKhoReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        XhPhieuXuatKho data = new XhPhieuXuatKho();
        BeanUtils.copyProperties(req,data,"id");

        data.setNguoiTaoId(userInfo.getId());
        data.setNgayTao(new Date());
        data.setTrangThai(Contains.DUTHAO);
        data.setMaDvi(userInfo.getDvql());
        data.setNam(LocalDate.now().getYear());
//        data.setId(Long.parseLong(data.getSoPhieu().split("/")[0]));
//        data.setIdNguoiKiemNghiem(userInfo.getId());
        mainRepository.save(data);
        return data;
    }

    @Override
    public XhPhieuXuatKho update(XhPhieuXuatKhoReq req) throws Exception {
        if(Objects.isNull(req)){
            throw new Exception("Bad reqeust");
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        Optional<XhPhieuXuatKho> byId = mainRepository.findById(req.getId());
        if(!byId.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhPhieuXuatKho dataDb = byId.get();
        BeanUtils.copyProperties(req,dataDb,"id");
        dataDb.setNgaySua(new Date());
        dataDb.setNguoiSuaId(userInfo.getId());

        mainRepository.save(dataDb);
        return dataDb;
    }

    @Override
    public XhPhieuXuatKho detail(Long id) throws Exception {
        if(Objects.isNull(id)){
            throw new Exception("Bad reqeust");
        }

        Optional<XhPhieuXuatKho> byId = mainRepository.findById(id);
        if(!byId.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucHh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        XhPhieuXuatKho data = byId.get();

//        data.setTenLoaiVthh(mapDmucHh.get(data.getLoaiVthh()));
//        data.setTenCloaiVthh(mapDmucHh.get(data.getCloaiVthh()));
//        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
//        if(!Objects.isNull(data.getIdKtv())){
//            data.setTenKtv(userInfoRepository.findById(data.getIdKtv()).get().getFullName());
//        }
//        if(!Objects.isNull(data.getIdTruongPhong())){
//            data.setTenTruongPhong(userInfoRepository.findById(data.getIdTruongPhong()).get().getFullName());
//        }
//        if(!Objects.isNull(data.getIdNguoiKiemNghiem())){
//            data.setTenNguoiKiemNghiem(userInfoRepository.findById(data.getIdNguoiKiemNghiem()).get().getFullName());
//        }
//        data.setTenDiemKho(mapDmucDvi.get(data.getMaDiemKho()));
//        data.setTenNhaKho(mapDmucDvi.get(data.getMaNganKho()));
//        data.setTenNganKho(mapDmucDvi.get(data.getMaNganKho()));
//        data.setTenLoKho(mapDmucDvi.get(data.getMaLoKho()));
//        data.setChildren(subRepository.findAllByIdHdr(id));

        return data;
    }

    @Override
    public XhPhieuXuatKho approve(XhPhieuXuatKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if(Objects.isNull(req.getId())){
            throw new Exception("Bad reqeust");
        }

        if (!Contains.CAP_CUC.equals(userInfo.getCapDvi())){
            throw new Exception("Bad Request");
        }

        Optional<XhPhieuXuatKho> byId = mainRepository.findById(req.getId());
        if(!byId.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhPhieuXuatKho data = byId.get();
        String status = req.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.DUTHAO:
                data.setNguoiGuiDuyetId(userInfo.getId());
                data.setNgayGuiDuyet(new Date());
                break;
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TU_CHOI_LDC + Contains.CHODUYET_LDC:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(new Date());
                data.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHO_DUYET_LDC + Contains.CHODUYET_TP:
            case Contains.DA_DUYET_LDC + Contains.CHO_DUYET_LDC:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(new Date());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(req.getTrangThai());
        mainRepository.save(data);
        return data;
    }

    @Override
    public void delete(Long id) throws Exception {
        if(Objects.isNull(id)){
            throw new Exception("Bad request");
        }

        Optional<XhPhieuXuatKho> byId = mainRepository.findById(id);
        if(!byId.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        mainRepository.delete(byId.get());
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(XhPhieuXuatKhoReq req, HttpServletResponse response) throws Exception {

    }


//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public XhPhieuXuatKhoRes create(XhPhieuXuatKhoReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        XhPhieuXuatKho item = new XhPhieuXuatKho();
//        BeanUtils.copyProperties(req, item, "id");
//        Long count = xuatKhoRepo.getMaxId();
//        if (count == null) count = 1L;
//        item.setSpXuatKho(count.intValue() + 1 + "/" + LocalDate.now().getYear() + MA_DS);
//        item.setNgayTao(new Date());
//        item.setNguoiTaoId(userInfo.getId());
//        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
//        xuatKhoRepo.save(item);
//
//        List<XhPhieuXuatKhoCt> ds = req.getDs().stream()
//                .map(d -> {
//                    d.setPxuatKhoId(item.getId());
//                    return d;
//                })
//                .map(d -> {
//                    XhPhieuXuatKhoCt xuatKhoCt = new XhPhieuXuatKhoCt();
//                    BeanUtils.copyProperties(d, xuatKhoCt, "id");
//                    return xuatKhoCt;
//                })
//                .collect(Collectors.toList());
//
//        xuatKhoCtRepo.saveAll(ds);
//        XhPhieuXuatKhoRes result = new XhPhieuXuatKhoRes();
//        BeanUtils.copyProperties(item, result, "id");
//
//        List<XhPhieuXuatKhoCtRes> dsRes = ds
//                .stream()
//                .map(user -> new ModelMapper().map(user, XhPhieuXuatKhoCtRes.class))
//                .collect(Collectors.toList());
//
//        result.setDs(dsRes);
//
//        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), item.getId(), XhPhieuXuatKho.TABLE_NAME);
//        item.setFileDinhKems(fileDinhKems);
//        return result;
//
//    }
//
//    @Override
//    public XhPhieuXuatKhoRes update(XhPhieuXuatKhoReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        Optional<XhPhieuXuatKho> optional = xuatKhoRepo.findById(req.getId());
//        if (!optional.isPresent())
//            throw new Exception("Phiếu xuất kho không tồn tại.");
//
//        this.validateSoQuyetDinh(optional.get(), req);
//
//        XhPhieuXuatKho item = optional.get();
//        BeanUtils.copyProperties(req, item, "id", "so", "nam", "trangThai");
//        item.setNgaySua(new Date());
//        item.setNguoiSuaId(userInfo.getId());
//        xuatKhoRepo.save(item);
//
//        Map<Long, XhPhieuXuatKhoCt> mapChiTiet = xuatKhoCtRepo.findByPxuatKhoIdIn(Collections.singleton(item.getId()))
//                .stream().collect(Collectors.toMap(XhPhieuXuatKhoCt::getId, Function.identity()));
//
//        List<XhPhieuXuatKhoCt> chiTiets = this.saveListChiTiet(item.getId(), req.getDs(), mapChiTiet);
//        item.setDs(chiTiets);
//        if (!CollectionUtils.isEmpty(mapChiTiet.values()))
//            xuatKhoCtRepo.deleteAll(mapChiTiet.values());
//
//        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), item.getId(), XhPhieuXuatKho.TABLE_NAME);
//        item.setFileDinhKems(fileDinhKems);
//        XhPhieuXuatKhoRes res = new XhPhieuXuatKhoRes();
//        BeanUtils.copyProperties(item, res);
//
//        return res;
//    }
//
//    private List<XhPhieuXuatKhoCt> saveListChiTiet(Long parentId,
//                                                   List<XhPhieuXuatKhoCtReq> chiTietReqs,
//                                                   Map<Long, XhPhieuXuatKhoCt> mapChiTiet) throws Exception {
//        List<XhPhieuXuatKhoCt> chiTiets = new ArrayList<>();
//        for (XhPhieuXuatKhoCtReq req : chiTietReqs) {
//            Long id = req.getId();
//            XhPhieuXuatKhoCt chiTiet = new XhPhieuXuatKhoCt();
//
//            if (id != null && id > 0) {
//                chiTiet = mapChiTiet.get(id);
//                if (chiTiet == null)
//                    throw new Exception("Phiếu xuất kho chi tiết không tồn tại.");
//                mapChiTiet.remove(id);
//            }
//
//            BeanUtils.copyProperties(req, chiTiet, "id");
//            chiTiet.setPxuatKhoId(parentId);
//            chiTiets.add(chiTiet);
//        }
//
//        if (!CollectionUtils.isEmpty(chiTiets))
//            xuatKhoCtRepo.saveAll(chiTiets);
//
//        return chiTiets;
//    }
//
//    private void validateSoQuyetDinh(XhPhieuXuatKho update, XhPhieuXuatKhoReq req) throws Exception {
//        String so = req.getSoHd();
//        if (!StringUtils.hasText(so))
//            return;
//        if (update == null || (StringUtils.hasText(update.getSoHd()) && !update.getSoHd().equalsIgnoreCase(so))) {
//            Optional<XhPhieuXuatKho> optional = xuatKhoRepo.findFirstBySoHd(so);
//            Long updateId = Optional.ofNullable(update).map(XhPhieuXuatKho::getId).orElse(null);
//            if (optional.isPresent() && !optional.get().getId().equals(updateId))
//                throw new Exception("Số Hợp đồng " + so + " đã tồn tại");
//        }
//    }
//
//
//    @Override
//    public XhPhieuXuatKhoRes detail(Long id) throws Exception {
//        XhPhieuXuatKho phieuXuatKho = xuatKhoRepo.findById(id).get();
//        if (phieuXuatKho == null)
//            throw new Exception("Không tìm thấy dữ liệu.");
//
//        XhPhieuXuatKhoRes item = new XhPhieuXuatKhoRes();
//        BeanUtils.copyProperties(phieuXuatKho, item, "id");
//        item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//        // chưa tìm ngăn kho lô kho các giá trị tên
//        item.setTenNhakho(ktNhaKhoRepository.findByMaNhakho(item.getMaNhakho()).getTenNhakho());
//        item.setTenDiemkho(ktDiemKhoRepository.findByMaDiemkho(item.getMaDiemkho()).getTenDiemkho());
//        item.setTenNgankho(ktNganKhoRepository.findByMaNgankho(item.getMaNgankho()).getTenNgankho());
//        item.setTenNganlo(ktNganLoRepository.findFirstByMaNganlo(item.getMaNganlo()).getTenNganlo());
//        item.setTenDvi(qlnvDmDonviRepository.findByMaDvi(item.getMaDvi()).getTenDvi());
//        item.setDs(findXuatKhoCtResByPxuatKho(id));
//
//        item.setFileDinhKems(fileDinhKemService.search(id, Collections.singleton(XhPhieuXuatKho.TABLE_NAME)));
//
//        return item;
//    }
//
//    public List<XhPhieuXuatKhoCtRes> findXuatKhoCtResByPxuatKho(Long id) {
//        List<XhPhieuXuatKhoCt> lstDs = xuatKhoCtRepo.findByPxuatKhoIdIn(Collections.singleton(id));
//
//        return lstDs.stream()
//                .map(user -> new ModelMapper().map(user, XhPhieuXuatKhoCtRes.class))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public boolean delete(Long id) throws Exception {
//        Optional<XhPhieuXuatKho> optional = xuatKhoRepo.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Phiếu xuất kho không tồn tại.");
//
//        XhPhieuXuatKho item = optional.get();
//        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
//            throw new Exception("Không thể xóa phiếu xuất kho đã đã duyệt");
//        }
//        xuatKhoCtRepo.deleteAllByPxuatKhoId(item.getId());
//        xuatKhoRepo.delete(item);
//        return true;
//    }
//
//    @Override
//    public boolean deleteMultiple(DeleteReq req) throws Exception {
//        xuatKhoCtRepo.deleteByPxuatKhoIdIn(req.getIds());
//        xuatKhoRepo.deleteByIdIn(req.getIds());
//        return true;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean updateStatus(StatusReq stReq) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<XhPhieuXuatKho> optional = xuatKhoRepo.findById(stReq.getId());
//        if (!optional.isPresent())
//            throw new Exception("Phiếu xuất kho không tồn tại.");
//
//        XhPhieuXuatKho item = optional.get();
//        String trangThai = item.getTrangThai();
//        if (NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
//                return false;
//
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId());
//            item.setNguoiGuiDuyetId(userInfo.getId());
//            item.setNgayGuiDuyet(new Date());
//
//        } else if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
//                return false;
//
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId());
//            item.setNguoiPduyetId(userInfo.getId());
//            item.setNgayPduyet(new Date());
//
//        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
//                return false;
//
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId());
//            item.setNguoiPduyetId(userInfo.getId());
//            item.setNgayPduyet(new Date());
//            item.setLyDoTuChoi(stReq.getLyDo());
//
//        } else {
//            throw new Exception("Bad request.");
//        }
//
//        return true;
//    }
//
//    @Override
//    public Page<XhPhieuXuatKhoRes> search(XhPhieuXuatKhoSearchReq req) throws Exception {
//        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//        List<XhPhieuXuatKhoRes> list = xuatKhoRepo.search(req);
//        list.forEach(item -> {
//            item.setDs(findXuatKhoCtResByPxuatKho(item.getId()));
//            item.setFileDinhKems(fileDinhKemService.search(item.getId(), Collections.singleton(XhPhieuXuatKho.TABLE_NAME)));
//            item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//        });
//        Page<XhPhieuXuatKhoRes> page = new PageImpl<>(list, pageable, list.size());
//
//        return page;
//    }
//
//    @Override
//    public boolean exportToExcel(XhPhieuXuatKhoSearchReq objReq, HttpServletResponse response) throws Exception {
//
//        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
//        List<XhPhieuXuatKhoRes> list = this.search(objReq).get().collect(Collectors.toList());
//
//        if (CollectionUtils.isEmpty(list))
//            return true;
//
//        String[] rowsName = new String[]{"STT", "Số phiếu xuất", "Số quyết định xuất", "Số hợp đồng",
//                "Ngày xuất kho", "Điểm kho", "Nhà kho", "Ngăn kho", "Lô kho", "Trạng thái"};
//        String filename = "Danh_sach_phieu_xuat_kho.xlsx";
//
//        List<Object[]> dataList = new ArrayList<Object[]>();
//        Object[] objs = null;
//
//        try {
//            for (int i = 0; i < list.size(); i++) {
//                XhPhieuXuatKhoRes item = list.get(i);
//                objs = new Object[rowsName.length];
//                objs[0] = i;
//                objs[1] = item.getSpXuatKho();
//                objs[2] = item.getTenSqdx();
//                objs[3] = item.getSoHd();
//                objs[4] = item.getXuatKho().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
//                objs[5] = item.getTenDiemkho();
//                objs[6] = item.getTenNhakho();
//                objs[7] = item.getTenNgankho();
//                objs[8] = item.getTenNganlo();
//                objs[9] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
//                dataList.add(objs);
//            }
//
//            ExportExcel ex = new ExportExcel(SHEET_PHIEU_XUAT_KHO, filename, rowsName, dataList, response);
//            ex.export();
//        } catch (Exception e) {
//            log.error("Error export", e);
//            return false;
//        }
//
//        return true;
//    }
}
