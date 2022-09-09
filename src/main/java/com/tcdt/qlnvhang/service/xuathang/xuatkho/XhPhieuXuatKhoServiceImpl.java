package com.tcdt.qlnvhang.service.xuathang.xuatkho;

import com.tcdt.qlnvhang.entities.xuathang.XhQdGiaoNvuXuat;
import com.tcdt.qlnvhang.entities.xuathang.XhQdGiaoNvuXuatCt;
import com.tcdt.qlnvhang.entities.xuathang.phieuxuatkho.XhPhieuXuatKho;
import com.tcdt.qlnvhang.entities.xuathang.phieuxuatkho.XhPhieuXuatKhoCt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.khotang.KtDiemKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNhaKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.bbtinhkho.XhBienBanTinhKhoCtRepository;
import com.tcdt.qlnvhang.repository.xuathang.phieuxuatkho.XhPhieuXuatKhoCtRepository;
import com.tcdt.qlnvhang.repository.xuathang.phieuxuatkho.XhPhieuXuatKhoRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhPhieuXuatKhoSearchReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatCtReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkho.XhPhieuXuatKhoCtReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkho.XhPhieuXuatKhoReq;
import com.tcdt.qlnvhang.response.xuathang.phieuxuatkho.XhPhieuXuatKhoCtRes;
import com.tcdt.qlnvhang.response.xuathang.phieuxuatkho.XhPhieuXuatKhoRes;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.UserUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class XhPhieuXuatKhoServiceImpl implements XhPhieuXuatKhoService {

    @Autowired
    XhPhieuXuatKhoRepository xuatKhoRepo;

    @Autowired
    XhPhieuXuatKhoCtRepository xuatKhoCtRepo;

    @Autowired
    FileDinhKemService fileDinhKemService;
    @Autowired
    KtNganLoRepository ktNganLoRepository;
    @Autowired
    KtDiemKhoRepository ktDiemKhoRepository;
    @Autowired
    KtNganKhoRepository ktNganKhoRepository;
    @Autowired
    KtNhaKhoRepository ktNhaKhoRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public XhPhieuXuatKhoRes create(XhPhieuXuatKhoReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        XhPhieuXuatKho item = new XhPhieuXuatKho();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(LocalDate.now());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        xuatKhoRepo.save(item);

        List<XhPhieuXuatKhoCt> ds = req.getDs().stream()
                .map(d -> {
                    d.setPxuatKhoId(item.getId());
                    return d;
                })
                .map(d -> {
                    XhPhieuXuatKhoCt xuatKhoCt = new XhPhieuXuatKhoCt();
                    BeanUtils.copyProperties(d, xuatKhoCt, "id");
                    return xuatKhoCt;
                })
                .collect(Collectors.toList());

        xuatKhoCtRepo.saveAll(ds);
        XhPhieuXuatKhoRes result = new XhPhieuXuatKhoRes();
        BeanUtils.copyProperties(item, result, "id");

        List<XhPhieuXuatKhoCtRes> dsRes = ds
                .stream()
                .map(user -> new ModelMapper().map(user, XhPhieuXuatKhoCtRes.class))
                .collect(Collectors.toList());

        result.setDs(dsRes);

        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), item.getId(), XhPhieuXuatKho.TABLE_NAME);
        item.setFileDinhKems(fileDinhKems);
        return result;

    }


    @Override
    public XhPhieuXuatKhoRes update(XhPhieuXuatKhoReq req) throws Exception {
//        UserInfo userInfo = UserUtils.getUserInfo();
//
//        Optional<XhPhieuXuatKho> optional = xuatKhoRepo.findById(req.getId());
//        if (!optional.isPresent())
//            throw new Exception("Phiếu xuất kho không tồn tại.");
//
//        this.validateSoQuyetDinh(optional.get(), req);
//
//        XhPhieuXuatKho item = optional.get();
//        BeanUtils.copyProperties(req, item, "id", "so", "nam");
//        item.setNgaySua(LocalDate.now());
//        item.setNguoiSuaId(userInfo.getId());
//        xuatKhoRepo.save(item);
//
//        Map<Long, XhPhieuXuatKhoCt> mapChiTiet = xuatKhoCtRepo.findByPxuatKhoIdIn(Collections.singleton(item.getId()))
//                .stream().collect(Collectors.toMap(XhPhieuXuatKhoCt::getId, Function.identity()));
//
//        List<XhPhieuXuatKhoCt> chiTiets = this.saveListChiTiet(item.getId(), req.getCts(), mapChiTiet);
//        item.setCts(chiTiets);
//        if (!CollectionUtils.isEmpty(mapChiTiet.values()))
//            xhQdGiaoNvuXuatCtRepository.deleteAll(mapChiTiet.values());
//
//        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), item.getId(), XhQdGiaoNvuXuat.TABLE_NAME);
//        item.setFileDinhKems(fileDinhKems);
//        return this.buildResponse(item);
       return null;
    }

    private void validateSoQuyetDinh(XhPhieuXuatKho update, XhPhieuXuatKhoReq req) throws Exception {
        String so = req.getSoHd();
        if (!StringUtils.hasText(so))
            return;
        if (update == null || (StringUtils.hasText(update.getSoHd()) && !update.getSoHd().equalsIgnoreCase(so))) {
            Optional<XhPhieuXuatKho> optional = xuatKhoRepo.findFirstBySoHd(so);
            Long updateId = Optional.ofNullable(update).map(XhPhieuXuatKho::getId).orElse(null);
            if (optional.isPresent() && !optional.get().getId().equals(updateId))
                throw new Exception("Số Hợp đồng " + so + " đã tồn tại");
        }
    }


    @Override
    public XhPhieuXuatKhoRes detail(Long id) throws Exception {
        XhPhieuXuatKho dsHangTieuHuy = xuatKhoRepo.findById(id).get();
        if (dsHangTieuHuy == null)
            throw new Exception("Không tìm thấy dữ liệu.");

        XhPhieuXuatKhoRes item = new XhPhieuXuatKhoRes();
        BeanUtils.copyProperties(dsHangTieuHuy, item, "id");
        item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
        // chưa tìm ngăn kho lô kho các giá trị tên
        item.setTenNhakho(ktNhaKhoRepository.findByMaNhakho(item.getMaNhakho()).getTenNhakho());
        item.setTenDiemkho(ktDiemKhoRepository.findByMaDiemkho(item.getMaDiemkho()).getTenDiemkho());
        item.setTenNgankho(ktNganKhoRepository.findByMaNgankho(item.getMaNgankho()).getTenNgankho());
        item.setTenNganlo(ktNganLoRepository.findFirstByMaNganlo(item.getMaNganlo()).getTenNganlo());
        item.setDs(findXuatKhoCtResByPxuatKho(id));

        item.setFileDinhKems(fileDinhKemService.search(id, Collections.singleton(XhPhieuXuatKho.TABLE_NAME)));

        return item;
    }

    public List<XhPhieuXuatKhoCtRes> findXuatKhoCtResByPxuatKho(Long id) {
        List<XhPhieuXuatKhoCt> lstDs = xuatKhoCtRepo.findByPxuatKhoIdIn(Collections.singleton(id));

        return lstDs.stream()
                .map(user -> new ModelMapper().map(user, XhPhieuXuatKhoCtRes.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(Long id) throws Exception {
        Optional<XhPhieuXuatKho> optional = xuatKhoRepo.findById(id);
        if (!optional.isPresent())
            throw new Exception("Phiếu xuất kho không tồn tại.");

        XhPhieuXuatKho item = optional.get();
        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(item.getTrangThai())) {
            throw new Exception("Không thể xóa phiếu xuất kho đã đã duyệt");
        }
        xuatKhoCtRepo.deleteAllByPxuatKhoId(item.getId());
        xuatKhoRepo.delete(item);
        return true;
    }

    @Override
    public boolean deleteMultiple(DeleteReq req) throws Exception {
        xuatKhoCtRepo.deleteByPxuatKhoIdIn(req.getIds());
        xuatKhoRepo.deleteByIdIn(req.getIds());
        return true;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateStatus(StatusReq stReq) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<XhPhieuXuatKho> optional = xuatKhoRepo.findById(stReq.getId());
        if (!optional.isPresent())
            throw new Exception("Phiếu xuất kho không tồn tại.");

        XhPhieuXuatKho item = optional.get();
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
    public Page<XhPhieuXuatKhoRes> search(XhPhieuXuatKhoSearchReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        List<XhPhieuXuatKhoRes> list = xuatKhoRepo.search(req);
        list.forEach(item -> {
            item.setDs(findXuatKhoCtResByPxuatKho(item.getId()));
            item.setFileDinhKems(fileDinhKemService.search(item.getId(), Collections.singleton(XhPhieuXuatKho.TABLE_NAME)));
            item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(item.getTrangThai()));
        });
        Page<XhPhieuXuatKhoRes> page = new PageImpl<>(list, pageable, list.size());

        return page;
    }

    @Override
    public boolean exportToExcel(XhPhieuXuatKhoSearchReq objReq, HttpServletResponse response) throws Exception {
        return false;

//        objReq.setLimit((int) hangTieuHuyRepository.count());
//        objReq.setPage(0);
//        List<DsHangThanhLyRes> list = hangTieuHuyRepository.search(objReq).stream().map(d -> {
//            DsHangThanhLyRes ds = new DsHangThanhLyRes();
//            ds.setId((Long) d[4]);
//            ds.setMaDanhSach(String.valueOf(d[0]));
//            ds.setTenDonvi(hangTieuHuyRepository.getTk((String) d[1]));
//            ds.setNgayTao(LocalDate.parse(d[2].toString()));
//            ds.setTrangThaiXuLy(String.valueOf(d[3]));
//            return ds;
//        }).collect(Collectors.toList());
//        System.out.println(list);
//
//        if (CollectionUtils.isEmpty(list))
//            return true;
//
//        String[] rowsName = new String[]{"STT", "Mã danh sách", "Tên đơn vị", "Ngày tạo",
//                "Trạng thái xử lý"};
//        String filename = "Danh_sach_hang_tieu_huy.xlsx";
//
//        List<Object[]> dataList = new ArrayList<Object[]>();
//        Object[] objs = null;
//
//        try {
//            for (int i = 0; i < list.size(); i++) {
//                DsHangThanhLyRes item = list.get(i);
//                objs = new Object[rowsName.length];
//                objs[0] = i;
//                objs[1] = item.getMaDanhSach();
//                objs[2] = item.getTenDonvi();
//                objs[3] = item.getNgayTao().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
//                objs[4] = item.getTrangThaiXuLy();
//                dataList.add(objs);
//            }
//
//            ExportExcel ex = new ExportExcel("Danh sách hàng tiêu hủy", filename, rowsName, dataList, response);
//            ex.export();
//        } catch (Exception e) {
//            log.error("Error export", e);
//            return false;
//        }
//        return true;
    }
}
