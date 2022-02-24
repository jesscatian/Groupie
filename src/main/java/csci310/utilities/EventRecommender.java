package csci310.utilities;

import csci310.models.Event;
import csci310.models.EventResponse;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class EventRecommender {

    public static String GetRecommendedEvent(String proposalID)
    {

        ArrayList<String> receivers =  DatabaseManager.object().getProposalReciever(proposalID);
        ArrayList<Event> events = DatabaseManager.object().getProposalEvents(proposalID);

        EventResponse response;
        int availableCount;
        int totalExcitedness;
        
        int highestAvailableCount = 0;
        int highestExcitednessAtHighestAvailable = 0;
        String bestEvent = "-1";
        
        for (int i = 0; i < events.size(); i++)
        {
            availableCount = 0;
            totalExcitedness = 0;
            
            for (int j = 0; j < receivers.size(); j++)
            {
                response = DatabaseManager.object().getRespondedEvent(receivers.get(j), events.get(i).getEventID());
                availableCount += response.getAvailability();
                totalExcitedness += response.getExcitement();
            }

            if (availableCount > highestAvailableCount || (availableCount == highestAvailableCount && totalExcitedness > highestExcitednessAtHighestAvailable && availableCount > 0))
            {
                highestAvailableCount = availableCount;
                highestExcitednessAtHighestAvailable = totalExcitedness;
                bestEvent = events.get(i).getEventID();
            }
        }

        
        return bestEvent;
    }
}
