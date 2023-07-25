package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattu;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtBbKtNhapKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtPhieuKdclHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatNhapKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtBbKtNhapKhoRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatNhapKhoRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtBbKtNhapKho;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtPhieuKdclHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatNhapKho;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhXkVtBbKtNhapKhoService extends BaseServiceImpl {


    @Autowired
    private XhXkVtBbKtNhapKhoRepository xhXkVtBbKtNhapKhoRepository;

    @Autowired
    private XhXkVtQdGiaonvXhRepository xhXkVtQdGiaonvXhRepository;

    @Autowired
    private XhXkVtPhieuXuatNhapKhoRepository xhXkVtPhieuXuatNhapKhoRepository;

    @Autowired
    private XhXkVtPhieuKdclHdrRepository xhXkVtPhieuKdclHdrRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhXkVtBbKtNhapKho> searchPage(CustomUserDetails currentUser, XhXkVtBbKtNhapKhoRequest req) throws Exception {
        req.setDvql(ObjectUtils.isEmpty(req.getDvql()) ? currentUser.getDvql() : req.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhXkVtBbKtNhapKho> search = xhXkVtBbKtNhapKhoRepository.search(req, pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        search.getContent().forEach(s -> {
            s.setMapDmucDvi(mapDmucDvi);
            s.setMapVthh(mapVthh);
            s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
        });
        return search;
    }

    @Transactional
    public XhXkVtBbKtNhapKho save(CustomUserDetails currentUser, XhXkVtBbKtNhapKhoRequest objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhXkVtBbKtNhapKho> optional = xhXkVtBbKtNhapKhoRepository.findBySoBienBan(objReq.getSoBienBan());
        if (optional.isPresent()) {
            throw new Exception("Số biên bản đã tồn tại");
        }
        XhXkVtBbKtNhapKho data = new XhXkVtBbKtNhapKho();
        BeanUtils.copyProperties(objReq, data);
        data.setTrangThai(Contains.DUTHAO);
        XhXkVtBbKtNhapKho created = xhXkVtBbKtNhapKhoRepository.save(data);
        // cập nhật trạng thái đang thực hiện cho QD giao nv nhập hàng
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtBbKtNhapKho.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
        //save lại số bb vào phiếu xuất kho
        if (!objReq.getListPhieuNhapKho().isEmpty()) {
            objReq.getListPhieuNhapKho().forEach(it -> {
                it.setSoBbKetThucNhapKho(data.getSoBienBan());
                it.setIdBbKetThucNhapKho(data.getId());
            });
            xhXkVtPhieuXuatNhapKhoRepository.saveAll(objReq.getListPhieuNhapKho());
        }
        return created;
    }

    @Transactional
    public XhXkVtBbKtNhapKho update(CustomUserDetails currentUser, XhXkVtBbKtNhapKhoRequest objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhXkVtBbKtNhapKho> optional = xhXkVtBbKtNhapKhoRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Optional<XhXkVtBbKtNhapKho> soDx = xhXkVtBbKtNhapKhoRepository.findBySoBienBan(objReq.getSoBienBan());
        if (soDx.isPresent()) {
            if (!soDx.get().getId().equals(objReq.getId())) {
                throw new Exception("Số biên bản đã tồn tại");
            }
        }
        XhXkVtBbKtNhapKho data = optional.get();
        Long idBcKqKdMauOld = data.getIdCanCu() != objReq.getIdCanCu() ? data.getIdCanCu() : null;
        BeanUtils.copyProperties(objReq, data);
        XhXkVtBbKtNhapKho updated = xhXkVtBbKtNhapKhoRepository.save(data);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhXkVtBbKtNhapKho.TABLE_NAME));
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), updated.getId(), XhXkVtBbKtNhapKho.TABLE_NAME);
        updated.setFileDinhKems(fileDinhKems);
        //Update lại phiếu nhập kho khi sửa số BC kết quả kiểm định mẫu
        if (!ObjectUtils.isEmpty(idBcKqKdMauOld)) {
            List<XhXkVtPhieuXuatNhapKho> listOld = xhXkVtPhieuXuatNhapKhoRepository.findAllByIdBcKqkdMau(idBcKqKdMauOld).stream().map(item -> {
                item.setIdBbKetThucNhapKho(null);
                item.setSoBbKetThucNhapKho(null);
                return item;
            }).collect(Collectors.toList());
            List<XhXkVtPhieuXuatNhapKho> listNew = xhXkVtPhieuXuatNhapKhoRepository.findAllByIdBcKqkdMau(updated.getIdCanCu()).stream().map(item -> {
                item.setIdBbKetThucNhapKho(updated.getId());
                item.setSoBbKetThucNhapKho(updated.getSoBienBan());
                return item;
            }).collect(Collectors.toList());
            listNew.addAll(listOld);
            xhXkVtPhieuXuatNhapKhoRepository.saveAll(listNew);
        }
        return updated;
    }

    public XhXkVtBbKtNhapKho detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
        Optional<XhXkVtBbKtNhapKho> optional = xhXkVtBbKtNhapKhoRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhXkVtBbKtNhapKho model = optional.get();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkVtBbKtNhapKho.TABLE_NAME));
        model.setFileDinhKems(fileDinhKem);
        model.setMapDmucDvi(mapDmucDvi);
        model.setMapVthh(mapVthh);
        model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
        model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
        return model;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhXkVtBbKtNhapKho> optional = xhXkVtBbKtNhapKhoRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        XhXkVtBbKtNhapKho data = optional.get();
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhXkVtPhieuXuatNhapKho.TABLE_NAME));
        List<XhXkVtPhieuXuatNhapKho> allByIdBbKetThucNhapKho = xhXkVtPhieuXuatNhapKhoRepository.findAllByIdBbKetThucNhapKho(data.getId());
        if (allByIdBbKetThucNhapKho.isEmpty()) {
            allByIdBbKetThucNhapKho.forEach(it -> {
                it.setSoBbKetThucNhapKho(null);
                it.setIdBbKetThucNhapKho(null);
            });
            xhXkVtPhieuXuatNhapKhoRepository.saveAll(allByIdBbKetThucNhapKho);
        }
        xhXkVtBbKtNhapKhoRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhXkVtBbKtNhapKho> list = xhXkVtBbKtNhapKhoRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhXkVtBbKtNhapKho.TABLE_NAME));
        xhXkVtBbKtNhapKhoRepository.deleteAll(list);
    }

    @Transactional
    public XhXkVtBbKtNhapKho approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhXkVtBbKtNhapKho> optional = xhXkVtBbKtNhapKhoRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status = statusReq.getTrangThai() + optional.get().getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDCC + Contains.DUTHAO:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                optional.get().setNguoiDuyetId(currentUser.getUser().getId());
                optional.get().setNgayDuyet(LocalDate.now());
                break;
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                optional.get().setNguoiDuyetId(currentUser.getUser().getId());
                optional.get().setNgayDuyet(LocalDate.now());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                optional.get().setNguoiDuyetId(currentUser.getUser().getId());
                optional.get().setNgayDuyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        XhXkVtBbKtNhapKho model = xhXkVtBbKtNhapKhoRepository.save(optional.get());
        return model;
    }

