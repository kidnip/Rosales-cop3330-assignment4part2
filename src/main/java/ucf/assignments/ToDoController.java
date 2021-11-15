package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Jeremy Rosales
 */
public class ToDoController extends Component implements Initializable{

    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private Label bottomLabel;
    @FXML
    private TextField nameField, descField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ListView<String> listView;
    @FXML
    private TableView<EventEntry> tableView;
    @FXML
    private TableColumn<EventEntry, String> eventDesc;
    @FXML
    private TableColumn<EventEntry, String> eventDate;
    @FXML
    private TableColumn<EventEntry, String> eventStatus;

    public ArrayList<ListEntry> userLists = new ArrayList<>();
    public ContextMenu popupMenu = new ContextMenu();
    public Menu showMenu = new Menu("View Options");
    public ContextMenu eventMenu = new ContextMenu();
    public Menu editMenu = new Menu("Edit Event");

    public int indexL, indexE;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        MenuItem showAll = new MenuItem("Show All");
        MenuItem showComplete = new MenuItem("Show Complete");
        MenuItem showIncomplete = new MenuItem("Show Incomplete");
        showMenu.getItems().addAll(showAll, showComplete, showIncomplete);

        MenuItem delete = new MenuItem("Delete List");
        MenuItem edit = new MenuItem("Edit Title");
        popupMenu.getItems().addAll(edit, delete, showMenu);

        MenuItem editDesc = new MenuItem("Edit Description");
        MenuItem editDate = new MenuItem("Edit Date");
        MenuItem markComplete = new MenuItem("Change Status");
        editMenu.getItems().addAll(editDesc, editDate, markComplete);

