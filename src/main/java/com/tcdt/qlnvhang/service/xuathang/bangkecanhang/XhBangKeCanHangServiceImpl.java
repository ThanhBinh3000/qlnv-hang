package com.tcdt.qlnvhang.service.xuathang.bangkecanhang;

import com.tcdt.qlnvhang.entities.xuathang.bangkecanhang.XhBangKeCanHang;
import com.tcdt.qlnvhang.entities.xuathang.bangkecanhang.XhBangKeCanHangCt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.khotang.KtDiemKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNhaKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.bangkecanhang.XhBangKeCanHangCtRepository;
import com.tcdt.qlnvhang.repository.xuathang.bangkecanhang.XhBangKeCanHangRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhBangKeCanHangSearchReq;
import com.tcdt.qlnvhang.request.xuathang.bangkecanhang.XhBangKeCanHangCtReq;
import com.tcdt.qlnvhang.request.xuathang.bangkecanhang.XhBangKeCanHangReq;
import com.tcdt.qlnvhang.response.xuathang.bangkecanhang.XhBangKeCanHangCtRes;
import com.tcdt.qlnvhang.response.xuathang.bangkecanhang.XhBangKeCanHangRes;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.LocalDateTimeUtils;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

@Log4j2
@Service
public class XhBangKeCanHangServiceImpl implements XhBangKeCanHangService {

    private static final String SHEET_BANG_KE_CAN_HANG = "Bảng kê cân hàng";

    private final String MA_DS = "/BKCH-CCDTVP";
    @Autowired
    XhBangKeCanHangRepository bangKeCanHangRepository;

    @Autowired
    XhBangKeCanHangCtRepository xhBangKeCanHangCtRepository;

    @Autowired
    KtNganLoRepository ktNganLoRepository;
    @Autowired
    KtDiemKhoRepository ktDiemKhoRepository;
    @Autowired
    KtNganKhoRepository ktNganKhoRepository;
    @Autowired
    KtNhaKhoRepository ktNhaKhoRepository;

    @Autowired
    QlnvDmDonviRepository qlnvDmDonviRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public XhBangKeCanHangRes create(XhBangKeCanHangReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        XhBangKeCanHang item = new XhBangKeCanHang();
        Long count = bangKeCanHangRepository.getMaxId();
        if (count == null) count = 1L;
        item.setSoBangKe(count.intValue() + 1 + "/" + LocalDate.now().getYear() + MA_DS);
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(LocalDate.now());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        bangKeCanHangRepository.save(item);

        List<XhBangKeCanHangCt> ds = req.getDs().stream()
                .map(d -> {
                    d.setBkCanHangID(item.getId());
                    return d;
                })
                .map(d -> {
                    XhBangKeCanHangCt xuatKhoCt = new XhBangKeCanHangCt();
                    BeanUtils.copyProperties(d, xuatKhoCt, "id");
                    return xuatKhoCt;
                })
                .collect(Collectors.toList());

        xhBangKeCanHangCtRepository.saveAll(ds);
        XhBangKeCanHangRes result = new XhBangKeCanHangRes();
        BeanUtils.copyProperties(item, result, "id");

        List<XhBangKeCanHangCtRes> dsRes = ds
                .stream()
                .map(user -> new ModelMapper().map(user, XhBangKeCanHangCtRes.class))
                .collect(Collectors.toList());

        result.setDs(dsRes);

        return result;

    }

    @Override
    public XhBangKeCanHangRes update(XhBangKeCanHangReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        Optional<XhBangKeCanHang> optional = bangKeCanHangRepository.findById(req.getId());
        if (!optional.isPresent())
            throw new Exception("Bảng kê cân hàng không tồn tại.");

        this.validateSoQuyetDinh(optional.get(), req);

        XhBangKeCanHang item = optional.get();
        BeanUtils.copyProperties(req, item, "id", "so", "nam", "trangThai");
        item.setNgaySua(LocalDate.now());
        item.setNguoiSuaId(userInfo.getId());
        bangKeCanHangRepository.save(item);

        Map<Long, XhBangKeCanHangCt> mapChiTiet = xhBangKeCanHangCtRepository.findByBkCanHangIDIn(Collections.singleton(item.getId()))
                .stream().collect(Collectors.toMap(XhBangKeCanHangCt::getId, Function.identity()));

        List<XhBangKeCanHangCt> chiTiets = this.saveListChiTiet(item.getId(), req.getDs(), mapChiTiet);
        item.setDs(chiTiets);
        if (!CollectionUtils.isEmpty(mapChiTiet.values()))
            xhBangKeCanHangCtRepository.deleteAll(mapChiTiet.values());

        XhBangKeCanHangRes res = new XhBangKeCanHangRes();
        BeanUtils.copyProperties(item, res);

        return res;
    }

