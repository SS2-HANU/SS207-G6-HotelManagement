package hanu.edu.hotelsystem.services.person.model;

import domainapp.basics.model.meta.DAttr;

/**
 * @overview Represents gender of a person

 * @author nguyen minh chau
 * @version 1.0
 */
public enum Gender {
    Male,
    Female,
    //Others
    ;

    @DAttr(name="name", type= DAttr.Type.String, id=true, length=10)
    public String getName() {
        return name();
    }

}
