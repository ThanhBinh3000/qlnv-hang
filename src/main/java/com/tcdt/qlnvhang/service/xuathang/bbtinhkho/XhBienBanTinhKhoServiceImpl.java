package com.tcdt.qlnvhang.service.xuathang.bbtinhkho;

import com.tcdt.qlnvhang.entities.xuathang.bbtinhkho.XhBienBanTinhKho;
import com.tcdt.qlnvhang.entities.xuathang.bbtinhkho.XhBienBanTinhKhoCt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.khotang.*;
import com.tcdt.qlnvhang.repository.xuathang.bbtinhkho.XhBienBanTinhKhoCtRepository;
import com.tcdt.qlnvhang.repository.xuathang.bbtinhkho.XhBienBanTinhKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhBienBanTinhKhoSearchReq;
import com.tcdt.qlnvhang.request.xuathang.bbtinhkho.XhBienBanTinhKhoReq;
import com.tcdt.qlnvhang.response.xuathang.bbtinhkho.XhBienBanTinhKhoCtRes;
import com.tcdt.qlnvhang.response.xuathang.bbtinhkho.XhBienBanTinhKhoRes;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class XhBienBanTinhKhoServiceImpl implements XhBienBanTinhKhoService {

    private static final String SHEET_BIEN_BAN_TINH_KHO = "Biên bản tịnh kho lỗi";
    private static final String STT = "STT";
    private static final String SO_QUYET_DINH = "Số Quyết Định Xuất";
    private static final String SO_BIEN_BAN = "Số Biên Bản";
    private static final String NGAY_BIEN_BAN = "Ngày Biên Bản";
    private static final String DIEM_KHO = "Điểm Kho";
    private static final String NHA_KHO = "Nhà Kho";
    private static final String NGAN_KHO = "Ngăn Kho";
    private static final String LO_KHO = "Lô Kho";
    private static final String TRANG_THAI = "Trạng Thái";
    private final String MA_DS = "/BBTK-CCDTVP";
    @Autowired
    XhBienBanTinhKhoRepository xhBienBanTinhKhoRepository;
    @Autowired
    XhBienBanTinhKhoCtRepository xhBienBanTinhKhoCtRepository;
    @Autowired
    XhQdGiaoNvXhRepository xhQdGiaoNvXhRepository;
    @Autowired
    KtDiemKhoRepository ktDiemKho;
    @Autowired
    KtNhaKhoRepository ktNhaKho;
    @Autowired
    KtNganKhoRepository ktNganKho;
    @Autowired
    KtNganLoRepository ktNganLo;
    @Autowired
    QlnvDmVattuRepository qlnvDmVattuRepository;
    @Autowired
    KtTongKhoRepository ktTongKhoRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public XhBienBanTinhKhoRes create(XhBienBanTinhKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        XhBienBanTinhKho item = new XhBienBanTinhKho();
        item.setQdgnvnxId(req.getQdId());
        item.setMaDvi(req.getMaDvi());
        item.setCapDvi(ktTongKhoRepository.findByMaTongKho(item.getMaDvi()).get().getTenTongKho());
        Long count = xhBienBanTinhKhoRepository.getMaxId();
        if (count == null) count = 1L;
        item.setSoBienBan(count.intValue() + 1 + "/" + LocalDate.now().getYear() + MA_DS);
        item.setMaDiemkho(req.getMaDiemkho());
        item.setMaNhakho(req.getMaNhakho());
        item.setMaNgankho(req.getMaNgankho());
        item.setMaLokho(req.getMaNganlo());
        item.setMaLoaiHangHoa(req.getLoaiHangHoa());
        item.setMaChungLoaiHangHoa(req.getChungLoaiHangHoa());
        item.setKienNghi(req.getKienNghi());
        item.setNguyenNhan(req.getNguyenNhan());
        item.setNam(LocalDate.now().getYear());
        item.setNgayTao(new Date());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(TrangThaiEnum.DU_THAO.getId());

        double slNhap = 0;
        double slSoSach = 0;
        double slThuaConLaiXuatCuoi = 0;
        double slThieuConLaiXuatCuoi = 0;
        //cần lấy tỉ lệ hao dôi từ biên bản hao dôi

        //tính số lượng
        List<Object[]> fromDb = xhBienBanTinhKhoRepository.getHangTrongKho(item.getMaLokho(), item.getMaChungLoaiHangHoa());
        if (null != fromDb && !fromDb.isEmpty()) {
            Object[] data = fromDb.get(0);
            item.setSoLuongXuat(req.getSoLuongXuat());
            item.setSlConlaiXuatcuoi(req.getSoLuongThucTeConLai());
            if (data != null || data.length > 0) {
                slNhap = ((BigDecimal) data[10]).doubleValue();
                slSoSach = ((BigDecimal) data[3]).doubleValue();
                if (slSoSach > item.getSlConlaiXuatcuoi()) {
                    slThuaConLaiXuatCuoi = slSoSach - item.getSlConlaiXuatcuoi();
                    slThieuConLaiXuatCuoi = 0;
                } else {
                    slThuaConLaiXuatCuoi = 0;
                    slThieuConLaiXuatCuoi = item.getSlConlaiXuatcuoi() - slSoSach;
                }
            }
            item.setSoLuongNhap(slNhap);
            item.setSlConlaiSosach(slSoSach);
            item.setSlThuaConlai(slThuaConLaiXuatCuoi);
            item.setSlThieuConlai(slThieuConLaiXuatCuoi);
        }

        xhBienBanTinhKhoRepository.save(item);
        List<XhBienBanTinhKhoCt> ds = req.getDs().stream().map(q -> {
            XhBienBanTinhKhoCt xhBienBanTinhKhoCt = new XhBienBanTinhKhoCt();
            BeanUtils.copyProperties(q, xhBienBanTinhKhoCt, "id");
            xhBienBanTinhKhoCt.setBbTinhKhoId(item.getId());
            return xhBienBanTinhKhoCt;
        }).collect(Collectors.toList());
        xhBienBanTinhKhoCtRepository.saveAll(ds);

        XhBienBanTinhKhoRes result = this.buildResponse(item);
        result.setDs(ds.stream().map(q -> this.buildResponseCt(q)).collect(Collectors.toList()));
        return result;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public XhBienBanTinhKhoRes update(XhBienBanTinhKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        XhBienBanTinhKho item = xhBienBanTinhKhoRepository.findById(req.getId()).get();
        item.setQdgnvnxId(req.getQdId());
        item.setMaDvi(req.getMaDvi());
        item.setCapDvi(ktTongKhoRepository.findByMaTongKho(item.getMaDvi()).get().getTenTongKho());
        item.setSoLuongXuat(req.getSoLuongXuat());
        item.setSlConlaiXuatcuoi(req.getSoLuongThucTeConLai());
        item.setMaDiemkho(req.getMaDiemkho());
        item.setMaNhakho(req.getMaNhakho());
        item.setMaNgankho(req.getMaNgankho());
        item.setMaLokho(req.getMaNganlo());
        item.setMaLoaiHangHoa(req.getLoaiHangHoa());
        item.setMaChungLoaiHangHoa(req.getChungLoaiHangHoa());
        item.setKienNghi(req.getKienNghi());
        item.setNguyenNhan(req.getNguyenNhan());
        item.setNam(LocalDate.now().getYear());
        item.setNgaySua(new Date());
        item.setNguoiSuaId(userInfo.getId());

        List<Object[]> fromDb = xhBienBanTinhKhoRepository.getHangTrongKho(item.getMaLokho(), item.getMaChungLoaiHangHoa());
        if (null != fromDb && !fromDb.isEmpty()) {
            Object[] data = fromDb.get(0);
            if (data != null || data.length > 0) {
                item.setSoLuongNhap(((BigDecimal) data[10]).doubleValue());
                item.setSlConlaiSosach(((BigDecimal) data[3]).doubleValue());
                if (item.getSlConlaiSosach() > item.getSlConlaiXuatcuoi()) {
                    item.setSlThuaConlai(item.getSlConlaiSosach() - item.getSlConlaiXuatcuoi());
                    item.setSlThieuConlai(0);
                } else {
                    item.setSlThuaConlai(0);
                    item.setSlThieuConlai(item.getSlConlaiXuatcuoi() - item.getSlConlaiSosach());
                }
            }
        }

        xhBienBanTinhKhoRepository.save(item);

        xhBienBanTinhKhoCtRepository.deleteByBbTinhKhoId(item.getId());
        List<XhBienBanTinhKhoCt> ds = req.getDs().stream().map(q -> {
            XhBienBanTinhKhoCt xhBienBanTinhKhoCt = new XhBienBanTinhKhoCt();
            BeanUtils.copyProperties(q, xhBienBanTinhKhoCt, "id");
            xhBienBanTinhKhoCt.setBbTinhKhoId(item.getId());
            return xhBienBanTinhKhoCt;
        }).collect(Collectors.toList());
        xhBienBanTinhKhoCtRepository.saveAll(ds);

        XhBienBanTinhKhoRes result = this.buildResponse(item);
        result.setDs(ds.stream().map(q -> this.buildResponseCt(q)).collect(Collectors.toList()));
        return result;
    }

    @Override
    public XhBienBanTinhKhoRes detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<XhBienBanTinhKho> optional = xhBienBanTinhKhoRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản tịnh kho không tồn tại.");

        XhBienBanTinhKho item = optional.get();
        List<XhBienBanTinhKhoCtRes> ds = xhBienBanTinhKhoCtRepository.findAllByBbTinhKhoId(item.getId()).stream().map(p -> this.buildResponseCt(p)).collect(Collectors.toList());
        XhBienBanTinhKhoRes result = this.buildResponse(item);
        result.setDs(ds);
        return result;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<XhBienBanTinhKho> optional = xhBienBanTinhKhoRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản tịnh kho không tồn tại.");

        XhBienBanTinhKho item = optional.get();
        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa Biên bản tịnh kho đã đã duyệt");
        }
        xhBienBanTinhKhoCtRepository.deleteByBbTinhKhoId(item.getId());
        xhBienBanTinhKhoRepository.delete(item);
        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean deleteMultiple(DeleteReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        xhBienBanTinhKhoCtRepository.deleteByBbTinhKhoIdIn(req.getIds());
        xhBienBanTinhKhoRepository.deleteByIdIn(req.getIds());
        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateStatus(StatusReq stReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<XhBienBanTinhKho> optional = xhBienBanTinhKhoRepository.findById(stReq.getId());
        if (!optional.isPresent())
            throw new Exception("Biên bản tịnh kho không tồn tại.");

        XhBienBanTinhKho item = optional.get();
        String trangThai = item.getTrangThai();
        if (NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId());
            item.setNguoiGuiDuyetId(userInfo.getId());
            item.setNgayGuiDuyet(new Date());

        } else if (NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId());
            item.setNguoiPduyetId(userInfo.getId());
            item.setNgayPduyet(new Date());

        } else if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId());
            item.setNguoiPduyetId(userInfo.getId());
            item.setNgayPduyet(new Date());

        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_KTVBQ.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_KTVBQ.getId());
            item.setNguoiPduyetId(userInfo.getId());
            item.setNgayPduyet(new Date());
            item.setLyDoTuChoi(stReq.getLyDo());

        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId());
            item.setNguoiPduyetId(userInfo.getId());
            item.setNgayPduyet(new Date());
            item.setLyDoTuChoi(stReq.getLyDo());

        } else {
            throw new Exception("Bad request.");
        }

        return true;
    }

    @Override
    public Page<XhBienBanTinhKhoRes> search(XhBienBanTinhKhoSearchReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());

        List<XhBienBanTinhKhoRes> responses = new ArrayList<>();
        xhBienBanTinhKhoRepository.search(req).forEach(item -> {
            XhBienBanTinhKhoRes response = this.buildResponse(convertObject(((BigDecimal) item[0]).longValue()));
            responses.add(response);
        });

        return new PageImpl<>(responses, pageable, xhBienBanTinhKhoRepository.count(req));
    }

    @Override
    public boolean exportToExcel(XhBienBanTinhKhoSearchReq objReq, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
        List<XhBienBanTinhKhoRes> list = this.search(objReq).get().collect(Collectors.toList());

        if (CollectionUtils.isEmpty(list))
            return true;

        String[] rowsName = new String[]{STT, SO_BIEN_BAN, SO_QUYET_DINH, NGAY_BIEN_BAN, DIEM_KHO
                , NHA_KHO, NGAN_KHO, LO_KHO, TRANG_THAI};
        String filename = "Danh_sach_bien_ban_tinh_kho_khi_xuat_doc_kho.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;

        try {
            for (int i = 0; i < list.size(); i++) {
                XhBienBanTinhKhoRes item = list.get(i);
                objs = new Object[rowsName.length];
                objs[0] = i;
                objs[1] = item.getSoBienBan();
                objs[2] = item.getSoQd();
                objs[3] = item.getNgayLapPhieu();
                objs[4] = item.getDiemKho();
                objs[5] = item.getNhaKho();
                objs[6] = item.getNganKho();
                objs[7] = item.getLoKho();
                objs[8] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
                dataList.add(objs);
            }

            ExportExcel ex = new ExportExcel(SHEET_BIEN_BAN_TINH_KHO, filename, rowsName, dataList, response);
            ex.export();
        } catch (Exception e) {
            log.error("Error export", e);
            return false;
        }

        return true;
    }

    private XhBienBanTinhKhoRes buildResponse(XhBienBanTinhKho xhBienBanTinhKho) {
        XhBienBanTinhKhoRes xhBienBanTinhKhoRes = new XhBienBanTinhKhoRes();
        xhBienBanTinhKhoRes.setId(xhBienBanTinhKho.getId());
//        xhBienBanTinhKhoRes.setSoQd(xhQdGiaoNvXhRepository.getOne(xhBienBanTinhKho.getQdgnvnxId()).getSoQuyetDinh());
        xhBienBanTinhKhoRes.setLoaiHH(qlnvDmVattuRepository.findByMa(xhBienBanTinhKho.getMaLoaiHangHoa()).getTen());
        xhBienBanTinhKhoRes.setChungLoaiHH(qlnvDmVattuRepository.findByMa(xhBienBanTinhKho.getMaChungLoaiHangHoa()).getTen());
        xhBienBanTinhKhoRes.setTenDvi(xhBienBanTinhKho.getCapDvi());
        xhBienBanTinhKhoRes.setMaDvi(xhBienBanTinhKho.getMaDvi());
        xhBienBanTinhKhoRes.setMaDiemKho(xhBienBanTinhKho.getMaDiemkho());
        xhBienBanTinhKhoRes.setMaNhaKho(xhBienBanTinhKho.getMaNhakho());
        xhBienBanTinhKhoRes.setMaNganKho(xhBienBanTinhKho.getMaNgankho());
        xhBienBanTinhKhoRes.setMaLoKho(xhBienBanTinhKho.getMaLokho());
        xhBienBanTinhKhoRes.setDiemKho(ktDiemKho.findByMaDiemkhoIn(Collections.singleton(xhBienBanTinhKho.getMaDiemkho())).get(0).getTenDiemkho());
        xhBienBanTinhKhoRes.setNhaKho(ktNhaKho.findByMaNhakhoIn(Collections.singleton(xhBienBanTinhKho.getMaNhakho())).get(0).getTenNhakho());
        xhBienBanTinhKhoRes.setNganKho(ktNganKho.findByMaNgankhoIn(Collections.singleton(xhBienBanTinhKho.getMaNgankho())).get(0).getTenNgankho());
        xhBienBanTinhKhoRes.setLoKho(ktNganLo.findByMaNganloIn(Collections.singleton(xhBienBanTinhKho.getMaLokho())).get(0).getTenNganlo());
        xhBienBanTinhKhoRes.setSoBienBan(xhBienBanTinhKho.getSoBienBan());
        xhBienBanTinhKhoRes.setSoLuongNhap(xhBienBanTinhKho.getSoLuongNhap());
        xhBienBanTinhKhoRes.setSoLuongXuat(xhBienBanTinhKho.getSoLuongXuat());
        xhBienBanTinhKhoRes.setSlConlaiSosach(xhBienBanTinhKho.getSlConlaiSosach());
        xhBienBanTinhKhoRes.setSlConlaiXuatcuoi(xhBienBanTinhKho.getSlConlaiXuatcuoi());
        xhBienBanTinhKhoRes.setSlThuaConlai(xhBienBanTinhKho.getSlThuaConlai());
        xhBienBanTinhKhoRes.setSlThieuConlai(xhBienBanTinhKho.getSlThieuConlai());
        xhBienBanTinhKhoRes.setNguyenNhan(xhBienBanTinhKho.getNguyenNhan());
        xhBienBanTinhKhoRes.setKienNghi(xhBienBanTinhKho.getKienNghi());
        xhBienBanTinhKhoRes.setNgayLapPhieu(xhBienBanTinhKho.getNgayTao());
        xhBienBanTinhKhoRes.setTrangThai(xhBienBanTinhKho.getTrangThai());
        xhBienBanTinhKhoRes.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(xhBienBanTinhKhoRes.getTrangThai()));
        xhBienBanTinhKhoRes.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(xhBienBanTinhKhoRes.getTrangThai()));
        return xhBienBanTinhKhoRes;
    }

    private XhBienBanTinhKhoCtRes buildResponseCt(XhBienBanTinhKhoCt xhBienBanTinhKhoct) {
        XhBienBanTinhKhoCtRes xhBienBanTinhKhoCtRes = new XhBienBanTinhKhoCtRes();
        BeanUtils.copyProperties(xhBienBanTinhKhoct, xhBienBanTinhKhoCtRes);
        return xhBienBanTinhKhoCtRes;
    }

    private XhBienBanTinhKho convertObject(Long o) {
        XhBienBanTinhKho xhBienBanTinhKho = xhBienBanTinhKhoRepository.findById(o).get();

//        xhBienBanTinhKho.setId(((BigDecimal) o[0]).longValue());
//        xhBienBanTinhKho.setQdgnvnxId(((BigDecimal) o[1]).longValue());
//        if (o[2] != null) xhBienBanTinhKho.setMaDvi(o[2].toString());
//        if (o[3] != null) xhBienBanTinhKho.setCapDvi(o[3].toString());
//        if (o[4] != null) xhBienBanTinhKho.setSoBienBan(o[4].toString());
//        if (o[5] != null) xhBienBanTinhKho.setSoLuongNhap(((BigDecimal) o[5]).doubleValue());
//        if (o[6] != null) xhBienBanTinhKho.setSoLuongXuat(((BigDecimal) o[6]).doubleValue());
//        if (o[7] != null) xhBienBanTinhKho.setMaLokho(o[7].toString());
//        if (o[8] != null) xhBienBanTinhKho.setMaNgankho(o[8].toString());
//        if (o[9] != null) xhBienBanTinhKho.setMaNhakho(o[9].toString());
//        if (o[10] != null) xhBienBanTinhKho.setMaDiemkho(o[10].toString());
//        if (o[11] != null) xhBienBanTinhKho.setMaChungLoaiHangHoa(o[11].toString());
//        if (o[12] != null) xhBienBanTinhKho.setMaLoaiHangHoa(o[12].toString());
//        if (o[13] != null) xhBienBanTinhKho.setKienNghi(o[13].toString());
//        if (o[14] != null) xhBienBanTinhKho.setNguyenNhan(o[14].toString());
//        if (o[15] != null) xhBienBanTinhKho.setTrangThai(o[15].toString());
//        if (o[16] != null) xhBienBanTinhKho.setLyDoTuChoi(o[16].toString());
//        if (o[17] != null) xhBienBanTinhKho.setNguoiTaoId(((BigDecimal) o[17]).longValue());
//        if (o[18] != null)
//            xhBienBanTinhKho.setNgayTao(LocalDate.parse(o[18].toString().split(" ")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        if (o[19] != null) xhBienBanTinhKho.setNguoiSuaId(((BigDecimal) o[19]).longValue());
//        if (o[20] != null)
//            xhBienBanTinhKho.setNgaySua(LocalDate.parse(o[20].toString().split(" ")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        if (o[21] != null) xhBienBanTinhKho.setNguoiGuiDuyetId(((BigDecimal) o[21]).longValue());
//        if (o[22] != null)
//            xhBienBanTinhKho.setNgayGuiDuyet(LocalDate.parse(o[22].toString().split(" ")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        if (o[23] != null) xhBienBanTinhKho.setNguoiPduyetId(((BigDecimal) o[23]).longValue());
//        if (o[24] != null)
//            xhBienBanTinhKho.setNgayPduyet(LocalDate.parse(o[24].toString().split(" ")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        if (o[25] != null) xhBienBanTinhKho.setSo(((BigDecimal) o[25]).intValue());
//        if (o[26] != null) xhBienBanTinhKho.setNam(((BigDecimal) o[26]).intValue());
//        if (o[27] != null) xhBienBanTinhKho.setSlConlaiSosach(((BigDecimal) o[27]).doubleValue());
//        if (o[28] != null) xhBienBanTinhKho.setSlConlaiXuatcuoi(((BigDecimal) o[28]).doubleValue());
//        if (o[29] != null) xhBienBanTinhKho.setSlThuaConlai(((BigDecimal) o[29]).doubleValue());
//        if (o[30] != null) xhBienBanTinhKho.setSlThieuConlai(((BigDecimal) o[30]).doubleValue());

        return xhBienBanTinhKho;
    }
}
