package com.orjrs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orjrs.entity.User;
import com.orjrs.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void login() throws Exception {
        // 准备测试数据
        User user = new User();
        user.setId("1");
        user.setOpenId("test_open_id");
        user.setNickname("测试用户");
        user.setAvatar("https://example.com/avatar.jpg");
        user.setPhone("13800138000");
        user.setGender("male");
        user.setStatus("active");
        user.setLastLoginTime(LocalDateTime.now());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        given(userService.login(any())).willReturn(user);

        // 执行测试并生成文档
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/user/login")
                .param("openId", "test_open_id")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("user-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("openId").description("微信openId")
                        ),
                        responseFields(
                                fieldWithPath("code").description("响应码"),
                                fieldWithPath("message").description("响应消息"),
                                fieldWithPath("data").description("用户信息"),
                                fieldWithPath("data.id").description("用户ID"),
                                fieldWithPath("data.openId").description("微信openId"),
                                fieldWithPath("data.nickname").description("昵称"),
                                fieldWithPath("data.avatar").description("头像"),
                                fieldWithPath("data.phone").description("手机号"),
                                fieldWithPath("data.gender").description("性别"),
                                fieldWithPath("data.status").description("状态"),
                                fieldWithPath("data.lastLoginTime").description("最后登录时间"),
                                fieldWithPath("data.createTime").description("创建时间"),
                                fieldWithPath("data.updateTime").description("更新时间"),
                                fieldWithPath("data.token").description("用户令牌")
                        )));
    }

    @Test
    public void updateUserInfo() throws Exception {
        // 准备测试数据
        User user = new User();
        user.setId("1");
        user.setNickname("新昵称");
        user.setAvatar("https://example.com/new-avatar.jpg");
        user.setGender("female");
        user.setUpdateTime(LocalDateTime.now());

        given(userService.updateUserInfo(any())).willReturn(user);

        // 执行测试并生成文档
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andDo(document("user-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("id").description("用户ID"),
                                fieldWithPath("nickname").description("昵称").optional(),
                                fieldWithPath("avatar").description("头像").optional(),
                                fieldWithPath("gender").description("性别").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("响应码"),
                                fieldWithPath("message").description("响应消息"),
                                fieldWithPath("data").description("用户信息"),
                                fieldWithPath("data.id").description("用户ID"),
                                fieldWithPath("data.nickname").description("昵称"),
                                fieldWithPath("data.avatar").description("头像"),
                                fieldWithPath("data.gender").description("性别"),
                                fieldWithPath("data.updateTime").description("更新时间")
                        )));
    }
} 