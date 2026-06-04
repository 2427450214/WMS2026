package org.example.wms_backend.config;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

/**
 * Spring Security配置类
 * 用于配置应用程序的安全策略，包括认证、授权、密码编码等
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Resource // 自动注入UserDetailsService实现类
    private UserDetailsService userDetailsService;

    @Resource // 自动注入UserMapper
    private org.example.wms_backend.mapper.UserMapper userMapper;

    @Resource // 自动注入BatchService
    private org.example.wms_backend.service.BatchService batchService;

    /**
     * 配置密码编码器Bean
     * 使用BCryptPasswordEncoder进行密码加密，这是Spring Security推荐的密码编码方式
     *
     * @return BCryptPasswordEncoder实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置DaoAuthenticationProvider Bean
     * 用于提供认证功能，将用户名和密码与数据库中的数据进行比较
     *
     * @return 配置好的DaoAuthenticationProvider实例
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // 创建DaoAuthenticationProvider实例
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // 设置用户详情服务，用于从数据库或其他存储中获取用户信息
        provider.setUserDetailsService(userDetailsService);
        // 设置密码编码器，用于对密码进行编码和验证
        provider.setPasswordEncoder(passwordEncoder());
        // 返回配置完成的认证提供者
        return provider;
    }

    /**
     * 配置身份验证管理器
     * 该方法用于创建并返回一个AuthenticationManager实例，这是Spring Security框架中用于处理身份验证的核心组件
     *
     * @param config Spring Security的AuthenticationConfiguration配置对象，用于获取默认的AuthenticationManager
     * @return 返回配置好的AuthenticationManager实例
     * @throws Exception 如果获取AuthenticationManager过程中发生异常，则抛出Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * 配置安全过滤器链
     * 定义了哪些URL需要认证，哪些不需要，以及其他安全设置
     *
     * @param http HttpSecurity对象，用于配置安全策略
     * @return 配置好的SecurityFilterChain实例
     * @throws Exception 配置过程中可能发生的异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> {
                    // 配置会话创建策略
                    session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                            .maximumSessions(1)
                            .maxSessionsPreventsLogin(false) // 允许新登录，旧会话会被失效
                            .expiredUrl("/user/login")
                            .expiredSessionStrategy(new JsonSessionInformationExpiredStrategy());
                })
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/login/**",
                                "/user/register/**",
                                "/user/register",
                                "/api/captcha/**",
                                "/user/reset/**","/user/reset/password").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"code\": 401, \"message\": \"未登录，请先登录\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write("{\"code\": 403, \"message\": \"没有权限访问该资源\"}");
                        })
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/user/login")
                        .usernameParameter("account")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            
                            // 获取用户名（账号）
                            String account = authentication.getName();
                            
                            // 检查是否为管理员
                            boolean isAdmin = authentication.getAuthorities().stream()
                                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
                            
                            // 任何用户登录时都更新已过期的批次状态
                            try {
                                batchService.updateExpiredBatchStatus();
                            } catch (Exception e) {
                                // 忽略更新过期批次的异常
                            }
                            
                            // 构建返回信息
                            String name = "";
                            try {
                                // 从数据库获取用户姓名
                                org.example.wms_backend.entity.User user = userMapper.selectByAccount(account);
                                if (user != null) {
                                    name = user.getName();
                                }
                            } catch (Exception e) {
                                // 忽略异常
                            }
                            
                            // 根据用户类型返回不同信息
                            if (isAdmin) {
                                response.getWriter().write("{\"code\": 200, \"message\": \"登录成功\", \"data\": {\"name\": \"" + name + "\", \"role\": \"管理员\"}}");
                            } else {
                                response.getWriter().write("{\"code\": 200, \"message\": \"登录成功\", \"data\": {\"name\": \"" + name + "\"}}");
                            }
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            String message = "登录失败：账号或密码错误";
                            if (exception != null && exception.getMessage() != null) {
                                if (exception.getMessage().contains("账号已被封禁")) {
                                    message = "登录失败：账号已被封禁";
                                } else if (exception.getMessage().contains("用户不存在")) {
                                    message = "登录失败：用户不存在";
                                }
                            }
                            response.getWriter().write("{\"code\": 401, \"message\": \"" + message + "\"}");
                        })
                )
                .logout(logout -> logout
                        .logoutUrl("/user/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"code\": 200, \"message\": \"退出登录成功\"}");
                        })
                )
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    /**
     * 自定义会话过期策略
     * 当用户会话过期时，返回JSON格式的错误信息
     */
    public class JsonSessionInformationExpiredStrategy implements org.springframework.security.web.session.SessionInformationExpiredStrategy {
        @Override
        public void onExpiredSessionDetected(org.springframework.security.web.session.SessionInformationExpiredEvent event) throws IOException {
            HttpServletResponse response = event.getResponse();
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\": 401, \"message\": \"会话已过期，请重新登录\"}");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    /**
     * 配置CORS（跨域资源共享）
     */
    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowedOrigins(java.util.Arrays.asList("http://localhost:5173", "http://localhost:5174", "http://localhost:5175"));
        configuration.setAllowedMethods(java.util.Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(java.util.Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
