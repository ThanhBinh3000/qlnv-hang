package com.tcdt.qlnvhang.service.nhaphang.luongthucmuoi.quanlybienbannhapdaykholuongthuc;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKhoCt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.HhBbNghiemthuKlstRepository;
import com.tcdt.qlnvhang.repository.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKhoCtRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKhoRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.quanlybienbannhapdaykholuongthuc.QlBienBanNdkCtLtReq;
import com.tcdt.qlnvhang.request.object.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtReq;
import com.tcdt.qlnvhang.request.search.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtSearchReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.response.quanlybienbannhapdaykholuongthuc.QlBienBanNdkCtLtRes;
import com.tcdt.qlnvhang.response.quanlybienbannhapdaykholuongthuc.QlBienBanNhapDayKhoLtRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.catalog.QlnvDmVattu;
import com.tcdt.qlnvhang.table.khotang.KtDiemKho;
import com.tcdt.qlnvhang.table.khotang.KtNganKho;
import com.tcdt.qlnvhang.table.khotang.KtNganLo;
import com.tcdt.qlnvhang.table.khotang.KtNhaKho;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.LocalDateTimeUtils;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
public class QlBienBanNhapDayKhoLtServiceImpl extends BaseServiceImpl implements QlBienBanNhapDayKhoLtService {

    @Autowired
    private NhBbNhapDayKhoRepository nhBbNhapDayKhoRepository;

    @Autowired
    private NhBbNhapDayKhoCtRepository nhBbNhapDayKhoCtRepository;

    @Override
    public Page<NhBbNhapDayKho> searchPage(QlBienBanNhapDayKhoLtReq req) {
        return null;
    }

    @Override
    public List<NhBbNhapDayKho> searchAll(QlBienBanNhapDayKhoLtReq req) {
        return null;
    }

    @Override
    public NhBbNhapDayKho create(QlBienBanNhapDayKhoLtReq req) throws Exception {
        if (req == null) {
            return null;
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");

        }
        NhBbNhapDayKho item = new NhBbNhapDayKho();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(LocalDate.now());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setNam(LocalDate.now().getYear());
        item.setId(Long.valueOf(req.getSoBienBanNhapDayKho().split("/")[0]));
        nhBbNhapDayKhoRepository.save(item);

        List<NhBbNhapDayKhoCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets());
        item.setChiTiets(chiTiets);

