package hu.fourig.partner;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.fourig.partner.config.JwtAuthenticationFilter;
import hu.fourig.partner.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@WithMockUser(value = "admin", authorities = "ADMIN")
public abstract class BaseWebMvcTest {

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() throws Exception {
        doAnswer(invocation -> {
            HttpServletRequest request = invocation.getArgument(0);
            HttpServletResponse response = invocation.getArgument(1);
            FilterChain filterChain = invocation.getArgument(2);
            filterChain.doFilter(request, response);
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
    }

    protected String toJson(Object o) throws Exception {
        return objectMapper.writeValueAsString(o);
    }
}
