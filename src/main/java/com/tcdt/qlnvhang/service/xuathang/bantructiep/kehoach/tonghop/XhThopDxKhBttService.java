package com.tcdt.qlnvhang.service.xuathang.bantructiep.kehoach.tonghop;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.dexuat.XhDxKhBanTrucTiepHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.tonghop.SearchXhThopDxKhBtt;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.tonghop.XhThopDxKhBttHdrReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.XhThopChiTieuReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class XhThopDxKhBttService extends BaseServiceImpl {

    @Autowired
    private XhDxKhBanTrucTiepHdrRepository xhDxKhBanTrucTiepHdrRepository;

    @Autowired
    private XhThopDxKhBttRepository xhThopDxKhBttRepository;

    @Autowired
    private XhThopDxKhBttDtlRepository xhThopDxKhBttDtlRepository;

    public Page<XhThopDxKhBttHdr> searchPage (SearchXhThopDxKhBtt req) throws Exception{
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhThopDxKhBttHdr> dataTongHop = xhThopDxKhBttRepository.searchPage(
                req,
                pageable);
        Map<String, String> mapDmucHangHoa = getListDanhMucHangHoa();
        dataTongHop.getContent().forEach(data->{
            if (mapDmucHangHoa.get((data.getLoaiVthh())) != null) {
                data.setTenLoaiVthh(mapDmucHangHoa.get(data.getLoaiVthh()));
            }
            if (mapDmucHangHoa.get((data.getCloaiVthh())) != null) {
                data.setTenCloaiVthh(mapDmucHangHoa.get(data.getCloaiVthh()));
            }
            data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        });
        return dataTongHop;
    }

    public XhThopDxKhBttHdr sumarryData(XhThopChiTieuReq req , HttpServletRequest servletRequest) throws Exception{
        if(req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        List<XhDxKhBanTrucTiepHdr>  dxuatBtt = xhDxKhBanTrucTiepHdrRepository.listTongHop(req);
        if (dxuatBtt.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
        }

        XhThopDxKhBttHdr tonghopBtt = new XhThopDxKhBttHdr();

        Map<String, String> listDanhMucHangHoa = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

        tonghopBtt.setNamKh(req.getNamKh());
        tonghopBtt.setLoaiVthh(req.getLoaiVthh());
        tonghopBtt.setTenLoaiVthh(listDanhMucHangHoa.get(req.getLoaiVthh()));
        tonghopBtt.setCloaiVthh(req.getCloaiVthh());
        tonghopBtt.setTenCloaiVthh(listDanhMucHangHoa.get(req.getCloaiVthh()));
        tonghopBtt.setNgayDuyetTu(req.getNgayDuyetTu());
        tonghopBtt.setNgayDuyetDen(req.getNgayDuyetDen());
        tonghopBtt.setMaDvi(userInfo.getDvql());
        List<XhThopDxKhBttDtl> tonghopBttDtlList = new ArrayList<>();
        for (XhDxKhBanTrucTiepHdr keHoachBtt : dxuatBtt) {
            XhThopDxKhBttDtl tonghopBttDtl = new XhThopDxKhBttDtl();
            BeanUtils.copyProperties(keHoachBtt,tonghopBttDtl,"id");
            tonghopBttDtl.setIdDxHdr(keHoachBtt.getId());
            tonghopBttDtl.setMaDvi(keHoachBtt.getMaDvi());
            tonghopBttDtl.setTenDvi(mapDmucDvi.get(keHoachBtt.getMaDvi()));
            tonghopBttDtl.setSoDxuat(keHoachBtt.getSoDxuat());
            tonghopBttDtl.setNgayPduyet(keHoachBtt.getNgayPduyet());
            tonghopBttDtl.setTrichYeu(keHoachBtt.getTrichYeu());
            tonghopBttDtl.setSlDviTsan(keHoachBtt.getSlDviTsan());
            tonghopBttDtl.setTrangThai(keHoachBtt.getTrangThai());
            tonghopBttDtl.setTongSoLuong(keHoachBtt.getTongSoLuong());
            tonghopBttDtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(tonghopBttDtl.getTrangThai()));
            tonghopBttDtlList.add(tonghopBttDtl);
            tonghopBtt.setLoaiHinhNx(keHoachBtt.getLoaiHinhNx());
            tonghopBtt.setKieuNx(keHoachBtt.getKieuNx());
        }
        tonghopBtt.setChildren(tonghopBttDtlList);
        return tonghopBtt;
    }

    @Transactional()
    public XhThopDxKhBttHdr create(XhThopDxKhBttHdrReq req, HttpServletRequest servletRequest) throws Exception{
        if (req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        XhThopDxKhBttHdr tonghopBtt = sumarryData(req,servletRequest);

        tonghopBtt.setId(req.getIdTh());
        tonghopBtt.setNgayTao(LocalDate.now());
        tonghopBtt.setNguoiTaoId(getUser().getId());
        tonghopBtt.setNoiDungThop(req.getNoiDungThop());
        tonghopBtt.setTrangThai(Contains.CHUATAO_QD);
        tonghopBtt.setNgayThop(LocalDate.now());
        tonghopBtt.setMaDvi(userInfo.getDvql());

        xhThopDxKhBttRepository.save(tonghopBtt);

        log.debug("Create chi tiết");
        for (XhThopDxKhBttDtl dtl : tonghopBtt.getChildren()){
            dtl.setIdThopHdr(tonghopBtt.getId());
            xhThopDxKhBttDtlRepository.save(dtl);
        }
        if (tonghopBtt.getId() > 0 && tonghopBtt.getChildren().size() > 0) {
            List<String> soDxuatList = tonghopBtt.getChildren().stream().map(XhThopDxKhBttDtl::getSoDxuat)
                    .collect(Collectors.toList());
            xhDxKhBanTrucTiepHdrRepository.updateStatusInList(soDxuatList, Contains.DATONGHOP,tonghopBtt.getId());
        }
        return tonghopBtt;
    }

    @Transactional()
    public XhThopDxKhBttHdr update (XhThopDxKhBttHdrReq req) throws Exception{
        if (req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");


        Optional<XhThopDxKhBttHdr> optional = xhThopDxKhBttRepository.findById(req.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");

        if (req.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(req.getLoaiVthh())){
            throw new Exception("Loại vật tư hành hóa không phù hợp");
        }

        XhThopDxKhBttHdr tonghopBtt = optional.get();

        XhThopDxKhBttHdr dataTonghopBtt = ObjectMapperUtils.map(req, XhThopDxKhBttHdr.class);
        dataTonghopBtt.setNgaySua(LocalDate.now());
        dataTonghopBtt.setNguoiTaoId(getUser().getId());
        updateObjectToObject(tonghopBtt, dataTonghopBtt);
        xhThopDxKhBttRepository.save(tonghopBtt);
        return tonghopBtt;
    }


    public XhThopDxKhBttHdr detail(String ids) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhThopDxKhBttHdr> optional = xhThopDxKhBttRepository.findById(Long.parseLong(ids));
        if (!optional.isPresent()) throw new UnsupportedOperationException("Không tồn tại bản ghi");

        XhThopDxKhBttHdr tonghopBtt = optional.get();

        Map<String, String> hashMapVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");

        List<XhThopDxKhBttDtl> listTh = xhThopDxKhBttDtlRepository.findByIdThopHdr(tonghopBtt.getId());
        listTh.forEach(tongHop -> {
            if (mapDmucDvi.containsKey(tongHop.getMaDvi())) {
                tongHop.setTenDvi(mapDmucDvi.get(tongHop.getMaDvi()));
            }
            tongHop.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(tongHop.getTrangThai()));
        });
        if (hashMapVthh.get((tonghopBtt.getLoaiVthh())) != null) {
            tonghopBtt.setTenLoaiVthh(hashMapVthh.get(tonghopBtt.getLoaiVthh()));
        }
        if (hashMapVthh.get((tonghopBtt.getCloaiVthh())) != null) {
            tonghopBtt.setTenCloaiVthh(hashMapVthh.get(tonghopBtt.getCloaiVthh()));
        }
        tonghopBtt.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(tonghopBtt.getTrangThai()));
        tonghopBtt.setChildren(listTh);
        return tonghopBtt;
    }


    public void delete(IdSearchReq idSearchReq) throws Exception{
        if (StringUtils.isEmpty(idSearchReq.getId())){
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu ");
        }
        Optional<XhThopDxKhBttHdr> qOptional = xhThopDxKhBttRepository.findById(idSearchReq.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
        XhThopDxKhBttHdr hdr = qOptional.get();
        List<XhThopDxKhBttDtl> listDtl = xhThopDxKhBttDtlRepository.findByIdThopHdr(hdr.getId());
        if (!CollectionUtils.isEmpty(listDtl)){
            List<Long> idDxList = listDtl.stream().map(XhThopDxKhBttDtl::getIdDxHdr).collect(Collectors.toList());
            List<XhDxKhBanTrucTiepHdr> listDxHdr = xhDxKhBanTrucTiepHdrRepository.findByIdIn(idDxList);
            if (!CollectionUtils.isEmpty(listDxHdr)) {
                listDxHdr.stream().map(item -> {
                    item.setTrangThaiTh(Contains.CHUATONGHOP);
                    item.setIdThop(null);
                    return item;
                }).collect(Collectors.toList());
            }
            xhDxKhBanTrucTiepHdrRepository.saveAll(listDxHdr);
        }
        xhThopDxKhBttDtlRepository.deleteAll(listDtl);
        xhThopDxKhBttRepository.delete(hdr);
    }

    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getIdList()))
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
        List<XhThopDxKhBttHdr> listThop = xhThopDxKhBttRepository.findAllByIdIn(idSearchReq.getIdList());
        for (XhThopDxKhBttHdr thopHdr : listThop) {
            List<XhThopDxKhBttDtl> listDls = xhThopDxKhBttDtlRepository.findByIdThopHdr(thopHdr.getId());
            if (!CollectionUtils.isEmpty(listDls)) {
                List<Long> idDxList = listDls.stream().map(XhThopDxKhBttDtl::getIdDxHdr).collect(Collectors.toList());
                List<XhDxKhBanTrucTiepHdr> listDxHdr = xhDxKhBanTrucTiepHdrRepository.findByIdIn(idDxList);
                if (!CollectionUtils.isEmpty(listDxHdr)) {
                    listDxHdr.stream().map(item -> {
                        item.setTrangThaiTh(Contains.CHUATONGHOP);
                        item.setIdThop(null);
                        return item;
                    }).collect(Collectors.toList());
                }
                xhDxKhBanTrucTiepHdrRepository.saveAll(listDxHdr);
            }
            xhThopDxKhBttDtlRepository.deleteAll(listDls);
        }
        xhThopDxKhBttRepository.deleteAllByIdIn(idSearchReq.getIdList());
    }

    public void export(SearchXhThopDxKhBtt searchReq,  HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        searchReq.setPaggingReq(paggingReq);
        Page<XhThopDxKhBttHdr> page = this.searchPage(searchReq);
        List<XhThopDxKhBttHdr> data = page.getContent();
        String title = "Danh sách thông tin tổng hợp kế hoạch bán trực tiếp";
        String[] rowsName = new String[]{"STT", "Mã tổng hợp", "Ngày tổng hợp", "Nội dung tổng hợp", "Năm kế hoạch", "Số QĐ phê duyệt KH bán trực tiếp", "Loại hàng hóa", "Trạng thái"};
        String filename = "Thong_tin_tong_hop_ke_hoach_ban_truc_tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhThopDxKhBttHdr thop = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = thop.getId();
            objs[2] = thop.getNgayThop();
            objs[3] = thop.getNoiDungThop();
            objs[4] = thop.getNamKh();
            objs[5] = thop.getSoQdPd();
            objs[6] = thop.getTenLoaiVthh();
            objs[7] = thop.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }
}
