package com.example.parking.Configuration;

import com.example.parking.Service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringConfiguration {

    private final MyUserDetailsService myUserDetailsService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests()
                .requestMatchers("api/v1/auth/register/admin",
                        "api/v1/auth/register/company",
                        "api/v1/auth/register/customer",
                        "api/v1/branch/get",
                        "api/v1/parking/get",
                        "api/v1/parking/get-time/{branchId}",
                        "api/v1/parking//get-not-available/{branchId}",
                        "api/v1/parking//get-available/{branchId}").permitAll()
                .requestMatchers("api/v1/auth/admin",
                        "api/v1/auth/get-users",
                        "api/v1/customer/get",
                        "api/v1/car/get",
                        "api/v1/booking/get",
                        "api/v1/company/get",
                        "api/v1/company/delete/{companyId}",
                        "api/v1/company/update-status/{companyId}/{status}").hasAuthority("ADMIN")
                .requestMatchers("api/v1/auth/customer",
                        "api/v1/customer/update/{customerId}",
                        "api/v1/customer/delete/{customerId}",
                        "api/v1/customer/get-cars",
                        "api/v1/customer/details",
                        "api/v1/customer/pay-fine",
                        "api/v1/customer/get-booking",
                        "api/v1/customer/get-old-booking",
                        "api/v1/car/add",
                        "api/v1/car/update/{carId}",
                        "api/v1/car/delete/{carId}",
                        "api/v1/booking/parking",
                        "api/v1/booking/update/{bookingId}",
                        "api/v1/booking/cancel/{bookingId}",
                        "api/v1/booking/checkout/{bookingId}",
                        "api/v1/booking/checkout/{bookingId}").hasAuthority("CUSTOMER")
                .requestMatchers("api/v1/auth/company",
                        "api/v1/company/update/{companyId}",
                        "api/v1/company/get-branch",
                        "api/v1/company/details",
                        "api/v1/branch/add",
                        "api/v1/branch/update/{branchId}",
                        "api/v1/branch/delete/{branchId}",
                        "api/v1/parking/add/{branchId}",
                        "api/v1/parking/update/{branchId}/{parkingId}",
                        "api/v1/parking/delete/{branchId}/{parkingId}").hasAuthority("COMPANY")
                .anyRequest().authenticated()
                .and()
                .logout().logoutUrl("api/v1/auth/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .httpBasic();
        return http.build();
    }
}
