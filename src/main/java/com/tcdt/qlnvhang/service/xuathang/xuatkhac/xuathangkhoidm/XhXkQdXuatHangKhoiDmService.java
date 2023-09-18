package com.tcdt.qlnvhang.service.xuathang.xuatkhac.xuathangkhoidm;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtBbLayMauHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatNhapKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.xuathangkhoidm.XhXkDsHangDtqgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.xuathangkhoidm.XhXkQdXuatHangKhoiDmRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtBbLayMauRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.xuathangkhoidm.XhXkQdXuatHangKhoiDmRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtBbLayMauHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm.XhXkDsHangDtqgDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm.XhXkDsHangDtqgHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm.XhXkQdXuatHangKhoiDm;
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

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhXkQdXuatHangKhoiDmService extends BaseServiceImpl {


    @Autowired
    private XhXkQdXuatHangKhoiDmRepository xhXkQdXuatHangKhoiDmRepository;

    @Autowired
    private XhXkDsHangDtqgDtlRepository xhXkDsHangDtqgDtlRepository;


    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhXkQdXuatHangKhoiDm> searchPage(CustomUserDetails currentUser, XhXkQdXuatHangKhoiDmRequest req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhXkQdXuatHangKhoiDm> search = xhXkQdXuatHangKhoiDmRepository.searchPage(req, pageable);
        search.getContent().forEach(s -> {
            s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
        });
        return search;
    }

    @Transactional
    public XhXkQdXuatHangKhoiDm save(CustomUserDetails currentUser, XhXkQdXuatHangKhoiDmRequest objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhXkQdXuatHangKhoiDm> optional = xhXkQdXuatHangKhoiDmRepository.findBySoQd(objReq.getSoQd());
        if (optional.isPresent()) {
            throw new Exception("Số quyết định đã tồn tại");
        }
        XhXkQdXuatHangKhoiDm data = new XhXkQdXuatHangKhoiDm();
        BeanUtils.copyProperties(objReq, data);
        data.setTrangThai(Contains.DUTHAO);
        XhXkQdXuatHangKhoiDm created = xhXkQdXuatHangKhoiDmRepository.save(data);
        xhXkDsHangDtqgDtlRepository.saveAll(objReq.getListDtl());
        created.setFileDinhKems(fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkDsHangDtqgHdr.TABLE_NAME));
        return created;
    }

    @Transactional()
    public XhXkQdXuatHangKhoiDm update(CustomUserDetails currentUser, XhXkQdXuatHangKhoiDmRequest objReq) throws Exception {
        if (objReq.getId() == null) {
            throw new Exception("Bad request!");
        }
        Optional<XhXkQdXuatHangKhoiDm> optional = xhXkQdXuatHangKhoiDmRepository.findBySoQd(objReq.getSoQd());
        if (!optional.isPresent()) throw new Exception("Không tồn tại bản ghi!");

        if (!ObjectUtils.isEmpty(objReq.getSoQd())) {
            Optional<XhXkQdXuatHangKhoiDm> optionalBySoQd = xhXkQdXuatHangKhoiDmRepository.findBySoQd(objReq.getSoQd());
            if (optionalBySoQd.isPresent() && optionalBySoQd.get().getId() != objReq.getId()) {
                if (!optionalBySoQd.isPresent()) throw new Exception("Số quyết định đã tồn tại!");
            }
        }
        XhXkQdXuatHangKhoiDm dx = optional.get();
        BeanUtils.copyProperties(objReq, dx);
        XhXkQdXuatHangKhoiDm created = xhXkQdXuatHangKhoiDmRepository.save(dx);
        xhXkDsHangDtqgDtlRepository.saveAll(objReq.getListDtl());
        fileDinhKemService.delete(dx.getId(), Collections.singleton(XhXkQdXuatHangKhoiDm.TABLE_NAME));
        //save file đính kèm
        fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkQdXuatHangKhoiDm.TABLE_NAME);
        return detail(created.getId());
    }


    @Transactional()
    public XhXkQdXuatHangKhoiDm detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
        Optional<XhXkQdXuatHangKhoiDm> optional = xhXkQdXuatHangKhoiDmRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhXkQdXuatHangKhoiDm model = optional.get();
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkQdXuatHangKhoiDm.TABLE_NAME));
        model.setFileDinhKems(fileDinhKem);
        model.setListDtl(this.buildTreeVattu(xhXkDsHangDtqgDtlRepository.findAllByIdHdr(model.getIdCanCu())));
        model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
        return model;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhXkQdXuatHangKhoiDm> optional = xhXkQdXuatHangKhoiDmRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        if (!optional.get().getTrangThai().equals(TrangThaiAllEnum.DU_THAO.getId())) {
            throw new Exception("Bản ghi có trạng thái khác dự thảo, không thể xóa.");
        }
        XhXkQdXuatHangKhoiDm data = optional.get();
        fileDinhKemService.deleteMultiple(Collections.singleton(data.getId()), Collections.singleton(XhXkQdXuatHangKhoiDm.TABLE_NAME));
        xhXkQdXuatHangKhoiDmRepository.delete(data);
    }


    @Transient
    public XhXkQdXuatHangKhoiDm approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhXkQdXuatHangKhoiDm> optional = xhXkQdXuatHangKhoiDmRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        String status = statusReq.getTrangThai() + optional.get().getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDV + Contains.DUTHAO:
            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDTC:
                optional.get().setNguoiDuyetId(currentUser.getUser().getId());
                optional.get().setNgayDuyet(LocalDate.now());
                break;
            case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
            case Contains.TUCHOI_LDTC + Contains.CHODUYET_LDTC:
                optional.get().setNguoiDuyetId(currentUser.getUser().getId());
                optional.get().setNgayDuyet(LocalDate.now());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.DADUYET_LDV + Contains.CHODUYET_LDV:
            case Contains.DADUYET_LDTC + Contains.CHODUYET_LDTC:
                optional.get().setNguoiDuyetId(currentUser.getUser().getId());
                optional.get().setNgayDuyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        XhXkQdXuatHangKhoiDm created = xhXkQdXuatHangKhoiDmRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, XhXkQdXuatHangKhoiDmRequest objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<XhXkQdXuatHangKhoiDm> page = this.searchPage(currentUser, objReq);
        List<XhXkQdXuatHangKhoiDm> data = page.getContent();

        String title = "Danh sách quyết định xuất hàng khỏi danh mục";
        String[] rowsName = new String[]{"STT", "Số quyết định", "Ngày ký", "Ngày hiệu lực", "Mã danh sách hàng ngoài danh mục",
                "Trích yếu", "Trạng thái"};
        String fileName = "danh-sach-qd-xuat-hang-khoi-danh-muc.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhXkQdXuatHangKhoiDm dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getSoQd();
            objs[2] = dx.getNgayKy();
            objs[3] = dx.getNgayHieuLuc();
            objs[4] = dx.getMaCanCu();
            objs[5] = dx.getTrichYeu();
            objs[6] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public List<XhXkDsHangDtqgDtl> buildTreeVattu(List<XhXkDsHangDtqgDtl> flatNodes) {
        Map<String, XhXkDsHangDtqgDtl> nodeMap = new HashMap<>();
        for (XhXkDsHangDtqgDtl node : flatNodes) {
            nodeMap.put(node.getMa(), node);
        }
        List<XhXkDsHangDtqgDtl> treeNodes = new ArrayList<>();
        for (XhXkDsHangDtqgDtl node : flatNodes) {
            node.setTenNhomHang(!ObjectUtils.isEmpty(node.getLoaiHang()) ? (node.getLoaiHang().equals("VT") ? "Vật tư" : "Lương Thực") : "");
            XhXkDsHangDtqgDtl parent = nodeMap.get(node.getMaCha());
            if (parent == null) {
                treeNodes.add(node);
            } else {
                node.setTenCha(parent.getTen());
                parent.getChildren().add(node);
            }
        }
        return treeNodes;
    }
}

