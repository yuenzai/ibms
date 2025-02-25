package cn.ecosync.ibms.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ConverterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.ParsingUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleReadListener implements ReadListener<Map<Integer, String>> {
    private static final Logger log = LoggerFactory.getLogger(SimpleReadListener.class);

    private final Map<Integer, String> head = new HashMap<>();
    private final List<Map<Integer, String>> body = new ArrayList<>();
    private final List<ExcelDataConvertException> exceptions = new ArrayList<>();

    @Override
    public void onException(Exception exception, AnalysisContext context) {
        if (exception instanceof ExcelDataConvertException) {
            exceptions.add((ExcelDataConvertException) exception);
        } else {
            log.atError().setCause(exception).log("");
        }
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        Map<Integer, String> head = ConverterUtils.convertToStringMap(headMap, context);
        for (Map.Entry<Integer, String> entry : head.entrySet()) {
            this.head.put(entry.getKey(), String.join("_", ParsingUtils.splitCamelCaseToLower(entry.getValue())));
        }
    }

    @Override
    public void invoke(Map<Integer, String> row, AnalysisContext context) {
        body.add(row);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
    }

    public Map<Integer, String> getHead() {
        return head;
    }

    public List<Map<Integer, String>> getBody() {
        return body;
    }

    public List<ExcelDataConvertException> getExceptions() {
        return exceptions;
    }
}
