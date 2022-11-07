package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bangkecanhang;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuktracl.NhPhieuKtChatLuong;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHangCt;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKho;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoCtRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHangRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeChCtRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoRepository;
import com.tcdt.qlnvhang.request.object.quanlybangkecanhangluongthuc.QlBangKeCanHangLtReq;
import com.tcdt.qlnvhang.request.object.quanlybangkecanhangluongthuc.QlBangKeChCtLtReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
@Log4j2
public class NhBangKeCanHangServiceImpl extends BaseServiceImpl implements NhBangKeCanHangService {

    @Autowired
    private NhBangKeCanHangRepository nhBangKeCanHangRepository;

    @Autowired
    private NhBangKeChCtRepository nhBangKeChCtRepository;

    @Autowired
    private HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;

    @Autowired
    private NhPhieuNhapKhoRepository nhPhieuNhapKhoRepository;

    @Autowired
    private NhPhieuNhapKhoCtRepository nhPhieuNhapKhoCtRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public Page<NhBangKeCanHang> searchPage(QlBangKeCanHangLtReq req) {
        return null;
    }

    @Override
    public List<NhBangKeCanHang> searchAll(QlBangKeCanHangLtReq req) {
        return null;
    }

    @Override
    @Transactional
    public NhBangKeCanHang create(QlBangKeCanHangLtReq req) throws Exception {
        if (req == null){
            return null;
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");

        }

        NhBangKeCanHang item = new NhBangKeCanHang();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(new Date());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setNam(LocalDate.now().getYear());
        item.setId(Long.valueOf(item.getSoBangKe().split("/")[0]));
        nhBangKeCanHangRepository.save(item);

        this.saveCtiet(item.getId(),req);

        return item;
    }

    @Transactional
    void saveCtiet(Long idHdr,QlBangKeCanHangLtReq req){
        nhBangKeChCtRepository.deleteByIdBangKeCanHangHdr(idHdr);
        for(QlBangKeChCtLtReq objCtiet : req.getChiTiets()){
            NhBangKeCanHangCt ctiet = new NhBangKeCanHangCt();
            BeanUtils.copyProperties(objCtiet,ctiet,"id");
            ctiet.setIdBangKeCanHangHdr(idHdr);
            nhBangKeChCtRepository.save(ctiet);
        }
    }

    @Override
    public NhBangKeCanHang update(QlBangKeCanHangLtReq req) throws Exception {
        if (req == null){
            return null;
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        Optional<NhBangKeCanHang> optional = nhBangKeCanHangRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Bảng kê không tồn tại.");
        }

        NhBangKeCanHang item = optional.get();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgaySua(new Date());
        item.setNguoiSuaId(userInfo.getId());
        nhBangKeCanHangRepository.save(item);
        this.saveCtiet(item.getId(),req);
        return item;
    }

    @Override
    public NhBangKeCanHang detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        Optional<NhBangKeCanHang> optional = nhBangKeCanHangRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Bảng kê không tồn tại.");

        NhBangKeCanHang item = optional.get();
        Map<String, String> listDanhMucDvi = getListDanhMucDvi("", "", "01");

        item.setChiTiets(nhBangKeChCtRepository.findAllByIdBangKeCanHangHdr(item.getId()));
        item.setTenDvi(listDanhMucDvi.get(item.getMaDvi()));
        item.setTenNguoiTao(ObjectUtils.isEmpty(item.getNguoiTaoId()) ? "" : userInfoRepository.findById(item.getNguoiTaoId()).get().getFullName());
        item.setTenNguoiPduyet(ObjectUtils.isEmpty(item.getNguoiPduyetId()) ? "" :userInfoRepository.findById(item.getNguoiPduyetId()).get().getFullName());
        return item;
    }

    @Override
    public NhBangKeCanHang approve(QlBangKeCanHangLtReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
            throw new Exception("Bad Request");
        }

        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        Optional<NhBangKeCanHang> optional = nhBangKeCanHangRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        NhBangKeCanHang phieu = optional.get();

        String status = req.getTrangThai() + phieu.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDCC + Contains.DUTHAO:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                phieu.setNguoiGuiDuyetId(userInfo.getId());
                phieu.setNgayGuiDuyet(new Date());
                break;
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                phieu.setNguoiPduyetId(userInfo.getId());
                phieu.setNgayPduyet(new Date());
                phieu.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                phieu.setNguoiPduyetId(userInfo.getId());
                phieu.setNgayPduyet(new Date());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        phieu.setTrangThai(req.getTrangThai());
        nhBangKeCanHangRepository.save(phieu);
        return phieu;
    }

    @Override
    public void delete(Long id) throws Exception {

    }

    @Override
    public void deleteMulti(List<Long> listMulti) {

    }

    @Override
    public boolean export(QlBangKeCanHangLtReq req) throws Exception {
        return false;
    }

    @Override
    public List<NhBangKeCanHang> findAllByIdQdGiaoNvNh(Long idQdGiaoNvNh) {
        return setDetailList(nhBangKeCanHangRepository.findAllByIdQdGiaoNvNh(idQdGiaoNvNh));

    }

    @Override
    public List<NhBangKeCanHang> findAllByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh) {
        return setDetailList(nhBangKeCanHangRepository.findByIdDdiemGiaoNvNh(idDdiemGiaoNvNh));
    }

    List<NhBangKeCanHang> setDetailList(List<NhBangKeCanHang> list){
        list.forEach( item -> {
//            item.setPhieuNhapKho(StringUtils.isEmpty(item.getPhieuNhapKho()) ? null : nhPhieuNhapKhoRepository.getOne(Long.valueOf(item.getSoPhieuNhapKho().split("/")[0])));
        });
        return list;
    }


