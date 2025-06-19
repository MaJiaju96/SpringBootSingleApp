package com.xiaoma.moudle.webService.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.rpc.ParameterMode;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class WebServiceUtil {
    /**
     * 创建webservice连接客户端
     *
     * @param webserviceUrl webservice地址
     * @param methodName    方法名
     * @param params        参数数组
     * @return String
     * @throws Exception
     */
    public static String getXmlStringByWebserviceWSDL(String webserviceUrl, String nameSpace , String methodName, Object... params) throws Exception {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(webserviceUrl + "?wsdl");

        // 添加日志拦截器
        client.getInInterceptors().add(new org.apache.cxf.interceptor.LoggingInInterceptor());
        client.getOutInterceptors().add(new org.apache.cxf.interceptor.LoggingOutInterceptor());

        QName operationName = new QName(nameSpace, methodName);
        Object[] invoke = client.invoke(operationName, params.clone());
        return invoke.length > 0 ? invoke[0].toString() : null;
    }

    /**
     * 通信并获取数据文本
     *
     * @param xmlString
     * @return Document
     * @throws Exception
     */
    public static Document getDocumentFromXmlString(String xmlString) throws Exception {
        String replace = "";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        if ((StringUtils.isNotBlank(xmlString) && xmlString.contains("<2>")) || (StringUtils.isNotBlank(xmlString) && xmlString.contains("</2>"))) {
            replace = xmlString.replace("<2>", "").replace("</2>", "");
        } else {
            replace = xmlString;
        }
        Document document = builder.parse(new InputSource(new StringReader(replace)));
        return document;
    }

    public static <T> List<T> jsonToObjectsByJsonPath(String json, Map<String, String> columnMap, Class<T> valueType, String jsonPathExpression) throws Exception {
        if (columnMap == null || columnMap.isEmpty()) {
            throw new RuntimeException("未查到映射关系");
        }
        List<T> resultList = new ArrayList<>();
        String path = "$" + jsonPathExpression.replace("/", ".");
        List<JSONObject> list = (List<JSONObject>) JSONPath.read(json, path);
        if (CollectionUtils.isEmpty(list)) {
            throw new RuntimeException("json解析没有接收到数据或解析错误，解析路径为:" + jsonPathExpression);
        }
        for (JSONObject jsonObj : list) {
            T resultObj = valueType.getDeclaredConstructor().newInstance();
            processElement(jsonObj, resultObj, columnMap);
            resultList.add(resultObj);
        }
        return resultList;
    }

    private static <T> void processElement(JSONObject jsonObj, T resultObj, Map<String, String> tagToFieldMap) {
        Map<String, Object> innerMap = jsonObj.getInnerMap();
        for (Map.Entry<String, Object> entry : innerMap.entrySet()) {
            String tagName = entry.getKey();
            String fieldName = tagToFieldMap.get(tagName);
            if (!StringUtils.isEmpty(fieldName)) {
                String textContent = entry.getValue() == null ? null : entry.getValue().toString().trim();
                try {
                    Field field = resultObj.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    if (StringUtils.isEmpty(textContent) || "null".equalsIgnoreCase(textContent)) {
                        field.set(resultObj, null);
                    } else if (field.getType().equals(Integer.class)) {
                        field.set(resultObj, Integer.parseInt(textContent));
                    } else if (field.getType().equals(Date.class)) {
                        try {
                            field.set(resultObj, TimeComputationUtil.timeCreator(textContent));
                        } catch (Exception e) {
                            throw new RuntimeException("时间格式转换异常:" + resultObj + "------" + e.getMessage());
                        }
                    } else {
                        field.set(resultObj, textContent);
                    }
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException("未找到 " + resultObj.getClass().getName() + " 的 " + fieldName + " 属性，请确认配置是否正确");
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static String getXmlStringByWebserviceCall(String webserviceUrl, String targetNamespace, String methodName, Map<Object, Object> map, Integer SOAPActionURI) {
        List<String> fieldList = new ArrayList<>();
        List<String> valueList = new ArrayList<>();
        for (Map.Entry entry : map.entrySet()) {
            fieldList.add(String.valueOf(entry.getKey()));
            valueList.add(String.valueOf(entry.getValue()));
        }
        String ref = null;
        //获取域名地址，server定义的
        String soapaction = StringUtils.isEmpty(targetNamespace) ? "http://tempuri.org/" : targetNamespace;
        String msg = "";

        Service service = new Service();
        try {
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(webserviceUrl);
            // 设置要调用哪个方法
            call.setOperationName(new QName(soapaction, methodName));
//             设置要传递的参数名
            for (String field : fieldList) {
                call.addParameter(new QName(soapaction, field), XMLType.XSD_STRING, ParameterMode.IN);
            }
//             提供标准类型
            call.setReturnType(XMLType.XSD_STRING);
            call.setUseSOAPAction(true);
            long t1 = System.currentTimeMillis();
            // 调用方法并传递参数
            try {
                if (SOAPActionURI == 1) {
                    call.setSOAPActionURI(soapaction + methodName);
                }
//                call.setTimeout(1000);
                ref = (String) call.invoke(valueList.toArray());
            } catch (Exception e) {
                msg = "原因: " + e.getMessage();
                call.setSOAPActionURI(soapaction + methodName);
                ref = (String) call.invoke(valueList.toArray());
                log.info("webservice-call 调用-" + methodName + "-耗时" + (System.currentTimeMillis() - t1) + "ms");
            }
            return ref;
        } catch (Exception e) {
            throw new RuntimeException(msg + "原因: " + e.getMessage());
        }
    }

    static String[] errorMsgs = {"不存在", "不足", "超时", "异常", "错误", "失败", "fail", "error", "无法", "未找到", "无效", "不能", "未知"};

    public static boolean containsErrorMsg(String inputString) {
        if (inputString.length() < 50) {
            for (String errorMsg : errorMsgs) {
                if (inputString.contains(errorMsg)) {
                    return true;
                }
            }
        }
        return false;
    }
}
