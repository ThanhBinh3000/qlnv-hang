package com.tcdt.qlnvhang.service.xuathang.xuattheophuongthucdaugia;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.XhThopDxKhBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.XhThopDxKhBdgRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.SearchXhThopDxKhBdg;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.XhThopChiTieuReq;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.XhThopDxKhBdgReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhThopDxKhBdgDtl;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhThopDxKhBdg;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhThopDxKhBdgService extends BaseServiceImpl {

    @Autowired
    private XhThopDxKhBdgRepository xhThopDxKhBdgRepository;

    @Autowired
    private XhThopDxKhBdgDtlRepository xhThopDxKhBdgDtlRepository;

    @Autowired
    private XhDxKhBanDauGiaRepository xhDxKhBanDauGiaRepository;

    public Page<XhThopDxKhBdg> searchPage(SearchXhThopDxKhBdg objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhThopDxKhBdg> data=xhThopDxKhBdgRepository.searchPage(
                objReq.getNamKh(),
                objReq.getLoaiVthh(),
                objReq.getCloaiVthh(),
                objReq.getNoiDungThop(),
                Contains.convertDateToString(objReq.getNgayThopTu()),
                Contains.convertDateToString(objReq.getNgayThopDen()),
                objReq.getTrangThai(),
                userInfo.getDvql(),
                pageable);
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();
        data.getContent().forEach(f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmhh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmhh.get(f.getCloaiVthh()));
        });
        return data;
    }

    public XhThopDxKhBdg sumarrayDaTa(XhThopChiTieuReq objReq, HttpServletRequest req) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        List<XhDxKhBanDauGia> khBanDauGiaList=xhDxKhBanDauGiaRepository.listTongHop(
                objReq.getNamKh(),
                objReq.getLoaiVthh(),
                objReq.getCloaiVthh(),
                objReq.getLoaiHdong(),
                Contains.convertDateToString(objReq.getNgayKyTu()),
                Contains.convertDateToString(objReq.getNgayKyDen()),
                userInfo.getDvql());
        if (khBanDauGiaList.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
        }

        XhThopDxKhBdg thopHdr=new XhThopDxKhBdg();
        thopHdr.setNamKh(objReq.getNamKh());
        thopHdr.setLoaiVthh(objReq.getLoaiVthh());
        thopHdr.setCloaiVthh(objReq.getCloaiVthh());

        List<XhThopDxKhBdgDtl> thopDtls= new ArrayList<>();

        for (XhDxKhBanDauGia dxuat : khBanDauGiaList) {
            XhThopDxKhBdgDtl dxuatDtl = new XhThopDxKhBdgDtl();
            dxuatDtl.setIdDxuat(dxuat.getId());
            dxuatDtl.setSoDxuat(dxuat.getSoDxuat());
            Optional<XhDxKhBanDauGia> dx = xhDxKhBanDauGiaRepository.findById(dxuat.getId());
            dxuatDtl.setDxKhBanDauGia(dx.get());

            thopDtls.add(dxuatDtl);
        }
        thopHdr.setThopDxKhBdgDtlList(thopDtls);


        return thopHdr;
    }


    public XhThopDxKhBdg create(XhThopDxKhBdgReq objReq, HttpServletRequest req) throws Exception {
        UserInfo userInfo =SecurityContextService.getUser();

        // Set thong tin hdr tong hop
        XhThopDxKhBdg thopHdr = sumarrayDaTa(objReq,req);
        thopHdr.setLoaiVthh(objReq.getLoaiVthh());
        thopHdr.setCloaiVthh(objReq.getCloaiVthh());
        thopHdr.setTrangThai(Contains.CHUATAO_QD);
        thopHdr.setNoiDungThop(objReq.getNoiDungThop());
        thopHdr.setMaDvi(userInfo.getDvql());
        thopHdr.setNamKh(objReq.getNamKh());
        xhThopDxKhBdgRepository.save(thopHdr);
        for (XhThopDxKhBdgDtl dtl : thopHdr.getThopDxKhBdgDtlList()){
            dtl.setIdHdr(thopHdr.getId());
            xhThopDxKhBdgDtlRepository.save(dtl);
        }
        if (thopHdr.getId() > 0 && thopHdr.getThopDxKhBdgDtlList().size() > 0) {
            List<String> soDxuatList = thopHdr.getThopDxKhBdgDtlList().stream().map(XhThopDxKhBdgDtl::getSoDxuat)
                    .collect(Collectors.toList());
            List<XhDxKhBanDauGia> list = xhDxKhBanDauGiaRepository.findBySoDxuatIn(soDxuatList);
            for (XhDxKhBanDauGia soDxuat : list){
                soDxuat.setMaThop(String.valueOf(thopHdr.getId()));
                xhDxKhBanDauGiaRepository.save(soDxuat);
            }
        }
        return thopHdr;
    }

    public XhThopDxKhBdg update(XhThopDxKhBdgReq objReq) throws Exception {
        if (StringUtils.isEmpty(objReq.getId()))
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

        Optional<XhThopDxKhBdg> qOptional = xhThopDxKhBdgRepository.findById(Long.valueOf(objReq.getId()));
        if (!qOptional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần sửa");

        XhThopDxKhBdg dataDTB = qOptional.get();
        XhThopDxKhBdg dataMap = ObjectMapperUtils.map(objReq, XhThopDxKhBdg.class);

        updateObjectToObject(dataDTB, dataMap);
        xhThopDxKhBdgRepository.save(dataDTB);
        return dataDTB;
    }

    public XhThopDxKhBdg detail(String ids) throws Exception {
        if (StringUtils.isEmpty(ids))
            throw new UnsupportedOperationException("Không tồn tại bản ghi");

        Optional<XhThopDxKhBdg> qOptional = xhThopDxKhBdgRepository.findById(Long.parseLong(ids));

        if (!qOptional.isPresent())
            throw new UnsupportedOperationException("Không tồn tại bản ghi");

        XhThopDxKhBdg hdrThop = qOptional.get();

        // Lay danh muc dung chung
        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();

        hdrThop.setTenLoaiVthh(StringUtils.isEmpty(hdrThop.getLoaiVthh()) ? null : hashMapDmHh.get(hdrThop.getLoaiVthh()));
        hdrThop.setTenCloaiVthh(hashMapDmHh.get(hdrThop.getCloaiVthh()));
        hdrThop.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(hdrThop.getTrangThai()));

        List<XhThopDxKhBdgDtl> thopDxKhBdgDtlList = xhThopDxKhBdgDtlRepository.findAllByIdHdr(hdrThop.getId());
        List<Long> idDxuat=thopDxKhBdgDtlList.stream().map(XhThopDxKhBdgDtl::getIdDxuat).collect(Collectors.toList());
        List<XhDxKhBanDauGia> lisDxuat=xhDxKhBanDauGiaRepository.findAllByIdIn(idDxuat);
        for (XhThopDxKhBdgDtl dtl : thopDxKhBdgDtlList){
            XhDxKhBanDauGia deXuat = xhDxKhBanDauGiaRepository.findAllById(dtl.getIdDxuat());
            dtl.setDxKhBanDauGia(deXuat);
        }
        Map<String, String> mapDmucDvi = getMapTenDvi();
        thopDxKhBdgDtlList.forEach(f -> {
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : mapDmucDvi.get(f.getMaDvi()));
        });
        hdrThop.setThopDxKhBdgDtlList(thopDxKhBdgDtlList);

        return hdrThop;
    }

    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getId()))
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");

        Optional<XhThopDxKhBdg> optional = xhThopDxKhBdgRepository.findById(idSearchReq.getId());
        if (!optional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần xoá");
        List<XhThopDxKhBdgDtl> listDls= xhThopDxKhBdgDtlRepository.findAllByIdHdr(optional.get().getId());
        if(!CollectionUtils.isEmpty(listDls)){
            List<Long> idDxList = listDls.stream().map(XhThopDxKhBdgDtl::getIdDxuat).collect(Collectors.toList());
            List<XhDxKhBanDauGia> listDxHdr = xhDxKhBanDauGiaRepository.findByIdIn(idDxList);
            if(!CollectionUtils.isEmpty(listDxHdr)){
                listDxHdr.stream().map(item ->{
                    item.setTrangThaiTh(Contains.CHUATONGHOP);
                    return item;
                }).collect(Collectors.toList());
            }
            xhDxKhBanDauGiaRepository.saveAll(listDxHdr);
        }
        xhThopDxKhBdgDtlRepository.deleteAll(listDls);
        xhThopDxKhBdgRepository.delete(optional.get());
    }

    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getIdList()))
            throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
        List<XhThopDxKhBdg> listThop= xhThopDxKhBdgRepository.findAllByIdIn(idSearchReq.getIdList());
        for (XhThopDxKhBdg thopHdr: listThop){
            List<XhThopDxKhBdgDtl> listDls= xhThopDxKhBdgDtlRepository.findAllByIdHdr(thopHdr.getId());
            if(!CollectionUtils.isEmpty(listDls)){
                List<Long> idDxList = listDls.stream().map(XhThopDxKhBdgDtl::getIdDxuat).collect(Collectors.toList());
                List<XhDxKhBanDauGia> listDxHdr = xhDxKhBanDauGiaRepository.findByIdIn(idDxList);
                if(!CollectionUtils.isEmpty(listDxHdr)){
                    listDxHdr.stream().map(item ->{
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

        String title = "Danh sách tổng hợp kế hoạch mua trực tiếp";
        String[] rowsName = new String[] { "STT", "Mã tổng hợp", "Ngày tổng hợp", "Nội dung tổng hợp",
                "Năm kế hoạch","Số QĐ phê duyêt KH BDG ","Loại hàng hóa", "Trạng thái"};
        String filename = "Tong_hop_de_xuat_ke_hoach_lua_chon_nha_thau.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhThopDxKhBdg dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getMaThop();
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
