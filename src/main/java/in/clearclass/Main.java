package in.clearclass;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import in.clearclass.entity.NameValue;
import in.clearclass.entity.MainForm;
import in.clearclass.entity.PartType;

public class Main {
   static Client client = ClientBuilder.newClient();
   static String url = "https://spicat.avx.com/Product/GetMlccSelectionFilter";
   static WebTarget webTarget = client.target(url)
                         .queryParam("SelectedGroup", "general")
                         .queryParam("SelectedTermination", "T");
   
   public static void main(String[] args) throws FileNotFoundException{
      List<String> diels = Arrays.asList("np0", "x7r", "x8r", "x8l", "x5r", "y5v");
      
      try(PrintStream ps = new PrintStream("avx_general.sql");
         PrintStream err = new PrintStream("error.log")){
         
         for(String diel : diels){
            NameValue[] caps = getCaps(diel);
            for(NameValue cap : caps){
               System.out.println(diel + " - " + cap);
               NameValue[] sizes = getSizes(diel, cap.getValue());
               for(NameValue size : sizes){
                  PartType[] partTypes = getPartTypes(diel, cap.getValue(), size.getValue());
                  for(PartType partType : partTypes){
                     if(partType.isCorrect())
                        ps.println(partType.toSql());
                     else err.println(partType);
                  }
               }
            }
         }
      }
   }
   
   // для заданного диэлектрика получаем все емкости
   static NameValue[] getCaps(String diel){
      return webTarget.queryParam("SelectedSeries", diel + "_general_mlcc")
                  .request(MediaType.APPLICATION_JSON)
                  .get(MainForm.class).check()
                  .getCaps();
   }
   
   // для каждой емкости - список типоразмеров
   static NameValue[] getSizes(String diel, String cap){
      return webTarget.queryParam("SelectedSeries", diel + "_general_mlcc")
                  .queryParam("SelectedCapacity", cap)
                  .request(MediaType.APPLICATION_JSON)
                  .get(MainForm.class).check()
                  .getSizes();
   }
   
   // для каждого типоразмера - список типономиналов
   static PartType[] getPartTypes(String diel, String cap, String size){
      return webTarget.queryParam("SelectedSeries", diel + "_general_mlcc")
                  .queryParam("SelectedCapacity", cap)
                  .queryParam("SelectedCaseSize", size)
                  .request(MediaType.APPLICATION_JSON)
                  .get(MainForm.class).check()
                  .getPartTypes();
   }
}