//package pwr.zpi.organization;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.AsyncHandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Optional;
//
//@Component
//public class TenantRequestInterceptor implements AsyncHandlerInterceptor {
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        return Optional.of(request)
//                .map(tenant -> setTenantContext("public"))
//                .orElse(false);
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
//        TenantContext.clear();
//    }
//
//    private boolean setTenantContext(String tenant) {
//        TenantContext.setCurrentTenant(tenant);
//        return true;
//    }
//}