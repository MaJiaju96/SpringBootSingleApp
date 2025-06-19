package com.xiaoma.moudle.webService.config;

import com.xiaoma.moudle.webService.service.impl.DhcServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;

@Configuration
public class WebServiceConfig {

    @Autowired
    private Bus bus;

    @Bean
    public Endpoint endpoint() {
        System.out.println("【WebService】正在发布服务...");
        EndpointImpl endpoint = new EndpointImpl(bus, new DhcServiceImpl());

        QName serviceName = new QName("http://tempuri.org", "DhcService");
        endpoint.setServiceName(serviceName);

        endpoint.publish("/DHC.Published.Report.BS.Report.CLS");
        System.out.println("【WebService】服务已发布至路径：/DHC.Published.Report.BS.Report.CLS");

        return endpoint;
    }


}
