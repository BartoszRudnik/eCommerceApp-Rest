package ecommerce.restConfiguration;

import ecommerce.entities.Country;
import ecommerce.entities.Product;
import ecommerce.entities.ProductCategory;
import ecommerce.entities.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private final EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors){

        HttpMethod [] unsupportedMethods = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE};

        readOnly(Product.class, config, unsupportedMethods);
        readOnly(ProductCategory.class, config, unsupportedMethods);
        readOnly(Country.class, config, unsupportedMethods);
        readOnly(State.class, config, unsupportedMethods);

        exposeIds(config);

    }

    private void readOnly(Class theClass, RepositoryRestConfiguration config, HttpMethod[] unsupportedMethods) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedMethods))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedMethods));
    }

    private void exposeIds(RepositoryRestConfiguration config){

        Set<EntityType<?>> entityTypes = entityManager.getMetamodel().getEntities();

        List<Class> entityClasses = new ArrayList<>();

        for(EntityType entityType : entityTypes){

            entityClasses.add(entityType.getJavaType());

        }

        Class[] domainTypes = entityClasses.toArray(new Class[0]);

        config.exposeIdsFor(domainTypes);

    }

}
