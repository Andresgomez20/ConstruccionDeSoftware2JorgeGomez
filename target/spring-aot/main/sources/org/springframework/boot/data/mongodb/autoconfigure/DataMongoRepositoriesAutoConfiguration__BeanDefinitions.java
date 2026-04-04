package org.springframework.boot.data.mongodb.autoconfigure;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link DataMongoRepositoriesAutoConfiguration}.
 */
@Generated
public class DataMongoRepositoriesAutoConfiguration__BeanDefinitions {
  /**
   * Get the bean definition for 'dataMongoRepositoriesAutoConfiguration'.
   */
  public static BeanDefinition getDataMongoRepositoriesAutoConfigurationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(DataMongoRepositoriesAutoConfiguration.class);
    beanDefinition.setInstanceSupplier(DataMongoRepositoriesAutoConfiguration::new);
    return beanDefinition;
  }
}
