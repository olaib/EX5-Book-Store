package hac;

import hac.Beans.CartBean;
import hac.Filters.CartInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * middle ware configuration
 */
@Configuration
public class FiltersConfig implements WebMvcConfigurer {
    /**
     * interceptor paths
     */

    static final String[] INTERCEPTOR_PATHS = {"/checkout", "/order-confirm"};
    /**
     * cart bean
     */
    @Resource(name="sessionBeanCart")
    private final CartBean cartBean;
    /**
     * constructor
     * @param cartBean cart bean
     */

    public FiltersConfig(CartBean cartBean) {
        this.cartBean = cartBean;
    }
    /**
     * add cart interceptor
     * @param registry interceptor registry
     */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CartInterceptor(cartBean))
                .addPathPatterns(INTERCEPTOR_PATHS);
    }
}
