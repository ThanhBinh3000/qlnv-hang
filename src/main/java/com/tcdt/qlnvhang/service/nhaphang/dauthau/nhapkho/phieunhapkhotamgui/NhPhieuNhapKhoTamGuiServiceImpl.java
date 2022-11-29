package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.phieunhapkhotamgui;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bienbanchuanbikho.NhBienBanChuanBiKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkhotamgui.NhPhieuNhapKhoTamGui;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiCt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiCtRepository;
import com.tcdt.qlnvhang.repository.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiRepository;
import com.tcdt.qlnvhang.request.object.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiCtReq;
import com.tcdt.qlnvhang.request.object.vattu.phieunhapkhotamgui.NhPhieuNhapKhoTamGuiReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class NhPhieuNhapKhoTamGuiServiceImpl extends BaseServiceImpl implements NhPhieuNhapKhoTamGuiService {

    @Autowired
    private final NhPhieuNhapKhoTamGuiRepository nhPhieuNhapKhoTamGuiRepository;

    @Autowired
    private final NhPhieuNhapKhoTamGuiCtRepository phieuNhapKhoTamGuiCtRepository;

    @Autowired
    private final FileDinhKemService fileDinhKemService;

    @Autowired
    private final HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;

    @Autowired
    private final UserInfoRepository userInfoRepository;

    @Autowired
    private final KtNganLoRepository ktNganLoRepository;
    private static final String SHEET_PHIEU_NHAP_KHO_TAM_GUI = "Phiếu nhập kho tạm gửi";
    private static final String STT = "STT";
    private static final String SO_PHIEU = "Số Phiếu";
    private static final String SO_QUYET_DINH_NHAP = "Số Quyết Định Nhập";
    private static final String NGAY_NHAP_KHO = "Ngày Nhập Kho";
    private static final String DIEM_KHO = "Điểm Kho";
    private static final String NHA_KHO = "Nhà Kho";
    private static final String NGAN_KHO = "Ngăn Kho";
    private static final String NGAN_LO = "Ngăn Lô";
    private static final String TRANG_THAI = "Trạng Thái";

    @Override
    public Page<NhPhieuNhapKhoTamGui> searchPage(NhPhieuNhapKhoTamGuiReq req) {
        return null;
    }

    @Override
    public List<NhPhieuNhapKhoTamGui> searchAll(NhPhieuNhapKhoTamGuiReq req) {
        return null;
    }

    @Override
    public NhPhieuNhapKhoTamGui create(NhPhieuNhapKhoTamGuiReq req) throws Exception {
                UserInfo userInfo = UserUtils.getUserInfo();

        NhPhieuNhapKhoTamGui item = new NhPhieuNhapKhoTamGui();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(new Date());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setNam(LocalDate.now().getYear());
        item.setId(Long.parseLong(req.getSoPhieuNhapKhoTamGui().split("/")[0]));
        nhPhieuNhapKhoTamGuiRepository.save(item);
        this.saveCtiet(req,item.getId());
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), item.getId(), NhPhieuNhapKhoTamGui.TABLE_NAME);
        item.setFileDinhKems(fileDinhKems);
        return item;
    }

    @Transactional
    void saveCtiet(NhPhieuNhapKhoTamGuiReq req, Long id){
        phieuNhapKhoTamGuiCtRepository.deleteAllByPhieuNkTgId(id);
        for(NhPhieuNhapKhoTamGuiCtReq reqCt :req.getChildren()){
            NhPhieuNhapKhoTamGuiCt dataCt = new NhPhieuNhapKhoTamGuiCt();
            BeanUtils.copyProperties(reqCt,dataCt,"id");
            dataCt.setPhieuNkTgId(id);
            phieuNhapKhoTamGuiCtRepository.save(dataCt);
        }
    }

    @Override
    public NhPhieuNhapKhoTamGui update(NhPhieuNhapKhoTamGuiReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        Optional<NhPhieuNhapKhoTamGui> optional = nhPhieuNhapKhoTamGuiRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Phiếu nhập kho tạm gửi không tồn tại.");
        }

        NhPhieuNhapKhoTamGui item = optional.get();
        if(!Objects.equals(item.getNguoiTaoId(), userInfo.getId())){
            throw new Exception("Bạn không có quyền thao tác trên dữ liệu này");
        }
        BeanUtils.copyProperties(req, item, "id");
        item.setNgaySua(new Date());
        item.setNguoiSuaId(userInfo.getId());
        nhPhieuNhapKhoTamGuiRepository.save(item);
        this.saveCtiet(req,item.getId());
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), item.getId(), NhPhieuNhapKhoTamGui.TABLE_NAME);
        item.setFileDinhKems(fileDinhKems);
        return item;
    }

    @Override
    public NhPhieuNhapKhoTamGui detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhPhieuNhapKhoTamGui> optional = nhPhieuNhapKhoTamGuiRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Phiếu nhập kho tạm gửi không tồn tại.");
        }
        NhPhieuNhapKhoTamGui item = optional.get();
        Map<String, String> listDanhMucHangHoa = getListDanhMucHangHoa();
        Map<String, String> listDanhMucDvi = getListDanhMucDvi(null, null, "01");
        item.setTenDvi(listDanhMucDvi.get(item.getMaDvi()));
        item.setTenDiemKho(listDanhMucDvi.get(item.getMaDiemKho()));
        item.setTenNhaKho(listDanhMucDvi.get(item.getMaNhaKho()));
        item.setTenNganKho(listDanhMucDvi.get(item.getMaNganKho()));
        item.setTenLoKho(listDanhMucDvi.get(item.getMaLoKho()));
        item.setTenNguoiTao(ObjectUtils.isEmpty(item.getNguoiTaoId()) ? null : userInfoRepository.findById(item.getNguoiTaoId()).get().getFullName());
        item.setTenLoaiVthh(listDanhMucHangHoa.get(item.getLoaiVthh()));
        item.setTenCloaiVthh(listDanhMucHangHoa.get(item.getCloaiVthh()));
        item.setChildren(phieuNhapKhoTamGuiCtRepository.findByPhieuNkTgId(item.getId()));
        item.setFileDinhKems(fileDinhKemService.search(item.getId(), Collections.singleton(NhPhieuNhapKhoTamGui.TABLE_NAME)));
        return item;
    }

    @Override
    public NhPhieuNhapKhoTamGui approve(NhPhieuNhapKhoTamGuiReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<NhPhieuNhapKhoTamGui> optional = nhPhieuNhapKhoTamGuiRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu.");
        }

        NhPhieuNhapKhoTamGui item = optional.get();
        String trangThai = req.getTrangThai() + item.getTrangThai();
        if (
            (NhapXuatHangTrangThaiEnum.DA_HOAN_THANH.getId() + NhapXuatHangTrangThaiEnum.DUTHAO.getId()).equals(trangThai)
        ) {
            item.setNguoiPduyetId(userInfo.getId());
            item.setNgayPduyet(new Date());
        }else{
            throw new Exception("Phê duyệt không thành công");
        }
        item.setTrangThai(req.getTrangThai());
        nhPhieuNhapKhoTamGuiRepository.save(item);
        return item;
    }

    @Override
    public void delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhPhieuNhapKhoTamGui> optional = nhPhieuNhapKhoTamGuiRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Phiếu nhập kho tạm gửi không tồn tại.");
        }

        NhPhieuNhapKhoTamGui item = optional.get();
        if(!Objects.equals(item.getNguoiTaoId(), userInfo.getId())){
            throw new Exception("Bạn không có quyền thao tác trên dữ liệu này");
        }
        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa bảng kê đã đã duyệt");
        }

        phieuNhapKhoTamGuiCtRepository.deleteByPhieuNkTgIdIn(Collections.singleton(item.getId()));
        nhPhieuNhapKhoTamGuiRepository.delete(item);
    }

    @Override
    public void deleteMulti(List<Long> listMulti) {

    }

    @Override
    public boolean export(NhPhieuNhapKhoTamGuiReq req) throws Exception {
        return false;
    }


