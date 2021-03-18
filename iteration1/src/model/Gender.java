package model;

import domainapp.basics.model.meta.DAttr;

/**
 * @overview Represents the gender of a person.
 *
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
