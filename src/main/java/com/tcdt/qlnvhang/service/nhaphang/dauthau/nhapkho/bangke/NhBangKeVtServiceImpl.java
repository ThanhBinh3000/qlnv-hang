package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bangke;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangke.NhBangKeVt;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangke.NhBangKeVtCt;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKho;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.vattu.bangke.NhBangKeVtCtRepository;
import com.tcdt.qlnvhang.repository.vattu.bangke.NhBangKeVtRepository;
import com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.nhapkho.NhBangKeVtPreview;
import com.tcdt.qlnvhang.request.object.bbanlaymau.BienBanLayMauReq;
import com.tcdt.qlnvhang.request.object.vattu.bangke.NhBangKeVtCtReq;
import com.tcdt.qlnvhang.request.object.vattu.bangke.NhBangKeVtReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.UserUtils;
import fr.opensagres.xdocreport.core.XDocReportException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class NhBangKeVtServiceImpl extends BaseServiceImpl implements NhBangKeVtService {
    @Autowired
    private final NhBangKeVtRepository bangKeVtRepository;

    @Autowired
    private final NhBangKeVtCtRepository bangKeVtCtRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;


    @Override
    public Page<NhBangKeVt> searchPage(NhBangKeVtReq req) {
        return null;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public NhBangKeVt create(NhBangKeVtReq req) throws Exception {
        if (req == null){
            return null;
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");

        }

        NhBangKeVt item = new NhBangKeVt();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(new Date());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setId(Long.parseLong(item.getSoBangKe().split("/")[0]));
        bangKeVtRepository.save(item);

        this.saveCtiet(item.getId(),req);

        return item;
    }
    @Transactional
    void saveCtiet(Long idHdr, NhBangKeVtReq req){
        bangKeVtCtRepository.deleteByBangKeVtId(idHdr);
        for(NhBangKeVtCtReq objCtiet : req.getChildren()){
            NhBangKeVtCt ctiet = new NhBangKeVtCt();
            BeanUtils.copyProperties(objCtiet,ctiet,"id");
            ctiet.setBangKeVtId(idHdr);
            bangKeVtCtRepository.save(ctiet);
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public NhBangKeVt update(NhBangKeVtReq req) throws Exception {
        if (req == null){
            return null;
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        Optional<NhBangKeVt> optional = bangKeVtRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Bảng kê không tồn tại.");
        }

        NhBangKeVt item = optional.get();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgaySua(new Date());
        item.setNguoiSuaId(userInfo.getId());
        bangKeVtRepository.save(item);
        this.saveCtiet(item.getId(),req);
        return item;
    }

    @Override
    public NhBangKeVt detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        Optional<NhBangKeVt> optional = bangKeVtRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Bảng kê không tồn tại.");

        NhBangKeVt item = optional.get();
        Map<String, String> listDanhMucDvi = getListDanhMucDvi("", "", "01");
        Map<String, String> listDanhMucHangHoa = getListDanhMucHangHoa();
        item.setChildren(bangKeVtCtRepository.findByBangKeVtId(item.getId()));
        item.setTenDvi(listDanhMucDvi.get(item.getMaDvi()));
        item.setTenLoaiVthh(listDanhMucHangHoa.get(item.getLoaiVthh()));
        item.setTenCloaiVthh(listDanhMucHangHoa.get(item.getCloaiVthh()));
        item.setTenDvi(listDanhMucDvi.get(item.getMaDvi()));
        item.setTenDiemKho(listDanhMucDvi.get(item.getMaDiemKho()));
        item.setTenNhaKho(listDanhMucDvi.get(item.getMaNhaKho()));
        item.setTenNganKho(listDanhMucDvi.get(item.getMaNganKho()));
        item.setTenLoKho(listDanhMucDvi.get(item.getMaLoKho()));
        item.setTenNguoiTao(ObjectUtils.isEmpty(item.getNguoiTaoId()) ? "" : userInfoRepository.findById(item.getNguoiTaoId()).get().getFullName());
        item.setTenTruongPhong(ObjectUtils.isEmpty(item.getIdTruongPhong()) ? null : userInfoRepository.findById(item.getIdTruongPhong()).get().getFullName());
        item.setTenNguoiPduyet(ObjectUtils.isEmpty(item.getNguoiPduyetId()) ? "" :userInfoRepository.findById(item.getNguoiPduyetId()).get().getFullName());
        return item;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public NhBangKeVt approve(NhBangKeVtReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
            throw new Exception("Bad Request");
        }

        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        Optional<NhBangKeVt> optional = bangKeVtRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        NhBangKeVt item = optional.get();

        String trangThai = req.getTrangThai() + item.getTrangThai();
        if (
            (TrangThaiAllEnum.CHODUYET_TBP_TVQT.getId() + NhapXuatHangTrangThaiEnum.DUTHAO.getId()).equals(trangThai) ||
            (TrangThaiAllEnum.CHODUYET_TBP_TVQT.getId() + TrangThaiAllEnum.TUCHOI_TBP_TVQT.getId()).equals(trangThai) ||
            (TrangThaiAllEnum.CHODUYET_TBP_TVQT.getId() + NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId()).equals(trangThai)
        ) {
            item.setNguoiGuiDuyetId(userInfo.getId());
            item.setNgayGuiDuyet(new Date());
        } else if (
            (NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId() + TrangThaiAllEnum.CHODUYET_TBP_TVQT.getId()).equals(trangThai) ||
            (TrangThaiAllEnum.TUCHOI_TBP_TVQT.getId() + TrangThaiAllEnum.CHODUYET_TBP_TVQT.getId()).equals(trangThai)
        ) {
            item.setIdTruongPhong(userInfo.getId());
            item.setLyDoTuChoi(req.getLyDoTuChoi());
        } else if (
            (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId()).equals(trangThai) ||
            (NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId()).equals(trangThai)
        ) {
            item.setNgayPduyet(new Date());
            item.setNguoiPduyetId(userInfo.getId());
            item.setLyDoTuChoi(req.getLyDoTuChoi());
        } else {
            throw new Exception("Phê duyệt không thành công");
        }
        item.setTrangThai(req.getTrangThai());
        bangKeVtRepository.save(item);
        return item;
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<NhBangKeVt> optional = bangKeVtRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        NhBangKeVt data = optional.get();
        List<NhBangKeVtCt> dtlList = bangKeVtCtRepository.findByBangKeVtId(data.getId());
        bangKeVtCtRepository.deleteAll(dtlList);
        bangKeVtRepository.delete(data);
    }

    @Override
    public void deleteMulti(List<Long> listMulti) {

    }

    @Override
    public void export(NhBangKeVtReq req, HttpServletResponse response) throws Exception {
//        return false;
    }

    @Override
    public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
        try {
            String fileName = DataUtils.safeToString(body.get("tenBaoCao"));
            String fileTemplate = "nhapdauthau/nhapkho/" + fileName;
            FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
            NhBangKeVt detail  = this.detail(DataUtils.safeToLong(body.get("id")));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }

//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public NhBangKeVtRes create(NhBangKeVtReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.validateSoPhieu(null, req);
//
//        NhBangKeVt item = new NhBangKeVt();
//        BeanUtils.copyProperties(req, item, "id");
//        item.setNgayTao(new Date());
//        item.setNguoiTaoId(userInfo.getId());
//        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
//        item.setMaDvi(userInfo.getDvql());
//        item.setCapDvi(userInfo.getCapDvi());
//        item.setSo(getSo());
//        item.setNam(LocalDate.now().getYear());
//        item.setSoBangKe(String.format("%s/%s/%s-%s", item.getSo(), item.getNam(), "BKNVT", userInfo.getMaPbb()));
//        bangKeVtRepository.save(item);
//
//        List<NhBangKeVtCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), new HashMap<>());
//        item.setChiTiets(chiTiets);
//
//        return this.buildResponse(item);
//    }
//
//    private List<NhBangKeVtCt> saveListChiTiet(Long parentId,
//                                               List<NhBangKeVtCtReq> chiTietReqs,
//                                               Map<Long, NhBangKeVtCt> mapChiTiet) throws Exception {
//        List<NhBangKeVtCt> chiTiets = new ArrayList<>();
//        for (NhBangKeVtCtReq req : chiTietReqs) {
//            Long id = req.getId();
//            NhBangKeVtCt chiTiet = new NhBangKeVtCt();
//
//            if (id != null && id > 0) {
//                chiTiet = mapChiTiet.get(id);
//                if (chiTiet == null)
//                    throw new Exception("Bảng kê vật tư chi tiết không tồn tại.");
//                mapChiTiet.remove(id);
//            }
//
//            BeanUtils.copyProperties(req, chiTiet, "id");
//            chiTiet.setBangKeVtId(parentId);
//            chiTiets.add(chiTiet);
//        }
//
//        if (!CollectionUtils.isEmpty(chiTiets))
//            bangKeVtCtRepository.saveAll(chiTiets);
//
//        return chiTiets;
//    }
//
//
//    private NhBangKeVtRes buildResponse(NhBangKeVt item) throws Exception {
//        NhBangKeVtRes res = new NhBangKeVtRes();
//        List<NhBangKeVtCtRes> chiTiets = new ArrayList<>();
//        BeanUtils.copyProperties(item, res);
//        res.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//        res.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//        for (NhBangKeVtCt ct : item.getChiTiets()) {
//            chiTiets.add(new NhBangKeVtCtRes(ct));
//        }
//        res.setChiTiets(chiTiets);
//
//        Set<String> maVatTus = Stream.of(item.getMaVatTu(), item.getMaVatTuCha()).filter(Objects::nonNull).collect(Collectors.toSet());
//        Set<QlnvDmVattu> vatTus = Sets.newHashSet(qlnvDmVattuRepository.findByMaIn(maVatTus));
//
//        if (CollectionUtils.isEmpty(vatTus))
//            throw new Exception("Không tìm thấy vật tư");
//
//        vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(item.getMaVatTu())).findFirst()
//                .ifPresent(v -> res.setTenVatTu(v.getTen()));
//        vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(item.getMaVatTuCha())).findFirst()
//                .ifPresent(v -> res.setTenVatTuCha(v.getTen()));
//
//        if (item.getQdgnvnxId() != null) {
//            Optional<HhQdGiaoNvuNhapxuatHdr> qdNhap = hhQdGiaoNvuNhapxuatRepository.findById(item.getQdgnvnxId());
//            if (!qdNhap.isPresent()) {
//                throw new Exception("Không tìm thấy quyết định nhập");
//            }
//            res.setSoQuyetDinhNhap(qdNhap.get().getSoQd());
//        }
//
//        if (item.getPhieuNhapKhoId() != null) {
//            Optional<NhPhieuNhapKho> phieuNhapKho = phieuNhapKhoRepository.findById(item.getPhieuNhapKhoId());
//            if (!phieuNhapKho.isPresent()) {
//                throw new Exception("Không tìm thấy Bảng kê");
//            }
////            res.setSoPhieuNhapKho(phieuNhapKho.get().getSoPhieu());
//        }
//
//        if (item.getHopDongId() != null) {
//            Optional<HhHopDongHdr> qOpHdong = hhHopDongRepository.findById(item.getHopDongId());
//            if (!qOpHdong.isPresent())
//                throw new Exception("Hợp đồng không tồn tại");
//
//            res.setSoHopDong(qOpHdong.get().getSoHd());
//        }
//
//        return res;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public NhBangKeVtRes update(NhBangKeVtReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        Optional<NhBangKeVt> optional = bangKeVtRepository.findById(req.getId());
//        if (!optional.isPresent())
//            throw new Exception("Bảng kê vật tư không tồn tại.");
//
//        this.validateSoPhieu(optional.get(), req);
//
//        NhBangKeVt item = optional.get();
//        BeanUtils.copyProperties(req, item, "id", "so", "nam");
//        item.setNgaySua(new Date());
//        item.setNguoiSuaId(userInfo.getId());
//
//        bangKeVtRepository.save(item);
//        Map<Long, NhBangKeVtCt> mapChiTiet = bangKeVtCtRepository.findByBangKeVtIdIn(Collections.singleton(item.getId()))
//                .stream().collect(Collectors.toMap(NhBangKeVtCt::getId, Function.identity()));
//
//        List<NhBangKeVtCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), mapChiTiet);
//        item.setChiTiets(chiTiets);
//
//        if (!CollectionUtils.isEmpty(mapChiTiet.values()))
//            bangKeVtCtRepository.deleteAll(mapChiTiet.values());
//
//
//        return this.buildResponse(item);
//    }
//
//    @Override
//    public NhBangKeVtRes detail(Long id) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<NhBangKeVt> optional = bangKeVtRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Bảng kê vật tư không tồn tại.");
//
//        NhBangKeVt item = optional.get();
//        item.setChiTiets(bangKeVtCtRepository.findByBangKeVtIdIn(Collections.singleton(item.getId())));
//        return this.buildResponse(item);
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean delete(Long id) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<NhBangKeVt> optional = bangKeVtRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Bảng kê vật tư không tồn tại.");
//
//        NhBangKeVt item = optional.get();
//        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
//            throw new Exception("Không thể xóa bảng kê đã đã duyệt");
//        }
//        bangKeVtCtRepository.deleteByBangKeVtIdIn(Collections.singleton(item.getId()));
//        bangKeVtRepository.delete(item);
//        return true;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean updateStatusQd(StatusReq stReq) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<NhBangKeVt> optional = bangKeVtRepository.findById(stReq.getId());
//        if (!optional.isPresent())
//            throw new Exception("Bảng kê vật tư không tồn tại.");
//
//        NhBangKeVt item = optional.get();
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
//    public Page<NhBangKeVtRes> search(NhBangKeVtSearchReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
//        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//        List<Object[]> data = bangKeVtRepository.search(req);
//        List<NhBangKeVtRes> responses = new ArrayList<>();
//        for (Object[] o : data) {
//            NhBangKeVtRes response = new NhBangKeVtRes();
//            NhBangKeVt item = (NhBangKeVt) o[0];
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
//        return new PageImpl<>(responses, pageable, bangKeVtRepository.count(req));
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean deleteMultiple(DeleteReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        bangKeVtCtRepository.deleteByBangKeVtIdIn(req.getIds());
//        bangKeVtRepository.deleteByIdIn(req.getIds());
//        return true;
//    }
//
//    @Override
//    public boolean exportToExcel(NhBangKeVtSearchReq objReq, HttpServletResponse response) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
//        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
//        List<NhBangKeVtRes> list = this.search(objReq).get().collect(Collectors.toList());
//
//        if (CollectionUtils.isEmpty(list))
//            return true;
//
//        String[] rowsName = new String[] { STT, SO_BANG_KE, SO_QUYET_DINH_NHAP, NGAY_TAO_BANG_KE,
//                DIEM_KHO, NHA_KHO, NGAN_KHO, NGAN_LO, TRANG_THAI};
//        String filename = "Danh_sach_bang_ke_nhap_vat_tu.xlsx";
//
//        List<Object[]> dataList = new ArrayList<Object[]>();
//        Object[] objs = null;
//
//        try {
//            for (int i = 0; i < list.size(); i++) {
//                NhBangKeVtRes item = list.get(i);
//                objs = new Object[rowsName.length];
//                objs[0] = i;
//                objs[1] = item.getSoBangKe();
//                objs[2] = item.getSoQuyetDinhNhap();
//                objs[3] = LocalDateTimeUtils.localDateToString(item.getNgayTaoBangKe());
//                objs[4] = item.getTenDiemKho();
//                objs[5] = item.getTenNhaKho();
//                objs[6] = item.getTenNganKho();
//                objs[7] = item.getTenNganLo();
//                objs[8] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
//                dataList.add(objs);
//            }
//
//            ExportExcel ex = new ExportExcel(SHEET_BANG_KE_NHAP_VAT_TU, filename, rowsName, dataList, response);
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
//        Integer so = bangKeVtRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
//        so = Optional.ofNullable(so).orElse(0);
//        so = so + 1;
//        return so;
//    }
//
//    private void validateSoPhieu(NhBangKeVt update, NhBangKeVtReq req) throws Exception {
//        String so = req.getSoBangKe();
//        if (!StringUtils.hasText(so))
//            return;
//        if (update == null || (StringUtils.hasText(update.getSoBangKe()) && !update.getSoBangKe().equalsIgnoreCase(so))) {
//            Optional<NhBangKeVt> optional = bangKeVtRepository.findFirstBySoBangKe(so);
//            Long updateId = Optional.ofNullable(update).map(NhBangKeVt::getId).orElse(null);
//            if (optional.isPresent() && !optional.get().getId().equals(updateId))
//                throw new Exception("Số bảng kê " + so + " đã tồn tại");
//        }
//    }
//
//    private void thongTinNganLo(NhBangKeVtRes item, KtNganLo nganLo) {
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
//        NhBangKeVtSearchReq countReq = new NhBangKeVtSearchReq();
//        countReq.setMaDvis(maDvis);
//        BaseNhapHangCount count = new BaseNhapHangCount();
//
//        count.setVatTu(bangKeVtRepository.count(countReq));
//        return count;
//    }
}
