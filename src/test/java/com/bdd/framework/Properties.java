package com.bdd.framework;

import org.springframework.test.web.servlet.MockMvc;

public class Properties {
    private MockMvc mockMvc;

    public MockMvc getMockMvc() {
        return mockMvc;
    }

    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }
}
