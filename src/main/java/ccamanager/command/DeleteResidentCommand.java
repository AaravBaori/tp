package ccamanager.command;


import ccamanager.exceptions.ResidentNotFoundException;
import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.ui.Ui;

/**
 * Command to delete any exisiting CCA
 */
public class DeleteResidentCommand extends Command {

    private String matricNumber;

    public DeleteResidentCommand(String matricNumber){
        this.matricNumber=matricNumber;

    }

    @Override
    public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui) {
        try {
            String residentName = residentManager.nameGivenMatricNumber(matricNumber);
            residentManager.deleteResident(matricNumber);
            ui.showMessage("Resident deleted: " + residentName);
        } catch (ResidentNotFoundException e) {
            ui.showMessage(e.getMessage());
        }
    }


}