//    public void export(CustomUserDetails currentUser, XhXkVtBbKtNhapKhoRequest objReq, HttpServletResponse response) throws Exception {
//        PaggingReq paggingReq = new PaggingReq();
//        paggingReq.setPage(0);
//        paggingReq.setLimit(Integer.MAX_VALUE);
//        objReq.setPaggingReq(paggingReq);
//        Page<XhXkVtBbKtNhapKho> page = this.searchPage(currentUser, objReq);
//        List<XhXkVtBbKtNhapKho> data = page.getContent();
//
//        String title = "Danh sách biên bản kết thúc nhập kho";
//        String[] rowsName = new String[]{"STT", "Số BC KQKĐ mẫu", "Năm báo cáo", "Điểm kho",
//                "Lô kho", "Chủng loại hàng hóa", "Số bb kết thúc nhập kho", "Ngày kết thúc nhập kho","Số phiếu NK","Ngày NK", "Trạng thái"};
//        String fileName = "danh-sach-bien-ban-ket-thuc-nhap-kho.xlsx";
//        List<Object[]> dataList = new ArrayList<Object[]>();
//        Object[] objs = null;
//        for (int i = 0; i < data.size(); i++) {
//            XhXkVtBbKtNhapKho dx = data.get(i);
//            objs = new Object[rowsName.length];
//            objs[0] = i;
//            objs[1] = dx.getSoCanCu();
//            objs[2] = dx.getNamKeHoach();
////            objs[3] = dx.getThoiGianGiaoHang();
//            objs[4] = dx.getTenDiemKho();
//            objs[5] = dx.getTenLoKho();
////            objs[6] = dx.getSoPhieu();
////            objs[7] = dx.getNgayXuatNhap();
////            objs[8] = dx.getSoPhieuKncl();
//            objs[9] = dx.getTenTrangThai();
//            dataList.add(objs);
//        }
//        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
//        ex.export();
//    }
}
