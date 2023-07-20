package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattu;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuXuatKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtPhieuKdclHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtPhieuXuatKho;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuXuatKhoReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatKhoRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuXuatKho;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class XhXkVtPhieuXuatKhoService extends BaseServiceImpl {


    @Autowired
    private XhXkVtPhieuXuatKhoRepository xhXkVtPhieuXuatKhoRepository;

    @Autowired
    private XhXkVtQdGiaonvXhRepository xhXkVtQdGiaonvXhRepository;

    @Autowired
    private XhXkVtPhieuKdclHdrRepository xhXkVtPhieuKdclHdrRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhXkVtPhieuXuatKho> searchPage(CustomUserDetails currentUser, XhXkVtPhieuXuatKhoRequest req) throws Exception {
        req.setDvql(ObjectUtils.isEmpty(req.getDvql()) ? currentUser.getDvql() : req.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhXkVtPhieuXuatKho> search = xhXkVtPhieuXuatKhoRepository.search(req, pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        List<Long> idsPhieuKncl = search.getContent().stream().map(XhXkVtPhieuXuatKho::getIdPhieuKncl).collect(Collectors.toList());
        Map<Long, Boolean> mapKetQuanKiemDinh = xhXkVtPhieuKdclHdrRepository.findByIdIn(idsPhieuKncl).stream().collect(Collectors.toMap(XhXkVtPhieuKdclHdr::getId, XhXkVtPhieuKdclHdr::getIsDat));
        search.getContent().forEach(s -> {
            s.setMapDmucDvi(mapDmucDvi);
            s.setMapVthh(mapVthh);
            s.setKetQuaKiemDinh(mapKetQuanKiemDinh.get(s.getIdPhieuKncl()));
            s.setTenLoai(Contains.getLoaiHinhXuat(s.getLoai()));
            s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
        });
        return search;
    }

    @Transactional
    public XhXkVtPhieuXuatKho save(CustomUserDetails currentUser, XhXkVtPhieuXuatKhoRequest objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhXkVtPhieuXuatKho> optional = xhXkVtPhieuXuatKhoRepository.findBySoPhieu(objReq.getSoPhieu());
        if (optional.isPresent()) {
            throw new Exception("số số phiếu đã tồn tại");
        }
        XhXkVtPhieuXuatKho data = new XhXkVtPhieuXuatKho();
        BeanUtils.copyProperties(objReq, data);
        data.setTrangThai(Contains.DUTHAO);
        XhXkVtPhieuXuatKho created = xhXkVtPhieuXuatKhoRepository.save(data);
        // cập nhật trạng thái đang thực hiện cho QD giao nv nhập hàng
        Optional<XhXkVtQdGiaonvXhHdr> qdGiaoNvXh = xhXkVtQdGiaonvXhRepository.findById(created.getIdCanCu());
        if (qdGiaoNvXh.isPresent()) {
            qdGiaoNvXh.get().setTrangThaiXh(TrangThaiAllEnum.DANG_THUC_HIEN.getId());
        }
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtPhieuXuatKho.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
        return created;
    }

    @Transactional
    public XhXkVtPhieuXuatKho update(CustomUserDetails currentUser, XhXkVtPhieuXuatKhoRequest objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhXkVtPhieuXuatKho> optional = xhXkVtPhieuXuatKhoRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Optional<XhXkVtPhieuXuatKho> soDx = xhXkVtPhieuXuatKhoRepository.findBySoPhieu(objReq.getSoPhieu());
        if (soDx.isPresent()) {
            if (!soDx.get().getId().equals(objReq.getId())) {
                throw new Exception("số số phiếu đã tồn tại");
            }
        }
        XhXkVtPhieuXuatKho data = optional.get();
        BeanUtils.copyProperties(objReq, data);
        XhXkVtPhieuXuatKho created = xhXkVtPhieuXuatKhoRepository.save(data);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhXkVtPhieuXuatKho.TABLE_NAME));
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtPhieuXuatKho.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
        return created;
    }

    public XhXkVtPhieuXuatKho detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
        Optional<XhXkVtPhieuXuatKho> optional = xhXkVtPhieuXuatKhoRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhXkVtPhieuXuatKho model = optional.get();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkVtPhieuXuatKho.TABLE_NAME));
        model.setFileDinhKems(fileDinhKem);
        model.setMapDmucDvi(mapDmucDvi);
        model.setMapVthh(mapVthh);
        model.setTenLoai(Contains.getLoaiHinhXuat(model.getLoai()));
        model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
        model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
        return model;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhXkVtPhieuXuatKho> optional = xhXkVtPhieuXuatKhoRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        XhXkVtPhieuXuatKho data = optional.get();
        //Update trạng thái chưa thực hiện xuất hàng cho qd giao nv xuất hàng
        Optional<XhXkVtQdGiaonvXhHdr> qdGiaoNv = xhXkVtQdGiaonvXhRepository.findById(data.getIdCanCu());
        if (qdGiaoNv.isPresent()) {
            qdGiaoNv.get().setTrangThaiXh(TrangThaiAllEnum.CHUA_THUC_HIEN.getId());
        }
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhXkVtPhieuXuatKho.TABLE_NAME));
        xhXkVtPhieuXuatKhoRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhXkVtPhieuXuatKho> list = xhXkVtPhieuXuatKhoRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> idsQdGiaoNv = list.stream().map(XhXkVtPhieuXuatKho::getIdCanCu).collect(Collectors.toList());
        List<XhXkVtQdGiaonvXhHdr> listQdGiaoNv = xhXkVtQdGiaonvXhRepository.findByIdIn(idsQdGiaoNv);
        if (!listQdGiaoNv.isEmpty()) {
            listQdGiaoNv.forEach(item -> {
                item.setTrangThaiXh(TrangThaiAllEnum.CHUA_THUC_HIEN.getId());
            });
            xhXkVtQdGiaonvXhRepository.saveAll(listQdGiaoNv);
        }
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhXkVtPhieuXuatKho.TABLE_NAME));
        xhXkVtPhieuXuatKhoRepository.deleteAll(list);
    }

    @Transactional
    public XhXkVtPhieuXuatKho approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhXkVtPhieuXuatKho> optional = xhXkVtPhieuXuatKhoRepository.findById(Long.valueOf(statusReq.getId()));
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
        XhXkVtPhieuXuatKho model = xhXkVtPhieuXuatKhoRepository.save(optional.get());
        return model;
    }

    public void export(CustomUserDetails currentUser, XhXkVtPhieuXuatKhoRequest objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<XhXkVtPhieuXuatKho> page = this.searchPage(currentUser, objReq);
        List<XhXkVtPhieuXuatKho> data = page.getContent();

        String title = "Danh sách phiếu xuất kho";
        String[] rowsName = new String[]{"STT", "Số QĐ giao nhiệm vụ XH", "Năm KH", "Thời hạn XH trước ngày", "Điểm kho",
                "Lô kho", "Số phiếu xuất kho", "Ngày xuất kho", "Số phiếu KNCL", "Trạng thái"};
        String fileName = "danh-sach-phieu-xuat-kho.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhXkVtPhieuXuatKho dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getSoCanCu();
            objs[2] = dx.getNamKeHoach();
            objs[3] = dx.getThoiGianGiaoHang();
            objs[4] = dx.getTenDiemKho();
            objs[5] = dx.getTenLoKho();
            objs[6] = dx.getSoPhieu();
            objs[7] = dx.getNgayXuat();
            objs[8] = dx.getSoPhieuKncl();
            objs[9] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}
