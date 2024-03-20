package com.ledgerlogic.advice;

import com.ledgerlogic.annotations.Admin;
import com.ledgerlogic.annotations.Manager;
import com.ledgerlogic.annotations.AuthRestriction;
import com.ledgerlogic.annotations.Authorized;

import com.ledgerlogic.exceptions.InvalidRoleException;
import com.ledgerlogic.exceptions.NotLoggedInException;
import com.ledgerlogic.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.management.relation.Role;

@Aspect
@Component
public class AuthAspect {

    private final HttpServletRequest request;

    public AuthAspect(HttpServletRequest request) {
        this.request = request;
    }

    // This advice will execute around any method annotated with @Authorized
    // If the user is not logged in, an exception will be thrown and handled
    // Otherwise, the original method will be invoked as normal.
    // In order to expand upon this, you just need to add additional
    // values to the AuthRestriction enum.
    // Examples might be "Manager" or "Customer" along with suitable Role
    // values in the User class.
    // Then this method can be expanded to throw exceptions if the user does
    // not have the matching role.
    // Example:
    // User loggedInUser = (User) session.getAttribute("user");
    // Role userRole = loggedInUser.getRole();
    // if(authorized.value().equals(AuthRestriction.Manager) && !Role.Manager.equals(userRole)) {
    //     throw new InvalidRoleException("Must be logged in as a Manager to perform this action");
    // }
    // Then the RestExceptionHandler class can be expanded to include
    // @ExceptionHandler(InvalidRoleException.class)
    // which should return a 403 Forbidden such as:
    // String errorMessage = "Missing required role to perform this action";
    // return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessage);
    @Around("@annotation(authorized)")
    public Object authenticate(ProceedingJoinPoint pjp, Authorized authorized) throws Throwable {

        HttpSession session = request.getSession();

        if(session.getAttribute("user") == null) {
            throw new NotLoggedInException("Must be logged in to perform this action");
        }

        return pjp.proceed(pjp.getArgs());
    }

    @Around("@annotation(manager)")
    public Object manager(ProceedingJoinPoint pjp, Manager manager) throws Throwable {
        HttpSession session = request.getSession();

        User loggedInUser = (User) session.getAttribute("user");

        String userRole = loggedInUser.getRole().toLowerCase();

        if (manager.value().equals(AuthRestriction.Manager) && !"manager".equals(userRole)) {
            throw new InvalidRoleException("Must be logged in as a " + userRole + " to perform this action");
        }

        return pjp.proceed(pjp.getArgs());
    }


    @Around("@annotation(admin)")
    public Object manager(ProceedingJoinPoint pjp, Admin admin) throws Throwable{
        HttpSession session = request.getSession();

        System.out.println("- Session from AuthAspect: " + session);
        User loggedInUser = (User) session.getAttribute("user");
        System.out.println(loggedInUser);


        String userRole = loggedInUser.getRole().toLowerCase();

        if(admin.value().equals(AuthRestriction.Admin) && !"admin".equals(userRole)) {
            throw new InvalidRoleException("Must be logged in as a" + userRole + "to perform this action");
        }

        return pjp.proceed(pjp.getArgs());
    }

}
