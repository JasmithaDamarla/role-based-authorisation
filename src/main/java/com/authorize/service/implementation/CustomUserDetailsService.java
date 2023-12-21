package com.authorize.service.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.authorize.exceptions.UnAuthorizedException;
import com.authorize.model.entity.Privilege;
import com.authorize.model.entity.Role;
import com.authorize.model.entity.User;
import com.authorize.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
//	@Autowired
//	private RoleRepository roleRepository;

//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		Optional<User> credential = Optional.ofNullable(userRepository.findByName(username));
//		log.info(credential.toString());
//		return credential.map(CustomUserDetails::new)
//				.orElseThrow(() -> new UsernameNotFoundException("user not found with name :" + username));
//	}

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

		User user = userRepository.findByName(name);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + name);
		}
		String orgsName = getOrganizationNameFromRequest();
		if (!hasRequiredPrivileges(user, orgsName)) {
			log.info("orgs not matched");
			throw new UnAuthorizedException("User does not have access to the specified organization");
		}

		return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(),
				getAuthorities(user.getRole()));
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {

		return getGrantedAuthorities(getPrivileges(roles));
	}

	private List<String> getPrivileges(Collection<Role> roles) {
		log.info("obtaining previlages");
		List<String> privileges = new ArrayList<>();
		List<Privilege> collection = new ArrayList<>();
		for (Role role : roles) {
			privileges.add(role.getName());
			collection.addAll(role.getPrivilege());
		}
		for (Privilege item : collection) {
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

	private boolean hasRequiredPrivileges(User user, String organizationName) {
//		user.getOrganization().getUsers().forEach(u->{
//			log.info("{}",u.getName());
//		});
//		log.info("{}",user.getOrganization().getUsers().contains(user.getReportingTo()));
		return user.getOrganization().getName().equals(organizationName) || organizationName=="";
//				|| user.getOrganization().getUsers().contains(user.getReportingTo());
	}

	private String getOrganizationNameFromRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes != null) {
			String organizationName = attributes.getRequest().getParameter("name");
			log.info("orgs name {}", organizationName);
			return organizationName != null ? organizationName : "";
		}
		return "";
	}
}