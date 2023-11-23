package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.*;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.hopdong.hopdongphuluc.HopDongMttHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhaptructiep.HhQdPduyetKqcgPreview;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdr;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class HhQdPduyetKqcgService extends BaseServiceImpl {

    @Autowired
    HhQdPduyetKqcgRepository hhQdPduyetKqcgRepository;

    @Autowired
    HhQdPheduyetKhMttHdrRepository hhQdPheduyetKhMttHdrRepository;

    @Autowired
    HhQdPheduyetKhMttDxRepository hhQdPheduyetKhMttDxRepository;
    @Autowired
    private HhCtietKqTtinCgiaRepository hhCtietKqTtinCgiaRepository;
    @Autowired
    private HhQdPdKqMttSlddDtlRepository hhQdPdKqMttSlddDtlRepository;
    @Autowired
    private HhQdPheduyetKqMttSLDDRepository hhQdPheduyetKqMttSLDDRepository;

    @Autowired
    HopDongMttHdrRepository hopDongMttHdrRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    public Page<HhQdPduyetKqcgHdr> searchPage(SearchHhQdPduyetKqcg req ) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<HhQdPduyetKqcgHdr> data = hhQdPduyetKqcgRepository.searchPage(
                req,
                pageable
        );
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        data.getContent().forEach(f->{
            List<HopDongMttHdr> listHd = hopDongMttHdrRepository.findAllByIdQdKq(f.getId());
            List<HopDongMttHdr> listHdDaKy = hopDongMttHdrRepository.findAllByIdQdKqAndTrangThai(f.getId(), Contains.DAKY);
            f.setSlHd(listHd.size());
            f.setSlHdDaKy(listHdDaKy.size());
            BigDecimal tongGtriHd = BigDecimal.ZERO;
            for (HopDongMttHdr hopDongMttHdr : listHd) {
//                tongGtriHd.add(hopDongMttHdr.getSoLuong().multiply(hopDongMttHdr.getDonGiaGomThue()).multiply(BigDecimal.valueOf(1000)));
                BigDecimal product = hopDongMttHdr.getSoLuong().multiply(hopDongMttHdr.getDonGiaGomThue()).multiply(new BigDecimal("1000"));
                tongGtriHd = tongGtriHd.add(product);
            }
            f.setTongGtriHd(tongGtriHd);
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenTrangThaiHd(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiHd()));
            f.setTenTrangThaiNh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiNh()));
            f.setTenDvi(hashMapDvi.get(f.getMaDvi()));
            f.setTenLoaiVthh(hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(hashMapVthh.get(f.getCloaiVthh()));
        });
        return data;
    }

    public HhQdPduyetKqcgHdr create (HhQdPduyetKqcgHdrReq req) throws Exception{

        if (!StringUtils.isEmpty(req.getSoQd())){
            Optional<HhQdPduyetKqcgHdr> qOptional = hhQdPduyetKqcgRepository.findBySoQdKq(req.getSoQd());
            if (qOptional.isPresent()){
                throw new Exception("Số quyết định " + req.getSoQdKq() + " kết quả chào giá đã tồn tại");
            }
        }
        HhQdPduyetKqcgHdr data = new HhQdPduyetKqcgHdr();
        BeanUtils.copyProperties(req, data, "id");
        data.setNamKh(LocalDate.now().getYear());
        data.setNgayTao(new Date());
        data.setNguoiTaoId(getUser().getId());
        data.setMaDvi(getUser().getDvql());
        data.setTrangThai(Contains.DUTHAO);
        data.setTrangThaiHd(NhapXuatHangTrangThaiEnum.CHUA_THUC_HIEN.getId());
        data.setTrangThaiNh(NhapXuatHangTrangThaiEnum.CHUA_THUC_HIEN.getId());

        Optional<HhQdPheduyetKhMttDx> dx = hhQdPheduyetKhMttDxRepository.findById(req.getIdPdKhDtl());
        if (dx.isPresent()){
            dx.get().setSoQdKq(req.getSoQdKq());
            hhQdPheduyetKhMttDxRepository.save(dx.get());
        }

        HhQdPduyetKqcgHdr created = hhQdPduyetKqcgRepository.save(data);
        if (!DataUtils.isNullObject(req.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Collections.singletonList(req.getFileDinhKem()), created.getId(), HhQdPduyetKqcgHdr.TABLE_NAME);
            created.setFileDinhKem(fileDinhKem.get(0));
        }
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), HhQdPduyetKqcgHdr.TABLE_NAME);
            created.setFileDinhKems(fileDinhKems);
        }
        saveDetail(created);
        return created;
    }
    @Transactional
    public  HhQdPduyetKqcgHdr update (HhQdPduyetKqcgHdrReq req) throws Exception{
        if(ObjectUtils.isEmpty(req.getId())){
            throw new Exception("Không tìn thấy dữ liệu cần sửa");
        }
        Optional<HhQdPduyetKqcgHdr> qOptional = hhQdPduyetKqcgRepository.findById(req.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }

        HhQdPduyetKqcgHdr data = qOptional.get();
        BeanUtils.copyProperties(req, data, "id");
        data.setNgaySua(new Date());
        data.setNguoiSuaId(getUser().getId());
        HhQdPduyetKqcgHdr created = hhQdPduyetKqcgRepository.save(data);

        if (!DataUtils.isNullObject(req.getFileDinhKem())){
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Arrays.asList(req.getFileDinhKem()), created.getId(), HhQdPduyetKqcgHdr.TABLE_NAME);
            data.setFileDinhKem(fileDinhKem.get(0));
        }

        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), HhQdPduyetKqcgHdr.TABLE_NAME);
            data.setFileDinhKems(fileDinhKems);
        }