//
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public QlBangKeCanHangLtRes create(QlBangKeCanHangLtReq req) throws Exception {
//        if (req == null)
//            return null;
//
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null)
//            throw new Exception("Bad request.");
//
//
//
//        this.validateSoBb(null, req);
//        QlBangKeCanHangLt item = new QlBangKeCanHangLt();
//        BeanUtils.copyProperties(req, item, "id");
//        item.setNgayTao(LocalDate.now());
//        item.setNguoiTaoId(userInfo.getId());
//        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
//        item.setMaDvi(userInfo.getDvql());
//        item.setCapDvi(userInfo.getCapDvi());
//        item.setSo(getSo());
//        item.setNam(LocalDate.now().getYear());
//        item.setSoBangKe(String.format("%s/%s/%s-%s", item.getSo(), item.getNam(), "BKCH", userInfo.getMaPbb()));
//        qlBangKeCanHangLtRepository.save(item);
//
//        List<QlBangKeChCtLtReq> chiTietReqs = req.getChiTiets();
//        List<QlBangKeChCtLt> chiTiets = this.saveListChiTiet(item.getId(), chiTietReqs, new HashMap<>());
//        item.setChiTiets(chiTiets);
//
//        this.updateThucNhapPnk(item);
//        return this.buildResponse(item);
//    }
//
//    /*
//     * Update số lượng thực nhập vào phiếu nhập kho
//     */
//    private void updateThucNhapPnk(QlBangKeCanHangLt item) throws Exception {
//        Long pnkId = item.getQlPhieuNhapKhoLtId();
//        NhPhieuNhapKho phieuNhapKho = null;
//        if (pnkId != null) {
//            phieuNhapKho = nhPhieuNhapKhoRepository.findById(pnkId)
//                    .orElseThrow(() -> new Exception("Không tìm thấy phiếu nhập kho"));
//        }
//
//        String maVatTu = item.getMaVatTu();
//        if (phieuNhapKho == null || !StringUtils.hasText(maVatTu))
//            return;
//
//        NhPhieuNhapKhoCt pnkCt = null;
//        if (pnkCt != null) {
//            pnkCt.setSoLuongThucNhap(item.getTongTrongLuongBaoBi());
//            nhPhieuNhapKhoCtRepository.save(pnkCt);
//        }
//    }
//    private QlBangKeCanHangLtRes buildResponse(QlBangKeCanHangLt item) throws Exception {
//
//        QlBangKeCanHangLtRes response = new QlBangKeCanHangLtRes();
//        BeanUtils.copyProperties(item, response);
//        response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//        response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//        QlnvDmDonvi donvi = getDviByMa(item.getMaDvi(), req);
//        response.setMaDvi(donvi.getMaDvi());
//        response.setTenDvi(donvi.getTenDvi());
//
//        Set<String> maVatTus = Stream.of(item.getMaVatTu(), item.getMaVatTuCha()).collect(Collectors.toSet());
//        if (!CollectionUtils.isEmpty(maVatTus)) {
//            Set<QlnvDmVattu> vatTus = qlnvDmVattuRepository.findByMaIn(maVatTus.stream().filter(Objects::nonNull).collect(Collectors.toSet()));
//            if (CollectionUtils.isEmpty(vatTus))
//                throw new Exception("Không tìm thấy vật tư");
//            vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(item.getMaVatTu())).findFirst()
//                    .ifPresent(v -> response.setTenVatTu(v.getTen()));
//            vatTus.stream().filter(v -> v.getMa().equalsIgnoreCase(item.getMaVatTuCha())).findFirst()
//                    .ifPresent(v -> response.setTenVatTuCha(v.getTen()));
//        }
//        List<QlBangKeChCtLt> chiTiets = item.getChiTiets();
//        List<QlBangKeChCtLtRes> chiTietResList = new ArrayList<>();
//        BigDecimal tongTrongLuongCaBi = BigDecimal.ZERO;
//        for (QlBangKeChCtLt chiTiet : chiTiets) {
//            QlBangKeChCtLtRes chiTietRes = new QlBangKeChCtLtRes();
//            BeanUtils.copyProperties(chiTiet, chiTietRes);
//            chiTietResList.add(chiTietRes);
//            tongTrongLuongCaBi = tongTrongLuongCaBi.add(Optional.ofNullable(chiTiet.getTrongLuongCaBi()).orElse(BigDecimal.ZERO));
//        }
//        response.setChiTiets(chiTietResList);
//
//        response.setTongTrongLuongCaBi(tongTrongLuongCaBi);
//        BigDecimal tongTrongLuongHangTruBi = tongTrongLuongCaBi.subtract(Optional.ofNullable(item.getTongTrongLuongBaoBi()).orElse(BigDecimal.ZERO));
//        response.setTongTrongLuongHangTruBiBangChu(MoneyConvert.docSoLuong(tongTrongLuongHangTruBi.toString(), null));
//
//        if (item.getQlPhieuNhapKhoLtId() != null) {
//            NhPhieuNhapKho phieuNhapKho = nhPhieuNhapKhoRepository.findById(item.getQlPhieuNhapKhoLtId())
//                    .orElseThrow(() -> new Exception("Không tìm thấy phiếu nhập kho"));
//
////            response.setSoPhieuNhapKho(phieuNhapKho.getSoPhieu());
//        }
//
//        if (item.getQdgnvnxId() != null) {
//            Optional<HhQdGiaoNvuNhapxuatHdr> qdNhap = hhQdGiaoNvuNhapxuatRepository.findById(item.getQdgnvnxId());
//            if (!qdNhap.isPresent()) {
//                throw new Exception("Không tìm thấy quyết định nhập");
//            }
//            response.setSoQuyetDinhNhap(qdNhap.get().getSoQd());
//        }
//
//        KtNganLo nganLo = null;
//        if (StringUtils.hasText(item.getMaNganLo())) {
//            nganLo = ktNganLoRepository.findFirstByMaNganlo(item.getMaNganLo());
//        }
//
//        this.thongTinNganLo(response, nganLo);
//        return response;
//    }
//
//    private List<QlBangKeChCtLt> saveListChiTiet(Long parentId, List<QlBangKeChCtLtReq> chiTietReqs, Map<Long, QlBangKeChCtLt> mapChiTiet) throws Exception {
//        List<QlBangKeChCtLt> chiTiets = new ArrayList<>();
//        for (QlBangKeChCtLtReq req : chiTietReqs) {
//            Long id = req.getId();
//            QlBangKeChCtLt chiTiet = new QlBangKeChCtLt();
//
//            if (id != null && id > 0) {
//                chiTiet = mapChiTiet.get(id);
//                if (chiTiet == null)
//                    throw new Exception("Bảng kê chi tiết hàng Hóa không tồn tại.");
//                mapChiTiet.remove(id);
//            }
//
//            BeanUtils.copyProperties(req, chiTiet, "id");
//            chiTiet.setQlBangKeCanHangLtId(parentId);
//            chiTiets.add(chiTiet);
//        }
//
//        if (!CollectionUtils.isEmpty(chiTiets))
//            qlBangKeChCtLtRepository.saveAll(chiTiets);
//
//        return chiTiets;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public QlBangKeCanHangLtRes update(QlBangKeCanHangLtReq req) throws Exception {
//        if (req == null)
//            return null;
//
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null)
//            throw new Exception("Bad request.");
//
//        Optional<QlBangKeCanHangLt> optional = qlBangKeCanHangLtRepository.findById(req.getId());
//        if (!optional.isPresent())
//            throw new Exception("Bảng kê không tồn tại.");
//
//        this.validateSoBb(optional.get(), req);
//        QlBangKeCanHangLt item = optional.get();
//        BeanUtils.copyProperties(req, item, "id");
//        item.setNgaySua(LocalDate.now());
//        item.setNguoiSuaId(userInfo.getId());
//        qlBangKeCanHangLtRepository.save(item);
//
//        Map<Long, QlBangKeChCtLt> map = qlBangKeChCtLtRepository.findAllByQlBangKeCanHangLtId(item.getId())
//                .stream().collect(Collectors.toMap(QlBangKeChCtLt::getId, Function.identity()));
//
//        List<QlBangKeChCtLtReq> chiTietReqs = req.getChiTiets();
//        List<QlBangKeChCtLt> chiTiets = this.saveListChiTiet(item.getId(), chiTietReqs, map);
//        item.setChiTiets(chiTiets);
//
//        if (!CollectionUtils.isEmpty(map.values()))
//            qlBangKeChCtLtRepository.deleteAll(map.values());
//
//        this.updateThucNhapPnk(item);
//        return this.buildResponse(item);
//    }
//
//    @Override
//    public QlBangKeCanHangLtRes detail(Long id) throws Exception {
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null)
//            throw new Exception("Bad request.");
//
//        Optional<QlBangKeCanHangLt> optional = qlBangKeCanHangLtRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Bảng kê không tồn tại.");
//
//        QlBangKeCanHangLt item = optional.get();
//        item.setChiTiets(qlBangKeChCtLtRepository.findAllByQlBangKeCanHangLtId(item.getId()));
//        return this.buildResponse(item);
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean delete(Long id) throws Exception {
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null)
//            throw new Exception("Bad request.");
//
//        Optional<QlBangKeCanHangLt> optional = qlBangKeCanHangLtRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Bảng kê không tồn tại.");
//
//        QlBangKeCanHangLt item = optional.get();
//        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
//            throw new Exception("Không thể xóa bảng kê đã đã duyệt");
//        }
//
//        List<QlBangKeChCtLt> chiTiets = qlBangKeChCtLtRepository.findAllByQlBangKeCanHangLtId(item.getId());
//        if (!CollectionUtils.isEmpty(chiTiets)) {
//            qlBangKeChCtLtRepository.deleteAll(chiTiets);
//        }
//
//        qlBangKeCanHangLtRepository.delete(item);
//        return true;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean updateStatusQd(StatusReq stReq) throws Exception {
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null)
//            throw new Exception("Bad request.");
//        Optional<QlBangKeCanHangLt> optional = qlBangKeCanHangLtRepository.findById(stReq.getId());
//        if (!optional.isPresent())
//            throw new Exception("Quyết định không tồn tại.");
//
//        QlBangKeCanHangLt bangKe = optional.get();
//
//        String trangThai = bangKe.getTrangThai();
//        if (NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
//                return false;
//
//            bangKe.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId());
//            bangKe.setNguoiGuiDuyetId(userInfo.getId());
//            bangKe.setNgayGuiDuyet(LocalDate.now());
//        } else if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
//                return false;
//            bangKe.setTrangThai(NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId());
//            bangKe.setNguoiPduyetId(userInfo.getId());
//            bangKe.setNgayPduyet(LocalDate.now());
//        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
//                return false;
//
//            bangKe.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId());
//            bangKe.setNguoiPduyetId(userInfo.getId());
//            bangKe.setNgayPduyet(LocalDate.now());
//        } else {
//            throw new Exception("Bad request.");
//        }
//
//        return true;
//    }
//
//    @Override
//    public Page<QlBangKeCanHangLtRes> search(QlBangKeCanHangLtSearchReq req) throws Exception {
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null)
//            throw new Exception("Bad request.");
//
//        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
//
//        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//        List<Object[]> data = qlBangKeCanHangLtRepository.search(req);
//        List<QlBangKeCanHangLtRes> responses = new ArrayList<>();
//        for (Object[] o : data) {
//            QlBangKeCanHangLtRes response = new QlBangKeCanHangLtRes();
//            QlBangKeCanHangLt item = (QlBangKeCanHangLt) o[0];
//            Long pnkId = (Long) o[1];
//            String soPnk = (String) o[2];
//            KtNganLo nganLo = o[3] != null ? (KtNganLo) o[3] : null;
//            Long qdNhapId = (Long) o[4];
//            String soQdNhap = (String) o[5];
//            String maVatTu = (String) o[6];
//            String tenVatTu = (String) o[7];
//            BeanUtils.copyProperties(item, response);
//            response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//            response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//            response.setQlPhieuNhapKhoLtId(pnkId);
//            response.setSoPhieuNhapKho(soPnk);
//            this.thongTinNganLo(response, nganLo);
//            response.setQdgnvnxId(qdNhapId);
//            response.setSoQuyetDinhNhap(soQdNhap);
//            response.setMaVatTu(maVatTu);
//            response.setTenVatTu(tenVatTu);
//            responses.add(response);
//        }
//
//        return new PageImpl<>(responses, pageable, qlBangKeCanHangLtRepository.count(req));
//    }
//
//    private void thongTinNganLo(QlBangKeCanHangLtRes item, KtNganLo nganLo) {
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
//    public BaseNhapHangCount count() throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        QlBangKeCanHangLtSearchReq req = new QlBangKeCanHangLtSearchReq();
//        this.prepareSearchReq(req, userInfo, null, req.getTrangThais());
//        BaseNhapHangCount count = new BaseNhapHangCount();
//
//        count.setTatCa(qlBangKeCanHangLtRepository.count(req));
//        return count;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean deleteMultiple(DeleteReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        if (CollectionUtils.isEmpty(req.getIds()))
//            return false;
//
//
//        qlBangKeChCtLtRepository.deleteByQlBangKeCanHangLtIdIn(req.getIds());
//        qlBangKeCanHangLtRepository.deleteByIdIn(req.getIds());
//        return true;
//    }
//
//    @Override
//    public boolean exportToExcel(QlBangKeCanHangLtSearchReq objReq, HttpServletResponse response) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
//        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
//        List<QlBangKeCanHangLtRes> list = this.search(objReq).get().collect(Collectors.toList());
//
//        if (CollectionUtils.isEmpty(list))
//            return true;
//
//        String[] rowsName = new String[] { STT, SO_BANG_KE, SO_QUYET_DINH_NHAP, SO_PHIEU_NHAP, NGAY_NHAP_KHO,
//                DIEM_KHO, NHA_KHO, NGAN_KHO, NGAN_LO, TRANG_THAI};
//        String filename = "Danh_sach_bang_ke_can_hang.xlsx";
//
//        List<Object[]> dataList = new ArrayList<Object[]>();
//        Object[] objs = null;
//
//        try {
//            for (int i = 0; i < list.size(); i++) {
//                QlBangKeCanHangLtRes item = list.get(i);
//                objs = new Object[rowsName.length];
//                objs[0] = i;
//                objs[1] = item.getSoBangKe();
//                objs[2] = item.getSoQuyetDinhNhap();
//                objs[3] = item.getSoPhieuNhapKho();
//                objs[4] = LocalDateTimeUtils.localDateToString(item.getNgayNhap());
//                objs[5] = item.getTenDiemKho();
//                objs[6] = item.getTenNhaKho();
//                objs[7] = item.getTenNganKho();
//                objs[8] = item.getTenNganLo();
//                objs[9] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
//                dataList.add(objs);
//            }
//
//            ExportExcel ex = new ExportExcel(SHEET_BANG_KE_CAN_HANG_LUONG_THUC, filename, rowsName, dataList, response);
//            ex.export();
//        } catch (Exception e) {
//            log.error("Error export", e);
//            return false;
//        }
//
//        return true;
//    }
//
//    private void validateSoBb(QlBangKeCanHangLt update, QlBangKeCanHangLtReq req) throws Exception {
//        String so = req.getSoBangKe();
//        if (!StringUtils.hasText(so))
//            return;
//        if (update == null || (StringUtils.hasText(update.getSoBangKe()) && !update.getSoBangKe().equalsIgnoreCase(so))) {
//            Optional<QlBangKeCanHangLt> optional = qlBangKeCanHangLtRepository.findFirstBySoBangKe(so);
//            Long updateId = Optional.ofNullable(update).map(QlBangKeCanHangLt::getId).orElse(null);
//            if (optional.isPresent() && !optional.get().getId().equals(updateId))
//                throw new Exception("Số bảng kê " + so + " đã tồn tại");
//        }
//    }
//
//    @Override
//    public Integer getSo() throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Integer so = qlBangKeCanHangLtRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
//        so = Optional.ofNullable(so).orElse(0);
//        so = so + 1;
//        return so;
//    }
//
//    @Override
//    public BaseNhapHangCount count(Set<String> maDvis) throws Exception {
//        QlBangKeCanHangLtSearchReq countReq = new QlBangKeCanHangLtSearchReq();
//        countReq.setMaDvis(maDvis);
//        BaseNhapHangCount count = new BaseNhapHangCount();
//
//        countReq.setMaVatTuCha(Contains.LOAI_VTHH_THOC);
//        count.setThoc(qlBangKeCanHangLtRepository.count(countReq));
//        countReq.setMaVatTuCha(Contains.LOAI_VTHH_GAO);
//        count.setGao(qlBangKeCanHangLtRepository.count(countReq));
//        countReq.setMaVatTuCha(Contains.LOAI_VTHH_MUOI);
//        count.setMuoi(qlBangKeCanHangLtRepository.count(countReq));
//        return count;
//    }
}
