package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlHopDongRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlHopDongHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlHopDongHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class XhTlHopDongService extends BaseServiceImpl {

    @Autowired
    private XhTlHopDongRepository xhTlHopDongRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhTlHopDongHdr> searchPage(CustomUserDetails currentUser, XhTlHopDongHdrReq req) throws Exception {
        String dvql = currentUser.getDvql();
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            req.setDvql(dvql.substring(0, 4));
        } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
            req.setDvql(dvql);
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhTlHopDongHdr> search = xhTlHopDongRepository.search(req, pageable);
        Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        search.getContent().forEach(f -> {
            if (mapDmucDvi.containsKey((f.getMaDvi()))) {
                Map<String, Object> objDonVi = mapDmucDvi.get(f.getMaDvi());
                f.setTenDvi(objDonVi.get("tenDvi").toString());
            }
            f.setMapVthh(mapVthh);
            f.setTenTrangThai(TrangThaiAllEnum.getLabelById(f.getTrangThai()));
        });
        return search;
    }

    @Transactional
    public XhTlHopDongHdr create(CustomUserDetails currentUser, XhTlHopDongHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        if (!DataUtils.isNullObject(req.getSoHd())) {
            Optional<XhTlHopDongHdr> optional = xhTlHopDongRepository.findBySoHd(req.getSoHd());
            if (optional.isPresent()) throw new Exception("Số hợp đồng đã tồn tại");
        }
        XhTlHopDongHdr data = new XhTlHopDongHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(Contains.DU_THAO);
        data.getHopDongDtl().forEach(f -> {
            f.setHopDongHdr(data);
        });
        XhTlHopDongHdr created = xhTlHopDongRepository.save(data);
        if (!DataUtils.isNullOrEmpty(req.getFileCanCu())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileCanCu(), created.getId(), XhTlHopDongHdr.TABLE_NAME + "_CAN_CU");
        }
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKem())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhTlHopDongHdr.TABLE_NAME);
        }
        return created;
    }

    @Transactional
    public XhTlHopDongHdr update(CustomUserDetails currentUser, XhTlHopDongHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");

        Optional<XhTlHopDongHdr> optional = xhTlHopDongRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");

        Optional<XhTlHopDongHdr> soHd = xhTlHopDongRepository.findBySoHd(req.getSoHd());
        if (soHd.isPresent()) {
            if (!soHd.get().getId().equals(req.getId())) throw new Exception("số hợp đồng đã tồn tại");
        }

        XhTlHopDongHdr data = optional.get();
        BeanUtils.copyProperties(req, data, "id", "maDvi");
        data.getHopDongDtl().forEach(f -> {
            f.setHopDongHdr(data);
        });

        XhTlHopDongHdr created = xhTlHopDongRepository.save(data);

        fileDinhKemService.delete(req.getId(), Lists.newArrayList(XhTlHopDongHdr.TABLE_NAME + "_CAN_CU"));
        if (!DataUtils.isNullOrEmpty(req.getFileCanCu())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileCanCu(), created.getId(), XhTlHopDongHdr.TABLE_NAME + "_CAN_CU");
        }

        fileDinhKemService.delete(req.getId(), Lists.newArrayList(XhTlHopDongHdr.TABLE_NAME));
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKem())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhTlHopDongHdr.TABLE_NAME);
        }
        return created;
    }

    public List<XhTlHopDongHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");

        List<XhTlHopDongHdr> list = xhTlHopDongRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) throw new Exception("Không tìm thấy dữ liệu");

        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();

        List<XhTlHopDongHdr> allById = xhTlHopDongRepository.findAllById(ids);
        allById.forEach(data -> {
            if (mapDmucDvi.get((data.getMaDvi())) != null) {
                data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
            }
            data.setMapVthh(mapVthh);
            data.setTenTrangThai(TrangThaiAllEnum.getLabelById(data.getTrangThai()));

            List<FileDinhKem> fileCanCu = fileDinhKemService.search(data.getId(), Arrays.asList(XhTlHopDongHdr.TABLE_NAME + "_CAN_CU"));
            data.setFileCanCu(fileCanCu);
            List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhTlHopDongHdr.TABLE_NAME));
            data.setFileDinhKem(fileDinhKem);

            data.getHopDongDtl().forEach(f -> {
                f.setMapDmucDvi(mapDmucDvi);
            });
        });
        return allById;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhTlHopDongHdr> optional = xhTlHopDongRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) throw new Exception("Bản ghi không tồn tại");
        XhTlHopDongHdr data = optional.get();
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhTlHopDongHdr.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhTlHopDongHdr.TABLE_NAME));
        xhTlHopDongRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhTlHopDongHdr> list = xhTlHopDongRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) throw new Exception("Bản ghi không tồn tại");
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhTlHopDongHdr.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhTlHopDongHdr.TABLE_NAME));
        xhTlHopDongRepository.deleteAll(list);
    }

    public XhTlHopDongHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

        if (StringUtils.isEmpty(statusReq.getId())) throw new Exception("Không tìm thấy dữ liệu");

        Optional<XhTlHopDongHdr> optional = xhTlHopDongRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu");

        String status = statusReq.getTrangThai() + optional.get().getTrangThai();
        switch (status) {
            case Contains.DAKY + Contains.DUTHAO:
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                optional.get().setNgayPduyet(LocalDate.now());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        XhTlHopDongHdr created = xhTlHopDongRepository.save(optional.get());
        return created;
    }

}
