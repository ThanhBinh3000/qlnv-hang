package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbQuyetDinhDcCHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbQuyetDinhDcC;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.response.DieuChuyenNoiBo.DcnbQuyetDinhDcCHdrDTO;
import com.tcdt.qlnvhang.service.feign.LuuKhoClient;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.apache.commons.lang3.SerializationUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


@Service
public class DcnbQuyetDinhDcCDtlService extends BaseServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(DcnbQuyetDinhDcCDtlService.class);

    @Autowired
    private DcnbQuyetDinhDcCHdrRepository dcnbQuyetDinhDcCHdrRepository;
    @Autowired
    private DcnbQuyetDinhDcTcHdrRepository dcnbQuyetDinhDcTcHdrRepository;
    @Autowired
    private DcnbQuyetDinhDcCDtlRepository dcnbQuyetDinhDcCDtlRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Autowired
    private DcnbKeHoachDcDtlRepository dcnbKeHoachDcDtlRepository;
    @Autowired
    private DcnbKeHoachDcHdrRepository dcnbKeHoachDcHdrRepository;
    @Autowired
    private DcnbKeHoachDcHdrService dcnbKeHoachDcHdrService;
    @Autowired
    private LuuKhoClient luuKhoClient;

    public Page<DcnbQuyetDinhDcCHdr> searchPage(CustomUserDetails currentUser, SearchDcnbQuyetDinhDcC req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            req.setType(Contains.NHAN_DIEU_CHUYEN);
        }
        if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            req.setType(Contains.DIEU_CHUYEN);
        }
        Page<DcnbQuyetDinhDcCHdr> search = dcnbQuyetDinhDcCHdrRepository.search(req, pageable);
        return search;
    }

    @Transactional
    public DcnbQuyetDinhDcCHdr save(CustomUserDetails currentUser, DcnbQuyetDinhDcCHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbQuyetDinhDcCHdr> optional = dcnbQuyetDinhDcCHdrRepository.findFirstBySoQdinhAndType(objReq.getSoQdinh(),Contains.DIEU_CHUYEN);
        if (optional.isPresent() && objReq.getSoQdinh().split("/").length == 1) {
            throw new Exception("số quyết định đã tồn tại");
        }
        DcnbQuyetDinhDcCHdr data = new DcnbQuyetDinhDcCHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getDvql());
        data.setTrangThai(Contains.DUTHAO);
        data.setType(Contains.DIEU_CHUYEN);

        for (DcnbQuyetDinhDcCDtl e : objReq.getDanhSachQuyetDinh()) {
            if (Contains.DCNB.equals(data.getLoaiDc()) && Contains.CAP_CUC.equals(currentUser.getUser().getCapDvi())) {
                // được phép thêm mới kế hoạch và update kế hoạch (ngầm)
                if (e.getKeHoachDcHdrId() == null) {
                    if (e.getDanhSachKeHoach() != null && !e.getDanhSachKeHoach().isEmpty()) {
                        DcnbKeHoachDcHdr dcnbKeHoachDcHdr = new DcnbKeHoachDcHdr();
                        dcnbKeHoachDcHdr.setType(Contains.NHAN_DIEU_CHUYEN_TS);
                        dcnbKeHoachDcHdr.setMaDviPq(e.getDanhSachKeHoach().get(0).getMaChiCucNhan());
                        dcnbKeHoachDcHdr.setPhuongAnDieuChuyen(new ArrayList<>());
                        BigDecimal total = e.getDanhSachKeHoach().stream().map(DcnbKeHoachDcDtl::getDuToanKphi)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        dcnbKeHoachDcHdr.setTongDuToanKp(total);
                        e.getDanhSachKeHoach().forEach(e1 -> {
                            e1.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr);
                            e1.setDaXdinhDiemNhap(true);
                        });
                        dcnbKeHoachDcHdr.setDaXdinhDiemNhap(true);
                        dcnbKeHoachDcHdr.setDanhSachHangHoa(e.getDanhSachKeHoach());
                        DcnbKeHoachDcHdr dcnbKeHoachDcHdrNew = dcnbKeHoachDcHdrRepository.save(dcnbKeHoachDcHdr);
                        e.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdrNew);
                        e.setKeHoachDcHdrId(dcnbKeHoachDcHdrNew.getId());
                    }
                } else {
                    throw new Exception("dcnbKeHoachDcHdr.id phải là null!");
                }
            }else {
                if (e.getKeHoachDcHdrId() == null) {
                    if (e.getDanhSachKeHoach() != null && !e.getDanhSachKeHoach().isEmpty()) {
                        Optional<DcnbKeHoachDcHdr> dcnbKeHoachDcHdr = dcnbKeHoachDcHdrRepository.findById(e.getDanhSachKeHoach().get(0).getHdrId());
                        if (!dcnbKeHoachDcHdr.isPresent()) {
                            throw new Exception("Không tìm thấy kế hoạch id = "+ e.getDanhSachKeHoach().get(0).getHdrId());
                        }
                        e.getDanhSachKeHoach().forEach(e1 -> {
                            e1.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr.get());
                        });
                        dcnbKeHoachDcHdr.get().setDanhSachHangHoa(e.getDanhSachKeHoach());
                        DcnbKeHoachDcHdr dcnbKeHoachDcHdrNew = dcnbKeHoachDcHdrRepository.save(dcnbKeHoachDcHdr.get());
                        e.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdrNew);
                        e.setKeHoachDcHdrId(dcnbKeHoachDcHdrNew.getId());
                    }
                } else {
                    throw new Exception("dcnbKeHoachDcHdr.id phải là null!");
                }
            }
            e.setDcnbQuyetDinhDcCHdr(data);
        }
        List<Long> longs = data.getDanhSachQuyetDinh().stream().map(DcnbQuyetDinhDcCDtl::getKeHoachDcHdrId).collect(Collectors.toList());

        List<Long> duplicates = longs.stream()
                .filter(number -> Collections.frequency(longs, number) > 1)
                .distinct()
                .collect(Collectors.toList());
        if(!duplicates.isEmpty()){
            throw new Exception("Mỗi danhSachQuyetDinh chỉ có 1 keHoachDcHdrId!");
        }
        DcnbQuyetDinhDcCHdr created = dcnbQuyetDinhDcCHdrRepository.save(data);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), DcnbQuyetDinhDcCHdr.TABLE_NAME + "_CAN_CU");
        List<FileDinhKem> quyetDinh = fileDinhKemService.saveListFileDinhKem(objReq.getQuyetDinh(), created.getId(), DcnbQuyetDinhDcCHdr.TABLE_NAME + "_QUYET_DINH");
        created.setCanCu(canCu);
        created.setQuyetDinh(quyetDinh);
        return created;
    }

    public List<DcnbQuyetDinhDcCHdrDTO> danhSachSoQdDieuChuyen(SearchDcnbQuyetDinhDcC req) throws Exception{
        List<DcnbQuyetDinhDcCHdrDTO> danhSachSoQdDieuChuyen = dcnbQuyetDinhDcCHdrRepository.findByLoaiDcAndTrangThai(req.getLoaiDc(),Contains.BAN_HANH, req.getMaDvi());
        return danhSachSoQdDieuChuyen;
    }

    @Transactional
    public DcnbQuyetDinhDcCHdr update(CustomUserDetails currentUser, DcnbQuyetDinhDcCHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbQuyetDinhDcCHdr> optional = dcnbQuyetDinhDcCHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Optional<DcnbQuyetDinhDcCHdr> soDxuat = dcnbQuyetDinhDcCHdrRepository.findFirstBySoQdinhAndType(objReq.getSoQdinh(),Contains.DIEU_CHUYEN);
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoQdinh())) {
            if (soDxuat.isPresent() && objReq.getSoQdinh().split("/").length == 1) {
                if (!soDxuat.get().getId().equals(objReq.getId())) {
                    throw new Exception("số quyết định đã tồn tại");
                }
            }
        }

        DcnbQuyetDinhDcCHdr data = optional.get();
        objReq.setMaDvi(data.getMaDvi());
        objReq.setType( data.getType());
        BeanUtils.copyProperties(objReq, data);
        data.setDanhSachQuyetDinh(objReq.getDanhSachQuyetDinh());
        for (DcnbQuyetDinhDcCDtl e : objReq.getDanhSachQuyetDinh()) {
            if (Contains.DCNB.equals(data.getLoaiDc()) && Contains.CAP_CUC.equals(currentUser.getUser().getCapDvi())) {
                // được phép thêm mới kế hoạch và update kế hoạch (ngầm)
                if (e.getKeHoachDcHdrId() != null) {
                    if (e.getDanhSachKeHoach() != null && !e.getDanhSachKeHoach().isEmpty()) {
                        DcnbKeHoachDcHdr dcnbKeHoachDcHdr = new DcnbKeHoachDcHdr();
                        dcnbKeHoachDcHdr.setType(Contains.NHAN_DIEU_CHUYEN_TS);
                        dcnbKeHoachDcHdr.setMaDviPq(e.getDanhSachKeHoach().get(0).getMaChiCucNhan());
                        dcnbKeHoachDcHdr.setPhuongAnDieuChuyen(new ArrayList<>());
                        Optional<DcnbKeHoachDcHdr> keHoachDcHdrOpt = dcnbKeHoachDcHdrRepository.findById(e.getKeHoachDcHdrId());
                        if (!keHoachDcHdrOpt.isPresent()) {
                            throw new Exception("dcnbKeHoachDcHdr.id không tìm thấy trong hệ thống!");
                        }
                        dcnbKeHoachDcHdr.setId(keHoachDcHdrOpt.get().getId());
                        dcnbKeHoachDcHdr.setType(Contains.NHAN_DIEU_CHUYEN_TS);
                        dcnbKeHoachDcHdr.setMaDviPq(e.getDcnbKeHoachDcHdr().getMaDvi());

                        BigDecimal total = e.getDanhSachKeHoach().stream().map(DcnbKeHoachDcDtl::getDuToanKphi)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        dcnbKeHoachDcHdr.setTongDuToanKp(total);
                        e.getDanhSachKeHoach().forEach(e1 -> {
                            e1.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr);
                            e1.setDaXdinhDiemNhap(true);
                        });
                        dcnbKeHoachDcHdr.setDaXdinhDiemNhap(true);
                        dcnbKeHoachDcHdr.setDanhSachHangHoa(e.getDanhSachKeHoach());
                        dcnbKeHoachDcHdrRepository.save(dcnbKeHoachDcHdr);
                    }
                } else {
                    if (e.getDanhSachKeHoach() != null && !e.getDanhSachKeHoach().isEmpty()) {
                        DcnbKeHoachDcHdr dcnbKeHoachDcHdr = new DcnbKeHoachDcHdr();
                        dcnbKeHoachDcHdr.setType(Contains.NHAN_DIEU_CHUYEN_TS);
                        dcnbKeHoachDcHdr.setMaDviPq(e.getDanhSachKeHoach().get(0).getMaDiemKhoNhan());
                        dcnbKeHoachDcHdr.setPhuongAnDieuChuyen(new ArrayList<>());

                        BigDecimal total = e.getDanhSachKeHoach().stream().map(DcnbKeHoachDcDtl::getDuToanKphi)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        dcnbKeHoachDcHdr.setTongDuToanKp(total);
                        e.getDanhSachKeHoach().forEach(e1 -> {
                            e1.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr);
                            e1.setDaXdinhDiemNhap(true);
                        });
                        dcnbKeHoachDcHdr.setDaXdinhDiemNhap(true);
                        dcnbKeHoachDcHdr.setDanhSachHangHoa(e.getDanhSachKeHoach());
                        DcnbKeHoachDcHdr dcnbKeHoachDcHdrNew = dcnbKeHoachDcHdrRepository.save(dcnbKeHoachDcHdr);
                        e.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdrNew);
                        e.setKeHoachDcHdrId(dcnbKeHoachDcHdrNew.getId());
                    }
                }
            } else if (Contains.GIUA_2_CUC_DTNN_KV.equals(data.getLoaiDc()) && Contains.QD_NHAP.equals(data.getLoaiQdinh()) && Contains.CAP_CHI_CUC.equals(currentUser.getUser().getCapDvi())) {
                // được phép update kế hoạch (ngầm)
                DcnbKeHoachDcHdr dcnbKeHoachDcHdr = e.getDcnbKeHoachDcHdr();
                if (dcnbKeHoachDcHdr.getId() != null) {
                    Optional<DcnbKeHoachDcHdr> keHoachDcHdrOpt = dcnbKeHoachDcHdrRepository.findById(dcnbKeHoachDcHdr.getId());
                    if (!keHoachDcHdrOpt.isPresent()) {
                        throw new Exception("dcnbKeHoachDcHdr.id không tìm thấy trong hệ thống!");
                    }
                    if (e.getDanhSachKeHoach() != null && !e.getDanhSachKeHoach().isEmpty()) {
                        e.getDanhSachKeHoach().forEach(e1 -> {
                            e1.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr);
                            e1.setDaXdinhDiemNhap(true);
                        });
                    }
                    dcnbKeHoachDcHdr.setDanhSachHangHoa(e.getDanhSachKeHoach());
                    dcnbKeHoachDcHdrRepository.save(dcnbKeHoachDcHdr);
                } else {
                    throw new Exception("dcnbKeHoachDcHdr.id phải khác null!");
                }
            }else {
                if (e.getKeHoachDcHdrId() != null) {
                    if (e.getDanhSachKeHoach() != null && !e.getDanhSachKeHoach().isEmpty()) {
                        Optional<DcnbKeHoachDcHdr> dcnbKeHoachDcHdr = dcnbKeHoachDcHdrRepository.findById(e.getDanhSachKeHoach().get(0).getHdrId());
                        if (!dcnbKeHoachDcHdr.isPresent()) {
                            throw new Exception("Không tìm thấy kế hoạch id = "+ e.getDanhSachKeHoach().get(0).getHdrId());
                        }
                        e.getDanhSachKeHoach().forEach(e1 -> {
                            e1.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr.get());
                        });
                        dcnbKeHoachDcHdr.get().setDanhSachHangHoa(e.getDanhSachKeHoach());
                        DcnbKeHoachDcHdr dcnbKeHoachDcHdrNew = dcnbKeHoachDcHdrRepository.save(dcnbKeHoachDcHdr.get());
                        e.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdrNew);
                        e.setKeHoachDcHdrId(dcnbKeHoachDcHdrNew.getId());
                    }
                } else {
                    throw new Exception("dcnbKeHoachDcHdr.id phải khác null!");
                }
            }
            e.setDcnbQuyetDinhDcCHdr(data);
        }
        List<Long> longs = data.getDanhSachQuyetDinh().stream().map(DcnbQuyetDinhDcCDtl::getKeHoachDcHdrId).collect(Collectors.toList());

        List<Long> duplicates = longs.stream()
                .filter(number -> Collections.frequency(longs, number) > 1)
                .distinct()
                .collect(Collectors.toList());
        if(!duplicates.isEmpty()){
            throw new Exception("Mỗi danhSachQuyetDinh chỉ có 1 keHoachDcHdrId!");
        }
        DcnbQuyetDinhDcCHdr created = dcnbQuyetDinhDcCHdrRepository.save(data);

        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbQuyetDinhDcCHdr.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), DcnbQuyetDinhDcCHdr.TABLE_NAME + "_CAN_CU");

        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbQuyetDinhDcCHdr.TABLE_NAME + "_QUYET_DINH"));
        List<FileDinhKem> quyetDinh = fileDinhKemService.saveListFileDinhKem(objReq.getQuyetDinh(), created.getId(), DcnbQuyetDinhDcCHdr.TABLE_NAME + "_QUYET_DINH");
        created.setCanCu(canCu);
        created.setQuyetDinh(quyetDinh);
        return created;
    }


    public List<DcnbQuyetDinhDcCHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<DcnbQuyetDinhDcCHdr> optional = dcnbQuyetDinhDcCHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        optional.forEach(data -> {
            List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(DcnbQuyetDinhDcCHdr.TABLE_NAME + "_CAN_CU"));
            data.setCanCu(canCu);
            List<FileDinhKem> quyetDinh = fileDinhKemService.search(data.getId(), Arrays.asList(DcnbQuyetDinhDcCHdr.TABLE_NAME + "_QUYET_DINH"));
            data.setQuyetDinh(quyetDinh);

            List<DcnbQuyetDinhDcCDtl> sachQuyetDinh = data.getDanhSachQuyetDinh();
            sachQuyetDinh.forEach(data1 -> {
                if (data1.getKeHoachDcHdrId() != null) {
                    Optional<DcnbKeHoachDcHdr> dcnbKeHoachDcHdr = dcnbKeHoachDcHdrRepository.findById(data1.getKeHoachDcHdrId());
                    if (dcnbKeHoachDcHdr.isPresent()) {
                        data1.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr.get());
                    }
                    List<DcnbKeHoachDcDtl> khs = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrId(data1.getKeHoachDcHdrId());
                    data1.setDanhSachKeHoach(khs);
                }
            });
        });
        return optional;
    }

    public DcnbQuyetDinhDcCHdr detail(Long id) throws Exception {
        List<DcnbQuyetDinhDcCHdr> details = detail(Arrays.asList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<DcnbQuyetDinhDcCHdr> optional = dcnbQuyetDinhDcCHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        DcnbQuyetDinhDcCHdr data = optional.get();
        List<DcnbQuyetDinhDcCDtl> list = dcnbQuyetDinhDcCDtlRepository.findByHdrId(data.getId());
        dcnbQuyetDinhDcCDtlRepository.deleteAll(list);
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(DcnbQuyetDinhDcCHdr.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(DcnbQuyetDinhDcCHdr.TABLE_NAME + "_QUYET_DINH"));
        dcnbQuyetDinhDcCHdrRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<DcnbQuyetDinhDcCHdr> list = dcnbQuyetDinhDcCHdrRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listId = list.stream().map(DcnbQuyetDinhDcCHdr::getId).collect(Collectors.toList());
        List<DcnbQuyetDinhDcCDtl> listDtl = dcnbQuyetDinhDcCDtlRepository.findByHdrIdIn(listId);
        dcnbQuyetDinhDcCDtlRepository.deleteAll(listDtl);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(DcnbQuyetDinhDcCHdr.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(DcnbQuyetDinhDcCHdr.TABLE_NAME + "_QUYET_DINH"));
        dcnbQuyetDinhDcCHdrRepository.deleteAll(list);
    }

    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbQuyetDinhDcCHdr details = detail(Long.valueOf(statusReq.getId()));
        Optional<DcnbQuyetDinhDcCHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        // check đang là điều chuyển hay là nhận điều chuyển
        if (Contains.DIEU_CHUYEN.equals(optional.get().getType())) {
            this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
        } else if (Contains.NHAN_DIEU_CHUYEN.equals(optional.get().getType())) {
            this.approveNhanDieuChuyen(currentUser, statusReq, optional); // Truyền giá trị của optional vào
        }
    }

    private DcnbQuyetDinhDcCHdr approveNhanDieuChuyen(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbQuyetDinhDcCHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.YC_CHICUC_PHANBO_DC + Contains.CHODUYET_TBP_TVQT:
            case Contains.TUCHOI_TBP_TVQT + Contains.CHODUYET_TBP_TVQT:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_TBP_TVQT:
                optional.get().setNgayGduyet(LocalDate.now());
                optional.get().setNguoiGduyetId(currentUser.getUser().getId());
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

                List<DcnbQuyetDinhDcCDtl> danhSachQuyetDinh = optional.get().getDanhSachQuyetDinh();
                for (DcnbQuyetDinhDcCDtl hh : danhSachQuyetDinh) {
                    List<DcnbKeHoachDcDtl> danhSachKeHoach = hh.getDanhSachKeHoach();
                    for (DcnbKeHoachDcDtl kh: danhSachKeHoach){
                        Optional<DcnbKeHoachDcDtl> parentDtl = dcnbKeHoachDcDtlRepository.findById(kh.getParentId());
                        if (parentDtl.isPresent()) {
                            parentDtl.get().setMaDiemKhoNhan(kh.getMaDiemKhoNhan());
                            parentDtl.get().setTenDiemKhoNhan(kh.getTenDiemKhoNhan());
                            parentDtl.get().setMaNhaKhoNhan(kh.getMaNhaKhoNhan());
                            parentDtl.get().setTenNhaKhoNhan(kh.getTenNhaKhoNhan());
                            parentDtl.get().setMaNganKhoNhan(kh.getMaNganKhoNhan());
                            parentDtl.get().setTenNganKhoNhan(kh.getTenNganKhoNhan());
                            parentDtl.get().setCoLoKhoNhan(kh.getCoLoKhoNhan());
                            parentDtl.get().setMaLoKhoNhan(kh.getMaLoKhoNhan());
                            parentDtl.get().setTenLoKhoNhan(kh.getTenLoKhoNhan());
                            parentDtl.get().setTichLuongKd(kh.getTichLuongKd());
                            parentDtl.get().setSoLuongPhanBo(kh.getSoLuongPhanBo());
                            parentDtl.get().setSlDcConLai(kh.getSlDcConLai());
                            parentDtl.get().setDaXdinhDiemNhap(true);
                            dcnbKeHoachDcDtlRepository.save(parentDtl.get());
                        }
                        kh.setDaXdinhDiemNhap(true);
                        dcnbKeHoachDcDtlRepository.save(kh);
                    }
                }
                for (DcnbQuyetDinhDcCDtl hh : danhSachQuyetDinh) {
                    Optional<DcnbKeHoachDcHdr> hdr = dcnbKeHoachDcHdrRepository.findById(hh.getKeHoachDcHdrId());
                    Optional<DcnbKeHoachDcHdr> parentHdr = dcnbKeHoachDcHdrRepository.findById(hdr.get().getParentId());
                    if (parentHdr.isPresent()) {
                        boolean allMatch = parentHdr.get().getDanhSachHangHoa().stream().allMatch(n -> (n.getDaXdinhDiemNhap() != null && n.getDaXdinhDiemNhap() == true));
                        if(allMatch){
                            parentHdr.get().setDaXdinhDiemNhap(true);
                            dcnbKeHoachDcHdrRepository.save(parentHdr.get());
                        }
                    }
                }
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbQuyetDinhDcCHdr created = dcnbQuyetDinhDcCHdrRepository.save(optional.get());
        return created;
    }

    public DcnbQuyetDinhDcCHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbQuyetDinhDcCHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.DUTHAO + Contains.YC_CHICUC_PHANBO_DC:
                // xử lý clone cho chi cục với TYPE là NDC
                List<DcnbQuyetDinhDcCDtl> danhSachQuyetDinh = optional.get().getDanhSachQuyetDinh();
                List<DcnbKeHoachDcDtl> danhSachKeHoachs = new ArrayList<>();
                for(DcnbQuyetDinhDcCDtl qdd: danhSachQuyetDinh){
                    danhSachKeHoachs.addAll(qdd.getDanhSachKeHoach());
                }
                Map<String, List<DcnbKeHoachDcDtl>> groupedByMaCc = danhSachKeHoachs.stream()
                        .collect(Collectors.groupingBy(DcnbKeHoachDcDtl::getMaChiCucNhan));
                AtomicBoolean loiData = new AtomicBoolean(false);
                groupedByMaCc.forEach((maChiCucThue, khList) -> {
                    DcnbQuyetDinhDcCHdr clonedObj = SerializationUtils.clone(optional.get());
                    clonedObj.setParentId(clonedObj.getId());
                    clonedObj.setId(null);
                    clonedObj.setMaDvi(maChiCucThue);
                    clonedObj.setTenDvi(khList.get(0).getTenChiCucNhan());
                    clonedObj.setType(Contains.NHAN_DIEU_CHUYEN);
                    Map<Long, List<DcnbKeHoachDcDtl>> groupedByKhh = khList.stream()
                            .collect(Collectors.groupingBy(DcnbKeHoachDcDtl::getHdrId));
                    List<DcnbQuyetDinhDcCDtl> quyetDinhDcCDtlsClone = new ArrayList<>();

                    groupedByKhh.forEach((dcnbKeHoachDcHdrId, dcnbKeHoachDcDtlList) -> {
                        List<DcnbQuyetDinhDcCDtl> quyetDinhDcCDtls = danhSachQuyetDinh.stream().filter(item -> dcnbKeHoachDcHdrId.equals(item.getKeHoachDcHdrId())).map(itemMap -> {
                            DcnbQuyetDinhDcCDtl clonedDtl = SerializationUtils.clone(itemMap);
                            clonedDtl.setParentId(clonedDtl.getId());
                            clonedDtl.setId(null);
                            Optional<DcnbKeHoachDcHdr> keHoachDcHdrOpt = null;
                            try {
                                keHoachDcHdrOpt = Optional.ofNullable(dcnbKeHoachDcHdrService.details(dcnbKeHoachDcHdrId));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            DcnbKeHoachDcHdr keHoachHdrObj = SerializationUtils.clone(keHoachDcHdrOpt.get());
                            keHoachHdrObj.setParentId(keHoachHdrObj.getId());
                            keHoachHdrObj.setId(null);
                            keHoachHdrObj.setMaDviPq(maChiCucThue);
                            keHoachHdrObj.setType(Contains.NHAN_DIEU_CHUYEN);
                            keHoachHdrObj.setTrangThai(statusReq.getTrangThai());
                            keHoachHdrObj.setDanhSachHangHoa(keHoachHdrObj.getDanhSachHangHoa().stream()
                                    .filter(item -> item.getMaChiCucNhan().equals(maChiCucThue)).map(itemMap1 -> {
                                        itemMap1.setParentId(itemMap1.getId());
                                        itemMap1.setId(null);
                                        return itemMap1;
                                    }).collect(Collectors.toList()));
                            keHoachHdrObj.setPhuongAnDieuChuyen(keHoachHdrObj.getPhuongAnDieuChuyen().stream()
                                    .filter(item -> item.getMaChiCucNhan().equals(maChiCucThue)).map(itemMap1 -> {
                                        itemMap1.setParentId(itemMap1.getId());
                                        itemMap1.setId(null);
                                        return itemMap1;
                                    }).collect(Collectors.toList()));
                            keHoachHdrObj.setCanCu(keHoachHdrObj.getCanCu().stream().map(itemMap1 -> {
                                itemMap1.setId(null);
                                itemMap1.setDataId(null);
                                return itemMap1;
                            }).collect(Collectors.toList()));
                            BigDecimal total = keHoachHdrObj.getDanhSachHangHoa().stream().map(DcnbKeHoachDcDtl::getDuToanKphi)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                            keHoachHdrObj.setTongDuToanKp(total);

                            keHoachHdrObj = dcnbKeHoachDcHdrRepository.save(keHoachHdrObj);

                            clonedDtl.setDcnbKeHoachDcHdr(keHoachHdrObj);
                            clonedDtl.setKeHoachDcHdrId(keHoachHdrObj.getId());

                            fileDinhKemService.delete(keHoachHdrObj.getId(), Lists.newArrayList(DcnbKeHoachDcHdr.TABLE_NAME + "_CAN_CU"));
                            List<FileDinhKemReq> fileDinhKemReqs = keHoachHdrObj.getCanCu().stream()
                                    .map(person -> new ModelMapper().map(person, FileDinhKemReq.class))
                                    .collect(Collectors.toList());
                            List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(fileDinhKemReqs, keHoachHdrObj.getId(), DcnbKeHoachDcHdr.TABLE_NAME + "_CAN_CU");
                            keHoachHdrObj.setCanCu(canCu);

                            return clonedDtl;
                        }).collect(Collectors.toList());
                        if(quyetDinhDcCDtls.size() > 1){
                            loiData.set(true);
                        }else{
                            DcnbQuyetDinhDcCDtl quyetDinhDcCDtl = quyetDinhDcCDtls.get(0);
                            quyetDinhDcCDtlsClone.add(quyetDinhDcCDtl);
                        }
                    });
                    clonedObj.setDanhSachQuyetDinh(quyetDinhDcCDtlsClone);
                    clonedObj.setTrangThai(Contains.YC_CHICUC_PHANBO_DC);
                    dcnbQuyetDinhDcCHdrRepository.save(clonedObj);
                    fileDinhKemService.delete(clonedObj.getId(), Lists.newArrayList(DcnbQuyetDinhDcCHdr.TABLE_NAME + "_CAN_CU"));
                    List<FileDinhKemReq> fileDinhKemReqs = clonedObj.getCanCu().stream()
                            .map(person -> new ModelMapper().map(person, FileDinhKemReq.class))
                            .collect(Collectors.toList());
                    List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(fileDinhKemReqs, clonedObj.getId(), DcnbQuyetDinhDcCHdr.TABLE_NAME + "_CAN_CU");
                    clonedObj.setCanCu(canCu);
                });

                if(loiData.get()){
                    throw new Exception("Mỗi DcnbQuyetDinhDcCDtl chỉ có 1 dcnbKeHoachDcHdrId!");
                }
                break;
            case Contains.DUTHAO + Contains.CHODUYET_TP:
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_TP:
                optional.get().setNgayGduyet(LocalDate.now());
                optional.get().setNguoiGduyetId(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
                optional.get().setNgayPduyet(LocalDate.now());
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_TP + Contains.CHODUYET_LDC:
                optional.get().setNgayPduyet(LocalDate.now());
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_LDC + Contains.TUCHOI_LDC:
                optional.get().setNgayDuyetTc(LocalDate.now());
                optional.get().setNguoiDuyetTcId(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDC + Contains.BAN_HANH:
                optional.get().setNgayDuyetTc(LocalDate.now());
                optional.get().setNguoiDuyetTcId(currentUser.getUser().getId());
                Optional<DcnbQuyetDinhDcTcHdr> dcnbQuyetDinhDcTcHdr = Optional.empty();
                if(optional.get().getCanCuQdTc() != null){
                    dcnbQuyetDinhDcTcHdr = dcnbQuyetDinhDcTcHdrRepository.findById(optional.get().getCanCuQdTc());
                }
                if(dcnbQuyetDinhDcTcHdr.isPresent()){
                    if("00".equals(optional.get().getLoaiQdinh())){
                        String qdinhNhapCucId = dcnbQuyetDinhDcTcHdr.get().getQdinhNhapCucId();
                        if(qdinhNhapCucId == null){
                            qdinhNhapCucId = "";
                        }else {
                            qdinhNhapCucId+=","+optional.get().getId();
                        }
                        dcnbQuyetDinhDcTcHdr.get().setQdinhNhapCucId(qdinhNhapCucId);
                        String soQdinh = dcnbQuyetDinhDcTcHdr.get().getSoQdinhNhapCuc();
                        if(soQdinh == null){
                            soQdinh = "";
                        }else {
                            soQdinh+=","+optional.get().getSoQdinh();
                        }
                        dcnbQuyetDinhDcTcHdr.get().setSoQdinhNhapCuc(soQdinh);
                    }
                    if("01".equals(optional.get().getLoaiQdinh())){
                        String qdinhXuatCucId = dcnbQuyetDinhDcTcHdr.get().getQdinhXuatCucId();
                        if(qdinhXuatCucId == null){
                            qdinhXuatCucId = "";
                        }else {
                            qdinhXuatCucId+=","+optional.get().getId();
                        }
                        dcnbQuyetDinhDcTcHdr.get().setQdinhXuatCucId(qdinhXuatCucId);
                        String soQdinh = dcnbQuyetDinhDcTcHdr.get().getSoQdinhXuatCuc();
                        if(soQdinh == null){
                            soQdinh = "";
                        }else {
                            soQdinh+=","+optional.get().getSoQdinh();
                        }
                        dcnbQuyetDinhDcTcHdr.get().setSoQdinhXuatCuc(soQdinh);
                    }
                }
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbQuyetDinhDcCHdr created = dcnbQuyetDinhDcCHdrRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, SearchDcnbQuyetDinhDcC objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<DcnbQuyetDinhDcCHdr> page = this.searchPage(currentUser, objReq);
        List<DcnbQuyetDinhDcCHdr> data = page.getContent();

        String title = "Danh sách phương án xuất cứu trợ, viện trợ ";
        String[] rowsName = new String[]{"STT", "Năm kH", "Số công văn/đề xuất", "Ngày duyệt LĐ Cục", "Loại điều chuyển", "Đơn vị đề xuất", "Trạng thái",};
        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbQuyetDinhDcCHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getNam();
            objs[2] = dx.getSoQdinh();
            objs[3] = dx.getNgayDuyetTc();
            objs[4] = dx.getLoaiDc();
            objs[5] = dx.getTenDvi();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}
