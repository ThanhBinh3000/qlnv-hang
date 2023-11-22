package com.tcdt.qlnvhang.service.xuathang.xuatkhac.xuathangkhoidm;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtBckqKiemDinhMauRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatNhapKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.xuathangkhoidm.XhXkThXuatHangKdmDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.xuathangkhoidm.XhXkThXuatHangKdmRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.xuathangkhoidm.XhXkVtBckqXuatHangKdmRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattu.XhXkVtBckqKiemDinhMauRequest;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.xuathangkhoidm.XhXkVtBckqXuatHangKdmRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtBckqKiemDinhMau;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatNhapKho;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtQdGiaonvXhHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm.XhXkThXuatHangKdmDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm.XhXkThXuatHangKdmHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm.XhXkVtBckqXuatHangKdm;
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
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class XhXkVtBckqXuatHangKdmService extends BaseServiceImpl {


    @Autowired
    private XhXkVtBckqXuatHangKdmRepository xhXkVtBckqXuatHangKdmRepository;

    @Autowired
    private XhXkVtQdGiaonvXhRepository xhXkVtQdGiaonvXhRepository;


    @Autowired
    private XhXkThXuatHangKdmDtlRepository xhXkThXuatHangKdmDtlRepository;

    @Autowired
    private XhXkThXuatHangKdmRepository xhXkThXuatHangKdmRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<XhXkVtBckqXuatHangKdm> searchPage(CustomUserDetails currentUser, XhXkVtBckqXuatHangKdmRequest req) throws Exception {
        req.setDvql(ObjectUtils.isEmpty(req.getDvql()) ? currentUser.getDvql() : req.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<XhXkVtBckqXuatHangKdm> search = xhXkVtBckqXuatHangKdmRepository.search(req, pageable);
        List<XhXkVtBckqXuatHangKdm> listData = search.getContent();
        if (!listData.isEmpty()) {
            Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
            Map<String, String> mapVthh = getListDanhMucHangHoa();
            List<Long> idsBcKqKdm = listData.stream().map(XhXkVtBckqXuatHangKdm::getId).collect(Collectors.toList());
//            List<XhXkVtPhieuXuatNhapKho> allByIdBcKqkdMauIn = xhXkVtPhieuXuatNhapKhoRepository.findAllByIdBcKqkdMauIn(idsBcKqKdm);
//            Map<Long, List<XhXkVtPhieuXuatNhapKho>> mapPxk = allByIdBcKqkdMauIn.stream()
//                    .collect(groupingBy(XhXkVtPhieuXuatNhapKho::getIdBcKqkdMau));
            listData.forEach(s -> {
                s.setTenDvi(mapDmucDvi.get(s.getMaDvi()));
                s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
//                List<XhXkVtPhieuXuatNhapKho> listByBcId = mapPxk.get(s.getId());
//                if (!ObjectUtils.isEmpty(listByBcId)) {
//                    listByBcId.forEach(item -> {
//                        item.setMapDmucDvi(mapDmucDvi);
//                        item.setMapVthh(mapVthh);
//                    });
////                    s.setXhXkVtPhieuXuatNhapKho(listByBcId);
//                }
            });
        }
        return search;
    }

    @Transactional
    public XhXkVtBckqXuatHangKdm save(CustomUserDetails currentUser, XhXkVtBckqXuatHangKdmRequest objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<XhXkVtBckqXuatHangKdm> optional = xhXkVtBckqXuatHangKdmRepository.findBySoBaoCao(objReq.getSoBaoCao());
        if (optional.isPresent()) {
            throw new Exception("Số báo cáo đã tồn tại");
        }
        Optional<XhXkThXuatHangKdmHdr> hdrDanhSachTh = xhXkThXuatHangKdmRepository.findByMaDanhSach(objReq.getMaDsTh());
        if (!hdrDanhSachTh.isPresent()) {
            throw new Exception("Không tìm thấy danh sách hàng thuộc diện xuất khỏi danh mục.");
        }
        XhXkVtBckqXuatHangKdm data = new XhXkVtBckqXuatHangKdm();
        BeanUtils.copyProperties(objReq, data);
        data.setTrangThai(Contains.DANG_NHAP_DU_LIEU);
        XhXkVtBckqXuatHangKdm created = xhXkVtBckqXuatHangKdmRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtBckqXuatHangKdm.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
        //save lại dtl tổng hợp -- update số lương đã xuất và số bc,id bc
        objReq.getXhXkThXuatHangKdmDtl().forEach(item -> {
            item.setSoBcKqXhKdm(created.getSoBaoCao());
            item.setIdBcKqXhKdm(created.getId());
            item.setTongHopHdr(hdrDanhSachTh.get());
        });
        xhXkThXuatHangKdmDtlRepository.saveAll(objReq.getXhXkThXuatHangKdmDtl());
        return created;
    }

    @Transactional()
    public XhXkVtBckqXuatHangKdm update(CustomUserDetails currentUser, XhXkVtBckqXuatHangKdmRequest objReq) throws Exception {
        if (objReq.getId() == null) {
            throw new Exception("Bad request!");
        }
        Optional<XhXkVtBckqXuatHangKdm> optional = xhXkVtBckqXuatHangKdmRepository.findById(objReq.getId());
        if (!optional.isPresent()) throw new Exception("Không tồn tại bản ghi!");

        if (objReq.getSoBaoCao().contains("/") && !ObjectUtils.isEmpty(objReq.getSoBaoCao().split("/")[0])) {
            Optional<XhXkVtBckqXuatHangKdm> optionalBySoBb = xhXkVtBckqXuatHangKdmRepository.findBySoBaoCao(objReq.getSoBaoCao());
            if (optionalBySoBb.isPresent() && optionalBySoBb.get().getId() != objReq.getId()) {
                if (!optionalBySoBb.isPresent()) throw new Exception("Số báo cáo đã tồn tại!");
            }
        }
        XhXkVtBckqXuatHangKdm dx = optional.get();
        BeanUtils.copyProperties(objReq, dx);
        XhXkVtBckqXuatHangKdm created = xhXkVtBckqXuatHangKdmRepository.save(dx);
        fileDinhKemService.delete(dx.getId(), Collections.singleton(XhXkVtBckqXuatHangKdm.TABLE_NAME));
        //save file đính kèm
        fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkVtBckqXuatHangKdm.TABLE_NAME);
        return detail(created.getId());
    }


    @Transactional()
    public XhXkVtBckqXuatHangKdm detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
        Optional<XhXkVtBckqXuatHangKdm> optional = xhXkVtBckqXuatHangKdmRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhXkVtBckqXuatHangKdm model = optional.get();

        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkVtBckqXuatHangKdm.TABLE_NAME));
        model.setFileDinhKems(fileDinhKem);
        model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
        model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        model.setXhXkThXuatHangKdmDtl(xhXkThXuatHangKdmDtlRepository.findAllByIdBcKqXhKdm(model.getId()));
        model.getXhXkThXuatHangKdmDtl().forEach(s -> {
            s.setMapDmucDvi(mapDmucDvi);
            s.setMapVthh(mapVthh);
        });
        return model;
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<XhXkVtBckqXuatHangKdm> optional = xhXkVtBckqXuatHangKdmRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        if (optional.get().getTrangThai().equals(TrangThaiAllEnum.DANG_NHAP_DU_LIEU.getId()) || optional.get().getTrangThai().equals(TrangThaiAllEnum.TU_CHOI_LDC.getId())) {
            XhXkVtBckqXuatHangKdm data = optional.get();
            fileDinhKemService.deleteMultiple(Collections.singleton(data.getId()), Collections.singleton(XhXkVtBckqXuatHangKdm.TABLE_NAME));
            //update lại số bc = null vào detail ds tổng hợp
            Optional<XhXkThXuatHangKdmHdr> hdrDanhSachTh = xhXkThXuatHangKdmRepository.findByMaDanhSach(data.getMaDsTh());
            List<XhXkThXuatHangKdmDtl> allByIdBcKqXhKdm = xhXkThXuatHangKdmDtlRepository.findAllByIdBcKqXhKdm(idSearchReq.getId());
            if (!allByIdBcKqXhKdm.isEmpty()) {
                allByIdBcKqXhKdm.forEach(item -> {
                    item.setSoBcKqXhKdm(null);
                    item.setIdBcKqXhKdm(null);
                    item.setTongHopHdr(hdrDanhSachTh.get());
                });
            }
            xhXkVtBckqXuatHangKdmRepository.delete(data);
        } else {
            throw new Exception("Bản ghi đang ở trạng thái " + TrangThaiAllEnum.getLabelById(optional.get().getTrangThai()) + " không thể xóa.");
        }
    }

    @Transient
    public XhXkVtBckqXuatHangKdm approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhXkVtBckqXuatHangKdm> optional = xhXkVtBckqXuatHangKdmRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        String status = statusReq.getTrangThai() + optional.get().getTrangThai();
        switch (status) {
            case Contains.HOANTHANHCAPNHAT + Contains.DANG_NHAP_DU_LIEU:
                break;
            default:
                throw new Exception("Hoàn thành không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        XhXkVtBckqXuatHangKdm created = xhXkVtBckqXuatHangKdmRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, XhXkVtBckqXuatHangKdmRequest objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<XhXkVtBckqXuatHangKdm> page = this.searchPage(currentUser, objReq);
        List<XhXkVtBckqXuatHangKdm> data = page.getContent();

        String title = "Danh sách báo cáo kết quả xuất hàng ngoài danh mục";
        String[] rowsName = new String[]{"STT", "Năm báo cáo", "Đơn vị gửi báo cáo", "Số báo cáo", "Tên báo cáo", "Ngày báo cáo", "Danh sách hàng thuộc diện xuất khỏi danh mục", "Số QĐ xuất hàng khỏi danh mục",
                "Trạng thái"};
        String fileName = "danh-sach-bao-cao-ket-qua-xuat-hang-ngoai-danh-muc.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhXkVtBckqXuatHangKdm dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getNam();
            objs[2] = dx.getSoBaoCao();
            objs[3] = dx.getTenBaoCao();
            objs[4] = dx.getNgayBaoCao();
            objs[5] = dx.getMaDsTh();
            objs[6] = null;
            objs[7] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}

