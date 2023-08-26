package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bienbanguihang;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bienbanchuanbikho.NhBienBanChuanBiKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbanguihang.NhBienBanGuiHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbanguihang.NhBienBanGuiHangCt;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkhotamgui.NhPhieuNhapKhoTamGui;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.hopdong.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bienbanguihang.NhBienBanGuiHangCtRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bienbanguihang.NhBienBanGuiHangRepository;
import com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.kiemtrachatluong.NhBienBanChuanBiKhoPreview;
import com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.nhapkho.NhBienBanGuiHangPreview;
import com.tcdt.qlnvhang.request.object.vattu.bienbanguihang.NhBienBanGuiHangCtReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbanguihang.NhBienBanGuiHangReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class NhBienBanGuiHangServiceImpl extends BaseServiceImpl implements NhBienBanGuiHangService {

    @Autowired
    private final NhBienBanGuiHangRepository bienBanGuiHangRepository;

    @Autowired
    private final NhBienBanGuiHangCtRepository bienBanGuiHangCtRepository;

    @Autowired
    private final HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;

    @Autowired
    private final UserInfoRepository userInfoRepository;

    @Autowired
    private final HhHopDongRepository hhHopDongRepository;

    private static final String SHEET_BIEN_BAN_GUI_HANG = "Biên bản gửi hàng";
    private static final String STT = "STT";
    private static final String SO_BIEN_BAN = "Số Biên Bản";
    private static final String SO_QUYET_DINH_NHAP = "Số Quyết Định Nhập";
    private static final String NAM_NHAP = "Năm Nhập";
    private static final String NGAY_GUI = "Ngày Gửi";
    private static final String BEN_NHAN = "Bên Nhận";
    private static final String BEN_GIAO = "Bên Giao";
    private static final String TRANG_THAI = "Trạng Thái";

    @Override
    public Page<NhBienBanGuiHang> searchPage(NhBienBanGuiHangReq req) {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<NhBienBanGuiHang> pages = bienBanGuiHangRepository.selectPage(pageable);
        Map<String, String> listDanhMucHangHoa = getListDanhMucHangHoa();
        Map<String, String> listDanhMucDvi = getListDanhMucDvi(null, null, "01");
        pages.getContent().forEach(x -> {
            x.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(x.getTrangThai()));
            x.setTenLoaiVthh(listDanhMucHangHoa.get(x.getLoaiVthh()));
            x.setTenCloaiVthh(listDanhMucHangHoa.get(x.getCloaiVthh()));
            x.setTenDvi(listDanhMucDvi.get(x.getMaDvi()));
            x.setTenDiemKho(listDanhMucDvi.get(x.getMaDiemKho()));
            x.setTenNhaKho(listDanhMucDvi.get(x.getMaNhaKho()));
            x.setTenNganKho(listDanhMucDvi.get(x.getMaNganKho()));
            x.setTenLoKho(listDanhMucDvi.get(x.getMaLoKho()));
        });
        return pages;
    }

    @Override
    @Transactional
    public NhBienBanGuiHang create(NhBienBanGuiHangReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        NhBienBanGuiHang item = new NhBienBanGuiHang();

        NhBienBanGuiHang byIdDdiemGiaoNvNh = bienBanGuiHangRepository.findByIdDdiemGiaoNvNh(req.getIdDdiemGiaoNvNh());
        if(!ObjectUtils.isEmpty(byIdDdiemGiaoNvNh)){
            throw new Exception("Ngăn lô kho đã được tạo biên bản gửi hàng, vui lòng chọn điểm kho khác");
        }

        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(new Date());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setNam(LocalDate.now().getYear());
        item.setId(Long.parseLong(item.getSoBienBanGuiHang().split("/")[0]));
        bienBanGuiHangRepository.save(item);
        this.saveDetail(req,item.getId());
        return item;
    }

    @Transactional
    void saveDetail(NhBienBanGuiHangReq req,Long id){
        bienBanGuiHangCtRepository.deleteByBienBanGuiHangId(id);
        for(NhBienBanGuiHangCtReq ctReq : req.getChildren()){
            NhBienBanGuiHangCt ct = new NhBienBanGuiHangCt();
            BeanUtils.copyProperties(ctReq,ct,"id");
            ct.setBienBanGuiHangId(id);
            bienBanGuiHangCtRepository.save(ct);
        }
    }

    @Override
    @Transactional
    public NhBienBanGuiHang update(NhBienBanGuiHangReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        Optional<NhBienBanGuiHang> optional = bienBanGuiHangRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Biên bản gửi hàng không tồn tại.");
        }


        NhBienBanGuiHang item = optional.get();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgaySua(new Date());
        item.setNguoiSuaId(userInfo.getId());
        bienBanGuiHangRepository.save(item);
        this.saveDetail(req,item.getId());
