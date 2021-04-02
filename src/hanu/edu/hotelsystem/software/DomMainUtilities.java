package hanu.edu.hotelsystem.software;

import domainapp.basics.exceptions.DataSourceException;
import domainapp.software.SoftwareFactory;
import domainapp.softwareimpl.DomSoftware;
import hanu.edu.hotelsystem.services.Person.model.Employee;

/**
 * @overview 
 *
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 */
public class DomMainUtilities {
  
  public static void main(String[] args) {
    DomSoftware sw = SoftwareFactory.createDefaultDomSoftware();
    
    // this should be run subsequent times
    sw.init();
    
    try {
      // print materialised domain model
      printMaterialisedDomainModel(sw);
      
      // delete a domain class
//     deleteClass(sw, Employee.class);
      
      // delete the domain model
      deleteDomainModel(sw);
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @effects 
   * 
   * @version 
   * 
   */
  private static void printMaterialisedDomainModel(DomSoftware sw) {
    String modelName = sw.getDomainModelName(Employee.class);
    if (modelName != null) {
      sw.printMaterialisedDomainModel(modelName);
    }
  }

  /**
   * @effects 
   * 
   * @version 
   * 
   */
  private static void deleteDomainModel(DomSoftware sw) {
    String modelName = sw.getDomainModelName(Employee.class);
    if (modelName != null) {
      try {
        sw.deleteDomainModel(modelName);
      } catch (DataSourceException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * @effects 
   * 
   * @version 
   * @param sw 
   * 
   */
  private static void deleteClass(DomSoftware sw, Class c) throws DataSourceException {
    boolean isReg = sw.isRegistered(c);
    boolean isMat = sw.isMaterialised(c);
    System.out.printf("%s%n  isRegistered: %b%n  isMaterialised: %b%n", 
        c.getSimpleName(), isReg, isMat);
    if (isMat) {
      Class[] toDelete = {c};
      System.out.printf("...unregistering/deleting%n");
      sw.deleteDomainModel(toDelete);
      isReg = sw.isRegistered(c);
      isMat = sw.isMaterialised(c);
      System.out.printf("  isRegistered: %b%n  isMaterialised: %b%n", isReg, isMat);
    }    
  }
}