//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public NhPhieuNhapKhoTamGuiRes create(NhPhieuNhapKhoTamGuiReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.validateSoPhieu(null, req);
//
//        NhPhieuNhapKhoTamGui item = new NhPhieuNhapKhoTamGui();
//        BeanUtils.copyProperties(req, item, "id");
//        item.setNgayTao(new Date());
//        item.setNguoiTaoId(userInfo.getId());
//        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
//        item.setMaDvi(userInfo.getDvql());
//        item.setCapDvi(userInfo.getCapDvi());
//        item.setSo(getSo());
//        item.setNam(LocalDate.now().getYear());
//        item.setSoPhieu(String.format("%s/%s/%s-%s", item.getSo(), item.getNam(), "PTG", userInfo.getMaPbb()));
//        nhPhieuNhapKhoTamGuiRepository.save(item);
//
//        List<NhPhieuNhapKhoTamGuiCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), new HashMap<>());
//        item.setChiTiets(chiTiets);
//        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), item.getId(), NhPhieuNhapKhoTamGui.TABLE_NAME);
//        item.setFileDinhKems(fileDinhKems);
//
//        return this.buildResponse(item);
//    }
//
//    private List<NhPhieuNhapKhoTamGuiCt> saveListChiTiet(Long parentId,
//                                                      List<NhPhieuNhapKhoTamGuiCtReq> chiTietReqs,
//                                                      Map<Long, NhPhieuNhapKhoTamGuiCt> mapChiTiet) throws Exception {
//        List<NhPhieuNhapKhoTamGuiCt> chiTiets = new ArrayList<>();
//        for (NhPhieuNhapKhoTamGuiCtReq req : chiTietReqs) {
//            Long id = req.getId();
//            NhPhieuNhapKhoTamGuiCt chiTiet = new NhPhieuNhapKhoTamGuiCt();
//
//            if (id != null && id > 0) {
//                chiTiet = mapChiTiet.get(id);
//                if (chiTiet == null)
//                    throw new Exception("Phiếu nhập kho tạm gửi chi tiết không tồn tại.");
//                mapChiTiet.remove(id);
//            }
//
//            BeanUtils.copyProperties(req, chiTiet, "id");
//            chiTiet.setPhieuNkTgId(parentId);
//            chiTiets.add(chiTiet);
//        }
//
//        if (!CollectionUtils.isEmpty(chiTiets))
//            phieuNhapKhoTamGuiCtRepository.saveAll(chiTiets);
//
//        return chiTiets;
//    }
//
//
//    private NhPhieuNhapKhoTamGuiRes buildResponse(NhPhieuNhapKhoTamGui item) throws Exception {
//        NhPhieuNhapKhoTamGuiRes res = new NhPhieuNhapKhoTamGuiRes();
//        List<NhPhieuNhapKhoTamGuiCtRes> chiTiets = new ArrayList<>();
//        BeanUtils.copyProperties(item, res);
//        res.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//        res.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//        for (NhPhieuNhapKhoTamGuiCt phieuNhapKhoTamGuiCt : item.getChiTiets()) {
//            chiTiets.add(new NhPhieuNhapKhoTamGuiCtRes(phieuNhapKhoTamGuiCt));
//        }
//        res.setChiTiets(chiTiets);
//
//        if (item.getQdgnvnxId() != null) {
//            Optional<HhQdGiaoNvuNhapxuatHdr> qdNhap = hhQdGiaoNvuNhapxuatRepository.findById(item.getQdgnvnxId());
//            if (!qdNhap.isPresent()) {
//                throw new Exception("Không tìm thấy quyết định nhập");
//            }
//            res.setSoQuyetDinhNhap(qdNhap.get().getSoQd());
//        }
//        res.setFileDinhKems(item.getFileDinhKems());
//        KtNganLo nganLo = null;
//        if (StringUtils.hasText(item.getMaNganLo())) {
//            nganLo = ktNganLoRepository.findFirstByMaNganlo(item.getMaNganLo());
//        }
//
//        this.thongTinNganLo(res, nganLo);
//        return res;
//    }
//
//    @Transactional(rollbackOn = Exception.class)
//    @Override
//    public NhPhieuNhapKhoTamGuiRes update(NhPhieuNhapKhoTamGuiReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        Optional<NhPhieuNhapKhoTamGui> optional = nhPhieuNhapKhoTamGuiRepository.findById(req.getId());
//        if (!optional.isPresent())
//            throw new Exception("Phiếu nhập kho tạm gửi không tồn tại.");
//
//        this.validateSoPhieu(optional.get(), req);
//
//        NhPhieuNhapKhoTamGui item = optional.get();
//        BeanUtils.copyProperties(req, item, "id");
//        item.setNgaySua(new Date());
//        item.setNguoiSuaId(userInfo.getId());
//
//        nhPhieuNhapKhoTamGuiRepository.save(item);
//        Map<Long, NhPhieuNhapKhoTamGuiCt> mapChiTiet = phieuNhapKhoTamGuiCtRepository.findByPhieuNkTgIdIn(Collections.singleton(item.getId()))
//                .stream().collect(Collectors.toMap(NhPhieuNhapKhoTamGuiCt::getId, Function.identity()));
//
//        List<NhPhieuNhapKhoTamGuiCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), mapChiTiet);
//        item.setChiTiets(chiTiets);
//        if (!CollectionUtils.isEmpty(mapChiTiet.values()))
//            phieuNhapKhoTamGuiCtRepository.deleteAll(mapChiTiet.values());
//
//        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), item.getId(), NhPhieuNhapKhoTamGui.TABLE_NAME);
//        item.setFileDinhKems(fileDinhKems);
//        return this.buildResponse(item);
//    }
//
//    @Override
//    public NhPhieuNhapKhoTamGuiRes detail(Long id) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<NhPhieuNhapKhoTamGui> optional = nhPhieuNhapKhoTamGuiRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Phiếu nhập kho tạm gửi không tồn tại.");
//
//        NhPhieuNhapKhoTamGui item = optional.get();
//        item.setChiTiets(phieuNhapKhoTamGuiCtRepository.findByPhieuNkTgIdIn(Collections.singleton(item.getId())));
//        item.setFileDinhKems(fileDinhKemService.search(item.getId(), Collections.singleton(NhPhieuNhapKhoTamGui.TABLE_NAME)));
//        return this.buildResponse(item);
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean delete(Long id) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<NhPhieuNhapKhoTamGui> optional = nhPhieuNhapKhoTamGuiRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Phiếu nhập kho tạm gửi không tồn tại.");
//
//        NhPhieuNhapKhoTamGui item = optional.get();
//        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
//            throw new Exception("Không thể xóa bảng kê đã đã duyệt");
//        }
//        phieuNhapKhoTamGuiCtRepository.deleteByPhieuNkTgIdIn(Collections.singleton(item.getId()));
//        nhPhieuNhapKhoTamGuiRepository.delete(item);
//        return true;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean updateStatusQd(StatusReq stReq) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<NhPhieuNhapKhoTamGui> optional = nhPhieuNhapKhoTamGuiRepository.findById(stReq.getId());
//        if (!optional.isPresent())
//            throw new Exception("Phiếu nhập kho tạm gửi không tồn tại.");
//
//        NhPhieuNhapKhoTamGui item = optional.get();
//        String trangThai = item.getTrangThai();
//
//        if (NhapXuatHangTrangThaiEnum.DAKY.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
//                return false;
//
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.DAKY.getId());
//            item.setNguoiPduyetId(userInfo.getId());
//            item.setNgayPduyet(new Date());
//        } else {
//            throw new Exception("Bad request.");
//        }
//        nhPhieuNhapKhoTamGuiRepository.save(item);
//
//        return true;
//    }
//
//    @Override
//    public Page<NhPhieuNhapKhoTamGuiRes> search(NhPhieuNhapKhoTamGuiSearchReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
//        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//        List<Object[]> data = nhPhieuNhapKhoTamGuiRepository.search(req);
//        List<NhPhieuNhapKhoTamGuiRes> responses = new ArrayList<>();
//        for (Object[] o : data) {
//            NhPhieuNhapKhoTamGuiRes response = new NhPhieuNhapKhoTamGuiRes();
//            NhPhieuNhapKhoTamGui item = (NhPhieuNhapKhoTamGui) o[0];
//            Long qdNhapId = (Long) o[1];
//            String soQdNhap = (String) o[2];
//            KtNganLo nganLo = o[3] != null ? (KtNganLo) o[3] : null;
//            BeanUtils.copyProperties(item, response);
//            response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//            response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//            response.setQdgnvnxId(qdNhapId);
//            response.setSoQuyetDinhNhap(soQdNhap);
//            this.thongTinNganLo(response, nganLo);
//            responses.add(response);
//        }
//
//        return new PageImpl<>(responses, pageable, nhPhieuNhapKhoTamGuiRepository.count(req));
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean deleteMultiple(DeleteReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        phieuNhapKhoTamGuiCtRepository.deleteByPhieuNkTgIdIn(req.getIds());
//        nhPhieuNhapKhoTamGuiRepository.deleteByIdIn(req.getIds());
//        return true;
//    }
//
//    @Override
//    public boolean exportToExcel(NhPhieuNhapKhoTamGuiSearchReq objReq, HttpServletResponse response) throws Exception {
//
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
//        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
//        List<NhPhieuNhapKhoTamGuiRes> list = this.search(objReq).get().collect(Collectors.toList());
//
//        if (CollectionUtils.isEmpty(list))
//            return true;
//
//        String[] rowsName = new String[] { STT, SO_PHIEU, SO_QUYET_DINH_NHAP, NGAY_NHAP_KHO,
//                DIEM_KHO, NHA_KHO, NGAN_KHO, NGAN_LO, TRANG_THAI};
//        String filename = "Danh_sach_ho_so_ky_thuat.xlsx";
//
//        List<Object[]> dataList = new ArrayList<Object[]>();
//        Object[] objs = null;
//
//        try {
//            for (int i = 0; i < list.size(); i++) {
//                NhPhieuNhapKhoTamGuiRes item = list.get(i);
//                objs = new Object[rowsName.length];
//                objs[0] = i;
//                objs[1] = item.getSoPhieu();
//                objs[2] = item.getSoQuyetDinhNhap();
//                objs[3] = LocalDateTimeUtils.localDateToString(item.getNgayNhapKho());
//                objs[4] = item.getTenDiemKho();
//                objs[5] = item.getTenNhaKho();
//                objs[6] = item.getTenNganKho();
//                objs[7] = item.getTenNganLo();
//                objs[8] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
//                dataList.add(objs);
//            }
//
//            ExportExcel ex = new ExportExcel(SHEET_PHIEU_NHAP_KHO_TAM_GUI, filename, rowsName, dataList, response);
//            ex.export();
//        } catch (Exception e) {
//            log.error("Error export", e);
//            return false;
//        }
//
//        return true;
//    }
//
//    private void thongTinNganLo(NhPhieuNhapKhoTamGuiRes item, KtNganLo nganLo) {
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
//    private void validateSoPhieu(NhPhieuNhapKhoTamGui update, NhPhieuNhapKhoTamGuiReq req) throws Exception {
//        String so = req.getSoPhieu();
//        if (!StringUtils.hasText(so))
//            return;
//        if (update == null || (StringUtils.hasText(update.getSoPhieu()) && !update.getSoPhieu().equalsIgnoreCase(so))) {
//            Optional<NhPhieuNhapKhoTamGui> optional = nhPhieuNhapKhoTamGuiRepository.findFirstBySoPhieu(so);
//            Long updateId = Optional.ofNullable(update).map(NhPhieuNhapKhoTamGui::getId).orElse(null);
//            if (optional.isPresent() && !optional.get().getId().equals(updateId))
//                throw new Exception("Số phiếu " + so + " đã tồn tại");
//        }
//    }
//
//    @Override
//    public Integer getSo() throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Integer so = nhPhieuNhapKhoTamGuiRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
//        so = Optional.ofNullable(so).orElse(0);
//        so = so + 1;
//        return so;
//    }
//
//    @Override
//    public BaseNhapHangCount count(Set<String> maDvis) throws Exception {
//        NhPhieuNhapKhoTamGuiSearchReq countReq = new NhPhieuNhapKhoTamGuiSearchReq();
//        countReq.setMaDvis(maDvis);
//        BaseNhapHangCount count = new BaseNhapHangCount();
//
//        count.setVatTu(nhPhieuNhapKhoTamGuiRepository.count(countReq));
//        return count;
//    }
}
