package com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.hosokythuat;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau.BienBanLayMau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoBienBan;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoKyThuat;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoKyThuatCt;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbanguihang.NhBienBanGuiHangCt;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.bbanlaymau.BienBanLayMauRepository;
import com.tcdt.qlnvhang.repository.kiemtrachatluong.NhHoSoBienBanRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.hopdong.HhHopDongRepository;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.vattu.hosokythuat.NhHoSoKyThuatCtRepository;
import com.tcdt.qlnvhang.repository.vattu.hosokythuat.NhHoSoKyThuatRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.kiemtrachatluong.NhHoSoBienBanPreview;
import com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.kiemtrachatluong.NhHoSoKyThuatCtPreview;
import com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.kiemtrachatluong.NhHoSoKyThuatPreview;
import com.tcdt.qlnvhang.request.object.vattu.bienbanguihang.NhBienBanGuiHangCtReq;
import com.tcdt.qlnvhang.request.object.vattu.bienbanguihang.NhBienBanGuiHangReq;
import com.tcdt.qlnvhang.request.object.vattu.hosokythuat.NhHoSoKyThuatCtReq;
import com.tcdt.qlnvhang.request.object.vattu.hosokythuat.NhHoSoKyThuatReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.HhQdPduyetKqlcntHdr;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
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
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class NhHoSoKyThuatServiceImpl extends BaseServiceImpl implements NhHoSoKyThuatService {

    @Autowired
    private final NhHoSoKyThuatRepository nhHoSoKyThuatRepository;

    @Autowired
    private final NhHoSoKyThuatCtRepository nhHoSoKyThuatCtRepository;

    @Autowired
    private final FileDinhKemService fileDinhKemService;

    @Autowired
    private final NhHoSoBienBanRepository nhHoSoBienBanRepository;

    @Autowired
    private BienBanLayMauRepository bienBanLayMauRepository;

    @Autowired
    private HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;

    @Autowired
    private HhHopDongRepository hhHopDongRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public Page<NhHoSoKyThuat> searchPage(NhHoSoKyThuatReq objReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<NhHoSoKyThuat> nhHoSoKyThuatPage = nhHoSoKyThuatRepository.selectPage(userInfo.getDvql(), pageable);
        Map<String, String> listDanhMucDvi = getListDanhMucDvi(null, null, "01");
        nhHoSoKyThuatPage.getContent().forEach(i -> {
            Optional<BienBanLayMau> firstBySoBienBan = bienBanLayMauRepository.findFirstBySoBienBan(i.getSoBbLayMau());
            if (firstBySoBienBan.isPresent()) {
                firstBySoBienBan.get().setTenDiemKho(StringUtils.isEmpty(firstBySoBienBan.get().getMaDiemKho()) ? null : listDanhMucDvi.get(firstBySoBienBan.get().getMaDiemKho()));
                if(StringUtils.isEmpty(firstBySoBienBan.get().getMaLoKho())) {
                    firstBySoBienBan.get().setTenNganLoKho(StringUtils.isEmpty(firstBySoBienBan.get().getMaNganKho()) ? null : listDanhMucDvi.get(firstBySoBienBan.get().getMaNganKho()));
                } else {
                    firstBySoBienBan.get().setTenNganLoKho(StringUtils.isEmpty(firstBySoBienBan.get().getMaNganKho()) ? null : listDanhMucDvi.get(firstBySoBienBan.get().getMaLoKho()) + " - " + listDanhMucDvi.get(firstBySoBienBan.get().getMaNganKho()));
                }
                i.setBienBanLayMau(firstBySoBienBan.get());
            }
            List<NhHoSoBienBan> nhHoSoBienBanList = nhHoSoBienBanRepository.findAllBySoHoSoKyThuat(i.getSoHoSoKyThuat());
            if (!nhHoSoBienBanList.isEmpty()) {
                Optional<NhHoSoBienBan> data1 = nhHoSoBienBanList.stream().filter(item -> item.getLoaiBb().equals("BBKTNQ")).findFirst();
                data1.ifPresent(nhHoSoBienBan -> i.setSoBbKtnq(nhHoSoBienBan.getSoBienBan()));
                Optional<NhHoSoBienBan> data2 = nhHoSoBienBanList.stream().filter(item -> item.getLoaiBb().equals("BBKTVH")).findFirst();
                data2.ifPresent(nhHoSoBienBan -> i.setSoBbKtvh(nhHoSoBienBan.getSoBienBan()));
                Optional<NhHoSoBienBan> data3 = nhHoSoBienBanList.stream().filter(item -> item.getLoaiBb().equals("BBKTHSKT")).findFirst();
                data3.ifPresent(nhHoSoBienBan -> i.setSoBbKthskt(nhHoSoBienBan.getSoBienBan()));
            }
        });
        return nhHoSoKyThuatPage;
    }

    @Override
    @Transactional
    public NhHoSoKyThuat create(NhHoSoKyThuatReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        NhHoSoKyThuat item = new NhHoSoKyThuat();
        BeanUtils.copyProperties(req, item, "id");
//        item.setNgayTao(new Date());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setNam(LocalDate.now().getYear());
        item.setId(Long.parseLong(req.getSoHoSoKyThuat().split("/")[0]));
        nhHoSoKyThuatRepository.save(item);
        this.saveDetail(req,item.getId());
        return item;
    }

    @Transactional
    void saveDetail(NhHoSoKyThuatReq req, Long id){
        nhHoSoKyThuatCtRepository.deleteAllByHoSoKyThuatId(id);
        for(NhHoSoKyThuatCtReq ctReq : req.getChildren()){
            NhHoSoKyThuatCt ct = new NhHoSoKyThuatCt();
            BeanUtils.copyProperties(ctReq,ct,"id");
            ct.setHoSoKyThuatId(id);
            nhHoSoKyThuatCtRepository.save(ct);
            fileDinhKemService.delete(ct.getId(), Lists.newArrayList("NH_HO_SO_KY_THUAT_CT"));
            if (!DataUtils.isNullOrEmpty(ctReq.getFileDinhKem())) {
                fileDinhKemService.saveListFileDinhKem(ctReq.getFileDinhKem(), ct.getId(), "NH_HO_SO_KY_THUAT_CT");
            }
        }
    }

    @Override
    public NhHoSoKyThuat update(NhHoSoKyThuatReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhHoSoKyThuat> optional = nhHoSoKyThuatRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Hồ sơ kỹ thuật không tồn tại.");
        NhHoSoKyThuat item = optional.get();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgaySua(new Date());
        item.setNguoiSuaId(userInfo.getId());
        nhHoSoKyThuatRepository.save(item);
        this.saveDetail(req,item.getId());
        return item;
    }

    @Override
    public NhHoSoKyThuat detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhHoSoKyThuat> optional = nhHoSoKyThuatRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Hồ sơ kỹ thuật không tồn tại.");
        Map<String, String> listDanhMucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapDmucHh = getListDanhMucHangHoa();
        NhHoSoKyThuat item = optional.get();
        item.setTenDvi(listDanhMucDvi.get(item.getMaDvi()));
        Optional<BienBanLayMau> bienBanLayMau = bienBanLayMauRepository.findById(item.getIdBbLayMau());
        if (bienBanLayMau.isPresent()) {
            bienBanLayMau.get().setTenDiemKho(listDanhMucDvi.get(bienBanLayMau.get().getMaDiemKho()));
            bienBanLayMau.get().setTenNhaKho(listDanhMucDvi.get(bienBanLayMau.get().getMaNhaKho()));
            bienBanLayMau.get().setTenNganLoKho(bienBanLayMau.get().getMaLoKho() != null ? listDanhMucDvi.get(bienBanLayMau.get().getMaLoKho()) + " - " + listDanhMucDvi.get(bienBanLayMau.get().getMaNganKho()): listDanhMucDvi.get(bienBanLayMau.get().getMaNganKho()));
            bienBanLayMau.get().setTenDvi(listDanhMucDvi.get(bienBanLayMau.get().getMaDvi()));
            bienBanLayMau.get().setTenNguoiPduyet(ObjectUtils.isEmpty(bienBanLayMau.get().getNguoiPduyetId()) ? "" :userInfoRepository.findById(bienBanLayMau.get().getNguoiPduyetId()).get().getFullName());
            item.setBienBanLayMau(bienBanLayMau.get());
        }
        Optional<NhQdGiaoNvuNhapxuatHdr> qdGiaoNvuNhapxuatHdr = hhQdGiaoNvuNhapxuatRepository.findById(item.getIdQdGiaoNvNh());
        if (qdGiaoNvuNhapxuatHdr.isPresent()) {
            qdGiaoNvuNhapxuatHdr.get().setTenLoaiVthh(mapDmucHh.get(qdGiaoNvuNhapxuatHdr.get().getLoaiVthh()));
            qdGiaoNvuNhapxuatHdr.get().setTenCloaiVthh(mapDmucHh.get(qdGiaoNvuNhapxuatHdr.get().getCloaiVthh()));
            Optional<HhHopDongHdr> hhHopDongHdr = hhHopDongRepository.findById(qdGiaoNvuNhapxuatHdr.get().getIdHd());
            hhHopDongHdr.ifPresent(qdGiaoNvuNhapxuatHdr.get()::setHopDong);
            item.setQdGiaoNvuNhapxuatHdr(qdGiaoNvuNhapxuatHdr.get());
        }
        List<NhHoSoKyThuatCt> ctiet = nhHoSoKyThuatCtRepository.findByHoSoKyThuatId(item.getId());
        for (NhHoSoKyThuatCt nhHoSoKyThuatCt : ctiet) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.search(nhHoSoKyThuatCt.getId(), Collections.singletonList("NH_HO_SO_KY_THUAT_CT"));
            nhHoSoKyThuatCt.setFileDinhKem(fileDinhKem);
            if (!fileDinhKem.isEmpty()) {
                nhHoSoKyThuatCt.setFileName(fileDinhKem.get(0).getFileName());
            }
        }
        item.setChildren(ctiet);
        item.setTenNguoiTao(ObjectUtils.isEmpty(item.getNguoiTaoId()) ? null : userInfoRepository.findById(item.getNguoiTaoId()).get().getFullName());
        item.setListHoSoBienBan(nhHoSoBienBanRepository.findAllBySoHoSoKyThuat(optional.get().getSoHoSoKyThuat()));
        return item;
    }

    @Override
    public NhHoSoKyThuat approve(NhHoSoKyThuatReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhHoSoKyThuat> optional = nhHoSoKyThuatRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Hồ sơ kỹ thuật không tồn tại.");

        NhHoSoKyThuat item = optional.get();
        String trangThai = req.getTrangThai() + item.getTrangThai();
        if (
            (NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId() + NhapXuatHangTrangThaiEnum.DUTHAO.getId()).equals(trangThai) ||
            (NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId() + NhapXuatHangTrangThaiEnum.TUCHOI_TP.getId()).equals(trangThai) ||
            (NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId() + NhapXuatHangTrangThaiEnum.TUCHOI_LDC.getId()).equals(trangThai)
        ) {
            item.setNguoiGuiDuyetId(userInfo.getId());
            item.setNgayGuiDuyet(new Date());
        } else if (
            (NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId()).equals(trangThai) ||
            (NhapXuatHangTrangThaiEnum.TUCHOI_TP.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId()).equals(trangThai)
        ) {
            item.setTruongPhong(userInfo.getFullName());
            item.setLyDoTuChoi(req.getLyDoTuChoi());
        } else if (
            (NhapXuatHangTrangThaiEnum.DADUYET_LDC.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId()).equals(trangThai) ||
            (NhapXuatHangTrangThaiEnum.TUCHOI_LDC.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId()).equals(trangThai)
        ) {
            item.setNgayPduyet(new Date());
            item.setNguoiPduyetId(userInfo.getId());
            item.setLanhDaoCuc(userInfo.getFullName());
            item.setLyDoTuChoi(req.getLyDoTuChoi());
        } else {
            throw new Exception("Phê duyệt không thành công");
        }
        item.setTrangThai(req.getTrangThai());
        nhHoSoKyThuatRepository.save(item);
        return item;
    }

    @Override
    public void delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<NhHoSoKyThuat> optional = nhHoSoKyThuatRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Hồ sơ kỹ thuật không tồn tại.");

        NhHoSoKyThuat item = optional.get();
        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa biên bản đã đã duyệt");
        }
        nhHoSoKyThuatCtRepository.deleteByHoSoKyThuatIdIn(Collections.singleton(item.getId()));
        nhHoSoKyThuatRepository.delete(item);
        fileDinhKemService.delete(item.getId(), Arrays.asList(NhHoSoKyThuat.TABLE_NAME, NhHoSoKyThuat.CAN_CU));
    }

    @Override
    public void deleteMulti(List<Long> listMulti) {

    }

    @Override
    public void export(NhHoSoKyThuatReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<NhHoSoKyThuat> page = searchPage(req);
        List<NhHoSoKyThuat> data = page.getContent();
        String title = "Danh sách hồ sơ kỹ thuật";
        String[] rowsName = new String[]{"STT", "Số hồ sơ kỹ thuật", "Ngày tạo HSKT", "Điểm kho", "Ngăn/Lô kho",
                "Số BB LM/BGM", "Số QĐ giao NVNH", "Số hợp đồng", "Số BB kiểm tra ngoại quan", "Số BB kiểm tra vận hành",
                "Số BB kiểm tra HSKT", "Trạng thái"};
        String filename = "danh-sach-hskt.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            NhHoSoKyThuat qd = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = qd.getSoHoSoKyThuat();
            objs[2] = convertDate(qd.getNgayTao());
            if (qd.getBienBanLayMau() != null) {
                objs[3] = qd.getBienBanLayMau().getTenDiemKho();
                objs[4] = qd.getBienBanLayMau().getTenNganLoKho();
            }
            objs[5] = qd.getSoBbLayMau();
            objs[6] = qd.getSoQdGiaoNvNh();
            objs[7] = qd.getSoHd();
            objs[8] = qd.getSoBbKtnq();
            objs[9] = qd.getSoBbKtvh();
            objs[10] = qd.getSoBbKthskt();
            objs[11] = qd.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public ReportTemplateResponse preview(NhHoSoKyThuatReq req) throws Exception {
        NhHoSoKyThuat hoSoBienBan = detail(req.getId());
        if (hoSoBienBan == null) {
            throw new Exception("Hồ sơ kỹ thuật không tồn tại.");
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        NhHoSoKyThuatPreview object = new NhHoSoKyThuatPreview();
        BeanUtils.copyProperties(hoSoBienBan,object);
        object.setTenCloaiVthh(hoSoBienBan.getQdGiaoNvuNhapxuatHdr().getTenCloaiVthh());
        object.setTenDiemKho(hoSoBienBan.getBienBanLayMau().getTenDiemKho());
        object.setTenNganLoKho(hoSoBienBan.getBienBanLayMau().getTenNganLoKho());
        object.setNgayPduyet(Objects.isNull(hoSoBienBan.getNgayPduyet()) ? null : formatter.format(hoSoBienBan.getNgayPduyet()));
        object.setNgayTao(Objects.isNull(hoSoBienBan.getNgayTao()) ? null : formatter.format(hoSoBienBan.getNgayTao()));
        List<NhHoSoBienBanPreview> listHoSoBienBan = new ArrayList<>();
        List<NhHoSoKyThuatCtPreview> children = new ArrayList<>();
        hoSoBienBan.getChildren().forEach(item -> {
            NhHoSoKyThuatCtPreview ctPreview = new NhHoSoKyThuatCtPreview();
            ctPreview.setTenHoSo(item.getTenHoSo());
            ctPreview.setLoaiTaiLieu(item.getLoaiTaiLieu());
            ctPreview.setSoLuong(item.getSoLuong());
            ctPreview.setGhiChu(item.getGhiChu());
            ctPreview.setTgianNhap(object.getNgayTao());
            children.add(ctPreview);
        });
        hoSoBienBan.getListHoSoBienBan().forEach( item -> {
            NhHoSoBienBanPreview hoSo = new NhHoSoBienBanPreview();
            hoSo.setNgayTao(Objects.isNull(item.getNgayTao()) ? null : formatter.format(item.getNgayTao()));
            hoSo.setTgianNhap(Objects.isNull(item.getTgianNhap()) ? null : formatter.format(item.getTgianNhap()));
            hoSo.setTenBb(item.getTenBb());
            listHoSoBienBan.add(hoSo);
        });
        object.setChildren(children);
        object.setListHoSoBienBan(listHoSoBienBan);
        ReportTemplate model = findByTenFile(req.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return docxToPdfConverter.convertDocxToPdf(inputStream, object);
    }


//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean updateStatusQd(StatusReq stReq) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Optional<NhHoSoKyThuat> optional = nhHoSoKyThuatRepository.findById(stReq.getId());
//        if (!optional.isPresent())
//            throw new Exception("Hồ sơ kỹ thuật không tồn tại.");
//
//        NhHoSoKyThuat item = optional.get();
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
//        nhHoSoKyThuatRepository.save(item);
//
//        return true;
//    }
//
//    @Override
//    public Page<NhHoSoKyThuatRes> search(NhHoSoKyThuatSearchReq req) throws Exception {
//        // TODO: Bien ban giao mau
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        this.prepareSearchReq(req, userInfo, req.getCapDvis(), req.getTrangThais());
//        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//        List<Object[]> data = nhHoSoKyThuatRepository.search(req);
//        List<NhHoSoKyThuatRes> responses = new ArrayList<>();
//        for (Object[] o : data) {
//            NhHoSoKyThuatRes response = new NhHoSoKyThuatRes();
//            NhHoSoKyThuat item = (NhHoSoKyThuat) o[0];
//            Long qdNhapId = (Long) o[1];
//            String soQdNhap = (String) o[2];
//            String maVatTu = (String) o[3];
//            String tenVatTu = (String) o[4];
//            String maVatTuCha = (String) o[5];
//            String tenVatTuCha = (String) o[6];
//            BeanUtils.copyProperties(item, response);
//            response.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
//            response.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
//            response.setQdgnvnxId(qdNhapId);
//            response.setSoQuyetDinhNhap(soQdNhap);
//            response.setMaVatTu(maVatTu);
//            response.setTenVatTu(tenVatTu);
//            response.setMaVatTuCha(maVatTuCha);
//            response.setTenVatTuCha(tenVatTuCha);
//            responses.add(response);
//        }
//
//        return new PageImpl<>(responses, pageable, nhHoSoKyThuatRepository.count(req));
//    }
//
//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public boolean deleteMultiple(DeleteReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        nhHoSoKyThuatCtRepository.deleteByHoSoKyThuatIdIn(req.getIds());
//        nhHoSoKyThuatRepository.deleteByIdIn(req.getIds());
//        fileDinhKemService.deleteMultiple(req.getIds(), Arrays.asList(NhHoSoKyThuat.TABLE_NAME, NhHoSoKyThuat.CAN_CU));
//        return true;
//    }
//
//    @Override
//    public boolean exportToExcel(NhHoSoKyThuatSearchReq objReq, HttpServletResponse response) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        this.prepareSearchReq(objReq, userInfo, objReq.getCapDvis(), objReq.getTrangThais());
//        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
//        List<NhHoSoKyThuatRes> list = this.search(objReq).get().collect(Collectors.toList());
//
//        if (CollectionUtils.isEmpty(list))
//            return true;
//
//        String[] rowsName = new String[] { STT, SO_BIEN_BAN, SO_QUYET_DINH_NHAP, NGAY,
//                LOAI_HANG_HOA, CHUNG_LOAI_HANG_HO, KET_LUAT, TRANG_THAI};
//        String filename = "Danh_sach_ho_so_ky_thuat.xlsx";
//
//        List<Object[]> dataList = new ArrayList<Object[]>();
//        Object[] objs = null;
//
//        try {
//            for (int i = 0; i < list.size(); i++) {
//                NhHoSoKyThuatRes item = list.get(i);
//                objs = new Object[rowsName.length];
//                objs[0] = i;
//                objs[1] = item.getSoBienBan();
//                objs[2] = item.getSoQuyetDinhNhap();
//                objs[3] = LocalDateTimeUtils.localDateToString(item.getNgayKiemTra());
//                objs[4] = item.getTenVatTuCha();
//                objs[5] = item.getTenVatTu();
//                objs[6] = item.getKetLuan();
//                objs[7] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
//                dataList.add(objs);
//            }
//
//            ExportExcel ex = new ExportExcel(SHEET_HO_SO_KY_THUAT, filename, rowsName, dataList, response);
//            ex.export();
//        } catch (Exception e) {
//            log.error("Error export", e);
//            return false;
//        }
//
//        return true;
//    }
//
//    private void validateSoBb(NhHoSoKyThuat update, NhHoSoKyThuatReq req) throws Exception {
//        String so = req.getSoBienBan();
//        if (!StringUtils.hasText(so))
//            return;
//        if (update == null || (StringUtils.hasText(update.getSoBienBan()) && !update.getSoBienBan().equalsIgnoreCase(so))) {
//            Optional<NhHoSoKyThuat> optional = nhHoSoKyThuatRepository.findFirstBySoBienBan(so);
//            Long updateId = Optional.ofNullable(update).map(NhHoSoKyThuat::getId).orElse(null);
//            if (optional.isPresent() && !optional.get().getId().equals(updateId))
//                throw new Exception("Số biên bản " + so + " đã tồn tại");
//        }
//    }
//
//    @Override
//    public Integer getSo() throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//        Integer so = nhHoSoKyThuatRepository.findMaxSo(userInfo.getDvql(), LocalDate.now().getYear());
//        so = Optional.ofNullable(so).orElse(0);
//        so = so + 1;
//        return so;
//    }
//
//    @Override
//    public BaseNhapHangCount count(Set<String> maDvis) throws Exception {
//        NhHoSoKyThuatSearchReq countReq = new NhHoSoKyThuatSearchReq();
//        countReq.setMaDvis(maDvis);
//        BaseNhapHangCount count = new BaseNhapHangCount();
//
//        count.setVatTu(nhHoSoKyThuatRepository.count(countReq));
//        return count;
//    }
}
