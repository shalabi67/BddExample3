package com.bdd.tests.integration;

import com.bdd.stylists.Stylist;
import com.bdd.stylists.StylistController;
import com.bdd.tests.factory.StylistSystem;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class IntegrationStylistSystem extends StylistSystem {
    private MockMvc mockMvc;

    public IntegrationStylistSystem(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Override
    public ResponseEntity<Stylist> addStylist(Stylist stylist) {
        JsonMapper<Stylist> jsonMapper = new JsonMapper<>();
        MvcResult mvcResult = null;
        try {
            mvcResult = this.mockMvc
                    .perform(
                            post(StylistController.URL)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonMapper.toString(stylist))
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andReturn();
            //TODO: String content = mvcResult.getResponse().getContentAsString();
            //TODO: notice we are returning the content, because we are not testing it. this part is ignored, IK can do it later if needed.
            return new ResponseEntity<>(HttpStatus.valueOf(mvcResult.getResponse().getStatus()));
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
