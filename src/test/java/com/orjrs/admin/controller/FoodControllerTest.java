package com.orjrs.admin.controller;

import com.orjrs.miniapp.controller.FoodController;
import com.orjrs.miniapp.entity.Food;
import com.orjrs.miniapp.service.FoodService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FoodController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class FoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FoodService foodService;

    @Test
    public void getFoodList() throws Exception {
        // 准备测试数据
        Food food = new Food();
        food.setId("1");
        food.setName("烤羊肉串");
        food.setPrice(new BigDecimal("6.00"));
        food.setDescription("新疆羔羊肉，鲜嫩多汁");
        food.setCategoryId("1");
        food.setStatus("on");
        food.setSales(1000);
        food.setRating(new BigDecimal("4.8"));

        List<Food> foodList = Arrays.asList(food);
        given(foodService.getFoodsByCategory(any())).willReturn(foodList);

        // 执行测试并生成文档
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/food/list")
                .param("categoryId", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("food-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("categoryId").description("分类ID").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("响应码"),
                                fieldWithPath("message").description("响应消息").optional(),
                                fieldWithPath("data").description("食品列表"),
                                fieldWithPath("data[].id").description("食品ID"),
                                fieldWithPath("data[].name").description("食品名称"),
                                fieldWithPath("data[].price").description("价格"),
                                fieldWithPath("data[].description").description("描述"),
                                fieldWithPath("data[].categoryId").description("分类ID"),
                                fieldWithPath("data[].status").description("状态"),
                                fieldWithPath("data[].sales").description("销量"),
                                fieldWithPath("data[].rating").description("评分"),
                                fieldWithPath("data[].image").description("图片").optional(),
                                fieldWithPath("data[].createTime").description("创建时间").optional(),
                                fieldWithPath("data[].updateTime").description("更新时间").optional()
                        )));
    }
} 