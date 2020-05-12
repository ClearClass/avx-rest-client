package in.clearclass.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MainForm {
   @JsonProperty("SelectedSeries")
   private String selectedSeries;
// @JsonProperty("SelectedGroup")
// private String selectedGroup;
   @JsonProperty("SelectedCapacity")
   private String selectedCapacity;
// @JsonProperty("SelectedVoltage")
// private String selectedVoltage;
   @JsonProperty("SelectedCaseSize")
   private String selectedCaseSize;
// @JsonProperty("SelectedTolerance")
// private String selectedTolerance;
// @JsonProperty("SelectedTermination")
// private String selectedTermination;
   @JsonProperty("Capacity")
   private NameValue[] caps;
// @JsonProperty("Voltage")
// private NameValue[] voltage;
   @JsonProperty("CaseSize")
   private NameValue[] sizes;
// @JsonProperty("Tolerance")
// private NameValue[] tolerance;
// @JsonProperty("Termination")
// private NameValue[] termination;
   @JsonProperty("PartTypeComplete")
   private Boolean partTypeComplete;
   @JsonProperty("SuggestedPartTypes")
   private PartType[] partTypes;
   @JsonProperty("RequestFailed")
   private Boolean requestFailed;
   @JsonProperty("Message")
   private String message;
// @JsonProperty("RequestedPartNumber")
// private String requestedPartNumber;
   
   public NameValue[] getCaps() {
      return caps;
   }
   
   public NameValue[] getSizes() {
      return sizes;
   }
   
   public PartType[] getPartTypes() {
      return partTypes;
   }
   
   public MainForm check() {
      boolean notComplete = selectedSeries!=null && 
                            selectedCapacity!=null && 
                            selectedCaseSize!=null && !partTypeComplete;
      
      if(notComplete || requestFailed)
         throw new RuntimeException(message);
      return this;
   }
}