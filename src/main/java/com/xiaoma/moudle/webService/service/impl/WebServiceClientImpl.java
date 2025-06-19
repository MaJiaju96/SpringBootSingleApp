package com.xiaoma.moudle.webService.service.impl;

import com.xiaoma.moudle.webService.service.DhcService;
import com.xiaoma.moudle.webService.service.WebServiceClient;
import lombok.SneakyThrows;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.stereotype.Service;

import java.net.URL;
@Service
public class WebServiceClientImpl implements WebServiceClient {

    public static void main(String[] args) {
        String str = sendSoapRequest();
        System.out.println(str);
    }

    @SneakyThrows
    public static String sendSoapRequest() {
        URL wsdlUrl = new URL("http://localhost:5599/csp/hsb/DHC.Published.Report.BS.Report.CLS?wsdl");

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(DhcService.class);
        factory.setAddress(wsdlUrl.toString());

        DhcService client = (DhcService) factory.create();

        // 使用 ClientProxy 获取 Client 实例
        Client cxfClient = ClientProxy.getClient(client);

        HTTPConduit conduit = (HTTPConduit) cxfClient.getConduit();
        HTTPClientPolicy policy = new HTTPClientPolicy();
        policy.setConnectionTimeout(5000);
        policy.setReceiveTimeout(10000);
        conduit.setClient(policy);

        String input1 = "ExamReportNew";
        String input2 = buildInput2Xml();  // 可以用 Document 或 String 构造

        return client.dhcService(input1, input2);
    }


    private static String buildInput2Xml() {
        // 使用 XML 构建工具生成结构化 XML
        return "<![CDATA[<Request>\n" +
                "    <ExamReportNew>\n" +
                "        <CheckFlow>1545454</CheckFlow>\n" +
                "        <CheckLink></CheckLink>\n" +
                "        <ExecLink></ExecLink>\n" +
                "        <ExamName></ExamName>\n" +
                "        <Date>2025-06-10</Date>\n" +
                "        <Findings>\n" +
                "        </Findings>\n" +
                "        <Result></Result>\n" +
                "        <Id>CB20250610-055</Id>\n" +
                "        <ExamPara></ExamPara>\n" +
                "        <IsAbnormal>N</IsAbnormal>\n" +
                "        <ReqDocID>LIURONG</ReqDocID>\n" +
                "        <ReqDateTime>2025-05-12 14:44:28</ReqDateTime>\n" +
                "        <ScheduledDocID></ScheduledDocID>\n" +
                "        <ScheduledDateTime></ScheduledDateTime>\n" +
                "        <RegDocID></RegDocID>\n" +
                "        <RegDateTime>2025-06-10 02:01:41</RegDateTime>\n" +
                "        <MeasureInfo></MeasureInfo>\n" +
                "        <thirdPartyFlag></thirdPartyFlag>\n" +
                "        <TechnicianID></TechnicianID>\n" +
                "        <ExamDateTime></ExamDateTime>\n" +
                "        <ReporterID>10714</ReporterID>\n" +
                "        <ReportDateTime>2025-6-10 17:12:18</ReportDateTime>\n" +
                "        <VerifyDocID>10714</VerifyDocID>\n" +
                "        <VerifyDateTime>2025-6-10 17:12:38</VerifyDateTime>\n" +
                "        <IssueDocID>10714</IssueDocID>\n" +
                "        <IssueDateTime>2025-6-10 17:12:38</IssueDateTime>\n" +
                "        <Status>4</Status>\n" +
                "        <OperatorDateTime>2025-6-10 17:12:49</OperatorDateTime>\n" +
                "        <PhoneNumber></PhoneNumber>\n" +
                "        <Crisis>N</Crisis>\n" +
                "        <CrisisContent></CrisisContent>\n" +
                "        <AbNormalResult>    </AbNormalResult>\n" +
                "        <RoomDesc></RoomDesc>\n" +
                "        <ExamItemsList>\n" +
                "            <ExaminationItems>\n" +
                "                <Code>D24050820</Code>\n" +
                "                <Name>123123</Name>\n" +
                "                <Value></Value>\n" +
                "            </ExaminationItems>\n" +
                "        </ExamItemsList>\n" +
                "    </ExamReportNew>\n" +
                "</Request>]]>";
    }
}
