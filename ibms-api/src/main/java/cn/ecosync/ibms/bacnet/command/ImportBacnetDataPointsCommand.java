package cn.ecosync.ibms.bacnet.command;

import cn.ecosync.ibms.command.Command;
import lombok.ToString;

import java.io.InputStream;

@ToString
public class ImportBacnetDataPointsCommand implements Command {
    private String dataAcquisitionCode;
    private InputStream inputStream;

    protected ImportBacnetDataPointsCommand() {
    }

    public ImportBacnetDataPointsCommand(String dataAcquisitionCode, InputStream inputStream) {
        this.dataAcquisitionCode = dataAcquisitionCode;
        this.inputStream = inputStream;
    }

    public String getDataAcquisitionCode() {
        return dataAcquisitionCode;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
