package com.itmayiedu.itmayiedushopppay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.itmayiedu.common.util.CommonUtil;
import com.itmayiedu.itmayiedushopppay.common.RedisUtil;
import com.itmayiedu.itmayiedushopppay.sdk.ali.AlipayNotifyParam;
import com.itmayiedu.itmayiedushopppay.sdk.ali.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.Executors.*;
import static java.util.concurrent.Executors.newCachedThreadPool;

/**
 * @author Administrator
 * @Auther: Administrator
 * @Date: 2018/12/13 10:29
 * @Description: 支付宝支付
 */
@Slf4j
@RestController
@RequestMapping("alipay")
public class AlipayController {
    @Autowired
    private RedisUtil redisUtil;
    private final Logger logger = LoggerFactory.getLogger(AlipayController.class);

    private ExecutorService pool;

    public static AlipayClient alipayClient = null;

    public AlipayController() {
        pool = newFixedThreadPool(10);
    }

    @PostConstruct
    public static void init(){
        alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", Constants.APPID, Constants.PRIVATE_KEY, "json",
                Constants.ENCODING, Constants.PUBLIC_KEY, "RSA2");
        System.out.println("SDK初始化开始");
    }
    @RequestMapping("pay")
    public void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //创建API对应的request
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        //在公共参数中设置回跳和通知地址
        alipayRequest.setReturnUrl("http://yhbp.natapp1.cc/alipay/complete");
        alipayRequest.setNotifyUrl("http://yhbp.natapp1.cc/alipay/payResponse");
        //商户订单号
        String out_trade_no = CommonUtil.getUUID().substring(0,16);
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\""+out_trade_no+"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":0.01," +
                "    \"subject\":\"Iphone6 16G\"," +
                "    \"body\":\"Iphone6 16G\"," +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\""+Constants.PID+"\"" +
                "    }"+
                "  }");//填充业务参数
        String form="";
        try {
            redisUtil.set(Long.toString(System.currentTimeMillis()),out_trade_no);
            //调用SDK生成表单
            form = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=" + Constants.ENCODING);
        //直接将完整的表单html输出到页面
        response.getWriter().write(form);
        response.getWriter().flush();
        response.getWriter().close();
    }

    @RequestMapping("complete")
    public ModelAndView complete(){
        return new ModelAndView("complete");
    }

    public static void main(String[] args) {
        // 支付宝退款测试
        String result = "";
        result = AlipayController.refundOrder("1cbebe3e790f486c",CommonUtil.getUUID(),0.01);
        System.out.println(result);
    }

    // 支付宝退款测试使用
    public static String refundOrder(String out_trade_no,String out_request_no,Double refund_amount){
        alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", Constants.APPID, Constants.PRIVATE_KEY, "json",
                Constants.ENCODING, Constants.PUBLIC_KEY, "RSA2");
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizContent("{" +
                "\"out_trade_no\":\""+out_trade_no+"\"," +
                "\"refund_amount\":"+refund_amount+"," +
                "\"refund_reason\":\"正常退款\"," +
                "\"out_request_no\":\""+out_request_no+"\"," +
                "\"org_pid\":\""+Constants.PID+"\"" +
                "  }");
        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("退款调用成功");
            return "success";
        } else {
            System.out.println("退款调用失败");
            return "fail";
        }
    }

    /**
     *
     * 功能描述: 支付宝服务端回调
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2018/12/14 11:44
     */
    @RequestMapping("payResponse")
    public String payResponse(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
        Map<String, String> params = convertRequestParamsToMap(request); // 将异步通知中收到的待验证所有参数都存放到map中
        String paramsJson = JSON.toJSONString(params);
        logger.info("支付宝回调，{}", paramsJson);
        try {
            // 调用SDK验证签名
            boolean signVerified = AlipaySignature.rsaCheckV1(params, Constants.PUBLIC_KEY,
                    Constants.ENCODING, Constants.SIGNTYPE);
            if (signVerified) {
                logger.info("支付宝回调签名认证成功");
                // 按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
                this.check(params);
                // 另起线程处理业务
                    pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        AlipayNotifyParam param = buildAlipayNotifyParam(params);
                        String trade_status = param.getTradeStatus();
                        logger.info("此时的订单状态为:::"+trade_status);
                        // 支付成功
                        if (trade_status.equals("TRADE_SUCCESS")) {
                            // 处理支付成功逻辑
                            try {
                                //*
                                    // 处理业务逻辑。。。
                                    //myService.process(param);
                                    logger.info("此处处理业务逻辑即可");
                                //*

                            } catch (Exception e) {
                                logger.error("支付宝回调业务处理报错,params:" + paramsJson, e);
                            }
                        } else {
                            logger.error("没有处理支付宝回调业务，支付宝交易状态：{},params:{}",trade_status,paramsJson);
                        }
                    }
                });
                // 如果签名验证正确，立即返回success，后续业务另起线程单独处理
                // 业务处理失败，可查看日志进行补偿，跟支付宝已经没多大关系。
                return "success";
            } else {
                logger.info("支付宝回调签名认证失败，signVerified=false, paramsJson:{}", paramsJson);
                return "failure";
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝回调签名认证失败,paramsJson:{},errorMsg:{}", paramsJson, e.getMessage());
            return "failure";
        }
    }

    /***
     * @Author 浪里小白龙
     * @Description //TODO 检验异步回调参数是否为支付宝回传
     * @Date 2018/12/14
     * @Param [params]
     * @return void
     **/

    private void check(Map<String, String> params) throws AlipayApiException{
        /*String outTradeNo = params.get("out_trade_no");
        // 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
        Order order = getOrderByOutTradeNo(outTradeNo); // 这个方法自己实现
        if (order == null) {
            throw new AlipayApiException("out_trade_no错误");
        }
        // 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
        long total_amount = new BigDecimal(params.get("total_amount")).multiply(new BigDecimal(100)).longValue();
        if (total_amount != order.getPayPrice().longValue()) {
            throw new AlipayApiException("error total_amount");
        }
        // 3、校验通知中的seller_id（或者seller_email)是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
        // 第三步可根据实际情况省略
        // 4、验证app_id是否为该商户本身。
        if (!params.get("app_id").equals(alipayConfig.getAppid())) {
            throw new AlipayApiException("app_id不一致");
        }*/
    }

    private Map<String,String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap<String, String>();
        Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();

        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            int valLen = values.length;

            if (valLen == 1) {
                retMap.put(name, values[0]);
            } else if (valLen > 1) {
                StringBuilder sb = new StringBuilder();
                for (String val : values) {
                    sb.append(",").append(val);
                }
                retMap.put(name, sb.toString().substring(1));
            } else {
                retMap.put(name, "");
            }
        }
        return retMap;
    }

    private AlipayNotifyParam buildAlipayNotifyParam(Map<String, String> params) {
        String json = JSON.toJSONString(params);
        return JSON.parseObject(json, AlipayNotifyParam.class);
    }
}
