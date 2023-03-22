package com.tcdt.qlnvhang.service.xuathang.bantructiep.nhiemvuxuat;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.hopdong.XhHopDongBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDvi;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bangkecanhang.XhBkeCanHangBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBtt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.hopdong.XhHopDongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.bienbanlaymau.XhBbLayMauBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDviRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.tochuctrienkhai.ketqua.XhKqBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.bangcankehang.XhBkeCanHangBttHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.xuatkho.phieuxuatkho.XhPhieuXkhoBttReposytory;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttDviReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat.XhQdNvXhBttHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class XhQdNvXhBttServiceImpI extends BaseServiceImpl implements XhQdNvXhBttService {

    @Autowired
    private XhQdNvXhBttHdrRepository xhQdNvXhBttHdrRepository;

    @Autowired
    private XhQdNvXhBttDtlRepository xhQdNvXhBttDtlRepository;

    @Autowired
    private XhQdNvXhBttDviRepository xhQdNvXhBttDviRepository;

    @Autowired
    FileDinhKemService fileDinhKemService;

    @Autowired
    private XhHopDongBttHdrRepository xhHopDongBttHdrRepository;

    @Autowired
    private XhQdPdKhBttHdrRepository xhQdPdKhBttHdrRepository;

    @Autowired
    private XhKqBttHdrRepository xhKqBttHdrRepository;

    @Autowired
    private XhBbLayMauBttHdrRepository xhBbLayMauBttHdrRepository;

    @Autowired
    private XhPhieuKtraCluongBttHdrRepository xhPhieuKtraCluongBttHdrRepository;

    @Autowired
    private XhPhieuXkhoBttReposytory xhPhieuXkhoBttReposytory;

    @Autowired
    private XhBkeCanHangBttHdrRepository xhBkeCanHangBttHdrRepository;


    @Override
    public Page<XhQdNvXhBttHdr> searchPage(XhQdNvXhBttHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhQdNvXhBttHdr> data = xhQdNvXhBttHdrRepository.searchPage(
                req,
                pageable
        );
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");

        data.getContent().forEach(f ->{
            // Set biên bản lấy mẫu
            List<XhBbLayMauBttHdr> allByIdQd = xhBbLayMauBttHdrRepository.findAllByIdQd(f.getId());
            allByIdQd.forEach(x ->{
                x.setTenLoaiVthh(hashMapVthh.get(x.getLoaiVthh()));
                x.setTenCloaiVthh(hashMapVthh.get(x.getCloaiVthh()));
                x.setTenDiemKho(hashMapDvi.get(x.getMaDiemKho()));
                x.setTenNhaKho(hashMapDvi.get(x.getMaNganKho()));
                x.setTenNganKho(hashMapDvi.get(x.getMaNganKho()));
                x.setTenLoKho(hashMapDvi.get(x.getMaLoKho()));
            });
            f.setXhBbLayMauBttHdrList(allByIdQd);

            // Set kiểm tra chất lượng
            List<XhPhieuKtraCluongBttHdr> xhPhieuKtraCluongBttHdrList = xhPhieuKtraCluongBttHdrRepository.findAllByIdQd(f.getId());
            xhPhieuKtraCluongBttHdrList.forEach(s ->{
                s.setTenLoaiVthh(hashMapVthh.get(s.getLoaiVthh()));
                s.setTenCloaiVthh(hashMapVthh.get(s.getCloaiVthh()));
                s.setTenDiemKho(hashMapDvi.get(s.getMaDiemKho()));
                s.setTenNhaKho(hashMapDvi.get(s.getMaNganKho()));
                s.setTenNganKho(hashMapDvi.get(s.getMaNganKho()));
                s.setTenLoKho(hashMapDvi.get(s.getMaLoKho()));
            });
            f.setXhPhieuKtraCluongBttHdrList(xhPhieuKtraCluongBttHdrList);

            // Phiếu xuất kho
            List<XhPhieuXkhoBtt> xhPhieuXkhoBttList = xhPhieuXkhoBttReposytory.findAllByIdQd(f.getId());
            xhPhieuXkhoBttList.forEach(a ->{
                a.setTenLoaiVthh(hashMapVthh.get(a.getLoaiVthh()));
                a.setTenCloaiVthh(hashMapVthh.get(a.getCloaiVthh()));
                a.setTenDiemKho(hashMapDvi.get(a.getMaDiemKho()));
                a.setTenNhaKho(hashMapDvi.get(a.getMaNganKho()));
                a.setTenNganKho(hashMapDvi.get(a.getMaNganKho()));
                a.setTenLoKho(hashMapDvi.get(a.getMaLoKho()));
            });
            f.setXhPhieuXkhoBttList(xhPhieuXkhoBttList);

            // Bảng kê cân hàng
            List<XhBkeCanHangBttHdr> xhBkeCanHangBttHdrList = xhBkeCanHangBttHdrRepository.findAllByIdQd(f.getId());
            xhBkeCanHangBttHdrList.forEach( b ->{
                b.setTenLoaiVthh(hashMapVthh.get(b.getLoaiVthh()));
                b.setTenCloaiVthh(hashMapVthh.get(b.getCloaiVthh()));
                b.setTenDiemKho(hashMapDvi.get(b.getMaDiemKho()));
                b.setTenNhaKho(hashMapDvi.get(b.getMaNganKho()));
                b.setTenNganKho(hashMapDvi.get(b.getMaNganKho()));
                b.setTenLoKho(hashMapDvi.get(b.getMaLoKho()));
            });
            f.setXhBkeCanHangBttHdrList(xhBkeCanHangBttHdrList);

            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenTrangThaiXh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiXh()));
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : hashMapDvi.get(f.getMaDvi()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapVthh.get(f.getCloaiVthh()));
        });
        return data;
    }

    @Override
    public XhQdNvXhBttHdr create(XhQdNvXhBttHdrReq req) throws Exception {

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        if (!StringUtils.isEmpty(req.getSoQd())){
            List<XhQdNvXhBttHdr> checkSoQd = xhQdNvXhBttHdrRepository.findBySoQd(req.getSoQd());
            if (!checkSoQd.isEmpty()){
                throw new Exception("Số quyết định" + req.getSoQd() + "đã tồn tại");
            }
        }

        XhHopDongBttHdr dataHd = new XhHopDongBttHdr();
        XhKqBttHdr dataQdPdKq = new XhKqBttHdr();
        if (req.getPhanLoai().equals("HD")){
            Optional<XhHopDongBttHdr> qOptionalHd = xhHopDongBttHdrRepository.findById(req.getIdHd());
            if (!qOptionalHd.isPresent()){
                throw new Exception("Không tìm thấy hợp đồng bán trực tiếp");
            }
            dataHd = qOptionalHd.get();
        }else {
            Optional<XhKqBttHdr> qOptionalQdPdKh = xhKqBttHdrRepository.findById(req.getIdQdKqCg());
            if (qOptionalQdPdKh.isPresent()){
                Optional<XhQdPdKhBttHdr> xhQdPdKhBttHdr = xhQdPdKhBttHdrRepository.findById(qOptionalQdPdKh.get().getIdPdKhHdr());
                if (!xhQdPdKhBttHdr.isPresent()){
                    throw new Exception("Không tìm thấy quyết định phê duyệt kế hoạch bán trực tiếp");
                }
            }
            dataQdPdKq = qOptionalQdPdKh.get();
        }

        XhQdNvXhBttHdr  dataMap = new XhQdNvXhBttHdr();
        BeanUtils.copyProperties(req, dataMap, "id");
        dataMap.setNgayTao(getDateTimeNow());
        dataMap.setNguoiTaoId(getUser().getId());
        dataMap.setTrangThai(Contains.DUTHAO);
        dataMap.setTrangThaiXh(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId());
        dataMap.setMaDvi(userInfo.getDvql());
        if(req.getPhanLoai().equals("QDDX")){
            dataMap.setMaDviTsan(String.join(",",req.getListMaDviTsan()));
        }
        XhQdNvXhBttHdr created = xhQdNvXhBttHdrRepository.save(dataMap);

        if (!DataUtils.isNullObject(req.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Collections.singletonList(req.getFileDinhKem()), created.getId(), XhQdNvXhBttHdr.TABLE_NAME);
            created.setFileDinhKem(fileDinhKem.get(0));
        }
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhQdNvXhBttHdr.TABLE_NAME);
            created.setFileDinhKems(fileDinhKems);
        }

        if (req.getPhanLoai().equals("HD")){
            dataHd.setTrangThaiXh(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId());
            dataHd.setSoQd(dataMap.getSoQd());
            xhHopDongBttHdrRepository.save(dataHd);
        }else {
            dataQdPdKq.setTrangThaiXh(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId());
            xhKqBttHdrRepository.save(dataQdPdKq);
        }

        this.saveDetail(req, dataMap.getId());
        return created;
    }

    public void saveDetail(XhQdNvXhBttHdrReq req, Long idHdr){
        xhQdNvXhBttDtlRepository.deleteAllByIdQdHdr(idHdr);
        for (XhQdNvXhBttDtlReq dtlReq : req.getChildren()){
            XhQdNvXhBttDtl dtl = new XhQdNvXhBttDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdQdHdr(idHdr);
            xhQdNvXhBttDtlRepository.save(dtl);
            xhQdNvXhBttDviRepository.deleteAllByIdDtl(dtlReq.getId());
            for (XhQdNvXhBttDviReq dviReq : dtlReq.getChildren()){
                XhQdNvXhBttDvi dvi = new XhQdNvXhBttDvi();
                BeanUtils.copyProperties(dviReq, dvi, "id");
                dvi.setId(null);
                dvi.setIdDtl(dtl.getId());
                xhQdNvXhBttDviRepository.save(dvi);
            }
        }
    }

    @Override
    public XhQdNvXhBttHdr update(XhQdNvXhBttHdrReq req) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Sửa thất bại không tìm thấy dữ liệu ");
        }

        Optional<XhQdNvXhBttHdr> optional = xhQdNvXhBttHdrRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }

        if (req.getPhanLoai().equals("HD")){
            Optional<XhHopDongBttHdr> qOptionalHd = xhHopDongBttHdrRepository.findById(req.getIdHd());
            if (!qOptionalHd.isPresent()){
                throw new Exception("Không tìm thấy hợp đồng bán trực tiếp");
            }
        }else {
            Optional<XhQdPdKhBttHdr> qOptionalQdPdKh = xhQdPdKhBttHdrRepository.findById(req.getIdQdPdKh());
            if (!qOptionalQdPdKh.isPresent()) {
                throw new Exception("Không tìm thấy quyết định phê duyệt kế hoạch bán trực tiếp");
            }
        }

        XhQdNvXhBttHdr dataDB = optional.get();
        BeanUtils.copyProperties(req, dataDB, "id");
        dataDB.setNgaySua(getDateTimeNow());
        dataDB.setNguoiSuaId(getUser().getId());
        XhQdNvXhBttHdr created = xhQdNvXhBttHdrRepository.save(dataDB);

        if (!DataUtils.isNullObject(req.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Arrays.asList(req.getFileDinhKem()), created.getId(), XhQdNvXhBttHdr.TABLE_NAME);
            dataDB.setFileDinhKem(fileDinhKem.get(0));
        }

        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhQdNvXhBttHdr.TABLE_NAME);
            dataDB.setFileDinhKems(fileDinhKems);
        }

        this.saveDetail(req, dataDB.getId());
        return created;
    }

    @Override
    public XhQdNvXhBttHdr detail(Long id) throws Exception {
       Optional<XhQdNvXhBttHdr> optional = xhQdNvXhBttHdrRepository.findById(id);
       if (!optional.isPresent()){
           throw new Exception("Bản ghi không tồn tại");
       }

        XhQdNvXhBttHdr data = optional.get();

        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");

        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenDvi(hashMapDvi.get(data.getMaDvi()));
        data.setTenLoaiVthh(hashMapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(hashMapVthh.get(data.getCloaiVthh()));

        if(data.getPhanLoai().equals("QDDX")){
            data.setListMaDviTsan(Arrays.asList(data.getMaDviTsan().split(",")));
        }

        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhQdNvXhBttHdr.TABLE_NAME));
        if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
            data.setFileDinhKem(fileDinhKem.get(0));
        }
        data.setFileDinhKems(fileDinhKem);

        List<XhPhieuXkhoBtt> phieuXkhoBttList = xhPhieuXkhoBttReposytory.findAllByIdQd(data.getId());
        phieuXkhoBttList.forEach(f->{
            f.setTenDiemKho(hashMapDvi.get(f.getMaDiemKho()));
            f.setTenNhaKho(hashMapDvi.get(f.getMaNhaKho()));
            f.setTenNganKho(hashMapDvi.get(f.getMaNganKho()));
            f.setTenLoKho(hashMapDvi.get(f.getMaLoKho()));
            f.setTenLoaiVthh(hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(hashMapVthh.get(f.getCloaiVthh()));
        });
        data.setXhPhieuXkhoBttList(phieuXkhoBttList);

        List<XhQdNvXhBttDtl> dtlList  = xhQdNvXhBttDtlRepository.findAllByIdQdHdr(data.getId());
        for (XhQdNvXhBttDtl dtl : dtlList){
            List<XhQdNvXhBttDvi> dviList = xhQdNvXhBttDviRepository.findAllByIdDtl(dtl.getId());
            dviList.forEach(f->{
                f.setTenDiemKho(hashMapDvi.get(f.getMaDiemKho()));
                f.setTenNhaKho(hashMapDvi.get(f.getMaNhaKho()));
                f.setTenNganKho(hashMapDvi.get(f.getMaNganKho()));
                f.setTenLoKho(hashMapDvi.get(f.getMaLoKho()));
            });
            dtl.setTenDvi(hashMapDvi.get(dtl.getMaDvi()));
            dtl.setChildren(dviList);
        }
        data.setChildren(dtlList);
        return  data;
    }

    @Override
    public XhQdNvXhBttHdr approve(XhQdNvXhBttHdrReq req) throws Exception {
       UserInfo userInfo = SecurityContextService.getUser();

       if (userInfo == null){
           throw new Exception("Bad request. ");
       }

        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        Optional<XhQdNvXhBttHdr> optional = xhQdNvXhBttHdrRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        XhQdNvXhBttHdr data = optional.get();

        String status = req.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                data.setNguoiGuiDuyetId(userInfo.getId());
                data.setNgayGuiDuyet(getDateTimeNow());
                break;
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
            case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(getDateTimeNow());
                data.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.BAN_HANH + Contains.CHODUYET_LDC:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(req.getTrangThai());
        xhQdNvXhBttHdrRepository.save(data);
        return data;
    }

    @Override
    public void delete(Long id) throws Exception {
        if (StringUtils.isEmpty(id)){
            throw new Exception("Xóa thất bại không tìm thấy dữ liệu");
        }

        Optional<XhQdNvXhBttHdr> optional = xhQdNvXhBttHdrRepository.findById(id);
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        XhQdNvXhBttHdr hdr = optional.get();

        if (!hdr.getTrangThai().equals(Contains.DUTHAO)
                && !hdr.getTrangThai().equals(Contains.TU_CHOI_TP)
                && !hdr.getTrangThai().equals(Contains.TUCHOI_LDC)) {
            throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối ");
        }

        List<XhQdNvXhBttDtl> dtlList = xhQdNvXhBttDtlRepository.findAllByIdQdHdr(id);
        for (XhQdNvXhBttDtl dtl : dtlList){
            xhQdNvXhBttDviRepository.deleteAllByIdDtl(dtl.getId());
        }
        xhQdNvXhBttDtlRepository.deleteAllByIdQdHdr(id);
        xhQdNvXhBttHdrRepository.delete(hdr);
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhQdNvXhBttHdr.TABLE_NAME));

    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (StringUtils.isEmpty(listMulti)){
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
        }

        List<XhQdNvXhBttHdr> list = xhQdNvXhBttHdrRepository.findByIdIn(listMulti);
        if (list.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }

        for (XhQdNvXhBttHdr hdr : list){
            this.delete(hdr.getId());
        }
    }

    @Override
    public void export(XhQdNvXhBttHdrReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhQdNvXhBttHdr> page = this.searchPage(req);
        List<XhQdNvXhBttHdr> data = page.getContent();

        String title="Danh sách quyết định giao nhiệm vụ xuất hàng";
        String[] rowsName = new String[]{"STT","Năm xuất", "Số quyết định", "Ngày quyết định", "Số hợp đồng", "Loại hàng hóa", "Chủng loại hành hóa", "Thời gian giao nhận hàng", "Trích yếu quyết định", "Số BB tịnh kho", "Số BB hao dôi", "Trạng thái QĐ", "Trạng thái XH"};
        String filename="danh-sach-dx-kh-ban-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i = 0; i < data.size(); i++) {
            XhQdNvXhBttHdr hdr = data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=hdr.getNamKh();
            objs[2]=hdr.getSoQd();
            objs[3]=hdr.getNgayTao();
            objs[4]=hdr.getSoHd();
            objs[5]=hdr.getTenLoaiVthh();
            objs[6]=hdr.getTenCloaiVthh();
            objs[7]=hdr.getTgianGnhan();
            objs[8]=hdr.getTrichYeu();
            objs[9] = null;
            objs[10]= null;
            objs[11]=hdr.getTenTrangThai();
            objs[12]=hdr.getTenTrangThaiXh();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }
}
