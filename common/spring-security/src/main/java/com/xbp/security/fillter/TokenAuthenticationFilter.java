package com.xbp.security.fillter;

import com.alibaba.fastjson.JSON;
import com.xbp.common.jwt.JwtHelper;
import com.xbp.common.result.Result;
import com.xbp.common.result.ResultCodeEnum;
import com.xbp.common.utils.ResponseUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author 14343
 * @version 1.0
 * @description: TODO
 * @date 2024/3/11 10:24
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate=redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
//        logger.info("uri:"+request.getRequestURI());
        //如果是登录接口，直接放行
        if("/admin/system/index/login".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if(null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            ResponseUtil.out(response, Result.fail());
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // token置于header里
        String token = request.getHeader("token");
//        logger.info("token:"+token);
        if (!StringUtils.isEmpty(token)) {
            String username = JwtHelper.getUsername(token);
//            logger.info("useruame:"+useruame);
            if (!StringUtils.isEmpty(username)) {
                String authoritiesString =(String) redisTemplate.opsForValue().get(username);
                List<Map> mapList = JSON.parseArray(authoritiesString, Map.class);
                ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
                for (Map map :mapList) {
                    authorities.add(new SimpleGrantedAuthority((String) map.get("authority")));
                }
                return new UsernamePasswordAuthenticationToken(username, null,authorities);
            }else {
                return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
            }
        }
        return null;
    }

}
