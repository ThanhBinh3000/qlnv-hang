package com.tcdt.qlnvhang.service.dieuchuyennoibo;


import com.tcdt.qlnvhang.enums.LoaiDieuChuyenKeHoach;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.THKeHoachDieuChuyenCucHdrRepository;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.ThKeHoachDieuChuyenCucHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.ThKeHoachDieuChuyenNoiBoCucDtlReq;
import com.tcdt.qlnvhang.table.TongHopKeHoachDieuChuyen.THKeHoachDieuChuyenCucHdr;
import com.tcdt.qlnvhang.table.TongHopKeHoachDieuChuyen.THKeHoachDieuChuyenNoiBoCucDtl;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbKeHoachDcDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.THKeHoachDieuChuyenNoiBoCucDtlRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.search.TongHopKeHoachDieuChuyenSearch;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.modelmapper.ModelMapper;
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
import javax.xml.bind.ValidationException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class THKeHoachDieuChuyenService extends BaseServiceImpl {

    @Autowired
    private THKeHoachDieuChuyenCucHdrRepository thKeHoachDieuChuyenHdrRepository;

    @Autowired
    private THKeHoachDieuChuyenNoiBoCucDtlRepository thKeHoachDieuChuyenNoiBoCucDtlRepository;

    @Autowired
    private DcnbKeHoachDcDtlRepository dcnbKeHoachDcDtlRepository;

    @Autowired
    private QlnvDmDonviRepository qlnvDmDonviRepository;



    public Page<THKeHoachDieuChuyenCucHdr> searchPage(CustomUserDetails currentUser, TongHopKeHoachDieuChuyenSearch req) throws Exception {
        req.setMaDonVi(currentUser.getDvql());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<THKeHoachDieuChuyenCucHdr> search = thKeHoachDieuChuyenHdrRepository.search(req, pageable);
        return search;
    }

    @Transactional
    public THKeHoachDieuChuyenCucHdr save(CustomUserDetails currentUser, ThKeHoachDieuChuyenCucHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new ValidationException("Bad request.");
        }
        Optional<THKeHoachDieuChuyenCucHdr> optional = thKeHoachDieuChuyenHdrRepository.findByMaTongHop(objReq.getMaTongHop());
        if (optional.isPresent() && objReq.getMaTongHop().split("/").length == 1) {
            throw new ValidationException("Mã tổng hợp đã tồn tại");
        }
        THKeHoachDieuChuyenCucHdr data = new THKeHoachDieuChuyenCucHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getUser().getDvql());
        data.setTenDvi(currentUser.getUser().getTenDvi());
        data.setTrangThai(Contains.DUTHAO);
        data.setLoaiDieuChuyen(data.getLoaiDieuChuyen());
        data.setNguoiTaoId(currentUser.getUser().getId());
        data.setNgaytao(new Date());

        List<THKeHoachDieuChuyenNoiBoCucDtl> chiTiet = new ArrayList<>();
        TongHopKeHoachDieuChuyenSearch tongHopSearch = new ModelMapper().map(objReq, TongHopKeHoachDieuChuyenSearch.class);
        objReq.setCtTongHopKeHoachDieuChuyen(createPlan(currentUser,tongHopSearch));
        if (objReq.getCtTongHopKeHoachDieuChuyen() != null && !objReq.getCtTongHopKeHoachDieuChuyen().isEmpty()) {
            for (ThKeHoachDieuChuyenNoiBoCucDtlReq ct : objReq.getCtTongHopKeHoachDieuChuyen()) {
                THKeHoachDieuChuyenNoiBoCucDtl ctTongHop = new ModelMapper().map(ct, THKeHoachDieuChuyenNoiBoCucDtl.class);
                chiTiet.add(ctTongHop);
            }
        }
        THKeHoachDieuChuyenCucHdr created = thKeHoachDieuChuyenHdrRepository.save(data);
        if (!chiTiet.isEmpty()) {
            for (THKeHoachDieuChuyenNoiBoCucDtl ct : chiTiet) {
                ct.setHdrId(created.getId());
            }
        }
        return created;
    }


    public List<THKeHoachDieuChuyenCucHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new ValidationException("Tham số không hợp lệ.");
        List<THKeHoachDieuChuyenCucHdr> optional = thKeHoachDieuChuyenHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new ValidationException("Không tìm thấy dữ liệu");
        }
        List<THKeHoachDieuChuyenCucHdr> allById = thKeHoachDieuChuyenHdrRepository.findAllById(ids);
        allById.forEach(data -> {
            List<THKeHoachDieuChuyenNoiBoCucDtl> list = thKeHoachDieuChuyenNoiBoCucDtlRepository.findByHdrId(data.getId());
            data.setThKeHoachDieuChuyenNoiBoCucDtls(list);
            list.forEach(data1 -> {
                List<DcnbKeHoachDcDtl> list1 = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrId(data1.getHdrId());
                data1.setDcnbKeHoachDcDtlList(list1);
            });
        });
        return allById;
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<THKeHoachDieuChuyenCucHdr> optional = thKeHoachDieuChuyenHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        THKeHoachDieuChuyenCucHdr data = optional.get();
        List<THKeHoachDieuChuyenNoiBoCucDtl> list = thKeHoachDieuChuyenNoiBoCucDtlRepository.findByHdrId(data.getId());
        thKeHoachDieuChuyenNoiBoCucDtlRepository.deleteAll(list);
        thKeHoachDieuChuyenHdrRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<THKeHoachDieuChuyenCucHdr> list = thKeHoachDieuChuyenHdrRepository.findAllById(idSearchReq.getIds());

        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listId = list.stream().map(THKeHoachDieuChuyenCucHdr::getId).collect(Collectors.toList());
        List<THKeHoachDieuChuyenNoiBoCucDtl> listTongHopKeHoach = thKeHoachDieuChuyenNoiBoCucDtlRepository.findAllByIdIn(listId);
        thKeHoachDieuChuyenNoiBoCucDtlRepository.deleteAll(listTongHopKeHoach);
        thKeHoachDieuChuyenHdrRepository.deleteAll(list);
    }

    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<THKeHoachDieuChuyenCucHdr> optional = thKeHoachDieuChuyenHdrRepository.findById(statusReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
            this.approveTongHop(currentUser, statusReq, optional); // Truyền giá trị của optional vào

    }

    @Transactional
    public THKeHoachDieuChuyenCucHdr update(CustomUserDetails currentUser, ThKeHoachDieuChuyenCucHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<THKeHoachDieuChuyenCucHdr> optional = thKeHoachDieuChuyenHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Optional<THKeHoachDieuChuyenCucHdr> maTongHop = thKeHoachDieuChuyenHdrRepository.findByMaTongHop(optional.get().getMaTongHop());
        if (maTongHop.isPresent() && objReq.getMaTongHop().split("/").length == 1) {
            if (!maTongHop.get().getId().equals(objReq.getId())) {
                throw new Exception("số đề xuất đã tồn tại");
            }
        }
        THKeHoachDieuChuyenCucHdr data = optional.get();
        data.setNguoiSuaId(currentUser.getUser().getId());
        data.setNgaySua(new Date());
        THKeHoachDieuChuyenCucHdr created = thKeHoachDieuChuyenHdrRepository.save(data);
        thKeHoachDieuChuyenHdrRepository.save(created);
        return created;
    }
    @Transactional
    public THKeHoachDieuChuyenCucHdr approveTongHop(CustomUserDetails currentUser, StatusReq statusReq, Optional<THKeHoachDieuChuyenCucHdr> optional) throws Exception {
        String status = statusReq.getTrangThai() + optional.get().getTrangThai();
        switch (status) {
            case  Contains.DUTHAO + Contains.CHODUYET_TP:
                optional.get().setNguoiTaoId(currentUser.getUser().getId());
                optional.get().setNgayGDuyet(new Date());
                break;
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:

            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
                optional.get().setNguoiDuyetLdcId(currentUser.getUser().getId());
                optional.get().setNgayDuyetLdc(new Date());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
                optional.get().setNguoiDuyetLdcId(currentUser.getUser().getId());
                optional.get().setNgayDuyetLdc(new Date());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        THKeHoachDieuChuyenCucHdr created = thKeHoachDieuChuyenHdrRepository.save(optional.get());
        return created;
    }

    @Transactional
    public void export(CustomUserDetails currentUser, TongHopKeHoachDieuChuyenSearch objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<THKeHoachDieuChuyenCucHdr> page = this.searchPage(currentUser, objReq);
        List<THKeHoachDieuChuyenCucHdr> data = page.getContent();

        String title = "Danh sách phương án xuất cứu trợ, viện trợ ";
        String[] rowsName = new String[]{"STT", "Năm kH", "Số công văn/đề xuất", "Ngày duyệt LĐ Cục", "Loại điều chuyển", "Đơn vị đề xuất", "Trạng thái",};
        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            THKeHoachDieuChuyenCucHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getNamKeHoach();
            objs[2] = dx.getMaTongHop();
            objs[3] = dx.getNgayDuyetLdc();
            objs[4] = dx.getLoaiDieuChuyen();
            objs[5] = dx.getTenDvi();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    @Transactional
    public List<ThKeHoachDieuChuyenNoiBoCucDtlReq> createPlan(CustomUserDetails currentUser, TongHopKeHoachDieuChuyenSearch req) throws Exception{

        Optional<THKeHoachDieuChuyenCucHdr> optional = thKeHoachDieuChuyenHdrRepository.findByMaTongHop(req.getMaTongHop());
        if (optional.isPresent()) {
            throw new Exception("Mã tổng hợp đã tồn tại");
        }

        List<QlnvDmDonvi> donvis = qlnvDmDonviRepository.findByMaDviChaAndTrangThai(currentUser.getDvql(),"01");
        QlnvDmDonvi qlnvDmDonvi = qlnvDmDonviRepository.findByMaDvi(currentUser.getDvql());
        if (donvis != null) {
            donvis.add(qlnvDmDonvi);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-DD");
        List<ThKeHoachDieuChuyenNoiBoCucDtlReq> result = new ArrayList<>();
        for (QlnvDmDonvi cqt : donvis) {
            if (Objects.equals(req.getLoaiDieuChuyen(), LoaiDieuChuyenKeHoach.GIUA_2_CHI_CUC_TRONG_1_CUC.getId())){
                List<DcnbKeHoachDcDtl> thKeHoachDieuChuyenNoiBoCucDtls = dcnbKeHoachDcDtlRepository.findByDonViChaAndTrangThai(cqt.getMaDvi(), TrangThaiAllEnum.DA_DUYET_LDC.getId(), formatter.format(req.getThoiGianTongHop()));
                ThKeHoachDieuChuyenNoiBoCucDtlReq chiTiet = new ThKeHoachDieuChuyenNoiBoCucDtlReq();
                chiTiet.setKeHoachDcDtlId(thKeHoachDieuChuyenNoiBoCucDtls.get(0).getId() == null ? null :thKeHoachDieuChuyenNoiBoCucDtls.get(0).getId());
                Optional<DcnbKeHoachDcDtl> dcnbKeHoachDcDtlList = dcnbKeHoachDcDtlRepository.findById(chiTiet.getId());
                result.add(chiTiet);
            }
            else if (Objects.equals(req.getLoaiDieuChuyen(), LoaiDieuChuyenKeHoach.GIUA_2_CUC_DTNN_KV)) {
                List<DcnbKeHoachDcDtl> thKeHoachDieuChuyenKhacCucDtls = dcnbKeHoachDcDtlRepository.findByDonViChaAndTrangThai(currentUser.getDvql(), TrangThaiAllEnum.DA_DUYET_LDC.getId(), formatter.format(req.getThoiGianTongHop()));
                ThKeHoachDieuChuyenNoiBoCucDtlReq chiTiet = new ThKeHoachDieuChuyenNoiBoCucDtlReq();
//                chiTiet.setDcnbKeHoachDcDtlList(thKeHoachDieuChuyenKhacCucDtls);
                result.add(chiTiet);
            } else if (Objects.equals(req.getLoaiDieuChuyen(), LoaiDieuChuyenKeHoach.TAT_CA)) {
                List<DcnbKeHoachDcDtl> thKeHoachDieuChuyenNoiBoCucDtls = dcnbKeHoachDcDtlRepository.findByDonViChaAndTrangThai(cqt.getMaDvi(), TrangThaiAllEnum.DA_DUYET_LDC.getId(), formatter.format(req.getThoiGianTongHop()));
                ThKeHoachDieuChuyenNoiBoCucDtlReq chiTiet = new ThKeHoachDieuChuyenNoiBoCucDtlReq();
//                chiTiet.setChiTietKeHoachDieuChuyen(thKeHoachDieuChuyenNoiBoCucDtls);

                List<DcnbKeHoachDcDtl> thKeHoachDieuChuyenKhacCucDtls = dcnbKeHoachDcDtlRepository.findByDonViChaAndTrangThai(cqt.getMaDvi(), TrangThaiAllEnum.DA_DUYET_LDC.getId(), formatter.format(req.getThoiGianTongHop()));

//                chiTiet.setChiTietKeHoachDieuChuyen(thKeHoachDieuChuyenKhacCucDtls);
                result.add(chiTiet);
            }

        }
        return result;
    }
}
