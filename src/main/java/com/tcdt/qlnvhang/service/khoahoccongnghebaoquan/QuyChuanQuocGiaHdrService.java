package com.tcdt.qlnvhang.service.khoahoccongnghebaoquan;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.bandaugia.quyetdinhpheduyetkehoachbandaugia.BhQdPheDuyetKhbdg;
import com.tcdt.qlnvhang.entities.khcn.quychuankythuat.QuyChuanQuocGiaDtl;
import com.tcdt.qlnvhang.entities.khcn.quychuankythuat.QuyChuanQuocGiaHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.khoahoccongnghebaoquan.QuyChuanQuocGiaDtlRepository;
import com.tcdt.qlnvhang.repository.khoahoccongnghebaoquan.QuyChuanQuocGiaHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.khoahoccongnghebaoquan.QuyChuanQuocGiaDtlReq;
import com.tcdt.qlnvhang.request.khoahoccongnghebaoquan.QuyChuanQuocGiaHdrReq;
import com.tcdt.qlnvhang.request.khoahoccongnghebaoquan.SearchQuyChuanQgReq;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.response.khoahoccongnghebaoquan.KhCnBaoQuanPreviewRes;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.DmDonViDTO;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
//        UserInfo userInfo = SecurityContextService.getUser();
//        objReq.setMaDvi(!ObjectUtils.isEmpty(objReq.getMaDvi()) ? objReq.getMaDvi() : userInfo.getDvql());
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("loaiVthh", "maBn").ascending());
        Page<QuyChuanQuocGiaHdr> data = quyChuanQuocGiaHdrRepository.search(objReq, pageable);
