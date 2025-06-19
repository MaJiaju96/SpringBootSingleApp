package com.xiaoma.moudle.webService.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(targetNamespace = "http://tempuri.org")
public interface DhcService {

    @WebMethod(operationName = "DhcService")
    @WebResult(name = "DhcServiceResult", targetNamespace = "http://tempuri.org")
    String dhcService(
        @WebParam(name = "input1", targetNamespace = "http://tempuri.org") String input1,
        @WebParam(name = "input2") String input2
    );
}
