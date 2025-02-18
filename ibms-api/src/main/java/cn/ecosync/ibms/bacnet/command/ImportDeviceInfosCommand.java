package cn.ecosync.ibms.bacnet.command;

import cn.ecosync.ibms.command.Command;
import lombok.ToString;

import java.io.InputStream;

@ToString
public class ImportDeviceInfosCommand implements Command {
    private String dataAcquisitionCode;
    private InputStream inputStream;

    protected ImportDeviceInfosCommand() {
    }

    public ImportDeviceInfosCommand(String dataAcquisitionCode, InputStream inputStream) {
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