    private List<XhBangKeCanHangCt> saveListChiTiet(Long parentId,
                                                    List<XhBangKeCanHangCtReq> chiTietReqs,
                                                    Map<Long, XhBangKeCanHangCt> mapChiTiet) throws Exception {
        List<XhBangKeCanHangCt> chiTiets = new ArrayList<>();
        for (XhBangKeCanHangCtReq req : chiTietReqs) {
            Long id = req.getId();
            XhBangKeCanHangCt chiTiet = new XhBangKeCanHangCt();

            if (id != null && id > 0) {
                chiTiet = mapChiTiet.get(id);
                if (chiTiet == null)
                    throw new Exception("Bảng kê cân hàng chi tiết không tồn tại.");
                mapChiTiet.remove(id);
            }

            BeanUtils.copyProperties(req, chiTiet, "id");
            chiTiet.setBkCanHangID(parentId);
            chiTiets.add(chiTiet);
        }

        if (!CollectionUtils.isEmpty(chiTiets))
            xhBangKeCanHangCtRepository.saveAll(chiTiets);

        return chiTiets;
    }

    private void validateSoQuyetDinh(XhBangKeCanHang update, XhBangKeCanHangReq req) throws Exception {
        String so = req.getSoBangKe();
        if (!StringUtils.hasText(so))
            return;
        if (update == null || (StringUtils.hasText(update.getSoBangKe()) && !update.getSoBangKe().equalsIgnoreCase(so))) {
            Optional<XhBangKeCanHang> optional = bangKeCanHangRepository.findFirstBySoBangKe(so);
            Long updateId = Optional.ofNullable(update).map(XhBangKeCanHang::getId).orElse(null);
            if (optional.isPresent() && !optional.get().getId().equals(updateId))
                throw new Exception("Số Hợp đồng " + so + " đã tồn tại");
        }
    }


    @Override
    public XhBangKeCanHangRes detail(Long id) throws Exception {
        XhBangKeCanHang phieuXuatKho = bangKeCanHangRepository.findById(id).get();
        if (phieuXuatKho == null)
            throw new Exception("Không tìm thấy dữ liệu.");

        XhBangKeCanHangRes item = new XhBangKeCanHangRes();
        BeanUtils.copyProperties(phieuXuatKho, item, "id");
        item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
        // chưa tìm ngăn kho lô kho các giá trị tên
        item.setTenNhakho(ktNhaKhoRepository.findByMaNhakho(item.getMaNhakho()).getTenNhakho());
        item.setTenDiemkho(ktDiemKhoRepository.findByMaDiemkho(item.getMaDiemkho()).getTenDiemkho());
        item.setTenNgankho(ktNganKhoRepository.findByMaNgankho(item.getMaNgankho()).getTenNgankho());
        item.setTenNganlo(ktNganLoRepository.findFirstByMaNganlo(item.getMaNganlo()).getTenNganlo());
        item.setTenDvi(qlnvDmDonviRepository.findByMaDvi(item.getMaDvi()).getTenDvi());
        item.setDs(findXuatKhoCtResByPxuatKho(id));

        return item;
    }

