package ccamanager.command;

import ccamanager.manager.CcaManager;
import ccamanager.manager.EventManager;
import ccamanager.manager.ResidentManager;
import ccamanager.model.Resident;
import ccamanager.ui.Ui;

import java.util.ArrayList;

public class ViewPointsCommand extends Command{
    public ViewPointsCommand(){
    }
    public void execute(CcaManager ccaManager, ResidentManager residentManager, EventManager eventManager, Ui ui){
        ArrayList<Resident> residentList = residentManager.getResidentList();
        ui.showCcaPoints(residentList);
    }
}