//        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.get(), item.getId(), NhPhieuNhapKhoTamGui.TABLE_NAME);
//        item.setFileDinhKems(fileDinhKems);
        return item;
    }

    @Override
    public NhBienBanGuiHang detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhBienBanGuiHang> optional = bienBanGuiHangRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản gửi hàng không tồn tại.");

        NhBienBanGuiHang item = optional.get();
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

        List<NhBienBanGuiHangCt> byBienBanGuiHangId = bienBanGuiHangCtRepository.findByBienBanGuiHangId(item.getId());
        item.setChildren(byBienBanGuiHangId);
        return item;
    }

    @Override
    public NhBienBanGuiHang approve(NhBienBanGuiHangReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<NhBienBanGuiHang> optional = bienBanGuiHangRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu.");
        }

        NhBienBanGuiHang item = optional.get();
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
        bienBanGuiHangRepository.save(item);
        return item;
    }

    @Override
    public void delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhBienBanGuiHang> optional = bienBanGuiHangRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản gửi hàng không tồn tại.");

        NhBienBanGuiHang item = optional.get();
        if (NhapXuatHangTrangThaiEnum.DA_HOAN_THANH.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa biên bản đã đã duyệt");
        }
        bienBanGuiHangCtRepository.deleteByBienBanGuiHangIdIn(Collections.singleton(item.getId()));
        bienBanGuiHangRepository.delete(item);
    }

    @Override
    public void deleteMulti(List<Long> listMulti) {

    }

    @Override
    public void export(NhBienBanGuiHangReq req, HttpServletResponse response) throws Exception {
//        return false;
    }

    @Override
    public ReportTemplateResponse preview(NhBienBanGuiHangReq req) throws Exception {
        NhBienBanGuiHang bienBanChuanBiKho = detail(req.getId());
        if (bienBanChuanBiKho == null) {
            throw new Exception("Biên bản chuẩn bị kho không tồn tại.");
        }
        NhBienBanGuiHangPreview object = new NhBienBanGuiHangPreview();
        ReportTemplate model = findByTenFile(req.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, object);
    }