    public List<XhBangKeCanHangCtRes> findXuatKhoCtResByPxuatKho(Long id) {
        List<XhBangKeCanHangCt> lstDs = xhBangKeCanHangCtRepository.findByBkCanHangIDIn(Collections.singleton(id));

        return lstDs.stream()
                .map(user -> new ModelMapper().map(user, XhBangKeCanHangCtRes.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(Long id) throws Exception {
        Optional<XhBangKeCanHang> optional = bangKeCanHangRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Bảng kê cân hàng không tồn tại.");

        XhBangKeCanHang item = optional.get();
        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa Bảng kê cân hàng đã đã duyệt");
        }
        xhBangKeCanHangCtRepository.deleteAllByBkCanHangID(item.getId());
        bangKeCanHangRepository.delete(item);
        return true;
    }

    @Override
    public boolean deleteMultiple(DeleteReq req) throws Exception {
        xhBangKeCanHangCtRepository.deleteByBkCanHangIDIn(req.getIds());
        bangKeCanHangRepository.deleteByIdIn(req.getIds());
        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateStatus(StatusReq stReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<XhBangKeCanHang> optional = bangKeCanHangRepository.findById(stReq.getId());
        if (!optional.isPresent())
            throw new Exception("Bảng kê cân hàng không tồn tại.");

        XhBangKeCanHang item = optional.get();
        String trangThai = item.getTrangThai();
        if (NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.DUTHAO.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId());
            item.setNguoiGuiDuyetId(userInfo.getId());
            item.setNgayGuiDuyet(LocalDate.now());

        } else if (NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId());
            item.setNguoiPduyetId(userInfo.getId());
            item.setNgayPduyet(LocalDate.now());

        } else if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId());
            item.setNguoiPduyetId(userInfo.getId());
            item.setNgayPduyet(LocalDate.now());

        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_KTVBQ.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_KTVBQ.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_KTVBQ.getId());
            item.setNguoiPduyetId(userInfo.getId());
            item.setNgayPduyet(LocalDate.now());
            item.setLyDoTuChoi(stReq.getLyDo());

        } else if (NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId().equals(stReq.getTrangThai())) {
            if (!NhapXuatHangTrangThaiEnum.CHODUYET_LDCC.getId().equals(trangThai))
                return false;

            item.setTrangThai(NhapXuatHangTrangThaiEnum.TUCHOI_LDCC.getId());
            item.setNguoiPduyetId(userInfo.getId());
            item.setNgayPduyet(LocalDate.now());
            item.setLyDoTuChoi(stReq.getLyDo());

        } else {
            throw new Exception("Bad request.");
        }

        return true;
    }

    @Override
    public Page<XhBangKeCanHangRes> search(XhBangKeCanHangSearchReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        List<XhBangKeCanHangRes> list = bangKeCanHangRepository.search(req);
        list.forEach(item -> {
            item.setDs(findXuatKhoCtResByPxuatKho(item.getId()));
            item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
        });
        Page<XhBangKeCanHangRes> page = new PageImpl<>(list, pageable, list.size());

        return page;
    }

    @Override
    public boolean exportToExcel(XhBangKeCanHangSearchReq objReq, HttpServletResponse response) throws Exception {

        objReq.setPaggingReq(new PaggingReq(Integer.MAX_VALUE, 0));
        List<XhBangKeCanHangRes> list = this.search(objReq).get().collect(Collectors.toList());

        if (CollectionUtils.isEmpty(list))
            return true;

        String[] rowsName = new String[]{"STT", "Số bảng kê", "Số quyết định xuất", "Số phiếu xuất",
                "Ngày nhập kho", "Điểm kho", "Nhà kho", "Ngăn kho", "Lô kho", "Trạng thái"};
        String filename = "Danh_bang_ke_can_hang.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;

        try {
            for (int i = 0; i < list.size(); i++) {
                XhBangKeCanHangRes item = list.get(i);
                objs = new Object[rowsName.length];
                objs[0] = i;
                objs[1] = item.getSoBangKe();
                objs[2] = item.getSoSqdx();
                objs[3] = item.getSoPhieuXuatKho();
                objs[4] = LocalDateTimeUtils.localDateToString(item.getNgayNhap());
                objs[5] = item.getTenDiemkho();
                objs[6] = item.getTenNhakho();
                objs[7] = item.getTenNgankho();
                objs[8] = item.getTenNganlo();
                objs[9] = NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai());
                dataList.add(objs);
            }

            ExportExcel ex = new ExportExcel(SHEET_BANG_KE_CAN_HANG, filename, rowsName, dataList, response);
            ex.export();
        } catch (Exception e) {
            log.error("Error export", e);
            return false;
        }

        return true;
    }
}
