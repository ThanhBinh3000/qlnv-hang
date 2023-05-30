package com.tcdt.qlnvhang.service.xuathang.daugia.kehoach.tonghop;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdgRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.SearchXhThopDxKhBdg;
import com.tcdt.qlnvhang.request.xuathang.daugia.XhThopChiTieuReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.tonghop.XhThopDxKhBdgReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdgDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.tonghop.XhThopDxKhBdg;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class XhThopDxKhBdgService extends BaseServiceImpl {

    @Autowired
    private XhThopDxKhBdgRepository xhThopDxKhBdgRepository;

    @Autowired
    private XhThopDxKhBdgDtlRepository xhThopDxKhBdgDtlRepository;

    @Autowired
    private XhDxKhBanDauGiaRepository xhDxKhBanDauGiaRepository;

    public Page<XhThopDxKhBdg> searchPage(SearchXhThopDxKhBdg req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhThopDxKhBdg> dataTongHopKeHoachDauGia = xhThopDxKhBdgRepository.searchPage(
                req,
                pageable);

        Map<String, String> mapDmucHangHoa = getListDanhMucHangHoa();

        dataTongHopKeHoachDauGia.getContent().forEach(tongHopKeHoachDauGia -> {
            if (mapDmucHangHoa.get((tongHopKeHoachDauGia.getLoaiVthh())) != null) {
                tongHopKeHoachDauGia.setTenLoaiVthh(mapDmucHangHoa.get(tongHopKeHoachDauGia.getLoaiVthh()));
            }
            if (mapDmucHangHoa.get((tongHopKeHoachDauGia.getCloaiVthh())) != null) {
                tongHopKeHoachDauGia.setTenCloaiVthh(mapDmucHangHoa.get(tongHopKeHoachDauGia.getCloaiVthh()));
            }
            tongHopKeHoachDauGia.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(tongHopKeHoachDauGia.getTrangThai()));
        });
        return dataTongHopKeHoachDauGia;
    }

    public XhThopDxKhBdg sumarryData(XhThopChiTieuReq req, HttpServletRequest servletRequest) throws Exception {
        if(req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        List<XhDxKhBanDauGia> deXuatKeHoachDauGia = xhDxKhBanDauGiaRepository.listTongHop(req);
        if (deXuatKeHoachDauGia.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
        }

        XhThopDxKhBdg tongHopKeHoachDauGia = new XhThopDxKhBdg();

        Map<String, String> listDanhMucHangHoa = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

        tongHopKeHoachDauGia.setNamKh(req.getNamKh());
        tongHopKeHoachDauGia.setLoaiVthh(req.getLoaiVthh());
        tongHopKeHoachDauGia.setTenLoaiVthh(listDanhMucHangHoa.get(req.getLoaiVthh()));
        tongHopKeHoachDauGia.setCloaiVthh(req.getCloaiVthh());
        tongHopKeHoachDauGia.setTenCloaiVthh(listDanhMucHangHoa.get(req.getCloaiVthh()));
        tongHopKeHoachDauGia.setNgayDuyetTu(req.getNgayDuyetTu());
        tongHopKeHoachDauGia.setNgayDuyetDen(req.getNgayDuyetDen());
        tongHopKeHoachDauGia.setMaDvi(userInfo.getDvql());

        List<XhThopDxKhBdgDtl> tongHopKeHoachDauGiaDtlList = new ArrayList<>();
        for (XhDxKhBanDauGia keHoachDauGia : deXuatKeHoachDauGia) {
            XhThopDxKhBdgDtl tongHopKeHoachDauGiaDtl = new XhThopDxKhBdgDtl();

            BeanUtils.copyProperties(keHoachDauGia,tongHopKeHoachDauGiaDtl,"id");
            tongHopKeHoachDauGiaDtl.setIdDxHdr(keHoachDauGia.getId());
            tongHopKeHoachDauGiaDtl.setMaDvi(keHoachDauGia.getMaDvi());
            tongHopKeHoachDauGiaDtl.setTenDvi(mapDmucDvi.get(keHoachDauGia.getMaDvi()));
            tongHopKeHoachDauGiaDtl.setSoDxuat(keHoachDauGia.getSoDxuat());
            tongHopKeHoachDauGiaDtl.setNgayPduyet(keHoachDauGia.getNgayPduyet());
            tongHopKeHoachDauGiaDtl.setTrichYeu(keHoachDauGia.getTrichYeu());
            tongHopKeHoachDauGiaDtl.setSlDviTsan(keHoachDauGia.getSlDviTsan());
            tongHopKeHoachDauGiaDtl.setTrangThai(keHoachDauGia.getTrangThai());
            tongHopKeHoachDauGiaDtl.setTongSoLuong(keHoachDauGia.getTongSoLuong());
            tongHopKeHoachDauGiaDtl.setKhoanTienDatTruoc(keHoachDauGia.getKhoanTienDatTruoc());

            BigDecimal donGiaDuocDuyet = BigDecimal.ZERO;
            if(keHoachDauGia.getLoaiVthh().startsWith("02")){
                donGiaDuocDuyet = xhThopDxKhBdgDtlRepository.getDonGiaVatVt(keHoachDauGia.getCloaiVthh(), keHoachDauGia.getNamKh());
                if (!DataUtils.isNullObject(donGiaDuocDuyet) && !StringUtils.isEmpty(keHoachDauGia.getTongSoLuong()) && !StringUtils.isEmpty(keHoachDauGia.getKhoanTienDatTruoc())){
                    BigDecimal giaKdTheoDonGiaDd = keHoachDauGia.getTongSoLuong().multiply(donGiaDuocDuyet);
                    BigDecimal khoanTienDtruocTheoDgiaDd = keHoachDauGia.getTongSoLuong().multiply(donGiaDuocDuyet).multiply(keHoachDauGia.getKhoanTienDatTruoc()).divide(BigDecimal.valueOf(100));
                    tongHopKeHoachDauGiaDtl.setGiaKdTheoDonGiaDd(giaKdTheoDonGiaDd);
                    tongHopKeHoachDauGiaDtl.setKhoanTienDtruocTheoDgiaDd(khoanTienDtruocTheoDgiaDd);
                }
            }else {
                donGiaDuocDuyet = xhThopDxKhBdgDtlRepository.getDonGiaVatLt(keHoachDauGia.getCloaiVthh(), keHoachDauGia.getMaDvi(), keHoachDauGia.getNamKh());
                if (!DataUtils.isNullObject(donGiaDuocDuyet) && !StringUtils.isEmpty(keHoachDauGia.getTongSoLuong()) && !StringUtils.isEmpty(keHoachDauGia.getKhoanTienDatTruoc())){
                    BigDecimal giaKdTheoDonGiaDd = keHoachDauGia.getTongSoLuong().multiply(donGiaDuocDuyet);
                    BigDecimal khoanTienDtruocTheoDgiaDd = keHoachDauGia.getTongSoLuong().multiply(donGiaDuocDuyet).multiply(keHoachDauGia.getKhoanTienDatTruoc()).divide(BigDecimal.valueOf(100));
                    tongHopKeHoachDauGiaDtl.setGiaKdTheoDonGiaDd(giaKdTheoDonGiaDd);
                    tongHopKeHoachDauGiaDtl.setKhoanTienDtruocTheoDgiaDd(khoanTienDtruocTheoDgiaDd);
                }
            }

            tongHopKeHoachDauGiaDtlList.add(tongHopKeHoachDauGiaDtl);
            tongHopKeHoachDauGia.setLoaiHinhNx(keHoachDauGia.getLoaiHinhNx());
            tongHopKeHoachDauGia.setKieuNx(keHoachDauGia.getKieuNx());
        }

        tongHopKeHoachDauGia.setChildren(tongHopKeHoachDauGiaDtlList);

        return tongHopKeHoachDauGia;
    }

    @Transactional()
    public XhThopDxKhBdg create(XhThopDxKhBdgReq  req, HttpServletRequest servletRequest) throws Exception {
        if (req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        XhThopDxKhBdg tongHopKeHoachDauGia = sumarryData(req,servletRequest);

        tongHopKeHoachDauGia.setId(req.getIdTh());
        tongHopKeHoachDauGia.setNgayTao(LocalDate.now());
        tongHopKeHoachDauGia.setNguoiTaoId(getUser().getId());
        tongHopKeHoachDauGia.setNoiDungThop(req.getNoiDungThop());
        tongHopKeHoachDauGia.setTrangThai(Contains.CHUATAO_QD);
        tongHopKeHoachDauGia.setNgayThop(LocalDate.now());
        tongHopKeHoachDauGia.setMaDvi(userInfo.getDvql());

        xhThopDxKhBdgRepository.save(tongHopKeHoachDauGia);

        log.debug("Create chi tiết");
        for (XhThopDxKhBdgDtl tongHopKeHoachDauGiaDtl : tongHopKeHoachDauGia.getChildren()) {
            tongHopKeHoachDauGiaDtl.setIdThopHdr(tongHopKeHoachDauGia.getId());
            xhThopDxKhBdgDtlRepository.save(tongHopKeHoachDauGiaDtl);
        }

        if (tongHopKeHoachDauGia.getId() > 0 && tongHopKeHoachDauGia.getChildren().size() > 0) {
            List<String> soDxuatList = tongHopKeHoachDauGia.getChildren().stream().map(XhThopDxKhBdgDtl::getSoDxuat)
                    .collect(Collectors.toList());
            xhDxKhBanDauGiaRepository.updateStatusInList(soDxuatList, Contains.DATONGHOP,tongHopKeHoachDauGia.getId());
        }

        return tongHopKeHoachDauGia;
    }

    @Transactional()
    public XhThopDxKhBdg update(XhThopDxKhBdgReq req) throws Exception {
        if (req == null) return null;

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhThopDxKhBdg> optional = xhThopDxKhBdgRepository.findById(Long.valueOf(req.getId()));
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");

        if (req.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(req.getLoaiVthh())){
            throw new Exception("Loại vật tư hành hóa không phù hợp");
        }

        XhThopDxKhBdg tongHopKeHoachDauGia = optional.get();

        log.info("Update Tong hop de xuat ban dau gia");
        XhThopDxKhBdg  tongHopKeHoachDauGiaMap = ObjectMapperUtils.map(req, XhThopDxKhBdg.class);

        tongHopKeHoachDauGiaMap.setNgaySua(LocalDate.now());
        tongHopKeHoachDauGiaMap.setNguoiTaoId(getUser().getId());
        updateObjectToObject(tongHopKeHoachDauGia, tongHopKeHoachDauGiaMap);

        xhThopDxKhBdgRepository.save(tongHopKeHoachDauGia);
        return tongHopKeHoachDauGia;
    }

    public XhThopDxKhBdg detail(String ids) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        Optional<XhThopDxKhBdg> optional = xhThopDxKhBdgRepository.findById(Long.parseLong(ids));
        if (!optional.isPresent()) throw new UnsupportedOperationException("Không tồn tại bản ghi");

        XhThopDxKhBdg tongHopKeHoachDauGia = optional.get();

        Map<String, String> mapDmucHangHoa = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");

        List<XhThopDxKhBdgDtl> tongHopKeHoachDauGiaList = xhThopDxKhBdgDtlRepository.findByIdThopHdr(tongHopKeHoachDauGia.getId());
        if(!CollectionUtils.isEmpty(tongHopKeHoachDauGiaList)){
            tongHopKeHoachDauGiaList.forEach(tongHop -> {
                if (mapDmucDvi.containsKey(tongHop.getMaDvi())) {
                    tongHop.setTenDvi(mapDmucDvi.get(tongHop.getMaDvi()));
                }
                BigDecimal donGiaDuocDuyet = BigDecimal.ZERO;
                if(tongHopKeHoachDauGia.getLoaiVthh().startsWith("02")){
                    donGiaDuocDuyet = xhThopDxKhBdgDtlRepository.getDonGiaVatVt(tongHopKeHoachDauGia.getCloaiVthh(), tongHopKeHoachDauGia.getNamKh());
                    if (!DataUtils.isNullObject(donGiaDuocDuyet) && !StringUtils.isEmpty(tongHop.getTongSoLuong()) && !StringUtils.isEmpty(tongHop.getKhoanTienDatTruoc())){
                        BigDecimal giaKdTheoDonGiaDd = tongHop.getTongSoLuong().multiply(donGiaDuocDuyet);
                        BigDecimal khoanTienDtruocTheoDgiaDd = tongHop.getTongSoLuong().multiply(donGiaDuocDuyet).multiply(tongHop.getKhoanTienDatTruoc()).divide(BigDecimal.valueOf(100));
                        tongHop.setGiaKdTheoDonGiaDd(giaKdTheoDonGiaDd);
                        tongHop.setKhoanTienDtruocTheoDgiaDd(khoanTienDtruocTheoDgiaDd);
                    }
                }else {
                    donGiaDuocDuyet = xhThopDxKhBdgDtlRepository.getDonGiaVatLt(tongHopKeHoachDauGia.getCloaiVthh(), tongHop.getMaDvi(), tongHopKeHoachDauGia.getNamKh());
                    if (!DataUtils.isNullObject(donGiaDuocDuyet) && !StringUtils.isEmpty(tongHop.getTongSoLuong()) && !StringUtils.isEmpty(tongHop.getKhoanTienDatTruoc())){
                        BigDecimal giaKdTheoDonGiaDd = tongHop.getTongSoLuong().multiply(donGiaDuocDuyet);
                        BigDecimal khoanTienDtruocTheoDgiaDd = tongHop.getTongSoLuong().multiply(donGiaDuocDuyet).multiply(tongHop.getKhoanTienDatTruoc()).divide(BigDecimal.valueOf(100));
                        tongHop.setGiaKdTheoDonGiaDd(giaKdTheoDonGiaDd);
                        tongHop.setKhoanTienDtruocTheoDgiaDd(khoanTienDtruocTheoDgiaDd);
                    }
                }
            });
            tongHopKeHoachDauGia.setChildren(tongHopKeHoachDauGiaList);
        }
        if (mapDmucHangHoa.get((tongHopKeHoachDauGia.getLoaiVthh())) != null) {
            tongHopKeHoachDauGia.setTenLoaiVthh(mapDmucHangHoa.get(tongHopKeHoachDauGia.getLoaiVthh()));
        }
        if (mapDmucHangHoa.get((tongHopKeHoachDauGia.getCloaiVthh())) != null) {
            tongHopKeHoachDauGia.setTenCloaiVthh(mapDmucHangHoa.get(tongHopKeHoachDauGia.getCloaiVthh()));
        }
        tongHopKeHoachDauGia.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(tongHopKeHoachDauGia.getTrangThai()));

        return tongHopKeHoachDauGia;
    }

    public void delete(IdSearchReq idSearchReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        if (StringUtils.isEmpty(idSearchReq.getId())) throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");

        Optional<XhThopDxKhBdg> optional = xhThopDxKhBdgRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần xóa");

        List<XhThopDxKhBdgDtl> listDls = xhThopDxKhBdgDtlRepository.findByIdThopHdr(optional.get().getId());
        if (!CollectionUtils.isEmpty(listDls)) {
            List<Long> idDxList = listDls.stream().map(XhThopDxKhBdgDtl::getIdDxHdr).collect(Collectors.toList());
            List<XhDxKhBanDauGia> listDxHdr = xhDxKhBanDauGiaRepository.findByIdIn(idDxList);
            if (!CollectionUtils.isEmpty(listDxHdr)) {
                listDxHdr.stream().map(item -> {
                    item.setTrangThaiTh(Contains.CHUATONGHOP);
                    item.setIdThop(null);
                    return item;
                }).collect(Collectors.toList());
            }
            xhDxKhBanDauGiaRepository.saveAll(listDxHdr);
        }
        xhThopDxKhBdgDtlRepository.deleteAll(listDls);
        xhThopDxKhBdgRepository.delete(optional.get());

    }

    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null) throw new Exception("Bad request.");

        if (StringUtils.isEmpty(idSearchReq.getIdList())) throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");

        List<XhThopDxKhBdg> listThop = xhThopDxKhBdgRepository.findAllByIdIn(idSearchReq.getIdList());
        for (XhThopDxKhBdg thopHdr : listThop) {
            List<XhThopDxKhBdgDtl> listDls = xhThopDxKhBdgDtlRepository.findByIdThopHdr(thopHdr.getId());
            if (!CollectionUtils.isEmpty(listDls)) {
                List<Long> idDxList = listDls.stream().map(XhThopDxKhBdgDtl::getIdDxHdr).collect(Collectors.toList());
                List<XhDxKhBanDauGia> listDxHdr = xhDxKhBanDauGiaRepository.findByIdIn(idDxList);
                if (!CollectionUtils.isEmpty(listDxHdr)) {
                    listDxHdr.stream().map(item -> {
                        item.setTrangThaiTh(Contains.CHUATONGHOP);
                        return item;
                    }).collect(Collectors.toList());
                }
                xhDxKhBanDauGiaRepository.saveAll(listDxHdr);
            }
            xhThopDxKhBdgDtlRepository.deleteAll(listDls);
        }
        xhThopDxKhBdgRepository.deleteAllByIdIn(idSearchReq.getIdList());
    }


    public void export(SearchXhThopDxKhBdg searchReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        searchReq.setPaggingReq(paggingReq);
        Page<XhThopDxKhBdg> page = this.searchPage(searchReq);
        List<XhThopDxKhBdg> data = page.getContent();
        String title = "Danh sách tổng hợp kế hoạch bán đấu giá";
        String[] rowsName = new String[]{"STT", "Mã tổng hợp", "Ngày tổng hợp", "Nội dung tổng hợp",
                "Năm kế hoạch", "Số QĐ phê duyêt KH BDG ", "Loại hàng hóa", "Trạng thái"};
        String filename = "Tong-hop-de-xuat-ke-hoach-ban-dau-gia.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhThopDxKhBdg dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getId();
            objs[2] = dx.getNgayThop();
            objs[3] = dx.getNoiDungThop();
            objs[4] = dx.getNamKh();
            objs[5] = dx.getSoQdPd();
            objs[6] = dx.getTenLoaiVthh();
            objs[7] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }
}
