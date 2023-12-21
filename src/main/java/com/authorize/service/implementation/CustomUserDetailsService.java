package com.authorize.service.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.authorize.model.entity.Previlage;
import com.authorize.model.entity.Role;
import com.authorize.model.entity.User;
import com.authorize.repository.RoleRepository;
import com.authorize.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		Optional<User> credential = Optional.ofNullable(userRepository.findByName(username));
//		log.info(credential.toString());
//		return credential.map(CustomUserDetails::new)
//				.orElseThrow(() -> new UsernameNotFoundException("user not found with name :" + username));
//	}
	
	@Override
    public UserDetails loadUserByUsername(String name)
      throws UsernameNotFoundException {
 
        User user = userRepository.findByName(name);
        if (user == null) {
            return new org.springframework.security.core.userdetails.User(
              " ", " ", true, true, true, true, 
              getAuthorities(Arrays.asList(
                roleRepository.findByName("ROLE_SUPPORTER"))));
        }

        return new org.springframework.security.core.userdetails.User(
          user.getName(), user.getPassword(), true, true, true, 
          true, getAuthorities(user.getRole()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
      Collection<Role> roles) {
 
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {
    	log.info("obtaining previlages");
        List<String> privileges = new ArrayList<>();
        List<Previlage> collection = new ArrayList<>();
        for (Role role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrevilage());
        }
        for (Previlage item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
    	log.info("granting previlages");
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
	
	
}