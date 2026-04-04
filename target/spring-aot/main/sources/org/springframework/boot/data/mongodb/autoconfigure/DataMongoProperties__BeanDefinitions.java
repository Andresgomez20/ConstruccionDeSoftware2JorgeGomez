package org.springframework.boot.data.mongodb.autoconfigure;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link DataMongoProperties}.
 */
@Generated
public class DataMongoProperties__BeanDefinitions {
  /**
   * Get the bean definition for 'dataMongoProperties'.
   */
  public static BeanDefinition getDataMongoPropertiesBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(DataMongoProperties.class);
    beanDefinition.setInstanceSupplier(DataMongoProperties::new);
    return beanDefinition;
  }
}