        MenuItem delete2 = new MenuItem("Delete Event");
        eventMenu.getItems().addAll(editMenu, delete2);

    }

    public void onRadioButtonClick(){
        //will check state of toggle group and changes the
        //label based on state returned
        ToggleButton hold = (ToggleButton) toggleGroup.getSelectedToggle();
        if(hold.getText().equals("Add New List")){
            System.out.println("\"" +hold.getText() +"\" selected");
            bottomLabel.setText("Enter only the list name and click Submit to add a new list");
        }
        if(hold.getText().equals("Add New Event")){
            System.out.println("\"" +hold.getText() +"\" selected");
            bottomLabel.setText("Enter the list name, description, and date, and click Submit to add an event to your list");
        }
    }

    public void onClickSubmit() {

        int flag = 0;
        if(bottomLabel.getText().equals("Enter the new date into the date picker and click Submit")){
            LocalDate date = datePicker.getValue();
            editDate(date.getYear() +"-" +date.getMonthValue() +"-" +date.getDayOfMonth());
            datePicker.getEditor().clear();
            return;
        }
        if (toggleGroup.getSelectedToggle() != null){
            ToggleButton hold = (ToggleButton) toggleGroup.getSelectedToggle();
            if (hold.getText().equals("Add New List")) {
                if(nameField.getText().equals("")) {
                    bottomLabel.setText("Please enter in a name for the list, then Submit");
                    System.out.println("missing element(s)");
                }else{
                    for(indexL = 0; indexL < userLists.size(); indexL++){
                        if(userLists.get(indexL).getListName().equals(nameField.getText())){
                            bottomLabel.setText("There already exists a list named \"" +nameField.getText() +"\"");
                            System.out.println("input matches existing list");
                            nameField.clear();
                            return;
                        }
                    }
                    ListEntry newList = new ListEntry();
                    newList.setListName(nameField.getText());
                    userLists.add(newList);
                    listView.getItems().add(newList.getListName());

                    bottomLabel.setText("List " + newList.getListName() + " has been created");
                    System.out.println("list created: " +newList.getListName());
                    nameField.clear();
                }
            }
            if(hold.getText().equals("Add New Event")){
                if(userLists.size() == 0){
                    bottomLabel.setText("There doesn't exist any lists");
                    System.out.println("no lists");
                }
                if(nameField.getText().equals("") || descField.getText().equals("") || datePicker.getValue() == null) {
                    bottomLabel.setText("Please enter in the list name, description, and/or date, then Submit");
                    System.out.println("missing element(s)");
                }
                else if(datePicker.getValue().isBefore(LocalDate.now())){
                    bottomLabel.setText("Due date cannot be before current date");
                    System.out.println("bad date input");
                    datePicker.getEditor().clear();
                }else {
                    for(indexL = 0; indexL < userLists.size(); indexL++){
                        if(userLists.get(indexL).getListName().equals(nameField.getText())){
                            flag = 1;
                            break;
                        }
                    }
                    if(flag == 0) {
                        bottomLabel.setText("There doesn't exist a list named \"" + nameField.getText() + "\"");
                        System.out.println("input does not match existing list");
                        return;
                    }
                    EventEntry newEvent = new EventEntry();
                    newEvent.setDescription(descField.getText());
                    LocalDate date = datePicker.getValue();
                    newEvent.setDate(date.getYear() +"-" +date.getMonthValue() +"-" +date.getDayOfMonth());
                    newEvent.setStatus("In Progress");

                    for (indexL = 0; indexL < userLists.size(); indexL++) {
                        if (userLists.get(indexL).getListName().equals(nameField.getText())) {
                            userLists.get(indexL).getEventArray().add(newEvent);
                            break;
                        }
                    }

                    tableView.setItems(userLists.get(indexL).getEventArray());
                    eventDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
                    eventDate.setCellValueFactory(new PropertyValueFactory<>("date"));
                    eventStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

                    nameField.clear();
                    descField.clear();
                    datePicker.getEditor().clear();
                    bottomLabel.setText("Event for list \"" +userLists.get(indexL).getListName() +"\" created");

                }
            }
        }
        else{
            bottomLabel.setText("Please select an option to start");
            System.out.println("no option selected");
        }
    }

    public void onLeftClickList(){
        String hold = listView.getSelectionModel().getSelectedItem();
        for (indexL = 0; indexL < userLists.size(); indexL++) {
            if (userLists.get(indexL).getListName().equals(hold)) {
                tableView.setItems(userLists.get(indexL).getEventArray());
                break;
            }
        }
        if(indexL == userLists.size())
            return;

        bottomLabel.setText("Showing all items for \"" +userLists.get(indexL).getListName() +"\"");
    }

    public void onRightClickList(MouseEvent e){
        String hold = listView.getSelectionModel().getSelectedItem();
        if (hold != null) {
            for (indexL = 0; indexL < userLists.size(); indexL++) {
                if (userLists.get(indexL).getListName().equals(hold)) {
                    if (bottomLabel.getText().equals("Showing completed items in \"" + userLists.get(indexL).getListName() + "\"")) {
                        showComplete();
                    } else if (bottomLabel.getText().equals("Showing in progress items in \"" + userLists.get(indexL).getListName() + "\"")) {
                        showIncomplete();
                    } else {
                        tableView.setItems(userLists.get(indexL).getEventArray());
                    }
                    break;
                }
            }
            popupMenu.show(listView, e.getScreenX(), e.getScreenY());
            popupMenu.setOnAction(e2 -> {
                if (((MenuItem) e2.getTarget()).getText().equals("Edit Title")) {
                    for (indexL = 0; indexL < userLists.size(); indexL++) {
                        if (userLists.get(indexL).getListName().equals(hold)) {
                            editTitle();
                            return;
                        }
                    }
                }
                if (((MenuItem) e2.getTarget()).getText().equals("Delete List")) {
                    for (indexL = 0; indexL < userLists.size(); indexL++) {
                        if (userLists.get(indexL).getListName().equals(hold)) {
                            deleteList();
                            return;
                        }
                    }
                } else {
                    popupMenu.hide();
                }
            });
            showMenu.setOnAction(e2 -> {
                for (indexL = 0; indexL < userLists.size(); indexL++) {
                    if (userLists.get(indexL).getListName().equals(hold)) {
                        if (((MenuItem) e2.getTarget()).getText().equals("Show All")) {
                            showAllItems();
                            System.out.println("Show all");
                            bottomLabel.setText("Showing all items in \"" + userLists.get(indexL).getListName() + "\"");
                        }
                        if (((MenuItem) e2.getTarget()).getText().equals("Show Complete")) {
                            showComplete();
                            System.out.println("Show complete");
                            bottomLabel.setText("Showing completed items in \"" + userLists.get(indexL).getListName() + "\"");
                        }
                        if (((MenuItem) e2.getTarget()).getText().equals("Show Incomplete")) {
                            showIncomplete();
                            System.out.println("Show incomplete");
                            bottomLabel.setText("Showing in progress items in \"" + userLists.get(indexL).getListName() + "\"");
                        } else {
                            showMenu.hide();
                            popupMenu.hide();
                        }
                        break;
                    }
                }
            });
        }
    }

    public void onClickList(){
        listView.setOnMouseClicked(e -> {
            if(e.getButton() == MouseButton.PRIMARY){
                if(popupMenu.isShowing()) {
                    popupMenu.hide();
                }
                onLeftClickList();

            }
            else if(e.getButton() == MouseButton.SECONDARY) {
                if(popupMenu.isShowing()) {
                    popupMenu.hide();
                }
                onRightClickList(e);
            }
        });
    }

    public void editTitle(){
        TextInputDialog dialog = new TextInputDialog("List " +(indexL+1));
        dialog.setTitle("Edit Title");
        dialog.setHeaderText("Enter list name:");
        Optional<String> result = dialog.showAndWait();

        String hold = userLists.get(indexL).getListName();
        result.ifPresent(change -> {
            userLists.get(indexL).setListName(change);

            listView.getItems().clear();
            for(ListEntry list : userLists)
                listView.getItems().add(list.getListName());

            bottomLabel.setText("List \"" +hold +"\" changed to \"" +userLists.get(indexL).getListName() +"\"");
            System.out.println("Title was changed");
        });
    }

    public void deleteList(){
        String hold = userLists.get(indexL).getListName();
        userLists.remove(indexL);
        listView.getItems().clear();
        tableView.getItems().clear();
        for(ListEntry list : userLists)
            listView.getItems().add(list.getListName());

        bottomLabel.setText("List \"" +hold +"\" has been removed");
        System.out.println(hold +" removed");
    }

    public void showAllItems(){
        if(userLists.get(indexL).getEventArray() != null) {
            tableView.setItems(userLists.get(indexL).getEventArray());
        }
    }

    public void showComplete(){
        ObservableList<EventEntry> complete = FXCollections.observableArrayList();
        for(int i = 0; i < userLists.get(indexL).getEventArray().size(); i++){
            if(userLists.get(indexL).getEventArray().get(i).getStatus().equals("Complete")){
                complete.add(userLists.get(indexL).getEventArray().get(i));
            }
        }
        tableView.setItems(complete);
    }

    public void showIncomplete(){
        ObservableList<EventEntry> incomplete = FXCollections.observableArrayList();
        for(int i = 0; i < userLists.get(indexL).getEventArray().size(); i++){
            if(!userLists.get(indexL).getEventArray().get(i).getStatus().equals("Complete")){
                incomplete.add(userLists.get(indexL).getEventArray().get(i));
            }
        }
        tableView.setItems(incomplete);
    }

    public void deleteEvent(){
        userLists.get(indexL).getEventArray().remove(indexE);
        tableView.refresh();
        bottomLabel.setText("An event in \"" +userLists.get(indexL).getListName() +"\" was deleted");
        System.out.println("event deleted");
    }

    public void editDescription(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Edit Event Description");
        dialog.setHeaderText("Enter new description");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(change -> {
            userLists.get(indexL).getEventArray().get(indexE).setDescription(change);

            tableView.refresh();
            bottomLabel.setText("Event description in \"" +userLists.get(indexL).getListName() +"\" was changed");
            System.out.println("description was changed");
        });
    }

    public void dateAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Event Date");
        alert.setHeaderText("Enter the new date into the date picker and click Submit");
        alert.showAndWait();
        bottomLabel.setText("Enter the new date into the date picker and click Submit");
    }

    public void editDate(String date){
        userLists.get(indexL).getEventArray().get(indexE).setDate(date);
        tableView.refresh();
        bottomLabel.setText("Event date in \"" +userLists.get(indexL).getListName() +"\" was changed");
        System.out.println("date was changed");
    }

    public void markComplete(){
        if(userLists.get(indexL).getEventArray().get(indexE).getStatus().equals("In Progress")){
            userLists.get(indexL).getEventArray().get(indexE).setStatus("Complete");
            tableView.refresh();
            bottomLabel.setText("Event status in \"" +userLists.get(indexL).getListName() +"\" was changed");
            System.out.println("status was changed");
        }
        else if(userLists.get(indexL).getEventArray().get(indexE).getStatus().equals("Complete")){
            userLists.get(indexL).getEventArray().get(indexE).setStatus("In Progress");
            tableView.refresh();
            bottomLabel.setText("Event status in \"" +userLists.get(indexL).getListName() +"\" was changed");
            System.out.println("status was changed");
        }
    }

    public void onRightClickTable(MouseEvent e) {

        eventMenu.show(tableView, e.getScreenX(), e.getScreenY());
        editMenu.setOnAction(e2 -> {
            int flag = 0;
            String holdDesc = tableView.getSelectionModel().getSelectedItem().getDescription();
            String holdDate = tableView.getSelectionModel().getSelectedItem().getDate();
            String holdStatus = tableView.getSelectionModel().getSelectedItem().getStatus();
            ObservableList<EventEntry> holdArray;

            for (indexL = 0; indexL < userLists.size(); indexL++) {
                holdArray = userLists.get(indexL).getEventArray();
                for (indexE = 0; indexE < holdArray.size(); indexE++) {
                    if (holdArray.get(indexE).getDescription().equals(holdDesc) && holdArray.get(indexE).getDate().equals(holdDate) && holdArray.get(indexE).getStatus().equals(holdStatus)) {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 1) {
                    break;
                }
            }
            if(flag == 1) {
                if (((MenuItem) e2.getTarget()).getText().equals("Edit Description")) {
                    editDescription();
                }
                if (((MenuItem) e2.getTarget()).getText().equals("Edit Date")) {
                    dateAlert();
                }
                if (((MenuItem) e2.getTarget()).getText().equals("Change Status")) {
                    markComplete();
                }
            }
        });
        eventMenu.setOnAction(e2 -> {
            int flag = 0;
            String holdDesc = tableView.getSelectionModel().getSelectedItem().getDescription();
            String holdDate = tableView.getSelectionModel().getSelectedItem().getDate();
            String holdStatus = tableView.getSelectionModel().getSelectedItem().getStatus();
            ObservableList<EventEntry> holdArray;

            for (indexL = 0; indexL < userLists.size(); indexL++) {
                holdArray = userLists.get(indexL).getEventArray();
                for (indexE = 0; indexE < holdArray.size(); indexE++) {
                    if (holdArray.get(indexE).getDescription().equals(holdDesc) && holdArray.get(indexE).getDate().equals(holdDate) && holdArray.get(indexE).getStatus().equals(holdStatus)) {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 1) {
                    break;
                }
            }
            if(flag == 1) {
                if (((MenuItem) e2.getTarget()).getText().equals("Delete Event")) {
                    deleteEvent();
                }
            }
        });
    }

    public void onClickTable(){
        tableView.setOnMouseClicked(e -> {
            if(e.getButton() == MouseButton.SECONDARY && !tableView.getSelectionModel().isEmpty()){
                if(eventMenu.isShowing()) {
                    eventMenu.hide();
                }
                onRightClickTable(e);
            }
            else if(e.getButton() == MouseButton.PRIMARY && eventMenu.isShowing()){
                eventMenu.hide();
            }
        });
    }

    public void saveList(){
        // open a pop-up view that asks user for the
        // name of the list that is wanted to be saved
        // and a destination folder

        // call createFile(), passing list name and file
        // path
        if(userLists.size() == 0){
            bottomLabel.setText("There are no lists to save. Please create one to save");
            System.out.println("no lists");
            return;
        }
        else if(listView.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Save List Info");
            alert.setHeaderText("Please select a list in the list view first, and then click Save List");
            alert.showAndWait();
            return;
        }
        System.out.println("saving list...");

        GridPane gridPane = new GridPane();
        TextField text = new TextField();
        text.setMinWidth(300);
        text.setPromptText("Enter directory/location");
        Button fileButton = new Button("Search");
        fileButton.setOnAction(e -> {
            System.out.println("searching file...");
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = fc.showSaveDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                File file = fc.getSelectedFile();
                text.setText(file.getAbsolutePath());
            }
        });
        gridPane.add(text, 0, 0);
        gridPane.add(fileButton, 1, 0);


        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Save List");
        dialog.setHeaderText("Please enter in a directory/folder location to save to");
        ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(ok, cancel);
        dialog.getDialogPane().setContent(gridPane);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == ok){
                return text.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(input -> {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(input +"\\todolist.txt"));
                indexL = listView.getSelectionModel().getSelectedIndex();
                writer.write("List " + (indexL + 1) +":\n");
                writer.write(userLists.get(indexL).getListName() +"\n");
                if(userLists.get(indexL).getEventArray() != null && userLists.get(indexL).getEventArray().size() > 0) {
                    for (indexE = 0; indexE < userLists.get(indexL).getEventArray().size(); indexE++) {
                        writer.write(userLists.get(indexL).getEventArray().get(indexE).getDescription() +" -- "
                                +userLists.get(indexL).getEventArray().get(indexE).getDate() +" -- "
                                +userLists.get(indexL).getEventArray().get(indexE).getStatus() +"\n");
                    }
                    writer.write("-end-");
                }
                writer.close();
                bottomLabel.setText("File for \"" +userLists.get(indexL).getListName() +"\" created in " +input);
                System.out.println("stored in " +input);
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Directory/location was not accessible");
                alert.showAndWait();
            }
        });
    }

    public void saveAll(){
        // open same pop-up and take just destination
        // path for the file

        // call createFile(), passing just file path
        // and listName as "ALL"
        if(userLists.size() == 0){
            bottomLabel.setText("There are no lists to save. Please create one to save");
            System.out.println("no lists");
            return;
        }
        System.out.println("saving all lists...");

        GridPane gridPane = new GridPane();
        TextField text = new TextField();
        text.setMinWidth(300);
        text.setPromptText("Enter directory/location");
        Button fileButton = new Button("Search");
        fileButton.setOnAction(e -> {
            System.out.println("searching file...");
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = fc.showSaveDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                File file = fc.getSelectedFile();
                text.setText(file.getAbsolutePath());
            }
        });
        gridPane.add(text, 0, 0);
        gridPane.add(fileButton, 1, 0);


        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Save All");
        dialog.setHeaderText("Please enter in a directory/folder location to save to");
        ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(ok, cancel);
        dialog.getDialogPane().setContent(gridPane);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == ok){
                return text.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(input -> {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(input +"\\todolist.txt"));
                for(indexL = 0; indexL < userLists.size(); indexL++) {
                    writer.write("List " + (indexL + 1) + ":\n");
                    writer.write(userLists.get(indexL).getListName() + "\n");
                    if (userLists.get(indexL).getEventArray() != null && userLists.get(indexL).getEventArray().size() > 0) {
                        for (indexE = 0; indexE < userLists.get(indexL).getEventArray().size(); indexE++) {
                            writer.write(userLists.get(indexL).getEventArray().get(indexE).getDescription() + " -- "
                                    + userLists.get(indexL).getEventArray().get(indexE).getDate() + " -- "
                                    + userLists.get(indexL).getEventArray().get(indexE).getStatus() + "\n");
                        }
                    }
                    writer.write("-end-");
                }
                writer.close();
                bottomLabel.setText("All files stored in " +input);
                System.out.println("stored in " +input);
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Directory/location was not accessible");
                alert.showAndWait();
            }
        });

    }

    public void loadLists() {
        // open pop-up and take file destination
        // path for file to load

        // call createLists(), passing file path
        System.out.println("loading list(s)...");

        GridPane gridPane = new GridPane();
        TextField text = new TextField();
        text.setMinWidth(300);
        text.setPromptText("Enter file directory/location");
        Button fileButton = new Button("Search");
        fileButton.setOnAction(e -> {
            System.out.println("searching file...");
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnVal = fc.showSaveDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                File file = fc.getSelectedFile();
                text.setText(file.getAbsolutePath());
            }
        });
        gridPane.add(text, 0, 0);
        gridPane.add(fileButton, 1, 0);


        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Load File(s)");
        dialog.setHeaderText("Please enter in a file directory/location to load from");
        ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(ok, cancel);
        dialog.getDialogPane().setContent(gridPane);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == ok){
                return text.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(input -> {
            File file = new File(input);
            try {
                if(file.length() == 0){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("File in directory is empty");
                    alert.showAndWait();
                }
                Scanner reader = new Scanner(file);
                String word = reader.next();
                while(reader.hasNextLine()){
                    if(word.equals("-end-")){
                        return;
                    }
                    if(word.equals("List")){
                        reader.nextLine();
                        word = reader.nextLine();
                    }
                    else {  //list name found
                        ListEntry list = new ListEntry();
                        list.setListName(word);
                        userLists.add(list);
                        listView.getItems().add(list.getListName());
                        word = reader.next();  //reading for description
                        while(!word.equals("List") && !word.equals("-end-")){
                            EventEntry event = new EventEntry();
                            StringBuilder descHold = new StringBuilder();
                            descHold.append(word);
                            word = reader.next();
                            while(!word.equals("--")) {
                                descHold.append(" ");
                                descHold.append(word);
                                word = reader.next();
                            }
                            String dateHold = reader.next();  //read date
                            reader.next();  //read --
                            String statusHold = reader.next(); //read status
                            if(statusHold.equals("In")){
                                statusHold = statusHold +" " +reader.next();  //read "In Progress"
                            }
                            event.setDescription(descHold.toString());  //
                            event.setDate(dateHold);                    //event created
                            event.setStatus(statusHold);                //
                            list.addEvent(event);
                            word = reader.next();
                        }
                        System.out.println(userLists.get(0).getListName());
                        System.out.println(userLists.get(0).getEventArray().get(0).getDescription());
                        System.out.println(userLists.get(0).getEventArray().get(0).getDate());
                        System.out.println(userLists.get(0).getEventArray().get(0).getStatus());
                    }
                }
                tableView.setItems(userLists.get(0).getEventArray());
                eventDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
                eventDate.setCellValueFactory(new PropertyValueFactory<>("date"));
                eventStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

                bottomLabel.setText("Lists loaded from file");
                System.out.println("loaded files");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Directory/location was not accessible");
                alert.showAndWait();
                System.out.println("folder not found");
            }
        });
    }

    public void help(){
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("User Guide");
        info.setHeaderText("Please refer to the \"readme.md\" file for help");
        info.showAndWait();
        bottomLabel.setText("Look down here for instructions, too!");
    }

    /*
    6.  To add a new todo list, user will click on the radio button
        that says "Add New List", and enter just the list name.
        On button click, the label at the bottom of the screen will
        change according to the button selected using an if statement
        and provide the user with instructions. After pressing the
        "Submit" button, an on button click method with an if statement
        comparing the state of the radio buttons to then take the value
        in the List Name text field and add it into a list, while
        showing it to the list view.

    7.  To remove an existing todo list, the user will have to right-click
        on a list, which will show a menu with a button that will say
        "Delete list". After an on button click, the list element will be
        removed from the list and list view.

    8.  Just like #7, user will right-click the list and the menu will
        also display a button called "Edit title", where the user can
        then type in a new title name and it will be saved and displayed
        in the list view.

    9.  To add a new item to a todo list, user will click on the button
        that says "Add New Event", and enter the list name the event
        will go into, event description, and due date. On button click
        of the radio button, the label at the button will change like in
        #6. After pressing the "Submit" button, an on button click method
        with an if statement will compare the state of the button group
        and find the list the event will go into. Each element will then
        be saved in another list for the list entered and be displayed in
        list view.

    10. To remove an item from a todo list, just like #7, the user will
        right-click the item in the list view and select a button from
        and menu that says "Remove item". An on button click method will
        delete the item from the list's list and remove it from the list
        view.

    11. To edit the description of an item in an existing todo list, the
        user will right-click the item in the list view, where a menu will
        appear and show an option to edit the description. It will then be
        saved to the event entry in the respective list

    12. To edit the due date, it will be the same as #11, but user will
        click an option that says "Edit Date". User will enter a new date
        and will be saved

    13. To mark an item in a todo list as complete, just like in #11 and
        #12, user will click an option that say "Mark as Complete". This
        will then update the completed value in the event entry and display
        an "*" or change the text color (TBD)

    14. The user will be able to see every item in a list by default once
        they click on the list (On click method). However, right-clicking
        the list of the event with show another menu within the other menu
        titled "View Options", with one of the choices being "Show All"

    15. To show only incomplete items, in the menu mentioned in #14, there
        will be an option titled "Show Incomplete". This will update the
        list view to only include those not marked as complete, which should
        be by default once entered. This will be done by hiding all events
        in the list view using a loop to find all events marked as incomplete

    16. Like #14 and #15, there will be an option that displays "Show Complete",
        This will update the list view to show only events marked as complete
        via user input. This will be done using a loop to find and hide all
        elements marked as complete and update the list view according

    17. To save all the items in a single todo list, there is a menu bar at
        the top of the application that a user can select named "Save" which
        will allow the user to click an option named "Save Items". This
        will trigger a pop-up that will prompt the user for the name of the
        list with items and a destination to save the data to. It will then
        create a text file with the name of the list at the top of the file
        and each event afterwards, separated with newlines.

    18. To save all the lists, like #17, there will be an option titled
        "Save All". A pop-up will appear prompting the user for a directory
        to save the data to. Each list will alternate by list name and events,
        in a way that distinguishes the lists from each other.

    19. To load a single todo list, there is a menu bar at the top of the
        application that a user can select called "Load" which will allow the
        user to click an option called "Load List". This will trigger a pop-up
        screen that asks for the file directory for the file containing the
        list data. It will then scan the file, with list name first and events
        after. It will then enter the list into the list array and display it
        to the list view.

    20. To load multiple lists, like #19, there will be an option titled
        "Load Lists" which will a trigger a pop up that will prompt the
        user for a file directory. Then the file will be scanned, reading
        the distinguishable list names and events and inserting them into
        the list array and displaying each lists and events in the list views







     */
}
