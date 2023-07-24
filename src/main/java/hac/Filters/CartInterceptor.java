package hac.Filters;

import hac.Beans.CartBean;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor to prevent access to /checkout when the cart is empty.
 */
@Component
public class CartInterceptor implements HandlerInterceptor {
    /**
     * The path to the cart page.
     */
    private static final String CART_PATH = "/cart";
    /**
     * NO BOOKS IN CART VALUE
     */
    private static final int NO_BOOKS_IN_CART = 0;

    private CartBean sessionBean;

    public CartInterceptor() {
    }

    /**
     * Constructs a CartInterceptor with the specified CartBean.
     *
     * @param sBean the CartBean instance
     */
    public CartInterceptor(CartBean sBean) {
        this.sessionBean = sBean;
    }

    /**
     * Pre-handle method to check if the cart is empty before allowing access to /checkout.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param handler  the handler for the request
     * @return {@code true} if the cart is not empty and further processing should continue,
     *         {@code false} otherwise, and redirects the user to /cart
     * @throws Exception in case of any error during handling the request
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (sessionBean != null && sessionBean.booksInCart() > NO_BOOKS_IN_CART) {
            return true; // Continue with the request processing
        } else {
            response.sendRedirect(CART_PATH);
            return false; // Stop further processing and redirect to /cart
        }
    }
}
