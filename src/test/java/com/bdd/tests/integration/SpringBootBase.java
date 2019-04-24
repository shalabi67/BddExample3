package com.bdd.tests.integration;

import com.bdd.BddExample3Application;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= BddExample3Application.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes= BddExample3Application.class)
public class SpringBootBase {
    @Autowired
    protected MockMvc mockMvc;
}
