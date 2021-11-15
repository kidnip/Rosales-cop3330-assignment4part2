package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListEntry {
    private String listName;
    private ObservableList<EventEntry> EventArray = FXCollections.observableArrayList();

    public ListEntry(){
        this.listName = listName;
        this.EventArray = EventArray;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public ObservableList<EventEntry> getEventArray() {
        return EventArray;
    }

    public void addEvent(EventEntry eventEntry){
        EventArray.add(eventEntry);
    }

    //Will store list name and the array
    //of events
}
