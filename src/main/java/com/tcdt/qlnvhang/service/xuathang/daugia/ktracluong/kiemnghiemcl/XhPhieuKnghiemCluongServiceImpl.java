package com.tcdt.qlnvhang.service.xuathang.daugia.ktracluong.kiemnghiemcl;

import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMau;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauCt;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl.XhPhieuKnghiemCluong;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.phieukiemnghiemcl.XhPhieuKnghiemCluongCt;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.kiemnghiemcl.XhPhieuKnghiemCluongCtRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.kiemnghiemcl.XhPhieuKnghiemCluongRepository;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauCtRequest;
import com.tcdt.qlnvhang.request.bandaugia.bienbanlaymau.XhBbLayMauRequest;
import com.tcdt.qlnvhang.request.xuathang.phieukiemnghiemchatluong.XhPhieuKnghiemCluongReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class XhPhieuKnghiemCluongServiceImpl extends BaseServiceImpl implements XhPhieuKnghiemCluongService {

    private final XhPhieuKnghiemCluongRepository mainRepository;

    private final XhPhieuKnghiemCluongCtRepository subRepository;

    private final UserInfoRepository userInfoRepository;

    @Override
    public Page<XhPhieuKnghiemCluong> searchPage(XhPhieuKnghiemCluongReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhPhieuKnghiemCluong> data = mainRepository.searchPage( req,pageable);
        return data;
    }

    @Override
    public XhPhieuKnghiemCluong create(XhPhieuKnghiemCluongReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        XhPhieuKnghiemCluong data = new XhPhieuKnghiemCluong();
        BeanUtils.copyProperties(req,data,"id");

        data.setNguoiTaoId(userInfo.getId());
        data.setNgayTao(new Date());
        data.setTrangThai(Contains.DUTHAO);
        data.setMaDvi(userInfo.getDvql());
        data.setNam(LocalDate.now().getYear());
        data.setId(Long.parseLong(data.getSoPhieu().split("/")[0]));
        data.setIdNguoiKiemNghiem(userInfo.getId());
        mainRepository.save(data);

        saveDetail(req,data.getId());
        return data;
    }

    void saveDetail(XhPhieuKnghiemCluongReq req, Long idHdr){
        subRepository.deleteAllByIdHdr(idHdr);
        for (XhPhieuKnghiemCluongCt ctReq :req.getChildren()) {
            XhPhieuKnghiemCluongCt ct = new XhPhieuKnghiemCluongCt();
            BeanUtils.copyProperties(ctReq,ct,"id");
            ct.setIdHdr(idHdr);
            subRepository.save(ct);
        }
    }

    @Override
    public XhPhieuKnghiemCluong update(XhPhieuKnghiemCluongReq req) throws Exception {
        if(Objects.isNull(req)){
            throw new Exception("Bad reqeust");
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        Optional<XhPhieuKnghiemCluong> byId = mainRepository.findById(req.getId());
        if(!byId.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhPhieuKnghiemCluong dataDb = byId.get();
        BeanUtils.copyProperties(req,dataDb,"id");
        dataDb.setNgaySua(new Date());
        dataDb.setNguoiSuaId(userInfo.getId());

        mainRepository.save(dataDb);
        this.saveDetail(req,dataDb.getId());
        return dataDb;
    }

    @Override
    public XhPhieuKnghiemCluong detail(Long id) throws Exception {
        if(Objects.isNull(id)){
            throw new Exception("Bad reqeust");
        }

        Optional<XhPhieuKnghiemCluong> byId = mainRepository.findById(id);
        if(!byId.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, String> mapDmucHh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        XhPhieuKnghiemCluong data = byId.get();

        data.setTenLoaiVthh(mapDmucHh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(mapDmucHh.get(data.getCloaiVthh()));
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
        data.setTenKtv(userInfoRepository.findById(data.getIdKtv()).get().getFullName());
        data.setTenDiemKho(mapDmucDvi.get(data.getMaDiemKho()));
        data.setTenNhaKho(mapDmucDvi.get(data.getMaNganKho()));
        data.setTenNganKho(mapDmucDvi.get(data.getMaNganKho()));
        data.setTenLoKho(mapDmucDvi.get(data.getMaLoKho()));
        data.setChildren(subRepository.findAllByIdHdr(id));

        return data;
    }

    @Override
    public XhPhieuKnghiemCluong approve(XhPhieuKnghiemCluongReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();


        if(Objects.isNull(req.getId())){
            throw new Exception("Bad reqeust");
        }

        if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
            throw new Exception("Bad Request");
        }

        Optional<XhPhieuKnghiemCluong> byId = mainRepository.findById(req.getId());
        if(!byId.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhPhieuKnghiemCluong data = byId.get();
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

        Optional<XhPhieuKnghiemCluong> byId = mainRepository.findById(id);
        if(!byId.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        mainRepository.delete(byId.get());
        subRepository.deleteAllByIdHdr(byId.get().getId());
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(XhPhieuKnghiemCluongReq req, HttpServletResponse response) throws Exception {

    }
}


//
//    private static final String SHEET_PHIEU_KIEM_NGHIEM_CHAT_LUONG = "Phiếu kiểm nghiệm chất lượng";
//    private static final String STT = "STT";
//    private static final String SO_PHIEU = "Số Phiếu";
//    private static final String NGAY_KIEM_NGHIEM = "Ngày Kiểm Nghiệm";
//    private static final String SO_BIEN_BAN_LAY_MAU = "Số Biên Bản Lấy Mẫu";
//    private static final String NGAY_LAY_MAU = "Ngày Lấy Mẫu";
//    private static final String DIEM_KHO = "Điểm Kho";
//    private static final String NHA_KHO = "Nhà Kho";
//    private static final String NGAN_KHO = "Ngăn Kho";
//    private static final String NGAN_LO = "Ngăn Lô";
//    private static final String TRANG_THAI = "Trạng Thái";

//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public XhPhieuKnghiemCluongRes create(XhPhieuKnghiemCluongReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.validateSoPhieu(null, req);
//
//        XhPhieuKnghiemCluong item = new XhPhieuKnghiemCluong();
//        BeanUtils.copyProperties(req, item, "id");
//        item.setNgayTao(new Date());
//        item.setNguoiTaoId(userInfo.getId());
//        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
//        item.setMaDvi(userInfo.getDvql());
//        item.setCapDvi(userInfo.getCapDvi());
//        item.setSo(getSo());
//        item.setNam(LocalDate.now().getYear());
//        item.setSoPhieu(String.format("%s/%s/%s-%s", item.getSo(), item.getNam(), "PKNCL", userInfo.getMaPbb()));
//        xhPhieuKnghiemCluongRepository.save(item);
//
//        List<XhPhieuKnghiemCluongCt> chiTiets = this.saveListChiTiet(item.getId(), req.getCts(), new HashMap<>());
//        item.setCts(chiTiets);
//
//        return this.buildResponse(item);
//    }
//
//    private List<XhPhieuKnghiemCluongCt> saveListChiTiet(Long parentId,
//                                               List<XhPhieuKnghiemCluongCtReq> chiTietReqs,
//                                               Map<Long, XhPhieuKnghiemCluongCt> mapChiTiet) throws Exception {
//        List<XhPhieuKnghiemCluongCt> chiTiets = new ArrayList<>();
//        for (XhPhieuKnghiemCluongCtReq req : chiTietReqs) {
//            Long id = req.getId();
//            XhPhieuKnghiemCluongCt chiTiet = new XhPhieuKnghiemCluongCt();
//
//            if (id != null && id > 0) {
//                chiTiet = mapChiTiet.get(id);
//                if (chiTiet == null)
//                    throw new Exception("Phiếu kiểm nghiệm chất lượng chi tiết không tồn tại.");
//                mapChiTiet.remove(id);
//            }
//
//            BeanUtils.copyProperties(req, chiTiet, "id");
//            chiTiet.setPhieuKnghiemId(parentId);
//            chiTiets.add(chiTiet);
//        }
//
//        if (!CollectionUtils.isEmpty(chiTiets))
//            xhPhieuKnghiemCluongCtRepository.saveAll(chiTiets);
//
//        return chiTiets;
//    }
//
//
//    private XhPhieuKnghiemCluongRes buildResponse(XhPhieuKnghiemCluong item) throws Exception {
//        XhPhieuKnghiemCluongRes res = new XhPhieuKnghiemCluongRes();
//        List<XhPhieuKnghiemCluongCtRes> chiTiets = new ArrayList<>();
//        BeanUtils.copyProperties(item, res);
//        res.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//        res.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//        for (XhPhieuKnghiemCluongCt ct : item.getCts()) {
//            chiTiets.add(new XhPhieuKnghiemCluongCtRes(ct));
//        }
//        res.setCts(chiTiets);
//
//        Map<String,String> mapVthh = getListDanhMucHangHoa();
//        res.setTenVatTu(mapVthh.get(item.getMaVatTu()));
//        res.setTenVatTuCha(mapVthh.get(item.getMaVatTuCha()));
//
//        if (item.getQdgnvxId() != null) {
//            Optional<XhQdGiaoNvXh> qdXuat = xhQdGiaoNvXhRepository.findById(item.getQdgnvxId());
//            if (!qdXuat.isPresent()) {
//                throw new Exception("Không tìm thấy quyết định xuất");
//            }
////            res.setSoQuyetDinhXuat(qdXuat.get().getSoQuyetDinh());
//        }
//
//        //TODO: Biên bản lấy mẫu
//        this.setThongTinDonVi(res, item.getMaDvi());
//        return res;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public XhPhieuKnghiemCluongRes update(XhPhieuKnghiemCluongReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        Optional<XhPhieuKnghiemCluong> optional = xhPhieuKnghiemCluongRepository.findById(req.getId());
//        if (!optional.isPresent())
//            throw new Exception("Phiếu kiểm nghiệm chất lượng không tồn tại.");
//
//        this.validateSoPhieu(optional.get(), req);
//
//        XhPhieuKnghiemCluong item = optional.get();
//        BeanUtils.copyProperties(req, item, "id", "so", "nam");
//        item.setNgaySua(new Date());
//        item.setNguoiSuaId(userInfo.getId());
//
//        xhPhieuKnghiemCluongRepository.save(item);
//        Map<Long, XhPhieuKnghiemCluongCt> mapChiTiet = xhPhieuKnghiemCluongCtRepository.findByPhieuKnghiemIdIn(Collections.singleton(item.getId()))
//                .stream().collect(Collectors.toMap(XhPhieuKnghiemCluongCt::getId, Function.identity()));
//
//        List<XhPhieuKnghiemCluongCt> chiTiets = this.saveListChiTiet(item.getId(), req.getCts(), mapChiTiet);
//        item.setCts(chiTiets);
//
//        if (!CollectionUtils.isEmpty(mapChiTiet.values()))
//            xhPhieuKnghiemCluongCtRepository.deleteAll(mapChiTiet.values());
//
//
//        return this.buildResponse(item);
//    }
//
//    @Override
//    public XhPhieuKnghiemCluongRes detail(Long id) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<XhPhieuKnghiemCluong> optional = xhPhieuKnghiemCluongRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Phiếu kiểm nghiệm chất lượng không tồn tại.");
//
//        XhPhieuKnghiemCluong item = optional.get();
//        item.setCts(xhPhieuKnghiemCluongCtRepository.findByPhieuKnghiemIdIn(Collections.singleton(item.getId())));
//        return this.buildResponse(item);
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean delete(Long id) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<XhPhieuKnghiemCluong> optional = xhPhieuKnghiemCluongRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Phiếu kiểm nghiệm chất lượng không tồn tại.");
//
//        XhPhieuKnghiemCluong item = optional.get();
//        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
//            throw new Exception("Không thể xóa bảng kê đã đã duyệt");
//        }
//        xhPhieuKnghiemCluongCtRepository.deleteByPhieuKnghiemIdIn(Collections.singleton(item.getId()));
//        xhPhieuKnghiemCluongRepository.delete(item);
//        return true;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean updateStatus(StatusReq stReq) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<XhPhieuKnghiemCluong> optional = xhPhieuKnghiemCluongRepository.findById(stReq.getId());
//        if (!optional.isPresent())
//            throw new Exception("Phiếu kiểm nghiệm chất lượng không tồn tại.");
//
//        XhPhieuKnghiemCluong item = optional.get();
//        String trangThai = item.getTrangThai();
//        if (NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
//                return false;
//
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId());
//            item.setNguoiGuiDuyetId(userInfo.getId());
//            item.setNgayGuiDuyet(new Date());
//        } else if (NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId().equals(trangThai))
//                return false;
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId());
//            item.setNguoiPduyetId(userInfo.getId());
//            item.setNgayPduyet(new Date());
//        } else if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
//                return false;
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId());
//            item.setNguoiPduyetId(userInfo.getId());
//            item.setNgayPduyet(new Date());
//        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_KT.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId().equals(trangThai))
//                return false;
//
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_KT.getId());
//            item.setNguoiPduyetId(userInfo.getId());
//            item.setNgayPduyet(new Date());
//            item.setLyDoTuChoi(stReq.getLyDo());
//        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
//                return false;
//
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId());
//            item.setNguoiPduyetId(userInfo.getId());
//            item.setNgayPduyet(new Date());
//            item.setLyDoTuChoi(stReq.getLyDo());
//        } else {
//            throw new Exception("Bad request.");
//        }
//
//        return true;
//    }
//
//    @Override
//    public Page<XhPhieuKnghiemCluongRes> search(XhPhieuKnghiemCluongSearchReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
//        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//        List<Object[]> data = xhPhieuKnghiemCluongRepository.search(req);
//        List<XhPhieuKnghiemCluongRes> responses = new ArrayList<>();
//        for (Object[] o : data) {
//            XhPhieuKnghiemCluongRes response = new XhPhieuKnghiemCluongRes();
//            XhPhieuKnghiemCluong item = (XhPhieuKnghiemCluong) o[0];
//            Long qdNhapId = (Long) o[1];
//            String soQdNhap = (String) o[2];
//            KtNganLo nganLo = o[3] != null ? (KtNganLo) o[3] : null;
//            BeanUtils.copyProperties(item, response);
//            response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//            response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//            response.setQdgnvxId(qdNhapId);
//            response.setSoQuyetDinhXuat(soQdNhap);
//            this.thongTinNganLo(response, nganLo);
//            responses.add(response);
//        }
//
//        return new PageImpl<>(responses, pageable, xhPhieuKnghiemCluongRepository.count(req));
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean deleteMultiple(DeleteReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        xhPhieuKnghiemCluongCtRepository.deleteByPhieuKnghiemIdIn(req.getIds());
//        xhPhieuKnghiemCluongRepository.deleteByIdIn(req.getIds());
//        return true;
//    }
//
//    @Override
//    public boolean exportToExcel(XhPhieuKnghiemCluongSearchReq objReq, HttpServletResponse response) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
//        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
//        List<XhPhieuKnghiemCluongRes> list = this.search(objReq).get().collect(Collectors.toList());
//
//        if (CollectionUtils.isEmpty(list))
//            return true;
//
//        String[] rowsName = new String[] { STT, SO_PHIEU, NGAY_KIEM_NGHIEM, SO_BIEN_BAN_LAY_MAU, NGAY_LAY_MAU,
//                DIEM_KHO, NHA_KHO, NGAN_KHO, NGAN_LO, TRANG_THAI};
//        String filename = "Danh_sach_phieu_kiem_nghiem_chat_luong_hang.xlsx";
//
//        List<Object[]> dataList = new ArrayList<Object[]>();
//        Object[] objs = null;
//
//        try {
//            for (int i = 0; i < list.size(); i++) {
//                XhPhieuKnghiemCluongRes item = list.get(i);
//                objs = new Object[rowsName.length];
//                objs[0] = i;
//                objs[1] = item.getSoPhieu();
//                objs[2] = LocalDateTimeUtils.localDateToString(item.getNgayKnghiem());
//                objs[3] = item.getSoBbLayMau();
//                objs[4] = LocalDateTimeUtils.localDateToString(item.getNgayLayMau());
//                objs[5] = item.getTenDiemKho();
//                objs[6] = item.getTenNhaKho();
//                objs[7] = item.getTenNganKho();
//                objs[8] = item.getTenNganLo();
//                objs[9] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
//                dataList.add(objs);
//            }
//
//            ExportExcel ex = new ExportExcel(SHEET_PHIEU_KIEM_NGHIEM_CHAT_LUONG, filename, rowsName, dataList, response);
//            ex.export();
//        } catch (Exception e) {
//            log.error("Error export", e);
//            return false;
//        }
//
//        return true;
//    }
//
//    public Integer getSo() throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Integer so = xhPhieuKnghiemCluongRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
//        so = Optional.ofNullable(so).orElse(0);
//        so = so + 1;
//        return so;
//    }
//
//    private void validateSoPhieu(XhPhieuKnghiemCluong update, XhPhieuKnghiemCluongReq req) throws Exception {
//        String so = req.getSoPhieu();
//        if (!StringUtils.hasText(so))
//            return;
//        if (update == null || (StringUtils.hasText(update.getSoPhieu()) && !update.getSoPhieu().equalsIgnoreCase(so))) {
//            Optional<XhPhieuKnghiemCluong> optional = xhPhieuKnghiemCluongRepository.findFirstBySoPhieu(so);
//            Long updateId = Optional.ofNullable(update).map(XhPhieuKnghiemCluong::getId).orElse(null);
//            if (optional.isPresent() && !optional.get().getId().equals(updateId))
//                throw new Exception("Số phiếu " + so + " đã tồn tại");
//        }
//    }
//
//    private void thongTinNganLo(XhPhieuKnghiemCluongRes item, KtNganLo nganLo) {
//        if (nganLo != null) {
//            item.setTenNganLo(nganLo.getTenNganlo());
//            KtNganKho nganKho = nganLo.getParent();
//            if (nganKho == null)
//                return;
//
//            item.setTenNganKho(nganKho.getTenNgankho());
//            item.setMaNganKho(nganKho.getMaNgankho());
//            KtNhaKho nhaKho = nganKho.getParent();
//            if (nhaKho == null)
//                return;
//
//            item.setTenNhaKho(nhaKho.getTenNhakho());
//            item.setMaNhaKho(nhaKho.getMaNhakho());
//            KtDiemKho diemKho = nhaKho.getParent();
//            if (diemKho == null)
//                return;
//
//            item.setTenDiemKho(diemKho.getTenDiemkho());
//            item.setMaDiemKho(diemKho.getMaDiemkho());
//        }
//    }
//
//    @Override
//    public BaseNhapHangCount count(Set<String> maDvis) throws Exception {
//        XhPhieuKnghiemCluongSearchReq countReq = new XhPhieuKnghiemCluongSearchReq();
//        countReq.setMaDvis(maDvis);
//        BaseNhapHangCount count = new BaseNhapHangCount();
//
//        count.setVatTu(xhPhieuKnghiemCluongRepository.count(countReq));
//        return count;
//    }