        return item;
    }

    @Transactional
    List<NhBbNhapDayKhoCt> saveListChiTiet(Long parentId, List<QlBienBanNdkCtLtReq> chiTietReqs) throws Exception {
        nhBbNhapDayKhoCtRepository.deleteByIdBbNhapDayKho(parentId);
        List<NhBbNhapDayKhoCt> chiTiets = new ArrayList<>();
        for (QlBienBanNdkCtLtReq req : chiTietReqs) {
            NhBbNhapDayKhoCt chiTiet = new NhBbNhapDayKhoCt();
            BeanUtils.copyProperties(req, chiTiet, "id");
            chiTiet.setIdBbNhapDayKho(parentId);
            chiTiets.add(chiTiet);
        }

        if (!CollectionUtils.isEmpty(chiTiets)) {
            nhBbNhapDayKhoCtRepository.saveAll(chiTiets);
        }

        return chiTiets;
    }

    @Override
    public NhBbNhapDayKho update(QlBienBanNhapDayKhoLtReq req) throws Exception {
        if (req == null) {
            return null;
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        Optional<NhBbNhapDayKho> optional = nhBbNhapDayKhoRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Biên bản không tồn tại.");

        NhBbNhapDayKho item = optional.get();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgaySua(LocalDate.now());
        item.setNguoiSuaId(userInfo.getId());
        nhBbNhapDayKhoRepository.save(item);

        List<NhBbNhapDayKhoCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets());
        item.setChiTiets(chiTiets);

        return item;
    }

    @Override
    public NhBbNhapDayKho detail(Long id) throws Exception {
        return null;
    }

    @Override
    public NhBbNhapDayKho approve(QlBienBanNhapDayKhoLtReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {

    }

    @Override
    public void deleteMulti(List<Long> listMulti) {

    }

    @Override
    public boolean export(QlBienBanNhapDayKhoLtReq req) throws Exception {
        return false;
    }

//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public QlBienBanNhapDayKhoLtRes create(QlBienBanNhapDayKhoLtReq req) throws Exception {
//        if (req == null)
//            return null;
//
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null)
//            throw new Exception("Bad request.");
//
//        this.validateSoBb(null, req);
//        NhBbNhapDayKho item = new NhBbNhapDayKho();
//        BeanUtils.copyProperties(req, item, "id");
//        item.setNgayTao(LocalDate.now());
//        item.setNguoiTaoId(userInfo.getId());
//        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
//        item.setMaDvi(userInfo.getDvql());
//        item.setCapDvi(userInfo.getCapDvi());
//        item.setSo(getSo());
//        item.setNam(LocalDate.now().getYear());
//        item.setSoBienBan(String.format("%s/%s/%s-%s", item.getSo(), item.getNam(), "BBDK", userInfo.getMaPbb()));
//        qlBienBanNhapDayKhoLtRepository.save(item);
//
//        List<NhBbNhapDayKhoCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), new HashMap<>());
//        item.setChiTiets(chiTiets);
//
//        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), item.getId(), NhBbNhapDayKho.TABLE_NAME);
//        item.setFileDinhKems(fileDinhKems);
//
//        return buildResponse(item);
//    }
//
//    private QlBienBanNhapDayKhoLtRes buildResponse(NhBbNhapDayKho item) throws Exception {
//        QlBienBanNhapDayKhoLtRes response = new QlBienBanNhapDayKhoLtRes();
//        BeanUtils.copyProperties(item, response);
//        response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//        response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//        QlnvDmDonvi donvi = getDviByMa(item.getMaDvi(), req);
//        response.setMaDvi(donvi.getMaDvi());
//        response.setTenDvi(donvi.getTenDvi());
//        response.setMaQhns(donvi.getMaQhns());
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
//
//        List<NhBbNhapDayKhoCt> chiTiets = item.getChiTiets();
//        List<QlBienBanNdkCtLtRes> chiTietResList = new ArrayList<>();
//        for (NhBbNhapDayKhoCt chiTiet : chiTiets) {
//            QlBienBanNdkCtLtRes chiTietRes = new QlBienBanNdkCtLtRes();
//            BeanUtils.copyProperties(chiTiet, chiTietRes);
//            chiTietResList.add(chiTietRes);
//        }
//        response.setChiTiets(chiTietResList);
//        if (item.getQdgnvnxId() != null) {
//            Optional<HhQdGiaoNvuNhapxuatHdr> qdNhap = hhQdGiaoNvuNhapxuatRepository.findById(item.getQdgnvnxId());
//            if (!qdNhap.isPresent()) {
//                throw new Exception("Không tìm thấy quyết định nhập");
//            }
//            response.setSoQuyetDinhNhap(qdNhap.get().getSoQd());
//        }
//
//        if (item.getBbNghiemThuId() != null) {
//            Optional<HhBbNghiemthuKlstHdr> bbNghiemThu = hhBbNghiemthuKlstRepository.findById(item.getBbNghiemThuId());
//            if (!bbNghiemThu.isPresent()) {
//                throw new Exception("Không tìm thấy biên bản nghiệm thu");
//            }
//            response.setSoBbNghiemThu(bbNghiemThu.get().getSoBb());
//        }
//
//        if (item.getHopDongId() != null) {
//            Optional<HhHopDongHdr> hopDong = hhHopDongRepository.findById(item.getHopDongId());
//            if (!hopDong.isPresent()) {
//                throw new Exception("Không tìm thấy hợp đồng");
//            }
//            response.setSoHopDong(hopDong.get().getSoHd());
//        }
//
//        KtNganLo nganLo = null;
//        if (StringUtils.hasText(item.getMaNganLo())) {
//            nganLo = ktNganLoRepository.findFirstByMaNganlo(item.getMaNganLo());
//        }
//
//        this.thongTinNganLo(response, nganLo);
//        response.setFileDinhKems(fileDinhKemService.search(item.getId(), Collections.singleton(NhBbNhapDayKho.TABLE_NAME)));
//        return response;
//    }
//
//    private List<NhBbNhapDayKhoCt> saveListChiTiet(Long parentId, List<QlBienBanNdkCtLtReq> chiTietReqs, Map<Long, NhBbNhapDayKhoCt> mapChiTiet) throws Exception {
//        List<NhBbNhapDayKhoCt> chiTiets = new ArrayList<>();
//        for (QlBienBanNdkCtLtReq req : chiTietReqs) {
//            Long id = req.getId();
//            NhBbNhapDayKhoCt chiTiet = new NhBbNhapDayKhoCt();
//
//            if (id != null && id > 0) {
//                chiTiet = mapChiTiet.get(id);
//                if (chiTiet == null)
//                    throw new Exception("Biên bản chi tiết hàng Hóa không tồn tại.");
//                mapChiTiet.remove(id);
//            }
//
//            BeanUtils.copyProperties(req, chiTiet, "id");
//            chiTiet.setQlBienBanNdkLtId(parentId);
//            chiTiets.add(chiTiet);
//        }
//
//        if (!CollectionUtils.isEmpty(chiTiets))
//            qlBienBanNdkCtLtRepository.saveAll(chiTiets);
//
//        return chiTiets;
//    }
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public QlBienBanNhapDayKhoLtRes update(QlBienBanNhapDayKhoLtReq req) throws Exception {
//        if (req == null)
//            return null;
//
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null)
//            throw new Exception("Bad request.");
//
//        Optional<NhBbNhapDayKho> optional = qlBienBanNhapDayKhoLtRepository.findById(req.getId());
//        if (!optional.isPresent())
//            throw new Exception("Biên bản không tồn tại.");
//
//        this.validateSoBb(optional.get(), req);
//        NhBbNhapDayKho item = optional.get();
//        BeanUtils.copyProperties(req, item, "id");
//        item.setNgaySua(LocalDate.now());
//        item.setNguoiSuaId(userInfo.getId());
//        qlBienBanNhapDayKhoLtRepository.save(item);
//
//        Map<Long, NhBbNhapDayKhoCt> map = qlBienBanNdkCtLtRepository.findAllByQlBienBanNdkLtIdOrderBySttAsc(item.getId())
//                .stream().collect(Collectors.toMap(NhBbNhapDayKhoCt::getId, Function.identity()));
//
//        List<QlBienBanNdkCtLtReq> chiTietReqs = req.getChiTiets();
//        List<NhBbNhapDayKhoCt> chiTiets = this.saveListChiTiet(item.getId(), chiTietReqs, map);
//        item.setChiTiets(chiTiets);
//        if (!CollectionUtils.isEmpty(map.values()))
//            qlBienBanNdkCtLtRepository.deleteAll(map.values());
//
//        return this.buildResponse(item);
//    }
//
//    @Override
//    public QlBienBanNhapDayKhoLtRes detail(Long id) throws Exception {
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null)
//            throw new Exception("Bad request.");
//
//        Optional<NhBbNhapDayKho> optional = qlBienBanNhapDayKhoLtRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Biên bản không tồn tại.");
//
//        NhBbNhapDayKho item = optional.get();
//        item.setChiTiets(qlBienBanNdkCtLtRepository.findAllByQlBienBanNdkLtIdOrderBySttAsc(item.getId()));
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
//        Optional<NhBbNhapDayKho> optional = qlBienBanNhapDayKhoLtRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Bảng kê không tồn tại.");
//
//        NhBbNhapDayKho item = optional.get();
//        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
//            throw new Exception("Không thể xóa bảng kê đã đã duyệt");
//        }
//
//        List<NhBbNhapDayKhoCt> chiTiets = qlBienBanNdkCtLtRepository.findAllByQlBienBanNdkLtIdOrderBySttAsc(item.getId());
//        if (!CollectionUtils.isEmpty(chiTiets)) {
//            qlBienBanNdkCtLtRepository.deleteAll(chiTiets);
//        }
//
//        qlBienBanNhapDayKhoLtRepository.delete(item);
//        return true;
//    }
//
//    @Override
//    public Page<QlBienBanNhapDayKhoLtRes> search(QlBienBanNhapDayKhoLtSearchReq req) throws Exception {
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null)
//            throw new Exception("Bad request.");
//
//        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
//        List<Object[]> list = qlBienBanNhapDayKhoLtRepository.search(req);
//        List<QlBienBanNhapDayKhoLtRes> responses = new ArrayList<>();
//        for (Object[] o : list) {
//            QlBienBanNhapDayKhoLtRes response = new QlBienBanNhapDayKhoLtRes();
//            NhBbNhapDayKho item = (NhBbNhapDayKho) o[0];
//            KtNganLo nganLo = o[1] != null ? (KtNganLo) o[1] : null;
//            Long qdNhapId = (Long) o[2];
//            String soQdNhap = (String) o[3];
//            String maVatTu = (String) o[4];
//            String tenVatTu = (String) o[5];
//
//            BeanUtils.copyProperties(item, response);
//            response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//            response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//            this.thongTinNganLo(response, nganLo);
//            response.setQdgnvnxId(qdNhapId);
//            response.setSoQuyetDinhNhap(soQdNhap);
//            response.setMaVatTu(maVatTu);
//            response.setTenVatTu(tenVatTu);
//            responses.add(response);
//        }
//
//        return new PageImpl<>(responses, PageRequest.of(req.getPaggingReq().getPage(),  req.getPaggingReq().getLimit()), qlBienBanNhapDayKhoLtRepository.count(req));
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean updateStatusQd(StatusReq stReq) throws Exception {
//        UserInfo userInfo = SecurityContextService.getUser();
//        if (userInfo == null)
//            throw new Exception("Bad request.");
//        Optional<NhBbNhapDayKho> optional = qlBienBanNhapDayKhoLtRepository.findById(stReq.getId());
//        if (!optional.isPresent())
//            throw new Exception("Biên bản không tồn tại.");
//
//        NhBbNhapDayKho bienBan = optional.get();
//
//        String trangThai = bienBan.getTrangThai();
//        if (NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
//                return false;
//
//            bienBan.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId());
//            bienBan.setNguoiGuiDuyetId(userInfo.getId());
//            bienBan.setNgayGuiDuyet(LocalDate.now());
//        } else if (NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId().equals(trangThai))
//                return false;
//            bienBan.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId());
//            bienBan.setNguoiGuiDuyetId(userInfo.getId());
//            bienBan.setNgayGuiDuyet(LocalDate.now());
//        } else if (NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_KT.getId().equals(trangThai))
//                return false;
//
//            bienBan.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId());
//            bienBan.setNguoiGuiDuyetId(userInfo.getId());
//            bienBan.setNgayGuiDuyet(LocalDate.now());
//        } else if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
//                return false;
//
//            bienBan.setTrangThai(NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId());
//            bienBan.setNguoiPduyetId(userInfo.getId());
//            bienBan.setNgayPduyet(LocalDate.now());
//            bienBan.setLyDoTuChoi(stReq.getLyDo());
//        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_KTVBQ.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId().equals(trangThai))
//                return false;
//
//            bienBan.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_KTVBQ.getId());
//            bienBan.setNguoiPduyetId(userInfo.getId());
//            bienBan.setNgayPduyet(LocalDate.now());
//            bienBan.setLyDoTuChoi(stReq.getLyDo());
//        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_KT.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.TUCHOI_KT.getId().equals(trangThai))
//                return false;
//
//            bienBan.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_KT.getId());
//            bienBan.setNguoiPduyetId(userInfo.getId());
//            bienBan.setNgayPduyet(LocalDate.now());
//            bienBan.setLyDoTuChoi(stReq.getLyDo());
//        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
//                return false;
//
//            bienBan.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId());
//            bienBan.setNguoiPduyetId(userInfo.getId());
//            bienBan.setNgayPduyet(LocalDate.now());
//            bienBan.setLyDoTuChoi(stReq.getLyDo());
//        } else {
//            throw new Exception("Bad request.");
//        }
//
//        return true;
//    }
//
//    private void thongTinNganLo(QlBienBanNhapDayKhoLtRes item, KtNganLo nganLo) {
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
//        QlBienBanNhapDayKhoLtSearchReq req = new QlBienBanNhapDayKhoLtSearchReq();
//        this.prepareSearchReq(req, userInfo, null, req.getTrangThais());
//        BaseNhapHangCount count = new BaseNhapHangCount();
//
//        count.setTatCa(qlBienBanNhapDayKhoLtRepository.count(req));
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
//        qlBienBanNdkCtLtRepository.deleteByQlBienBanNdkLtIdIn(req.getIds());
//        qlBienBanNhapDayKhoLtRepository.deleteByIdIn(req.getIds());
//        return true;
//    }
//
//    @Override
//    public boolean exportToExcel(QlBienBanNhapDayKhoLtSearchReq objReq, HttpServletResponse response) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
//        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
//        List<QlBienBanNhapDayKhoLtRes> list = this.search(objReq).get().collect(Collectors.toList());
//
//        if (CollectionUtils.isEmpty(list))
//            return true;
//
//        String[] rowsName = new String[] { STT, SO_BIEN_BAN, SO_QUYET_DINH_NHAP, NGAY_NHAP_DAY_KHO,
//                DIEM_KHO, NHA_KHO, NGAN_KHO, NGAN_LO, TRANG_THAI};
//        String filename = "Danh_sach_bien_ban_nhap_day_kho.xlsx";
//
//        List<Object[]> dataList = new ArrayList<Object[]>();
//        Object[] objs = null;
//
//        try {
//            for (int i = 0; i < list.size(); i++) {
//                QlBienBanNhapDayKhoLtRes item = list.get(i);
//                objs = new Object[rowsName.length];
//                objs[0] = i;
//                objs[1] = item.getSoBienBan();
//                objs[2] = item.getSoQuyetDinhNhap();
//                objs[3] = LocalDateTimeUtils.localDateToString(item.getNgayNhapDayKho());
//                objs[4] = item.getTenDiemKho();
//                objs[5] = item.getTenNhaKho();
//                objs[6] = item.getTenNganKho();
//                objs[7] = item.getTenNganLo();
//                objs[8] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
//                dataList.add(objs);
//            }
//
//            ExportExcel ex = new ExportExcel(SHEET_BIEN_BAN_NHAP_DAY_KHO, filename, rowsName, dataList, response);
//            ex.export();
//        } catch (Exception e) {
//            log.error("Error export", e);
//            return false;
//        }
//
//        return true;
//    }
//
//    private void validateSoBb(NhBbNhapDayKho update, QlBienBanNhapDayKhoLtReq req) throws Exception {
//        String soBB = req.getSoBienBan();
//        if (!StringUtils.hasText(soBB))
//            return;
//        if (update == null || (StringUtils.hasText(update.getSoBienBan()) && !update.getSoBienBan().equalsIgnoreCase(soBB))) {
//            Optional<NhBbNhapDayKho> optional = qlBienBanNhapDayKhoLtRepository.findFirstBySoBienBan(soBB);
//            Long updateId = Optional.ofNullable(update).map(NhBbNhapDayKho::getId).orElse(null);
//            if (optional.isPresent() && !optional.get().getId().equals(updateId))
//                throw new Exception("Số biên bản " + soBB + " đã tồn tại");
//        }
//    }
//
//    @Override
//    public Integer getSo() throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Integer so = qlBienBanNhapDayKhoLtRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
//        so = Optional.ofNullable(so).orElse(0);
//        so = so + 1;
//        return so;
//    }
//
//    @Override
//    public BaseNhapHangCount count(Set<String> maDvis) throws Exception {
//        QlBienBanNhapDayKhoLtSearchReq countReq = new QlBienBanNhapDayKhoLtSearchReq();
//        countReq.setMaDvis(maDvis);
//        BaseNhapHangCount count = new BaseNhapHangCount();
//
//        countReq.setMaVatTuCha(Contains.LOAI_VTHH_THOC);
//        count.setThoc(qlBienBanNhapDayKhoLtRepository.count(countReq));
//        countReq.setMaVatTuCha(Contains.LOAI_VTHH_GAO);
//        count.setGao(qlBienBanNhapDayKhoLtRepository.count(countReq));
//        countReq.setMaVatTuCha(Contains.LOAI_VTHH_MUOI);
//        count.setMuoi(qlBienBanNhapDayKhoLtRepository.count(countReq));
//        return count;
//    }
}
