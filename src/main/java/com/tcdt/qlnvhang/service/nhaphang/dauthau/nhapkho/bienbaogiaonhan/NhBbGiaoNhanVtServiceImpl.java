package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bienbaogiaonhan;


import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbangiaonhan.NhBbGiaoNhanVt;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbangiaonhan.NhBbGiaoNhanVtCt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.vattu.bienbangiaonhan.NhBbGiaoNhanVtCtRepository;
import com.tcdt.qlnvhang.repository.vattu.bienbangiaonhan.NhBbGiaoNhanVtRepository;
import com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.nhapkho.NhBbGiaoNhanVtPreview;
import com.tcdt.qlnvhang.request.object.vattu.bienbangiaonhan.NhBbGiaoNhanVtCtReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbangiaonhan.NhBbGiaoNhanVtReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.UserUtils;
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
import java.time.LocalDate;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class NhBbGiaoNhanVtServiceImpl extends BaseServiceImpl implements NhBbGiaoNhanVtService {
    @Autowired
    private final NhBbGiaoNhanVtRepository nhBbGiaoNhanVtRepository;

    @Autowired
    private final NhBbGiaoNhanVtCtRepository nhBbGiaoNhanVtCtRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;


    @Override
    public Page<NhBbGiaoNhanVt> searchPage(NhBbGiaoNhanVtReq req) {
        return null;
    }

    @Override
    public NhBbGiaoNhanVt create(NhBbGiaoNhanVtReq req) throws Exception {
        if (req == null){
            return null;
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");

        }
        NhBbGiaoNhanVt item = new NhBbGiaoNhanVt();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(new Date());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setNam(LocalDate.now().getYear());
        item.setId(Long.valueOf(item.getSoBbGiaoNhan().split("/")[0]));

        nhBbGiaoNhanVtRepository.save(item);

        this.saveCtiet(item.getId(),req);

        return item;
    }

    @Transactional
    void saveCtiet(Long idHdr, NhBbGiaoNhanVtReq req){
        nhBbGiaoNhanVtCtRepository.deleteByBbGiaoNhanVtId(idHdr);
        for(NhBbGiaoNhanVtCtReq objCtiet : req.getChiTiets()){
            NhBbGiaoNhanVtCt ctiet = new NhBbGiaoNhanVtCt();
            BeanUtils.copyProperties(objCtiet,ctiet,"id");
            ctiet.setBbGiaoNhanVtId(idHdr);
            nhBbGiaoNhanVtCtRepository.save(ctiet);
        }
    }

    @Override
    public NhBbGiaoNhanVt update(NhBbGiaoNhanVtReq req) throws Exception {
        if (req == null){
            return null;
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        Optional<NhBbGiaoNhanVt> optional = nhBbGiaoNhanVtRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Bảng kê không tồn tại.");
        }

        NhBbGiaoNhanVt item = optional.get();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgaySua(new Date());
        item.setNguoiSuaId(userInfo.getId());
        nhBbGiaoNhanVtRepository.save(item);
        this.saveCtiet(item.getId(),req);
        return item;
    }

    @Override
    public NhBbGiaoNhanVt detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        Optional<NhBbGiaoNhanVt> optional = nhBbGiaoNhanVtRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Bảng kê không tồn tại.");

        NhBbGiaoNhanVt item = optional.get();
        Map<String, String> listDanhMucDvi = getListDanhMucDvi("", "", "01");

        item.setChiTiets(nhBbGiaoNhanVtCtRepository.findByBbGiaoNhanVtId(item.getId()));
        item.setTenDvi(listDanhMucDvi.get(item.getMaDvi()));
        item.setTenNguoiTao(ObjectUtils.isEmpty(item.getNguoiTaoId()) ? "" : userInfoRepository.findById(item.getNguoiTaoId()).get().getFullName());
        item.setTenNguoiPduyet(ObjectUtils.isEmpty(item.getNguoiPduyetId()) ? "" :userInfoRepository.findById(item.getNguoiPduyetId()).get().getFullName());
        return item;
    }

    @Override
    public NhBbGiaoNhanVt approve(NhBbGiaoNhanVtReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        if (!Contains.CAP_CUC.equals(userInfo.getCapDvi())){
            throw new Exception("Bad Request");
        }

        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        Optional<NhBbGiaoNhanVt> optional = nhBbGiaoNhanVtRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        NhBbGiaoNhanVt phieu = optional.get();

        String status = req.getTrangThai() + phieu.getTrangThai();
        if (
            (NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId() + NhapXuatHangTrangThaiEnum.DUTHAO.getId()).equals(status) ||
            (NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId() + NhapXuatHangTrangThaiEnum.TUCHOI_LDC.getId()).equals(status)
        ) {
            phieu.setNguoiGuiDuyetId(userInfo.getId());
            phieu.setNgayGuiDuyet(new Date());
        } else if (
            (NhapXuatHangTrangThaiEnum.DADUYET_LDC.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId()).equals(status) ||
            (NhapXuatHangTrangThaiEnum.TUCHOI_LDC.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId()).equals(status)
        ) {
            phieu.setNgayPduyet(new Date());
            phieu.setNguoiPduyetId(userInfo.getId());
            phieu.setLyDoTuChoi(req.getLyDoTuChoi());
        } else {
            throw new Exception("Phê duyệt không thành công");
        }
        phieu.setTrangThai(req.getTrangThai());
        nhBbGiaoNhanVtRepository.save(phieu);
        return phieu;
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<NhBbGiaoNhanVt> optional = nhBbGiaoNhanVtRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Bản ghi không tồn tại");
        }
        NhBbGiaoNhanVt data = optional.get();
        List<NhBbGiaoNhanVtCt> dtlList = nhBbGiaoNhanVtCtRepository.findByBbGiaoNhanVtId(data.getId());
        nhBbGiaoNhanVtCtRepository.deleteAll(dtlList);
        nhBbGiaoNhanVtRepository.delete(data);
    }

    @Override
    public void deleteMulti(List<Long> listMulti) {

    }

    @Override
    public void export(NhBbGiaoNhanVtReq req, HttpServletResponse response) throws Exception {
//        return false;
    }

    @Override
    public ReportTemplateResponse preview(NhBbGiaoNhanVtReq req) throws Exception {
        NhBbGiaoNhanVt nhBbGiaoNhanVt = detail(req.getId());
        if (nhBbGiaoNhanVt == null) {
            throw new Exception("Bản kê nhập vật tư không tồn tại.");
        }
        NhBbGiaoNhanVtPreview object = new NhBbGiaoNhanVtPreview();
        ReportTemplate model = findByTenFile(req.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, object);
    }

