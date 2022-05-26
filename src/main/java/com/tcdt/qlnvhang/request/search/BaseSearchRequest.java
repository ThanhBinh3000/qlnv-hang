package com.tcdt.qlnvhang.request.search;

import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
public class BaseSearchRequest {
    private Pageable pageable;
}
