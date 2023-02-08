package com.tcdt.qlnvhang.service.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtVtQdPdDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtVtQdPdDxRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtVtQdPdHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtTongHopHdr;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtVtQuyetDinhPdDtlReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtVtQuyetDinhPdDxReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtVtQuyetDinhPdHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.XhQdCuuTroHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtVtQuyetDinhPdDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtVtQuyetDinhPdDx;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtVtQuyetDinhPdHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhCtVtQdPdHdrService extends BaseServiceImpl {


    @Autowired
    private XhCtVtQdPdHdrRepository xhCtVtQdPdHdrRepository;

    @Autowired
    private XhCtVtQdPdDtlRepository xhCtVtQdPdDtlRepository;

    @Autowired

    private XhCtVtQdPdDxRepository xhCtVtQdPdDxRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;


    public Page<XhCtVtQuyetDinhPdHdr> searchPage(CustomUserDetails currentUser, SearchXhCtvtTongHopHdr req) throws Exception {
        req.setDvql(currentUser.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhCtVtQuyetDinhPdHdr> search = xhCtVtQdPdHdrRepository.search(req, pageable);
        Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");

        Map<String, String> mapVthh = getListDanhMucHangHoa();
        search.getContent().forEach(s -> {
            if (mapDmucDvi.containsKey((s.getMaDvi()))) {
                Map<String, Object> objDonVi = mapDmucDvi.get(s.getMaDvi());
                s.setTenDvi(objDonVi.get("tenDvi").toString());
            }
            if (mapVthh.get((s.getLoaiVthh())) != null) {
                s.setTenLoaiVthh(mapVthh.get(s.getLoaiVthh()));
            }
            if (mapVthh.get((s.getCloaiVthh())) != null) {
                s.setTenCloaiVthh(mapVthh.get(s.getCloaiVthh()));
            }
            s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
        });
        return search;
    }

    @Transactional
    public XhCtVtQuyetDinhPdHdr save(CustomUserDetails currentUser, XhCtVtQuyetDinhPdHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhCtVtQuyetDinhPdHdr> optional = xhCtVtQdPdHdrRepository.findBySoQd(objReq.getSoDx());
        if (optional.isPresent()) {
            throw new Exception("số quyết định đã tồn tại");
        }
        XhCtVtQuyetDinhPdHdr data = new XhCtVtQuyetDinhPdHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(Contains.DUTHAO);
        data.setMaTongHop("Chưa tổng hợp");
        ;
        XhCtVtQuyetDinhPdHdr created = xhCtVtQdPdHdrRepository.save(data);

        if (!DataUtils.isNullOrEmpty(objReq.getCanCu())) {
            fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhCtVtQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU");
        }
        if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
            fileDinhKemService.saveListFileDinhKem(Arrays.asList(objReq.getFileDinhKem()), created.getId(), XhCtVtQuyetDinhPdHdr.TABLE_NAME);
        }

        this.saveCtiet(created.getId(), objReq);
        return created;
    }

    @Transactional()
    void saveCtiet(Long idHdr, XhCtVtQuyetDinhPdHdrReq objReq) {
        for (XhCtVtQuyetDinhPdDtlReq quyetDinhPdDtlReq : objReq.getQuyetDinhPdDtl()) {
            XhCtVtQuyetDinhPdDtl quyetDinhPdDtl = new XhCtVtQuyetDinhPdDtl();
            BeanUtils.copyProperties(quyetDinhPdDtlReq, quyetDinhPdDtl);
            quyetDinhPdDtl.setId(null);
            quyetDinhPdDtl.setIdHdr(idHdr);
            xhCtVtQdPdDtlRepository.save(quyetDinhPdDtl);
            for (XhCtVtQuyetDinhPdDxReq quyetDinhPdDxReq : quyetDinhPdDtlReq.getQuyetDinhPdDx()) {
                XhCtVtQuyetDinhPdDx quyetDinhPdDx = new XhCtVtQuyetDinhPdDx();
                BeanUtils.copyProperties(quyetDinhPdDxReq, quyetDinhPdDx);
                quyetDinhPdDx.setId(null);
                quyetDinhPdDx.setIdHdr(idHdr);
                xhCtVtQdPdDxRepository.save(quyetDinhPdDx);
            }
        }
    }

    @Transactional
    public XhCtVtQuyetDinhPdHdr update(CustomUserDetails currentUser, XhCtVtQuyetDinhPdHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhCtVtQuyetDinhPdHdr> optional = xhCtVtQdPdHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Optional<XhCtVtQuyetDinhPdHdr> soDx = xhCtVtQdPdHdrRepository.findBySoQd(objReq.getSoDx());
        if (soDx.isPresent()) {
            if (!soDx.get().getId().equals(objReq.getId())) {
                throw new Exception("số quyết định đã tồn tại");
            }
        }
        XhCtVtQuyetDinhPdHdr data = optional.get();
        BeanUtils.copyProperties(objReq, data);
        XhCtVtQuyetDinhPdHdr created = xhCtVtQdPdHdrRepository.save(data);

        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhCtVtQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU"));

        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhCtVtQuyetDinhPdHdr.TABLE_NAME));

        if (!DataUtils.isNullOrEmpty(objReq.getCanCu())) {
            fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhCtVtQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU");
        }
        if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
            fileDinhKemService.saveListFileDinhKem(Arrays.asList(objReq.getFileDinhKem()), created.getId(), XhCtVtQuyetDinhPdHdr.TABLE_NAME);
        }

        List<XhCtVtQuyetDinhPdDtl> quyetDinhPdDtl = xhCtVtQdPdDtlRepository.findByIdHdr(objReq.getId());
        xhCtVtQdPdDtlRepository.deleteAll(quyetDinhPdDtl);

        List<Long> XhCtVtQuyetDinhPdDx = quyetDinhPdDtl.stream().map(XhCtVtQuyetDinhPdDtl::getId).collect(Collectors.toList());
        List<XhCtVtQuyetDinhPdDx> quyetDinhPdDx = xhCtVtQdPdDxRepository.findByIdHdrIn(XhCtVtQuyetDinhPdDx);
        xhCtVtQdPdDxRepository.deleteAll(quyetDinhPdDx);
        this.saveCtiet(created.getId(), objReq);
        return created;
    }


    public List<XhCtVtQuyetDinhPdHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<XhCtVtQuyetDinhPdHdr> optional = xhCtVtQdPdHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        List<XhCtVtQuyetDinhPdHdr> allById = xhCtVtQdPdHdrRepository.findAllById(ids);
        allById.forEach(data -> {
            if (mapDmucDvi.containsKey(data.getMaDvi())) {
                data.setTenDvi(mapDmucDvi.get(data.getMaDvi()).get("tenDvi").toString());
            }
            data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
            data.setTenCloaiVthh(mapVthh.get(data.getCloaiVthh()));
            data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));

            List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdCuuTroHdr.TABLE_NAME));
            if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
                data.setFileDinhKem(fileDinhKem.get(0));
            }

            List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(XhCtVtQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU"));
            data.setCanCu(canCu);

            List<XhCtVtQuyetDinhPdDtl> ListDtl = xhCtVtQdPdDtlRepository.findByIdHdr(data.getId());
            for (XhCtVtQuyetDinhPdDtl quyetDinhPdDtl : ListDtl) {
                if (mapDmucDvi.containsKey(quyetDinhPdDtl.getMaDviDx())) {
                    quyetDinhPdDtl.setTenDviDx(mapDmucDvi.get(quyetDinhPdDtl.getMaDviDx()).get("tenDvi").toString());

                    List<Long> XhCtVtQuyetDinhPdDx = ListDtl.stream().map(XhCtVtQuyetDinhPdDtl::getId).collect(Collectors.toList());
                    List<XhCtVtQuyetDinhPdDx> listQuyetDinhPdDx = xhCtVtQdPdDxRepository.findByIdHdrIn(XhCtVtQuyetDinhPdDx);
                    for (XhCtVtQuyetDinhPdDx quyetDinhPdDx : listQuyetDinhPdDx) {
                        if (mapDmucDvi.containsKey(quyetDinhPdDx.getMaDviCuc())) {
                            quyetDinhPdDx.setTenCuc(mapDmucDvi.get(quyetDinhPdDx.getMaDviCuc()).get("tenDvi").toString());
                        }
                        if (mapDmucDvi.containsKey(quyetDinhPdDx.getMaDviChiCuc())) {
                            quyetDinhPdDx.setTenChiCuc(mapDmucDvi.get(quyetDinhPdDx.getMaDviChiCuc()).get("tenDvi").toString());
                        }
                        quyetDinhPdDx.setTenCloaiVthh(mapVthh.get(quyetDinhPdDx.getCloaiVthh()));
                    }
                    quyetDinhPdDtl.setQuyetDinhPdDx(listQuyetDinhPdDx);
                }
                data.setQuyetDinhPdDtl(ListDtl);
            }
        });
        return allById;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhCtVtQuyetDinhPdHdr> optional = xhCtVtQdPdHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        XhCtVtQuyetDinhPdHdr data = optional.get();
        List<XhCtVtQuyetDinhPdDtl> quyetDinhPdDtl = xhCtVtQdPdDtlRepository.findByIdHdr(data.getId());
        xhCtVtQdPdDtlRepository.deleteAll(quyetDinhPdDtl);

        List<Long> XhCtVtQuyetDinhPdDx = quyetDinhPdDtl.stream().map(XhCtVtQuyetDinhPdDtl::getId).collect(Collectors.toList());
        List<XhCtVtQuyetDinhPdDx> quyetDinhPdDx = xhCtVtQdPdDxRepository.findByIdHdrIn(XhCtVtQuyetDinhPdDx);
        xhCtVtQdPdDxRepository.deleteAll(quyetDinhPdDx);

        fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhCtVtQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhCtVtQuyetDinhPdHdr.TABLE_NAME));
        xhCtVtQdPdHdrRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhCtVtQuyetDinhPdHdr> list = xhCtVtQdPdHdrRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listId = list.stream().map(XhCtVtQuyetDinhPdHdr::getId).collect(Collectors.toList());
        List<XhCtVtQuyetDinhPdDtl> listDtl = xhCtVtQdPdDtlRepository.findAllByIdHdrIn(listId);
        List<Long> XhCtVtQuyetDinhPdDx = listDtl.stream().map(XhCtVtQuyetDinhPdDtl::getId).collect(Collectors.toList());
        List<XhCtVtQuyetDinhPdDx> quyetDinhPdDx = xhCtVtQdPdDxRepository.findByIdHdrIn(XhCtVtQuyetDinhPdDx);
        xhCtVtQdPdDxRepository.deleteAll(quyetDinhPdDx);
        xhCtVtQdPdDtlRepository.deleteAll(listDtl);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhCtVtQuyetDinhPdHdr.TABLE_NAME));
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhCtVtQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU"));
        xhCtVtQdPdHdrRepository.deleteAll(list);
    }


    public XhCtVtQuyetDinhPdHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhCtVtQuyetDinhPdHdr> optional = xhCtVtQdPdHdrRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status = statusReq.getTrangThai() + optional.get().getTrangThai();
        switch (status) {
            case Contains.BAN_HANH + Contains.DUTHAO:
                optional.get().setNgayPduyet(LocalDate.now());
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        XhCtVtQuyetDinhPdHdr created = xhCtVtQdPdHdrRepository.save(optional.get());
        return created;
    }


    public void export(CustomUserDetails currentUser, SearchXhCtvtTongHopHdr objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<XhCtVtQuyetDinhPdHdr> page = this.searchPage(currentUser, objReq);
        List<XhCtVtQuyetDinhPdHdr> data = page.getContent();

        String title = "Danh sách quyết định phương án xuất cứu trợ, viện trợ ";
        String[] rowsName = new String[]{"STT", "Số quyết định", "Ngày ký quyết định", "Mã tổng hợp", "Ngày tổng hợp", "Số công văn/đề xuất", "Ngày đề xuất",
                "Loại hàng hóa", "Tổng SL đề xuất cứu trợ,viện trợ (kg)", "Tổng SL xuất kho cứu trợ,viện trợ (kg)", "SL xuất CT,VT chuyển sang xuất cấp", "Trích yếu", "Trạng thái quyết định",};
        String fileName = "danh-sach-phuong-an-xuat-cuu-tro-vien-tro";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhCtVtQuyetDinhPdHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getSoQd();
            objs[2] = dx.getNgayKy();
            objs[3] = dx.getMaTongHop();
            objs[4] = dx.getNgayThop();
            objs[5] = dx.getSoDx();
            objs[6] = dx.getNgayDx();
            objs[7] = dx.getTenLoaiVthh();
            objs[8] = dx.getTongSoLuongDx();
            objs[9] = dx.getTongSoLuong();
            objs[10] = dx.getSoLuongXuaCap();
            objs[11] = dx.getTrichYeu();
            objs[12] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}