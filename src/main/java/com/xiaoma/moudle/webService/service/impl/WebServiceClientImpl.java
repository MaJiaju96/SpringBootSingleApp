package com.xiaoma.moudle.webService.service.impl;

import com.xiaoma.moudle.webService.service.DhcService;
import com.xiaoma.moudle.webService.service.WebServiceClient;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.pdfbox.util.Charsets;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
@Service
public class WebServiceClientImpl implements WebServiceClient {

    @SneakyThrows
    public static void main(String[] args) {
//        String str = sendSoapRequest();
//        System.out.println(str);

        String str = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.boot.spdrt.com/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <ser:getMedicalStaffInformation>\n" +
                "         <!--Optional:-->\n" +
                "         <arg0>ExamReportNew</arg0>\n" +
                "         <!--Optional:-->\n" +
                "         <arg1><![CDATA[<Request>\n" +
                " <ExamReportNew>\n" +
                "  <CheckFlow>15281626346230643670</CheckFlow>\n" +
                "  <CheckLink>http://192.168.2.89/ClinicRptViewFSCT/2018-06-05/0007097691_蒋熙雯/C20180605-504/C20180605-504.html</CheckLink>\n" +
                "  <ExecLink>http://192.168.2.7/RoganViewPro-X/PacsConnect/VPXShowGUI.aspx?UserName=administrator&Password=XYPACS&Action=ShowImageViewer&AccessionNumber=C20180605-504</ExecLink>\n" +
                "</ExamReportNew>\n" +
                "</Request> ]]></arg1>\n" +
                "      </ser:getMedicalStaffInformation>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        String result = sendWebServiceRequest("http://192.168.32.4:9898/ws/spdrt/xy?wsdl" , "" , str);
        System.out.println(result);
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


    public static String sendWebServiceRequest(String soapEndpointUrl, String soapAction, String xmlRequest) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(soapEndpointUrl).openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
        connection.setRequestProperty("Content-Length", String.valueOf(xmlRequest.getBytes(Charsets.UTF_8).length));
        if (StringUtils.isNotBlank(soapAction)){
            connection.setRequestProperty("SOAPAction", soapAction);
        }

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(xmlRequest.getBytes(Charsets.UTF_8));
        outputStream.close();

        // 读取响应内容
        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }
}