//        List<QuyChuanQuocGiaHdr> dataFinal = data.getContent();
//        if (ObjectUtils.isEmpty(objReq.getTrangThaiHl())) {
//
//        }
        Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
        List<Long> idsHdr = data.getContent().stream().map(QuyChuanQuocGiaHdr::getId).collect(Collectors.toList());
        List<QuyChuanQuocGiaDtl> allByIdHdrIn = quyChuanQuocGiaDtlRepository.findAllByIdHdrIn(idsHdr);
        Map<String, String> listDanhMucDvi = getListDanhMucDvi("0", null, "01");
        Map<Long, List<QuyChuanQuocGiaDtl>> mapDtl = allByIdHdrIn.stream()
                .collect(Collectors.groupingBy(QuyChuanQuocGiaDtl::getIdHdr));
        Map<String, String> mapTenChiTieu = getListDanhMucChung("CHI_TIEU_CL");
        Map<String, String> mapTenNhomChiTieu = getListDanhMucChung("NHOM_CHI_TIEU_CL");
        Map<String, String> mapTenToanTu = getListDanhMucChung("TOAN_TU");
        data.getContent().forEach(f -> {
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
            f.setTenBn(listDanhMucDvi.get(f.getMaBn()));
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenTrangThaiHl(f.getTrangThaiHl().equals(Contains.CON_HIEU_LUC) ? "Còn hiệu lực" : (f.getTrangThaiHl().equals(Contains.HET_HIEU_LUC) ? "Hết hiệu lực" : "Chưa có hiệu lực"));
            if (!mapDtl.get(f.getId()).isEmpty() && ObjectUtils.isEmpty(objReq.getIsSearch())) {
                if (f.getApDungCloaiVthh() == false) {
                    for (QuyChuanQuocGiaDtl dtl : mapDtl.get(f.getId())) {
                        dtl.setTenLoaiVthh(ObjectUtils.isEmpty(dtl.getLoaiVthh()) ? null : hashMapDmHh.get(dtl.getLoaiVthh()));
                        dtl.setTenCloaiVthh(ObjectUtils.isEmpty(dtl.getCloaiVthh()) ? null : hashMapDmHh.get(dtl.getCloaiVthh()));
                        dtl.setTenChiTieu(mapTenChiTieu.get(dtl.getMaChiTieu()));
                        dtl.setTenNhomCtieu(mapTenNhomChiTieu.get(dtl.getNhomCtieu()));
                        dtl.setTenChiTieu(mapTenToanTu.get(dtl.getTenToanTu()));
                    }
                    f.setTieuChuanKyThuat(mapDtl.get(f.getId()));
                } else {
                    List<QuyChuanQuocGiaDtl> listQuyChuan = new ArrayList<>();
                    List<String> listTenChiTieu = mapDtl.get(f.getId()).stream().map(QuyChuanQuocGiaDtl::getTenChiTieu).collect(Collectors.toList());
                    if (!listTenChiTieu.isEmpty()) {
                        mapDtl.get(f.getId()).forEach(item -> {
                            item.setTenChiTieu(mapTenChiTieu.get(item.getMaChiTieu()));
                            List<String> listStringCompare = listQuyChuan.stream().map(QuyChuanQuocGiaDtl::getTenChiTieu).collect(Collectors.toList());
                            if (!listStringCompare.contains(item.getTenChiTieu())) {
                                item.setLoaiVthh(null);
                                item.setCloaiVthh(null);
                                listQuyChuan.add(item);
                            }
                        });
                    }
                    f.setTieuChuanKyThuat(listQuyChuan);
                }
            }
            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(f.getId(), Collections.singleton(QuyChuanQuocGiaHdr.TABLE_NAME));
            if (!CollectionUtils.isEmpty(fileDinhKems)) {
                fileDinhKems = fileDinhKems.stream().filter(item -> String.valueOf(item.getFileType()).equals("CAN_CU_PHAP_LY")).collect(Collectors.toList());
                f.setFileDinhKems(fileDinhKems);
            }
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
//        List<String> listCloai = quyChuanQuocGiaDtlRepository.findAllCloaiHoatDong(Contains.HOAT_DONG, null);
        List<QuyChuanQuocGiaHdr> allHdrCoHieuLuc = quyChuanQuocGiaHdrRepository.findAll().stream().filter(item -> item.getTrangThaiHl().equals("01")).collect(Collectors.toList());
        //loai bỏ những id được thay thế
        List<Long> listIdThayThe = new ArrayList<>();
        if (!ObjectUtils.isEmpty(objReq.getIdVanBanThayThe())) {
            listIdThayThe = Arrays.asList(Arrays.stream(objReq.getIdVanBanThayThe().split(","))
                    .map(String::trim)
                    .map(Long::valueOf)
                    .toArray(Long[]::new));
        }
        List<Long> finalListIdThayTheSd = listIdThayThe;
        if (!ObjectUtils.isEmpty(objReq.getIdVanBanSuaDoi())) {
            finalListIdThayTheSd.add(objReq.getIdVanBanSuaDoi());
        }
        List<Long> listHdrCoHieuLuc = allHdrCoHieuLuc.stream().map(QuyChuanQuocGiaHdr::getId).collect(Collectors.toList()).stream()
                .filter(item -> !finalListIdThayTheSd.contains(item))
                .collect(Collectors.toList());

        List<String> listCloai = quyChuanQuocGiaDtlRepository.findAllByIdHdrIn(listHdrCoHieuLuc).stream().map(QuyChuanQuocGiaDtl::getCloaiVthh).collect(Collectors.toList());
        List<String> listCloaiReq = objReq.getTieuChuanKyThuat().stream().map(QuyChuanQuocGiaDtlReq::getCloaiVthh).distinct().collect(Collectors.toList());
        listCloai.retainAll(listCloaiReq);
        if (!listCloai.isEmpty()) {
            throw new Exception("Có chủng loại hàng hóa đã được tạo tiêu chuẩn kỹ thuật ở bản ghi khác");
        }
        QuyChuanQuocGiaHdr data = new ModelMapper().map(objReq, QuyChuanQuocGiaHdr.class);
        data.setMaDvi(userInfo.getDvql());
        data.setTrangThai(Contains.DUTHAO);
        QuyChuanQuocGiaHdr created = quyChuanQuocGiaHdrRepository.save(data);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), data.getId(), "KHCN_QUY_CHUAN_QG_HDR");
        created.setFileDinhKems(fileDinhKems);
        this.saveCtiet(data, objReq);
        //Check bản ghi vừa thêm có hiệu lực và có văn bản thay thế ko , nếu có vb thay thế thì hết hiệu lực vb thay thế luôn
        if (created.getTrangThaiHl().equals("01") && !listIdThayThe.isEmpty() && listIdThayThe.size() > 0) {
            List<QuyChuanQuocGiaHdr> allByIdIn = quyChuanQuocGiaHdrRepository.findAllByIdIn(listIdThayThe);
            allByIdIn.forEach(item -> {
                item.setNgayHetHieuLuc(created.getNgayHieuLuc().minusDays(1));
                item.setTrangThaiHl(Contains.HET_HIEU_LUC);
            });
            quyChuanQuocGiaHdrRepository.saveAll(allByIdIn);
        }
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
        List<QuyChuanQuocGiaHdr> allHdrCoHieuLuc = quyChuanQuocGiaHdrRepository.findAll().stream().filter(item -> item.getTrangThaiHl().equals("01")).collect(Collectors.toList());
        //loai bỏ những id được thay thế
        List<Long> listIdThayThe = new ArrayList<>();
        if (!ObjectUtils.isEmpty(objReq.getIdVanBanThayThe())) {
            listIdThayThe = Arrays.asList(Arrays.stream(objReq.getIdVanBanThayThe().split(","))
                    .map(String::trim)
                    .map(Long::valueOf)
                    .toArray(Long[]::new));
        }
        List<Long> finalListIdThayTheSd = listIdThayThe;
        if (!ObjectUtils.isEmpty(objReq.getIdVanBanSuaDoi())) {
            finalListIdThayTheSd.add(objReq.getIdVanBanSuaDoi());
        }
        List<Long> listHdrCoHieuLuc = allHdrCoHieuLuc.stream().map(QuyChuanQuocGiaHdr::getId).collect(Collectors.toList()).stream()
                .filter(item -> !finalListIdThayTheSd.contains(item))
                .collect(Collectors.toList());

        List<String> listCloai = quyChuanQuocGiaDtlRepository.findAllByIdHdrIn(listHdrCoHieuLuc).stream().map(QuyChuanQuocGiaDtl::getCloaiVthh).collect(Collectors.toList());
        List<String> listCloaiReq = objReq.getTieuChuanKyThuat().stream().map(QuyChuanQuocGiaDtlReq::getCloaiVthh).distinct().collect(Collectors.toList());
        listCloai.retainAll(listCloaiReq);
        if (!listCloai.isEmpty() && objReq.getTrangThai().equals(TrangThaiAllEnum.DU_THAO.getId())) {
            throw new Exception("Có chủng loại hàng hóa đã được tạo tiêu chuẩn kỹ thuật ở bản ghi khác");
        }
        QuyChuanQuocGiaHdr data = optional.get();
        BeanUtils.copyProperties(objReq, data, "id", "maDvi");
        QuyChuanQuocGiaHdr created = quyChuanQuocGiaHdrRepository.save(data);
        List<Long> ids = new ArrayList<>();
        ids.add(created.getId());
        fileDinhKemService.deleteMultiple(ids, Lists.newArrayList("KHCN_QUY_CHUAN_QG_HDR"));
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), data.getId(), "KHCN_QUY_CHUAN_QG_HDR");
        created.setFileDinhKems(fileDinhKems);
        List<QuyChuanQuocGiaDtl> dtlList = quyChuanQuocGiaDtlRepository.findAllByIdHdr(data.getId());
        List<Long> idCtList = dtlList.stream().map(QuyChuanQuocGiaDtl::getId).collect(Collectors.toList());
        fileDinhKemService.deleteMultiple(idCtList, Lists.newArrayList(QuyChuanQuocGiaDtl.TABLE_NAME));
        quyChuanQuocGiaDtlRepository.deleteAll(dtlList);
        this.saveCtiet(data, objReq);
        //Check bản ghi vừa update có hiệu lực và có văn bản thay thế ko , nếu có vb thay thế thì hết hiệu lực vb thay thế luôn
        if (created.getTrangThaiHl().equals("01") && listIdThayThe.size() > 0) {
            List<QuyChuanQuocGiaHdr> allByIdIn = quyChuanQuocGiaHdrRepository.findAllByIdIn(listIdThayThe);
            allByIdIn.forEach(item -> {
                item.setNgayHetHieuLuc(created.getNgayHieuLuc().minusDays(1));
                item.setTrangThaiHl(Contains.HET_HIEU_LUC);
            });
            quyChuanQuocGiaHdrRepository.saveAll(allByIdIn);
        }
        return created;
    }

    public void saveCtiet(QuyChuanQuocGiaHdr data, QuyChuanQuocGiaHdrReq objReq) {
        for (QuyChuanQuocGiaDtlReq dtlReq : objReq.getTieuChuanKyThuat()) {
            QuyChuanQuocGiaDtl dtl = new ModelMapper().map(dtlReq, QuyChuanQuocGiaDtl.class);
            dtl.setId(null);
            dtl.setIdHdr(data.getId());
            QuyChuanQuocGiaDtl dtlSave = quyChuanQuocGiaDtlRepository.save(dtl);
            List<FileDinhKemReq> listFile = new ArrayList<>();
            if (!ObjectUtils.isEmpty(dtlReq.getFileDinhKem())) {
                listFile.add(dtlReq.getFileDinhKem());
                List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(listFile, dtlSave.getId(), QuyChuanQuocGiaDtl.TABLE_NAME);
                dtl.setFileDinhKem(fileDinhKems.get(0));
            }
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
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Collections.singleton(QuyChuanQuocGiaHdr.TABLE_NAME));
        data.setFileDinhKems(fileDinhKems);
        List<QuyChuanQuocGiaDtl> dtlList = quyChuanQuocGiaDtlRepository.findAllByIdHdr(data.getId());
        if (!dtlList.isEmpty()) {
            Map<String, String> mapTenChiTieu = getListDanhMucChung("CHI_TIEU_CL");
            Map<String, String> mapTenNhomChiTieu = getListDanhMucChung("NHOM_CHI_TIEU_CL");
            Map<String, String> mapTenToanTu = getListDanhMucChung("TOAN_TU");
            if (data.getApDungCloaiVthh() == false) {
                for (QuyChuanQuocGiaDtl dtl : dtlList) {
                    dtl.setTenLoaiVthh(ObjectUtils.isEmpty(dtl.getLoaiVthh()) ? null : hashMapDmHh.get(dtl.getLoaiVthh()));
                    dtl.setTenCloaiVthh(ObjectUtils.isEmpty(dtl.getCloaiVthh()) ? null : hashMapDmHh.get(dtl.getCloaiVthh()));
                    dtl.setTenChiTieu(mapTenChiTieu.get(dtl.getMaChiTieu()));
                    dtl.setTenNhomCtieu(mapTenNhomChiTieu.get(dtl.getNhomCtieu()));
                    dtl.setTenToanTu(mapTenToanTu.get(dtl.getToanTu()));
                    List<FileDinhKem> fileDinhKemCt = fileDinhKemService.search(dtl.getId(), Collections.singleton(QuyChuanQuocGiaDtl.TABLE_NAME));
                    if (!CollectionUtils.isEmpty(fileDinhKemCt)) {
                        dtl.setFileDinhKem(fileDinhKemCt.get(0));
                    }
                }
                data.setTieuChuanKyThuat(dtlList);
            } else {
                List<QuyChuanQuocGiaDtl> listQuyChuan = new ArrayList<>();
                List<String> listTenChiTieu = dtlList.stream().map(QuyChuanQuocGiaDtl::getTenChiTieu).collect(Collectors.toList());
                if (!listTenChiTieu.isEmpty()) {
                    dtlList.forEach(item -> {
                        List<FileDinhKem> fileDinhKemCt = fileDinhKemService.search(item.getId(), Collections.singleton(QuyChuanQuocGiaDtl.TABLE_NAME));
                        if (!CollectionUtils.isEmpty(fileDinhKemCt)) {
                            item.setFileDinhKem(fileDinhKemCt.get(0));
                        }
                        item.setTenChiTieu(mapTenChiTieu.get(item.getMaChiTieu()));
                        item.setTenNhomCtieu(mapTenNhomChiTieu.get(item.getNhomCtieu()));
                        item.setTenToanTu(mapTenToanTu.get(item.getToanTu()));
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
        List<Long> ids = new ArrayList<>();
        ids.add(data.getId());
        fileDinhKemService.deleteMultiple(ids, Collections.singleton(BhQdPheDuyetKhbdg.TABLE_NAME));
        List<QuyChuanQuocGiaDtl> dtlList = quyChuanQuocGiaDtlRepository.findAllByIdHdr(data.getId());
        List<Long> idCtList = dtlList.stream().map(QuyChuanQuocGiaDtl::getId).collect(Collectors.toList());
        fileDinhKemService.deleteMultiple(idCtList, Lists.newArrayList(QuyChuanQuocGiaDtl.TABLE_NAME));
        quyChuanQuocGiaDtlRepository.deleteAll(dtlList);
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

    @Transactional
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
        List<Long> listIdThayThe = new ArrayList<>();
        if (!ObjectUtils.isEmpty(created.getIdVanBanThayThe())) {
            listIdThayThe = Arrays.asList(Arrays.stream(created.getIdVanBanThayThe().split(","))
                    .map(String::trim)
                    .map(Long::valueOf)
                    .toArray(Long[]::new));
        }
        if (listIdThayThe.size() > 0 && created.getTrangThaiHl().equals("01")) {
            List<QuyChuanQuocGiaHdr> allByIdIn = quyChuanQuocGiaHdrRepository.findAllByIdIn(listIdThayThe);
            allByIdIn.forEach(item -> {
                item.setNgayHetHieuLuc(LocalDate.now().minusDays(1));
                item.setTrangThaiHl(Contains.HET_HIEU_LUC);
            });
            quyChuanQuocGiaHdrRepository.saveAll(allByIdIn);
        }
        return created;
    }


    public List<QuyChuanQuocGiaDtl> getAllQuyChuanByCloaiVthh(String loaiVthh) throws Exception {
        List<QuyChuanQuocGiaDtl> allQuyChuanByCloaiVthh = quyChuanQuocGiaDtlRepository.getAllQuyChuanByCloaiVthh(loaiVthh);
        List<QuyChuanQuocGiaDtl> listQuyChuanApDung = new ArrayList<>();
        if (!allQuyChuanByCloaiVthh.isEmpty()) {
            List<Long> idsHdr = allQuyChuanByCloaiVthh.stream().map(QuyChuanQuocGiaDtl::getIdHdr).collect(Collectors.toList());
            List<QuyChuanQuocGiaHdr> allHdrCoHieuLuc = quyChuanQuocGiaHdrRepository.findAllByIdIn(idsHdr).stream().filter(item -> item.getTrangThaiHl().equals("01")).collect(Collectors.toList());
            if (!allHdrCoHieuLuc.isEmpty()) {
                List<Long> idsHdrCoHieuLuc = allHdrCoHieuLuc.stream().map(QuyChuanQuocGiaHdr::getId).collect(Collectors.toList());
                String soHieu = allHdrCoHieuLuc.stream()
                        .map(QuyChuanQuocGiaHdr::getSoHieuQuyChuan)
                        .collect(Collectors.joining(", "));
                listQuyChuanApDung = quyChuanQuocGiaDtlRepository.getAllQuyChuanByCloaiVthhApDung(loaiVthh, idsHdrCoHieuLuc);
                listQuyChuanApDung.forEach(item -> item.setSoHieuQuyChuan(soHieu));
            }
        }
        return listQuyChuanApDung;
    }

    public ReportTemplateResponse preview(QuyChuanQuocGiaHdrReq req) throws Exception {
        Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
        List<String> listDvi = new ArrayList<>();
        listDvi.add(req.getMaBn());
        Map<String, DmDonViDTO> mapDmucDvi = getListDanhMucDviByMadviIn(listDvi, "01");
        ReportTemplate model = findByTenFile(req.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        KhCnBaoQuanPreviewRes previewRes = new KhCnBaoQuanPreviewRes();
        List<String> listVthh = Arrays.asList(req.getLoaiVthh().split(","));
        String tenVthh = "";
        if (!listVthh.isEmpty()) {
            for (int i = 0; i < listVthh.size(); i++) {
                tenVthh = tenVthh + (!ObjectUtils.isEmpty(hashMapDmHh.get(listVthh.get(i))) ? hashMapDmHh.get(listVthh.get(i)).toUpperCase() : "") + (listVthh.size() > (i + 1) ? ", " : "");
            }
        }
        previewRes.setTenLoaiVthh(tenVthh);
        previewRes.setTenDvi(ObjectUtils.isEmpty(req.getMaBn()) ? "" : mapDmucDvi.get(req.getMaBn()).getTenDvi().toUpperCase());
        previewRes.setNgayHieuLuc(!ObjectUtils.isEmpty(req.getNgayHieuLuc()) ? req.getNgayHieuLuc().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "");
        previewRes.setNgayHetHieuLuc(!ObjectUtils.isEmpty(req.getNgayHetHieuLuc()) ? req.getNgayHetHieuLuc().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "");
        List<QuyChuanQuocGiaDtlReq> dtlList = req.getTieuChuanKyThuat();
        if (!CollectionUtils.isEmpty(dtlList)) {
            for (int i = 0; i < dtlList.size(); i++) {
                dtlList.get(i).setStt(String.valueOf(i + 1));
                dtlList.get(i).setTenCloaiVthh(!ObjectUtils.isEmpty(dtlList.get(i).getCloaiVthh()) ? hashMapDmHh.get(dtlList.get(i).getCloaiVthh()) : "Toàn bộ");
            }
        }
        previewRes.setTieuChuanList(dtlList);
        return docxToPdfConverter.convertDocxToPdf(inputStream, previewRes);
    }


}
