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

@Aspect
@Component
public class AuthAspect {

    private final HttpServletRequest request;

    public AuthAspect(HttpServletRequest request) {
        this.request = request;
    }

    @Around("@annotation(authorized)")
    public Object authenticate(ProceedingJoinPoint pjp, Authorized authorized) throws Throwable {

        HttpSession session = request.getSession();

        if(session.getAttribute("user") == null) {
            throw new NotLoggedInException("Must be logged in to perform this action");
        }

        return pjp.proceed(pjp.getArgs());
    }

    @Around("@annotation(manager)")
    public Object manager(ProceedingJoinPoint pjp, Manager manager) throws Throwable{
        HttpSession session = request.getSession();

        User loggedInUser = (User) session.getAttribute("user");
        String userRole = loggedInUser.getRole().toLowerCase();

        if(manager.value().equals(AuthRestriction.Manager) && !"manager".equals(userRole)) {
            throw new InvalidRoleException("Must be logged in as a" + userRole + "to perform this action");
        }

        return pjp.proceed(pjp.getArgs());
    }

    @Around("@annotation(admin)")
    public Object manager(ProceedingJoinPoint pjp, Admin admin) throws Throwable{
        HttpSession session = request.getSession();

        User loggedInUser = (User) session.getAttribute("user");
        String userRole = loggedInUser.getRole().toLowerCase();

        if(admin.value().equals(AuthRestriction.Admin) && !"admin".equals(userRole)) {
            throw new InvalidRoleException("Must be logged in as a" + userRole + "to perform this action");
        }

        return pjp.proceed(pjp.getArgs());
    }

}