//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public NhBbGiaoNhanVtRes create(NhBbGiaoNhanVtReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.validateSoBb(null, req);
//
//        NhBbGiaoNhanVt item = new NhBbGiaoNhanVt();
//        BeanUtils.copyProperties(req, item, "id");
//        item.setNgayTao(new Date());
//        item.setNguoiTaoId(userInfo.getId());
//        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
//        item.setMaDvi(userInfo.getDvql());
//        item.setCapDvi(userInfo.getCapDvi());
//        item.setSo(getSo());
//        item.setNam(LocalDate.now().getYear());
//        item.setSoBienBan(String.format("%s/%s/%s-%s", item.getSo(), item.getNam(), "BBKTNK", userInfo.getMaPbb()));
//        nhBbGiaoNhanVtRepository.save(item);
//
//        List<NhBbGiaoNhanVtCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), new HashMap<>());
//        item.setChiTiets(chiTiets);
//        item.setFileDinhKems(fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReqs(), item.getId(), NhBbGiaoNhanVt.TABLE_NAME));
//        item.setCanCus(fileDinhKemService.saveListFileDinhKem(req.getCanCus(), item.getId(), NhBbGiaoNhanVt.CAN_CU));
//        return this.buildResponse(item);
//    }
//
//    private NhBbGiaoNhanVtRes buildResponse(NhBbGiaoNhanVt item) throws Exception {
//        NhBbGiaoNhanVtRes res = new NhBbGiaoNhanVtRes();
//        List<NhBbGiaoNhanVtCtRes> chiTiets = new ArrayList<>();
//        BeanUtils.copyProperties(item, res);
//        res.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//        res.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//        for (NhBbGiaoNhanVtCt ct : item.getChiTiets()) {
//            chiTiets.add(new NhBbGiaoNhanVtCtRes(ct));
//        }
//
//        QlnvDmDonvi donvi = getDviByMa(item.getMaDvi(), req);
//        res.setMaDvi(donvi.getMaDvi());
//        res.setTenDvi(donvi.getTenDvi());
//        res.setMaQhns(donvi.getMaQhns());
//
//        res.setChiTiets(chiTiets);
//        Set<String> maVatTus = Stream.of(item.getMaVatTu(), item.getMaVatTuCha()).collect(Collectors.toSet());
//        if (!CollectionUtils.isEmpty(maVatTus)) {
//            Set<QlnvDmVattu> vatTus = qlnvDmVattuRepository.findByMaIn(maVatTus.stream().filter(Objects::nonNull).collect(Collectors.toSet()));
//            if (CollectionUtils.isEmpty(vatTus))
//                throw new Exception("Không tìm thấy vật tư");
//            vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(item.getMaVatTu())).findFirst()
//                    .ifPresent(v -> res.setTenVatTu(v.getTen()));
//            vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(item.getMaVatTuCha())).findFirst()
//                    .ifPresent(v -> res.setTenVatTuCha(v.getTen()));
//        }
//
//        if (item.getQdgnvnxId() != null) {
//            Optional<HhQdGiaoNvuNhapxuatHdr> qdNhap = hhQdGiaoNvuNhapxuatRepository.findById(item.getQdgnvnxId());
//            if (!qdNhap.isPresent()) {
//                throw new Exception("Không tìm thấy quyết định nhập");
//            }
//            res.setSoQuyetDinhNhap(qdNhap.get().getSoQd());
//        }
//
//        if (item.getHopDongId() != null) {
//            Optional<HhHopDongHdr> hopDong = hhHopDongRepository.findById(item.getHopDongId());
//            if (!hopDong.isPresent()) {
//                throw new Exception("Không tìm thấy hợp đồng");
//            }
//            res.setSoHopDong(hopDong.get().getSoHd());
//        }
//
//        if (item.getBbGuiHangId() != null) {
//            Optional<NhBienBanGuiHang> bbGh = nhBienBanGuiHangRepository.findById(item.getBbGuiHangId());
//            if (!bbGh.isPresent()) {
//                throw new Exception("Không tìm thấy biên bản gửi hàng");
//            }
////            res.setSoBbGh(bbGh.get().getSoBienBan());
//        }
//
//        if (item.getHoSKyThuatId() != null) {
//            Optional<NhHoSoKyThuat> hskt = nhHoSoKyThuatRepository.findById(item.getHoSKyThuatId());
//            if (!hskt.isPresent()) {
//                throw new Exception("Không tìm thấy hồ sơ kỹ thuật");
//            }
//            res.setSoBbGh(hskt.get().getSoHoSoKyThuat());
//        }
//
//        if (item.getBbKtNhapKhoId() != null) {
//            Optional<NhBbKtNhapKhoVt> bbKtNk = bbKtNhapKhoVtRepository.findById(item.getBbKtNhapKhoId());
//            if (!bbKtNk.isPresent()) {
//                throw new Exception("Không tìm thấy biên bản kết thúc nhập kho");
//            }
//            res.setSoBbKtNk(bbKtNk.get().getSoBienBan());
//        }
//        res.setFileDinhKems(fileDinhKemService.search(item.getId(), Collections.singleton(NhBbGiaoNhanVt.TABLE_NAME)));
//        res.setCanCus(fileDinhKemService.search(item.getId(), Collections.singleton(NhBbGiaoNhanVt.CAN_CU)));
//        return res;
//    }
//
//    private List<NhBbGiaoNhanVtCt> saveListChiTiet(Long parentId,
//                                                    List<NhBbGiaoNhanVtCt> chiTietReqs,
//                                                    Map<Long, NhBbGiaoNhanVtCt> mapChiTiet) throws Exception {
//        List<NhBbGiaoNhanVtCt> chiTiets = new ArrayList<>();
//        for (NhBbGiaoNhanVtCt req : chiTietReqs) {
//            Long id = req.getId();
//            NhBbGiaoNhanVtCt chiTiet = new NhBbGiaoNhanVtCt();
//
//            if (id != null && id > 0) {
//                chiTiet = mapChiTiet.get(id);
//                if (chiTiet == null)
//                    throw new Exception("Biên bản chuẩn bị kho chi tiết không tồn tại.");
//                mapChiTiet.remove(id);
//            }
//
//            BeanUtils.copyProperties(req, chiTiet, "id");
//            chiTiet.setBbGiaoNhanVtId(parentId);
//            chiTiets.add(chiTiet);
//        }
//
//        if (!CollectionUtils.isEmpty(chiTiets))
//            nhBbGiaoNhanVtCtRepository.saveAll(chiTiets);
//
//        return chiTiets;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public NhBbGiaoNhanVtRes update(NhBbGiaoNhanVtReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        Optional<NhBbGiaoNhanVt> optional = nhBbGiaoNhanVtRepository.findById(req.getId());
//        if (!optional.isPresent())
//            throw new Exception("Biên bản chuẩn bị kho không tồn tại.");
//
//        this.validateSoBb(optional.get(), req);
//
//        NhBbGiaoNhanVt item = optional.get();
//        BeanUtils.copyProperties(req, item, "id", "so", "nam");
//        item.setNgaySua(new Date());
//        item.setNguoiSuaId(userInfo.getId());
//        nhBbGiaoNhanVtRepository.save(item);
//        Map<Long, NhBbGiaoNhanVtCt> mapChiTiet = nhBbGiaoNhanVtCtRepository.findByBbGiaoNhanVtIdIn(Collections.singleton(item.getId()))
//                .stream().collect(Collectors.toMap(NhBbGiaoNhanVtCt::getId, Function.identity()));
//
//        List<NhBbGiaoNhanVtCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), mapChiTiet);
//        item.setChiTiets(chiTiets);
//
//        if (!CollectionUtils.isEmpty(mapChiTiet.values()))
//            nhBbGiaoNhanVtCtRepository.deleteAll(mapChiTiet.values());
//        item.setFileDinhKems(fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReqs(), item.getId(), NhBbGiaoNhanVt.TABLE_NAME));
//        item.setCanCus(fileDinhKemService.saveListFileDinhKem(req.getCanCus(), item.getId(), NhBbGiaoNhanVt.CAN_CU));
//        return this.buildResponse(item);
//    }
//
//    @Override
//    public NhBbGiaoNhanVtRes detail(Long id) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<NhBbGiaoNhanVt> optional = nhBbGiaoNhanVtRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Biên bản chuẩn bị kho không tồn tại.");
//
//        NhBbGiaoNhanVt item = optional.get();
//        item.setChiTiets(nhBbGiaoNhanVtCtRepository.findByBbGiaoNhanVtIdIn(Collections.singleton(item.getId())));
//        item.setFileDinhKems(fileDinhKemService.search(item.getId(), Collections.singleton(NhBbGiaoNhanVt.TABLE_NAME)));
//        return this.buildResponse(item);
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean delete(Long id) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<NhBbGiaoNhanVt> optional = nhBbGiaoNhanVtRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Biên bản giao nhận không tồn tại.");
//
//        NhBbGiaoNhanVt item = optional.get();
//        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
//            throw new Exception("Không thể xóa biên bản đã đã duyệt");
//        }
//        nhBbGiaoNhanVtCtRepository.deleteByBbGiaoNhanVtIdIn(Collections.singleton(item.getId()));
//        nhBbGiaoNhanVtRepository.delete(item);
//        fileDinhKemService.delete(item.getId(), Collections.singleton(NhBbGiaoNhanVt.TABLE_NAME));
//        fileDinhKemService.delete(item.getId(), Collections.singleton(NhBbGiaoNhanVt.CAN_CU));
//        return true;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean updateStatusQd(StatusReq stReq) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<NhBbGiaoNhanVt> optional = nhBbGiaoNhanVtRepository.findById(stReq.getId());
//        if (!optional.isPresent())
//            throw new Exception("Biên bản giao nhận không tồn tại.");
//
//        NhBbGiaoNhanVt item = optional.get();
//
//        String trangThai = item.getTrangThai();
//        if (NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
//                return false;
//
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId());
//            item.setNguoiGuiDuyetId(userInfo.getId());
//            item.setNgayGuiDuyet(new Date());
//        } else if (NhapXuatHangTrangThaiEnum.DADUYET_LDC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId().equals(trangThai))
//                return false;
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.DADUYET_LDC.getId());
//            item.setNguoiPduyetId(userInfo.getId());
//            item.setNgayPduyet(new Date());
//        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_LDC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId().equals(trangThai))
//                return false;
//
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_LDC.getId());
//            item.setNguoiPduyetId(userInfo.getId());
//            item.setNgayPduyet(new Date());
//            item.setLyDoTuChoi(stReq.getLyDo());
//        } else {
//            throw new Exception("Bad request.");
//        }
//        nhBbGiaoNhanVtRepository.save(item);
//
//        return true;
//    }
//
//    @Override
//    public Page<NhBbGiaoNhanVtRes> search(NhBbGiaoNhanVtSearchReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
//        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//
//        List<Object[]> data = nhBbGiaoNhanVtRepository.search(req);
//        Set<Long> ids = data.stream()
//                .map(o -> (NhBbGiaoNhanVt) o[0])
//                .map(NhBbGiaoNhanVt::getId).collect(Collectors.toSet());
//
//        if (CollectionUtils.isEmpty(ids)) {
//            return new PageImpl<>(new ArrayList<>(), pageable, nhBbGiaoNhanVtRepository.count(req));
//        }
//        Map<Long, NhBbGiaoNhanVtCt> mapCt = nhBbGiaoNhanVtCtRepository.findByBbGiaoNhanVtIdInAndLoaiDaiDien(ids, LoaiDaiDienEnum.BEN_GIAO.getId())
//                .stream().collect(Collectors.toMap(NhBbGiaoNhanVtCt::getBbGiaoNhanVtId, Function.identity(), (o1, o2) -> o2));
//
//        List<NhBbGiaoNhanVtRes> responses = new ArrayList<>();
//        for (Object[] o : data) {
//            NhBbGiaoNhanVtRes response = new NhBbGiaoNhanVtRes();
//            NhBbGiaoNhanVt item = (NhBbGiaoNhanVt) o[0];
//            Long qdNhapId = (Long) o[1];
//            String soQdNhap = (String) o[2];
//            Long hopDongId = (Long) o[3];
//            String soHd = (String) o[4];
//            NhBbGiaoNhanVtCt daiDienBenGiao = mapCt.get(item.getId());
//            BeanUtils.copyProperties(item, response);
//            response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//            response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//            response.setQdgnvnxId(qdNhapId);
//            response.setSoQuyetDinhNhap(soQdNhap);
//            response.setHopDongId(hopDongId);
//            response.setSoHopDong(soHd);
//            if (daiDienBenGiao != null) {
//                response.setBenGiao(daiDienBenGiao.getDaiDien());
//            }
//            responses.add(response);
//        }
//
//        return new PageImpl<>(responses, pageable, nhBbGiaoNhanVtRepository.count(req));
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean deleteMultiple(DeleteReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        nhBbGiaoNhanVtCtRepository.deleteByBbGiaoNhanVtIdIn(req.getIds());
//        nhBbGiaoNhanVtRepository.deleteByIdIn(req.getIds());
//        fileDinhKemService.deleteMultiple(req.getIds(), Arrays.asList(NhBbGiaoNhanVt.TABLE_NAME, NhBbGiaoNhanVt.CAN_CU));
//        return true;
//    }
//
//    @Override
//    public boolean exportToExcel(NhBbGiaoNhanVtSearchReq objReq, HttpServletResponse response) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
//        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
//        List<NhBbGiaoNhanVtRes> list = this.search(objReq).get().collect(Collectors.toList());
//
//        if (CollectionUtils.isEmpty(list))
//            return true;
//
//        String[] rowsName = new String[] { STT, SO_BIEN_BAN, SO_QUYET_DINH_NHAP, NAM_NHAP,
//                NGAY_BIEN_BAN, SO_HOP_DONG, BEN_GIAO, TRANG_THAI};
//        String filename = "Danh_sach_bien_giao_nhan.xlsx";
//
//        List<Object[]> dataList = new ArrayList<Object[]>();
//        Object[] objs = null;
//
//        try {
//            for (int i = 0; i < list.size(); i++) {
//                NhBbGiaoNhanVtRes item = list.get(i);
//                objs = new Object[rowsName.length];
//                objs[0] = i;
//                objs[1] = item.getSoBienBan();
//                objs[2] = item.getSoQuyetDinhNhap();
//                objs[3] = item.getNam();
//                objs[4] = LocalDateTimeUtils.localDateToString(item.getNgayKy());
//                objs[5] = item.getSoHopDong();
//                objs[6] = item.getBenGiao();
//                objs[7] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
//                dataList.add(objs);
//            }
//
//            ExportExcel ex = new ExportExcel(SHEET_BIEN_BAN_GIAO_NHAN_VAT_TU, filename, rowsName, dataList, response);
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
//        Integer so = nhBbGiaoNhanVtRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
//        so = Optional.ofNullable(so).orElse(0);
//        so = so + 1;
//        return so;
//    }
//
//    private void validateSoBb(NhBbGiaoNhanVt update, NhBbGiaoNhanVtReq req) throws Exception {
//        String so = req.getSoBienBan();
//        if (!StringUtils.hasText(so))
//            return;
//        if (update == null || (StringUtils.hasText(update.getSoBienBan()) && !update.getSoBienBan().equalsIgnoreCase(so))) {
//            Optional<NhBbGiaoNhanVt> optional = nhBbGiaoNhanVtRepository.findFirstBySoBienBan(so);
//            Long updateId = Optional.ofNullable(update).map(NhBbGiaoNhanVt::getId).orElse(null);
//            if (optional.isPresent() && !optional.get().getId().equals(updateId))
//                throw new Exception("Số biên bản " + so + " đã tồn tại");
//        }
//    }
//
//    @Override
//    public BaseNhapHangCount count(Set<String> maDvis) throws Exception {
//        NhBbGiaoNhanVtSearchReq countReq = new NhBbGiaoNhanVtSearchReq();
//        countReq.setMaDvis(maDvis);
//        BaseNhapHangCount count = new BaseNhapHangCount();
//
//        count.setVatTu(nhBbGiaoNhanVtRepository.count(countReq));
//        return count;
//    }
}