//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public NhBienBanGuiHangRes create(NhBienBanGuiHangReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.validateSoBb(null, req);
//
//        NhBienBanGuiHang item = new NhBienBanGuiHang();
//        BeanUtils.copyProperties(req, item, "id");
//        item.setNgayTao(new Date());
//        item.setNguoiTaoId(userInfo.getId());
//        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
//        item.setMaDvi(userInfo.getDvql());
//        item.setCapDvi(userInfo.getCapDvi());
//        item.setSo(getSo());
//        item.setNam(LocalDate.now().getYear());
//        item.setSoBienBan(String.format("%s/%s/%s-%s", item.getSo(), item.getNam(), "BBGH", userInfo.getMaPbb()));
//
//        bienBanGuiHangRepository.save(item);
//
//        List<NhBienBanGuiHangCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), new HashMap<>());
//        item.setChiTiets(chiTiets);
//
//        return this.buildResponse(item);
//    }
//
//    private NhBienBanGuiHangRes buildResponse(NhBienBanGuiHang item) throws Exception {
//        NhBienBanGuiHangRes res = new NhBienBanGuiHangRes();
//        List<NhBienBanGuiHangCtRes> chiTiets = new ArrayList<>();
//        BeanUtils.copyProperties(item, res);
//        res.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//        res.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//        for (NhBienBanGuiHangCt bienBanGuiHangCt : item.getChiTiets()) {
//            chiTiets.add(new NhBienBanGuiHangCtRes(bienBanGuiHangCt));
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
//
//        if (item.getHopDongId() != null) {
//            Optional<HhHopDongHdr> hd = hhHopDongRepository.findById(item.getHopDongId());
//            if (!hd.isPresent()) {
//                throw new Exception("Không tìm thấy hợp đồng");
//            }
//            res.setSoHopDong(hd.get().getSoHd());
//        }
//        return res;
//    }
//
//    private List<NhBienBanGuiHangCt> saveListChiTiet(Long parentId,
//                                                         List<NhBienBanGuiHangCtReq> chiTietReqs,
//                                                         Map<Long, NhBienBanGuiHangCt> mapChiTiet) throws Exception {
//        List<NhBienBanGuiHangCt> chiTiets = new ArrayList<>();
//        for (NhBienBanGuiHangCtReq req : chiTietReqs) {
//            Long id = req.getId();
//            NhBienBanGuiHangCt chiTiet = new NhBienBanGuiHangCt();
//
//            if (id != null && id > 0) {
//                chiTiet = mapChiTiet.get(id);
//                if (chiTiet == null)
//                    throw new Exception("Biên bản gửi hàng chi tiết không tồn tại.");
//                mapChiTiet.remove(id);
//            }
//
//            BeanUtils.copyProperties(req, chiTiet, "id");
//            chiTiet.setBienBanGuiHangId(parentId);
//            chiTiets.add(chiTiet);
//        }
//
//        if (!CollectionUtils.isEmpty(chiTiets))
//            bienBanGuiHangCtRepository.saveAll(chiTiets);
//
//        return chiTiets;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public NhBienBanGuiHangRes update(NhBienBanGuiHangReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        Optional<NhBienBanGuiHang> optional = bienBanGuiHangRepository.findById(req.getId());
//        if (!optional.isPresent())
//            throw new Exception("Biên bản gửi hàng không tồn tại.");
//
//        this.validateSoBb(optional.get(), req);
//
//        NhBienBanGuiHang item = optional.get();
//        BeanUtils.copyProperties(req, item, "id");
//        item.setNgaySua(new Date());
//        item.setNguoiSuaId(userInfo.getId());
//        bienBanGuiHangRepository.save(item);
//        Map<Long, NhBienBanGuiHangCt> mapChiTiet = bienBanGuiHangCtRepository.findByBienBanGuiHangIdIn(Collections.singleton(item.getId()))
//                .stream().collect(Collectors.toMap(NhBienBanGuiHangCt::getId, Function.identity()));
//
//        List<NhBienBanGuiHangCt> chiTiets = this.saveListChiTiet(item.getId(), req.getChiTiets(), mapChiTiet);
//        item.setChiTiets(chiTiets);
//
//        if (!CollectionUtils.isEmpty(mapChiTiet.values()))
//            bienBanGuiHangCtRepository.deleteAll(mapChiTiet.values());
//        return this.buildResponse(item);
//    }
//
//    @Override
//    public NhBienBanGuiHangRes detail(Long id) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<NhBienBanGuiHang> optional = bienBanGuiHangRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Biên bản gửi hàng không tồn tại.");
//
//        NhBienBanGuiHang item = optional.get();
//        item.setChiTiets(bienBanGuiHangCtRepository.findByBienBanGuiHangIdIn(Collections.singleton(item.getId())));
//        return this.buildResponse(item);
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean delete(Long id) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<NhBienBanGuiHang> optional = bienBanGuiHangRepository.findById(id);
//        if (!optional.isPresent())
//            throw new Exception("Biên bản gửi hàng không tồn tại.");
//
//        NhBienBanGuiHang item = optional.get();
//        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
//            throw new Exception("Không thể xóa biên bản đã đã duyệt");
//        }
//        bienBanGuiHangCtRepository.deleteByBienBanGuiHangIdIn(Collections.singleton(item.getId()));
//        bienBanGuiHangRepository.delete(item);
//        return true;
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean updateStatusQd(StatusReq stReq) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<NhBienBanGuiHang> optional = bienBanGuiHangRepository.findById(stReq.getId());
//        if (!optional.isPresent())
//            throw new Exception("Biên bản gửi hàng không tồn tại.");
//
//        NhBienBanGuiHang item = optional.get();
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
//        bienBanGuiHangRepository.save(item);
//
//        return true;
//
//    }
//
//    @Override
//    public Page<NhBienBanGuiHangRes> search(NhBienBanGuiHangSearchReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
//        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//        List<Object[]> data = bienBanGuiHangRepository.search(req);
//        List<NhBienBanGuiHangRes> responses = new ArrayList<>();
//        for (Object[] o : data) {
//            NhBienBanGuiHangRes response = new NhBienBanGuiHangRes();
//            NhBienBanGuiHang item = (NhBienBanGuiHang) o[0];
//            Long qdNhapId = (Long) o[1];
//            String soQdNhap = (String) o[2];
//            BeanUtils.copyProperties(item, response);
//            response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//            response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//            response.setQdgnvnxId(qdNhapId);
//            response.setSoQuyetDinhNhap(soQdNhap);
//            response.setNamNhap(Optional.ofNullable(item.getThoiGian()).map(LocalDateTime::getYear).orElse(LocalDateTime.now().getYear()));
//            responses.add(response);
//        }
//
//        return new PageImpl<>(responses, pageable, bienBanGuiHangRepository.count(req));
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean deleteMultiple(DeleteReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        bienBanGuiHangCtRepository.deleteByBienBanGuiHangIdIn(req.getIds());
//        bienBanGuiHangRepository.deleteByIdIn(req.getIds());
//        return true;
//    }
//
//    @Override
//    public boolean exportToExcel(NhBienBanGuiHangSearchReq objReq, HttpServletResponse response) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
//        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
//        List<NhBienBanGuiHangRes> list = this.search(objReq).get().collect(Collectors.toList());
//
//        if (CollectionUtils.isEmpty(list))
//            return true;
//
//        String[] rowsName = new String[] { STT, SO_BIEN_BAN, SO_QUYET_DINH_NHAP, NAM_NHAP,
//                NGAY_GUI, BEN_NHAN, BEN_GIAO, TRANG_THAI};
//        String filename = "Danh_sach_bien_ban_gui_hang.xlsx";
//
//        List<Object[]> dataList = new ArrayList<Object[]>();
//        Object[] objs = null;
//
//        try {
//            for (int i = 0; i < list.size(); i++) {
//                NhBienBanGuiHangRes item = list.get(i);
//                objs = new Object[rowsName.length];
//                objs[0] = i;
//                objs[1] = item.getSoBienBan();
//                objs[2] = item.getSoQuyetDinhNhap();
//                objs[3] = Optional.ofNullable(item.getThoiGian()).map(LocalDateTime::getYear).orElse(LocalDate.now().getYear());
//                objs[4] = LocalDateTimeUtils.localDateToString(item.getNgayGui());
//                objs[5] = item.getBenNhan();
//                objs[6] = item.getBenGiao();
//                objs[7] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
//                dataList.add(objs);
//            }
//
//            ExportExcel ex = new ExportExcel(SHEET_BIEN_BAN_GUI_HANG, filename, rowsName, dataList, response);
//            ex.export();
//        } catch (Exception e) {
//            log.error("Error export", e);
//            return false;
//        }
//
//        return true;
//    }
//
//    private void validateSoBb(NhBienBanGuiHang update, NhBienBanGuiHangReq req) throws Exception {
//        String so = req.getSoBienBan();
//        if (!StringUtils.hasText(so))
//            return;
//        if (update == null || (StringUtils.hasText(update.getSoBienBan()) && !update.getSoBienBan().equalsIgnoreCase(so))) {
//            Optional<NhBienBanGuiHang> optional = bienBanGuiHangRepository.findFirstBySoBienBan(so);
//            Long updateId = Optional.ofNullable(update).map(NhBienBanGuiHang::getId).orElse(null);
//            if (optional.isPresent() && !optional.get().getId().equals(updateId))
//                throw new Exception("Số biên bản " + so + " đã tồn tại");
//        }
//    }
//
//    @Override
//    public Integer getSo() throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Integer so = bienBanGuiHangRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
//        so = Optional.ofNullable(so).orElse(0);
//        so = so + 1;
//        return so;
//    }
//
//    @Override
//    public BaseNhapHangCount count(Set<String> maDvis) throws Exception {
//        NhBienBanGuiHangSearchReq countReq = new NhBienBanGuiHangSearchReq();
//        countReq.setMaDvis(maDvis);
//        BaseNhapHangCount count = new BaseNhapHangCount();
//
//        count.setVatTu(bienBanGuiHangRepository.count(countReq));
//        return count;
//    }
}
