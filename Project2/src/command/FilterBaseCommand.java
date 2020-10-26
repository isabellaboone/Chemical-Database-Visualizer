package command;

import java.util.ArrayList;
import java.util.List;

import model.BaseDataMapper;
import model.Base;
import model.BaseDataMapperInterface;
import model.DomainModelException;
import reports.FilterBaseReport;
import reports.ReportObserverConnector;

public class FilterBaseCommand implements Command {
  String[] filter;

  public FilterBaseCommand(String filter) {    
    this.filter = filter.split("-");
    
  }

  @Override
  public void execute() {
    BaseDataMapperInterface baseMapper = new BaseDataMapper();
    List<Base> bases = new ArrayList<>();
    try {
      switch (filter[0]) {
      case ("1"): {
        System.out.println("name");
        String name = filter[1];
        
        bases = baseMapper.filterByNameLike(name);
      }
      case ("2"): {
        System.out.println("solute");
        
        bases = baseMapper.filterBySolute(Integer.parseInt(filter[1]));
      }
      case ("3"): {
        System.out.println("inventory");
        
        bases = baseMapper.filterByInventory(Integer.parseInt(filter[1]));
      }
      case("4"): {
        System.out.println("inventory range");
        
        bases = baseMapper.filterByInventoryBetween(Integer.parseInt(filter[1]), Integer.parseInt(filter[2]));
      }
      default: 
        System.out.println("getall");
        
        bases = baseMapper.getAll();

      }
    } catch (DomainModelException e) {
      e.printStackTrace();
    }
    
    ReportObserverConnector.getSingleton().sendReport(new FilterBaseReport(bases));
    
  }

}
