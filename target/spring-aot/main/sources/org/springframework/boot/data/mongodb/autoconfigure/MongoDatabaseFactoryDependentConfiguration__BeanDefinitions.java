package org.springframework.boot.data.mongodb.autoconfigure;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**
 * Bean definitions for {@link MongoDatabaseFactoryDependentConfiguration}.
 */
@Generated
public class MongoDatabaseFactoryDependentConfiguration__BeanDefinitions {
  /**
   * Get the bean definition for 'mongoDatabaseFactoryDependentConfiguration'.
   */
  public static BeanDefinition getMongoDatabaseFactoryDependentConfigurationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MongoDatabaseFactoryDependentConfiguration.class);
    beanDefinition.setInstanceSupplier(MongoDatabaseFactoryDependentConfiguration::new);
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'mongoTemplate'.
   */
  private static BeanInstanceSupplier<MongoTemplate> getMongoTemplateInstanceSupplier() {
    return BeanInstanceSupplier.<MongoTemplate>forFactoryMethod(MongoDatabaseFactoryDependentConfiguration.class, "mongoTemplate", MongoDatabaseFactory.class, MongoConverter.class)
            .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.data.mongodb.autoconfigure.MongoDatabaseFactoryDependentConfiguration", MongoDatabaseFactoryDependentConfiguration.class).mongoTemplate(args.get(0), args.get(1)));
  }

  /**
   * Get the bean definition for 'mongoTemplate'.
   */
  public static BeanDefinition getMongoTemplateBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MongoTemplate.class);
    beanDefinition.setFactoryBeanName("org.springframework.boot.data.mongodb.autoconfigure.MongoDatabaseFactoryDependentConfiguration");
    beanDefinition.setInstanceSupplier(getMongoTemplateInstanceSupplier());
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'gridFsTemplate'.
   */
  private static BeanInstanceSupplier<GridFsTemplate> getGridFsTemplateInstanceSupplier() {
    return BeanInstanceSupplier.<GridFsTemplate>forFactoryMethod(MongoDatabaseFactoryDependentConfiguration.class, "gridFsTemplate", DataMongoProperties.class, MongoDatabaseFactory.class, MongoTemplate.class)
            .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.data.mongodb.autoconfigure.MongoDatabaseFactoryDependentConfiguration", MongoDatabaseFactoryDependentConfiguration.class).gridFsTemplate(args.get(0), args.get(1), args.get(2)));
  }

  /**
   * Get the bean definition for 'gridFsTemplate'.
   */
  public static BeanDefinition getGridFsTemplateBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(GridFsTemplate.class);
    beanDefinition.setFactoryBeanName("org.springframework.boot.data.mongodb.autoconfigure.MongoDatabaseFactoryDependentConfiguration");
    beanDefinition.setInstanceSupplier(getGridFsTemplateInstanceSupplier());
    return beanDefinition;
  }
}
