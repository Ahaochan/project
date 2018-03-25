<%--<%@page contentType="text/html; charset=UTF-8"%>--%>
<%-- 此文件用于一些非常敏感的JSP，其内容快速失效，且需要立即刷新 --%>
<%
    response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
    response.setHeader("Pragma","no-cache"); //HTTP 1.0
    response.setDateHeader("Expires", -1); //prevents caching at the proxy server // -1会导致oscache不缓存
%>