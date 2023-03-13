package com.tcdt.qlnvhang.service.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhCtvtQdXuatCapDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhCtvtQdXuatCapHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtVtQdPdDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtVtQdPdHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap.SearchXhCtvtQdXuatCap;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhCtvtQdXuatCapDtlReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhCtvtQdXuatCapReq;
import com.tcdt.qlnvhang.response.xuathang.xuatcuutrovientro.xuatcap.XhCtvtQdXuatCap;
import com.tcdt.qlnvhang.response.xuathang.xuatcuutrovientro.xuatcap.XhCtvtQdXuatCapChiTiet;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhCtvtQdXuatCapDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhCtvtQdXuatCapHdr;

import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtVtQuyetDinhPdDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtVtQuyetDinhPdHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhCtvtQdXuatCapService extends BaseServiceImpl {
    @Autowired
    private XhCtvtQdXuatCapHdrRepository xhCtvtQdXuatCapHdrRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Autowired
    private XhCtvtQdXuatCapDtlRepository xhCtvtQdXuatCapDtlRepository;
    @Autowired
    private XhCtVtQdPdDtlRepository xhCtVtQdPdDtlRepository;

    public Page<XhCtvtQdXuatCapHdr> search(SearchXhCtvtQdXuatCap req, CustomUserDetails currentUser) {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhCtvtQdXuatCapHdr> data = xhCtvtQdXuatCapHdrRepository.search(req, pageable);
        data.getContent().forEach(item -> item.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(item.getTrangThai())));
        return data;
    }

    @Transactional
    public boolean save(CustomUserDetails currentUser, XhCtvtQdXuatCapReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (objReq != null) {
            if (objReq.getSoQd() != null && !"".equals(objReq.getSoQd())) {
                Optional<XhCtvtQdXuatCapHdr> optional = xhCtvtQdXuatCapHdrRepository.findBySoQd(objReq.getSoQd());
                if (optional.isPresent()) {
                    throw new Exception("số quyết định đã tồn tại");
                }
            }
            XhCtvtQdXuatCapHdr data = new XhCtvtQdXuatCapHdr();
            BeanUtils.copyProperties(objReq, data, "id");
            data.setMaDvi(currentUser.getUser().getDepartment());
            data.setTrangThai(Contains.DUTHAO);
            XhCtvtQdXuatCapHdr created = xhCtvtQdXuatCapHdrRepository.save(data);
            if (!DataUtils.isNullOrEmpty(objReq.getCanCu())) {
                fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhCtvtQdXuatCapHdr.TABLE_NAME + "_CAN_CU");
            }
            if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
                fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), created.getId(), XhCtvtQdXuatCapHdr.TABLE_NAME);
            }
            saveChiTiet(created.getId(), objReq);
            return true;
        }
        throw new Exception("Thông tin thêm mới không hợp lệ.");
    }

    @Transactional
    public boolean update(CustomUserDetails currentUser, XhCtvtQdXuatCapReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (objReq != null) {
            Optional<XhCtvtQdXuatCapHdr> xhCtvtQdXuatCapHdr = xhCtvtQdXuatCapHdrRepository.findById(objReq.getId());
            if (!xhCtvtQdXuatCapHdr.isPresent()) {
                throw new Exception("Không tìm thấy dữ liệu cần sửa");
            }
            if (objReq.getSoQd() != null && !"".equals(objReq.getSoQd())) {
                Optional<XhCtvtQdXuatCapHdr> optional = xhCtvtQdXuatCapHdrRepository.findBySoQd(objReq.getSoQd());
                if (optional.isPresent() && !optional.get().getId().equals(objReq.getId())) {
                    throw new Exception("số quyết định đã tồn tại");
                }
            }
            XhCtvtQdXuatCapHdr data = xhCtvtQdXuatCapHdr.get();
            BeanUtils.copyProperties(objReq, data, "id");
            XhCtvtQdXuatCapHdr created = xhCtvtQdXuatCapHdrRepository.save(data);
            fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhCtvtQdXuatCapHdr.TABLE_NAME + "_CAN_CU"));
            fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhCtvtQdXuatCapHdr.TABLE_NAME));
            if (!DataUtils.isNullOrEmpty(objReq.getCanCu())) {
                fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhCtvtQdXuatCapHdr.TABLE_NAME + "_CAN_CU");
            }
            if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
                fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), created.getId(), XhCtvtQdXuatCapHdr.TABLE_NAME);
            }
            xhCtvtQdXuatCapDtlRepository.deleteAllByHdrId(objReq.getId());
            saveChiTiet(created.getId(), objReq);
            return true;
        }
        throw new Exception("Thông tin cập nhật không hợp lệ.");
    }

    public XhCtvtQdXuatCapChiTiet detail(Long id) throws Exception {
        if (id == null) {
            throw new Exception("Tham số không hợp lệ.");
        }
        Optional<XhCtvtQdXuatCapHdr> xhCtvtQdXuatCapHdr = xhCtvtQdXuatCapHdrRepository.findById(id);
        if (!xhCtvtQdXuatCapHdr.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhCtvtQdXuatCapChiTiet data = new XhCtvtQdXuatCapChiTiet();
        BeanUtils.copyProperties(xhCtvtQdXuatCapHdr.get(), data);
        if (xhCtvtQdXuatCapHdr.get().getQdPaXuatCapId() != null) {
            data.setQdPaXuatCapId(xhCtvtQdXuatCapHdr.get().getQdPaXuatCapId());
            List<XhCtVtQuyetDinhPdDtl> listDtl = xhCtVtQdPdDtlRepository.findByIdHdr(xhCtvtQdXuatCapHdr.get().getQdPaXuatCapId());
            data.setQuyetDinhPdDtl(listDtl);
            data.setIsChonPhuongAn(true);
        }
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(id, Collections.singletonList(XhCtVtQuyetDinhPdHdr.TABLE_NAME));
        data.setFileDinhKem(fileDinhKem);
        List<FileDinhKem> canCu = fileDinhKemService.search(id, Collections.singletonList(XhCtVtQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU"));
        data.setCanCu(canCu);
        Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
        List<XhCtvtQdXuatCapDtl> dtlList = xhCtvtQdXuatCapDtlRepository.findByHdrId(id);
        List<XhCtvtQdXuatCapDtlReq> dataDtl = new ArrayList<>();
        for (XhCtvtQdXuatCapDtl item : dtlList) {
            XhCtvtQdXuatCapDtlReq target = new XhCtvtQdXuatCapDtlReq();
            BeanUtils.copyProperties(item, target, "id");
            if (mapDmucDvi.containsKey(item.getMaDviCuc())) {
                target.setTenCuc(mapDmucDvi.get(item.getMaDviCuc()).get("tenDvi").toString());
            }
            if (mapDmucDvi.containsKey(item.getMaDviChiCuc())) {
                target.setTenChiCuc(mapDmucDvi.get(item.getMaDviChiCuc()).get("tenDvi").toString());
            }
            target.setTenCloaiVthh(mapVthh.get(item.getCloaiVthh()));
            dataDtl.add(target);
        }
        data.setDeXuatPhuongAn(dataDtl);
        return data;
    }

    @Transactional
    public void saveChiTiet(Long id, XhCtvtQdXuatCapReq objReq) {
        for (XhCtvtQdXuatCapDtlReq qdXuatCapDtlReq : objReq.getDeXuatPhuongAn()) {
            XhCtvtQdXuatCapDtl xhCtvtQdXuatCapDtl = new XhCtvtQdXuatCapDtl();
            BeanUtils.copyProperties(qdXuatCapDtlReq, xhCtvtQdXuatCapDtl);
            xhCtvtQdXuatCapDtl.setHdrId(id);
            xhCtvtQdXuatCapDtlRepository.save(xhCtvtQdXuatCapDtl);
        }
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhCtvtQdXuatCapHdr> xhCtvtQdXuatCapHdr = xhCtvtQdXuatCapHdrRepository.findById(idSearchReq.getId());
        if (!xhCtvtQdXuatCapHdr.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<XhCtvtQdXuatCapDtl> xhCtvtQdXuatCapDtls = xhCtvtQdXuatCapDtlRepository.findByHdrId(xhCtvtQdXuatCapHdr.get().getId());
        xhCtvtQdXuatCapDtlRepository.deleteAll(xhCtvtQdXuatCapDtls);
        fileDinhKemService.delete(xhCtvtQdXuatCapHdr.get().getId(), Lists.newArrayList(XhCtvtQdXuatCapHdr.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.delete(xhCtvtQdXuatCapHdr.get().getId(), Lists.newArrayList(XhCtvtQdXuatCapHdr.TABLE_NAME));
        xhCtvtQdXuatCapHdrRepository.delete(xhCtvtQdXuatCapHdr.get());
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhCtvtQdXuatCapHdr> list = xhCtvtQdXuatCapHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listId = list.stream().map(XhCtvtQdXuatCapHdr::getId).collect(Collectors.toList());
        List<XhCtvtQdXuatCapDtl> listDtl = xhCtvtQdXuatCapDtlRepository.findAllByHdrIdIn(listId);
        xhCtvtQdXuatCapDtlRepository.deleteAll(listDtl);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhCtvtQdXuatCapHdr.TABLE_NAME));
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhCtvtQdXuatCapHdr.TABLE_NAME + "_CAN_CU"));
        xhCtvtQdXuatCapHdrRepository.deleteAll(list);
    }
}
