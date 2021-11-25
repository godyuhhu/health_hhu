package edu.hhu.service;

import edu.hhu.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpringSecurityUserService2 implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    public  Map<String, User> map = new HashMap<>();

    public void initData(){
        User user1 = new User();
        user1.setUsername("admin");
        user1.setPassword(passwordEncoder.encode("admin"));
        User user2 = new User();
        user2.setUsername("xiaoMing");
        user2.setPassword(passwordEncoder.encode("1234"));
        map.put(user1.getUsername(),user1);
        map.put(user2.getUsername(),user2);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        initData();
        System.out.println(username);
        User user = map.get(username);
        System.out.println(user);
        if (user == null){
            return null;
        }else{
            List<GrantedAuthority> list = new ArrayList<>();
            list.add(new SimpleGrantedAuthority("add"));
            list.add(new SimpleGrantedAuthority("delete"));
            if ("admin".equals(username)){
                list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
            System.out.println(user.getPassword());
            org.springframework.security.core.userdetails.User springSecurityUser = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),list);
            return springSecurityUser;
        }
    }
}
