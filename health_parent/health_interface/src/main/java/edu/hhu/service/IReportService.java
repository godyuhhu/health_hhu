package edu.hhu.service;

import java.util.Map;

/**
 * 运营数据的业务层接口
 */
public interface IReportService {
    Map<String, Object> findBusinessReportData() throws Exception;
}
