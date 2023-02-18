package com.tcdt.qlnvhang.service.xuathang.daugia.nhiemvuxuat;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMau;
import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXh;
import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDdiem;
import com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDtl;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.daugia.ktracluong.bienbanlaymau.XhBbLayMauRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDdiemRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.nhiemvuxuat.XhQdGiaoNvXhRepository;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.xuathang.XhQdGiaoNvuXuatSearchReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvXhDdiemReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatCtReq;
import com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat.XhQdGiaoNvuXuatReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor
public class XhQdGiaoNvXhServiceImpl extends BaseServiceImpl implements XhQdGiaoNvXhService {

    @Autowired
    private XhQdGiaoNvXhRepository xhQdGiaoNvXhRepository;

    @Autowired
    private XhQdGiaoNvXhDtlRepository xhQdGiaoNvXhDtlRepository;

    @Autowired
    private XhQdGiaoNvXhDdiemRepository xhQdGiaoNvXhDdiemRepository;

    @Autowired
    private XhBbLayMauRepository xhBbLayMauRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Override
    public Page<XhQdGiaoNvXh> searchPage(XhQdGiaoNvuXuatReq objReq) throws Exception {
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhQdGiaoNvXh> data = xhQdGiaoNvXhRepository.searchPage( objReq,pageable);
        Map<String, String> mapDmucHh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

        data.getContent().forEach(item -> {
            List<XhBbLayMau> allByIdQd = xhBbLayMauRepository.findAllByIdQd(item.getId());
            allByIdQd.forEach(x -> {
                x.setTenLoaiVthh(mapDmucHh.get(x.getLoaiVthh()));
                x.setTenCloaiVthh(mapDmucHh.get(x.getCloaiVthh()));
                x.setTenDiemKho(mapDmucDvi.get(x.getMaDiemKho()));
                x.setTenNhaKho(mapDmucDvi.get(x.getMaNganKho()));
                x.setTenNganKho(mapDmucDvi.get(x.getMaNganKho()));
                x.setTenLoKho(mapDmucDvi.get(x.getMaLoKho()));
            });
            item.setXhBbLayMauList(allByIdQd);
            item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai()));
            item.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThaiXh()));
            item.setTenDvi(mapDmucDvi.get(item.getMaDvi()));
            item.setTenLoaiVthh(mapDmucHh.get(item.getLoaiVthh()));
            item.setTenCloaiVthh(mapDmucHh.get(item.getCloaiVthh()));
        });
        return data;
    }



    @Override
    @Transactional
    public XhQdGiaoNvXh create(XhQdGiaoNvuXuatReq objReq) throws Exception {

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }


        Optional<XhQdGiaoNvXh> optional = xhQdGiaoNvXhRepository.findAllBySoQd(objReq.getSoQd());
        if (optional.isPresent()) {
            throw new Exception("Số quyết định đã tồn tại");
        }

        Map<String, String> mapDmucHh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

        XhQdGiaoNvXh dataMap = new XhQdGiaoNvXh();
        BeanUtils.copyProperties(objReq, dataMap, "id");

        dataMap.setNguoiTaoId(userInfo.getId());
        dataMap.setNgayTao(new Date());
        dataMap.setTrangThai(Contains.DUTHAO);
        dataMap.setTrangThaiXh(Contains.CHUACAPNHAT);
        dataMap.setMaDvi(userInfo.getDvql());
        dataMap.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : mapDmucDvi.get(userInfo.getDvql()));
        XhQdGiaoNvXh created = xhQdGiaoNvXhRepository.save(dataMap);
        if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Arrays.asList(objReq.getFileDinhKem()), created.getId(), XhQdGiaoNvXh.TABLE_NAME);
            created.setFileDinhKems(fileDinhKem);
        }
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), dataMap.getId(), XhQdGiaoNvXh.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
        this.saveDetail(dataMap, objReq);
        return created;
    }

    public void saveDetail(XhQdGiaoNvXh dataMap, XhQdGiaoNvuXuatReq objReq) {
        for (XhQdGiaoNvuXuatCtReq req : objReq.getChildren()) {
            XhQdGiaoNvXhDtl dtl = new ModelMapper().map(req, XhQdGiaoNvXhDtl.class);
            dtl.setId(null);
            dtl.setIdQdHdr(dataMap.getId());
            dtl.setTrangThai(NhapXuatHangTrangThaiEnum.CHUA_THUC_HIEN.getId());
            xhQdGiaoNvXhDtlRepository.save(dtl);
            for (XhQdGiaoNvXhDdiemReq ddiemReq : req.getChildren()) {
                XhQdGiaoNvXhDdiem ddiem = new ModelMapper().map(ddiemReq, XhQdGiaoNvXhDdiem.class);
                ddiem.setId(null);
                ddiem.setIdDtl(dtl.getId());
                xhQdGiaoNvXhDdiemRepository.save(ddiem);
            }
        }
    }

    @Override
    @Transactional
    public XhQdGiaoNvXh update(XhQdGiaoNvuXuatReq objReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        if (StringUtils.isEmpty(objReq.getId())) {
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
        }

        Optional<XhQdGiaoNvXh> optional = xhQdGiaoNvXhRepository.findById(objReq.getId());
        if (!optional.isPresent())
            throw new Exception("Không cần thấy dữ liệu cần sửa");

        XhQdGiaoNvXh data = optional.get();
        BeanUtils.copyProperties(objReq, data, "id");

        data.setNgaySua(new Date());
        data.setNguoiSuaId(userInfo.getId());
        XhQdGiaoNvXh created = xhQdGiaoNvXhRepository.save(data);
        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhQdGiaoNvXh.TABLE_NAME));
        if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Arrays.asList(objReq.getFileDinhKem()), created.getId(), XhQdGiaoNvXh.TABLE_NAME);
            created.setFileDinhKems(fileDinhKem);
        }
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), data.getId(), XhQdGiaoNvXh.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
        List<XhQdGiaoNvXhDtl> listDtl = xhQdGiaoNvXhDtlRepository.findAllByIdQdHdr(data.getId());
        xhQdGiaoNvXhDtlRepository.deleteAll(listDtl);
        List<Long> listId = listDtl.stream().map(XhQdGiaoNvXhDtl::getId).collect(Collectors.toList());
        List<XhQdGiaoNvXhDdiem> listDd = xhQdGiaoNvXhDdiemRepository.findAllByIdDtlIn(listId);
        xhQdGiaoNvXhDdiemRepository.deleteAll(listDd);
        this.saveDetail(data, objReq);
        return created;
    }

    @Override
    public XhQdGiaoNvXh detail(Long id) throws Exception {
        Optional<XhQdGiaoNvXh> optional = xhQdGiaoNvXhRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }

        XhQdGiaoNvXh data = optional.get();
        Map<String, String> mapDmucHh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
        data.setTenLoaiVthh(mapDmucHh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(mapDmucHh.get(data.getCloaiVthh()));

        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdGiaoNvXh.TABLE_NAME));
        if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
            data.setFileDinhKem(fileDinhKem.get(0));
        }
        List<FileDinhKem> fileDinhkems = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdGiaoNvXh.TABLE_NAME));
        if (!DataUtils.isNullOrEmpty(fileDinhkems)) {
            data.setFileDinhKems(fileDinhkems);
        }

        List<XhQdGiaoNvXhDtl> listDtl = xhQdGiaoNvXhDtlRepository.findAllByIdQdHdr(data.getId());
        List<Long> listId = listDtl.stream().map(XhQdGiaoNvXhDtl::getId).collect(Collectors.toList());
        List<XhQdGiaoNvXhDdiem> listDd = xhQdGiaoNvXhDdiemRepository.findAllByIdDtlIn(listId);
        for (XhQdGiaoNvXhDtl dtl : listDtl) {
            dtl.setTenDvi(mapDmucDvi.get(dtl.getMaDvi()));
            dtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dtl.getTrangThai()));
            listDd.forEach(item -> {
                item.setTenDiemKho(mapDmucDvi.get(item.getMaDiemKho()));
                item.setTenNhaKho(mapDmucDvi.get(item.getMaNhaKho()));
                item.setTenNganKho(mapDmucDvi.get(item.getMaNganKho()));
                item.setTenLoKho(mapDmucDvi.get(item.getMaLoKho()));
            });
            dtl.setChildren(listDd);
        }
        data.setChildren(listDtl);

        return data;
    }

    @Override
    public XhQdGiaoNvXh approve(XhQdGiaoNvuXuatReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (StringUtils.isEmpty(req.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhQdGiaoNvXh> optional = xhQdGiaoNvXhRepository.findById(Long.valueOf(req.getId()));
        if (!optional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu.");
        XhQdGiaoNvXh data = optional.get();
        String status = req.getTrangThai() + optional.get().getTrangThai();
        switch (status) {
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                data.setNguoiGuiDuyetId(userInfo.getId());
                data.setNgayGuiDuyet(getDateTimeNow());
                break;
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(getDateTimeNow());
                data.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.BAN_HANH + Contains.CHODUYET_LDC:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(req.getTrangThai());
        xhQdGiaoNvXhRepository.save(data);
        return data;
    }

    @Override
    public void delete(Long id) throws Exception {

    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(XhQdGiaoNvuXuatReq req, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        req.setMaDvi(userInfo.getDvql());
        Page<XhQdGiaoNvXh> page = this.searchPage(req);
        List<XhQdGiaoNvXh> data = page.getContent();

        String title = "Danh sách quyết định giao nhiệm vụ xuất hàng";
        String[] rowsName = new String[]{"STT", "Năm xuất", "Số quyết định", "Ngày quyết định", "Số hợp đồng", "Loại hàng hóa", "Chủng loại hàng hóa", "Thời gian giao nhận hàng", "Trích yếu quyết định", "Số BB tịnh kho", "Số BB hao dôi", "Trạng thái QĐ", "Trạng thái XH"};
        String filename = "Danh_sach_quyet_dinh_giao_nhiem_vu_xuat_hang.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhQdGiaoNvXh qdXh = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = qdXh.getNam();
            objs[2] = qdXh.getSoQd();
            objs[3] = qdXh.getNgayTao();
            objs[4] = qdXh.getSoHd();
            objs[5] = qdXh.getTenLoaiVthh();
            objs[6] = qdXh.getTenCloaiVthh();
            objs[7] = qdXh.getTgianGnhan();
            objs[8] = qdXh.getTrichYeu();
            objs[9] = qdXh.getBbTinhKho();
            objs[10] = qdXh.getBbHaoDoi();
            objs[11] = qdXh.getTenTrangThai();
            objs[12] = qdXh.getTenTrangThaiXh();
            dataList.add(objs);

        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }

//    @Override
//    @Transactional()
//    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
//        List<XhQdGiaoNvXh> list = xhQdGiaoNvXhRepository.findAllByIdIn(idSearchReq.getIdList());
//        if (list.isEmpty()) {
//            throw new Exception("Xóa thất bại bản ghi không tồn tại");
//        }
//
//        for (XhQdGiaoNvXh xhQdGiaoNvXh : list) {
//            if (!xhQdGiaoNvXh.getTrangThai().equals(Contains.DUTHAO)
//                    && !xhQdGiaoNvXh.getTrangThai().equals(Contains.TUCHOI_LDC)) {
//                throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
//            }
//        }
//
//        List<Long> listIdHdr = list.stream().map(XhQdGiaoNvXh::getId).collect(Collectors.toList());
//        List<XhQdGiaoNvXhDtl> listDtl = xhQdGiaoNvXhDtlRepository.findAllByIdQdHdrIn(listIdHdr);
//        xhQdGiaoNvXhDtlRepository.deleteAll(listDtl);
//        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhQdGiaoNvXh.TABLE_NAME));
//        xhQdGiaoNvXhRepository.deleteAll(list);
//    }

}
