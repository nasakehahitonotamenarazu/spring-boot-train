package com.example.demo.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SpringSecurity6.2設定
 */
@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        // ログイン
        .formLogin(login -> login
            .defaultSuccessUrl("/", true)
            .permitAll()
        // ログアウト
        ).logout(logout -> logout
            .logoutSuccessUrl("/login")
        ).authorizeHttpRequests(authz -> authz
            // item更新画面とitem削除画面はDATA_MANAGERロールを保持しているユーザのみアクセス可能
            .requestMatchers("/WBA0401/**", "/WBA0501/**").hasRole("DATA_MANAGER")
            // その他画面は認証済みが必須
            .anyRequest().authenticated()
        );
        return http.build();

        // 参考：以下、spring security5.7の設定
        // http
        //         .authorizeRequests()
        //         // 開発向け、h2DBコンソールは認証しない
        //         .mvcMatchers("/h2-console/**").permitAll()
        //         // ヘルスチェックURLは認証しない
        //         .mvcMatchers("/actuator/health").permitAll()
        //         // item更新画面とitem削除画面はDATA_MANAGERロールを保持しているユーザのみアクセス可能
        //         .mvcMatchers("/WBA0401/**", "/WBA0501/**").hasRole("DATA_MANAGER")
        //         // その他は認証する
        //         .anyRequest().authenticated()
        //         // ログイン
        //         .and().formLogin().defaultSuccessUrl("/", true)
        //         // ログアウト
        //         .and().logout((logout) -> logout.permitAll())
        //         // 開発向け、h2DBコンソール向け設定
        //         .csrf().ignoringAntMatchers("/h2-console/**")
        //         .and().headers().frameOptions().sameOrigin();
        // return http.build();
    }
}
