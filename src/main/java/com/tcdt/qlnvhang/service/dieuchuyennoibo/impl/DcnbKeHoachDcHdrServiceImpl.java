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
import com.tcdt.qlnvhang.response.feign.TrangThaiHtResponce;
import com.tcdt.qlnvhang.service.feign.LuuKhoClient;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
    private LuuKhoClient luuKhoClient;

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

    @Transactional
    public DcnbKeHoachDcHdr save(CustomUserDetails currentUser, DcnbKeHoachDcHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbKeHoachDcHdr> optional = dcnbKeHoachDcHdrRepository.findFirstBySoDxuat(objReq.getSoDxuat());
        if (optional.isPresent() && objReq.getSoDxuat().split("/").length == 1) {
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
            });
        }
        if (objReq.getPhuongAnDieuChuyen() != null) {
            objReq.getPhuongAnDieuChuyen().forEach(e -> e.setDcnbKeHoachDcHdr(data));
        }
        DcnbKeHoachDcHdr created = dcnbKeHoachDcHdrRepository.save(data);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), DcnbKeHoachDcHdr.TABLE_NAME + "_CAN_CU");
        created.setCanCu(canCu);
        return created;
    }

    public List<DcnbKeHoachDcDtlDTO> danhSachMaLokho(SearchDcnbKeHoachDc req) throws Exception {
        List<DcnbKeHoachDcDtlDTO> danhSachMaLoKho = dcnbKeHoachDcDtlRepository.findByQdDcIdAndBbLayMauId(req.getIdQdDc());
        return danhSachMaLoKho;
    }

    @Transactional
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
            if (soDxuat.isPresent() && objReq.getSoDxuat().split("/").length == 1) {
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
            });
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
                    TrangThaiHtReq objReq = new TrangThaiHtReq();
                    objReq.setMaDvi(hh.getCoLoKho() ? hh.getMaLoKho() : hh.getMaNganKho());
                    ResponseEntity<BaseResponse> response = luuKhoClient.trangThaiHt(objReq);
                    BaseResponse body = response.getBody();
                    if (body != null && EnumResponse.RESP_SUCC.getDescription().equals(body.getMsg())) {
                        logger.debug(body.toString());
                        TypeToken<List<TrangThaiHtResponce>> token = new TypeToken<List<TrangThaiHtResponce>>() {
                        };
                        Gson gson = new Gson();
                        List<TrangThaiHtResponce> res = gson.fromJson(gson.toJson(body.getData()), token.getType());
                        if (res == null || res.isEmpty()) {
                            throw new Exception("Không lấy được trạng thái kho hiện thời!");
                        }
                        if (res.size() > 1) {
                            throw new Exception("Tìm thấy 2 trạng thái kho hiện thời!");
                        }
                        if (!hh.getCloaiVthh().equals(res.get(0).getCloaiVthh())) {
                            throw new Exception("Chủng loại hàng hóa không đúng trong kho hiện thời!");
                        }
                        BigDecimal slHienThoi = new BigDecimal(res.get(0).getSlHienThoi());
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
                    } else {
                        throw new Exception("Không lấy được trạng thái kho hiện thời!");
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
                for (DcnbKeHoachDcDtl hh : danhSachHangHoa) {
                    hh.setDaXdinhDiemNhap(true);
                    dcnbKeHoachDcDtlRepository.save(hh);
                    Optional<DcnbKeHoachDcDtl> parentDtl = dcnbKeHoachDcDtlRepository.findById(hh.getParentId());
                    if (parentDtl.isPresent()) {
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
