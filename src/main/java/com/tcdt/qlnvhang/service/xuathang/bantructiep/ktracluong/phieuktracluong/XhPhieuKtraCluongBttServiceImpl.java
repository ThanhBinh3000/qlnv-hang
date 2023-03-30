package com.tcdt.qlnvhang.service.xuathang.bantructiep.ktracluong.phieuktracluong;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdrRepository;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.phieuktracluong.XhPhieuKtraCluongBttHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.UserUtils;
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
public class XhPhieuKtraCluongBttServiceImpl extends BaseServiceImpl implements XhPhieuKtraCluongBttService {

    @Autowired
    private XhPhieuKtraCluongBttHdrRepository xhPhieuKtraCluongBttHdrRepository;

    @Autowired
    private XhPhieuKtraCluongBttDtlRepository xhPhieuKtraCluongBttDtlRepository;

    @Autowired
    FileDinhKemService fileDinhKemService;

    @Autowired
    UserInfoRepository userInfoRepository;


    @Override
    public Page<XhPhieuKtraCluongBttHdr> searchPage(XhPhieuKtraCluongBttHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhPhieuKtraCluongBttHdr> data = xhPhieuKtraCluongBttHdrRepository.searchPage(
                req,
                pageable
        );
        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        data.getContent().forEach(f ->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : hashMapDvi.get(f.getMaDvi()));
            f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho()) ? null : hashMapDvi.get(f.getMaDiemKho()));
            f.setTenNhaKho(StringUtils.isEmpty(f.getMaNhaKho()) ? null : hashMapDvi.get(f.getMaNhaKho()));
            f.setTenNganKho(StringUtils.isEmpty(f.getMaNganKho()) ? null : hashMapDvi.get(f.getMaNganKho()));
            f.setTenTrangThai(StringUtils.isEmpty(f.getMaLoKho()) ? null : hashMapDvi.get(f.getMaLoKho()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapVthh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapVthh.get(f.getCloaiVthh()));
        });
        return data;
    }

    @Override
    public XhPhieuKtraCluongBttHdr create(XhPhieuKtraCluongBttHdrReq req) throws Exception {

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        if (StringUtils.isEmpty(req.getSoPhieu())){
            Optional<XhPhieuKtraCluongBttHdr> qOptional = xhPhieuKtraCluongBttHdrRepository.findBySoPhieu(req.getSoPhieu());
            if (qOptional.isPresent()){
                throw new Exception("Số Phiếu " + req.getSoPhieu() + "đã tồn tại");
            }
        }

        XhPhieuKtraCluongBttHdr data = new XhPhieuKtraCluongBttHdr();
        BeanUtils.copyProperties(req, data,"id");
        data.setNgayTao(getDateTimeNow());
        data.setNguoiTaoId(userInfo.getId());
        data.setTrangThai(Contains.DUTHAO);
        data.setId(Long.parseLong(data.getSoPhieu().split("/")[0]));
        data.setIdNgKnghiem(userInfo.getId());
        data.setIdTruongPhong(userInfo.getId());
        XhPhieuKtraCluongBttHdr created = xhPhieuKtraCluongBttHdrRepository.save(data);

        if (!DataUtils.isNullObject(req.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Collections.singletonList(req.getFileDinhKem()), created.getId(), XhPhieuKtraCluongBttHdr.TABLE_NAME);
            created.setFileDinhKem(fileDinhKem.get(0));
        }

        saveDetail(req, data.getId());
        return created;
    }

    void saveDetail(XhPhieuKtraCluongBttHdrReq req, Long idHdr){
        xhPhieuKtraCluongBttDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhPhieuKtraCluongBttDtlReq dtlReq : req.getChildren()){
            XhPhieuKtraCluongBttDtl dtl = new XhPhieuKtraCluongBttDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            xhPhieuKtraCluongBttDtlRepository.save(dtl);
        }
    }

    @Override
    public XhPhieuKtraCluongBttHdr update(XhPhieuKtraCluongBttHdrReq req) throws Exception {
        if(Objects.isNull(req)){
            throw new Exception("Bad reqeust");
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Sủa thất bại, không tìm thấy dữ liệu");
        }

        Optional<XhPhieuKtraCluongBttHdr> qOptional = xhPhieuKtraCluongBttHdrRepository.findById(req.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }

        XhPhieuKtraCluongBttHdr dataDB = qOptional.get();
        BeanUtils.copyProperties(req, dataDB, "id");
        dataDB.setNgaySua(getDateTimeNow());
        dataDB.setNguoiSuaId(userInfo.getId());
        XhPhieuKtraCluongBttHdr created = xhPhieuKtraCluongBttHdrRepository.save(dataDB);

        if (!DataUtils.isNullObject(req.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Arrays.asList(req.getFileDinhKem()), created.getId(), XhPhieuKtraCluongBttHdr.TABLE_NAME);
            dataDB.setFileDinhKem(fileDinhKem.get(0));
        }

        this.saveDetail(req, dataDB.getId());
        return created;

    }

    @Override
    public XhPhieuKtraCluongBttHdr detail(Long id) throws Exception {
        if (StringUtils.isEmpty(id))
            throw new Exception("Không tồn tại bản ghi");

        Optional<XhPhieuKtraCluongBttHdr> qOptional = xhPhieuKtraCluongBttHdrRepository.findById(id);

        if (!qOptional.isPresent()){
            throw new UnsupportedOperationException("Bản ghi không tồn tại");
        }

        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");

        XhPhieuKtraCluongBttHdr data = qOptional.get();

        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhPhieuKtraCluongBttHdr.TABLE_NAME));
        if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
            data.setFileDinhKem(fileDinhKem.get(0));
        }

        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenLoaiVthh(hashMapVthh.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(hashMapVthh.get(data.getCloaiVthh()));
        data.setTenDvi(hashMapDvi.get(data.getMaDvi()));
        if(!Objects.isNull(data.getIdKtv())){
            data.setTenKtv(userInfoRepository.findById(data.getIdKtv()).get().getFullName());
        }
        if(!Objects.isNull(data.getIdTruongPhong())){
            data.setTenTruongPhong(userInfoRepository.findById(data.getIdTruongPhong()).get().getFullName());
        }
        if(!Objects.isNull(data.getIdNgKnghiem())){
            data.setTenNguoiKiemNghiem(userInfoRepository.findById(data.getIdNgKnghiem()).get().getFullName());
        }
        data.setTenDiemKho(hashMapDvi.get(data.getMaDiemKho()));
        data.setTenNhaKho(hashMapDvi.get(data.getMaNganKho()));
        data.setTenNganKho(hashMapDvi.get(data.getMaNganKho()));
        data.setTenLoKho(hashMapDvi.get(data.getMaLoKho()));
        data.setChildren(xhPhieuKtraCluongBttDtlRepository.findAllByIdHdr(id));

        return data;
    }

    @Override
    public XhPhieuKtraCluongBttHdr approve(XhPhieuKtraCluongBttHdrReq req) throws Exception {

        UserInfo userInfo = UserUtils.getUserInfo();
        if(Objects.isNull(req.getId())){
            throw new Exception("Bad reqeust");
        }

        if (!Contains.CAP_CUC.equals(userInfo.getCapDvi())){
            throw new Exception("Bad Request");
        }

        Optional<XhPhieuKtraCluongBttHdr> optional = xhPhieuKtraCluongBttHdrRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }

        XhPhieuKtraCluongBttHdr data = optional.get();
        String status = req.getTrangThai() + data.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
            case Contains.CHODUYET_TP + Contains.DUTHAO:
            case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
                data.setNguoiGuiDuyetId(userInfo.getId());
                data.setNgayGuiDuyet(new Date());
                break;
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TU_CHOI_LDC + Contains.CHODUYET_LDC:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(new Date());
                data.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.CHO_DUYET_LDC + Contains.CHODUYET_TP:
            case Contains.DA_DUYET_LDC + Contains.CHO_DUYET_LDC:
                data.setNguoiPduyetId(userInfo.getId());
                data.setNgayPduyet(new Date());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(req.getTrangThai());
        xhPhieuKtraCluongBttHdrRepository.save(data);
        return data;
    }

    @Override
    public void delete(Long id) throws Exception {
        if(Objects.isNull(id)){
            throw new Exception("Bad request");
        }

        Optional<XhPhieuKtraCluongBttHdr> optional = xhPhieuKtraCluongBttHdrRepository.findById(id);
        if (!optional.isPresent()){
             throw new Exception("Không tìm thấy dữ liệu");
        }

        xhPhieuKtraCluongBttHdrRepository.delete(optional.get());
        xhPhieuKtraCluongBttDtlRepository.deleteAllByIdHdr(optional.get().getId());
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhPhieuKtraCluongBttHdr.TABLE_NAME));

    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(XhPhieuKtraCluongBttHdrReq req, HttpServletResponse response) throws Exception {

    }
}
