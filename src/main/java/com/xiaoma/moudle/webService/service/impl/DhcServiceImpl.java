package com.xiaoma.moudle.webService.service.impl;
import com.xiaoma.moudle.webService.service.DhcService;

import javax.jws.WebService;

@WebService(
        endpointInterface = "com.xiaoma.moudle.webService.service.DhcService",
        targetNamespace = "http://tempuri.org",
        serviceName = "DhcService",
        portName = "DhcServicePort"
)
public class DhcServiceImpl implements DhcService {

    @Override
    public String dhcService(String input1, String input2) {
        System.out.println("Received input1: " + input1);
        System.out.println("Received input2: " + input2);

        // 构造返回的 XML 响应
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Response><ExamReportNewReturn><Returncode>0</Returncode><ResultContent>处理成功!</ResultContent></ExamReportNewReturn></Response>";
    }
}


