package edu.hhu.service;

import edu.hhu.domain.Permission;
import edu.hhu.domain.Role;
import edu.hhu.domain.User;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 基于springSecurity实现用户登录操作
 */
@Service
public class SpringSecurityUserService implements UserDetailsService {
    @Reference
    private IUserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByName(username);
       // System.out.println(user);
        if (user == null){
            return null;
        }else{
            //返回SpringSecurity框架所需要的用户对象
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            //获得该用户所具有的角色
            Set<Role> roles = user.getRoles();
            for (Role role : roles) {
                //将用户对应的角色封装
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getKeyword()));
                //获取该角色对所具有的权限
                Set<Permission> permissions = role.getPermissions();
                for (Permission permission : permissions) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(permission.getKeyword()));
                }
            }
            //System.out.println(grantedAuthorities);
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            System.out.println(bCryptPasswordEncoder.encode("1234"));
            org.springframework.security.core.userdetails.User springSecurityUser = new org.springframework.security.core.userdetails.User(username,user.getPassword(),grantedAuthorities);
            return springSecurityUser;
        }

    }
}
