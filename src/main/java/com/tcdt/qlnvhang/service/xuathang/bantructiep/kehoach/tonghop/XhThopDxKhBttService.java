package com.tcdt.qlnvhang.service.xuathang.bantructiep.kehoach.tonghop;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.tonghop.SearchXhThopDxKhBtt;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttHdrReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.XhThopChiTieuReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhThopDxKhBttService extends BaseServiceImpl {


    @Autowired
    private XhThopDxKhBttRepository xhThopDxKhBttRepository;
    @Autowired
    private XhDxKhBanTrucTiepHdrRepository xhDxKhBanTrucTiepHdrRepository;

    public Page<XhThopDxKhBttHdr> searchPage(CustomUserDetails currentUser, SearchXhThopDxKhBtt req) throws Exception {
        req.setDvql(currentUser.getDvql());
        if (!DataUtils.isNullObject(req.getNgayThopTu())) {
            req.setNgayThopTu(req.getNgayThopTu().toLocalDate().atTime(LocalTime.MAX));
        }
        if (!DataUtils.isNullObject(req.getNgayThopDen())) {
            req.setNgayThopDen(req.getNgayThopDen().toLocalDate().atTime(LocalTime.MIN));
        }
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhThopDxKhBttHdr> search = xhThopDxKhBttRepository.searchPage(req, pageable);
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        search.getContent().forEach(data -> {
            data.setMapVthh(mapVthh);
            data.setTrangThai(data.getTrangThai());
        });
        return search;
    }

    public XhThopDxKhBttHdr sumarryData(CustomUserDetails currentUser, XhThopChiTieuReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        List<XhDxKhBanTrucTiepHdr> dxuatList = xhDxKhBanTrucTiepHdrRepository.listTongHop(req);
        if (dxuatList.isEmpty()) throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
        XhThopDxKhBttHdr thopHdr = new XhThopDxKhBttHdr();
        thopHdr.setId(getNextSequence("XH_THOP_DX_KH_BTT_HDR_SEQ"));
        List<XhThopDxKhBttDtl> thopDtls = new ArrayList<>();
        Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
        for (XhDxKhBanTrucTiepHdr dxuat : dxuatList) {
            XhThopDxKhBttDtl thopDtl = new XhThopDxKhBttDtl();
            BeanUtils.copyProperties(dxuat, thopDtl, "id");
            thopDtl.setMaDvi(dxuat.getMaDvi());
            if (mapDmucDvi.containsKey((thopDtl.getMaDvi()))) {
                Map<String, Object> objDonVi = mapDmucDvi.get(thopDtl.getMaDvi());
                thopDtl.setTenDvi(objDonVi.get("tenDvi").toString());
            }
            thopDtl.setIdDxHdr(dxuat.getId());
            thopDtl.setSoDxuat(dxuat.getSoDxuat());
            thopDtl.setNgayPduyet(dxuat.getNgayPduyet());
            thopDtl.setTrichYeu(dxuat.getTrichYeu());
            thopDtl.setSlDviTsan(dxuat.getSlDviTsan());
            thopDtl.setTongSoLuong(dxuat.getTongSoLuong());
            thopDtl.setDonGia(dxuat.getDonGia());
            thopDtl.setThanhTien(dxuat.getThanhTien());
            thopDtl.setTrangThai(dxuat.getTrangThai());
            thopDtls.add(thopDtl);
        }
        thopHdr.setChildren(thopDtls);
        return thopHdr;
    }

    @Transactional
    public XhThopDxKhBttHdr create(CustomUserDetails currentUser, XhThopDxKhBttHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        if (!DataUtils.isNullObject(req.getIdTh())) {
            Optional<XhThopDxKhBttHdr> optional = xhThopDxKhBttRepository.findById(req.getIdTh());
            if (optional.isPresent()) throw new Exception(" Mã tổng hợp " + req.getIdTh() + " đã tồn tại ");
        }
        XhThopDxKhBttHdr data = new XhThopDxKhBttHdr();
        BeanUtils.copyProperties(req, data);
        data.setId(req.getIdTh());
        data.setMaDvi(currentUser.getUser().getDepartment());
        data.setTrangThai(Contains.CHUATAO_QD);
        XhThopDxKhBttHdr created = xhThopDxKhBttRepository.save(data);


        if (data.getId() > 0 && data.getChildren().size() > 0) {
            List<String> soDxuatList = data.getChildren().stream().map(XhThopDxKhBttDtl::getSoDxuat)
                    .collect(Collectors.toList());
            xhDxKhBanTrucTiepHdrRepository.updateStatusInList(soDxuatList, Contains.DATONGHOP, data.getId());
        }
        return created;
    }

    @Transactional
    public XhThopDxKhBttHdr update(CustomUserDetails currentUser, XhThopDxKhBttHdrReq req) throws Exception {
        if (currentUser == null) throw new Exception("Bad request.");
        Optional<XhThopDxKhBttHdr> optional = xhThopDxKhBttRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");
        XhThopDxKhBttHdr data = optional.get();
        BeanUtils.copyProperties(req, data, "id", "maDvi");
        XhThopDxKhBttHdr updated = xhThopDxKhBttRepository.save(data);
        return updated;
    }

    public List<XhThopDxKhBttHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
        List<XhThopDxKhBttHdr> list = xhThopDxKhBttRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(list)) throw new Exception("Không tìm thấy dữ liệu");
        Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        List<XhThopDxKhBttHdr> allById = xhThopDxKhBttRepository.findAllById(ids);
        allById.forEach(data -> {
            data.getChildren().forEach(dataDtl -> {
                if (mapDmucDvi.containsKey((dataDtl.getMaDvi()))) {
                    Map<String, Object> objDonVi = mapDmucDvi.get(dataDtl.getMaDvi());
                    dataDtl.setTenDvi(objDonVi.get("tenDvi").toString());
                }
                dataDtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dataDtl.getTrangThai()));
            });
            data.setMapVthh(mapVthh);
            data.setTrangThai(data.getTrangThai());
        });
        return allById;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhThopDxKhBttHdr> optional = xhThopDxKhBttRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) throw new Exception("Bản ghi không tồn tại");
        XhThopDxKhBttHdr data = optional.get();
        if (!data.getTrangThai().equals(Contains.CHUATAO_QD)) {
            throw new Exception("Chỉ thực hiện xóa tổng hợp ở trạng thái chưa tạo quyết định");
        }
        List<XhDxKhBanTrucTiepHdr> listDx = xhDxKhBanTrucTiepHdrRepository.findAllByIdThop(data.getId());
        listDx.forEach(dataDx -> {
            dataDx.setIdThop(null);
            dataDx.setTrangThaiTh(Contains.CHUATONGHOP);
        });
        xhDxKhBanTrucTiepHdrRepository.saveAll(listDx);
        xhThopDxKhBttRepository.delete(data);
    }

    @Transactional
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<XhThopDxKhBttHdr> list = xhThopDxKhBttRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) throw new Exception("Bản ghi không tồn tại");
        for (XhThopDxKhBttHdr hdr : list) {
            if (!hdr.getTrangThai().equals(Contains.CHUATAO_QD)) {
                throw new Exception("Chỉ thực hiện xóa tổng hợp ở trạng thái chưa tạo quyết định");
            }
        }
        List<Long> listIdThop = list.stream().map(XhThopDxKhBttHdr::getId).collect(Collectors.toList());
        List<XhDxKhBanTrucTiepHdr> listDx = xhDxKhBanTrucTiepHdrRepository.findByIdThopIn(listIdThop);
        listDx.forEach(dataDx -> {
            dataDx.setIdThop(null);
            dataDx.setTrangThaiTh(Contains.CHUATONGHOP);
        });
        xhDxKhBanTrucTiepHdrRepository.saveAll(listDx);
        xhThopDxKhBttRepository.deleteAll(list);
    }

    public void export(CustomUserDetails currentUser, SearchXhThopDxKhBtt req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhThopDxKhBttHdr> page = this.searchPage(currentUser, req);
        List<XhThopDxKhBttHdr> data = page.getContent();
        String title = "Danh sách thông tin tổng hợp kế hoạch bán trực tiếp";
        String[] rowsName = new String[]{"STT", "Mã tổng hợp", "Ngày tổng hợp", "Nội dung tổng hợp", "Năm kế hoạch", "Số QĐ phê duyệt KH bán trực tiếp", "Loại hàng hóa", "Trạng thái"};
        String fileName = "Thong_tin_tong_hop_ke_hoach_ban_truc_tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhThopDxKhBttHdr thop = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = thop.getId();
            objs[2] = thop.getNgayThop();
            objs[3] = thop.getNoiDungThop();
            objs[4] = thop.getNamKh();
            objs[5] = thop.getSoQdPd();
            objs[6] = thop.getTenLoaiVthh();
            objs[7] = thop.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}
