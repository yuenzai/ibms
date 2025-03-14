package cn.ecosync.ibms.prometheus.controller;

import cn.ecosync.ibms.prometheus.protos.Request;
import cn.ecosync.ibms.prometheus.protos.Sample;
import cn.ecosync.ibms.prometheus.protos.TimeSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prometheus")
public class PrometheusRemoteWriteController {
    private static final Logger log = LoggerFactory.getLogger(PrometheusRemoteWriteController.class);

    @PostMapping("/api/v1/write")
    public ResponseEntity<Object> onMessage(@RequestBody RequestEntity<Request> requestEntity) {
        HttpHeaders headers = requestEntity.getHeaders();
        MediaType contentType = headers.getContentType();
        if (contentType == null) {
            log.atError().log("Content-Type is null");
            return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
        log.atInfo().addKeyValue("headers", headers).log("");
        Request request = requestEntity.getBody();
        Assert.notNull(request, "request is null");
        int sampleCount = handle(request);
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        responseEntity.getHeaders().set("X-Prometheus-Remote-Write-Samples-Written", String.valueOf(sampleCount));
        return responseEntity;
    }

    private int handle(Request request) {
        int sampleCount = 0;
        for (TimeSeries timeSeries : request.getTimeseriesList()) {
            sampleCount += handle(timeSeries);
        }
        return sampleCount;
    }

    private int handle(TimeSeries timeSeries) {
        int sampleCount = 0;
        for (Sample sample : timeSeries.getSamplesList()) {
            log.atInfo().addKeyValue("value", sample.getValue())
                    .addKeyValue("timestamp", sample.getTimestamp())
                    .log("Sample");
            sampleCount++;
        }
        return sampleCount;
    }
}
