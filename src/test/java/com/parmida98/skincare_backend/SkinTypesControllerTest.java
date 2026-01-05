package com.parmida98.skincare_backend;

import com.parmida98.skincare_backend.controller.SkinTypeController;
import com.parmida98.skincare_backend.dto.SkinTypeDTO;
import com.parmida98.skincare_backend.service.SkinTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SkinTypeController.class)
public class SkinTypesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private SkinTypeService skinTypeService;

    @Test
    void getAll_shouldReturnListOfSkinTypes() throws Exception {
        when(skinTypeService.getAllSkinTypes()).thenReturn(List.of(
                new SkinTypeDTO("DRY", "Dry skin", "Dry description"),
                new SkinTypeDTO("OILY", "Oily skin", "Oily description")
        ));

        mockMvc.perform(get("/skin-types").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].label").value("DRY"))
                .andExpect(jsonPath("$[1].label").value("OILY"));
    }
}