//        for (HhQdPheduyetKqMttSLDDReq hhQdPheduyetKqMttSLDDReq : req.getDanhSachCtiet()) {
//            hhQdPheduyetKqMttSLDDRepository.deleteByIdQdPdKq(hhQdPheduyetKqMttSLDDReq.getIdQdPdKq());
//            HhQdPheduyetKqMttSLDD sldd = new HhQdPheduyetKqMttSLDD();
//            BeanUtils.copyProperties(hhQdPheduyetKqMttSLDDReq, sldd, "id");
//            sldd.setId(null);
//            hhQdPheduyetKqMttSLDDRepository.save(sldd);
//            for (HhChiTietKqTTinChaoGiaReq child : hhQdPheduyetKqMttSLDDReq.getListChaoGia()) {
//                HhChiTietKqTTinChaoGia chaoGia = new HhChiTietKqTTinChaoGia();
//                BeanUtils.copyProperties(child, chaoGia, "id");
//                chaoGia.setId(null);
//                HhChiTietKqTTinChaoGia save = hhCtietKqTtinCgiaRepository.save(chaoGia);
//                if (!DataUtils.isNullObject(child.getFileDinhKems())) {
//                    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(Collections.singletonList(child.getFileDinhKems()), save.getId(), HhChiTietTTinChaoGia.TABLE_NAME);
//                    chaoGia.setFileDinhKems(fileDinhKems.get(0));
//                }
//            }
//        }
            saveDetail(created);
        return created;
    }

    public void saveDetail(HhQdPduyetKqcgHdr req){
        hhQdPheduyetKqMttSLDDRepository.deleteByIdQdPdKq(req.getId());
        for (HhQdPheduyetKqMttSLDD hhQdPheduyetKqMttSLDDReq :  req.getDanhSachCtiet()) {
            HhQdPheduyetKqMttSLDD sldd = new HhQdPheduyetKqMttSLDD();
            BeanUtils.copyProperties(hhQdPheduyetKqMttSLDDReq, sldd, "id");
            sldd.setId(null);
            sldd.setIdQdPdKq(req.getId());
            HhQdPheduyetKqMttSLDD data = hhQdPheduyetKqMttSLDDRepository.save(sldd);
            hhQdPdKqMttSlddDtlRepository.deleteAllByIdDiaDiem(hhQdPheduyetKqMttSLDDReq.getId());
            for (HhQdPdKQMttSlddDtl child : hhQdPheduyetKqMttSLDDReq.getChildren()) {
                HhQdPdKQMttSlddDtl dtl = new HhQdPdKQMttSlddDtl();
                BeanUtils.copyProperties(child, dtl, "id");
                dtl.setId(null);
                dtl.setIdDiaDiem(data.getId());
                hhQdPdKqMttSlddDtlRepository.save(dtl);
            }
            hopDongMttHdrRepository.deleteAllByIdQdPdSldd(hhQdPheduyetKqMttSLDDReq.getId());
//            for (HopDongMttHdr list : hhQdPheduyetKqMttSLDDReq.getListHdong()) {
//                HopDongMttHdr hopDong = new HopDongMttHdr();
//                BeanUtils.copyProperties(list, hopDong, "id");
//                hopDong.setId(null);
//                hopDong.setIdQdPdSldd(data.getId());
//                HopDongMttHdr save = hopDongMttHdrRepository.save(hopDong);
//                if (!DataUtils.isNullObject(list.getFileDinhKems())) {
//                    List<FileDinhKemReq> fileDinhKemReq = new ArrayList<>((Collection) list.getFileDinhKems());
//                    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(fileDinhKemReq, save.getId(), HopDongMttHdr.TABLE_NAME);
//                    hopDong.setFileDinhKems(fileDinhKems);
//                }
//            }
            hhCtietKqTtinCgiaRepository.deleteAllByIdQdPdKqSldd(hhQdPheduyetKqMttSLDDReq.getId());
            for (HhChiTietKqTTinChaoGia child : hhQdPheduyetKqMttSLDDReq.getListChaoGia()) {
                HhChiTietKqTTinChaoGia chaoGia = new HhChiTietKqTTinChaoGia();
                BeanUtils.copyProperties(child, chaoGia, "id");
                chaoGia.setId(null);
                chaoGia.setIdQdPdKqSldd(data.getId());
                chaoGia.setSigned(false);
                HhChiTietKqTTinChaoGia save = hhCtietKqTtinCgiaRepository.save(chaoGia);
                if (!DataUtils.isNullObject(child.getFileDinhKems())) {
                    FileDinhKemReq file = new FileDinhKemReq();
                    BeanUtils.copyProperties(child.getFileDinhKems(), file, "id");
                    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(Collections.singletonList(file), save.getId(), HhChiTietTTinChaoGia.TABLE_NAME);
                    chaoGia.setFileDinhKems(fileDinhKems.get(0));
                }
                if(save.getLuaChon()){
                    HopDongMttHdr hopDong = new HopDongMttHdr();
                    hopDong.setIdQdPdSldd(data.getId());
                    hopDong.setDviCungCap(save.getCanhanTochuc());
                    hopDong.setIdKqCgia(save.getId());
                    hopDong.setSoLuong(save.getSoLuong());
                    hopDong.setDonGiaGomThue(save.getDonGia());
                    hopDong.setThanhTien(save.getThanhTien());
                    hopDong.setTrangThai(Contains.DUTHAO);
                    hopDong.setIdQdKq(req.getId());
                    hopDong.setSoQdKq(req.getSoQdKq());
                    hopDongMttHdrRepository.save(hopDong);
                }
            }
        }
    }

    public HhQdPduyetKqcgHdr detail(String ids) throws Exception{
        if (ObjectUtils.isEmpty(ids)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }

        Optional<HhQdPduyetKqcgHdr> qOptional = hhQdPduyetKqcgRepository.findById(Long.parseLong(ids));
        if (!qOptional.isPresent()){
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        HhQdPduyetKqcgHdr data = qOptional.get();
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
//        List<HopDongMttHdr> listHd = hopDongMttHdrRepository.findAllByIdQdKq(data.getId());
//        List<HopDongMttHdr> listHdDaKy = hopDongMttHdrRepository.findAllByIdQdGiaoNvNhAndTrangThai(f.getId(), Contains.DAKY);
//        f.setSlHd(listHd.size());
//        f.setSlHdDaKy(listHdDaKy.size());
        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenTrangThaiHd(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiHd()));
        data.setTenTrangThaiNh(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiNh()));
        data.setTenDvi(hashMapDvi.get(data.getMaDvi()));
        data.setTenLoaiVthh(hashMapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(hashMapVthh.get(data.getCloaiVthh()));
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(HhQdPduyetKqcgHdr.TABLE_NAME));
        if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
            data.setFileDinhKem(fileDinhKem.get(0));
        }
        data.setFileDinhKems(fileDinhKem);
        List<HhQdPheduyetKqMttSLDD> listGthau = hhQdPheduyetKqMttSLDDRepository.findAllByIdQdPdKq(data.getId());
        for (HhQdPheduyetKqMttSLDD hhQdPheduyetKqMttSLDD : listGthau) {
            hhQdPheduyetKqMttSLDD.setTenDvi(hashMapDvi.get(hhQdPheduyetKqMttSLDD.getMaDvi()));
            List<HhQdPdKQMttSlddDtl> hhQdPdKQMttSlddDtls = new ArrayList<>();
            for (HhQdPdKQMttSlddDtl hhQdPdKQMttSlddDtl : hhQdPdKqMttSlddDtlRepository.findAllByIdDiaDiem(hhQdPheduyetKqMttSLDD.getId())) {
                hhQdPdKQMttSlddDtl.setTenDiemKho(hashMapDvi.get(hhQdPdKQMttSlddDtl.getMaDiemKho()));
                hhQdPdKQMttSlddDtls.add(hhQdPdKQMttSlddDtl);
            }
            List<HopDongMttHdr> allById = hopDongMttHdrRepository.findAllByIdQdPdSldd(hhQdPheduyetKqMttSLDD.getId());
            allById.forEach(f ->{
                f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
                f.setTenTrangThaiNh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiNh()));
            });
            List<HhChiTietKqTTinChaoGia> chaoGia = hhCtietKqTtinCgiaRepository.findAllByIdQdPdKqSldd(hhQdPheduyetKqMttSLDD.getId());
            List<HopDongMttHdr> listHopDong = hopDongMttHdrRepository.findAllByIdQdPdSldd(hhQdPheduyetKqMttSLDD.getId());
            hhQdPheduyetKqMttSLDD.setListChaoGia(chaoGia);
            hhQdPheduyetKqMttSLDD.setListHdong(allById);
            hhQdPheduyetKqMttSLDD.setChildren(hhQdPdKQMttSlddDtls);
            hhQdPheduyetKqMttSLDD.setListHdong(listHopDong);
        }
        data.setDanhSachCtiet(listGthau);
        return data;
    }

    @Transactional(rollbackOn =  Exception.class)
    public HhQdPduyetKqcgHdr approve (StatusReq req) throws Exception{

        UserInfo userInfo= SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }


        Optional<HhQdPduyetKqcgHdr> optional = hhQdPduyetKqcgRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        HhQdPduyetKqcgHdr data = optional.get();

        String status = req.getTrangThai() + data.getTrangThai();
        if(req.getTrangThai().equals(NhapXuatHangTrangThaiEnum.DA_HOAN_THANH.getId())
                && data.getTrangThaiHd().equals(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId()))
        {
            data.setTrangThaiHd(req.getTrangThai());
        } else {
            switch (status) {
                case Contains.CHODUYET_TP + Contains.DUTHAO:
                case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
                case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                    data.setNguoiGuiDuyetId(userInfo.getId());
                    data.setNgayGuiDuyet(getDateTimeNow());
                case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
                case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                    data.setNguoiPduyetId(userInfo.getId());
                    data.setNgayPduyet(getDateTimeNow());
                    data.setLyDoTuChoi(req.getLyDoTuChoi());
                    break;
                case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
                case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                    data.setNguoiPduyetId(userInfo.getId());
                    data.setNgayPduyet(getDateTimeNow());
                    break;
                default:
                    throw new Exception("Phê duyệt không thành công");
            }
            data.setTrangThai(req.getTrangThai());
        }
        return hhQdPduyetKqcgRepository.save(data);
    }

