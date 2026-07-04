package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.config.SecurityConfig;
import com.polarbookshop.catalogservice.domain.BookNotFoundException;
import com.polarbookshop.catalogservice.domain.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest( BookController.class )
@Import( SecurityConfig.class )
public class BookControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    JwtDecoder jwtDecoder;

    @MockitoBean
    private BookService bookService;

    @Test
    public void whenGetBookNotExistingThenShouldReturn404() throws Exception {
        String isbn = "73737313940";
        given( bookService.bookDetails( isbn ) )
            .willThrow( BookNotFoundException.class );

        mockMvc
            .perform( get( "/books/" + isbn ) )
            .andExpect( status().isNotFound() );
    }

    @Test
    public void whenDeleteBookWithEmployeeRoleThenShouldReturn204() throws Exception {
        String isbn = "7373731394";
        mockMvc
            .perform(
                delete( "/books/" + isbn )
                    .with( jwt().authorities( new SimpleGrantedAuthority( "ROLE_employee" ) ) )
            )
            .andExpect( status().isNoContent() );
    }

    @Test
    public void whenDeleteBookWithCustomerRoleThenShouldReturn403() throws Exception {
        String isbn = "7373731394";
        mockMvc
            .perform(
                delete( "/books/" + isbn )
                    .with( jwt().authorities( new SimpleGrantedAuthority( "ROLE_customer" ) ) )
            )
            .andExpect( status().isForbidden() );
    }

    @Test
    public void whenDeleteBookNotAuthenticatedThenShouldReturn401() throws Exception {
        String isbn = "7373731394";
        mockMvc
            .perform( delete( "/books/" + isbn ) )
            .andExpect( status().isUnauthorized() );
    }
}
