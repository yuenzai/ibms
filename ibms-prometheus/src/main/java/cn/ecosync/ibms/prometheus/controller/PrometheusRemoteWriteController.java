package cn.ecosync.ibms.prometheus.controller;

import cn.ecosync.ibms.prometheus.protos.Request;
import cn.ecosync.ibms.prometheus.protos.Sample;
import cn.ecosync.ibms.prometheus.protos.TimeSeries;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xerial.snappy.Snappy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/prometheus")
public class PrometheusRemoteWriteController {
    private static final Logger log = LoggerFactory.getLogger(PrometheusRemoteWriteController.class);

    @PostMapping(value = "/api/v1/write")
    public ResponseEntity<Object> onMessage(ServletRequest servletRequest) throws IOException {
        ServletInputStream inputStream = servletRequest.getInputStream();
        byte[] bytes = copyToByteArray(inputStream);
        byte[] uncompress = Snappy.uncompress(bytes);

        Request request = Request.parseFrom(uncompress);

        int sampleCount = handle(request);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>(1);
        headers.set("X-Prometheus-Remote-Write-Samples-Written", String.valueOf(sampleCount));
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
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

    public static byte[] copyToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024]; // 缓冲区大小
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }
}