//    void updateDataApprove(Optional<HhQdPduyetKqcgHdr> optional) throws Exception {
//        Optional<HhQdPheduyetKhMttDx> byId = hhQdPheduyetKhMttDxRepository.findById(optional.get().getIdQdPdKhDtl());
//        if (byId.isPresent()){
//            HhQdPheduyetKhMttDx hhQdPheduyetKhMttDx = byId.get();
//            if (!StringUtils.isEmpty(hhQdPheduyetKhMttDx.getSoQdPdKqMtt())){
//                throw new Exception(
//                        "Thông tin kế hoạch mua trực tiếp đã được bạn hành quyết định phê duyệt kế hoạch mua trực tiếp, xin vui lòng chọn lại");
//            }
//            hhQdPheduyetKhMttDx.setSoQdPdKqMtt(optional.get().getSoQd());
//            hhQdPheduyetKhMttDxRepository.save(hhQdPheduyetKhMttDx);
//        }else {
//            throw new Exception("Số quyết định phê duyệt kế hoạch mua trực tiếp" + optional.get().getSoQdPdKh()+ " không tồn tại");
//
//        }
//    }
    @Transactional()
    public void delete(Long id) throws Exception{
        if (StringUtils.isEmpty(id))
            throw new Exception("Xóa thất bại, Không tìm thấy dữ liệu");
        Optional<HhQdPduyetKqcgHdr> optional = hhQdPduyetKqcgRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)){
            throw new Exception("CHỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }
        List<HhQdPheduyetKqMttSLDD> listGthau = hhQdPheduyetKqMttSLDDRepository.findAllByIdQdPdKq(optional.get().getId());
        for (HhQdPheduyetKqMttSLDD hhQdPheduyetKqMttSLDD : listGthau) {
            hhQdPdKqMttSlddDtlRepository.deleteAllByIdDiaDiem(hhQdPheduyetKqMttSLDD.getId());
            hhCtietKqTtinCgiaRepository.deleteAllByIdQdPdKqSldd(hhQdPheduyetKqMttSLDD.getId());
            hopDongMttHdrRepository.deleteAllByIdQdPdSldd(hhQdPheduyetKqMttSLDD.getId());
        }
        HhQdPheduyetKhMttDx dx = hhQdPheduyetKhMttDxRepository.findBySoQdKq(optional.get().getSoQdKq());
        dx.setSoQdKq(null);
        hhQdPheduyetKhMttDxRepository.save(dx);
        hhQdPheduyetKqMttSLDDRepository.deleteByIdQdPdKq(optional.get().getId());
        hhQdPduyetKqcgRepository.delete(optional.get());
    }
    @Transactional()
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (ObjectUtils.isEmpty(listMulti)){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        listMulti.forEach(item ->{
            try {
                this.delete(item);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        });
    }

    public void export(SearchHhQdPduyetKqcg objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<HhQdPduyetKqcgHdr> page = this.searchPage(objReq);
        List<HhQdPduyetKqcgHdr> data = page.getContent();

        String title ="Danh sách quyết định mua trưc tiếp";
        String[] rowsName=new String[]{"STT","Số QĐ PDKQ chào giá","Ngày ký QĐ","Đơn vi","Số QĐ PHKH mua trực tiếp","Loại hành hóa","Chủng loại hành hóa","Trạng thái"};
        String fileName="danh-sach-quyet-dinh-phe-duyet-kq-mua-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            HhQdPduyetKqcgHdr qd=data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=qd.getSoQd();
            objs[2]=qd.getNgayTao();
            objs[3]=qd.getTenDvi();
            objs[4]=qd.getSoQd();
            objs[5]=qd.getTenLoaiVthh();
            objs[6]=qd.getTenCloaiVthh();
            objs[7]=qd.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }

    public ReportTemplateResponse preview(HhQdPduyetKqcgHdrReq hhQdPduyetKqcgHdrReq) throws Exception {
        HhQdPduyetKqcgHdr hhQdPduyetKqcgHdr = detail(hhQdPduyetKqcgHdrReq.getId().toString());
        ReportTemplate model = findByTenFile(hhQdPduyetKqcgHdrReq.getReportTemplateRequest());
        byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        BigDecimal tongThanhTien = BigDecimal.ZERO;
        BigDecimal tongThanhTienCg = BigDecimal.ZERO;
        BigDecimal tongSoLuong = BigDecimal.ZERO;
        for (HhQdPheduyetKqMttSLDD sldd : hhQdPduyetKqcgHdr.getDanhSachCtiet()) {
            sldd.setTongThanhTienStr(docxToPdfConverter.convertBigDecimalToStr(sldd.getTongThanhTien()));
            tongSoLuong = tongSoLuong.add(sldd.getTongSoLuong());
            tongThanhTien = tongThanhTien.add(sldd.getTongThanhTien());
            for (HhChiTietKqTTinChaoGia hhChiTietKqTTinChaoGia : sldd.getListChaoGia()) {
                hhChiTietKqTTinChaoGia.setThanhTienStr(docxToPdfConverter.convertBigDecimalToStr(hhChiTietKqTTinChaoGia.getDonGia().multiply(hhChiTietKqTTinChaoGia.getSoLuong())));
                tongThanhTienCg = tongThanhTienCg.add(hhChiTietKqTTinChaoGia.getDonGia().multiply(hhChiTietKqTTinChaoGia.getSoLuong()));
            }
        }
        hhQdPduyetKqcgHdr.setTongThanhTienStr(docxToPdfConverter.convertBigDecimalToStr(tongThanhTien));
        hhQdPduyetKqcgHdr.setTongThanhTienCg(docxToPdfConverter.convertBigDecimalToStr(tongThanhTienCg));
        hhQdPduyetKqcgHdr.setTongSoLuong(tongSoLuong);
        hhQdPduyetKqcgHdr.setTenDvi(hhQdPduyetKqcgHdr.getTenDvi().toUpperCase());
        hhQdPduyetKqcgHdr.setTenCloaiVthh(hhQdPduyetKqcgHdr.getTenCloaiVthh().toUpperCase());

        return docxToPdfConverter.convertDocxToPdf(inputStream, hhQdPduyetKqcgHdr);
    }
}
