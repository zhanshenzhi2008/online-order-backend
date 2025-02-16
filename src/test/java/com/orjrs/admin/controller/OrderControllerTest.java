package com.orjrs.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orjrs.miniapp.controller.OrderController;
import com.orjrs.miniapp.entity.Order;
import com.orjrs.miniapp.service.OrderService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    public void createOrder() throws Exception {
        // 准备测试数据
        Order order = new Order();
        order.setId("1");
        order.setOrderNo("ORDER1234567890");
        order.setUserId("user1");
        order.setShopId("shop1");
        order.setAddressId("addr1");
        order.setTotalAmount(new BigDecimal("100.00"));
        order.setPayAmount(new BigDecimal("100.00"));
        order.setStatus("pending");
        order.setStatusText("待付款");
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        given(orderService.createOrder(any())).willReturn(order);

        // 执行测试并生成文档
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/order/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andDo(document("order-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("id").description("订单ID").optional(),
                                fieldWithPath("orderNo").description("订单号").optional(),
                                fieldWithPath("userId").description("用户ID"),
                                fieldWithPath("shopId").description("商店ID"),
                                fieldWithPath("addressId").description("地址ID"),
                                fieldWithPath("totalAmount").description("总金额"),
                                fieldWithPath("payAmount").description("支付金额"),
                                fieldWithPath("status").description("状态").optional(),
                                fieldWithPath("statusText").description("状态文本").optional(),
                                fieldWithPath("paymentMethod").description("支付方式").optional(),
                                fieldWithPath("payTime").description("支付时间").optional(),
                                fieldWithPath("remark").description("备注").optional(),
                                fieldWithPath("createTime").description("创建时间").optional(),
                                fieldWithPath("updateTime").description("更新时间").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("响应码"),
                                fieldWithPath("message").description("响应消息").optional(),
                                fieldWithPath("data").description("订单信息"),
                                fieldWithPath("data.id").description("订单ID"),
                                fieldWithPath("data.orderNo").description("订单号"),
                                fieldWithPath("data.userId").description("用户ID"),
                                fieldWithPath("data.shopId").description("商店ID"),
                                fieldWithPath("data.addressId").description("地址ID"),
                                fieldWithPath("data.totalAmount").description("总金额"),
                                fieldWithPath("data.payAmount").description("支付金额"),
                                fieldWithPath("data.status").description("状态"),
                                fieldWithPath("data.statusText").description("状态文本"),
                                fieldWithPath("data.paymentMethod").description("支付方式").optional(),
                                fieldWithPath("data.payTime").description("支付时间").optional(),
                                fieldWithPath("data.remark").description("备注").optional(),
                                fieldWithPath("data.createTime").description("创建时间"),
                                fieldWithPath("data.updateTime").description("更新时间")
                        )));
    }

    @Test
    public void getOrderList() throws Exception {
        // 准备测试数据
        Order order = new Order();
        order.setId("1");
        order.setOrderNo("ORDER1234567890");
        order.setUserId("user1");
        order.setShopId("shop1");
        order.setAddressId("addr1");
        order.setTotalAmount(new BigDecimal("100.00"));
        order.setPayAmount(new BigDecimal("100.00"));
        order.setStatus("pending");
        order.setStatusText("待付款");
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        List<Order> orderList = Arrays.asList(order);
        given(orderService.getOrderList(any(), any())).willReturn(orderList);

        // 执行测试并生成文档
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/order/list")
                .param("userId", "user1")
                .param("status", "pending")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("order-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("userId").description("用户ID"),
                                parameterWithName("status").description("订单状态").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("响应码"),
                                fieldWithPath("message").description("响应消息").optional(),
                                fieldWithPath("data").description("订单列表"),
                                fieldWithPath("data[].id").description("订单ID"),
                                fieldWithPath("data[].orderNo").description("订单号"),
                                fieldWithPath("data[].userId").description("用户ID"),
                                fieldWithPath("data[].shopId").description("商店ID"),
                                fieldWithPath("data[].addressId").description("地址ID"),
                                fieldWithPath("data[].totalAmount").description("总金额"),
                                fieldWithPath("data[].payAmount").description("支付金额"),
                                fieldWithPath("data[].status").description("状态"),
                                fieldWithPath("data[].statusText").description("状态文本"),
                                fieldWithPath("data[].paymentMethod").description("支付方式").optional(),
                                fieldWithPath("data[].payTime").description("支付时间").optional(),
                                fieldWithPath("data[].remark").description("备注").optional(),
                                fieldWithPath("data[].createTime").description("创建时间"),
                                fieldWithPath("data[].updateTime").description("更新时间")
                        )));
    }
} 