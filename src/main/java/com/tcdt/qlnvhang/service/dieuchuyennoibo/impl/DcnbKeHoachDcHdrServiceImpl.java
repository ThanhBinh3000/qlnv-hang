package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbKeHoachDcDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbKeHoachDcHdrRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbPhuongAnDcRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.THKeHoachDieuChuyenNoiBoCucDtlRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbKeHoachDcHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbKeHoachDc;
import com.tcdt.qlnvhang.request.feign.TrangThaiHtReq;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbKeHoachDcDtlDTO;
import com.tcdt.qlnvhang.response.feign.KtMlk;
import com.tcdt.qlnvhang.response.feign.TrangThaiHtResponce;
import com.tcdt.qlnvhang.service.feign.KhoClient;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhuongAnDc;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class DcnbKeHoachDcHdrServiceImpl extends BaseServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(DcnbKeHoachDcHdrServiceImpl.class);

    @Autowired
    private DcnbKeHoachDcHdrRepository dcnbKeHoachDcHdrRepository;

    @Autowired
    private DcnbKeHoachDcDtlRepository dcnbKeHoachDcDtlRepository;
    @Autowired
    private DcnbPhuongAnDcRepository dcnbPhuongAnDcRepository;
    @Autowired
    private THKeHoachDieuChuyenNoiBoCucDtlRepository tHKeHoachDieuChuyenNoiBoCucDtlRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Autowired
    private KhoClient khoClient;

    public Page<DcnbKeHoachDcHdr> searchPage(CustomUserDetails currentUser, SearchDcnbKeHoachDc req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Page<DcnbKeHoachDcHdr> search = null;
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            req.setType(Contains.DIEU_CHUYEN);
            search = dcnbKeHoachDcHdrRepository.searchCuc(req, pageable);
        } else {
            search = dcnbKeHoachDcHdrRepository.search(req, pageable);
        }
        return search;
    }

    public DcnbKeHoachDcHdr save(CustomUserDetails currentUser, DcnbKeHoachDcHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbKeHoachDcHdr> optional = dcnbKeHoachDcHdrRepository.findFirstBySoDxuat(objReq.getSoDxuat());
        if (optional.isPresent()) {
            throw new Exception("số đề xuất đã tồn tại");
        }
        DcnbKeHoachDcHdr data = new DcnbKeHoachDcHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setSoDxuat(objReq.getSoDxuat() + "/DCNB");
        data.setMaDviPq(currentUser.getDvql());
        data.setType(Contains.DIEU_CHUYEN);
        data.setTrangThai(Contains.DUTHAO);
        data.setDaXdinhDiemNhap(false);
        if (objReq.getDanhSachHangHoa() != null) {
            BigDecimal total = objReq.getDanhSachHangHoa().stream()
                    .map(DcnbKeHoachDcDtl::getDuToanKphi)
                    .map(kphi -> kphi != null ? kphi : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            data.setTongDuToanKp(total);
            objReq.getDanhSachHangHoa().forEach(e -> {
                e.setDcnbKeHoachDcHdr(data);
                e.setDaXdinhDiemNhap(false);
                e.setThayDoiThuKho(true);
            });
            // check hàng hóa trong kho xuất, kho nhận
            // Check số lượng hiện thời từng lo kho - (tổng đề kế hoạch xuất - tổng xuất trong thực tế)> 0 ;
            for (DcnbKeHoachDcDtl hh : objReq.getDanhSachHangHoa()) {
                hh.setCoLoKho(!StringUtils.isEmpty(hh.getMaLoKho()));
                if(!StringUtils.isEmpty(hh.getMaNganKho())){
                    KtMlk tinKho = getThongTinKho(hh.getMaLoKho(),hh.getTenLoKho(), hh.getMaNganKho(),hh.getTenNganKho());
                    hh.setLoaiVthh(tinKho.getObject().getLoaiVthh());
                    hh.setCloaiVthh(tinKho.getObject().getCloaiVthh());
                }
                hh.setCoLoKhoNhan(!StringUtils.isEmpty(hh.getMaLoKhoNhan()));
                if(!StringUtils.isEmpty(hh.getMaNganKhoNhan())){
                    KtMlk tinKho = getThongTinKho(hh.getMaLoKhoNhan(),hh.getTenLoKhoNhan(), hh.getMaNganKhoNhan(),hh.getTenNganKhoNhan());
                    if (!hh.getCloaiVthh().equals(tinKho.getObject().getCloaiVthh())) {
                        throw new Exception("Chủng loại hàng hóa kho xuất và nhận không giống nhau!");
                    }
                }
            }
        }
        if (objReq.getPhuongAnDieuChuyen() != null) {
            objReq.getPhuongAnDieuChuyen().forEach(e -> e.setDcnbKeHoachDcHdr(data));
        }
        DcnbKeHoachDcHdr created = dcnbKeHoachDcHdrRepository.save(data);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), DcnbKeHoachDcHdr.TABLE_NAME + "_CAN_CU");
        created.setCanCu(canCu);
        return created;
    }

    private KtMlk getThongTinKho(String maLoKho, String tenLoKho,String maNganKho, String tenNganKho) throws Exception {
        TrangThaiHtReq trangThaiHtReq = new TrangThaiHtReq();
        trangThaiHtReq.setMaDvi(!StringUtils.isEmpty(maLoKho) ? maLoKho : maNganKho);
        trangThaiHtReq.setTenDvi(!StringUtils.isEmpty(maLoKho) ? tenLoKho : tenNganKho);
        ResponseEntity<BaseResponse> responseNhan = khoClient.infoMlk(trangThaiHtReq);
        BaseResponse body = responseNhan.getBody();
        if (body != null && EnumResponse.RESP_SUCC.getDescription().equals(body.getMsg())) {
            logger.debug(body.toString());
            Gson gson = new Gson();
            KtMlk res = gson.fromJson(gson.toJson(body.getData()), KtMlk.class);
            if (res == null) {
                throw new Exception("Không tìm thấy thông tin kho! Ngăn/Lô: "+trangThaiHtReq.getTenDvi());
            }
            if(res.getObject().getCloaiVthh() == null){
                throw new Exception("Chưa khởi tạo kho đầu kì! Ngăn/Lô: "+trangThaiHtReq.getTenDvi());
            }
            return res;
        } else {
            throw new Exception("Không tìm thấy thông tin kho! Ngăn/Lô: "+trangThaiHtReq.getTenDvi());
        }
    }

    public DcnbKeHoachDcHdr update(CustomUserDetails currentUser, DcnbKeHoachDcHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbKeHoachDcHdr> optional = dcnbKeHoachDcHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Optional<DcnbKeHoachDcHdr> soDxuat = dcnbKeHoachDcHdrRepository.findFirstBySoDxuat(objReq.getSoDxuat());
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoDxuat())) {
            if (soDxuat.isPresent()) {
                if (!soDxuat.get().getId().equals(objReq.getId()) && !Contains.NHAN_DIEU_CHUYEN.equals(optional.get().getType())) {
                    throw new Exception("số đề xuất đã tồn tại");
                }
            }
        }

        DcnbKeHoachDcHdr data = optional.get();
        objReq.setType(data.getType());
        objReq.setMaDviPq(data.getMaDviPq());
        BeanUtils.copyProperties(objReq, data);
        data.setDanhSachHangHoa(objReq.getDanhSachHangHoa());
        data.setPhuongAnDieuChuyen(objReq.getPhuongAnDieuChuyen());
        data.setDaXdinhDiemNhap(false);
        if (objReq.getDanhSachHangHoa() != null) {
            BigDecimal total = objReq.getDanhSachHangHoa().stream()
                    .map(DcnbKeHoachDcDtl::getDuToanKphi)
                    .map(kphi -> kphi != null ? kphi : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            data.setTongDuToanKp(total);
            objReq.getDanhSachHangHoa().forEach(e -> {
                e.setDaXdinhDiemNhap(false);
                e.setThayDoiThuKho(true);
            });
            // check hàng hóa trong kho xuất, kho nhận
            // Check số lượng hiện thời từng lo kho - (tổng đề kế hoạch xuất - tổng xuất trong thực tế)> 0 ;
            for (DcnbKeHoachDcDtl hh : objReq.getDanhSachHangHoa()) {
                hh.setCoLoKho(!StringUtils.isEmpty(hh.getMaLoKho()));
                if(!StringUtils.isEmpty(hh.getMaNganKho())){
                    KtMlk tinKho = getThongTinKho(hh.getMaLoKho(),hh.getTenLoKho(), hh.getMaNganKho(),hh.getTenNganKho());
                    hh.setLoaiVthh(tinKho.getObject().getLoaiVthh());
                    hh.setCloaiVthh(tinKho.getObject().getCloaiVthh());
                }
                hh.setCoLoKhoNhan(!StringUtils.isEmpty(hh.getMaLoKhoNhan()));
                if(!StringUtils.isEmpty(hh.getMaNganKhoNhan())){
                    KtMlk tinKho = getThongTinKho(hh.getMaLoKhoNhan(),hh.getTenLoKhoNhan(), hh.getMaNganKhoNhan(),hh.getTenNganKhoNhan());
                    if (!StringUtils.isEmpty(tinKho.getObject().getCloaiVthh()) && !hh.getCloaiVthh().equals(tinKho.getObject().getCloaiVthh())) {
                        throw new Exception("Chủng loại hàng hóa kho xuất và nhận không giống nhau!");
                    }
                }
            }
        }
        DcnbKeHoachDcHdr created = dcnbKeHoachDcHdrRepository.save(data);

        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbKeHoachDcHdr.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), DcnbKeHoachDcHdr.TABLE_NAME + "_CAN_CU");
        created.setCanCu(canCu);
        return created;
    }


    public List<DcnbKeHoachDcHdr> details(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<DcnbKeHoachDcHdr> optional = dcnbKeHoachDcHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        List<DcnbKeHoachDcHdr> allById = dcnbKeHoachDcHdrRepository.findAllById(ids);
        allById.forEach(data -> {
            List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(DcnbKeHoachDcHdr.TABLE_NAME + "_CAN_CU"));
            data.setCanCu(canCu);
        });
        return allById;
    }

    public DcnbKeHoachDcHdr details(Long id) throws Exception {
        List<DcnbKeHoachDcHdr> details = details(Arrays.asList(id));
        DcnbKeHoachDcHdr result = details.isEmpty() ? null : details.get(0);
        if (result != null) {
            Hibernate.initialize(result.getDanhSachHangHoa());
            Hibernate.initialize(result.getPhuongAnDieuChuyen());
        }
        return result;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<DcnbKeHoachDcHdr> optional = dcnbKeHoachDcHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        DcnbKeHoachDcHdr data = optional.get();
        List<DcnbKeHoachDcDtl> list = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrId(data.getId());
        dcnbKeHoachDcDtlRepository.deleteAll(list);
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(DcnbKeHoachDcHdr.TABLE_NAME + "_CAN_CU"));
        dcnbKeHoachDcHdrRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<DcnbKeHoachDcHdr> list = dcnbKeHoachDcHdrRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listId = list.stream().map(DcnbKeHoachDcHdr::getId).collect(Collectors.toList());
        List<DcnbKeHoachDcDtl> listPhuongAn = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrIdIn(listId);
        dcnbKeHoachDcDtlRepository.deleteAll(listPhuongAn);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(DcnbKeHoachDcHdr.TABLE_NAME + "_CAN_CU"));
        dcnbKeHoachDcHdrRepository.deleteAll(list);
    }

    @Transactional
    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbKeHoachDcHdr details = details(Long.valueOf(statusReq.getId()));
        Optional<DcnbKeHoachDcHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        // check đang là điều chuyển hay là nhận điều chuyển
        if (Contains.DIEU_CHUYEN.equals(optional.get().getType())) {
            this.approveDieuChuyen(currentUser, statusReq, optional); // Truyền giá trị của optional vào
        } else if (Contains.NHAN_DIEU_CHUYEN.equals(optional.get().getType())) {
            this.approveNhanDieuChuyen(currentUser, statusReq, optional); // Truyền giá trị của optional vào
        }
    }

    public DcnbKeHoachDcHdr approveDieuChuyen(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbKeHoachDcHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.DUTHAO + Contains.CHODUYET_TBP_TVQT:
            case Contains.TUCHOI_TBP_TVQT + Contains.CHODUYET_TBP_TVQT:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_TBP_TVQT:
                optional.get().setNgayGduyet(LocalDate.now());
                optional.get().setNguoiGduyetId(currentUser.getUser().getId());
                // Check số lượng hiện thời từng lo kho - (tổng đề kế hoạch xuất - tổng xuất trong thực tế)> 0 ;
                //  /qlnv-luukho/hang-trong-kho/trang-thai-ht
                List<DcnbKeHoachDcDtl> danhSachHangHoa = optional.get().getDanhSachHangHoa();
                if (danhSachHangHoa == null || danhSachHangHoa.isEmpty()) {
                    throw new Exception("Vui lòng thêm danh sách hàng hóa!");
                }
                for (DcnbKeHoachDcDtl hh : danhSachHangHoa) {
                    KtMlk tinKho = getThongTinKho(hh.getMaLoKho(),hh.getTenLoKho(), hh.getMaNganKho(),hh.getTenNganKho());
                    BigDecimal slHienThoi = tinKho.getObject().getSlTon();
                    BigDecimal slConLai = new BigDecimal(0);
                    if (hh.getCoLoKho()) {
                        BigDecimal total = danhSachHangHoa.stream().filter(item -> item.getMaLoKho().equals(hh.getMaLoKho()))
                                .map(DcnbKeHoachDcDtl::getSoLuongDc)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        slConLai = slHienThoi.subtract(total);
                    } else {
                        BigDecimal total = danhSachHangHoa.stream().filter(item -> item.getMaNganKho().equals(hh.getMaNganKho()))
                                .map(DcnbKeHoachDcDtl::getSoLuongDc)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        slConLai = slHienThoi.subtract(total);
                    }

                    int result = slConLai.compareTo(BigDecimal.valueOf(0));
                    if (result < 0) {
                        throw new Exception(hh.getTenLoKho() + ": Không đủ số lượng xuất hàng!");
                    }
                }
                break;
            case Contains.CHODUYET_TBP_TVQT + Contains.TUCHOI_TBP_TVQT:
                optional.get().setNgayPduyet(LocalDate.now());
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_TBP_TVQT + Contains.CHODUYET_LDCC:
                optional.get().setNgayPduyet(LocalDate.now());
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                optional.get().setNgayDuyetLdcc(LocalDate.now());
                optional.get().setNguoiDuyetLdccId(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                optional.get().setNgayDuyetLdcc(LocalDate.now());
                optional.get().setNguoiDuyetLdccId(currentUser.getUser().getId());

                // xử lý clone tờ kế hoạch cho các chi cục
                for (DcnbPhuongAnDc dc : optional.get().getPhuongAnDieuChuyen()) {
                    DcnbKeHoachDcHdr clonedObj = SerializationUtils.clone(optional.get());
                    clonedObj.setParentId(clonedObj.getId());
                    clonedObj.setId(null);
                    clonedObj.setMaDviPq(dc.getMaChiCucNhan());
                    clonedObj.setType(Contains.NHAN_DIEU_CHUYEN);
                    clonedObj.setTrangThai(statusReq.getTrangThai());
                    clonedObj.setDanhSachHangHoa(clonedObj.getDanhSachHangHoa().stream()
                            .filter(item -> item.getMaChiCucNhan().equals(dc.getMaChiCucNhan())).map(itemMap -> {
                                itemMap.setParentId(itemMap.getId());
                                itemMap.setId(null);
                                return itemMap;
                            }).collect(Collectors.toList()));
                    clonedObj.setPhuongAnDieuChuyen(clonedObj.getPhuongAnDieuChuyen().stream()
                            .filter(item -> item.getMaChiCucNhan().equals(dc.getMaChiCucNhan())).map(itemMap -> {
                                itemMap.setParentId(itemMap.getId());
                                itemMap.setId(null);
                                return itemMap;
                            }).collect(Collectors.toList()));
                    clonedObj.setCanCu(clonedObj.getCanCu().stream().map(itemMap -> {
                        itemMap.setId(null);
                        itemMap.setDataId(null);
                        return itemMap;
                    }).collect(Collectors.toList()));
                    BigDecimal total = clonedObj.getDanhSachHangHoa().stream()
                            .map(DcnbKeHoachDcDtl::getDuToanKphi)
                            .map(kphi -> kphi != null ? kphi : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    clonedObj.setTongDuToanKp(total);
                    clonedObj = dcnbKeHoachDcHdrRepository.save(clonedObj);
                    fileDinhKemService.delete(clonedObj.getId(), Lists.newArrayList(DcnbKeHoachDcHdr.TABLE_NAME + "_CAN_CU"));
                    List<FileDinhKemReq> fileDinhKemReqs = clonedObj.getCanCu().stream()
                            .map(person -> new ModelMapper().map(person, FileDinhKemReq.class))
                            .collect(Collectors.toList());
                    List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(fileDinhKemReqs, clonedObj.getId(), DcnbKeHoachDcHdr.TABLE_NAME + "_CAN_CU");
                    clonedObj.setCanCu(canCu);
                }
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbKeHoachDcHdr created = dcnbKeHoachDcHdrRepository.save(optional.get());
        return created;
    }

    private BigDecimal getTongSoLuongXuatKho(String cloaiVthh, String maLoKho) {
        // todo
        return new BigDecimal(0);
    }

    private BigDecimal getTongKeHoachDeXuat(String cloaiVthh, String maLoKho, Long hdrId) {
        if (maLoKho.length() == 16) {
            return dcnbKeHoachDcHdrRepository.countTongKeHoachDeXuatLoKho(cloaiVthh, maLoKho, hdrId);
        } else {
            return dcnbKeHoachDcHdrRepository.countTongKeHoachDeXuatNganKho(cloaiVthh, maLoKho, hdrId);
        }
    }

    public DcnbKeHoachDcHdr approveNhanDieuChuyen(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbKeHoachDcHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.YC_CHICUC_PHANBO_DC + Contains.DA_PHANBO_DC_CHODUYET_TBP_TVQT:
            case Contains.DA_PHANBO_DC_TUCHOI_TP + Contains.DA_PHANBO_DC_CHODUYET_TBP_TVQT:
            case Contains.DA_PHANBO_DC_TUCHOI_LDC + Contains.DA_PHANBO_DC_CHODUYET_TBP_TVQT:
            case Contains.DA_PHANBO_DC_TUCHOI_TBP_TVQT + Contains.DA_PHANBO_DC_CHODUYET_TBP_TVQT:
            case Contains.DA_PHANBO_DC_TUCHOI_LDCC + Contains.DA_PHANBO_DC_CHODUYET_TBP_TVQT:
                optional.get().setNgayGduyet(LocalDate.now());
                optional.get().setNguoiGduyetId(currentUser.getUser().getId());
                break;
            case Contains.DA_PHANBO_DC_CHODUYET_TBP_TVQT + Contains.DA_PHANBO_DC_TUCHOI_TBP_TVQT:
                optional.get().setNgayPduyet(LocalDate.now());
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.DA_PHANBO_DC_CHODUYET_TBP_TVQT + Contains.DA_PHANBO_DC_CHODUYET_LDCC:
                optional.get().setNgayPduyet(LocalDate.now());
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                break;
            case Contains.DA_PHANBO_DC_CHODUYET_LDCC + Contains.DA_PHANBO_DC_TUCHOI_LDCC:
                optional.get().setNgayDuyetLdcc(LocalDate.now());
                optional.get().setNguoiDuyetLdccId(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.DA_PHANBO_DC_CHODUYET_LDCC + Contains.DA_PHANBO_DC_DADUYET_LDCC:
                optional.get().setNgayDuyetLdcc(LocalDate.now());
                optional.get().setNguoiDuyetLdccId(currentUser.getUser().getId());
                optional.get().setDaXdinhDiemNhap(true);
                optional.get().setXdLaiDiemNhap(false);
                // update lại các kho nhận điều chuyển trong danh sách hàng hóa cha.
                List<DcnbKeHoachDcDtl> danhSachHangHoa = optional.get().getDanhSachHangHoa();
                Set<Long> parentIds = new HashSet<>();
                for (DcnbKeHoachDcDtl hh : danhSachHangHoa) {
                    hh.setDaXdinhDiemNhap(true);
                    dcnbKeHoachDcDtlRepository.save(hh);
                    Optional<DcnbKeHoachDcDtl> parentDtl = dcnbKeHoachDcDtlRepository.findById(hh.getParentId());
                    boolean contains = parentIds.contains(hh.getParentId());
                    if (parentDtl.isPresent() && !contains) {
                        parentIds.add(hh.getParentId());
                        parentDtl.get().setMaDiemKhoNhan(hh.getMaDiemKhoNhan());
                        parentDtl.get().setTenDiemKhoNhan(hh.getTenDiemKhoNhan());
                        parentDtl.get().setMaNhaKhoNhan(hh.getMaNhaKhoNhan());
                        parentDtl.get().setTenNhaKhoNhan(hh.getTenNhaKhoNhan());
                        parentDtl.get().setMaNganKhoNhan(hh.getMaNganKhoNhan());
                        parentDtl.get().setTenNganKhoNhan(hh.getTenNganKhoNhan());
                        parentDtl.get().setCoLoKhoNhan(hh.getCoLoKhoNhan());
                        parentDtl.get().setMaLoKhoNhan(hh.getMaLoKhoNhan());
                        parentDtl.get().setTenLoKhoNhan(hh.getTenLoKhoNhan());
                        parentDtl.get().setTichLuongKd(hh.getTichLuongKd());
                        parentDtl.get().setSoLuongPhanBo(hh.getSoLuongPhanBo());
                        parentDtl.get().setSlDcConLai(hh.getSlDcConLai());
                        parentDtl.get().setThuKhoNhan(hh.getThuKhoNhan());
                        parentDtl.get().setThuKhoNhanId(hh.getThuKhoNhanId());
                        parentDtl.get().setThayDoiThuKho(hh.getThayDoiThuKho());
                        parentDtl.get().setDaXdinhDiemNhap(true);
                        dcnbKeHoachDcDtlRepository.save(parentDtl.get());
                    }else  if (parentDtl.isPresent()){
                        DcnbKeHoachDcDtl clone = SerializationUtils.clone(parentDtl.get());
                        clone.setId(null);
                        clone.setMaDiemKhoNhan(hh.getMaDiemKhoNhan());
                        clone.setTenDiemKhoNhan(hh.getTenDiemKhoNhan());
                        clone.setMaNhaKhoNhan(hh.getMaNhaKhoNhan());
                        clone.setTenNhaKhoNhan(hh.getTenNhaKhoNhan());
                        clone.setMaNganKhoNhan(hh.getMaNganKhoNhan());
                        clone.setTenNganKhoNhan(hh.getTenNganKhoNhan());
                        clone.setCoLoKhoNhan(hh.getCoLoKhoNhan());
                        clone.setMaLoKhoNhan(hh.getMaLoKhoNhan());
                        clone.setTenLoKhoNhan(hh.getTenLoKhoNhan());
                        clone.setTichLuongKd(hh.getTichLuongKd());
                        clone.setSoLuongPhanBo(hh.getSoLuongPhanBo());
                        clone.setSlDcConLai(hh.getSlDcConLai());
                        clone.setThuKhoNhan(hh.getThuKhoNhan());
                        clone.setThuKhoNhanId(hh.getThuKhoNhanId());
                        clone.setThayDoiThuKho(hh.getThayDoiThuKho());
                        clone.setDaXdinhDiemNhap(true);
                        dcnbKeHoachDcDtlRepository.save(clone);
                    }
                }

                Optional<DcnbKeHoachDcHdr> parentHdr = dcnbKeHoachDcHdrRepository.findById(optional.get().getParentId());
                if (parentHdr.isPresent()) {
                    boolean allMatch = parentHdr.get().getDanhSachHangHoa().stream().allMatch(n -> (n.getDaXdinhDiemNhap() != null && n.getDaXdinhDiemNhap() == true));
                    if (allMatch) {
                        parentHdr.get().setDaXdinhDiemNhap(true);
                        dcnbKeHoachDcHdrRepository.save(parentHdr.get());
                    }
                }
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbKeHoachDcHdr created = dcnbKeHoachDcHdrRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, SearchDcnbKeHoachDc objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<DcnbKeHoachDcHdr> page = this.searchPage(currentUser, objReq);
        List<DcnbKeHoachDcHdr> data = page.getContent();

        String title = "Danh sách kế hoạch điều chuyển nội bộ ";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số công văn/đề xuất", "Ngày lập KH", "Ngày duyệt LĐ Chi cục", "Loại điều chuyển", "Đơn vị đề xuất", "Trạng thái"};
        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbKeHoachDcHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            objs[1] = dx.getNam();
            objs[2] = dx.getSoDxuat();
            objs[3] = dx.getNgayLapKh();
            objs[4] = dx.getNgayDuyetLdcc();
            objs[5] = dx.getTenLoaiDc();
            objs[6] = dx.getTenDvi();
            objs[7] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public List<DcnbKeHoachDcDtl> detailDtl(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<DcnbKeHoachDcDtl> optional = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        return optional;
    }
}
