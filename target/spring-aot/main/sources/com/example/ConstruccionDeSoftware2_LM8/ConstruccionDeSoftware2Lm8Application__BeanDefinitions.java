package com.example.ConstruccionDeSoftware2_LM8;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ConstruccionDeSoftware2Lm8Application}.
 */
@Generated
public class ConstruccionDeSoftware2Lm8Application__BeanDefinitions {
  /**
   * Get the bean definition for 'construccionDeSoftware2Lm8Application'.
   */
  public static BeanDefinition getConstruccionDeSoftwareLmApplicationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ConstruccionDeSoftware2Lm8Application.class);
    beanDefinition.setInstanceSupplier(ConstruccionDeSoftware2Lm8Application::new);
    return beanDefinition;
  }
}
