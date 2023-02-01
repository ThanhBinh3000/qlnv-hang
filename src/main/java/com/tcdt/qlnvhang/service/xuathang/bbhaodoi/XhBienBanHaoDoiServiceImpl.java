package com.tcdt.qlnvhang.service.xuathang.bbhaodoi;

import com.tcdt.qlnvhang.entities.xuathang.bbhaodoi.XhBienBanHaoDoi;
import com.tcdt.qlnvhang.entities.xuathang.bbhaodoi.XhBienBanHaoDoiCt;
import com.tcdt.qlnvhang.entities.xuathang.bbtinhkho.XhBienBanTinhKho;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiEnum;
import com.tcdt.qlnvhang.repository.khotang.*;
import com.tcdt.qlnvhang.repository.xuathang.bbhaodoi.XhBienBanHaoDoiCtRepository;
import com.tcdt.qlnvhang.repository.xuathang.bbhaodoi.XhBienBanHaoDoiRepository;
import com.tcdt.qlnvhang.repository.xuathang.bbtinhkho.XhBienBanTinhKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhBienBanHaoDoiSearchReq;
import com.tcdt.qlnvhang.request.xuathang.bbhaodoi.XhBienBanHaoDoiReq;
import com.tcdt.qlnvhang.response.xuathang.bbhaodoi.XhBienBanHaoDoiCtRes;
import com.tcdt.qlnvhang.response.xuathang.bbhaodoi.XhBienBanHaoDoiRes;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class XhBienBanHaoDoiServiceImpl implements XhBienBanHaoDoiService {

    private static final String SHEET_BIEN_BAN_TINH_KHO = "Biên bản hao dôi lỗi";
    private static final String STT = "STT";
    private static final String SO_QUYET_DINH = "Số Quyết Định Xuất";
    private static final String SO_BIEN_BAN = "Số Biên Bản";
    private static final String NGAY_BIEN_BAN = "Ngày Biên Bản";
    private static final String DIEM_KHO = "Điểm Kho";
    private static final String NHA_KHO = "Nhà Kho";
    private static final String NGAN_KHO = "Ngăn Kho";
    private static final String LO_KHO = "Lô Kho";
    private static final String TRANG_THAI = "Trạng Thái";
    private final String MA_DS = "/BBHD-CCDTVP";
    @Autowired
    XhBienBanHaoDoiRepository xhBienBanHaoDoiRepository;
    @Autowired
    XhBienBanHaoDoiCtRepository xhBienBanHaoDoiCtRepository;
    @Autowired
    XhQdGiaoNvXhRepository xhQdGiaoNvXhRepository;
    @Autowired
    XhBienBanTinhKhoRepository xhBienBanTinhKhoRepository;
    @Autowired
    KtDiemKhoRepository ktDiemKho;
    @Autowired
    KtNhaKhoRepository ktNhaKho;
    @Autowired
    KtNganKhoRepository ktNganKho;
    @Autowired
    KtNganLoRepository ktNganLo;
    @Autowired
    KtTongKhoRepository ktTongKhoRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public XhBienBanHaoDoiRes create(XhBienBanHaoDoiReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        XhBienBanHaoDoi item = new XhBienBanHaoDoi();
        item.setBbTinhkhoId(req.getBbTinhKhoId());
        item.setMaDvi(req.getMaDvi());
        item.setCapDvi(ktTongKhoRepository.findByMaTongKho(item.getMaDvi()).get().getTenTongKho());
        Long count = xhBienBanTinhKhoRepository.getMaxId();
        if (count == null) count = 1L;
        item.setSoBienBan(count.intValue() + 1 + "/" + LocalDate.now().getYear() + MA_DS);
        XhBienBanTinhKho data = xhBienBanTinhKhoRepository.findById(item.getBbTinhkhoId()).get();
        item.setSoLuongNhap(data.getSoLuongNhap());
        item.setSoLuongXuat(data.getSoLuongXuat());
//        item.setNgayNhap();
//        item.setNgayXuat();
        item.setKienNghi(req.getKienNghi());
        item.setNguyenNhan(req.getNguyenNhan());
        item.setNam(LocalDate.now().getYear());
        item.setNgayTao(new Date());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(TrangThaiEnum.DU_THAO.getId());

        item.setSlHaoThucte(item.getSoLuongNhap()-item.getSoLuongXuat());
        item.setTileThucte(item.getSlHaoThucte()/item.getSoLuongXuat());

        xhBienBanHaoDoiRepository.save(item);
        List<XhBienBanHaoDoiCt> ds = req.getDs().stream().map(q -> {
            XhBienBanHaoDoiCt xhBienBanHaoDoiCt = new XhBienBanHaoDoiCt();
            BeanUtils.copyProperties(q, xhBienBanHaoDoiCt, "id");
            xhBienBanHaoDoiCt.setBbHaoDoiId(item.getId());
            return xhBienBanHaoDoiCt;
        }).collect(Collectors.toList());
        xhBienBanHaoDoiCtRepository.saveAll(ds);

        XhBienBanHaoDoiRes result = this.buildResponse(item);
        result.setDs(ds.stream().map(q -> this.buildResponseCt(q)).collect(Collectors.toList()));
        return result;
    }

    @Override
    public XhBienBanHaoDoiRes update(XhBienBanHaoDoiReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        XhBienBanHaoDoi item = xhBienBanHaoDoiRepository.findById(req.getId()).get();
        item.setBbTinhkhoId(req.getBbTinhKhoId());
        item.setMaDvi(req.getMaDvi());
        item.setCapDvi(ktTongKhoRepository.findByMaTongKho(item.getMaDvi()).get().getTenTongKho());
        XhBienBanTinhKho data = xhBienBanTinhKhoRepository.findById(item.getBbTinhkhoId()).get();
        item.setSoLuongNhap(data.getSoLuongNhap());
        item.setSoLuongXuat(data.getSoLuongXuat());
//        item.setNgayNhap();
//        item.setNgayXuat();
        item.setKienNghi(req.getKienNghi());
        item.setNguyenNhan(req.getNguyenNhan());
        item.setNam(LocalDate.now().getYear());
        item.setNgaySua(new Date());
        item.setNguoiSuaId(userInfo.getId());

        //tinh lai so luong


        xhBienBanHaoDoiRepository.save(item);

        xhBienBanHaoDoiCtRepository.deleteAllByBbHaoDoiId(item.getId());
        List<XhBienBanHaoDoiCt> ds = req.getDs().stream().map(q -> {
            XhBienBanHaoDoiCt xhBienBanHaoDoiCt = new XhBienBanHaoDoiCt();
            BeanUtils.copyProperties(q, xhBienBanHaoDoiCt, "id");
            xhBienBanHaoDoiCt.setBbHaoDoiId(item.getId());
            return xhBienBanHaoDoiCt;
        }).collect(Collectors.toList());
        xhBienBanHaoDoiCtRepository.saveAll(ds);

        XhBienBanHaoDoiRes result = this.buildResponse(item);
        result.setDs(ds.stream().map(q -> this.buildResponseCt(q)).collect(Collectors.toList()));
        return result;
    }

    @Override
    public XhBienBanHaoDoiRes detail(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<XhBienBanHaoDoi> optional = xhBienBanHaoDoiRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản hao dôi không tồn tại.");

        XhBienBanHaoDoi item = optional.get();
//        List<XhBienBanHaoDoiCtRes> ds = xhBienBanHaoDoiCtRepository.findAllByBbHaoDoiId(item.getId()).stream().map(p -> this.buildResponseCt(p)).collect(Collectors.toList());
        XhBienBanHaoDoiRes result = this.buildResponse(item);
//        result.setDs(ds);
        return result;
    }

    @Override
    public boolean delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<XhBienBanHaoDoi> optional = xhBienBanHaoDoiRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Biên bản hao dôi không tồn tại.");

        XhBienBanHaoDoi item = optional.get();
        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa Biên bản hao dôi đã đã duyệt");
        }
        xhBienBanHaoDoiCtRepository.deleteAllByBbHaoDoiId(item.getId());
        xhBienBanHaoDoiRepository.delete(item);
        return true;
    }

    @Override
    public boolean deleteMultiple(DeleteReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        xhBienBanHaoDoiCtRepository.deleteByBbHaoDoiIdIn(req.getIds());
        xhBienBanHaoDoiRepository.deleteByIdIn(req.getIds());
        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateStatus(StatusReq stReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<XhBienBanHaoDoi> optional = xhBienBanHaoDoiRepository.findById(stReq.getId());
        if (!optional.isPresent())
            throw new Exception("Biên bản hao dôi không tồn tại.");

        XhBienBanHaoDoi item = optional.get();
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
    public Page<XhBienBanHaoDoiRes> search(XhBienBanHaoDoiSearchReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());

        List<XhBienBanHaoDoiRes> responses = new ArrayList<>();
        xhBienBanHaoDoiRepository.search(req).forEach(item -> {
            XhBienBanHaoDoiRes response = this.buildResponse(item);
            responses.add(response);
        });

        return new PageImpl<>(responses, pageable, xhBienBanHaoDoiRepository.count(req));
    }

    @Override
    public boolean exportToExcel(XhBienBanHaoDoiSearchReq objReq, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
        List<XhBienBanHaoDoiRes> list = this.search(objReq).get().collect(Collectors.toList());

        if (CollectionUtils.isEmpty(list))
            return true;

        String[] rowsName = new String[]{STT, SO_BIEN_BAN, SO_QUYET_DINH, NGAY_BIEN_BAN, DIEM_KHO
                , NHA_KHO, NGAN_KHO, LO_KHO, TRANG_THAI};
        String filename = "Danh_sach_bien_ban_hao_doi.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;

        try {
            for (int i = 0; i < list.size(); i++) {
                XhBienBanHaoDoiRes item = list.get(i);
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

    private XhBienBanHaoDoiRes buildResponse(XhBienBanHaoDoi xhBienBanHaoDoi) {
        XhBienBanHaoDoiRes xhBienBanHaoDoiRes = new XhBienBanHaoDoiRes();
        xhBienBanHaoDoiRes.setId(xhBienBanHaoDoi.getId());
        XhBienBanTinhKho data = xhBienBanTinhKhoRepository.findById(xhBienBanHaoDoi.getBbTinhkhoId()).get();
        xhBienBanHaoDoiRes.setSoBienBanTinhKho(data.getSoBienBan());
//        xhBienBanHaoDoiRes.setSoQd(xhQdGiaoNvXhRepository.getOne(data.getQdgnvnxId()).getSoQuyetDinh());
        xhBienBanHaoDoiRes.setDiemKho(ktDiemKho.findByMaDiemkho(data.getMaDiemkho()).getTenDiemkho());
        xhBienBanHaoDoiRes.setNhaKho(ktNhaKho.findByMaNhakho(data.getMaNhakho()).getTenNhakho());
        xhBienBanHaoDoiRes.setNganKho(ktNganKho.findByMaNgankho(data.getMaNgankho()).getTenNgankho());
        xhBienBanHaoDoiRes.setLoKho(ktNganLo.findFirstByMaNganlo(data.getMaLokho()).getMaNganlo());
        xhBienBanHaoDoiRes.setSoLuongNhap(xhBienBanHaoDoi.getSoLuongNhap());
        xhBienBanHaoDoiRes.setSoLuongXuat(xhBienBanHaoDoi.getSoLuongXuat());
        xhBienBanHaoDoiRes.setNgayNhap(xhBienBanHaoDoi.getNgayNhap());
        xhBienBanHaoDoiRes.setNgayXuat(xhBienBanHaoDoi.getNgayXuat());
        xhBienBanHaoDoiRes.setTenDvi(xhBienBanHaoDoi.getCapDvi());
        xhBienBanHaoDoiRes.setMaDvi(xhBienBanHaoDoi.getMaDvi());
        xhBienBanHaoDoiRes.setSlHaoThucte(xhBienBanHaoDoi.getSlHaoThucte());
        xhBienBanHaoDoiRes.setTileThucte(xhBienBanHaoDoi.getTileThucte());
        xhBienBanHaoDoiRes.setSlHaoThanhly(xhBienBanHaoDoi.getSlHaoThanhly());
        xhBienBanHaoDoiRes.setTileThanhly(xhBienBanHaoDoi.getTileThanhly());
        xhBienBanHaoDoiRes.setSoBienBan(xhBienBanHaoDoi.getSoBienBan());
        xhBienBanHaoDoiRes.setSlVuotDm(xhBienBanHaoDoi.getSlVuotDm());
        xhBienBanHaoDoiRes.setSlDuoiDm(xhBienBanHaoDoi.getSlDuoiDm());
        xhBienBanHaoDoiRes.setTileVuotDm(xhBienBanHaoDoi.getTileVuotDm());
        xhBienBanHaoDoiRes.setTileDuoiDm(xhBienBanHaoDoi.getTileDuoiDm());
        xhBienBanHaoDoiRes.setNguyenNhan(xhBienBanHaoDoi.getNguyenNhan());
        xhBienBanHaoDoiRes.setKienNghi(xhBienBanHaoDoi.getKienNghi());
        xhBienBanHaoDoiRes.setNgayLapPhieu(xhBienBanHaoDoi.getNgayTao());
        xhBienBanHaoDoiRes.setTrangThai(xhBienBanHaoDoi.getTrangThai());
        xhBienBanHaoDoiRes.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(xhBienBanHaoDoiRes.getTrangThai()));
        xhBienBanHaoDoiRes.setTrangThaiDuyet(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(xhBienBanHaoDoiRes.getTrangThai()));
        return xhBienBanHaoDoiRes;
    }

    private XhBienBanHaoDoiCtRes buildResponseCt(XhBienBanHaoDoiCt xhBienBanHaoDoict) {
        XhBienBanHaoDoiCtRes xhBienBanHaoDoiCtRes = new XhBienBanHaoDoiCtRes();
        BeanUtils.copyProperties(xhBienBanHaoDoict, xhBienBanHaoDoiCtRes);
        return xhBienBanHaoDoiCtRes;
    }

}
