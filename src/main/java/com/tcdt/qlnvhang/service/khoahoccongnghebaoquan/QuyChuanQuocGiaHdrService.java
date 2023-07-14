package com.tcdt.qlnvhang.service.khoahoccongnghebaoquan;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.khcn.quychuankythuat.QuyChuanQuocGiaDtl;
import com.tcdt.qlnvhang.entities.khcn.quychuankythuat.QuyChuanQuocGiaHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.khoahoccongnghebaoquan.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.khoahoccongnghebaoquan.*;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuyChuanQuocGiaHdrService extends BaseServiceImpl {


    @Autowired
    private QuyChuanQuocGiaHdrRepository quyChuanQuocGiaHdrRepository;

    @Autowired
    private QuyChuanQuocGiaDtlRepository quyChuanQuocGiaDtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<QuyChuanQuocGiaHdr> searchPage(SearchQuyChuanQgReq objReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
//        objReq.setMaDvi(userInfo.getDvql());
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<QuyChuanQuocGiaHdr> data = quyChuanQuocGiaHdrRepository.search(objReq, pageable);
        Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
        data.getContent().forEach(f -> {
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenTrangThaiHl(f.getTrangThaiHl().equals(Contains.CON_HIEU_LUC) ? "Còn hiệu lực" : "Hết hiệu lực");
        });

        return data;
    }

    @Transactional
    public QuyChuanQuocGiaHdr save(QuyChuanQuocGiaHdrReq objReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }
        Optional<QuyChuanQuocGiaHdr> optional = quyChuanQuocGiaHdrRepository.findAllBySoVanBan(objReq.getSoVanBan());
        if (optional.isPresent()) {
            throw new Exception("Số văn bản đã tồn tại");
        }
        if (objReq.getTieuChuanKyThuat().isEmpty()) {
            throw new Exception("Không tìm thấy thông tin tiêu chuẩn kỹ thuật");
        }
        List<String> listCloai = quyChuanQuocGiaDtlRepository.findAllCloaiHoatDong(Contains.HOAT_DONG, null);
        List<String> listCloaiReq = objReq.getTieuChuanKyThuat().stream().map(QuyChuanQuocGiaDtlReq::getCloaiVthh).distinct().collect(Collectors.toList());
        listCloai.retainAll(listCloaiReq);
        if (!listCloai.isEmpty()) {
            throw new Exception("Có chủng loại hàng hóa đã được tạo tiêu chuẩn kỹ thuật ở bản ghi khác");
        }
        QuyChuanQuocGiaHdr data = new ModelMapper().map(objReq, QuyChuanQuocGiaHdr.class);
        data.setMaDvi(userInfo.getDvql());
        data.setTrangThai(Contains.DUTHAO);
        if (DataUtils.isNullObject(objReq.getIdVanBanThayThe())) {
            data.setIdVanBanThayThe(data.getId());
        }
        QuyChuanQuocGiaHdr created = quyChuanQuocGiaHdrRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), data.getId(), "KHCN_QUY_CHUAN_QG_HDR");
        created.setFileDinhKems(fileDinhKems);
        this.saveCtiet(data, objReq);
        return created;
    }

    @Transactional
    public QuyChuanQuocGiaHdr update(QuyChuanQuocGiaHdrReq objReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }
        Optional<QuyChuanQuocGiaHdr> optional = quyChuanQuocGiaHdrRepository.findById(objReq.getId());
        Optional<QuyChuanQuocGiaHdr> soQd = quyChuanQuocGiaHdrRepository.findAllBySoVanBan(objReq.getSoVanBan());
        if (soQd.isPresent()) {
            if (!soQd.get().getId().equals(objReq.getId())) {
                throw new Exception("Số văn bản đã tồn tại");
            }
        }
        if (objReq.getTieuChuanKyThuat().isEmpty()) {
            throw new Exception("Không tìm thấy thông tin tiêu chuẩn kỹ thuật");
        }
        List<String> listCloai = quyChuanQuocGiaDtlRepository.findAllCloaiHoatDong(Contains.HOAT_DONG, objReq.getId());
        List<String> listCloaiReq = objReq.getTieuChuanKyThuat().stream().map(QuyChuanQuocGiaDtlReq::getCloaiVthh).distinct().collect(Collectors.toList());
        listCloai.retainAll(listCloaiReq);
        if (!listCloai.isEmpty()) {
            throw new Exception("Có chủng loại hàng hóa đã được tạo tiêu chuẩn kỹ thuật ở bản ghi khác");
        }
        QuyChuanQuocGiaHdr data = optional.get();
        BeanUtils.copyProperties(objReq, data, "id", "maDvi");
        QuyChuanQuocGiaHdr created = quyChuanQuocGiaHdrRepository.save(data);
        List<QuyChuanQuocGiaDtl> dtlList = quyChuanQuocGiaDtlRepository.findAllByIdHdr(data.getId());
        quyChuanQuocGiaDtlRepository.deleteAll(dtlList);
        this.saveCtiet(data, objReq);
        return created;
    }

    public void saveCtiet(QuyChuanQuocGiaHdr data, QuyChuanQuocGiaHdrReq objReq) {
        for (QuyChuanQuocGiaDtlReq dtlReq : objReq.getTieuChuanKyThuat()) {
            QuyChuanQuocGiaDtl dtl = new ModelMapper().map(dtlReq, QuyChuanQuocGiaDtl.class);
            dtl.setId(null);
            dtl.setIdHdr(data.getId());
            quyChuanQuocGiaDtlRepository.save(dtl);
        }
    }

    public QuyChuanQuocGiaHdr detail(String ids) throws Exception {
        Optional<QuyChuanQuocGiaHdr> optional = quyChuanQuocGiaHdrRepository.findById(Long.valueOf(ids));
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        QuyChuanQuocGiaHdr data = optional.get();
        Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        data.setTenDvi(StringUtils.isEmpty(data.getMaDvi()) ? null : hashMapDvi.get(data.getMaDvi()));
        data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh()) ? null : hashMapDmHh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh()) ? null : hashMapDmHh.get(data.getCloaiVthh()));
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Collections.singleton(QuyChuanQuocGiaHdr.TABLE_NAME));
        data.setFileDinhKems(fileDinhKems);
        List<QuyChuanQuocGiaDtl> dtlList = quyChuanQuocGiaDtlRepository.findAllByIdHdr(data.getId());
        if (!dtlList.isEmpty()) {
            if (data.isApDungCloaiVthh() == false) {
                for (QuyChuanQuocGiaDtl dtl : dtlList) {
                    dtl.setTenLoaiVthh(StringUtils.isEmpty(dtl.getLoaiVthh()) ? null : hashMapDmHh.get(dtl.getLoaiVthh()));
                    dtl.setTenCloaiVthh(StringUtils.isEmpty(dtl.getCloaiVthh()) ? null : hashMapDmHh.get(dtl.getCloaiVthh()));
                }
                data.setTieuChuanKyThuat(dtlList);
            } else {
                List<QuyChuanQuocGiaDtl> listQuyChuan = new ArrayList<>();
                List<String> listTenChiTieu = dtlList.stream().map(QuyChuanQuocGiaDtl::getTenChiTieu).collect(Collectors.toList());
                if (!listTenChiTieu.isEmpty()) {
                    dtlList.forEach(item -> {
                        List<String> listStringCompare = listQuyChuan.stream().map(QuyChuanQuocGiaDtl::getTenChiTieu).collect(Collectors.toList());
                        if (!listStringCompare.contains(item.getTenChiTieu())) {
                            item.setLoaiVthh(null);
                            item.setCloaiVthh(null);
                            listQuyChuan.add(item);
                        }
                    });
                }
                data.setTieuChuanKyThuat(listQuyChuan);
            }
        }
        return data;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<QuyChuanQuocGiaHdr> optional = quyChuanQuocGiaHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        QuyChuanQuocGiaHdr data = optional.get();
        List<QuyChuanQuocGiaDtl> dtlList = quyChuanQuocGiaDtlRepository.findAllByIdHdr(data.getId());
        quyChuanQuocGiaDtlRepository.deleteAll(dtlList);
        fileDinhKemService.delete(data.getId(), Lists.newArrayList("KHCN_QUY_CHUAN_QG_HDR"));
        quyChuanQuocGiaHdrRepository.delete(data);

    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<QuyChuanQuocGiaHdr> list = quyChuanQuocGiaHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listIdHdr = list.stream().map(QuyChuanQuocGiaHdr::getId).collect(Collectors.toList());
        List<QuyChuanQuocGiaDtl> dtlList = quyChuanQuocGiaDtlRepository.findAllByIdHdrIn(listIdHdr);
        quyChuanQuocGiaDtlRepository.deleteAll(dtlList);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList("HH_QD_GIAO_NV_NHAP_HANG"));
        quyChuanQuocGiaHdrRepository.deleteAll(list);

    }

    public void export(SearchQuyChuanQgReq objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<QuyChuanQuocGiaHdr> page = this.searchPage(objReq);
        List<QuyChuanQuocGiaHdr> data = page.getContent();

        String title = "Danh sách quy chuẩn kỹ thuật quốc gia";
        String[] rowsName = new String[]{"STT", "Số văn bản", "Số hiệu quy chuẩn,tiêu chuẩn văn bản", "Áp dụng tại", "Loại hàng hóa", "Ngày quyết dịnh", "Ngày hiệu lực", "Trạng thái", "Hiệu lực"};
        String fileName = "danh-sach-quy-chuan-ky-thuat-quoc-gia.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            QuyChuanQuocGiaHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getSoVanBan();
            objs[2] = dx.getSoHieuQuyChuan();
            objs[3] = dx.getApDungTai();
            objs[4] = dx.getListTenLoaiVthh();
            objs[5] = dx.getNgayKy();
            objs[6] = dx.getNgayHieuLuc();
            objs[7] = dx.getTenTrangThai();
            objs[8] = dx.getTrangThaiHl();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public QuyChuanQuocGiaHdr approve(StatusReq statusReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<QuyChuanQuocGiaHdr> optional = quyChuanQuocGiaHdrRepository.findById(Long.valueOf(statusReq.getId()));
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }

        String status = statusReq.getTrangThai() + optional.get().getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDV + Contains.DUTHAO:
            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
                optional.get().setNguoiGduyetId(userInfo.getId());
                optional.get().setNgayGduyet(getDateTimeNow());
                break;
            case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
                optional.get().setNguoiPduyetId(userInfo.getId());
                optional.get().setNgayPduyet(getDateTimeNow());
                optional.get().setLdoTuChoi(statusReq.getLyDo());
                break;
            case Contains.BAN_HANH + Contains.CHODUYET_LDV:
                optional.get().setNguoiPduyetId(userInfo.getId());
                optional.get().setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        QuyChuanQuocGiaHdr created = quyChuanQuocGiaHdrRepository.save(optional.get());
        if (created.getIdVanBanThayThe() != null && created.getTrangThai().equals(Contains.BAN_HANH)) {
            quyChuanQuocGiaHdrRepository.findById(created.getIdVanBanThayThe())
                    .ifPresent(vanBanThayThe -> {
                        vanBanThayThe.setNgayHetHieuLuc(LocalDate.now());
                        vanBanThayThe.setTrangThaiHl(Contains.HET_HIEU_LUC);
                        quyChuanQuocGiaHdrRepository.save(vanBanThayThe);
                    });
        }
        return created;
    }


    public List<QuyChuanQuocGiaDtl> getAllQuyChuanByCloaiVthh(String loaiVthh) throws Exception {
        return quyChuanQuocGiaHdrRepository.getAllQuyChuanByCloaiVthh(loaiVthh).isEmpty() ? new ArrayList<>() : quyChuanQuocGiaHdrRepository.getAllQuyChuanByCloaiVthh(loaiVthh);
    }
}
