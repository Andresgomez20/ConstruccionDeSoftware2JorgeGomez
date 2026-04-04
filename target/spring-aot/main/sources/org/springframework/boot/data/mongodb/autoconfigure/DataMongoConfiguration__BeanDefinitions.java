package org.springframework.boot.data.mongodb.autoconfigure;

import java.lang.Class;
import java.util.Collections;
import java.util.List;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.data.domain.ManagedTypes;
import org.springframework.data.mongodb.MongoManagedTypes;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * Bean definitions for {@link DataMongoConfiguration}.
 */
@Generated
public class DataMongoConfiguration__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'org.springframework.boot.data.mongodb.autoconfigure.DataMongoConfiguration'.
   */
  private static BeanInstanceSupplier<DataMongoConfiguration> getDataMongoConfigurationInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<DataMongoConfiguration>forConstructor(DataMongoProperties.class)
            .withGenerator((registeredBean, args) -> new DataMongoConfiguration(args.get(0)));
  }

  /**
   * Get the bean definition for 'dataMongoConfiguration'.
   */
  public static BeanDefinition getDataMongoConfigurationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(DataMongoConfiguration.class);
    beanDefinition.setInstanceSupplier(getDataMongoConfigurationInstanceSupplier());
    return beanDefinition;
  }

  /**
   * Get the bean instance for 'mongoManagedTypes'.
   */
  private static InstanceSupplier<MongoManagedTypes> mongoManagedTypesInstance() {
    return (registeredBean ->  {
      List<Class<?>> types = Collections.emptyList();
      ManagedTypes managedTypes = ManagedTypes.fromIterable(types);
      return MongoManagedTypes.from(managedTypes);
    } );
  }

  /**
   * Get the bean definition for 'mongoManagedTypes'.
   */
  public static BeanDefinition getMongoManagedTypesBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(DataMongoConfiguration.class);
    beanDefinition.setTargetType(MongoManagedTypes.class);
    beanDefinition.setInstanceSupplier(DataMongoConfiguration__BeanDefinitions.mongoManagedTypesInstance());
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'mongoMappingContext'.
   */
  private static BeanInstanceSupplier<MongoMappingContext> getMongoMappingContextInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<MongoMappingContext>forFactoryMethod(DataMongoConfiguration.class, "mongoMappingContext", MongoCustomConversions.class, MongoManagedTypes.class)
            .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.data.mongodb.autoconfigure.DataMongoConfiguration", DataMongoConfiguration.class).mongoMappingContext(args.get(0), args.get(1)));
  }

  /**
   * Get the bean definition for 'mongoMappingContext'.
   */
  public static BeanDefinition getMongoMappingContextBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MongoMappingContext.class);
    beanDefinition.setFactoryBeanName("org.springframework.boot.data.mongodb.autoconfigure.DataMongoConfiguration");
    beanDefinition.setInstanceSupplier(getMongoMappingContextInstanceSupplier());
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'mongoCustomConversions'.
   */
  private static BeanInstanceSupplier<MongoCustomConversions> getMongoCustomConversionsInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<MongoCustomConversions>forFactoryMethod(DataMongoConfiguration.class, "mongoCustomConversions")
            .withGenerator((registeredBean) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.data.mongodb.autoconfigure.DataMongoConfiguration", DataMongoConfiguration.class).mongoCustomConversions());
  }

  /**
   * Get the bean definition for 'mongoCustomConversions'.
   */
  public static BeanDefinition getMongoCustomConversionsBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MongoCustomConversions.class);
    beanDefinition.setFactoryBeanName("org.springframework.boot.data.mongodb.autoconfigure.DataMongoConfiguration");
    beanDefinition.setInstanceSupplier(getMongoCustomConversionsInstanceSupplier());
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'mappingMongoConverter'.
   */
  private static BeanInstanceSupplier<MappingMongoConverter> getMappingMongoConverterInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<MappingMongoConverter>forFactoryMethod(DataMongoConfiguration.class, "mappingMongoConverter", ObjectProvider.class, MongoMappingContext.class, MongoCustomConversions.class)
            .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.data.mongodb.autoconfigure.DataMongoConfiguration", DataMongoConfiguration.class).mappingMongoConverter(args.get(0), args.get(1), args.get(2)));
  }

  /**
   * Get the bean definition for 'mappingMongoConverter'.
   */
  public static BeanDefinition getMappingMongoConverterBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MappingMongoConverter.class);
    beanDefinition.setFactoryBeanName("org.springframework.boot.data.mongodb.autoconfigure.DataMongoConfiguration");
    beanDefinition.setInstanceSupplier(getMappingMongoConverterInstanceSupplier());
    return beanDefinition;
  }
}
