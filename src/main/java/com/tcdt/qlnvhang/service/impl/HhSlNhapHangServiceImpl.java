package com.tcdt.qlnvhang.service.impl;

import com.tcdt.qlnvhang.common.DocxToPdfConverter;
import com.tcdt.qlnvhang.entities.FileDKemJoinDxKhLcntCcxdg;
import com.tcdt.qlnvhang.entities.FileDKemJoinDxKhLcntHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.HhSlNhapHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.*;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.HhDxKhLcntThopDtlRepository;
import com.tcdt.qlnvhang.repository.HhDxKhLcntThopHdrRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.HhSlNhapHangRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.*;
import com.tcdt.qlnvhang.request.CountKhlcntSlReq;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.*;
import com.tcdt.qlnvhang.request.search.HhDxuatKhLcntSearchReq;
import com.tcdt.qlnvhang.secification.HhDxuatKhLcntSpecification;
import com.tcdt.qlnvhang.service.HhDxuatKhLcntHdrService;
import com.tcdt.qlnvhang.service.HhSlNhapHangService;
import com.tcdt.qlnvhang.service.feign.BaoCaoClient;
import com.tcdt.qlnvhang.table.DmDonViDTO;
import com.tcdt.qlnvhang.table.HhDxKhLcntThopDtl;
import com.tcdt.qlnvhang.table.HhDxKhLcntThopHdr;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.report.HhDxKhlcntDsgthauReport;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.util.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class HhSlNhapHangServiceImpl extends BaseServiceImpl implements HhSlNhapHangService {

    @Autowired
    HttpServletRequest request;

    @Autowired
    private HhSlNhapHangRepository hhSlNhapHangRepository;



    @Override
    @Transactional
    public HhSlNhapHang create(HhSlNhapHangReq objReq) throws Exception {

        HhSlNhapHang dataMap = new ModelMapper().map(objReq, HhSlNhapHang.class);
        dataMap.setNgayTao(objReq.getNgayTao());
        dataMap.setNguoiTaoId(getUser().getId());
        hhSlNhapHangRepository.save(dataMap);
        return dataMap;
    }

    @Override
    @Transactional
    public HhSlNhapHang update(HhSlNhapHangReq objReq) throws Exception {
        if (StringUtils.isEmpty(objReq.getId()))
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");

        Optional<HhSlNhapHang> qOptional = hhSlNhapHangRepository.findById(Long.valueOf(objReq.getId()));
        if (!qOptional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần sửa");

        HhSlNhapHang dataDTB = qOptional.get();
        HhSlNhapHang dataMap = ObjectMapperUtils.map(objReq, HhSlNhapHang.class);

        updateObjectToObject(dataDTB, dataMap);

        dataDTB.setNgaySua(LocalDateTime.now());
        dataDTB.setNguoiSuaId(getUser().getId());

        hhSlNhapHangRepository.save(dataDTB);
        return dataDTB;
    }

    @Override
    public HhSlNhapHang detail(Long ids) throws Exception {
        Optional<HhSlNhapHang> qOptional = hhSlNhapHangRepository.findById(ids);

        if (!qOptional.isPresent()) {
            throw new UnsupportedOperationException("Không tồn tại bản ghi");
        }

        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

        qOptional.get().setTenLoaiVthh(StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : mapVthh.get(qOptional.get().getLoaiVthh()));
        qOptional.get().setTenCloaiVthh(StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null : mapVthh.get(qOptional.get().getCloaiVthh()));
        qOptional.get().setTenDvi(mapDmucDvi.get(qOptional.get().getMaDvi()));
        return qOptional.get();
    }

}
