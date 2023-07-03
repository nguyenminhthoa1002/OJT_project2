package project.security_jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
//         Get AuthenticationManager bean
        return super.authenticationManagerBean();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        // Password encoder, để Spring Security sử dụng mã hóa mật khẩu người dùng
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService) // Cung cap customUserDetailService cho spring security
                .passwordEncoder(passwordEncoder()); // cung cấp password encoder
    }
//    @Bean
//    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
//        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
//        return (userRequest) -> {
//            OAuth2User oAuth2User = delegate.loadUser(userRequest);
//            return oAuth2User;
//        };
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors() // Ngăn chặn request từ một domain khác
                .and().csrf().disable()
                .authorizeRequests()
//                User
                .antMatchers("/api/v1/users/register").permitAll()// Cho phép tất cả mọi người truy cập vào địa chỉ này
                .antMatchers("/api/v1/users/signIn").permitAll()
                .antMatchers("/api/v1/users/**").permitAll()
                .antMatchers("/api/v1/**").permitAll()

//                Category
                .antMatchers("/api/v1/catalog/**").permitAll()
                .antMatchers("/api/v1/slider/**").permitAll()
                .antMatchers("/api/v1/product/**").permitAll()
                .antMatchers("/api/v1/flashSale/**").permitAll()
                .antMatchers("/api/v1/blog/**").permitAll()
                .antMatchers("/api/v1/comment/**").permitAll()
                .antMatchers("/api/v1/blog/**").permitAll()
                .antMatchers("/api/v1/catalogOfBlog/**").permitAll()
                .antMatchers("/api/v1/blog/**").permitAll()
                .antMatchers("/api/v1/coupon/**").permitAll()
                 .antMatchers("/api/v1/location/**").permitAll();


//                .anyRequest().authenticated().and()
//                .oauth2Login()
//                .loginPage("/signIn")
//                .defaultSuccessUrl("/api/v1/users/success/profile")
//                .userInfoEndpoint()
//                .userService(oAuth2UserService());

        // Thêm một lớp Filter kiểm tra jwt
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
