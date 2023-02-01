package com.tcdt.qlnvhang.service.xuathang.daugia.nhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXh;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong.XhHopDongHdrRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDdiemRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhQdGiaoNvuXuatSearchReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatCtReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatReq;
import com.tcdt.qlnvhang.response.IdAndNameDto;
import com.tcdt.qlnvhang.response.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatCtRes;
import com.tcdt.qlnvhang.response.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatRes;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.hopdong.XhHopDongHdr;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.khotang.KtDiemKho;
import com.tcdt.qlnvhang.table.khotang.KtNganKho;
import com.tcdt.qlnvhang.table.khotang.KtNganLo;
import com.tcdt.qlnvhang.table.khotang.KtNhaKho;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.LocalDateTimeUtils;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class XhQdGiaoNvXhServiceImpl extends BaseServiceImpl implements XhQdGiaoNvXhService {

    @Override
    public Page<XhQdGiaoNvXh> searchPage(XhQdGiaoNvuXuatReq req) throws Exception {
        return null;
    }

    @Override
    public List<XhQdGiaoNvXh> searchAll(XhQdGiaoNvuXuatReq req) {
        return null;
    }

    @Override
    public XhQdGiaoNvXh create(XhQdGiaoNvuXuatReq req) throws Exception {
        return null;
    }

    @Override
    public XhQdGiaoNvXh update(XhQdGiaoNvuXuatReq req) throws Exception {
        return null;
    }

    @Override
    public XhQdGiaoNvXh detail(Long id) throws Exception {
        return null;
    }

    @Override
    public XhQdGiaoNvXh approve(XhQdGiaoNvuXuatReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {

    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(XhQdGiaoNvuXuatReq req, HttpServletResponse response) throws Exception {

    }
//    private final XhQdGiaoNvXhRepository xhQdGiaoNvXhRepository;
//    private final XhQdGiaoNvXhDtlRepository xhQdGiaoNvXhDtlRepository;
//    private final XhQdGiaoNvXhDdiemRepository xhQdGiaoNvXhDdiemRepository;
//    private final XhHopDongHdrRepository hopDongRepository;
//    private final FileDinhKemService fileDinhKemService;
//    private final KtNganLoRepository ktNganLoRepository;
//
//    private static final String SHEET_QUYET_DINH_GIAO_NHIEM_VU_XUAT_HANG = "Quyết định giao nhiệm vụ xuất hàng";
//    private static final String STT = "STT";
//    private static final String SO_QUYET_DINH = "Số Quyết Định";
//    private static final String TRICH_YEU = "Trích Yếu";
//    private static final String NGAY_QUYET_DINH = "Ngày Quyết Định";
//    private static final String NAM_XUAT = "Năm Xuất";
//    private static final String TRANG_THAI = "Trạng Thái";
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public XhQdGiaoNvuXuatRes create(XhQdGiaoNvuXuatReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.validateSoQuyetDinh(null, req);
//
//        XhQdGiaoNvuXuat item = new XhQdGiaoNvuXuat();
//        BeanUtils.copyProperties(req, item, "id");
//        item.setNgayTao(new Date());
//        item.setNguoiTaoId(userInfo.getId());
//        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
//        item.setMaDvi(userInfo.getDvql());
//        item.setCapDvi(userInfo.getCapDvi());
//        xhQdGiaoNvXhRepository.save(item);
//
//        List<XhQdGiaoNvuXuatCt> chiTiets = this.saveListChiTiet(item.getId(), req.getCts(), new HashMap<>());
//        item.setCts(chiTiets);
//        item.setCt1s(this.saveListChiTiet1(item.getId(), req.getHopDongIds()));
//
//        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), item.getId(), XhQdGiaoNvuXuat.TABLE_NAME);
//        item.setFileDinhKems(fileDinhKems);
//        return this.buildResponse(item);
//    }
//
//    private List<XhQdGiaoNvuXuatCt1> saveListChiTiet1(Long parentId, List<Long> hopDongIds) {
//        xhQdGiaoNvXhDdiemRepository.deleteByQdgnvxIdIn(Collections.singleton(parentId));
//        List<XhQdGiaoNvuXuatCt1> ct1s = new ArrayList<>();
//        hopDongIds.forEach(id -> {
//            XhQdGiaoNvuXuatCt1 ct1 = new XhQdGiaoNvuXuatCt1();
//            ct1.setHopDongId(id);
//            ct1.setQdgnvxId(parentId);
//            ct1s.add(ct1);
//
//        });
//
//        if (!CollectionUtils.isEmpty(ct1s))
//            xhQdGiaoNvXhDdiemRepository.saveAll(ct1s);
//        return ct1s;
//    }
//
//    private List<XhQdGiaoNvuXuatCt> saveListChiTiet(Long parentId,
//                                               List<XhQdGiaoNvuXuatCtReq> chiTietReqs,
//                                               Map<Long, XhQdGiaoNvuXuatCt> mapChiTiet) throws Exception {
//        List<XhQdGiaoNvuXuatCt> chiTiets = new ArrayList<>();
//        for (XhQdGiaoNvuXuatCtReq req : chiTietReqs) {
//            Long id = req.getId();
//            XhQdGiaoNvuXuatCt chiTiet = new XhQdGiaoNvuXuatCt();
//
//            if (id != null && id > 0) {
//                chiTiet = mapChiTiet.get(id);
//                if (chiTiet == null)
//                    throw new Exception("Quyết định giao nhiệm vụ xuất chi tiết không tồn tại.");
//                mapChiTiet.remove(id);
//            }
//
//            BeanUtils.copyProperties(req, chiTiet, "id");
//            chiTiet.setQdgnvxId(parentId);
//            chiTiets.add(chiTiet);
//        }
//
//        if (!CollectionUtils.isEmpty(chiTiets))
//            xhQdGiaoNvXhDtlRepository.saveAll(chiTiets);
//
//        return chiTiets;
//    }
//
//
//    private XhQdGiaoNvuXuatRes buildResponse(XhQdGiaoNvuXuat item) throws Exception {
//        XhQdGiaoNvuXuatRes res = new XhQdGiaoNvuXuatRes();
//        List<XhQdGiaoNvuXuatCtRes> chiTiets = new ArrayList<>();
//        BeanUtils.copyProperties(item, res);
//        res.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//        res.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//
//        Set<String> maNganLos = item.getCts().stream().map(XhQdGiaoNvuXuatCt::getMaNganLo).collect(Collectors.toSet());
//        Map<String, KtNganLo> mapNganLo = new HashMap<>();
//        if (!CollectionUtils.isEmpty(maNganLos)) {
//            mapNganLo = ktNganLoRepository.findByMaNganloIn(maNganLos)
//                    .stream().collect(Collectors.toMap(KtNganLo::getMaNganlo, Function.identity()));
//        }
//
//        Map<String,String> mapVthh = getListDanhMucHangHoa();
//        for (XhQdGiaoNvuXuatCt ct : item.getCts()) {
//            XhQdGiaoNvuXuatCtRes xhQdGiaoNvuXuatCtRes = new XhQdGiaoNvuXuatCtRes(ct);
//            xhQdGiaoNvuXuatCtRes.setTenVatTuCha(mapVthh.get(ct.getMaVatTuCha()));
//            xhQdGiaoNvuXuatCtRes.setTenVatTu(mapVthh.get(ct.getMaVatTu()));
//            xhQdGiaoNvuXuatCtRes.setMaVatTuCha(ct.getMaVatTuCha());
//            xhQdGiaoNvuXuatCtRes.setMaVatTu(ct.getMaVatTu());
//            this.thongTinNganLo(xhQdGiaoNvuXuatCtRes, mapNganLo.get(ct.getMaNganLo()));
//            chiTiets.add(xhQdGiaoNvuXuatCtRes);
//        }
//        res.setCts(chiTiets);
//        this.setThongTinDonVi(res, item.getMaDvi());
//
//        res.setHopDongIds(item.getCt1s().stream().map(XhQdGiaoNvuXuatCt1::getHopDongId).collect(Collectors.toList()));
//        List<XhHopDongHdr> hopDongs = hopDongRepository.findAllById(res.getHopDongIds());
//        res.setHopDongs(hopDongs.stream().map(h -> new IdAndNameDto(h.getId(), h.getSoHd())).collect(Collectors.toList()));
//        return res;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public XhQdGiaoNvuXuatRes update(XhQdGiaoNvuXuatReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        Optional<XhQdGiaoNvuXuat> optional = xhQdGiaoNvXhRepository.findById(req.getId());
//        if (!optional.isPresent())
//            throw new Exception("Quyết định giao nhiệm vụ xuất không tồn tại.");
//
//        this.validateSoQuyetDinh(optional.get(), req);
//
//        XhQdGiaoNvuXuat item = optional.get();
//        BeanUtils.copyProperties(req, item, "id", "so", "nam");
//        item.setNgaySua(new Date());
//        item.setNguoiSuaId(userInfo.getId());
//        xhQdGiaoNvXhRepository.save(item);
//
//        Map<Long, XhQdGiaoNvuXuatCt> mapChiTiet = xhQdGiaoNvXhDtlRepository.findByQdgnvxIdIn(Collections.singleton(item.getId()))
//                .stream().collect(Collectors.toMap(XhQdGiaoNvuXuatCt::getId, Function.identity()));
//
//        List<XhQdGiaoNvuXuatCt> chiTiets = this.saveListChiTiet(item.getId(), req.getCts(), mapChiTiet);
//        item.setCts(chiTiets);
//        if (!CollectionUtils.isEmpty(mapChiTiet.values()))
//            xhQdGiaoNvXhDtlRepository.deleteAll(mapChiTiet.values());
//
//        item.setCt1s(this.saveListChiTiet1(item.getId(), req.getHopDongIds()));
//
//        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), item.getId(), XhQdGiaoNvuXuat.TABLE_NAME);
//        item.setFileDinhKems(fileDinhKems);
//        return this.buildResponse(item);
//    }
//
//    @Override
//    public XhQdGiaoNvuXuatRes detail(Long id) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<XhQdGiaoNvuXuat> optional = xhQdGiaoNvXhRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Quyết định giao nhiệm vụ xuất không tồn tại.");
//
//        XhQdGiaoNvuXuat item = optional.get();
//        item.setCts(xhQdGiaoNvXhDtlRepository.findByQdgnvxIdIn(Collections.singleton(item.getId())));
//        item.setCt1s(xhQdGiaoNvXhDdiemRepository.findByQdgnvxIdIn(Collections.singleton(item.getId())));
//        item.setFileDinhKems(fileDinhKemService.search(item.getId(), Collections.singleton(XhQdGiaoNvuXuat.TABLE_NAME)));
//        return this.buildResponse(item);
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean delete(Long id) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<XhQdGiaoNvuXuat> optional = xhQdGiaoNvXhRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Quyết định giao nhiệm vụ xuất không tồn tại.");
//
//        XhQdGiaoNvuXuat item = optional.get();
//        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
//            throw new Exception("Không thể xóa Quyết định giao nhiệm vụ xuất đã đã duyệt");
//        }
//        xhQdGiaoNvXhDtlRepository.deleteByQdgnvxIdIn(Collections.singleton(item.getId()));
//        xhQdGiaoNvXhDdiemRepository.deleteByQdgnvxIdIn(Collections.singleton(item.getId()));
//        xhQdGiaoNvXhRepository.delete(item);
//        return true;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean updateStatusQd(StatusReq stReq) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<XhQdGiaoNvuXuat> optional = xhQdGiaoNvXhRepository.findById(stReq.getId());
//        if (!optional.isPresent())
//            throw new Exception("Quyết định giao nhiệm vụ xuất không tồn tại.");
//
//        XhQdGiaoNvuXuat item = optional.get();
//        String trangThai = item.getTrangThai();
//        if (NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
//                return false;
//
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId());
//            item.setNguoiGuiDuyetId(userInfo.getId());
//            item.setNgayGuiDuyet(new Date());
//
//        } else if (NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId().equals(trangThai))
//                return false;
//
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId());
//            item.setNguoiPduyetId(userInfo.getId());
//            item.setNgayPduyet(new Date());
//
//        } else if (NhapXuatHangTrangThaiEnum.BAN_HANH.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId().equals(trangThai))
//                return false;
//
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.BAN_HANH.getId());
//            item.setNguoiPduyetId(userInfo.getId());
//            item.setNgayPduyet(new Date());
//
//        }else if (NhapXuatHangTrangThaiEnum.TUCHOI_TP.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId().equals(trangThai))
//                return false;
//
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_TP.getId());
//            item.setNguoiPduyetId(userInfo.getId());
//            item.setNgayPduyet(new Date());
//
//        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_LDC.getId().equals(stReq.getTrangThai())) {
//            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId().equals(trangThai))
//                return false;
//
//            item.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_LDC.getId());
//            item.setNguoiPduyetId(userInfo.getId());
//            item.setNgayPduyet(new Date());
//
//        } else {
//            throw new Exception("Bad request.");
//        }
//
//        return true;
//    }
//
//    @Override
//    public Page<XhQdGiaoNvuXuatRes> search(XhQdGiaoNvuXuatSearchReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
//        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//        List<XhQdGiaoNvuXuatRes> responses = new ArrayList<>();
//        xhQdGiaoNvXhRepository.search(req, userInfo.getCapDvi()).forEach(item -> {
//            XhQdGiaoNvuXuatRes response = new XhQdGiaoNvuXuatRes();
//            BeanUtils.copyProperties(item, response);
//            response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//            response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//            responses.add(response);
//        });
//
//        return new PageImpl<>(responses, pageable, xhQdGiaoNvXhRepository.count(req, userInfo.getCapDvi()));
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean deleteMultiple(DeleteReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        xhQdGiaoNvXhDtlRepository.deleteByQdgnvxIdIn(req.getIds());
//        xhQdGiaoNvXhDdiemRepository.deleteByQdgnvxIdIn(req.getIds());
//        fileDinhKemService.deleteMultiple(req.getIds(), Collections.singleton(XhQdGiaoNvuXuat.TABLE_NAME));
//        xhQdGiaoNvXhRepository.deleteByIdIn(req.getIds());
//        return true;
//    }
//
//    @Override
//    public boolean exportToExcel(XhQdGiaoNvuXuatSearchReq objReq, HttpServletResponse response) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
//        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
//        List<XhQdGiaoNvuXuatRes> list = this.search(objReq).get().collect(Collectors.toList());
//
//        if (CollectionUtils.isEmpty(list))
//            return true;
//
//        String[] rowsName = new String[] { STT, SO_QUYET_DINH, TRICH_YEU, NGAY_QUYET_DINH,
//                NAM_XUAT, TRANG_THAI};
//        String filename = "Danh_sach_quyet_dinh_giao_nhiem_vu_xuat_hang.xlsx";
//
//        List<Object[]> dataList = new ArrayList<Object[]>();
//        Object[] objs = null;
//
//        try {
//            for (int i = 0; i < list.size(); i++) {
//                XhQdGiaoNvuXuatRes item = list.get(i);
//                objs = new Object[rowsName.length];
//                objs[0] = i;
//                objs[1] = item.getSoQuyetDinh();
//                objs[2] = item.getTrichYeu();
//                objs[3] = LocalDateTimeUtils.localDateToString(item.getNgayQuyetDinh());
//                objs[4] = item.getNamXuat();
//                objs[5] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
//                dataList.add(objs);
//            }
//
//            ExportExcel ex = new ExportExcel(SHEET_QUYET_DINH_GIAO_NHIEM_VU_XUAT_HANG, filename, rowsName, dataList, response);
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
//        Integer so = xhQdGiaoNvXhRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
//        so = Optional.ofNullable(so).orElse(0);
//        so = so + 1;
//        return so;
//    }
//
//    private void validateSoQuyetDinh(XhQdGiaoNvuXuat update, XhQdGiaoNvuXuatReq req) throws Exception {
//        String so = req.getSoQuyetDinh();
//        if (!StringUtils.hasText(so))
//            return;
//        if (update == null || (StringUtils.hasText(update.getSoQuyetDinh()) && !update.getSoQuyetDinh().equalsIgnoreCase(so))) {
//            Optional<XhQdGiaoNvuXuat> optional = xhQdGiaoNvXhRepository.findFirstBySoQuyetDinh(so);
//            Long updateId = Optional.ofNullable(update).map(XhQdGiaoNvuXuat::getId).orElse(null);
//            if (optional.isPresent() && !optional.get().getId().equals(updateId))
//                throw new Exception("Số Quyết định giao nhiệm vụ xuất " + so + " đã tồn tại");
//        }
//    }
//
//    private void thongTinNganLo(XhQdGiaoNvuXuatCtRes item, KtNganLo nganLo) {
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
}
