package com.doit.wheels.auth;

import com.doit.wheels.utils.UserRoleEnum;
import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.ui.UI;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * This demonstrates how you can control access to views.
 */
@Component
public class CustomViewAccessControl implements ViewAccessControl {

    @Override
    public boolean isAccessGranted(UI ui, String beanName) {
        Set<UserRoleEnum> roles = new HashSet<>();
        if (beanName.equals("homeView") ||
                beanName.equals("landingView") ||
                beanName.equals("userManagementView") ||
                beanName.equals("generalPropertiesView") ||
                beanName.equals("createEditCustomerView") ||
                beanName.equals("customersOrdersListView") ||
                beanName.equals("orderView") ||
                beanName.equals("orderDetailsView") ||
                beanName.equals("guidelinesView")
                ) {
            roles.add(UserRoleEnum.ADMIN);
            roles.add(UserRoleEnum.ENGINEER);
            return SecurityUtils.hasRole(roles);
        } else {
            roles.add(UserRoleEnum.DRIVER);
            return SecurityUtils.hasRole(roles);
        }
    }
}
