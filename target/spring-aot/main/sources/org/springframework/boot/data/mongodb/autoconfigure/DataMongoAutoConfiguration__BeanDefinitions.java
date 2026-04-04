package org.springframework.boot.data.mongodb.autoconfigure;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link DataMongoAutoConfiguration}.
 */
@Generated
public class DataMongoAutoConfiguration__BeanDefinitions {
  /**
   * Get the bean definition for 'dataMongoAutoConfiguration'.
   */
  public static BeanDefinition getDataMongoAutoConfigurationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(DataMongoAutoConfiguration.class);
    beanDefinition.setInstanceSupplier(DataMongoAutoConfiguration::new);
    return beanDefinition;
  }
}
